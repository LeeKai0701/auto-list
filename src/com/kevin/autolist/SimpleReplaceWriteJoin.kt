package com.kevin.autolist

class SimpleReplaceWriteJoin(private val origin: String,
                             private val target: String,
                             private val checkStr: String,
                             private var joinPriority: Int): WriteJoin {

    override fun getPriority(): Int {
        return joinPriority
    }

    override fun onWriteLine(line: String): Pair<String, Boolean> {
        if (line.startsWith(checkStr)) {
            val result = line.replace(origin, target)
            return Pair(result.substring(checkStr.length), true)
        }
        return Pair(line, false)
    }

}