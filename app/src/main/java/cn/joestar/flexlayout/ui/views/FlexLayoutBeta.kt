package cn.joestar.flexlayout.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.*
import kotlin.math.max

@Composable
fun FlexLayoutBeta(
    modifier: Modifier,
    divider: Dp = 4.dp,
    arrangement: Arrangement.Horizontal = Arrangement.SpaceAround,
    content: @Composable () -> Unit
) {
    val policy = MeasurePolicy { measurables, constraints ->
        val childConstraints = Constraints(0, constraints.maxWidth, 0, constraints.maxHeight)
        val div = divider.toPx().toInt()
        val maxWidth = constraints.maxWidth
        var lineWidth = 0
        var lineHeight = 0
        val lines = LinkedList<FlexLine>()
        var temp = LinkedList<Placeable>()
        var width = 0
        var height = 0
        measurables.forEach {
            val placeable = it.measure(childConstraints)
            val pWidth = placeable.width
            if (pWidth + width >= maxWidth) {
                lines.add(FlexLine(lineWidth, lineHeight, temp))
                temp = LinkedList<Placeable>()
                height += lineHeight + div
                width = 0
                lineHeight = 0
                lineWidth = 0
            }
            temp.add(placeable)
            width += pWidth + div
            lineWidth += pWidth
            lineHeight = max(lineHeight, placeable.height)
        }
        lines.add(FlexLine(lineWidth, lineHeight, temp))
        height += lineHeight
        layout(constraints.maxWidth, height) {
            var childY = 0
            lines.forEach { lineData ->
                when (arrangement) {
                    Arrangement.Start -> layoutByStart(lineData, childY, div, maxWidth)
                    Arrangement.End -> layoutByEnd(lineData, childY, div, maxWidth)
                    Arrangement.Center -> layoutByCenter(lineData, childY, div, maxWidth)
                    Arrangement.SpaceAround -> layoutByAround(lineData, childY, div, maxWidth)
                    Arrangement.SpaceBetween -> layoutByBetween(lineData, childY, div, maxWidth)
                    Arrangement.SpaceEvenly -> layoutByEvenly(lineData, childY, div, maxWidth)
                }
                childY += lineData.lineHeight + div
            }
        }
    }
    //  create compose with use measurePolicy to measure and layout content
    Layout(content = content, modifier = modifier, measurePolicy = policy)
}

fun Placeable.PlacementScope.layoutByStart(
    lineData: FlexLine,
    childY: Int,
    div: Int,
    maxWidth: Int
) {
    var childX = 0
    lineData.list.forEach {
        it.place(childX, childY + (lineData.lineHeight - it.height) / 2)
        childX += it.width + div
    }
}

fun Placeable.PlacementScope.layoutByEnd(lineData: FlexLine, childY: Int, div: Int, maxWidth: Int) {
    val start = (maxWidth - (lineData.lineWidth + (lineData.list.size - 1) * div))
    var childX = start
    lineData.list.forEach {
        it.place(childX, childY + (lineData.lineHeight - it.height) / 2)
        childX += it.width + div
    }
}

fun Placeable.PlacementScope.layoutByCenter(
    lineData: FlexLine,
    childY: Int,
    div: Int,
    maxWidth: Int
) {
    val start = (maxWidth - (lineData.lineWidth + (lineData.list.size - 1) * div)) / 2
    var childX = start
    lineData.list.forEach {
        it.place(childX, childY + (lineData.lineHeight - it.height) / 2)
        childX += it.width + div
    }
}

fun Placeable.PlacementScope.layoutByEvenly(
    lineData: FlexLine,
    childY: Int,
    div: Int,
    maxWidth: Int
) {
    val space = (maxWidth - lineData.lineWidth) / (lineData.list.size + 1)
    var childX = space
    lineData.list.forEach {
        it.place(childX, childY + (lineData.lineHeight - it.height) / 2)
        childX += it.width + space
    }
}

fun Placeable.PlacementScope.layoutByAround(
    lineData: FlexLine,
    childY: Int,
    div: Int,
    maxWidth: Int
) {
    val space = (maxWidth - lineData.lineWidth) / lineData.list.size
    val start = space / 2
    var childX = start
    lineData.list.forEach {
        it.place(childX, childY + (lineData.lineHeight - it.height) / 2)
        childX += it.width + space
    }
}

fun Placeable.PlacementScope.layoutByBetween(
    lineData: FlexLine,
    childY: Int,
    div: Int,
    maxWidth: Int
) {
    val space = (maxWidth - lineData.lineWidth) / (lineData.list.size - 1)
    val start = 0
    var childX = start
    lineData.list.forEach {
        it.place(childX, childY + (lineData.lineHeight - it.height) / 2)
        childX += it.width + space
    }
}

/**
 * a pojo for save data of each item line
 * @param[lineWidth]  the total width by sum item's width
 * @param[lineHeight] the max height of item
 * @param[list] item's [Placeable] of line for layout
 */
data class FlexLine(
    val lineWidth: Int,
    val lineHeight: Int,
    val list: List<Placeable> = LinkedList()
)

@Preview
@Composable
fun FlexLayoutBetaStartPreview() {
    TestPreview {
        FlexLayoutBeta(modifier = Modifier.padding(8.dp), arrangement = Arrangement.Start) {
            TestItems()
        }
    }
}


@Preview
@Composable
fun FlexLayoutBetaEndPreview() {
    TestPreview {
        FlexLayoutBeta(modifier = Modifier.padding(8.dp), arrangement = Arrangement.End) {
            TestItems()
        }
    }
}

@Preview
@Composable
fun FlexLayoutBetaCenterPreview() {
    TestPreview {
        FlexLayoutBeta(modifier = Modifier.padding(8.dp), arrangement = Arrangement.Center) {
            TestItems()
        }
    }
}

@Preview
@Composable
fun FlexLayoutBetaAroundPreview() {
    TestPreview {
        FlexLayoutBeta(
            modifier = Modifier.padding(8.dp),
            arrangement = Arrangement.SpaceAround
        ) {
            TestItems()
        }
    }
}

@Preview
@Composable
fun FlexLayoutBetaEvenlyPreview() {
    TestPreview {
        FlexLayoutBeta(
            modifier = Modifier.padding(8.dp),
            arrangement = Arrangement.SpaceEvenly
        ) {
            TestItems()
        }
    }
}

@Preview
@Composable
fun FlexLayoutBetaBetweenPreview() {
    TestPreview {
        FlexLayoutBeta(
            modifier = Modifier.padding(8.dp),
            arrangement = Arrangement.SpaceBetween
        ) {
            TestItems()
        }
    }
}