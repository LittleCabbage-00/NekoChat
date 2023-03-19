package com.myneko.neko_ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.UnknownHostException;
import java.util.Map;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FriendOperationActivity extends JFrame {

	  
		// 聊天好友用户Id
		public static  String friendUserId;
		//好友信息
	    public static FriendsInfoActivity friendsInfoActivity;


	/**
	 * Create the frame.
	 */
	public FriendOperationActivity(int x, int y, Map<String, String> friend) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}

		});
		//获取好友ID
		friendUserId = friend.get("user_id");
		// 窗体大小不能改变
	    this.setResizable(false);
		setType(Type.UTILITY);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(x, y, 170, 105);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("发送消息");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setBounds(0, 0, 154, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("文件传输");
		btnNewButton_1.addActionListener(new ActionListener() {
			//修改当前用户信息//找回密码
			public void actionPerformed(ActionEvent e) {
				//new  MainActivity(friend).close();
				close();
				FileTransferMainActivity fileTransferMainActivity =new FileTransferMainActivity();
				fileTransferMainActivity.setVisible(true);
			}
		});
		btnNewButton_1.setBackground(Color.WHITE);
		btnNewButton_1.setBounds(0, 22, 154, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("查看资料");
		btnNewButton_2.addActionListener(new ActionListener() {
			//查看资料
			public void actionPerformed(ActionEvent e) {
					try {
						if (friendsInfoActivity ==null) {
							friendsInfoActivity =new FriendsInfoActivity(friend);
						}
						friendsInfoActivity.setVisible(true);
						close();
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}
				
			}
		});
		btnNewButton_2.setBackground(Color.WHITE);
		btnNewButton_2.setBounds(0, 43, 154, 23);
		contentPane.add(btnNewButton_2);
	}
	
	private void close() {
		MainActivity.friendOperationActivity =null;
		this.setVisible(false);
	}
}
