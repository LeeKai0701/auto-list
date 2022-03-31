package com.kevin.autolist

import java.io.*
import java.lang.Exception

class TemplateWriter(private val templatePath: String, private val outFilePath: String) {

    private val mJoins = mutableListOf<WriteJoin>()

    fun startWork() {
        var bufferReader: BufferedReader? = null
        var bufferWriter: BufferedWriter? = null
        try {
            bufferReader = BufferedReader(InputStreamReader(javaClass.getResourceAsStream(templatePath)))
            bufferWriter = BufferedWriter(OutputStreamWriter(initOutFile(outFilePath)))
            var lineStr: String?
            while (bufferReader.readLine().also { lineStr = it } != null) {
                handleWrite(lineStr!!, bufferWriter)
            }
        } catch (e: Exception) {
            println("TemplateWriter: 模版读取失败")
        } finally {
            try {
                bufferReader?.close()
            } catch (e: Exception) {
                println("TemplateWriter: 读取流关闭失败")
            }
            try {
                bufferWriter?.close()
            } catch (e: Exception) {
                println("TemplateWriter: 写入流关闭失败")
            }
        }
    }

    private fun handleWrite(lineStr: String, bufferWriter: BufferedWriter) {
        var result: Pair<String, Boolean>? = null
        for (join in mJoins) {
            result = join.onWriteLine(lineStr)
            if (result.second) {
                break
            }
        }

        if (result?.second == true) {
            bufferWriter.write(result.first)
        } else {
            bufferWriter.write(lineStr)
        }
        bufferWriter.write("\n")
    }

    fun join(joins: List<WriteJoin>?): TemplateWriter {
        joins?.let {
            it.sortedBy { e -> e.getPriority() }
            mJoins.clear()
            mJoins.addAll(it)
        }
        return this
    }

    fun join(join: WriteJoin?): TemplateWriter {
        join?.let {
            mJoins.add(it)
            mJoins.sortBy { e -> e.getPriority() }
        }
        return this
    }

    private fun initOutFile(str: String): FileOutputStream {
        val file = File(str)
        if (file.exists()) {
            file.delete()
        }
        return FileOutputStream(file)
    }

}