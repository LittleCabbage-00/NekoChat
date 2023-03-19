package com.myneko.dev_method

import com.myneko.neko_chat_interface.Friend
import com.myneko.neko_chat_interface.User
import com.myneko.dev_operation.ReaderFileString
import com.myneko.dev_operation.SaveFileString
import com.myneko.dev_operation.WriteErrorLog
import com.myneko.neko_chat_database_operation.UserDB.Changestatu
import com.myneko.neko_chat_database_operation.UserDB.Loginstatus
import com.myneko.neko_chat_database_operation.UserDB.Regisration
import com.myneko.neko_chat_database_operation.UserDB.addFirend
import com.myneko.neko_chat_database_operation.UserDB.deleteFirend
import com.myneko.neko_chat_database_operation.UserDB.findAll
import com.myneko.neko_chat_database_operation.UserDB.findById
import com.myneko.neko_chat_database_operation.UserDB.findFriends
import com.myneko.neko_chat_database_operation.UserDB.updateUserInfo
import com.myneko.neko_chat_database_operation.UserDB.updateUserPwd
import com.myneko.neko.Client
import com.myneko.neko.Server
import org.json.JSONObject
import java.io.IOException
import java.net.DatagramPacket
import java.net.InetAddress
import java.sql.SQLException
import javax.swing.JOptionPane

class UserDao {
    //int errtage=0;//定义异常标识
    // 实现登入功能
    fun login(objUser: User): Map<*, *>? {
        // 准备一个缓冲区
        val buffer = ByteArray(1024)
        val address: InetAddress
        try {
            address = InetAddress.getByName(Client.client_IP)
            val jsonObj = JSONObject()
            jsonObj.put("command", Server.COMMAND_LOGIN)
            jsonObj.put("user_id", objUser.user_id?.trim { it <= ' ' } )
            jsonObj.put("user_pwd", objUser.user_pwd)
            // 字节数组
            val b = jsonObj.toString().toByteArray()

            // 创建DatagramPacket对象
            var packet = DatagramPacket(b, b.size, address, Client.client_PORT)
            // 发送
            Client.socket?.send(packet)

            /* 接收数据报 */packet = DatagramPacket(buffer, buffer.size, address, Client.client_PORT)
            Client.socket?.receive(packet)
            // 接收数据长度
            val len = packet.length
            val str = String(buffer, 0, len)
            println("receivedjsonObj = $str")
            val receivedjsonObj = JSONObject(str)
            if (receivedjsonObj["result"] as Int == 0) {
                val userMap: Map<*, *>? = receivedjsonObj.toMap()
                if (userMap == null) {
                    JOptionPane.showMessageDialog(null, "您QQ号码或密码不正确")
                }
                return userMap
            }
        } catch (e: Exception) {
            WriteErrorLog.SaveError("在class UserDao下的public void login(User objUser)中出现异常,异常信息:$e")
            System.err.println(e.toString())
            JOptionPane.showMessageDialog(null, "连接服务器超时!,异常信息:\n$e")
            return null
        }
        return null
    }

    //实现注册功能
    fun Regisration(user: User?): Boolean {
        //创建SQL语句
        val sql = "INSERT INTO user VALUES(?,?,?,?,?);"
        return Regisration(user!!, sql)
    }

    // 查询所有用户信息
    fun findAll(): List<Map<String, String>> {
        // SQL语句
        val sql = "select user_id,user_pwd,user_name, user_icon,state from user"
        return findAll(sql)
    }

    // 按照主键查询
    fun findById(id: String?): Map<String, String>? {
        // SQL语句
        val sql = "select user_id,user_pwd,user_name, user_icon,state from user where user_id = ?"
        return findById(id, sql)
    }

    //查询是否为好友
    @Throws(SQLException::class)
    fun selectFriends(userid: String?, friendid: String): Boolean {
        val objfindFriends = findFriends(userid)
        // 初始化好友列表
        for (i in objfindFriends.indices) {
            val friend = objfindFriends[i]
            val friendUserId = friend["user_id"]
            if (friendid == friendUserId) {
                return true
            }
        }
        return false
    }

    //查询用户登入情况
    fun Loginstatus(userid: String?): Boolean {
        val sql = "select state from user where user_id=?"
        return Loginstatus(userid, sql)
    }

    //备注:更改用户名;修改用户登入状态;更改用户名这里可以考虑采用复用技术
    //修改用户登入状态
    fun Changestatu(user: User?): Boolean {
        val sql = "update  user set state=? where user_id=?"
        return Changestatu(user!!, sql)
    }

    //修改用户密码
    fun updateuserPwd(user: User?): Boolean {
        val sql = "update user set user_pwd=? where user_id=? "
        return updateUserPwd(user!!, sql)
    }

    //更改用户名
    fun updateUserInfo(user: User?, user_id: String?): Boolean {
        val sql = "update user set user_name=? ,user_id=? where user_id=?"
        return updateUserInfo(user!!, sql, user_id!!)
    }

    //删除好友
    fun deleteFirend(objfFriend: Friend?): Boolean {
        val sql = "delete  from friend  where user_id1=? and  user_id2=?"
        return deleteFirend(objfFriend!!, sql)
    }

    // 查询好友 列表
    @Throws(SQLException::class)
    fun findFriends(id: String?): List<MutableMap<String, String>> {
        // SQL语句
        val sql = ("select user_id,user_pwd,user_name,user_icon,state FROM user " + " WHERE "
                + "    user_id IN (select user_id2 as user_id  from friend where user_id1 = ?)"
                + " OR user_id IN (select user_id1 as user_id  from friend where user_id2 = ?)")
        return findFriends(id, sql) as List<MutableMap<String, String>>
    }

    //保存账号和密码
    fun SaveUserInfo(userId: String?, password: String?) {
        SaveFileString.SaveFileStr(userId, password, "Userinfo.txt", false)
    }

    //保存登入界面上单选按钮状态
    fun Savestate(saveuserInfoState: Boolean, autoLogin: Boolean) {
        var state1 = "0"
        var state2 = "0"
        if (saveuserInfoState) {
            state1 = "1"
        }
        if (autoLogin) {
            state2 = "1"
        }
        SaveFileString.SaveFileStr(state1, state2, "saveAutoLoginState.txt", false)
    }

    //读取自动登入状态
    fun readerAutoLoginState(): ArrayList<String>? {
        return try {
            ReaderFileString.readerFilestr("saveAutoLoginState.txt")
        } catch (e: IOException) {
            e.printStackTrace()
            WriteErrorLog.SaveError("在class UserDao下的public ArrayList<String> readerAutoLoginState()中出现异常,异常信息:$e")
            null
        }
    }

    //读取账号和密码
    fun readerUserInfo(): ArrayList<String>? {
        return try {
            ReaderFileString.readerFilestr("Userinfo.txt")
        } catch (e: IOException) {
            e.printStackTrace()
            WriteErrorLog.SaveError("在class UserDao下的public ArrayList<String> readerUserInfo()中出现异常,异常信息:$e")
            null
        }
    }

    //添加好友
    fun addFirend(objfriend: Friend?): Boolean {
        //创建SQL语句
        val sql = "INSERT INTO friend VALUES(?,?);"
        return addFirend(objfriend!!, sql)
    }
}