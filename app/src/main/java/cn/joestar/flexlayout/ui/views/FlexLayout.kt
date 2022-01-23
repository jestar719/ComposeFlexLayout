package cn.joestar.flexlayout.ui.views

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

@Composable
fun FlexLayout(
    modifier: Modifier,
    divider: Dp = 4.dp,
    arrangement: IFlexArrangement = FlexArrangementCenter(),
    content: @Composable () -> Unit
) {
    val policy = MeasurePolicy { measurables, constraints ->
        val childConstraints = Constraints(0, constraints.maxWidth, 0, constraints.maxHeight)
        val div = divider.toPx().toInt()
        val maxWidth = constraints.maxWidth
        var lineHeight = 0
        var lineWidth = 0
        val lines = LinkedList<FlexLine>()
        var temp = LinkedList<Placeable>()
        var width = 0
        var height = 0
        measurables.forEach {
            val placeable = it.measure(childConstraints)
            val pWidth = placeable.width
            if (pWidth + width > maxWidth) {
                lines.add(FlexLine(lineWidth, lineHeight, temp))
                temp = LinkedList<Placeable>()
                height += lineHeight + div
                width = 0
                lineWidth = 0
                lineHeight = 0
            }
            temp.add(placeable)
            lineHeight = maxOf(lineHeight, placeable.height)
            width += pWidth + div
            lineWidth += pWidth
        }
        lines.add(FlexLine(lineWidth, lineHeight, temp))
        height += lineHeight
        layout(constraints.maxWidth, height) {
            var childY = 0
            val action = { placeable: Placeable, x: Int, y: Int -> placeable.place(x, y) }
            lines.forEach { lineData ->
                arrangement.flexArrange(lineData, childY, div, maxWidth, action)
                childY += lineData.lineHeight + div
            }
        }
    }
    //  create compose with use measurePolicy to measure and layout content
    Layout(content = content, modifier = modifier, measurePolicy = policy)
}

/**
 * A strategy to help item layout. it computer and provider item's x,y
 */
interface IFlexArrangement {
    /**
     * call for layout each item.
     * @param[lineData]  the date of every line, see [FlexLine]
     * @param[top]  top of line for [Placeable] will layout
     * @param[div] min divider between two [Placeable].
     * @param[maxWidth] width limit of line by parent
     * @param[action] the function call to layout item. need call when iterate[FlexLine.list]
     */
    fun flexArrange(
        lineData: FlexLine,
        top: Int,
        div: Int,
        maxWidth: Int,
        action: (placeable: Placeable, x: Int, y: Int) -> Unit
    )
}

/**
 * The abstract class as [IFlexArrangement] child
 *
 * Fixed item layout flow, child just provide value of start and space
 *
 * Provide default vertical strategy
 */
abstract class AbsFlexArrangement : IFlexArrangement {
    /**
     * Implementation [IFlexArrangement] to fix layout flow
     * @see [IFlexArrangement]
     */
    override fun flexArrange(
        line: FlexLine,
        top: Int,
        div: Int,
        maxWidth: Int,
        action: (placeable: Placeable, x: Int, y: Int) -> Unit
    ) {
        val space = computerSpace(line, maxWidth, div)
        val start = computerStart(line, maxWidth, space)
        var x = start
        line.list.forEach {
            action(it, x, top + computerOffsetY(it.height, line.lineHeight))
            x += it.width + space
        }
    }

    /**
     * computer offset of line top, default strategy is CenterVertical
     *
     * It can be override if need
     *
     * @param height item's height
     * @param linHeight the height of line
     */
    protected open fun computerOffsetY(height: Int, linHeight: Int): Int = (linHeight - height) / 2

    /**
     * computer the start of line. it means the firs item's x or margin start
     * @param line  the data of line
     * @param maxWidth the maxWidth of line
     * @param space  the divider between two item. get by [computerSpace]
     */
    protected abstract fun computerStart(line: FlexLine, maxWidth: Int, space: Int): Int

    /**
     *computer the divider between two item
     * @param line  the data of line
     * @param maxWidth the maxWidth of line
     * @param div  the min divider between two item
     */
    protected abstract fun computerSpace(line: FlexLine, maxWidth: Int, div: Int): Int
}

/***
 * layout item by start,space is div
 */
class FlexArrangementStart : AbsFlexArrangement() {

    override fun computerStart(line: FlexLine, maxWidth: Int, space: Int): Int = 0

    override fun computerSpace(line: FlexLine, maxWidth: Int, div: Int): Int = div
}

/***
 * layout item by end,space is div
 */
class FlexArrangementEnd : AbsFlexArrangement() {

    override fun computerStart(line: FlexLine, maxWidth: Int, space: Int): Int {
        return (maxWidth - (line.lineWidth + (line.list.size - 1) * space))
    }

    override fun computerSpace(line: FlexLine, maxWidth: Int, div: Int): Int = div
}

/***
 * layout items at center,space is div
 */
class FlexArrangementCenter : AbsFlexArrangement() {
    override fun computerStart(line: FlexLine, maxWidth: Int, space: Int): Int {
        return (maxWidth - line.lineWidth - (line.list.size - 1) * space) / 2
    }

    override fun computerSpace(line: FlexLine, maxWidth: Int, div: Int) = div
}

/**
 * layout items at center,space count is item count -1
 * the margin of start and end is same of space
 */
class FlexArrangementEvenly : AbsFlexArrangement() {
    override fun computerStart(line: FlexLine, maxWidth: Int, space: Int) = space
    override fun computerSpace(line: FlexLine, maxWidth: Int, div: Int): Int {
        return (maxWidth - line.lineWidth) / (line.list.size + 1)
    }
}

/***
 * layout items at center,space count is item count-1
 * but the margin of start and end is half of space
 */
class FlexArrangementAround : AbsFlexArrangement() {
    override fun computerStart(line: FlexLine, maxWidth: Int, space: Int) = space / 2
    override fun computerSpace(line: FlexLine, maxWidth: Int, div: Int): Int {
        return (maxWidth - line.lineWidth) / line.list.size
    }
}

/**
 *  the first item layout start,last item layout end
 *  space count is item count -1
 */
class FlexArrangementBetween : AbsFlexArrangement() {
    override fun computerStart(line: FlexLine, maxWidth: Int, space: Int) = 0

    override fun computerSpace(line: FlexLine, maxWidth: Int, div: Int): Int {
        return (maxWidth - line.lineWidth) / (line.list.size - 1)
    }
}


@Preview
@Composable
fun FlexLayoutStartPreview() {
    TestPreview {
        FlexLayout(modifier = Modifier.padding(8.dp), arrangement = FlexArrangementStart()) {
            TestItems()
        }
    }
}


@Preview
@Composable
fun FlexLayoutEndPreview() {
    TestPreview {
        FlexLayout(modifier = Modifier.padding(8.dp), arrangement = FlexArrangementEnd()) {
            TestItems()
        }
    }
}

@Preview
@Composable
fun FlexLayoutCenterPreview() {
    TestPreview {
        FlexLayout(modifier = Modifier.padding(8.dp), arrangement = FlexArrangementCenter()) {
            TestItems()
        }
    }
}

@Preview
@Composable
fun FlexLayoutAroundPreview() {
    TestPreview {
        FlexLayout(
            modifier = Modifier.padding(8.dp),
            arrangement = FlexArrangementAround()
        ) {
            TestItems()
        }
    }
}

@Preview
@Composable
fun FlexLayoutEvenlyPreview() {
    TestPreview {
        FlexLayout(
            modifier = Modifier.padding(8.dp),
            arrangement = FlexArrangementEvenly()
        ) {
            TestItems()
        }
    }
}

@Preview
@Composable
fun FlexLayoutBetweenPreview() {
    TestPreview {
        FlexLayout(
            modifier = Modifier.padding(8.dp),
            arrangement = FlexArrangementBetween()
        ) {
            TestItems()
        }
    }
}

