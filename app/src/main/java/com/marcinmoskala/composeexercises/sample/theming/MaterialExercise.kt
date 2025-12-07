package com.marcinmoskala.composeexercises.sample.theming

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ExerciseLightColors = lightColorScheme(
    primary = Color(0xFF00639A),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF4DD0E1),
    onSecondary = Color(0xFF00363D),
    background = Color(0xFFF7FAFD),
    onBackground = Color(0xFF111418),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF111418),
)

private val ExerciseDarkColors = darkColorScheme(
    primary = Color(0xFF7CC8FF),
    onPrimary = Color(0xFF00344F),
    secondary = Color(0xFF72DAE6),
    onSecondary = Color(0xFF001F24),
    background = Color(0xFF101418),
    onBackground = Color(0xFFE3E7EA),
    surface = Color(0xFF171B1F),
    onSurface = Color(0xFFE3E7EA),
)

private val ExerciseShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(28.dp),
)

private val ExerciseTypography = Typography(
    titleLarge = Typography().titleLarge.copy(letterSpacing = 0.5.sp),
    bodyMedium = Typography().bodyMedium.copy(lineHeight = 20.sp),
)

@Composable
fun MaterialExerciseTheme(
    content: @Composable () -> Unit
) {
    content()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialExerciseScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Material Theme Exercise",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* no-op */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* no-op */ }) {
                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    // TODO: Specify colors for containerColor, titleContentColor, navigationIconContentColor,
                    // actionIconContentColor based on primary container
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    // TODO: Specify colors for containerColor and contentColor based on primary container
                )
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "Welcome to Material 3",
                        // TODO: Specify title large typography
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Theme-aware UI with light and dark support",
                        // TODO: Specify body medium typography
                    )
                }
            }

            Text(
                text = "This text uses primary color explicitly",
                // TODO: Specify primary color
            )

            Button(onClick = { /* no-op */ }) { Text("Primary") }
            FilledTonalButton(onClick = { /* no-op */ }) { Text("Tonal") }
            OutlinedButton(onClick = { /* no-op */ }) { Text("Outlined") }

            AssistChip(
                onClick = { /* no-op */ },
                label = { Text("Assist chip") },
                colors = AssistChipDefaults.assistChipColors(
                    // TODO: Specify onSurface color for label
                )
            )

            ElevatedCard {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "Elevated card",
                        // TODO: Specify title large typography
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(text = "Uses elevation and surface colors from the theme.")
                }
            }

            Card(colors = CardDefaults.cardColors(
                // TODO: Specify surfaceVariant color for container
            )) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "Tonal card",
                        // TODO: Specify title large typography and onSurfaceVariant color
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(text = "Uses surfaceVariant/onSurfaceVariant for a softer look.")
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(
                        // TODO: Specify outline color with 40% alpha
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(text = "Shapes come from MaterialTheme.shapes.medium by default.")
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Dynamic Mode - Red Dominated Wallpaper", showBackground = true, wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
@Preview(name = "Dynamic Mode - Green Dominated Wallpaper", showBackground = true, wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@Preview(name = "Dynamic Mode - Blue Dominated Wallpaper", showBackground = true, wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE)
@Composable
private fun MaterialExerciseDarkPreview() {
    MaterialExerciseTheme {
        MaterialExerciseScreen()
    }
}

