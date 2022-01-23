package cn.joestar.flexlayout.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cn.joestar.flexlayout.ui.theme.FlexLayoutTheme
import kotlin.math.min
import kotlin.random.Random

@Composable
fun TestPreview(content: @Composable () -> Unit) {
    FlexLayoutTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            content = content
        )
    }
}

@Composable
fun TestItems(count: Int = 30, maxLength: Int = 7) {
    repeat(count) {
        val randomLength =
            Text(text = getRandomText(maxLength))
    }
}

fun getRandomText(maxLength: Int): String {
    val text = "ABCDEFGHIJK"
    val length = min(text.length, Random.nextInt(1, maxLength))
    return text.substring(0, length)
}