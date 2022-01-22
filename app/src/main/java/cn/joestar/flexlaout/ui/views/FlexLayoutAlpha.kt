package cn.joestar.flexlaout.ui.views

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FlexLayoutAlpha(modifier: Modifier, divider: Dp = 4.dp, content: @Composable () -> Unit) {
    val policy = MeasurePolicy { measurables, constraints ->
        val childConstraints = Constraints(0, constraints.maxWidth, 0, constraints.maxHeight)
        val div = divider.toPx().toInt()
        val maxWidth = constraints.maxWidth
        var height = 0
        var width = 0
        var lineHeight = 0
        val placeable = measurables.map {
            val placeable = it.measure(childConstraints)
            if (placeable.width + width > maxWidth) {
                height += lineHeight + div
                width = 0
                lineHeight = 0
            }
            lineHeight = maxOf(lineHeight, placeable.height)
            width += placeable.width + div
            placeable
        }
        height += lineHeight
        layout(constraints.maxWidth, height) {
            var childX = 0
            var childY = 0
            placeable.forEach {
                if (childX + it.width > maxWidth) {
                    childX = 0
                    childY += lineHeight + div
                    lineHeight = 0
                }
                it.place(childX, childY)
                childX += it.width + div
                lineHeight = maxOf(lineHeight, it.height)
            }
        }
    }
    //  create compose with use measurePolicy to measure and layout content
    Layout(content = content, modifier = modifier, measurePolicy = policy)
}

@Preview
@Composable
fun FlexLayoutAlphaPreview() {
    TestPreview {
        FlexLayoutAlpha(modifier = Modifier.padding(8.dp)) {
            TestItems(maxLength = 10)
        }
    }
}
