package com.marcinmoskala.composeexercises.sample.semantics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcinmoskala.composeexercises.R

data class SemanticsExerciseMessage(
    val id: Int,
    val sender: String,
    val subject: String,
    val preview: String,
    val avatarRes: Int,
    val unread: Boolean,
    val starred: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemanticsExerciseScreen(
    showError: Boolean,
    downloading: Boolean,
    progress: Float,
    messages: List<SemanticsExerciseMessage>,
    onDismissError: () -> Unit = {},
    onToggleStar: (Int) -> Unit = {},
    onOpen: (Int) -> Unit = {},
    onArchive: (Int) -> Unit = {},
    onMarkReadToggle: (Int) -> Unit = {},
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Inbox",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .semantics {
                                paneTitle = "Inbox"
                                heading()
                            }
                    )
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            val newCount = messages.count { it.unread }
            if(newCount > 0) {
                Text(
                    text = "$newCount new messages",
                    modifier = Modifier
                        .semantics {
                            liveRegion = LiveRegionMode.Polite
                        },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(8.dp))
            }

            if (showError) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.12f))
                        .padding(12.dp)
                        .semantics {
                            error("Failed to sync. Tap to retry.")
                            liveRegion = LiveRegionMode.Assertive
                        }
                        .combinedClickable(
                            onClickLabel = "Retry sync",
                            onLongClickLabel = "Dismiss error",
                            onClick = { onDismissError() },
                            onLongClick = { onDismissError() }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Failed to sync. Tap to retry.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(Modifier.height(12.dp))
            }

            if (downloading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Syncing…", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(6.dp))
                    SemanticsAwareLinearProgressIndicator(progress = progress)
                }
                Spacer(Modifier.height(16.dp))
            }

            Text(
                text = "Messages",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .semantics { heading() }
            )
            Spacer(Modifier.height(4.dp))
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                itemsIndexed(messages, key = { _, m -> m.id }) { index, message ->
                    MessageRow(
                        index = index,
                        message = message,
                        onToggleStar = onToggleStar,
                        onOpen = onOpen,
                        onArchive = onArchive,
                        onMarkReadToggle = onMarkReadToggle,
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun SemanticsAwareLinearProgressIndicator(
    progress: Float,
) {
    val clamped = progress.coerceIn(0f, 1f)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(clamped, 0f..1f, 0)
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(clamped)
                .height(8.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
private fun MessageRow(
    index: Int,
    message: SemanticsExerciseMessage,
    onToggleStar: (Int) -> Unit,
    onOpen: (Int) -> Unit,
    onArchive: (Int) -> Unit,
    onMarkReadToggle: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = buildString {
                    append("From ${message.sender}. ")
                    append("Subject: ${message.subject}. ")
                    if (message.unread) append("Unread. ") else append("Read. ")
                    if (message.starred) append("Starred.") else append("Not starred.")
                }
                customActions = listOf(
                    CustomAccessibilityAction(label = "Archive") { onArchive(message.id); true },
                    CustomAccessibilityAction(label = if (message.unread) "Mark as read" else "Mark as unread") {
                        onMarkReadToggle(message.id); true
                    }
                )
            }
            .clickable(
                onClickLabel = "Open message",
                onClick = { onOpen(message.id) },
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = message.avatarRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = message.sender,
                    style = MaterialTheme.typography.titleSmall
                )
                if (message.unread) {
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "•",
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Text(
                text = message.subject,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = message.preview,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        IconButton(
            onClick = { onToggleStar(message.id) },
            modifier = Modifier.semantics {
                stateDescription = if (message.starred) "Starred" else "Not starred"
            },
        ) {
            val icon = if (message.starred) R.drawable.heart_full else R.drawable.heart_empty
            val tint = if (message.starred) MaterialTheme.colorScheme.primary else Color.Gray
            Icon(
                painter = painterResource(id = icon),
                contentDescription = if (message.starred) "Remove star" else "Add star",
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(name = "Semantics exercise")
@Composable
private fun SemanticsExercisePreview() {
    _root_ide_package_.com.marcinmoskala.composeexercises.sample.theming.AppTheme(darkTheme = false) {
        var showError by remember { mutableStateOf(false) }
        var downloading by remember { mutableStateOf(true) }
        var progress by remember { mutableStateOf(0.65f) }
        var messages by remember {
            mutableStateOf(
                listOf(
                    SemanticsExerciseMessage(
                        1,
                        "Alice",
                        "Trip photos",
                        "Check out these pictures from the mountains.",
                        R.drawable.avatar,
                        unread = true,
                        starred = false
                    ),
                    SemanticsExerciseMessage(
                        2,
                        "Bob",
                        "Invoice #3411",
                        "Please find attached the invoice for November.",
                        R.drawable.avatar,
                        unread = false,
                        starred = true
                    ),
                    SemanticsExerciseMessage(
                        3,
                        "Carol",
                        "Team meeting",
                        "Meeting rescheduled to 3pm tomorrow.",
                        R.drawable.avatar,
                        unread = true,
                        starred = false
                    ),
                    SemanticsExerciseMessage(
                        4,
                        "Dave",
                        "Lunch?",
                        "Are you up for lunch this Friday?",
                        R.drawable.avatar,
                        unread = false,
                        starred = false
                    )
                )
            )
        }

        SemanticsExerciseScreen(
            showError = showError,
            downloading = downloading,
            progress = progress,
            messages = messages,
            onDismissError = { showError = false },
            onToggleStar = { id ->
                messages = messages.map { if (it.id == id) it.copy(starred = !it.starred) else it }
            },
            onOpen = { /* no-op demo */ },
            onArchive = { id -> messages = messages.filterNot { it.id == id } },
            onMarkReadToggle = { id ->
                messages = messages.map { if (it.id == id) it.copy(unread = !it.unread) else it }
            }
        )
    }
}

