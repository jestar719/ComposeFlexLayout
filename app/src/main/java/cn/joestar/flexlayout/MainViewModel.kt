package cn.joestar.flexlayout

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import cn.joestar.flexlayout.ui.views.*
import java.util.*

class MainViewModel : ViewModel() {
    val arrangementState: MutableState<IFlexArrangement> = mutableStateOf(FlexArrangementCenter())
    val itemNames: MutableState<List<String>> = mutableStateOf(emptyList())
    val types = arrayListOf(
        "Start",
        "End",
        "Center",
        "Evenly",
        "Around",
        "Between"
    )
    private val typeMap: Map<String, IFlexArrangement> by lazy {
        mapOf(
            "Start" to FlexArrangementStart(),
            "End" to FlexArrangementEnd(),
            "Center" to FlexArrangementCenter(),
            "Evenly" to FlexArrangementEvenly(),
            "Around" to FlexArrangementAround(),
            "Between" to FlexArrangementBetween()
        )
    }

    var mLength = 5
    var mCount = 30
    fun onAction(length: String, count: String) {
        mLength = convert2Int(length)
        mCount = convert2Int(count)
    }

    private fun convert2Int(str: String): Int {
        return if (str.isEmpty()) 1 else str.toInt()
    }


    fun onTypeSelect(type: String) {
        arrangementState.value = typeMap[type]!!
    }

    fun onClick() {
        val list = LinkedList<String>()
        repeat(mCount) {
            val text = if (mLength == 1) "A" else getRandomText(mLength)
            list.add(text)
        }
        itemNames.value = list
    }

    fun onSingleSelect(index: Int): String {
        return itemNames.value[index]
    }


    fun onMultiSelect(sequence: String): String {
        val value = sequence.asSequence().mapIndexed { index, c ->
            if (c == '1') itemNames.value[index] else ""
        }.filter {
            it.isNotEmpty()
        }.toList().toString()
        return "$value was selected"
    }
}