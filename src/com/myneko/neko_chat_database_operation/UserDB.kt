package com.myneko.neko_chat_database_operation

import com.myneko.dev_operation.WriteErrorLog
import com.myneko.neko_chat_interface.Friend
import com.myneko.neko_chat_interface.User
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import javax.swing.JOptionPane

/*
 * 用户数据库操作类:很多处出现重复考虑复用技术
 */
object UserDB {
    //实现注册功能
	@JvmStatic
	fun Regisration(user: User, sql: String?): Boolean {
        var i = 0
        //创建SQL语句
        val pstmt: PreparedStatement
        try {
            //创建连接
            val conn = DBHelper.connection
            //初始化参数
            pstmt = conn.prepareStatement(sql) as PreparedStatement
            pstmt.setString(1, user.user_id)
            pstmt.setString(2, user.user_pwd)
            pstmt.setString(3, user.user_name)
            pstmt.setString(4, user.user_icon)
            pstmt.setString(5, user.state)
            i = pstmt.executeUpdate()
            pstmt.close()
            conn.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            WriteErrorLog.SaveError("在class Userdb下的public  static boolean Regisration(User user,String sql)中出现异常,异常信息:$e")
            e.printStackTrace()
            JOptionPane.showMessageDialog(null, "账号已被注册，请重新编辑账号")
            return false
        }
        return i != 0
    }

    //添加好友
	@JvmStatic
	fun addFirend(friend: Friend, sql: String?): Boolean {
        var i = 0
        //创建SQL语句
        val pstmt: PreparedStatement
        try {
            //创建连接
            val conn = DBHelper.connection
            //初始化参数
            pstmt = conn.prepareStatement(sql) as PreparedStatement
            pstmt.setString(1, friend.user_id1)
            pstmt.setString(2, friend.user_id2)
            i = pstmt.executeUpdate()
            pstmt.close()
            conn.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            WriteErrorLog.SaveError("在class Userdb下的public  static boolean Regisration(User user,String sql)中出现异常,异常信息:$e")
            e.printStackTrace()
            return false
        }
        return i != 0
    }

    // 查询所有用户信息
	@JvmStatic
	fun findAll(sql: String?): List<Map<String, String>> {
        val list: MutableList<Map<String, String>> = ArrayList()
        try {
            DBHelper.connection.use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    pstmt.executeQuery().use { rs ->

                        // 6. 遍历结果集
                        while (rs.next()) {
                            val row: MutableMap<String, String> = HashMap()
                            row["user_id"] = rs.getString("user_id")
                            row["user_name"] = rs.getString("user_name")
                            row["user_pwd"] = rs.getString("user_pwd")
                            row["user_icon"] = rs.getString("user_icon")
                            row["state"] = rs.getString("state")
                            list.add(row)
                        }
                    }
                }
            }
        } catch (e: SQLException) {
            WriteErrorLog.SaveError("在class Userdb下的public static List<Map<String, String>> findAll(String sql)中出现异常,异常信息:$e")
            e.printStackTrace()
        }
        return list
    }

    // 按照主键查询
	@JvmStatic
	fun findById(id: String?, sql: String?): Map<String, String>? {
        var conn: Connection? = null
        var pstmt: PreparedStatement? = null
        var rs: ResultSet? = null
        try {
            // 2.创建数据库连接
            conn = DBHelper.connection
            // 3. 创建语句对象
            pstmt = conn.prepareStatement(sql)
            // 4. 绑定参数
            pstmt.setString(1, id)
            // 5. 执行查询（R）
            rs = pstmt.executeQuery()
            // 6. 遍历结果集
            if (rs.next()) {
                val row: MutableMap<String, String> = HashMap()
                row["user_id"] = rs.getString("user_id")
                row["user_name"] = rs.getString("user_name")
                row["user_pwd"] = rs.getString("user_pwd")
                row["user_icon"] = rs.getString("user_icon")
                row["state"] = rs.getString("state")
                return row
            }
        } catch (e: SQLException) {
            WriteErrorLog.SaveError("在class Userdb下的public static Map<String, String> findById(String id,String sql)中出现异常,异常信息:$e")
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
        return null
    }

    // 查询好友 列表
    @JvmStatic
	@Throws(SQLException::class)
    fun findFriends(id: String?, sql: String?): List<Map<String, String>> {
        var conn: Connection? = null
        var pstmt: PreparedStatement? = null
        var rs: ResultSet? = null
        val friends: MutableList<Map<String, String>> = ArrayList()
        try {
            // 2.创建数据库连接
            conn = DBHelper.connection
            // 3. 创建语句对象
            pstmt = conn.prepareStatement(sql)
            // 4. 绑定参数
            pstmt.setString(1, id)
            pstmt.setString(2, id)
            // 5. 执行查询（R）
            rs = pstmt.executeQuery()
            // 6. 遍历结果集
            while (rs.next()) {
                val row: MutableMap<String, String> = HashMap()
                row["user_id"] = rs.getString("user_id")
                row["user_name"] = rs.getString("user_name")
                row["user_pwd"] = rs.getString("user_pwd")
                row["user_icon"] = rs.getString("user_icon")
                row["state"] = rs.getInt("state").toString()
                friends.add(row)
            }
        } catch (e: SQLException) {
            WriteErrorLog.SaveError("在class Userdb下的public static  List<Map<String, String>> findFriends(String id,String sql)中出现异常,异常信息:$e")
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
        return friends
    }

    //查询用户登入情况
	@JvmStatic
	fun Loginstatus(userid: String?, sql: String?): Boolean {
        var conn: Connection? = null
        var pstmt: PreparedStatement? = null
        var rs: ResultSet? = null
        try {
            // 2.创建数据库连接
            conn = DBHelper.connection
            // 3. 创建语句对象
            pstmt = conn.prepareStatement(sql)
            // 4. 绑定参数
            pstmt.setString(1, userid)
            // 5. 执行查询（R）
            rs = pstmt.executeQuery()
            if (rs.next()) {
                val result = rs.getString("state")
                return result.trim { it <= ' ' } == "1"
            }
        } catch (e: Exception) {
            WriteErrorLog.SaveError("在class Userdb下的public boolean Loginstatus(String userid)中出现异常,异常信息:$e")
            e.printStackTrace()
        }
        return false
    }

    //修改用户登入状态
	@JvmStatic
	fun Changestatu(user: User, sql: String?): Boolean {
        var i = 0
        //创建SQL语句
        val pstmt: PreparedStatement
        try {
            //创建连接
            val conn = DBHelper.connection
            //初始化参数
            pstmt = conn.prepareStatement(sql) as PreparedStatement
            pstmt.setString(1, user.state)
            pstmt.setString(2, user.user_id)
            i = pstmt.executeUpdate()
            pstmt.close()
            conn.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            WriteErrorLog.SaveError("在class Userdb下的public  static boolean Regisration(User user,String sql)中出现异常,异常信息:$e")
            e.printStackTrace()
            return false
        }
        return i != 0
    }

    //修改用户密码
	@JvmStatic
	fun updateUserPwd(user: User, sql: String?): Boolean {
        var i = 0
        //创建SQL语句
        val pstmt: PreparedStatement
        try {
            //创建连接
            val conn = DBHelper.connection
            //初始化参数
            pstmt = conn.prepareStatement(sql) as PreparedStatement
            pstmt.setString(1, user.user_pwd)
            pstmt.setString(2, user.user_id)
            i = pstmt.executeUpdate()
            pstmt.close()
            conn.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            WriteErrorLog.SaveError("在class Userdb下的public  static boolean updateUserPwd(User user,String sql)中出现异常,异常信息:$e")
            e.printStackTrace()
            return false
        }
        return i != 0
    }

    //修改用户账号和用户名
	@JvmStatic
	fun updateUserInfo(user: User, sql: String?, user_id: String): Boolean {
        var i = 0
        //创建SQL语句
        val pstmt: PreparedStatement
        try {
            //创建连接
            val conn = DBHelper.connection
            //初始化参数
            pstmt = conn.prepareStatement(sql) as PreparedStatement
            pstmt.setString(1, user.user_name)
            pstmt.setString(2, user.user_id)
            if (!user_id.trim { it <= ' ' }.isEmpty()) {
                pstmt.setString(3, user_id)
            } else {
                pstmt.setString(3, user.user_id)
            }
            i = pstmt.executeUpdate()
            pstmt.close()
            conn.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            WriteErrorLog.SaveError("在class Userdb下的public  static boolean updateUserInfo(User user,String sql)中出现异常,异常信息:$e")
            e.printStackTrace()
            return false
        }
        return i != 0
    }

    //修改用户密码
	@JvmStatic
	fun deleteFirend(objfriend: Friend, sql: String?): Boolean {
        var i = 0
        //创建SQL语句
        val pstmt: PreparedStatement
        try {
            //创建连接
            val conn = DBHelper.connection
            //初始化参数
            pstmt = conn.prepareStatement(sql) as PreparedStatement
            pstmt.setString(1, objfriend.user_id1)
            pstmt.setString(2, objfriend.user_id2)
            i = pstmt.executeUpdate()
            pstmt.close()
            conn.close()
        } catch (e: SQLException) {
            e.printStackTrace()
            WriteErrorLog.SaveError("在class Userdb下的public  static boolean updateUserPwd(User user,String sql)中出现异常,异常信息:$e")
            e.printStackTrace()
            return false
        }
        return i != 0
    }
}