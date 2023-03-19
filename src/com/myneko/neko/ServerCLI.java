package com.myneko.neko;

import com.myneko.neko_chat_interface.ClientInfo;
import com.myneko.dev_operation.SaveFileString;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.UnknownHostException;
import java.sql.SQLException;

public class ServerCLI extends JFrame {

	private JTextField inputPort;
	public static  ClientInfo  clientinfo;

	public static void main(String[] args) {
		ServerCLI setparametersFrame = new ServerCLI();
		setparametersFrame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public ServerCLI() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		createView();
	}

	private void createView() {
		setType(Type.UTILITY);
		setBounds(100, 100, 440, 270);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("端口号:");
		lblNewLabel_1.setForeground(Color.black);
		lblNewLabel_1.setBackground(Color.black);
		lblNewLabel_1.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(75, 77, 54, 15);
		contentPane.add(lblNewLabel_1);

		inputPort = new JTextField();
		inputPort.setForeground(Color.black);
		inputPort.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		inputPort.setOpaque(false);
		inputPort.setBounds(139, 73, 188, 21);
		contentPane.add(inputPort);
		inputPort.setColumns(10);

		JButton btn_setingParamete = new JButton("设置");
		btn_setingParamete.setOpaque(false);
		btn_setingParamete.setForeground(Color.black);
		// #05BAFB
		btn_setingParamete.setBackground(new Color(5, 186, 251));// 设置登录按钮字体颜色
		// 设置点击事件
		btn_setingParamete.addActionListener(e -> {
			if (inputPort.getText().trim().isEmpty()) {
				return;
			}

			clientinfo=new ClientInfo();
			clientinfo.port = new Integer(inputPort.getText().trim());
			//将端口写入到指定文件中
			SaveFileString.SaveFileStr("0", Integer.toString(clientinfo.port), "clientinfo.txt", false);
			// 关闭当前窗体
			close();
			try {
				Server.Start();
			} catch (SQLException | UnknownHostException e1) {
				e1.printStackTrace();
			}

		});
		btn_setingParamete.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		btn_setingParamete.setBounds(146, 127, 93, 23);
		contentPane.add(btn_setingParamete);
		// 设置背景
		Container cp = this.getContentPane();
		((JPanel) cp).setOpaque(false); // 注意这里，将内容面板设为透明。这样LayeredPane面板中的背景才能显示出来。
	}

	private void close() {
		this.setVisible(false);
	}
}
