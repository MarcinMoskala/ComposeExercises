#!/usr/bin/env bash
# Records a Perfetto trace of MarketDashboardScreen and saves it in traces/.
#
# Usage:
#   ./gradlew :app:installBenchmark          # release-like build, profileable
#   scripts/record-perfetto-trace.sh idle    # just look at the screen
#   scripts/record-perfetto-trace.sh scroll  # scroll the list up and down while tracing
#
# Open the result in https://ui.perfetto.dev (or Android Studio: Profiler -> Load from file).
set -euo pipefail

MODE="${1:-idle}"
PACKAGE="com.marcinmoskala.composeexercises"
ACTIVITY="$PACKAGE/.exercises.advanced.MarketDashboardActivity"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
OUT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)/traces"
DEVICE_CONFIG="/data/local/tmp/market-dashboard-perfetto.txtpb"
DEVICE_TRACE="/data/misc/perfetto-traces/market-dashboard.perfetto-trace"
LOCAL_TRACE="$OUT_DIR/market-dashboard-$MODE-$(date +%Y%m%d-%H%M%S).perfetto-trace"

ADB="${ADB:-adb}"
if ! command -v "$ADB" >/dev/null && [ -n "${ANDROID_HOME:-}" ]; then
  ADB="$ANDROID_HOME/platform-tools/adb"
fi

case "$MODE" in
  idle|scroll) ;;
  *) echo "Unknown mode '$MODE', use 'idle' or 'scroll'" >&2; exit 1 ;;
esac

echo "Starting $ACTIVITY"
"$ADB" shell am force-stop "$PACKAGE"
"$ADB" shell am start -W -n "$ACTIVITY" >/dev/null
sleep 2

echo "Enabling composition tracing"
"$ADB" shell am broadcast \
  -a androidx.tracing.perfetto.action.ENABLE_TRACING \
  -n "$PACKAGE/androidx.tracing.perfetto.TracingReceiver" | tail -1

echo "Recording trace ($MODE, 10 s)"
"$ADB" push "$SCRIPT_DIR/perfetto-config.txtpb" "$DEVICE_CONFIG" >/dev/null 2>&1
# perfetto cannot read files from /data/local/tmp directly on every Android version, so the config goes through stdin
"$ADB" shell "cat $DEVICE_CONFIG | perfetto --txt -c - -o $DEVICE_TRACE" >/dev/null 2>&1 &
PERFETTO_PID=$!
sleep 1

if [ "$MODE" = "scroll" ]; then
  read -r WIDTH HEIGHT < <("$ADB" shell wm size | sed -E 's/.*: ([0-9]+)x([0-9]+).*/\1 \2/')
  X=$((WIDTH / 2))
  for _ in 1 2 3; do
    "$ADB" shell input swipe "$X" $((HEIGHT * 8 / 10)) "$X" $((HEIGHT * 4 / 10)) 400
    sleep 0.5
  done
  for _ in 1 2 3; do
    "$ADB" shell input swipe "$X" $((HEIGHT * 4 / 10)) "$X" $((HEIGHT * 8 / 10)) 400
    sleep 0.5
  done
fi

wait "$PERFETTO_PID"
mkdir -p "$OUT_DIR"
"$ADB" pull "$DEVICE_TRACE" "$LOCAL_TRACE" >/dev/null 2>&1
echo "Trace saved to $LOCAL_TRACE"
echo "Open it in https://ui.perfetto.dev"
