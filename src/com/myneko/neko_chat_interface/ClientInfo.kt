package com.myneko.neko_chat_interface

import java.net.InetAddress

class ClientInfo {
    // 用户Id
	@JvmField
	var userId: String? = null

    // 客户端IP地址
	@JvmField
	var address: InetAddress? = null

    // 客户端端口号
	@JvmField
	var port = 0
}