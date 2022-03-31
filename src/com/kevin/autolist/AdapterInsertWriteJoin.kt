package com.kevin.autolist

import java.lang.StringBuilder

class AdapterInsertWriteJoin(private val itemXmlName :String?,
                             private val viewMap: Map<String, String>,
                             joinPriority: Int) : BaseInsertWriteJoin(joinPriority) {

    override fun insertCode(tab: String, tagNo: Char): String {
        return when(tagNo) {
            '0' -> layoutResStr(tab)
            '1' -> defineViewsStr(tab)
            '2' -> findViewsStr(tab)
            else -> ""
        }
    }

    private fun layoutResStr(tab: String): String {
        return if (itemXmlName.isNullOrEmpty()) {
            ""
        } else {
            if (ConfigBean.isKt) {
                "${tab}return R.layout.${itemXmlName}\n"
            } else {
                "${tab}return R.layout.${itemXmlName};\n"
            }
        }
    }

    private fun defineViewsStr(tab: String): String {
        if (viewMap.isNullOrEmpty()) {
            return ""
        }
        val sb = StringBuilder()
        if (ConfigBean.isKt) {
            for (entry in viewMap) {
                sb.append(tab).append("private var m${entry.key}: ${entry.key}? = null\n")
            }
        } else {
            for (entry in viewMap) {
                sb.append(tab).append("private ${entry.key} m${entry.key};\n")
            }
        }
        return sb.toString()
    }

    private fun findViewsStr(tab: String): String {
        if (viewMap.isNullOrEmpty()) {
            return ""
        }
        val sb = StringBuilder()
        if (ConfigBean.isKt) {
            for (entry in viewMap) {
                sb.append(tab).append("m${entry.key} = itemView.findViewById(R.id.${entry.value})\n")
            }
        } else {
            for (entry in viewMap) {
                sb.append(tab).append("m${entry.key} = itemView.findViewById(R.id.${entry.value});\n")
            }
        }
        return sb.toString()
    }

}