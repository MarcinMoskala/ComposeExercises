package com.marcinmoskala.composeexercises.sample

import android.widget.Button
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcinmoskala.composeexercises.sample.modifiers.fade
import org.w3c.dom.Text
import java.nio.file.WatchEvent

@Preview
@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }
    println("Counter")

    Box(
//        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.size(200.dp),
    ) {
        Button(
            onClick = { count-- },
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text("-")
        }
        CounterText(
            { count },
            modifier = Modifier.align(Alignment.Center)
        )
        Button(
            onClick = { count++ },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Text("+")
        }
    }
}

@Composable
private fun CounterText(count: () -> Int, modifier: Modifier) {
    println("Text")
    Text(
        text = count().toString(),
        fontSize = 30.sp,
        modifier = modifier.padding(10.dp)
    )
}