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