package com.myneko.neko

import com.myneko.neko_chat_interface.ClientInfo
import com.myneko.dev_operation.WriteErrorLog
import com.myneko.neko_chat_database_operation.OnlineInfoDB
import com.myneko.dev_method.UserDao
import org.json.JSONObject
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.UnknownHostException
import java.sql.SQLException
import java.util.concurrent.CopyOnWriteArrayList

object Server {
    // 命令代码
    const val COMMAND_LOGIN = 1 // 登录命令
    const val COMMAND_LOGOUT = 2 // 注销命令
    const val COMMAND_SENDMSG = 3 // 发消息命令
    const val COMMAND_GROUPCHART = 4 // 群发
    var SERVER_PORT = 7788

    // 所有已经登录的客户端信息
	@JvmField
	var clientList: MutableList<ClientInfo> = CopyOnWriteArrayList()

    // 创建数据访问对象
    var dao = UserDao()
    @JvmStatic
	@Throws(SQLException::class, UnknownHostException::class)
    fun Start() {
        // 获取端口
        SERVER_PORT = ServerCLI.clientinfo.port
        // 初始化
        val list = OnlineInfoDB().onLineInfo
        clientList = list.toMutableList()
        println(SERVER_PORT)
        System.out.printf("服务器启动, 监听自己的端口%d...\n", SERVER_PORT)
        val buffer = ByteArray(2048)
        try {
            DatagramSocket(SERVER_PORT).use { socket ->
                while (true) {
                    println("服务器端监听端口：" + SERVER_PORT)
                    var packet = DatagramPacket(buffer, buffer.size)
                    socket.receive(packet)
                    // 接收数据长度
                    val len = packet.length
                    val str = String(buffer, 0, len)
                    // 从客户端传来的数据包中得到客户端地址
                    val address = packet.address
                    // System.out.println(address);
                    // 从客户端传来的数据包中得到客户端端口号
                    val port = packet.port
                    // System.out.println(port);
                    var jsonObj = JSONObject(str)
                    println(jsonObj)
                    val cmd = jsonObj["command"] as Int
                    if (cmd == COMMAND_LOGIN) { // 用户登录过程
                        // 通过用户Id查询用户信息
                        val userId = jsonObj["user_id"] as String
                        val user: Map<String, String>? = dao.findById(userId)

                        // 判断客户端发送过来的密码与数据库的密码是否一致
                        if (user != null && jsonObj["user_pwd"] == user["user_pwd"]) {
                            val sendJsonObj = JSONObject(user)
                            // 添加result:0键值对，0表示成功，-1表示失败
                            sendJsonObj.put("result", 0)

                            // 封装信息
                            val cInfo = ClientInfo()
                            cInfo.userId = userId
                            cInfo.address = address
                            cInfo.port = port
                            clientList.plus(cInfo)
                            // 将信息保存到数据库中
                            OnlineInfoDB().addOnLineInfo(cInfo)

                            // 取出好友用户列表
                            val friends: List<MutableMap<String, String>> = dao.findFriends(userId)

                            // 设置好友状态，更新friends集合，添加online字段
                            for (friend in friends) {
                                val fid = friend["user_id"]
                                val state = friend["state"]
                                println("添加好友状态$fid")
                                if (fid == "1024" || state == "1") {
                                    // 添加好友状态 1在线 0离线
                                    friend["online"] = "1"
                                } else {
                                    // 添加好友状态 1在线 0离线
                                    friend["online"] = "0"
                                    // 好友在clientList集合中存在，则在线
                                    for (c in clientList) {
                                        val uid = c.userId
                                        // 好友在线
                                        if (uid == fid) {
                                            // 更新好友状态 1在线 0离线
                                            friend["online"] = "1"
                                            break
                                        }
                                    }
                                }
                            }
                            sendJsonObj.put("friends", friends)

                            // 创建DatagramPacket对象，用于向客户端发送数据
                            val b = sendJsonObj.toString().toByteArray()
                            packet = DatagramPacket(b, b.size, address, port)
                            socket.send(packet)

                            // 广播当前用户上线了
                            for (info in clientList) {
                                // 给其他好友发送，当前用户上线消息
                                if (info.userId != userId) {
                                    jsonObj = JSONObject()
                                    jsonObj.put("user_id", userId)
                                    jsonObj.put("online", "1")
                                    val b2 = jsonObj.toString().toByteArray()
                                    packet = DatagramPacket(b2, b2.size, info.address, info.port)
                                    // 转发给好友
                                    socket.send(packet)
                                }
                            }
                        } else {
                            // 送失败消息
                            val sendJsonObj = JSONObject()
                            sendJsonObj.put("result", -1)
                            val b = sendJsonObj.toString().toByteArray()
                            packet = DatagramPacket(b, b.size, address, port)
                            // 向请求登录的客户端发送数据
                            socket.send(packet)
                        }
                    } else if (cmd == COMMAND_SENDMSG) { // 用户发送消息
                        // 获得好友Id
                        val friendUserId = jsonObj["receive_user_id"] as String

                        // 向客户端发送数据
                        for (info in clientList) {
                            // 找到好友的IP地址和端口号
                            if (info.userId == friendUserId) {
                                jsonObj.put("OnlineUserList", userOnlineStateList)

                                // 创建DatagramPacket对象，用于向客户端发送数据
                                val b = jsonObj.toString().toByteArray()
                                println(info.address)
                                packet = DatagramPacket(b, b.size, info.address, info.port)
                                // 转发给好友
                                socket.send(packet)
                                break
                            }
                        }
                    } else if (cmd == COMMAND_LOGOUT) { // 用户发送注销命令

                        // 获得用户Id
                        val userId = jsonObj["user_id"] as String

                        // 从clientList集合中删除用户
                        for (info in clientList) {
                            if (info.userId == userId) {
                                clientList.remove(info)
                                break
                            }
                        }

                        // 向其他客户端广播该用户下线
                        for (info in clientList) {
                            jsonObj = JSONObject()
                            jsonObj.put("user_id", userId)
                            jsonObj.put("online", "0")
                            val b2 = jsonObj.toString().toByteArray()
                            packet = DatagramPacket(b2, b2.size, info.address, info.port)
                            socket.send(packet)
                        }
                        // 发送群消息
                    } else if (cmd == COMMAND_GROUPCHART) {
                        println("群聊")
                        // 分别发消息给1024的好友
                        // 获得好友Id
                        val friendUserId = jsonObj["receive_user_id"] as String // 1024
                        // 获取1024群聊里用户信息
                        val groupChartUsers: List<Map<String, String>> = UserDao().findFriends(friendUserId)
                        System.err.println(groupChartUsers)
                        // 向客户端发送数据
                        for (info in clientList) {
                            System.err.println(info)
                            for (map in groupChartUsers) {
                                if (map["user_id"] == jsonObj["user_id"]) {
                                    // 不用把消息发给自己
                                    continue
                                }
                                // 找到群聊的IP地址和端口号
                                if (info.userId == map["user_id"]) {
                                    jsonObj.put("OnlineUserList", userOnlineStateList)
                                    // 创建DatagramPacket对象，用于向客户端发送数据
                                    val b = jsonObj.toString().toByteArray()
                                    println(info.address)
                                    packet = DatagramPacket(b, b.size, info.address, info.port)
                                    // 转发给好友
                                    socket.send(packet)
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: IOException) {
            WriteErrorLog.SaveError("在class Server下的public static void main(String[] args)中出现异常,异常信息:$e")
            e.printStackTrace()
        }
    }

    private val userOnlineStateList: List<Map<String, String?>?>
        // 获得用户在线状态
        get() {
            // 从数据库查询所有用户信息
            val userList = dao.findAll()
            // 保存用户在线状态集合
            val list: MutableList<Map<String, String?>?> = ArrayList()
            for (user in userList) {
                val userId = user["user_id"]
                val map: MutableMap<String, String?> = HashMap()
                map["user_id"] = userId
                // 默认离线
                map["online"] = "0"
                for (info in clientList) {
                    // 如果clientList（已经登录的客户端信息）中有该用户，则该用户在线
                    if (info.userId == userId) {
                        // 设置为在线
                        map["online"] = "1"
                        break
                    }
                }
                list.add(map)
            }
            return list
        }
}