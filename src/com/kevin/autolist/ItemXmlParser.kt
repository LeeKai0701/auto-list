package com.kevin.autolist

import org.w3c.dom.Node
import org.xml.sax.SAXException
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

class ItemXmlParser {

    fun parseElement(filePath: String?): Map<String, String>{
        val map = mutableMapOf<String, String>()
        if (filePath.isNullOrEmpty()) {
            return map
        }
        val factory = DocumentBuilderFactory.newInstance()
        try {
            val builder = factory.newDocumentBuilder()
            val document = builder.parse(filePath)
            val elements = document.documentElement
            handleNode(elements, map)
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        } catch (e: SAXException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return map
    }

    private fun handleNode(node: Node?, map: MutableMap<String, String>) {
        if (node == null || node.nodeType != Node.ELEMENT_NODE) {
            return
        }
        val viewName = node.nodeName
        val attrs = node.attributes
        if (attrs != null) {
            val idNode = attrs.getNamedItem("android:id")
            if (idNode.nodeType == Node.ATTRIBUTE_NODE) {
                val viewId = idNode.nodeValue
                if (!viewName.isNullOrEmpty() && !viewId.isNullOrEmpty()) {
                    map[getSimpleClassName(viewName)] = getViewId(viewId)
                }
            }
        }
        val children = node.childNodes
        if (children != null) {
            val len = children.length
            for (i in 0 until len) {
                handleNode(children.item(i), map)
            }
        }
    }

    private fun getSimpleClassName(origin: String): String {
        val indexDot = origin.lastIndexOf('.')
        if (indexDot >= 0 && indexDot < origin.length) {
            return origin.substring(indexDot + 1, origin.length)
        }
        return origin
    }

    private fun getViewId(attrValue: String): String {
        val index = attrValue.lastIndexOf('/')
        if (index >= 0 && index < attrValue.length) {
            return attrValue.substring(index + 1, attrValue.length)
        }
        return attrValue
    }



}