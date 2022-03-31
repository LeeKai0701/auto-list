package com.kevin.autolist

import java.lang.StringBuilder

abstract class BaseInsertWriteJoin(private var joinPriority: Int): WriteJoin {

    companion object {
        private const val INSERT_CHECK = ">"
    }

    override fun getPriority(): Int {
        return joinPriority
    }

    override fun onWriteLine(line: String): Pair<String, Boolean> {
        if (line.startsWith(INSERT_CHECK)) {
            val endIndex = line.indexOf("#")
            val tagNoChar = line[endIndex - 1]
            return Pair(insertCode(getTabString(endIndex), tagNoChar), true)
        }
        return Pair(line, false)
    }

    private fun getTabString(endIndex: Int): String {
        val sb = StringBuilder()
        for (i in 0..endIndex) {
            sb.append(" ")
        }
        return sb.toString()
    }

    abstract fun insertCode(tab: String, tagNo: Char): String

}