package com.myneko.neko_ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.myneko.neko_chat_interface.*;
import com.myneko.neko.Client;
import com.myneko.dev_method.UserDao;

public class LoginActivity extends JFrame {
	// 创建登入界面上的业务逻辑类
	UserDao objuserDao;
	// 创建实体对象
	User objuser;
	int tage = 0;
	// 定义所需要的组件
	JLabel jlbTop, jlbImg, jlbReg, jlbPsw,jpsdtips,jacctips,jartical_0;
	JPanel jp; // 用于界面底部区域
	JTextField jtf;// 账号
	JPasswordField jpf;// 密码
	JCheckBox box1, box2;
	JButton jbLogin;// 登陆按钮
	// 创建定时器
	Timer timer;

	public LoginActivity() {
		onCreateActivity();
	}

	public LoginActivity(int tage) {
		this.tage = tage;
		onCreateActivity();
	}

	private void onCreateActivity() {
		// 北部
		jlbTop = new JLabel(new ImageIcon("image/Loginheader.jpg"));
		// 中部
		jp = new JPanel();
		jp.setLayout(null);// 绝对layout布局

		jartical_0=new JLabel("永远相信");
		jartical_0.setBounds(3,0,70,70);
		jartical_0.setForeground(Color.gray);
		jp.add(jartical_0);
		jartical_0=new JLabel("美好的事情");
		jartical_0.setBounds(3,15,90,70);
		jartical_0.setForeground(Color.gray);
		jp.add(jartical_0);
		jartical_0=new JLabel("即将发生");
		jartical_0.setBounds(3,30,70,70);
		jartical_0.setForeground(Color.gray);
		jp.add(jartical_0);

		jacctips = new JLabel("账号");
		jacctips.setBounds(77, 15, 45, 15);
		jpsdtips = new JLabel("密码");
		jpsdtips.setBounds(77, 45, 45, 15);

		jtf = new JTextField(15);
		jtf.setBounds(110, 10, 265, 30);
		jpf = new JPasswordField(15);
		jpf.setBounds(110, 40, 265, 30);
		box1 = new JCheckBox("记住密码");
		box1.setBounds(110, 75, 90, 15);
		box2 = new JCheckBox("自动登录");
		box2.setBounds(210, 75, 90, 15);

		jlbReg = new JLabel("注册账号");
		jlbReg.setBounds(20, 110, 60, 15);
		jlbReg.setForeground(Color.blue);
		jlbPsw = new JLabel("找回密码");
		jlbPsw.setBounds(310, 75, 60, 15);
		jlbPsw.setForeground(Color.black);

		// 南部
		jbLogin = new JButton(new ImageIcon("image/Login_1.png"));
		jbLogin.setBounds(110, 100, 263, 32);

		// 添加组件

		jp.add(jtf);
		jp.add(jpf);
		jp.add(box1);
		jp.add(box2);
		jp.add(jlbReg);
		jp.add(jlbPsw);
		jp.add(jbLogin);
		jp.add(jacctips);
		jp.add(jpsdtips);

		// 添加组件到JFrame
		this.add(jlbTop, "North");
		this.add(jp, "Center");
		// 设置窗体属性
		this.setTitle("neko酱的入口喵~");
		this.setIconImage(new ImageIcon("Image/Logo.jpg").getImage());
		this.setSize(400, 350);
		// 居中显示
		this.setLocationRelativeTo(null);
		// 窗体大小不能改变
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// 注册登录按钮事件监听器
		jbLogin.addActionListener(e -> {
			if (objuserDao.Loginstatus(jtf.getText())) {
				JOptionPane.showMessageDialog(null, "该用户已在线!");
				return;
			} else {
				// 进行登入
				login();
			}

		});

		// 为注册添加点击事件
		// 注册账号鼠标事件
		jlbReg.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				jlbReg.setText("注册账号");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				jlbReg.setCursor(new Cursor(Cursor.HAND_CURSOR));
				jlbReg.setText("<html><u>注册账号</u><html>");
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// 实例化注册页面
				RegistrationActivity register = new RegistrationActivity();
				// 关闭当前窗体
				close();
				// 显示注册页面
				register.setVisible(true);
			}

		});

		// qq找回密码鼠标事件
		jlbPsw.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {
				jlbPsw.setText("找回密码");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				jlbPsw.setCursor(new Cursor(Cursor.HAND_CURSOR));
				jlbPsw.setText("<html><u>找回密码</u><html>");
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// 实例化找回密码界面
				RetrievePasswordActivity retrievePasswordActivity = new RetrievePasswordActivity();
				// 关闭当前窗体
				close();
				// 显示找回密码界面
				retrievePasswordActivity.setVisible(true);
			}

		});

		// 如果用户选择自动登入，将记住密码单选框勾选
		box2.addActionListener(e -> {
			if (box2.isSelected()) {
				box1.setSelected(true);
			}
		});

		// 记住密码
		box1.addActionListener(e -> {

			if (!box1.isSelected()) {

				box1.setSelected(false);
				if (box2.isSelected()) {
					box2.setSelected(false);
				}
			}

		});

		// 实现界面监听事件
		this.addWindowListener(new WindowAdapter() {
			// 窗体打开时激活
			@Override
			public void windowOpened(WindowEvent e) {
				System.out.println("获得焦点");
				// 读取状态
				if (objuserDao == null) {
					objuserDao = new UserDao();
				}
				ArrayList<String> readerState = objuserDao.readerAutoLoginState();
				if (readerState != null) {
					box1.setSelected(readerState.get(0).trim().equals("1"));

					box2.setSelected(readerState.get(1).trim().equals("1"));
				}
				autoLoginActivity();
			}

			private void autoLoginActivity() {
				// 如果为自动登入状态进行登入
				if (box2.isSelected()) {
					System.out.println(Client.client_PORT + ";" + Client.client_IP);
					// 读取账号和密码
					ArrayList<String> readerUserInfo = objuserDao.readerUserInfo();
					assert readerUserInfo != null;
					if (readerUserInfo.size() > 0) {
						jtf.setText(readerUserInfo.get(0));
						jpf.setText(readerUserInfo.get(1));
						// 判断用户是否登入
						if (objuserDao.Loginstatus(jtf.getText())) {
							JOptionPane.showMessageDialog(null, "该用户已在线!");
							return;
						} else {
							// 进行自动登入
							if (tage == 0) {
								if (timer == null) {
									timer = new Timer();
								}
								if (objuser == null) {
									objuser = new User();
									objuser.user_id = readerUserInfo.get(0);
									objuser.user_pwd = readerUserInfo.get(1);
								}
								timer.schedule(task, 100, 1000000);
							}
						}
					}
				}
			}

			// 窗体图标事件
			@Override
			public void windowIconified(WindowEvent e) {
			}

			// 窗体还原触发
			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			// 窗体停用事件
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			// 正在关闭
			@Override
			public void windowClosing(WindowEvent e) {
				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				int n = JOptionPane.showConfirmDialog(null, "你确定要关闭当前程序?", "警告", JOptionPane.YES_NO_OPTION); // 返回值为0或1
				if (n == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}

			// 已经关闭
			@Override
			public void windowClosed(WindowEvent e) {
			}

			// 窗体激活事件
			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
	}

	public void close() {
		this.setVisible(false);
	}

	// 执行定时器上的业务
	TimerTask task = new TimerTask() {

		@Override
		public void run() {
			if (objuser != null) {
				LoginOnStartActivity loginOnStartActivity = new LoginOnStartActivity(objuser);
				// 保存用户登入状态
				SaveState();
				loginOnStartActivity.setVisible(true);
				close();
			}
		}
	};

	private void dataValidation(String userId, String password) {
		// 数据验证部分
		if (userId.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "账号不能为空!");
			return;
		}
		if (password.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "密码不能为空!");
			return;
		}
	}

	// 客户端向服务器发送登录请求
	public void login() {
		if (objuser == null) {
			objuser = new User();
		}
		if (objuserDao == null) {
			objuserDao = new UserDao();
		}
		// 先进行用户输入验证，验证通过再登录
		dataValidation(jtf.getText(), new String(jpf.getPassword()));
		objuser.user_id = jtf.getText();
		objuser.user_pwd = new String(jpf.getPassword());
		Map usermap = objuserDao.login(objuser);
		System.err.println("从数据库中返回的数据"+usermap);

		if (usermap != null) {
			// 登录成功调转界面
			System.out.println("登录成功调转界面");
			// 更改用户状态
			objuser.state = "1";// 设为在线
			objuserDao.Changestatu(objuser);
			// 保存用户界面上单选框的状态
			SaveState();
			// 设置登录窗口不可见
			this.setVisible(false);
			// FriendsFrame frame = new FriendsFrame(usermap);
			MainActivity frame = new MainActivity(usermap);
			frame.setVisible(true);

		} else {
			JOptionPane.showMessageDialog(null, "账号或密码不正确!");
		}

	}

	// 保存状态
	private void SaveState() {
		boolean saveUserInfoState = false, autoLogin = false;
		// 判断用户是否点击了保存密码
		if (box1.isSelected()) {
			// 保存密码
			objuserDao.SaveUserInfo(objuser.user_id, objuser.user_pwd);
			saveUserInfoState = true;
		}

		// 判断用户是否点击了单选框上的状态
		if (box2.isSelected()) {
			autoLogin = true;
		}
		// 保存用户界面上的状态
		objuserDao.Savestate(saveUserInfoState, autoLogin);
	}

}
