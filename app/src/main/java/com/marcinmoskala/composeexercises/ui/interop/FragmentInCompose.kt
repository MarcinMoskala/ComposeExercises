package com.marcinmoskala.composeexercises.ui.interop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.fragment.compose.AndroidFragment

// Run FragmentInComposeActivity below
@Composable
private fun ComponentUsingFragment() {
    AndroidFragment<SimpleFragment>(
        modifier = Modifier.fillMaxSize()
    )

//    // With arguments
//    AndroidFragment<SimpleFragment>(
//        arguments = Bundle().apply { putString("text", "Hello from Fragment with an argument") },
//        modifier = Modifier.fillMaxSize()
//    )
//
//    // With hoisted state
//    val fragmentState = rememberFragmentState()
//    AndroidFragment<SimpleFragment>(
//        fragmentState = fragmentState,
//        modifier = Modifier.fillMaxSize()
//    )
//
//    var fontSize by remember { mutableStateOf(20f) }
//    ClickableChangeable(
//        textSize = fontSize,
//        onClick = { fontSize += 5f },
//    )
}

class SimpleFragment : androidx.fragment.app.Fragment() {
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

@Composable
fun ClickableChangeable(
    onClick: () -> Unit,
    textSize: Float = 20f
) {
    AndroidFragment<ClickableChangeableFragment>(
        onUpdate = { fragment ->
            fragment.textSize = 20f
            fragment.onClickListener = { onClick() }
        },
        modifier = Modifier.fillMaxSize(),
    )
}

//@Composable
//fun ClickableChangeable(
//    onClick: () -> Unit,
//    textSize: Float = 20f
//) {
//    var fragmentRef by remember { mutableStateOf<ClickableChangeableFragment?>(null) }
//    LaunchedEffect(onClick, textSize) {
//        fragmentRef?.textSize = textSize
//        fragmentRef?.onClickListener = { onClick() }
//    }
//    AndroidFragment<ClickableChangeableFragment>(
//        onUpdate = { fragment ->
//            fragment.textSize = 20f
//            fragment.onClickListener = { onClick() }
//            fragmentRef = fragment
//        },
//        modifier = Modifier.fillMaxSize(),
//    )
//}

class ClickableChangeableFragment : androidx.fragment.app.Fragment() {
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

class FragmentInComposeActivity: FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComponentUsingFragment()
        }
    }
}