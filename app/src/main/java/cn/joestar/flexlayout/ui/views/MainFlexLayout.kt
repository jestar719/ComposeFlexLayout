package cn.joestar.flexlayout.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun MainFlexLayout(color: Color, arrangement: IFlexArrangement, texts: List<String>) {
    FlexLayout(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp), arrangement = arrangement) {
        var select by remember {
            mutableStateOf(-1)
        }
        texts.forEachIndexed { index, s ->
            Text(
                text = s,
                color = if (index == select) color else Color.Gray,
                modifier = Modifier.clickable {
                    select = index
                })
        }
    }
}