package com.qq.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;//服务器

    public Server(){
        try {
            serverSocket = new ServerSocket(8080);//创建服务器，并指定端口号
            System.out.println("服务器启动成功，端口号："+serverSocket.getLocalPort());//打印启动信息
            while (true){
                //从服务器队列中获取一个socket连接
                Socket accept = serverSocket.accept();
                //打印队列中获取的socket连接的地址信息
                System.out.println("服务器开始处理请求："+accept.getInetAddress().getHostAddress()
                        +"//"+accept.getPort());
                //创建一个处理这个socket连接的线程
                ServerThread serverThread = new ServerThread(accept);
                //开启此线程
                new Thread(serverThread).start();
            }
        } catch (IOException e) {
            System.out.println("服务器启动失败！");//打印启动失败信息
            e.printStackTrace();
        } finally {
            //关闭服务器
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        //服务器启动入口
        new Server();
    }
}
