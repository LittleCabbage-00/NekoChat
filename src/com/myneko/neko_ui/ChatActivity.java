package com.myneko.neko_ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

import org.json.JSONObject;

import com.myneko.neko_chat_interface.ClientInfo;
import com.myneko.neko_chat_interface.Friend;
import com.myneko.dev_method.UserDao;
import com.myneko.neko.Client;
import com.myneko.neko.Server;
import com.myneko.dev_utils.ChatActivityUtil;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChatActivity extends JFrame implements Runnable {

	// mulu:用户拖拽的路径
	String mulu = "";
	// 文件使用的次数
	public static int count = 0;
	private JPanel contentPane;
	private boolean isRunning = true;
	// 文件传输界面
	FileTransferMainActivity fileTransferMainActivity;
	// 当前用户Id
	private String userId;
	// 聊天好友用户Id
	public String friendUserId;
	// 聊天好友用户名
	private String friendUserName;
	// 发送消息文本区
	private JTextPane txtInfo;
	// 消息日志
	private StringBuffer infoLog;
	private String current_User;

	// 当前用户的Ip
	private InetAddress address;
	private final int port = Client.client_PORT;
	// 日期格式化
	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	JTextPane txtMainInfo;
	// 朋友对象
	private Friend objfriend;
	// private StyledDocument doc = null;

	// 群聊标识
	private boolean isGroupChart = false;
	private List<Map<String, String>> all_user;

	public ChatActivity(MainActivity friendsFrame, Map<String, String> user, Map<String, String> friend)
			throws UnknownHostException {
		all_user=new UserDao().findAll();
		// 初始化成员变量
		// 好友列表Frame
		this.userId = user.get("user_id");

		String userIcon = user.get("user_icon");
		current_User = user.get("user_name");
		System.out.println("current_User:" + current_User);
		

		String friendIco = friend.get("user_icon");
		this.friendUserId = friend.get("user_id");
		this.friendUserName = friend.get("user_name");
		this.infoLog = new StringBuffer();
		
		if (friendUserName.equals("群聊")) {
			isGroupChart =true;
		}

		/// 初始化当前Frame
		String iconFile = String.format("/resource/img/%s.jpg", friendIco);
		System.out.println(friendIco);
		// 封装朋友对象
		if (objfriend == null) {
			objfriend = new Friend(friendUserId, friendUserName, iconFile,
					InetAddress.getByName(Client.client_IP).toString());
		}
		setIconImage(Toolkit.getDefaultToolkit().getImage(Client.class.getResource(iconFile)));

		String title="";
		if (isGroupChart) {
			title = " 与全体成员聊天中喵...";
		}else{
			title = String.format("与%s聊天中喵...", friendUserName);
		}
		
		setTitle(title);
		setResizable(false);
		initView(objfriend);
		// getContentPane().setLayout(null);

		// 接收消息子线程
		Thread receiveMessageThread = new Thread(this);
		receiveMessageThread.start();
		// 注册窗口事件
		addWindowListener(new WindowAdapter() {
			// 单击窗口关闭按钮时调用
			public void windowClosing(WindowEvent e) {
				isRunning = false;
				setVisible(false);
				// 重启好友列表线程
				friendsFrame.resetThread();
			}
		});
	}

	public ChatActivity() {
		initView(null);
	}

	private void initView(Friend objfriend) {
		setBackground(new Color(220, 238, 247));
		this.setSize(520, 470);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(102, 187, 226));
		panel.setBounds(0, 0, 520, 90);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel headimage = new JLabel();
		headimage.setBounds(10, 10, 46, 46);
		if (!objfriend.friend_icon.isEmpty()) {
			// headimage.setIcon(new ImageIcon(pathImage));
			headimage.setIcon(new ImageIcon(Objects.requireNonNull(Client.class.getResource(objfriend.friend_icon))));
		} else {
			headimage.setIcon(new ImageIcon("image/HeadIcon.jpg"));
		}
		panel.add(headimage);

		JLabel lblNewLabel_3 = new JLabel("None");
		if (!objfriend.friend_name.isEmpty()) {
			lblNewLabel_3.setText(objfriend.friend_name);
		}
		lblNewLabel_3.setFont(new Font("宋体", Font.BOLD, 16));
		lblNewLabel_3.setBounds(66, 20, 54, 15);
		panel.add(lblNewLabel_3);

		JButton filetranfer = new JButton();
		filetranfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (count == 0) {
					if (fileTransferMainActivity == null) {
						fileTransferMainActivity = new FileTransferMainActivity();
					}
					fileTransferMainActivity.setVisible(true);
				} else {
					JOptionPane.showOptionDialog(null, "文件传输每启动一次只能使用一次,你已经使用过了喵!", "提示", JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, null, null);
				}
			}
		});
		filetranfer.setBounds(10, 61, 22, 22);
		filetranfer.setIcon(new ImageIcon("image/FileTransmission.png"));
		filetranfer.setOpaque(false);
		panel.add(filetranfer);

		JLabel lblNewLabel = new JLabel("电脑和Pad,手机互传文件吗?");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 449, 160, 15);
		getContentPane().add(lblNewLabel);

		JButton btnNewButton = new JButton("关闭");
		btnNewButton.setBackground(new Color(235, 246, 250));
		btnNewButton.setForeground(Color.BLACK);
		btnNewButton.setBounds(172, 445, 62, 23);
		btnNewButton.setBorderPainted(false);
		getContentPane().add(btnNewButton);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(232, 244, 249));
		panel_2.setBounds(0, 319, 325, 28);
		getContentPane().add(panel_2);
		panel_2.setLayout(null);

		if (!isGroupChart) {
			
			JPanel panel_4 = new JPanel();
			panel_4.setBackground(new Color(194, 226, 239));
			panel_4.setBounds(325, 89, 195, 25);
			getContentPane().add(panel_4);
			
			JLabel lblNewLabel_1 = new JLabel("个人信息");
			lblNewLabel_1.setFont(new Font("宋体", Font.BOLD, 12));
			panel_4.add(lblNewLabel_1);

			JPanel panel_5 = new JPanel();
			panel_5.setBackground(new Color(239, 246, 249));
			panel_5.setBounds(325, 114, 175, 325);
			getContentPane().add(panel_5);
			panel_5.setLayout(null);

			JLabel lblNewLabel_2 = new JLabel("姓名:");
			lblNewLabel_2.setFont(new Font("宋体", Font.BOLD, 14));
			lblNewLabel_2.setBounds(26, 57, 43, 15);
			panel_5.add(lblNewLabel_2);

			JLabel user_name = new JLabel("None");
			if (!objfriend.friend_name.isEmpty()) {
				user_name.setText(objfriend.friend_name);
			}
			user_name.setFont(new Font("宋体", Font.BOLD, 14));
			user_name.setBounds(69, 57, 54, 15);
			panel_5.add(user_name);

			JLabel lblNewLabel_4 = new JLabel("neko号:\r\n");
			lblNewLabel_4.setFont(new Font("宋体", Font.BOLD, 14));
			lblNewLabel_4.setBounds(26, 87, 90, 15);
			panel_5.add(lblNewLabel_4);

			JLabel user_section = new JLabel("None");
			if (!objfriend.friend_id.isEmpty()) {
				user_section.setText(objfriend.friend_id);
			}
			user_section.setFont(new Font("宋体", Font.BOLD, 14));
			user_section.setBounds(110, 87, 54, 15);
			panel_5.add(user_section);

			JLabel lblNewLabel_8 = new JLabel("邮箱:");
			lblNewLabel_8.setFont(new Font("宋体", Font.BOLD, 14));
			lblNewLabel_8.setBounds(26, 117, 43, 15);
			panel_5.add(lblNewLabel_8);

			JLabel user_postbox = new JLabel("None\r\n");
			if (!objfriend.friend_id.isEmpty()) {
				user_postbox.setText(objfriend.friend_id + "@neko.com");
			}
			user_postbox.setFont(new Font("宋体", Font.BOLD, 14));
			user_postbox.setBounds(69, 117, 130, 15);
			panel_5.add(user_postbox);

			JLabel lblNewLabel_10 = new JLabel("IP地址:");
			lblNewLabel_10.setFont(new Font("宋体", Font.BOLD, 14));
			lblNewLabel_10.setBounds(26, 147, 54, 15);
			panel_5.add(lblNewLabel_10);

			JLabel user_IpAddress = new JLabel("None");
			if (!objfriend.ipAddress.isEmpty()) {
				user_IpAddress.setText(objfriend.ipAddress);
			}
			user_IpAddress.setFont(new Font("宋体", Font.BOLD, 12));
			user_IpAddress.setBounds(79, 147, 86, 15);
			panel_5.add(user_IpAddress);
		}else{
			JPanel panel_4 = new JPanel();
			panel_4.setBackground(new Color(194, 226, 239));
			panel_4.setBounds(325, 89, 195, 25);
			getContentPane().add(panel_4);

			JLabel lblNewLabel_1 = new JLabel("在线用户");
			lblNewLabel_1.setFont(new Font("宋体", Font.BOLD, 12));
			panel_4.add(lblNewLabel_1);
			// 用户名,ip:
			JPanel panel_5 = new JPanel();
			panel_5.setBackground(new Color(239, 246, 249));
			panel_5.setBounds(325, 114, 175, 325);
			getContentPane().add(panel_5);
			panel_5.setLayout(null);


			// 获取在线群聊信息
			List<ClientInfo> list=Server.clientList;
			int y = 10;
			// user_id,user_pwd,user_name
			for (Map<String, String> map : all_user) {
				// 在线
				System.err.println(map);
				String user_name = map.get("user_name");
				String info ="";
				if (map.get("state")!=null && Integer.parseInt(map.get("state")) == 1 && !(user_name.equals("群聊"))) {
					info = user_name+"  "+"在线";
					JLabel lblNewLabel_2 = new JLabel(info);
					lblNewLabel_2.setFont(new Font("宋体", Font.BOLD, 14));
					lblNewLabel_2.setBounds(10, y, 200, 15);
					panel_5.add(lblNewLabel_2);
					y+=25;
				}
			}
			System.out.println(list.size());
		}
		


		txtInfo = new JTextPane();
		// 屏蔽回车换行
		KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
		txtInfo.getInputMap().put(enter, "none");
		// 用户是否按下回车按钮
		txtInfo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ((char) e.getKeyChar() == KeyEvent.VK_ENTER) {
					if (!mulu.trim().isEmpty()) {
						sendMessage(mulu);
						mulu = "";
					} else {
						sendMessage(txtInfo.getText().trim());
					}
					txtInfo.setText("");
				}
			}
		});
		new DropTarget(txtInfo, new DropTargetAdapter() {

			public void drop(DropTargetDropEvent arg0) {
				ChatActivity.this.drop(arg0);
			}
		});
		txtInfo.setBounds(0, 348, 325, 91);
		getContentPane().add(txtInfo);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setBackground(new Color(235, 246, 250));
		comboBox.setModel(new DefaultComboBoxModel<>(new String[] { "发送", "关闭" }));
		comboBox.setBounds(244, 446, 62, 23);
		getContentPane().add(comboBox);

		txtMainInfo = new JTextPane();
		txtMainInfo.setEditable(false);
		txtMainInfo.setBounds(0, 89, 325, 230);
		txtMainInfo.setCaretPosition(txtMainInfo.getStyledDocument().getLength());
		getContentPane().add(txtMainInfo);

		// 下滑
		JScrollPane scrollPane = new JScrollPane(txtMainInfo);
		scrollPane.setBounds(0, 89, 325, 230);
		scrollPane.setPreferredSize(new Dimension(325, 230));
		getContentPane().add(scrollPane);
	}

	// 发送消息
	private void sendMessage(String sendstr) {
		// 发送内容不为空
		if (!sendstr.isEmpty()) {
			// 判断当前的用户是否为群聊里发送
			if (friendUserId.equals("1024")) {
				sned_GroupChart(sendstr);
			} else {
				sned_user(sendstr);
			}
		}
	}

	private void sned_GroupChart(String sendstr) {
		// 获得当前时间，并格式化
		String date = dateFormat.format(new Date());
		String info = String.format("#%s#" + "%s在%s里发言：\n%s", date, current_User, friendUserName, sendstr);
		infoLog.append(info).append('\n');
		if (ChatActivityUtil.isFilelink(sendstr)) {
			String infodata = String.format("#%s#" + "%s在%s里发言：\n", date, current_User, friendUserName);
			insert(infodata);
			insertImage(sendstr, txtMainInfo);
		} else {
			// 发送文本
			insert(info + "\n");
		}

		Map<String, String> message = new HashMap<String, String>();
		message.put("receive_user_id", friendUserId);
		message.put("user_id", userId);
		message.put("message", sendstr);

		JSONObject jsonObj = new JSONObject(message);
		jsonObj.put("command", Server.COMMAND_GROUPCHART);

		try {
			InetAddress address = InetAddress.getByName(Client.client_IP);
			/* 发送数据报 */
			byte[] b = jsonObj.toString().getBytes();
			DatagramPacket packet = new DatagramPacket(b, b.length, address, Client.client_PORT);
			System.err.println(Client.client_IP);
			Client.socket.send(packet);
		} catch (IOException ignored) {
		}
	}

	private void sned_user(String sendstr) {
		// 获得当前时间，并格式化
		String date = dateFormat.format(new Date());
		String info = String.format("#%s#" + "您对%s说：\n%s", date, friendUserName, sendstr);
		infoLog.append(info).append('\n');
		 if (ChatActivityUtil.isFilelink(sendstr)) {
			String infodata = String.format("#%s#" + "您对%s说：\n", date, friendUserName);
			insert(infodata);
			insertImage(sendstr, txtMainInfo);
		} else {
			// 发送文本
			insert(info + "\n");
		}

		Map<String, String> message = new HashMap<String, String>();
		message.put("receive_user_id", friendUserId);
		message.put("user_id", userId);
		message.put("message", sendstr);

		JSONObject jsonObj = new JSONObject(message);
		jsonObj.put("command", Server.COMMAND_SENDMSG);

		try {
			InetAddress address = InetAddress.getByName(Client.client_IP);
			/* 发送数据报 */
			byte[] b = jsonObj.toString().getBytes();
			DatagramPacket packet = new DatagramPacket(b, b.length, address, Client.client_PORT);
			Client.socket.send(packet);

		} catch (IOException ignored) {

		}
	}

	// 用户拖拽的文件路径
	protected void drop(DropTargetDropEvent arg0) {
		arg0.acceptDrop(3);// 接受滴 没这句就会报错 3表示移动or复制
		List<?> list = null;
		try {
			list = (List<?>) arg0.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
			// get可转让的 get数据传输 数据的味道。java文件列表的味道
			File fi = (File) list.get(0);
			mulu = fi.getPath();
			// 显示图片
			insertImage(mulu, txtInfo);
		} catch (UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
	}

	// 接受消息
	@Override
	public void run() {

		while (isRunning) {
			if (friendUserId.equals("1024")) {
				getDataGroupChart();
			} else {
				getData_PrivateChart();
			}
		}
	}

	private void getDataGroupChart() {
		// 准备一个缓冲区
		byte[] buffer = new byte[1024];
		try {
			if (address == null) {
				address = InetAddress.getByName(Client.client_IP);
			}
			// System.out.println(Client.SERVER_PORT);
			/* 接收数据报 */
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);

			// 开始接收
			Client.socket.receive(packet);
			// 接收数据长度
			int len = packet.getLength();
			String str = new String(buffer, 0, len);

			// 打印接收的数据
			System.out.printf("从服务器接收的数据：【%s】\n", str);
			JSONObject jsonObj = new JSONObject(str);

			// 获得当前时间，并格式化
			String date = dateFormat.format(new Date());
			String message = (String) jsonObj.get("message");
			String sendUserName = Objects.requireNonNull(new UserDao().findById((String) jsonObj.get("user_id"))).get("user_name");
			String infoStr = String.format("#%s#" + "%s在%s里发言:\n %s", date, sendUserName, friendUserName, message);
			if (ChatActivityUtil.isFilelink(message)) {
				String infodata = String.format("#%s#" + "%s在%s里发言：\n", date, sendUserName, friendUserName);
				insert(infodata);
				// 判断是否为链接
				insertImage(message, txtMainInfo);
			} else {
				insert(infoStr + "\n");
			}
			Thread.sleep(100);

		} catch (Exception ignored) {
		}
	}

	private void getData_PrivateChart() {
		// 准备一个缓冲区
		byte[] buffer = new byte[1024];
		try {

			InetAddress address = InetAddress.getByName(Client.client_IP);
			/* 接收数据报 */
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, Client.client_PORT);

			// 开始接收
			Client.socket.receive(packet);
			// 接收数据长度
			int len = packet.getLength();
			String str = new String(buffer, 0, len);

			// 打印接收的数据
			System.out.printf("从服务器接收的数据：【%s】\n", str);
			JSONObject jsonObj = new JSONObject(str);

			// 获得当前时间，并格式化
			String date = dateFormat.format(new Date());
			String message = (String) jsonObj.get("message");
			String infoStr = String.format("#%s#" + "%s对您说：\n%s", date, friendUserName, message);
			if (ChatActivityUtil.isFilelink(message)) {
				String infodata = String.format("#%s#" + "您对%s说：\n", date, friendUserName);
				insert(infodata);
				// 判断是否为链接
				insertImage(message, txtMainInfo);
			} else {
				insert(infoStr + "\n");
			}
			Thread.sleep(100);

		} catch (Exception ignored) {
		}
	}

	private void insert(String sendStr) {
		try { // 插入文本
			StyledDocument doc = (StyledDocument) txtMainInfo.getDocument();
			Style style = doc.addStyle("StyleName", null);
			doc.insertString(doc.getLength(), sendStr, style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private void insertImage(String path, JTextPane textPane) {
		StyledDocument doc = (StyledDocument) textPane.getDocument();
		textPane.setCaretPosition(doc.getLength()); // 设置插入位置
		textPane.insertIcon(new ImageIcon(path));
		insert("\n");
	}

}
