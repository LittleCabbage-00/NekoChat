package com.myneko.neko;

import com.myneko.neko_chat_interface.ClientInfo;
import com.myneko.dev_operation.ReaderFileString;
import com.myneko.dev_operation.SaveFileString;
import com.myneko.dev_utils.ValidateUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientGUI extends JFrame {
	private JTextField inputIp;
	private JTextField inputPort;
    public static ClientInfo clientInfo;

  public static void main(String[] args) throws IOException {
	  ClientGUI setparametersFrame=new ClientGUI();
	  setparametersFrame.setVisible(true);
  }
	/**
	 * Create the activity.
	 */
	public ClientGUI() throws IOException {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setType(Type.UTILITY);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
			  System.exit(0);
			}
		});
	     createView();
	}
	private void createView() throws IOException {
		//获取端口号
		ArrayList<String> readerUserInfo= ReaderFileString.readerFilestr("clientinfo.txt");
		assert readerUserInfo != null;
		if (readerUserInfo.size()>0) {
			  clientInfo=new ClientInfo();
			  clientInfo.port = new Integer(readerUserInfo.get(1));
			  JOptionPane.showOptionDialog(null,
					  "IP地址格式为127.0.0.x"+ "\r\n请不要超过255，不然跑不起来>_<喵~"+"\r\n\n检测到服务器端口为"+ clientInfo.port,
					  "注意", JOptionPane.DEFAULT_OPTION,
						 JOptionPane.INFORMATION_MESSAGE,
					  null, null, null);
		}
		setBounds(100, 100, 440, 270);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JLabel lblNewLabel = new JLabel("IP地址:");
		lblNewLabel.setForeground(Color.black);
		lblNewLabel.setBackground(Color.black);
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		lblNewLabel.setBounds(86, 59, 54, 15);
		contentPane.add(lblNewLabel);
		
		inputIp = new JTextField();
		inputIp.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		inputIp.setForeground(Color.black);
		inputIp.setOpaque(false);
		inputIp.setBounds(150, 57, 188, 21);
		contentPane.add(inputIp);
		inputIp.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("端口号:");
		lblNewLabel_1.setForeground(Color.black);
		lblNewLabel_1.setBackground(Color.black);
		lblNewLabel_1.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(86, 113, 54, 15);
		contentPane.add(lblNewLabel_1);
		
		inputPort = new JTextField();
		//inputPort.setEditable(false);
		inputPort.setForeground(Color.black);
		inputPort.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		inputPort.setOpaque(false);
		if (clientInfo!=null) {
			inputPort.setText(Integer.toString(clientInfo.port));
		}
		inputPort.setBounds(150, 111, 188, 21);
		contentPane.add(inputPort);
		inputPort.setColumns(10);
		
		JButton btn_setingParamete = new JButton("设置");
		btn_setingParamete.setOpaque(false);
		btn_setingParamete.setForeground(Color.black);
		  //#05BAFB
		btn_setingParamete.setBackground(new Color(5,186,251));//设置登录按钮字体颜色
		//设置点击事件
		btn_setingParamete.addActionListener(e -> {
			//数据验证
			if (inputIp.getText().trim().isEmpty()) {
				JOptionPane.showOptionDialog(null, "IP不能为空喵~","提示", JOptionPane.DEFAULT_OPTION,
						 JOptionPane.INFORMATION_MESSAGE,null, null, null);
				return;
			}

			if (inputPort.getText().trim().isEmpty()) {
				JOptionPane.showOptionDialog(null, "请启动服务器喵~","提示", JOptionPane.DEFAULT_OPTION,
						 JOptionPane.INFORMATION_MESSAGE,null, null, null);
				return;
			}

			//通过正则表达式认证
			if (!ValidateUtil.isIPLegal(inputIp.getText().trim())) {
				JOptionPane.showOptionDialog(null, "IP不符合规则喵~","提示", JOptionPane.DEFAULT_OPTION,
						 JOptionPane.INFORMATION_MESSAGE,null, null, null);
				return;
			}
			System.out.println(inputIp.getText().trim().substring(0,inputIp.getText().trim().indexOf(".")));

			// 设置客户端IP
			Client.client_IP=inputIp.getText().trim();
			try {
				clientInfo.address = InetAddress.getByName(inputIp.getText().trim());
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
			//将IP写入文件中去
			SaveFileString.SaveFileStr(inputIp.getText().trim(),Integer.toString(clientInfo.port), "clientinfo.txt", false);
			Client.client_PORT=new Integer(inputPort.getText().trim());
			Client.tag=1;
			//关闭当前窗体
			close();
			Client.start();

		});
		btn_setingParamete.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		btn_setingParamete.setBounds(173, 183, 93, 23);
		contentPane.add(btn_setingParamete);
		 //设置背景
        Container cp=this.getContentPane();
        cp.setLayout(new BorderLayout());
        ((JPanel)cp).setOpaque(false); //注意这里，将内容面板设为透明。这样LayeredPane面板中的背景才能显示出来。
	}
	
	private void close() {
		this.setVisible(false);
	}
}
