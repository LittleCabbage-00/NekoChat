package com.myneko.dev_utils

import java.util.regex.Pattern

object ChatActivityUtil {
    @JvmStatic
	fun isFilelink(path: String): Boolean {
        //这个表达式可以验证带有window非法字符的文件路径，以及带有多个连续“/”的无意义的路径。不足之处是末尾不能有“/”。
        //^[a-zA-Z]:(//[^///:"<>/|]+)+$
        if (path.trim { it <= ' ' }.isEmpty()) {
            return false
        }
        val pattern = Pattern.compile("^[A-z]:\\\\\\S+$")
        val matcher = pattern.matcher(path)
        return matcher.find()
    }
}