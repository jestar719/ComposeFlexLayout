package cn.joestar.flexlayout

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cn.joestar.flexlayout.ui.theme.FlexLayoutTheme
import cn.joestar.flexlayout.ui.views.MainView
import cn.joestar.flexlayout.ui.views.MultiSelectFlexLayout
import cn.joestar.flexlayout.ui.views.SingleSelectFlexLayout

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlexLayoutTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val model = viewModels<MainViewModel>().value
                    var single by remember {
                        mutableStateOf(true)
                    }
                    MainView(
                        color = MaterialTheme.colors.primary,
                        single = single,
                        list = model.types,
                        onAction = model::onAction,
                        onTypeSelect = model::onTypeSelect,
                        onClick = model::onClick,
                        onSingleChange = { single = it },
                    ) {
                        val color = MaterialTheme.colors.primary
                        if (single) {
                            SingleSelectFlexLayout(
                                color = color,
                                arrangement = model.arrangementState.value,
                                texts = model.itemNames.value,
                                onItemSelect = {
                                    Log.d("SingleSelect", model.onSingleSelect(it))
                                }
                            )
                        } else {
                            MultiSelectFlexLayout(
                                color = color,
                                arrangement = model.arrangementState.value,
                                texts = model.itemNames.value
                            ) {
                                Log.d("MultiSelect", model.onMultiSelect(it))
                            }
                        }
                    }
                }
            }
        }
    }
}
