package com.myneko.neko_ui;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.myneko.neko_chat_interface.Friend;
import com.myneko.neko.Client;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class FriendsInfoActivity extends JFrame {


	// 当前用户Id
	private String userId;
	// 聊天好友用户Id
	public String friendUserId;
	//朋友对象
	private Friend objfriend;
	/**
	 * Create the frame.
	 */
	public FriendsInfoActivity(Map<String, String> friend) throws UnknownHostException {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		setType(Type.UTILITY);
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
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 300, 350);
		JPanel contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel headimage = new JLabel("");
		headimage.setBounds(24, 67, 36, 36);
		if (!objfriend.friend_icon.isEmpty()) {
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
		
		JLabel label = new JLabel("好友信息");
		label.setFont(new Font("微软雅黑", Font.BOLD, 20));
		label.setBounds(84, 0, 98, 33);
		panel.add(label);
		
		JLabel lblNewLabel_1 = new JLabel("姓名:");
		lblNewLabel_1.setFont(new Font("微软雅黑", Font.BOLD, 15));
		lblNewLabel_1.setBounds(80, 67, 54, 15);
		contentPane.add(lblNewLabel_1);
		
		JLabel user_name = new JLabel("None");
		user_name.setFont(new Font("微软雅黑", Font.BOLD, 15));
		user_name.setBounds(181, 67, 54, 15);
		if (!objfriend.friend_name.isEmpty()) {
			user_name.setText(objfriend.friend_name);
		}
		contentPane.add(user_name);
		
		JLabel lblNewLabel_3 = new JLabel("neko号:");
		lblNewLabel_3.setFont(new Font("微软雅黑", Font.BOLD, 15));
		lblNewLabel_3.setBounds(80, 104, 100, 15);
		contentPane.add(lblNewLabel_3);
		
		JLabel user_section = new JLabel("None");
		user_section.setFont(new Font("微软雅黑", Font.BOLD, 15));
		user_section.setBounds(181, 104, 54, 15);
		if (!objfriend.friend_id.isEmpty()) {
			user_section.setText(objfriend.friend_id);
		}
		contentPane.add(user_section);
		
		JLabel lblNewLabel_7 = new JLabel("邮箱:");
		lblNewLabel_7.setFont(new Font("微软雅黑", Font.BOLD, 15));
		lblNewLabel_7.setBounds(40, 148, 54, 15);
		contentPane.add(lblNewLabel_7);
		
		JLabel lblNewLabel_8 = new JLabel("IP地址:");
		lblNewLabel_8.setFont(new Font("微软雅黑", Font.BOLD, 15));
		lblNewLabel_8.setBounds(80, 195, 54, 15);
		contentPane.add(lblNewLabel_8);
		
		JLabel user_postbox = new JLabel("None");
		user_postbox.setFont(new Font("微软雅黑", Font.BOLD, 15));
		user_postbox.setBounds(80, 148, 160, 15);
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
	}
	
	private void close() {
		 FriendOperationActivity.friendsInfoActivity =null;
		this.setVisible(false);
	}
}
