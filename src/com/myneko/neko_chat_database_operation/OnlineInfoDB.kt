package com.myneko.neko_chat_database_operation

import com.myneko.neko_chat_interface.ClientInfo
import com.myneko.dev_operation.WriteErrorLog
import java.net.InetAddress
import java.net.UnknownHostException
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class OnlineInfoDB {
    // 用户登入将IP和端口号放入到数据库中去
    fun addOnLineInfo(clientInfo: ClientInfo): Boolean {
        val sql = "insert into nekodb.online_info(user_id,ip,port) values(?,?,?)"
        val i: Int
        //创建SQL语句
        val pstmt: PreparedStatement
        try {
            //创建连接
            val conn = DBHelper.connection
            //初始化参数
            pstmt = conn.prepareStatement(sql) as PreparedStatement
            pstmt.setString(1, clientInfo.userId)
            var ip = clientInfo.address.toString()
            ip = ip.substring(1, ip.length)
            System.err.println(ip + "修改后的IP")
            pstmt.setString(2, ip)
            pstmt.setInt(3, clientInfo.port)
            i = pstmt.executeUpdate()
            pstmt.close()
            conn.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            WriteErrorLog.SaveError("在class OnLinedb下的public boolean addOnLineInfo(ClientInfo clientInfo)中出现异常,异常信息:$e")
            e.printStackTrace()
            return false
        }
        return i != 0
    }

    @get:Throws(UnknownHostException::class)
    val onLineInfo: List<ClientInfo>
        // 从数据库查询用户信息IP,port
        get() {
            val sql = "select * from nekodb.online_info"
            var conn: Connection? = null
            var pstmt: PreparedStatement? = null
            var rs: ResultSet? = null
            val onLineInfo: MutableList<ClientInfo> = ArrayList()
            try {
                // 2.创建数据库连接
                conn = DBHelper.connection
                // 3. 创建语句对象
                pstmt = conn.prepareStatement(sql)
                // 5. 执行查询（R）
                rs = pstmt.executeQuery()
                // 6. 遍历结果集
                while (rs.next()) {
                    val clientInfo = ClientInfo()
                    clientInfo.userId = rs.getString("user_id")
                    clientInfo.port = rs.getInt("port")
                    clientInfo.address = InetAddress.getByName(rs.getString("ip"))
                    onLineInfo.add(clientInfo)
                }
            } catch (e: SQLException) {
                WriteErrorLog.SaveError("在class OnLinedb下的public List<ClientInfo> getOnLineInfo()中出现异常,异常信息:$e")
                e.printStackTrace()
            } finally { // 释放资源
                if (rs != null) {
                    try {
                        rs.close()
                    } catch (_: SQLException) {
                    }
                }
                if (pstmt != null) {
                    try {
                        pstmt.close()
                    } catch (_: SQLException) {
                    }
                }
                if (conn != null) {
                    try {
                        conn.close()
                    } catch (_: SQLException) {
                    }
                }
            }
            return onLineInfo
        }

    // 用户退出从表中移除数据
    fun removeInfo(user_id: String?): Boolean {
        val sql = "delete from nekodb.online_info where user_id = ?;"
        val i: Int
        //创建SQL语句
        val pstmt: PreparedStatement
        try {
            //创建连接
            val conn = DBHelper.connection
            //初始化参数
            pstmt = conn.prepareStatement(sql) as PreparedStatement
            pstmt.setString(1, user_id)
            i = pstmt.executeUpdate()
            pstmt.close()
            conn.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            WriteErrorLog.SaveError("在class OnLinedb下的public boolean removeInfo(String user_id) 中出现异常,异常信息:$e")
            e.printStackTrace()
            return false
        }
        return i != 0
    }
}