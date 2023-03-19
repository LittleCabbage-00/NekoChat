package com.myneko.neko_ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;

public class FileTransferMainActivity extends JFrame {

	//接收界面
	public static ClientActivity client;
    //发送界面
	public static SendFileActivity sendFileActivity;
	/**
	 * Create the frame.
	 */
	public FileTransferMainActivity() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//当前窗体可不显示
				close();
			}
		});
		// 窗体大小不能改变
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		JPanel contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("接收");
		btnNewButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
		btnNewButton.setForeground(Color.BLACK);
		//btnNewButton.setBackground(Color.WHITE);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (client==null) {
					 client=new ClientActivity();
				}
				JOptionPane.showOptionDialog(null, "程序启动一次文件传输只能传输一次喵~","提示", JOptionPane.DEFAULT_OPTION,
						 JOptionPane.INFORMATION_MESSAGE,null, null, null);
				close();
			    client.setVisible(true);
			}
		});
		btnNewButton.setBounds(49, 126, 118, 115);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("发送");
		//btnNewButton_1.setBackground(Color.WHITE);
		btnNewButton_1.setFont(new Font("微软雅黑", Font.BOLD, 20));
		btnNewButton_1.setForeground(Color.BLACK);
		btnNewButton_1.addActionListener(e -> {
			if (sendFileActivity ==null) {
				 sendFileActivity =new SendFileActivity();
			}
			JOptionPane.showOptionDialog(null, "程序启动一次文件传输只能传输一次喵~","提示", JOptionPane.DEFAULT_OPTION,
					 JOptionPane.INFORMATION_MESSAGE,null, null, null);
			close();
			sendFileActivity.setVisible(true);
		});
		btnNewButton_1.setBounds(259, 123, 118, 115);
		contentPane.add(btnNewButton_1);
		
		JLabel lblNewLabel = new JLabel("使用规则:");
		lblNewLabel.setForeground(Color.black);
		lblNewLabel.setBounds(22, 22, 63, 15);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("一端选择接收，一端选择发送，接收端选择保存的路径");
		lblNewLabel_1.setForeground(Color.black);
		lblNewLabel_1.setBounds(95, 22, 318, 15);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("发送端选择要发送的文件;注意接收端要等待发送端");
		lblNewLabel_2.setForeground(Color.black);
		lblNewLabel_2.setBounds(95, 47, 292, 15);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("进行发送后再进行接收，不然会出现异常。");
		lblNewLabel_3.setForeground(Color.black);
		lblNewLabel_3.setBounds(93, 72, 284, 15);
		contentPane.add(lblNewLabel_3);
	}

	public void close(){
		this.setVisible(false);
	}
}
