package com.kevin.autolist

import java.io.*
import java.lang.Exception

object ConfigBean {

    var language: String = ".java"
    var isKt = false
    var itemXmlPath: String = ""
    var hasPullRefresh = false
    var layoutXmlName: String = ""
    var hasItemDecoration = false
    var dataBeanName: String = ""
    var bizName: String = ""

    fun parseConfig(configFilePath: String): Boolean {
        val configFile = File(configFilePath)
        if (!configFile.exists()) {
            println("ConfigManager: 配置文件不存在")
            return false
        }
        var bufferReader: BufferedReader? = null
        try {
            bufferReader = BufferedReader(InputStreamReader(FileInputStream(configFile)))
            var lineStr: String?
            while (bufferReader.readLine().also { lineStr = it } != null) {
                handleLine(lineStr!!)
            }
            return true
        } catch (e: Exception) {
            println("ConfigManager: 配置读取失败")
        } finally {
            try {
                bufferReader?.close()
            } catch (e: Exception) {
                println("ConfigManager: 配置流关闭失败")
            }
        }
        return false
    }

    private fun handleLine(lineStr: String) {
        if (lineStr.isEmpty() || lineStr.startsWith("#")) {
            return
        }
        val configPair = lineStr.split("=")
        if (configPair.size == 2 && configPair[0].isNotEmpty() && configPair[1].isNotEmpty()) {
            saveConfig(configPair[0], configPair[1])
        }
    }

    private fun saveConfig(key: String, value: String) {
        when (key)  {
            "language" -> {
                if ("kotlin" == value.toLowerCase()) {
                    language = ".kt"
                    isKt = true
                } else {
                    language = ".java"
                    isKt = false
                }
            }
            "itemXmlPath" -> {
                itemXmlPath = value
            }
            "hasPullRefresh" -> {
                hasPullRefresh = value.toLowerCase() == "true"
            }
            "layoutXmlName" -> {
                layoutXmlName = value
            }
            "hasSpaceDecoration" -> {
                hasItemDecoration = value.toLowerCase() == "true"
            }
            "dataBeanName" -> {
                dataBeanName = value
            }
            "bizName" -> {
                bizName = value
            }
        }
    }

    override fun toString(): String {
        return "language:$language, itemXmlPath:$itemXmlPath, hasPullRefresh:$hasPullRefresh, layoutXmlName:$layoutXmlName,\n" +
                "hasSpaceDecoration:$hasItemDecoration, dataBeanName:$dataBeanName, bizName:$bizName"
    }

}