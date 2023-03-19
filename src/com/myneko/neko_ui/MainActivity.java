package com.myneko.neko_ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONObject;

import com.myneko.neko_chat_interface.User;
import com.myneko.dev_operation.WriteErrorLog;
import com.myneko.neko_chat_database_operation.OnlineInfoDB;
import com.myneko.dev_method.UserDao;
import com.myneko.neko.Client;
import com.myneko.neko.Server;
import com.myneko.dev_utils.ValidateUtil;

import javax.swing.JTextField;
import java.awt.event.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.awt.Point;

import javax.swing.JScrollPane;
import java.awt.Font;
import java.util.Objects;

public class MainActivity extends JFrame implements Runnable {

	private JPanel contentPane;
	private JTextField txtqq;
	// 头像框
	JLabel HeaderImg;
	JScrollPane scrollPane;

	// 好友聊天窗体
	ChatActivity chatActivity = null;
	// 线程运行状态
	private boolean isRunning = true;
	// 用户信息
	private final Map user;
	// 好友列表
	private List<Map<String, String>> friends;
	// 好友标签控件列表
	private List<JLabel> lblFriendList;
	User objuser;
	// 好友操作
	public static FriendOperationActivity friendOperationActivity = null;
	// 好友列表面板
	private JPanel friendListPanel;

	/**
	 * Create the frame.
	 */
	public MainActivity(Map user) {
		// 初始化成员变量
		this.user = user;
		business();
	}

	@SuppressWarnings("unchecked")
	private void business() {
		/// 初始化用户列表
		this.friends = (List<Map<String, String>>) user.get("friends");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setIconImage(new ImageIcon("Image/Logo.jpg").getImage());

		String userId = (String) user.get("user_id");
		String userName = (String) user.get("user_name");
		String userIcon = (String) user.get("user_icon");
		if (objuser == null) {
			objuser = new User();
		}
		objuser.user_icon = userIcon;
		objuser.user_name = userName;

		// 创建视图
		createView(objuser);

		JPanel panel1 = new JPanel();
		scrollPane.setViewportView(panel1);
		panel1.setLayout(new BorderLayout(0, 0));

		// 好友列表面板
		friendListPanel = new JPanel();
		panel1.add(friendListPanel);
		friendListPanel.setLayout(new GridLayout(50, 0, 0, 5));

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 345, 140);
		contentPane.add(panel);
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setLayout(null);

		HeaderImg = new JLabel();
		HeaderImg.addMouseListener(new MouseAdapter() {
			// 点击头像弹出自己的信息
			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});
		HeaderImg.setBounds(10, 49, 36, 36);
		panel.add(HeaderImg);

		//user_name = new JLabel("None");
		JLabel user_name = new JLabel("None");
		user_name.setForeground(Color.WHITE);
		user_name.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		user_name.setBounds(56, 40, 69, 23);

		if (!objuser.user_name.trim().isEmpty()) {
			System.out.println(objuser.user_name);
			user_name.setText(objuser.user_name);
		}
		user_name.setOpaque(false);
		panel.add(user_name);

		JLabel user_Signature = new JLabel("在线");
		user_Signature.setForeground(Color.WHITE);
		user_Signature.setBackground(Color.WHITE);
		user_Signature.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		user_Signature.setBounds(56, 62, 69, 23);
		if (!objuser.user_icon.trim().isEmpty()) {
			String iconFile = String.format("/resource/img/%s.jpg", objuser.user_icon);
			HeaderImg.setIcon(new ImageIcon(Objects.requireNonNull(MainActivity.class.getResource(iconFile))));
		}
		user_Signature.setOpaque(false);
		panel.add(user_Signature);

		txtqq = new JTextField();
		txtqq.addFocusListener(new MainActivityTextInput(txtqq,"在这里输入以搜索用户"));

		txtqq.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			// 查询用户信息
			@Override
			public void keyPressed(KeyEvent e) {
				txtqq.setOpaque(true);
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// 数据验证
					if (txtqq.getText().trim().isEmpty()) {
						JOptionPane.showOptionDialog(null, "输入的neko号不能为空喵", "提示", JOptionPane.DEFAULT_OPTION,
								JOptionPane.INFORMATION_MESSAGE, null, null, null);
						return;
					}
					// 采用正则表达式进行验证
					if (!ValidateUtil.isNumber(txtqq.getText().trim())) {
						JOptionPane.showOptionDialog(null, "输入的neko号不符合规则喵", "提示", JOptionPane.DEFAULT_OPTION,
								JOptionPane.INFORMATION_MESSAGE, null, null, null);
						return;
					}

					try {
						// 通过用户id获取用户信息
						Map<String, String> objfriend = new UserDao().findById(txtqq.getText().trim());
						if (objfriend == null) {
							JOptionPane.showOptionDialog(null, "neko不存在喵", "提示", JOptionPane.DEFAULT_OPTION,
									JOptionPane.INFORMATION_MESSAGE, null, null, null);
							return;
						}
						// 亲空
						txtqq.setText("");
						AddFriendsActivity addFriendsActivity = new AddFriendsActivity(objfriend, userId);
						addFriendsActivity.setVisible(true);
					} catch (UnknownHostException | SQLException e1) {
						// 异常处理写入错误日志，便于项目维护
						WriteErrorLog.SaveError(
								"在public class MainActivity 下的private void business(Map user) 下public void keyPressed(KeyEvent e) 中出现异常,异常信息:"
										+ e.toString());
						e1.printStackTrace();
					}
				}

			}
		});

		txtqq.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtqq.setEnabled(true);
				txtqq.setOpaque(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				txtqq.setEnabled(false);
				txtqq.setOpaque(false);
			}
		});

		txtqq.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		txtqq.setForeground(Color.BLACK);
		txtqq.setBounds(0, 108, 345, 32);
		txtqq.setOpaque(false);
		panel.add(txtqq);
		txtqq.setBackground(Color.WHITE);
		txtqq.setColumns(10);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(0, 0, 345, 140);
		lblNewLabel.setIcon(new ImageIcon("Image/MainPageBackground.jpg"));
		panel.add(lblNewLabel);

		// 刷新好友列表
		freshFriendsList(friends);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// 注册窗口事件
		addWindowListener(new WindowAdapter() {
			// 单击窗口关闭按钮时调用
			public void windowClosing(WindowEvent e) {

				// 当前用户下线
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("command", Server.COMMAND_LOGOUT);
				jsonObj.put("user_id", userId);
				byte[] b = jsonObj.toString().getBytes();

				InetAddress address;
				try {
					address = InetAddress.getByName(Client.client_IP);
					// 创建DatagramPacket对象
					DatagramPacket packet = new DatagramPacket(b, b.length, address, Client.client_PORT);
					// 发送
					Client.socket.send(packet);

				} catch (IOException e1) {
					WriteErrorLog
							.SaveError("在class FriendsFrame下的public FriendsFrame(Map user) 中出现异常,异常信息:" + e.toString());
					e1.printStackTrace();
				}

				// 退出系统
				int n = JOptionPane.showConfirmDialog(null, "你确定要关闭当前程序?", "警告", JOptionPane.YES_NO_OPTION); // 返回值为0或1
				if (n == JOptionPane.YES_OPTION) {
					// 将用户信息改为离线
					User objuser = new User();
					objuser.user_id = (String) user.get("user_id");
					objuser.state = "0";
					System.out.println(objuser.user_id + ";" + objuser.state);
					new UserDao().Changestatu(objuser);
					// 将Onclent中的数据移除
					new OnlineInfoDB().removeInfo(objuser.user_id);
					System.exit(0);
				}
			}
		});

		// 启动接收消息子线程
		//	resetThread();
		Thread thread = new Thread(this);
		thread.start();
	}

	private void createView(User objuser) {
		// 窗体大小不能改变
		this.setResizable(false);
		setTitle("neko的地方喵~");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 345, 698);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 608, 345, 61);
		panel_1.setBackground(new Color(198, 211, 228));
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		JLabel bottom = new JLabel();
		bottom.setBounds(0, 0, 345, 61);
		bottom.setIcon(new ImageIcon("Image/MainButton.jpg"));
		panel_1.add(bottom);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 140, 345, 469);
		contentPane.add(scrollPane);
		close();
	}

	public void close() {
		this.setVisible(false);
	}

	public void run() {
		// 准备一个缓冲区
		byte[] buffer = new byte[1024];
		while (isRunning) {

			try {
				InetAddress address = InetAddress.getByName(Client.client_IP);
				/* 接收数据报 */
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, Client.client_PORT);
				System.err.println("客户端IP:"+Client.client_IP+"客户端端口"+Client.client_PORT);
				// 开始接收
				Client.socket.receive(packet);
				// 接收数据长度
				int len = packet.getLength();
				String str = new String(buffer, 0, len);

				System.out.println("客户端：  " + str);

				JSONObject jsonObj = new JSONObject(str);
				String userId = (String) jsonObj.get("user_id");
				String online = (String) jsonObj.get("online");

				
				// 刷新好友列表
				System.out.println("刷新好友信息");
				refreshFriendList(userId, online);

			} catch (Exception ignored) {
			}
		}
	}

	private void freshFriendsList(List<Map<String, String>> friendsMap) {
		friends = friendsMap;
		 lblFriendList = new ArrayList<JLabel>();
		 // 初始化好友列表
		for (Map<String, String> friend : friends) {
			String friendUserId = friend.get("user_id");
			String friendUserName = friend.get("user_name");
			String friendUserIcon = friend.get("user_icon");
			// 获得好友在线状态
			String friendUserOnline = friend.get("online");

			JLabel lblFriend = new JLabel(friendUserName);
			lblFriend.setToolTipText(friendUserId);
			String friendIconFile = "/resource/img/" + friendUserIcon + ".jpg";
			URL location = MainActivity.class.getResource(friendIconFile);
			assert location != null;
			lblFriend.setIcon(new ImageIcon(location));

			// 在线设置可用，离线设置不可用
			lblFriend.setEnabled(!friendUserOnline.equals("0"));


			lblFriend.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// 用户图标双击鼠标时显示对话框
					if (e.getClickCount() == 2) {
						try {
							//该对象是否创建过,防止多次打开相同窗体
							if (chatActivity == null) {
								chatActivity = new ChatActivity(MainActivity.this, user, friend);
							}
							//获取单击图片的ID和我已经打开的ID是否一致，一致表示该窗体已被打开无需再打开
							if (!Objects.equals(chatActivity.friendUserId, lblFriend.getToolTipText())) {
								chatActivity = new ChatActivity(MainActivity.this, user, friend);
							}
							chatActivity.setVisible(true);
							isRunning = false;
						} catch (UnknownHostException e1) {
							//异常处理写入错误日志，便于项目维护
							WriteErrorLog.SaveError("在public class MainActivity 下的private void business(Map user) 下lblFriend.addMouseListener(new MouseAdapter()中出现异常,异常信息:" + e.toString());
							e1.printStackTrace();
						}

					}

					//鼠标右键
					if (e.isMetaDown()) {

						//获取x,y
						//jPopupMenu1.show(this, e.getX(), e.getX());
						Point point = java.awt.MouseInfo.getPointerInfo().getLocation();
						//如果是计算当前Fream的坐标，可以用getLocation()(Point p =lblFriend.getLocation();)
						if (friendOperationActivity == null) {
							friendOperationActivity = new
									FriendOperationActivity((int) point.getX(), (int) point.getY(), friend);

						}
						if (!Objects.equals(FriendOperationActivity.friendUserId, lblFriend.getToolTipText())) {
							friendOperationActivity = new
									FriendOperationActivity((int) point.getX(), (int) point.getY(), friend);
						}
						friendOperationActivity.setVisible(true);

					}


				}
			});
			// 添加到列表集合
			lblFriendList.add(lblFriend);
			// 添加到面板
			friendListPanel.add(lblFriend);
		}
	}

	// 刷新好友列表
	public void refreshFriendList(String userId,String online) {
		// 初始化好友列表
		for (JLabel lblFriend : lblFriendList) {
			// 判断用户Id是否一致
			if (userId.equals(lblFriend.getToolTipText())) {
				lblFriend.setEnabled(online.equals("1"));
			}
		}
		
	}

	// 重新启动接收消息子线程
	public void resetThread() {
		isRunning = true;
		// 接收消息子线程
		Thread receiveMessageThread = new Thread();
		// 启动接收消息线程
		receiveMessageThread.start();
	}

}
