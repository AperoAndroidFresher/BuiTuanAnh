package com.example.buituananh.util

fun Pair<Int, Int>.formatToString(): String {
    val str = StringBuilder("")
    if(first < 10) {
        str.append("0$first")
    } else {
        str.append(first)
    }
    str.append(":")
    if(second < 10) {
        str.append("0$second")
    } else {
        str.append(second)
    }
    return str.toString()
}
fun Long.toPairDuration(): Pair<Int, Int>  {
    val totalSeconds = this / 1000
    val minutes = (totalSeconds / 60).toInt()
    val seconds = (totalSeconds % 60).toInt()
    return minutes to seconds
}


fun Long.toMinuteSecondString(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
