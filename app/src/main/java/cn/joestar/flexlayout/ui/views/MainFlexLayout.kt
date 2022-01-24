package cn.joestar.flexlayout.ui.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@ExperimentalMaterialApi
@Composable
fun SingleSelectFlexLayout(
    color: Color,
    arrangement: IFlexArrangement,
    texts: List<String>,
    onItemSelect: (Int) -> Unit
) {
    FlexLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        divider = 8.dp,
        arrangement = arrangement
    ) {
        var select by remember {
            mutableStateOf(-1)
        }
        texts.forEachIndexed { index, s ->
            val enable = index == select
            CardText(index = index, text = s, color = color, enable = enable, onItemSelect = {
                select = index
                onItemSelect(index)
            })
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MultiSelectFlexLayout(
    color: Color,
    arrangement: IFlexArrangement,
    texts: List<String>,
    onItemSelect: (String) -> Unit
) {
    val size = texts.size
    val builder = StringBuilder()
    repeat(size) {
        builder.append("0")
    }
    FlexLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        divider = 8.dp,
        arrangement = arrangement
    ) {
        var select by remember {
            mutableStateOf(builder.toString())
        }
        texts.forEachIndexed { index, s ->
            val enable = index < select.length && select[index] == '1'
            CardText(index = index, text = s, color = color, enable = enable, onItemSelect = {
                select = builder.replace(index, index + 1, if (enable) "0" else "1").toString()
                onItemSelect(select)
            })
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun CardText(index: Int, text: String, color: Color, enable: Boolean, onItemSelect: (Int) -> Unit) {
    val textColor = if (enable) Color.White else Color.Gray
    Card(
        onClick = {
            onItemSelect(index)
        },
        backgroundColor = if (enable) color else Color.White
    ) {
        Text(
            text = text,
            color = textColor,
            modifier = Modifier.padding(4.dp)
        )
    }
}

