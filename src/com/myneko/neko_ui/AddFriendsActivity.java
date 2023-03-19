package com.myneko.neko_ui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.myneko.neko_chat_interface.Friend;
import com.myneko.neko_chat_interface.User;
import com.myneko.dev_method.UserDao;
import com.myneko.neko.Client;


import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Map;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Objects;
import javax.swing.JTextField;

public class AddFriendsActivity extends JFrame {
	//创建用户业务类
	private UserDao objUserDao;
	// 当前用户Id
	private final String userId;
	// 聊天好友用户Id
	public String friendUserId;
	//修改用户账号【qq号】
	private boolean updateUserNum=false;
	//朋友对象
		private Friend objfriend;
		private final JTextField user_name;
		private final JTextField user_section;
	/**
	 * Create the frame.
	 */
	public AddFriendsActivity(Map<String, String> friend, String userid) throws UnknownHostException, SQLException {
		//获取好友信息
		String friendIco=friend.get("user_icon");
		this.friendUserId = friend.get("user_id");
		// 聊天好友用户名
		String friendUserName = friend.get("user_name");
		/// 初始化当前Frame
		String iconFile = String.format("/resource/img/%s.jpg", friendIco);
		//封装朋友对象
		if (objfriend==null) {
		 objfriend=new Friend(friendUserId, friendUserName, iconFile,InetAddress.getByName(Client.client_IP).toString());	
		}
		
		//获取当前用户ID
		this.userId=userid;
		//查询该用户是否为自己的好友和是否是自己
		if (objUserDao==null) {
			objUserDao=new UserDao();
		}
		String titleStr = "";
		//添加好友
		boolean addFriend = false;
		//删除好友
		boolean deleteFriend = false;
		//修改用户姓名【备注】
		//修改用户信息
		boolean updateInfo = false;
		if (userId.trim().equals(friendUserId)) {
					titleStr ="neko neko喵~";
			updateUserNum=true;
					updateInfo =true;
				}else
				{
					titleStr ="不认识的人喵~";
					addFriend =true;
					updateUserNum=false;
				}
				if (objUserDao.selectFriends(userId,friendUserId)) {
					//为好友
					titleStr ="neko酱的好友喵~";
					addFriend =false;
					deleteFriend =true;
					updateUserNum=false;
					updateInfo =true;
				}
		
		
	    addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						close();
					}
		});
		setType(Type.UTILITY);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 600, 406);
		JPanel contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel headimage = new JLabel("");
		headimage.setBounds(10, 69, 60, 60);
		if (!objfriend.friend_icon.isEmpty()) {
			//headimage.setIcon(new ImageIcon(pathImage));
			headimage.setIcon(new ImageIcon(Objects.requireNonNull(Client.class.getResource(objfriend.friend_icon))));
		}else
		{
			headimage.setIcon(new ImageIcon("image/HeadIcon.jpg"));
		}
		contentPane.add(headimage);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBounds(0, 0, 284, 33);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("neko信息");
		label.setText(titleStr);
		label.setFont(new Font("微软雅黑", Font.BOLD, 20));
		label.setBounds(96, 0, 98, 33);
		panel.add(label);
		
		JLabel lblNewLabel_1 = new JLabel("姓名:");
		lblNewLabel_1.setFont(new Font("微软雅黑", Font.BOLD, 15));
		lblNewLabel_1.setBounds(80, 67, 54, 15);
		contentPane.add(lblNewLabel_1);
	
		
		JLabel lblNewLabel_3 = new JLabel("neko号:");
		lblNewLabel_3.setFont(new Font("微软雅黑", Font.BOLD, 15));
		lblNewLabel_3.setBounds(80, 104, 80, 15);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_7 = new JLabel("邮箱:");
		lblNewLabel_7.setFont(new Font("微软雅黑", Font.BOLD, 15));
		lblNewLabel_7.setBounds(70, 150, 54, 15);
		contentPane.add(lblNewLabel_7);
		
		JLabel lblNewLabel_8 = new JLabel("IP地址:");
		lblNewLabel_8.setFont(new Font("微软雅黑", Font.BOLD, 15));
		lblNewLabel_8.setBounds(80, 195, 54, 15);
		contentPane.add(lblNewLabel_8);
		
		JLabel user_postbox = new JLabel("None");
		user_postbox.setFont(new Font("微软雅黑", Font.BOLD, 15));
		user_postbox.setBounds(111, 150, 160, 15);
		if (!objfriend.friend_id.isEmpty()) {
			user_postbox.setText(objfriend.friend_id +"@neko.com");
		}
		contentPane.add(user_postbox);
		
		JLabel user_IpAddress = new JLabel("None");
		user_IpAddress.setFont(new Font("微软雅黑", Font.BOLD, 15));
		user_IpAddress.setBounds(141, 195, 133, 15);
		if (!objfriend.ipAddress.isEmpty()) {
			user_IpAddress.setText(objfriend.ipAddress);
		}
		contentPane.add(user_IpAddress);
		
		JButton btn_add = new JButton("添加neko");
		btn_add.setVisible(addFriend);
		btn_add.addActionListener(new ActionListener() {
			//添加好友
			public void actionPerformed(ActionEvent e) {
				//封装对象
				Friend  field=new Friend();
				field.user_id1 = userId;
				System.out.println(userId);
				field.user_id2 = friendUserId;
				if (objUserDao.addFirend(field)) {
					 JOptionPane.showOptionDialog(null, "添加成功喵!","提示", JOptionPane.DEFAULT_OPTION,
							 JOptionPane.INFORMATION_MESSAGE,null, null, null);
					 close();
				}else
				{
					 JOptionPane.showOptionDialog(null, "添加失败喵!","提示", JOptionPane.DEFAULT_OPTION,
							 JOptionPane.INFORMATION_MESSAGE,null, null, null);
				}
				
			}
		});

		btn_add.setBounds(49, 268, 163, 23);
		contentPane.add(btn_add);
		
		JButton btn_remove = new JButton("删除neko");
		btn_remove.setVisible(deleteFriend);
		//删除好友
		btn_remove.addActionListener(e -> {
			//封装对象
			Friend  field=new Friend();
			field.user_id1 = userId;
			field.user_id2 = friendUserId;
			if (objUserDao.deleteFirend(field)) {
				 JOptionPane.showOptionDialog(null, "删除成功喵!","提示", JOptionPane.DEFAULT_OPTION,
						 JOptionPane.INFORMATION_MESSAGE,null, null, null);
				 close();
			}else
			{
				 JOptionPane.showOptionDialog(null, "删除失败喵!","提示", JOptionPane.DEFAULT_OPTION,
						 JOptionPane.INFORMATION_MESSAGE,null, null, null);
			}
		});
		btn_remove.setBounds(49, 301, 163, 23);
		contentPane.add(btn_remove);
		
		JButton btnSaveInfo = new JButton("保存neko信息");
		btnSaveInfo.addActionListener(new ActionListener() {
			//保存用户信息
			public void actionPerformed(ActionEvent e) {
				//封装对象
				User objUser=new User();
				objUser.user_id = user_section.getText().trim();
				objUser.user_name = user_name.getText().trim();
				boolean result=false;
				if (updateUserNum) {
					result=objUserDao.updateUserInfo(objUser,userId);
				}else
				{
					result=objUserDao.updateUserInfo(objUser,"");
				}
				//实现修改业务
				if (result) {
					JOptionPane.showOptionDialog(null, "修改成功喵!","提示", JOptionPane.DEFAULT_OPTION,
							 JOptionPane.INFORMATION_MESSAGE,null, null, null);
					//用户下线
					objUser.state = "0";
					objUser.user_id = userId;
					new UserDao().Changestatu(objUser);
					JOptionPane.showOptionDialog(null, "数据上传neko服务器成功请重新启动程序喵!","提示", JOptionPane.DEFAULT_OPTION,
							 JOptionPane.INFORMATION_MESSAGE,null, null, null);
					//保存用户状态
					
					//强制用户下线
					System.exit(0);
				}else
				{
					JOptionPane.showOptionDialog(null, "修改失败喵!","提示", JOptionPane.DEFAULT_OPTION,
							 JOptionPane.INFORMATION_MESSAGE,null, null, null);
				}
			}
		});
		btnSaveInfo.setBounds(49, 334, 163, 23);
		contentPane.add(btnSaveInfo);
		
		user_name = new JTextField();
		if (!objfriend.friend_name.isEmpty()) {
			user_name.setText(objfriend.friend_name);
		}
		user_name.setBounds(124, 65, 150, 21);
		contentPane.add(user_name);
		user_name.setColumns(10);
		
		user_section = new JTextField();
		user_section.setBounds(174, 102, 100, 21);
		if (!objfriend.friend_id.isEmpty()) {
			user_section.setText(objfriend.friend_id);
		}
		contentPane.add(user_section);
		user_section.setColumns(10);
		//用户试考可修改
		if (updateInfo) {
			user_name.setEditable(true);
			user_section.setEditable(updateUserNum);	
			btnSaveInfo.setVisible(true);
		}else {
			user_name.setEditable(false);	
			user_section.setEditable(false);	
			btnSaveInfo.setVisible(false);
		}
		
		
	}
	
	private void close() {
		//释放对象
		objUserDao=null;
		this.setVisible(false);
	}
}
