package com.marcinmoskala.composeexercises.theming

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.AccessibilityAction
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.LiveRegionMode
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
import com.marcinmoskala.composeexercises.sample.theming.AppTheme
import com.marcinmoskala.composeexercises.sample.theming.SemanticsExerciseMessage
import com.marcinmoskala.composeexercises.sample.theming.SemanticsExerciseScreen
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

        val paneTitleMatcher = SemanticsMatcher.expectValue(SemanticsProperties.PaneTitle, "Inbox")
        val headingMatcher = SemanticsMatcher.keyIsDefined(SemanticsProperties.Heading)

        composeTestRule.onNodeWithText("Inbox")
            .assert(paneTitleMatcher)
            .assert(headingMatcher)
    }

    @Test
    fun newMessagesText_isPoliteLiveRegion() {
        setContent()

        // Initial data has 2 unread messages
        val politeLiveRegion = SemanticsMatcher.expectValue(SemanticsProperties.LiveRegion, LiveRegionMode.Polite)

        composeTestRule.onNodeWithText("2 new messages")
            .assert(politeLiveRegion)
    }

    @Test
    fun messagesHeader_hasHeading() {
        setContent()

        val headingMatcher = SemanticsMatcher.keyIsDefined(SemanticsProperties.Heading)
        composeTestRule.onNodeWithText("Messages")
            .assert(headingMatcher)
    }

    @Test
    fun firstMessageRow_hasMergedContent_andCustomActions_andClickActions() {
        setContent()

        // Row merges descendants and exposes a composed contentDescription for the first message (Alice)
        val rowMatcher = SemanticsMatcher.expectValue(
            SemanticsProperties.ContentDescription,
            listOf("From Alice. Subject: Trip photos. Unread. Not starred.")
        )
        val rowNode = composeTestRule.onNodeWithContentDescription("From Alice. Subject: Trip photos. Unread. Not starred.")
        rowNode.assert(rowMatcher)

        // Row is clickable via combinedClickable label
        rowNode.assert(hasClickAction())

        // Assert combinedClickable labels on the row
        rowNode.assert(hasOnClickLabel("Open message"))

        // Assert custom action labels are present on the row
        rowNode.assert(hasCustomActionLabel("Archive"))
        rowNode.assert(hasCustomActionLabel("Mark as read"))
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
        val notStarredMatcher = SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Not starred")
        val starredMatcher = SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Starred")
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

        val errorMatcher = SemanticsMatcher.expectValue(
            SemanticsProperties.Error,
            "Failed to sync. Tap to retry."
        )
        val assertiveMatcher = SemanticsMatcher.expectValue(
            SemanticsProperties.LiveRegion,
            LiveRegionMode.Assertive
        )

        // Find the row by its visible text
        val errorNode = composeTestRule.onNodeWithText("Failed to sync. Tap to retry.")
        errorNode.assert(errorMatcher)
        errorNode.assert(assertiveMatcher)

        // Assert combinedClickable labels on the error banner container
        errorNode.assert(hasOnClickLabel("Retry sync"))
        errorNode.assert(hasOnLongClickLabel("Dismiss error"))
    }

    @Test
    fun firstMessageRow_markReadToggle_customAction_invokes_and_updates_semantics() {
        setContent()

        // Sanity: initially 2 unread messages
        composeTestRule.onAllNodesWithText("2 new messages").assertCountEquals(1)

        // Find Alice row
        val aliceRow = composeTestRule.onNodeWithContentDescription(
            "From Alice. Subject: Trip photos. Unread. Not starred."
        )
        // It should expose custom action "Mark as read"
        aliceRow.assert(hasCustomActionLabel("Mark as read"))

        // Invoke the custom action by label by extracting and calling it on UI thread
        aliceRow.performCustomAction("Mark as read", composeTestRule)

        // After toggling, new messages count should update to 1
        composeTestRule.onAllNodesWithText("1 new messages").assertCountEquals(1)

        // And the row's content description should now say Read and expose opposite label
        composeTestRule.onNodeWithContentDescription(
            "From Alice. Subject: Trip photos. Read. Not starred."
        ).assertExists()
            .assert(hasCustomActionLabel("Mark as unread"))
    }

    @Test
    fun firstMessageRow_archive_customAction_removes_item_from_list() {
        setContent()

        // Ensure Alice is visible initially
        val aliceRow = composeTestRule.onNodeWithContentDescription(
            "From Alice. Subject: Trip photos. Unread. Not starred."
        )
        aliceRow.assertExists()
        aliceRow.assert(hasCustomActionLabel("Archive"))

        // Perform Archive custom action
        aliceRow.performCustomAction("Archive", composeTestRule)

        // Alice row should no longer exist
        composeTestRule.onAllNodesWithContentDescription(
            "From Alice. Subject: Trip photos. Unread. Not starred."
        ).assertCountEquals(0)

        // And list still shows others, e.g., Bob exists
        composeTestRule.onAllNodesWithContentDescription(
            "From Bob. Subject: Invoice #3411. Read. Starred."
        ).assertCountEquals(1)
    }
}

private fun withTextExactly(text: String): SemanticsMatcher = hasText(text, substring = false, ignoreCase = false)

private fun hasOnClickLabel(expected: String): SemanticsMatcher =
    SemanticsMatcher("has OnClick label '$expected'") { node ->
        node.config.getOrNull(SemanticsActions.OnClick)?.label == expected
    }

private fun hasOnLongClickLabel(expected: String): SemanticsMatcher =
    SemanticsMatcher("has OnLongClick label '$expected'") { node ->
        node.config.getOrNull(SemanticsActions.OnLongClick)?.label == expected
    }

private fun hasCustomActionLabel(expected: String): SemanticsMatcher =
    SemanticsMatcher("has custom action label '$expected'") { node ->
        val list = node.config.getOrNull(SemanticsActions.CustomActions) ?: emptyList()
        list.any { it.label == expected }
    }

// Invoke a custom accessibility action by its label, on the UI thread
private fun androidx.compose.ui.test.SemanticsNodeInteraction.performCustomAction(
    label: String,
    rule: ComposeContentTestRule,
) {
    val list =
        fetchSemanticsNode().config.getOrNull<List<CustomAccessibilityAction>>(SemanticsActions.CustomActions)
            ?: emptyList<CustomAccessibilityAction>()
    val custom = list.firstOrNull { it.label == label }
        ?: error("Custom action with label '$label' not found on node")

    rule.runOnIdle {
        val any = custom.action
            ?: error("Custom action '$label' is null")

        // Build a zero-arg invoker that returns Boolean regardless of underlying signature
        val invoker: () -> Boolean = when (any) {
            is AccessibilityAction<*> -> {
                val inner = any.action
                when (inner) {
                    is Function0<*> -> { { (inner as Function0<Boolean>).invoke() } }
                    is Function1<*, *> -> { { (inner as (Any?) -> Boolean).invoke(Unit) || (inner as (Any?) -> Boolean).invoke(null) } }
                    else -> error("Unsupported AccessibilityAction.inner type for '$label'")
                }
            }
            is Function0<*> -> { { (any as Function0<Boolean>).invoke() } }
            is Function1<*, *> -> { { (any as (Any?) -> Boolean).invoke(Unit) || (any as (Any?) -> Boolean).invoke(null) } }
            else -> error("Unsupported CustomAccessibilityAction.action type for '$label'")
        }

        invoker.invoke()
    }
}

