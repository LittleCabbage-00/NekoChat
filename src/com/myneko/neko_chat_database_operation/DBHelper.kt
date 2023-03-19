package com.myneko.neko_chat_database_operation

import java.io.IOException
import java.sql.*
import java.util.*

class DBHelper {

    companion object {
        // 连接数据库url
        private var url: String? = null

        // 创建Properties对象
        private var info = Properties()

        // 1.驱动程序加载
        init {
            // 获得属性文件输入流
            val input = DBHelper::class.java.classLoader
                .getResourceAsStream("com/myneko/neko_chat_database_operation/config.properties")
            try {
                // 加载属性文件内容到Properties对象
                info.load(input)
                // 从属性文件中取出url
                url = info.getProperty("url")
                // Class.forName("com.mysql.jdbc.Driver");
                // 从属性文件中取出driver
                val driverClassName = info.getProperty("driver")
                Class.forName(driverClassName)
                println("驱动程序加载成功...")
            } catch (e: ClassNotFoundException) {
                println("驱动程序加载失败...")
            } catch (e: IOException) {
                println("加载属性文件失败...")
            }
        }

        @get:Throws(SQLException::class)
        val connection: Connection
            get() =// 创建数据库连接
                DriverManager.getConnection(url, info)
    }
}