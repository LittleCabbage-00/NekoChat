package com.myneko.neko_ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;


public class SendFileActivity extends JFrame {

	private final int port = 65;

	boolean run=false;
	//获取文件文本
	JTextArea textField;
	//文件路径
	private  String mulu="";
	JLabel connecttionState;
	JButton sendFile;
	/**
	 * Create the frame.
	 */
	public SendFileActivity() {
		setType(Type.UTILITY);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		
		 this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		JPanel contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("文件路径: (把文件拖到这里)");
		lblNewLabel.setForeground(Color.black);
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		lblNewLabel.setBounds(10, 159, 245, 24);
		contentPane.add(lblNewLabel);
		
		textField = new JTextArea();
		textField.setEditable(false);
		textField.setBounds(10, 196, 414, 24);
		textField.setColumns(10);
		contentPane.add(textField);
		
        new DropTarget(textField,new DropTargetAdapter() {
			
			public void drop(DropTargetDropEvent arg0) {
				SendFileActivity.this.drop(arg0);
			}
		});
		
        sendFile = new JButton("发送");
        sendFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//数据验证
				if (mulu.trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "请将要传输的文件拖放到指定区域里");
				
				}else
				{
					sendFile.setEnabled(false);
					
					run=true;
					//开始进行连接
					start(mulu);//这里端口我使用的是固定值
					//验证客服机是否有设备在连接
					//System.out.println("你点击了改按钮");
				}
			}
		});
        sendFile.setBounds(331, 228, 93, 23);
		contentPane.add(sendFile);
		
		JLabel lblNewLabel_1 = new JLabel("当前连接状态:");
		lblNewLabel_1.setForeground(Color.black);
		lblNewLabel_1.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(58, 73, 149, 28);
		contentPane.add(lblNewLabel_1);
		
		 connecttionState = new JLabel("无连接...");
		 connecttionState.setForeground(Color.black);
		connecttionState.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		connecttionState.setBounds(202, 73, 86, 24);
		contentPane.add(connecttionState);
	}

	//回去用户拖拽的文件路径
	protected void drop(DropTargetDropEvent arg0) {
		arg0.acceptDrop(3);//接受 没这句就会报错 3表示移动or复制
		List<?> list=null;
		try {
			list = (List<?>) arg0.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
								//get可转让的		get数据传输		数据的味道。java文件列表的味道
			File fi=(File)list.get(0);
			mulu=fi.getPath();
			textField.setText(mulu);
		} catch (UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
	}
	
	 //发文件
    void start(String filePath) {
        Socket s;
        try {
            ServerSocket ss = new ServerSocket(65);
            while (run) {
                // 选择进行传输的文件
                File fi = new File(filePath);

                System.out.println("文件长度:" + (int) fi.length());

                // public Socket accept() throws
                // IOException侦听并接受到此套接字的连接。此方法在进行连接之前一直阻塞。
                //等待对方连接
                connecttionState.setText("等待客服机进行连接...");
                System.out.println("等待客服机进行连接...");
                s = ss.accept();
                //连接成功
                //System.out.println("建立socket链接");
                connecttionState.setText("建立socket链接");
                DataInputStream dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                dis.readByte();

                DataInputStream fis = new DataInputStream(new BufferedInputStream(Files.newInputStream(Paths.get(filePath))));
                DataOutputStream ps = new DataOutputStream(s.getOutputStream());
                //将文件名及长度传给客户端。这里要真正适用所有平台，例如中文名的处理，还需要加工，具体可以参见Think In Java 4th里有现成的代码。
                ps.writeUTF(fi.getName());
                ps.flush();
                ps.writeLong((long) fi.length());
                ps.flush();

                int bufferSize = 8192;
                byte[] buf = new byte[bufferSize];

                while (true) {
                    int read = 0;
					read = fis.read(buf);

					if (read == -1) {
                        break;
                    }
                    ps.write(buf, 0, read);
                }
                ps.flush();
                // 注意关闭socket链接哦，不然客户端会等待server的数据过来，
                // 直到socket超时，导致数据不完整。                
                fis.close();
                s.close();  
                connecttionState.setText("文件传输完成");
                System.out.println("文件传输完成");
                JOptionPane.showMessageDialog(null, "文件发送完毕");
                textField.setText("");
                ChatActivity.count=1;
                sendFile.setEnabled(true);
                break;
            }
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void close() {
    	FileTransferMainActivity.sendFileActivity =null;
    	run=false;
    	this.setVisible(false);
	}
}
