package com.myneko.neko_ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.myneko.neko_chat_interface.User;
import com.myneko.dev_method.UserDao;

public class LoginOnStartActivity extends JFrame{
	Timer timer;
	private final User objuser;
	public LoginOnStartActivity(User objuser) {
	  this.objuser=objuser;
	  showActivity();
	}
	//显示窗体
private void showActivity() {
	JButton btnregister=new JButton(new ImageIcon("image/LonginIngCancle.png"));
	this.add(btnregister);
	btnregister.setBounds(152,270,100,30);//150,300,100,30
	//设置窗体属性
	this.setTitle("正在进入全是neko酱的地方喵~");
	this.setIconImage(new ImageIcon("Image/Logo.jpg").getImage());
	//this.setSize(330,300);
	this.setSize(400,340);
	this.setBackground(new Color(255,255,255));//#FFFFFF
	//设置背景
	Container cp=this.getContentPane();
	cp.setLayout(new BorderLayout());
	((JPanel)cp).setOpaque(false); //注意这里，将内容面板设为透明。这样LayeredPane面板中的背景才能显示出来。
	// 居中显示
	this.setLocationRelativeTo(null);
	// 窗体大小不能改变
	 this.setResizable(false);
	 this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	 
	 
		//实现界面监听事件
		this.addWindowListener(new WindowListener() {
			//窗体打开时激活
			@Override
			public void windowOpened(WindowEvent e) {
				System.out.println("获取焦点");
				if (timer==null) {
					 timer=new Timer();	
				}
				timer.schedule(task, 200);
			}
			
			//窗体图标事件
			@Override
			public void windowIconified(WindowEvent e) {
			}
			
			
			//窗体还原触发
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			
			//窗体停用事件
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
			
			//正在关闭
			@Override
			public void windowClosing(WindowEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "正在登入,你确定要关闭?", "警告",JOptionPane.YES_NO_OPTION); //返回值为0或1
				if (n==JOptionPane.YES_OPTION) {
					LoginActivity frame=new LoginActivity(1);
					frame.setVisible(true);
					//System.exit(0);
					if (timer!=null) {
						timer.cancel();
						timer=null;
					}
					 close();
				}
			}
			
			//已经关闭
			@Override
			public void windowClosed(WindowEvent e) {
			}
			
			//窗体激活事件
			@Override
			public void windowActivated(WindowEvent e) {
			}

		});
		
		
		//取消按钮点击事件
		btnregister.addActionListener(e->
		{
			int n = JOptionPane.showConfirmDialog(null, "正在登入,你确定要取消?", "警告",JOptionPane.YES_NO_OPTION); //返回值为0或1
			if (n==JOptionPane.YES_OPTION) {
				LoginActivity frame=new LoginActivity(1);
				frame.setVisible(true);
				//System.exit(0);
				if (timer!=null) {
					timer.cancel();
					timer=null;
				}
				 close();
			}
		});
}




//执行定时器业务
TimerTask task=new TimerTask() {
	
	@Override
	public void run() {
		if (objuser!=null) {
			login(objuser);	
		}
	}
};


//关闭窗体
public void close() {
this.setVisible(false);
}

//客户端向服务器发送登录请求
	public void login(User objusre) {
				Map   usermap=new UserDao().login(objusre);
				if (usermap != null) {
					// 登录成功调转界面
					System.out.println("登录成功调转界面");
					//改变状态
					objuser.state = "1";
					new UserDao().Changestatu(objuser);
					// 设置登录窗口不可见
					this.setVisible(false);
					// 设置登录窗口可见
					MainActivity frame=new MainActivity(usermap);
					frame.setVisible(true);
				}else{
					JOptionPane.showMessageDialog(null, "登入失败!");
					close();
					LoginActivity loginActivity =new LoginActivity(1);
					loginActivity.setVisible(true);
				}
		
		}


}
