package com.myneko.neko_ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;

import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JProgressBar;
import java.awt.Color;

public class ClientActivity extends JFrame {

	//文件路径
	private final JTextField textField;
	//mulu:用户拖拽的路径
	String mulu;
	JButton connection;
	//进度条
	JProgressBar progressBar;

	
	    private ClientSocket cs = null;

	    boolean run=true;


	private boolean createConnection() {
	    	/*
	    	ip=Client.SERVER_IP;
	    	port=Client.SERVER_PORT;
	    	*/
			// 设置成服务器IP
			String ip = "localhost";
			int port = 65;
			cs = new ClientSocket(ip, port);
	        try {
	            cs.CreateConnection();
	            System.out.print("连接服务器成功!" + "\n");
	            return true;
	        } catch (Exception e) {
	            System.out.print("连接服务器失败!" + "\n");
	            return false;
	        }

	    }

	    private void sendMessage() {
	        if (cs == null)
	            return;
	        try {
				String sendMessage = "Windows";
				cs.sendMessage(sendMessage);
	        } catch (Exception e) {
	            System.out.print("发送消息失败!" + "\n");
	        }
	    }

	   

	//关闭窗体
	private void close() {
		FileTransferMainActivity.client=null;
		 run=false;
		this.setVisible(false);
	}

	/**
	 * Create the frame.
	 */
	public ClientActivity() {
		setBackground(Color.BLACK);
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
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		 connection = new JButton("连接");
		 connection.setFont(new Font("微软雅黑", Font.BOLD, 15));
		connection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//数据验证
				if (textField.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "请指定区域");
				}else
				{
					try {
						    if(createConnection())
						    {
						    	//发送消息
				                sendMessage();
				                connection.setEnabled(false);
				                run=true;
				                //获取消息
				                getMessage(textField.getText().trim()+"\\");
						    }

			        } catch (Exception ex) {
			            ex.printStackTrace();
			        }
				}
			}
		});
		connection.setBounds(159, 197, 93, 23);
		contentPane.add(connection);
		
		JLabel lblNewLabel = new JLabel("另存为目录:");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		lblNewLabel.setBounds(26, 79, 149, 28);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(26, 117, 374, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		//拖拽事件
		 new DropTarget(textField,new DropTargetAdapter() {
				
				public void drop(DropTargetDropEvent arg0) {
					ClientActivity.this.drop(arg0);
				}
			});
		 
		  progressBar = new JProgressBar();
		  progressBar.setForeground(Color.CYAN);
		  progressBar.setBackground(Color.GREEN);
		 progressBar.setBounds(26, 159, 374, 14);
		 progressBar.setValue(0);
		 progressBar.setStringPainted(true);
		 contentPane.add(progressBar);
	}
	
	
	//用户拖拽的文件路径
	protected void drop(DropTargetDropEvent arg0) {
		arg0.acceptDrop(3);//接受滴 没这句就会报错 3表示移动or复制
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
	//获取信息
	 private void getMessage(String path) {
	        if (cs == null)
	            return;
	        DataInputStream inputStream = null;
	        try {
	            inputStream = cs.getMessageStream();
	        } catch (Exception e) {
	            System.out.print("接收消息缓存错误\n");
	            return;
	        }

	        try {
	            //本地保存路径，文件名会自动从服务器端继承而来。
	        	String savePath;
	            int bufferSize = 8192;
	            byte[] buf = new byte[bufferSize];
	            int passedlen = 0;
	            long len=0;
	            
	            path+= inputStream.readUTF();
	            savePath=path;
	            System.out.println(savePath);
	            int count=0;
	            //处理文件重复情况
		        while (new File(savePath).exists()) {
		        	savePath=path;
		        	++count;
		        	  int index=savePath.indexOf(".");
			        	String frontStrPath=savePath.substring(0,index)+"("+count+")";
			        	String lastStrPath=savePath.substring(index);
			        	savePath=frontStrPath+lastStrPath;
				}
		           
	            DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream(new BufferedOutputStream(Files.newOutputStream(Paths.get(savePath)))));
	            len = inputStream.readLong();
	            
	            System.out.println("文件的长度为:" + len + "\n");
	            System.out.println("开始接收文件!" + "\n");
	                    
	            while (run) {
	                int read = 0;
					read = inputStream.read(buf);
					passedlen += read;
	                if (read == -1) {
	                    break;
	                }
	                //下面进度条本为图形界面的prograssBar做的，这里如果是打文件，可能会重复打印出一些相同的百分比
	                System.out.println("文件接收了" +  (passedlen * 100L / len) + "%\n");
	                //显示进度
	                progressBar.setValue( (int) (passedlen * 100/ len));
	                fileOut.write(buf, 0, read);
	            }
	            System.out.println("接收完成，文件存为" + savePath + "\n");
	            fileOut.close();
	            JOptionPane.showMessageDialog(null, "文件传输完成");
	            //表示已经使用
	            ChatActivity.count=1;
	            connection.setEnabled(true);
	            close();
	        } catch (Exception e) {
	            System.out.println("接收消息错误" + "\n");
	            return;
	        }
	    }
	 
	
}
