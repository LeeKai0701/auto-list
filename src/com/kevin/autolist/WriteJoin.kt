package com.kevin.autolist

interface WriteJoin {

    fun getPriority(): Int // 数字越小，优先级越高
    fun onWriteLine(line: String): Pair<String, Boolean>

}