package com.marcinmoskala.composeexercises.accessibility

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.AccessibilityAction
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.marcinmoskala.composeexercises.R
import com.marcinmoskala.composeexercises.sample.accessibility.SemanticsExerciseMessage
import com.marcinmoskala.composeexercises.sample.accessibility.SemanticsExerciseScreen
import com.marcinmoskala.composeexercises.sample.theming.AppTheme
import org.junit.Rule
import org.junit.Test

class SemanticsExerciseTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun initialMessages(): List<SemanticsExerciseMessage> = listOf(
        SemanticsExerciseMessage(1, "Alice", "Trip photos", "Check out these pictures from the mountains.", R.drawable.avatar, unread = true, starred = false),
        SemanticsExerciseMessage(2, "Bob", "Invoice #3411", "Please find attached the invoice for November.", R.drawable.avatar, unread = false, starred = true),
        SemanticsExerciseMessage(3, "Carol", "Team meeting", "Meeting rescheduled to 3pm tomorrow.", R.drawable.avatar, unread = true, starred = false),
        SemanticsExerciseMessage(4, "Dave", "Lunch?", "Are you up for lunch this Friday?", R.drawable.avatar, unread = false, starred = false)
    )

    @Composable
    private fun SemanticsExerciseTestHost(showErrorInitial: Boolean = false) {
        var showError by remember { mutableStateOf(showErrorInitial) }
        var downloading by remember { mutableStateOf(true) }
        var progress by remember { mutableStateOf(0.65f) }
        var messages by remember { mutableStateOf(initialMessages()) }

        SemanticsExerciseScreen(
            showError = showError,
            downloading = downloading,
            progress = progress,
            messages = messages,
            onDismissError = { showError = false },
            onToggleStar = { id ->
                messages = messages.map { if (it.id == id) it.copy(starred = !it.starred) else it }
            },
            onOpen = { /* no-op in tests */ },
            onArchive = { id -> messages = messages.filterNot { it.id == id } },
            onMarkReadToggle = { id -> messages = messages.map { if (it.id == id) it.copy(unread = !it.unread) else it } },
        )
    }

    private fun setContent(showError: Boolean = false) {
        composeTestRule.setContent {
            AppTheme(darkTheme = false) {
                SemanticsExerciseTestHost(showErrorInitial = showError)
            }
        }
    }

    @Test
    fun topBarTitle_hasPaneTitle_andHeading() {
        setContent()

        val paneTitleMatcher = hasPaneTitleWithGuidance("Inbox")
        val headingMatcher = isHeadingWithGuidance("Add heading() to the top bar title")

        composeTestRule.onNodeWithText("Inbox")
            .assert(paneTitleMatcher)
            .assert(headingMatcher)
    }

    @Test
    fun newMessagesText_isPoliteLiveRegion() {
        setContent()

        // Initial data has 2 unread messages
        val politeLiveRegion = hasLiveRegionWithGuidance(
            LiveRegionMode.Polite,
            "Add liveRegion = LiveRegionMode.Polite to the 'new messages' Text semantics"
        )

        composeTestRule.onNodeWithText("2 new messages")
            .assert(politeLiveRegion)
    }

    @Test
    fun messagesHeader_hasHeading() {
        setContent()

        val headingMatcher = isHeadingWithGuidance("Add heading() to the section header")
        composeTestRule.onNodeWithText("Messages")
            .assert(headingMatcher)
    }

    @Test
    fun progressIndicator_exposes_progressBarRangeInfo() {
        setContent()

        // Find the progress indicator by its ProgressBarRangeInfo semantics
        val expected = ProgressBarRangeInfo(current = 0.65f, range = 0f..1f, steps = 0)
        val matcher = hasProgressBarRangeInfoWithGuidance(
            expected,
            hint = "Expose progressBarRangeInfo = ProgressBarRangeInfo(progress, 0f..1f, 0) on the progress bar semantics",
        )

        composeTestRule.onNode(matcher)
            .assert(matcher)
    }

    @Test
    fun firstMessageRow_hasMergedContent_andCustomActions_andClickActions() {
        setContent()

        // Row merges descendants and exposes a composed contentDescription for the first message (Alice)
        val expectedAlice = expectedRowDescription(
            sender = "Alice",
            subject = "Trip photos",
            unread = true,
            starred = false
        )
        val rowMatcher = hasExactContentDescriptionWithGuidance(expectedAlice)
        val rowNode = composeTestRule.onNodeWithContentDescription(expectedAlice)
        rowNode.assert(rowMatcher)

        // Row is clickable via combinedClickable label
        rowNode.assert(hasClickAction())

        // Assert combinedClickable labels on the row
        rowNode.assert(hasOnClickLabelWithGuidance("Open message"))

        // Assert custom action labels are present on the row
        rowNode.assert(hasCustomActionLabelWithGuidance("Archive"))
        rowNode.assert(hasCustomActionLabelWithGuidance("Mark as read"))
    }

    @Test
    fun starButton_stateDescription_and_iconContentDescription_toggle() {
        setContent()

        // Initially for the first row, the icon should say "Add star" and the button has state "Not starred"
        // State description on the IconButton is present but older test APIs make it hard to access the ancestor.
        // We still validate the icon contentDescription toggles which mirrors state changes for users.
        // There are multiple "Add star" icons (for messages 1, 3, and 4). Click the first one deterministically.
        composeTestRule.onAllNodesWithContentDescription("Remove star").assertCountEquals(1) // message 2 initially starred
        composeTestRule.onAllNodesWithContentDescription("Add star").assertCountEquals(3)

        // Assert stateDescription is exposed correctly BEFORE toggle
        val notStarredMatcher = hasStateDescriptionWithGuidance(
            "Not starred",
            "Add Modifier.semantics { stateDescription = \"Not starred\" } to the star IconButton")
        val starredMatcher = hasStateDescriptionWithGuidance(
            "Starred",
            "Add Modifier.semantics { stateDescription = \"Starred\" } to the star IconButton when starred")
        composeTestRule.onAllNodesWithContentDescription("Add star").assertAll(notStarredMatcher)
        composeTestRule.onAllNodesWithContentDescription("Remove star").assertAll(starredMatcher)

        // Click the first message's star icon
        composeTestRule.onAllNodesWithContentDescription("Add star").onFirst().performClick()

        // After toggle the number of "Remove star" icons should increase by one
        composeTestRule.onAllNodesWithContentDescription("Remove star").assertCountEquals(2)

        // And stateDescription should reflect new states on all icons
        composeTestRule.onAllNodesWithContentDescription("Add star").assertAll(notStarredMatcher)
        composeTestRule.onAllNodesWithContentDescription("Remove star").assertAll(starredMatcher)
    }

    @Test
    fun errorBanner_hasErrorSemantics_andAssertiveLiveRegion() {
        setContent(showError = true)

        val errorMatcher = hasErrorWithGuidance(
            "Failed to sync. Tap to retry.",
            "Add error(\"Failed to sync. Tap to retry.\") to the error banner container semantics")
        val assertiveMatcher = hasLiveRegionWithGuidance(LiveRegionMode.Assertive, "Add liveRegion = LiveRegionMode.Assertive to the error banner container")

        // Find the row by its visible text
        val errorNode = composeTestRule.onNodeWithText("Failed to sync. Tap to retry.")
        errorNode.assert(errorMatcher)
        errorNode.assert(assertiveMatcher)

        // Assert combinedClickable labels on the error banner container
        errorNode.assert(hasOnClickLabelWithGuidance("Retry sync"))
        errorNode.assert(hasOnLongClickLabelWithGuidance("Dismiss error"))
    }

    @Test
    fun firstMessageRow_markReadToggle_customAction_invokes_and_updates_semantics() {
        setContent()

        // Sanity: initially 2 unread messages
        composeTestRule.onAllNodesWithText("2 new messages").assertCountEquals(1)

        // Find Alice row
        val aliceRowDesc = expectedRowDescription("Alice", "Trip photos", unread = true, starred = false)
        val aliceRow = composeTestRule.onNodeWithContentDescription(aliceRowDesc)
        // It should expose custom action "Mark as read"
        aliceRow.assert(hasCustomActionLabelWithGuidance("Mark as read"))

        // Invoke the custom action by label by extracting and calling it on UI thread
        aliceRow.performCustomAction("Mark as read", composeTestRule)

        // After toggling, new messages count should update to 1
        composeTestRule.onAllNodesWithText("1 new messages").assertCountEquals(1)

        // And the row's content description should now say Read and expose opposite label
        val aliceReadDesc = expectedRowDescription("Alice", "Trip photos", unread = false, starred = false)
        composeTestRule.onNodeWithContentDescription(aliceReadDesc)
            .assertExists()
            .assert(hasCustomActionLabelWithGuidance("Mark as unread"))
    }

    @Test
    fun firstMessageRow_archive_customAction_removes_item_from_list() {
        setContent()

        // Ensure Alice is visible initially
        val aliceRowDesc = expectedRowDescription("Alice", "Trip photos", unread = true, starred = false)
        val aliceRow = composeTestRule.onNodeWithContentDescription(aliceRowDesc)
        aliceRow.assertExists()
        aliceRow.assert(hasCustomActionLabelWithGuidance("Archive"))

        // Perform Archive custom action
        aliceRow.performCustomAction("Archive", composeTestRule)

        // Alice row should no longer exist
        composeTestRule.onAllNodesWithContentDescription(aliceRowDesc).assertCountEquals(0)

        // And list still shows others, e.g., Bob exists
        val bobRowDesc = expectedRowDescription("Bob", "Invoice #3411", unread = false, starred = true)
        composeTestRule.onAllNodesWithContentDescription(bobRowDesc).assertCountEquals(1)
    }
}

private fun withTextExactly(text: String): SemanticsMatcher = hasText(text, substring = false, ignoreCase = false)

private fun hasOnClickLabelWithGuidance(expected: String): SemanticsMatcher =
    SemanticsMatcher("Missing or wrong OnClick label '$expected'. Hint: pass onClickLabel = '$expected' to clickable/combinedClickable") { node ->
        node.config.getOrNull(SemanticsActions.OnClick)?.label == expected
    }

private fun hasOnLongClickLabelWithGuidance(expected: String): SemanticsMatcher =
    SemanticsMatcher("Missing or wrong OnLongClick label '$expected'. Hint: pass onLongClickLabel = '$expected' to combinedClickable") { node ->
        node.config.getOrNull(SemanticsActions.OnLongClick)?.label == expected
    }

private fun hasCustomActionLabelWithGuidance(expected: String): SemanticsMatcher =
    SemanticsMatcher("Missing custom action '$expected'. Hint: include CustomAccessibilityAction(label = '$expected') in customActions list") { node ->
        val list = node.config.getOrNull(SemanticsActions.CustomActions) ?: emptyList()
        list.any { it.label == expected }
    }

private fun hasPaneTitleWithGuidance(expected: String): SemanticsMatcher =
    SemanticsMatcher("Missing paneTitle '$expected'. Hint: add semantics { paneTitle = '$expected' } to the title Text") { node ->
        node.config.getOrNull(SemanticsProperties.PaneTitle) == expected
    }

private fun isHeadingWithGuidance(hint: String): SemanticsMatcher =
    SemanticsMatcher("Missing heading() semantics. Hint: $hint") { node ->
        node.config.getOrNull(SemanticsProperties.Heading) != null
    }

private fun hasLiveRegionWithGuidance(mode: LiveRegionMode, hint: String): SemanticsMatcher =
    SemanticsMatcher("Missing liveRegion = $mode. Hint: $hint") { node ->
        node.config.getOrNull(SemanticsProperties.LiveRegion) == mode
    }

private fun hasStateDescriptionWithGuidance(expected: String, hint: String): SemanticsMatcher =
    SemanticsMatcher("Missing stateDescription = '$expected'. Hint: $hint") { node ->
        node.config.getOrNull(SemanticsProperties.StateDescription) == expected
    }

private fun hasErrorWithGuidance(expected: String, hint: String): SemanticsMatcher =
    SemanticsMatcher("Missing error('$expected') semantics. Hint: $hint") { node ->
        node.config.getOrNull(SemanticsProperties.Error) == expected
    }

private fun hasExactContentDescriptionWithGuidance(expected: String): SemanticsMatcher =
    SemanticsMatcher("Row contentDescription mismatch. Expected exactly: '$expected'. Hint: ensure you compose it inside semantics { contentDescription = buildString { ... } and set mergeDescendants = true }") { node ->
        val value = node.config.getOrNull(SemanticsProperties.ContentDescription) ?: return@SemanticsMatcher false
        value == listOf(expected)
    }

private fun hasProgressBarRangeInfoWithGuidance(
    expected: ProgressBarRangeInfo,
    hint: String,
): SemanticsMatcher =
    SemanticsMatcher("Missing progressBarRangeInfo = $expected. Hint: $hint") { node ->
        node.config.getOrNull(SemanticsProperties.ProgressBarRangeInfo) == expected
    }

private fun expectedRowDescription(sender: String, subject: String, unread: Boolean, starred: Boolean): String = buildString {
    append("From $sender. ")
    append("Subject: $subject. ")
    append(if (unread) "Unread. " else "Read. ")
    append(if (starred) "Starred." else "Not starred.")
}

// Invoke a custom accessibility action by its label, on the UI thread
private fun androidx.compose.ui.test.SemanticsNodeInteraction.performCustomAction(
    label: String,
    rule: ComposeContentTestRule,
) {
    val list =
        fetchSemanticsNode().config.getOrNull<List<CustomAccessibilityAction>>(SemanticsActions.CustomActions)
            ?: emptyList()
    val custom = list.firstOrNull { it.label == label }
        ?: error("Custom action '$label' not found. Hint: add it to customActions = listOf(CustomAccessibilityAction(label = '$label') { ... }) on this node")

    rule.runOnIdle {
        val any = custom.action
            ?: error("Custom action '$label' has null action. Hint: provide a lambda that returns true and calls your handler")

        // Build a zero-arg invoker that returns Boolean regardless of underlying signature
        val invoker: () -> Boolean = when (any) {
            is AccessibilityAction<*> -> {
                val inner = any.action
                when (inner) {
                    is Function0<*> -> { { (inner as Function0<Boolean>).invoke() } }
                    is Function1<*, *> -> { { (inner as (Any?) -> Boolean).invoke(Unit) || (inner as (Any?) -> Boolean).invoke(null) } }
                    else -> error("Unsupported AccessibilityAction.inner type for '$label'. This test expects a Function0<Boolean> or Function1<Any?, Boolean>")
                }
            }
            is Function0<*> -> { { (any as Function0<Boolean>).invoke() } }
            is Function1<*, *> -> { { (any as (Any?) -> Boolean).invoke(Unit) || (any as (Any?) -> Boolean).invoke(null) } }
            else -> error("Unsupported CustomAccessibilityAction.action type for '$label'. Provide a zero-arg or one-arg lambda.")
        }

        invoker.invoke()
    }
}

