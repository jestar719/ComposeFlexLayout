package cn.joestar.flexlaout.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.unit.Constraints

@Composable
fun SimpleCustomLayout(modifier: Modifier, content: @Composable () -> Unit) {
    val policy = MeasurePolicy { measurables, constraints ->
        val childConstraints = Constraints(0, constraints.maxWidth, 0, constraints.maxHeight)
        var height = 0
        val placeable = measurables.map {
            it.measure(childConstraints)
        }
        // TODO: computer height with child's placeable
        layout(constraints.maxWidth, height) {
            var childX = 0
            var childY = 0
            placeable.forEach {
                // TODO:  computer child rect x and y and use place method to layout it
                it.place(childX, childY)
            }
        }
    }
    //  create compose with use measurePolicy to measure and layout content
    Layout(content = content, modifier = modifier, measurePolicy = policy)
}