package com.marcinmoskala.composeexercises.ui.interop

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.fragment.app.Fragment
import com.marcinmoskala.composeexercises.R
import com.marcinmoskala.composeexercises.loremIpsum
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf


sealed class ArticleParagraph {
    data class Text(
        val text: String,
        val isTitle: Boolean = false
    ) : ArticleParagraph()

    data class Image(
        val url: String
    ) : ArticleParagraph()

    data class Ad(
        val text: String,
        val imageUrl: String,
        val onClick: () -> Unit
    ) : ArticleParagraph()
}

@Composable
fun ArticleScreen(content: PersistentList<ArticleParagraph>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(content) { paragraph ->
            when (paragraph) {
                is ArticleParagraph.Text -> ArticleParagraph(paragraph)
                is ArticleParagraph.Image -> { /* TODO: Use ImageFragment */
                }

                is ArticleParagraph.Ad -> { /* TODO: Use AdView */
                }
            }
        }
    }
}

@Composable
private fun ArticleParagraph(paragraph: ArticleParagraph.Text) {
    Text(
        paragraph.text,
        fontSize = if (paragraph.isTitle) 46.sp else 20.sp,
        modifier = Modifier.padding(10.dp)
    )
}

class AdView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val imageView = ImageView(context)
    private val textView = TextView(context)

    var ad: ArticleParagraph.Ad? = null
        set(value) {
            field = value
            if (value != null) {
                textView.text = value.text
                setImage(value.imageUrl)
            }
        }

    init {
        orientation = VERTICAL

        // Add ImageView
        imageView.apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            adjustViewBounds = true
            scaleType = ImageView.ScaleType.CENTER_CROP
            ad?.let { setImage(it.imageUrl) }
        }
        addView(imageView)

        // Add TextView
        textView.apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setPadding(16, 8, 16, 8)
            ad?.let { text = it.text }
        }
        addView(textView)

        // Set click listener
        setOnClickListener {
            ad?.onClick?.invoke()
        }
    }

    fun setImage(url: String) {
        imageView.setImageResource(R.drawable.compose) // Placeholder
        // TODO: We removed Glide dependency, use Coil AsyncImage
//                Glide.with(this)
//                    .load(imageUrl)
//                    .into(imageView)
    }
}

class ImageFragment : Fragment() {

    private lateinit var imageUrl: String
    private lateinit var caption: String

    private lateinit var imageView: ImageView
    private lateinit var captionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = it.getString(ARG_IMAGE_URL, "")
            caption = it.getString(ARG_CAPTION, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)

        imageView = view.findViewById<ImageView>(R.id.imageView)
        captionTextView = view.findViewById<TextView>(R.id.captionTextView)

        displayData()

        return view
    }

    fun update(imageUrl: String, caption: String) {
        this.imageUrl = imageUrl
        this.caption = caption
        displayData()
    }

    private fun displayData() {
        loadImage()
        captionTextView.text = caption
    }

    private fun loadImage() {
        imageView.setImageResource(R.drawable.compose) // Placeholder
        // TODO: We removed Glide dependency, use Coil AsyncImage
//                Glide.with(this)
//                    .load(imageUrl)
//                    .into(imageView)
    }

    companion object {
        private const val ARG_IMAGE_URL = "imageUrl"
        private const val ARG_CAPTION = "caption"
    }
}

@Preview
@Composable
fun ArticleScreenPreview() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val articles = persistentListOf(
        persistentListOf(
            ArticleParagraph.Text("Hello", isTitle = true),
            ArticleParagraph.Image("https://example.com/image.jpg"),
            ArticleParagraph.Text(loremIpsum(10)),
            ArticleParagraph.Ad(
                text = "Ad",
                imageUrl = "https://example.com/ad.jpg",
                onClick = { Toast.makeText(context, "Ad clicked", Toast.LENGTH_SHORT).show() }
            ),
            ArticleParagraph.Text(loremIpsum(40))
        ),
        persistentListOf(
            ArticleParagraph.Text("World", isTitle = true),
            ArticleParagraph.Image("https://example.com/image2.jpg"),
            ArticleParagraph.Text(loremIpsum(40)),
            ArticleParagraph.Ad(
                text = "Ad2",
                imageUrl = "https://example.com/ad2.jpg",
                onClick = { Toast.makeText(context, "Ad2 clicked", Toast.LENGTH_SHORT).show() }
            ),
            ArticleParagraph.Text(loremIpsum(80))
        )
    )
    var currentArticle by remember { mutableStateOf(0) }
    Box(modifier = Modifier.fillMaxSize()) {
        ArticleScreen(articles[currentArticle])
        Button(
            onClick = { currentArticle = (currentArticle + 1) % articles.size },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Text("Next article")
        }
    }
}