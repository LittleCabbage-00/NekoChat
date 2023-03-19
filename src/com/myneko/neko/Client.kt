package com.myneko.neko

import com.myneko.neko_ui.LoginActivity
import java.io.IOException
import java.net.DatagramSocket

object Client {
    // 命令代码
	@JvmField
	var socket: DatagramSocket? = null

    // 服务器端IP
	@JvmField
	var client_IP = "192.168.2.1"

    // 服务器端端口号
	@JvmField
	var client_PORT = 2056
    @JvmField
	var tag = 0
    @JvmStatic
	fun start() {
        try {
            // 创建DatagramSocket对象，由系统分配可以使用的端口
            socket = DatagramSocket()
            // 设置超时5秒，不再等待接收数据
            socket!!.soTimeout = 5000
            println("客户端运行...")
            val frame = LoginActivity()
            frame.isVisible = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}