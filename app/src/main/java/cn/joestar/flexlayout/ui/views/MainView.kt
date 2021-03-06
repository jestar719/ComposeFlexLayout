package cn.joestar.flexlayout.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.regex.Pattern

@Composable
fun BottomBar(onInputChanged: (length: String, count: String) -> Unit) {
    val maxLength = 10
    val maxCount = 100
    var count by remember {
        mutableStateOf("")
    }
    var length by remember {
        mutableStateOf("")
    }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp, 16.dp, 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NumberInput(length,
            label = "MaxLength",
            onValueChanged = {
                var numFilter = numFilter(it)
                if (numFilter.isNotEmpty()) {
                    numFilter = minOf(numFilter.toInt(), maxLength).toString()
                }
                length = numFilter
                onInputChanged(length, count)
            })
        NumberInput(count,
            label = "Item Count",
            onValueChanged = {
                var numFilter = numFilter(it)
                if (numFilter.isNotEmpty()) {
                    numFilter = minOf(numFilter.toInt(), maxCount).toString()
                }
                count = numFilter
                onInputChanged(length, count)
            })
    }
}

@Composable
fun NumberInput(value: String, label: String, onValueChanged: (String) -> Unit) {
    val keyboardOptions =
        KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done)
    val focusManager = LocalFocusManager.current
    TextField(
        value = value,
        onValueChange = {
            onValueChanged(it)
        },
        modifier = Modifier
            .width(96.dp),
        placeholder = {
            Text(
                text = label,
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        textStyle = TextStyle(textAlign = TextAlign.Center)
    )
}

fun numFilter(string: String): String {
    val regEx = "[^0-9]"
    val p = Pattern.compile(regEx)
    return p.matcher(string).replaceAll("").trim()
}

@Composable
fun FlexTopBar(
    isSingleSelect: Boolean,
    types: List<String>,
    onTypeSelect: (String) -> Unit,
    onSingleChange: (Boolean) -> Unit
) {
    val expend = remember {
        mutableStateOf(false)
    }
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = { Text(text = "FlexLayoutDemo") },
        actions = {
            Button(onClick = { onSingleChange(!isSingleSelect) }) {
                Text(text = if (isSingleSelect) "??????" else "??????")
            }
            IconButton(onClick = { expend.value = !expend.value }) {
                Icon(Icons.Default.MoreVert, contentDescription = "more")
            }
            DropdownMenu(expanded = expend.value, onDismissRequest = { expend.value = false }) {
                types.forEach {
                    DropdownMenuItem(onClick = {
                        expend.value = false
                        onTypeSelect(it)
                    }) {
                        Text(text = it)
                    }
                }
            }
        }
    )
}

@Composable
fun MainView(
    color: Color,
    isSingle: Boolean,
    list: List<String>,
    onInputChanged: (String, String) -> Unit,
    onTypeSelect: (String) -> Unit,
    onSingleChanged: (Boolean) -> Unit,
    onCreateItems: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { FlexTopBar(isSingle, list, onTypeSelect, onSingleChanged) },
        bottomBar = {
            BottomAppBar(
                cutoutShape = RoundedCornerShape(50)
            ) {
                BottomBar(onInputChanged)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateItems,
                backgroundColor = color,
                shape = RoundedCornerShape(50),
            ) {
                Icon(Icons.Default.Create, contentDescription = "add", tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        content = content
    )
}

