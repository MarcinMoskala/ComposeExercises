package com.marcinmoskala.composeexercises.sample.interop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.compose.AndroidFragment
import androidx.fragment.compose.FragmentState
import androidx.fragment.compose.rememberFragmentState

// Run FragmentInComposeActivity below
@Preview
@Composable
private fun ComponentUsingFragment() {
    AndroidFragment<SimpleFragment>()

    // With arguments
    AndroidFragment<SimpleFragment>(
        arguments = bundleOf("text" to "Hello from Fragment with an argument"),
    )

    // With hoisted state
    val fragmentState = rememberFragmentState()
    AndroidFragment<SimpleFragment>(
        fragmentState = fragmentState,
    )

    // Changing fragment state
    AndroidFragment<AmazingTextFragment>(
        onUpdate = { fragment ->
            fragment.textSize = 20f
        },
    )

//    var textSize by remember { mutableStateOf(20f) }
//    ClickableChangeable(
//        onClick = { textSize += 10f },
//        textSize = textSize
//    )
}

class SimpleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val textToDisplay = arguments?.getString("text") ?: "Hello from Fragment"
        return TextView(requireContext()).apply {
            text = textToDisplay
        }
    }
}

//@Composable
//fun ClickableChangeable(
//    onClick: () -> Unit,
//    textSize: Float
//) {
//    AndroidFragment<AmazingTextFragment>(
//        onUpdate = { fragment ->
//            fragment.textSize = textSize
//            fragment.onClickListener = { onClick() }
//        },
//        modifier = Modifier.fillMaxSize(),
//    )
//}

//@Composable
//fun ClickableChangeable(
//    onClick: () -> Unit,
//    textSize: Float = 20f
//) {
//    var fragmentRef by remember { mutableStateOf<AmazingTextFragment?>(null) }
//    LaunchedEffect(onClick, textSize) {
//        fragmentRef?.textSize = textSize
//        fragmentRef?.onClickListener = { onClick() }
//    }
//    AndroidFragment<AmazingTextFragment>(
//        onUpdate = { fragment ->
//            fragment.textSize = textSize
//            fragment.onClickListener = { onClick() }
//            fragmentRef = fragment
//        },
//        modifier = Modifier.fillMaxSize(),
//    )
//}

//@Composable
//fun ClickableChangeable(
//    onClick: () -> Unit,
//    textSize: Float = 20f
//) {
//    AndroidFragmentV2<AmazingTextFragment>(
//        update = { fragment ->
//            fragment.textSize = textSize
//            fragment.onClickListener = { onClick() }
//        },
//        modifier = Modifier.fillMaxSize(),
//    )
//}

@Composable
inline fun <reified T : Fragment> AndroidFragmentV2(
    modifier: Modifier = Modifier,
    fragmentState: FragmentState = rememberFragmentState(),
    arguments: Bundle = Bundle.EMPTY,
    updateKeys: Array<Any> = emptyArray(),
    noinline update: (T) -> Unit = { },
) {
    if (LocalInspectionMode.current) {
        Text(
            text = "Fragment Preview",
            modifier = modifier.border(1.dp, Color.Black)
        )
        return
    }
    var fragmentRef by remember { mutableStateOf<T?>(null) }
    LaunchedEffect(update, *updateKeys) {
        fragmentRef?.let { update(it) }
    }
    AndroidFragment(clazz = T::class.java, modifier, fragmentState, arguments,
        onUpdate = {
            update(it)
            fragmentRef = it
        }
    )
}

class AmazingTextFragment : Fragment() {
    private lateinit var textView: TextView
    var onClickListener: (() -> Unit)? = null
    var textSize = 20f
        set(value) {
            field = value
            textView.textSize = value
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return TextView(requireContext()).apply {
            text = "Custom fragment"
            textView = this
            setOnClickListener { onClickListener?.invoke() }
            this.textSize = textSize
        }
    }
}

class FragmentInComposeActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComponentUsingFragment()
        }
    }
}