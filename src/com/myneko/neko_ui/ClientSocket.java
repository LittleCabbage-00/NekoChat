package com.myneko.neko_ui;


import java.net.*;
import java.io.*;

public class ClientSocket {
    private final String ip;

    private final int port;

    private Socket socket = null;

    DataOutputStream out = null;

    DataInputStream getMessageStream = null;

    public ClientSocket(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * 创建socket连接
     * @throws Exception
     * exception
     */
    public void CreateConnection() throws Exception {
        try {
            socket = new Socket(ip, port);
        } catch (Exception e) {
            e.printStackTrace();
            if (socket != null)
                socket.close();
            throw e;
        } finally {
        }
    }

    public void sendMessage(String sendMessage) throws Exception {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            if (sendMessage.equals("Windows")) {
                out.writeByte(0x1);
                out.flush();
                return;
            }
            if (sendMessage.equals("Unix")) {
                out.writeByte(0x2);
                out.flush();
                return;
            }
            if (sendMessage.equals("Linux")) {
                out.writeByte(0x3);
                out.flush();
            } else {
                out.writeUTF(sendMessage);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (out != null)
                out.close();
            throw e;
        }
    }

    public DataInputStream getMessageStream() throws Exception {
        try {
            getMessageStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            return getMessageStream;
        } catch (Exception e) {
            e.printStackTrace();
            if (getMessageStream != null)
                getMessageStream.close();
            throw e;
        }
    }
}
