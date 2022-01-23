package cn.joestar.flexlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cn.joestar.flexlayout.ui.theme.FlexLayoutTheme
import cn.joestar.flexlayout.ui.views.MainFlexLayout
import cn.joestar.flexlayout.ui.views.MainView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlexLayoutTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val model = viewModels<MainViewModel>().value
                    MainView(
                        color = MaterialTheme.colors.primary,
                        list = model.types,
                        onAction = model::onAction,
                        onTypeSelect = model::onTypeSelect,
                        onClick = model::onClick
                    ) {
                        MainFlexLayout(
                            color = MaterialTheme.colors.primary,
                            arrangement = model.arrangementState.value,
                            texts = model.itemNames.value
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlexLayoutTheme {
        Greeting("Android")
    }
}