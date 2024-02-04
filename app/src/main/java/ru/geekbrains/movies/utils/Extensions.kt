package ru.geekbrains.movies.utils

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

fun View.showSnakeBar(text: String, length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, text, length).show()
}

fun View.click(click: () -> Unit) = setOnClickListener { click() }

fun View.longClick(longClick: () -> Boolean) = setOnLongClickListener { longClick() }

fun Fragment.arguments(vararg arguments: Pair<String, Any>): Fragment {
    this.arguments = bundleOf(*arguments)
    return this
}

fun getColorByValue(value: Int): Int =
    try {
        when (value) {
            in 0..25 -> Color.parseColor("#FF0000")
            in 26..50 -> Color.parseColor("#FF8C00")
            in 51..75 -> Color.parseColor("#FFFF00")
            else -> Color.parseColor("#00FF00")
        }
    } catch (err: Exception) {
        Log.d("VVVV", "${err.localizedMessage}")
        0
    }

fun toDateString(value: String): String? =
    try {
        SimpleDateFormat(DATE_FORMAT_IN, Locale.getDefault())
            .parse(value)?.let { date ->
                SimpleDateFormat(DATE_FORMAT_OUT, Locale.getDefault()).format(date)
            }
    } catch (err: Exception) {
        null
    }

fun durationToString(duration: Int): String {
    val hours: Int = duration / 60
    val minutes: Int = duration % 60
    return String.format(TIME_STRING_TEMPLATE, hours, minutes)
}

fun releaseDateToString(date: String): String? {
    return try {
        var result: String = ""
        toDateString(date)?.let { date ->
            date.split(" ").forEach {
                result += it.replaceFirstChar(Char::titlecase) + " "
            }
        }
        result
    } catch (err: Exception) {
        null
    }
}

const val DATE_FORMAT_IN = "yyyy-MM-dd"
const val DATE_FORMAT_OUT = "dd MMMM yyyy"
const val TIME_STRING_TEMPLATE = "%sч %sм"