package com.kevin.autolist

import java.lang.StringBuilder

class LayoutInsertWriteJoin(private val decName :String,
                            joinPriority: Int) : BaseInsertWriteJoin(joinPriority) {

    override fun insertCode(tab: String, tagNo: Char): String {
        return when(tagNo) {
            '0' -> defineRefreshStr(tab)
            '1' -> addListFuncStr(tab)
            '2' -> addRefreshFuncStr(tab)
            else -> ""
        }
    }

    private fun defineRefreshStr(tab: String): String {
        return if (ConfigBean.hasPullRefresh) {
            if (ConfigBean.isKt) {
                "${tab}private lateinit var mSwipeLayout: SwipeRefreshLayout"
            } else {
                "${tab}private SwipeRefreshLayout mSwipeLayout;"
            }
        } else {
            ""
        }
    }

    private fun addListFuncStr(tab: String): String {
        val sb = StringBuilder()
        if (ConfigBean.hasItemDecoration) {
            if (ConfigBean.isKt) {
                sb.append(tab).append("mList.addItemDecoration(${decName}(0, 0))\n")
            } else {
                sb.append(tab).append("mList.addItemDecoration(new ${decName}(0, 0));\n")
            }
        }
        if (ConfigBean.hasPullRefresh) {
            if (ConfigBean.isKt) {
                sb.append(tab).append("mSwipeLayout = findViewById(R.id.srl_layout)\n")
                sb.append(tab).append("mSwipeLayout.setOnRefreshListener(this)\n")
            } else {
                sb.append(tab).append("mSwipeLayout = findViewById(R.id.srl_layout);\n")
                sb.append(tab).append("mSwipeLayout.setOnRefreshListener(this);\n")
            }
        }
        return sb.toString()
    }

    private fun addRefreshFuncStr(tab: String): String {
        val sb = StringBuilder()
        if (ConfigBean.hasPullRefresh) {
            if (ConfigBean.isKt) {
                sb.append(tab).append("private void refreshOver() {\n")
                sb.append(tab).append("    mSwipeLayout.setRefreshing(false);\n")
                sb.append(tab).append("}\n")
                sb.append(tab).append("\n")
                sb.append(tab).append("@Override\n")
                sb.append(tab).append("public void onRefresh() { }\n")
            } else {
                sb.append(tab).append("private fun refreshOver() {\n")
                sb.append(tab).append("    mSwipeLayout.isRefreshing = false\n")
                sb.append(tab).append("}\n")
                sb.append(tab).append("\n")
                sb.append(tab).append("override fun onRefresh() { }\n")
            }
        }
        return sb.toString()
    }

}