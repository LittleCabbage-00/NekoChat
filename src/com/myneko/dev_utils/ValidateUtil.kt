package com.myneko.dev_utils

import java.util.regex.Pattern

/*
 * 正则表达验证
 */
object ValidateUtil {
    //是否为数字
    @JvmStatic
    fun isNumber(str: String?): Boolean? {
        val pattern = Pattern.compile("^[0-9]*$")
        val matcher = str?.let { pattern.matcher(it) }
        return matcher?.matches()

    }

    //是否为字母
    fun isString(str: String?): Boolean? {
        val pattern = Pattern.compile("^[a-zA-Z]*$")
        val matcher = str?.let { pattern.matcher(it) }
        return matcher?.matches()
    }

    //是否为中文
    fun isChinese(str: String?): Boolean? {
        // 编码
        val pattern = Pattern.compile("^[一-龥]*$")
        val matcher = str?.let { pattern.matcher(it) }
        return matcher?.matches()
    }

    @JvmStatic
    fun isIPLegal(ipStr: String): Boolean {
        if (ipStr.trim { it <= ' ' }.isEmpty()) {
            return false
        }
        val pattern =
            Pattern.compile("^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$")
        val matcher = pattern.matcher(ipStr)
        return matcher.find()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        /*
         * 验证部分
         */
        println(isString("javajpo"))
        println(isNumber("1111111"))
        println(isChinese("���"))
    }
}