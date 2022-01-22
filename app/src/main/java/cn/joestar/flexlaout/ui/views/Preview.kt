package cn.joestar.flexlaout.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cn.joestar.flexlaout.ui.theme.FlexLaoutTheme
import kotlin.math.min
import kotlin.random.Random

@Composable
fun TestPreview(content: @Composable () -> Unit) {
    FlexLaoutTheme {
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
        val randomLength = Random.nextInt(1, maxLength)
        Text(text = getRandomText(randomLength))
    }
}

fun getRandomText(maxLength: Int): String {
    val text = "ABCDEFGHIJK"
    val length = min(text.length, maxLength)
    return text.substring(0, length)
}