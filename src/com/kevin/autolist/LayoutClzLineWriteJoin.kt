package com.kevin.autolist

class LayoutClzLineWriteJoin(private val checkStr: String,
                             private val origin: String,
                             private val target: String) : WriteJoin {

    override fun getPriority(): Int {
        return 0
    }

    override fun onWriteLine(line: String): Pair<String, Boolean> {
        if (line.startsWith(checkStr)) {
            var result = line.replace(origin, target)
            if (ConfigBean.hasPullRefresh) {
                if (ConfigBean.isKt) {
                    result = result.replace("{", ", SwipeRefreshLayout.OnRefreshListener {")
                } else {
                    result = result.replace("{", "implements SwipeRefreshLayout.OnRefreshListener {")
                }
            }
            return Pair(result.substring(checkStr.length), true)
        }
        return Pair(line, false)
    }

}