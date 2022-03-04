package com.qq.network;


import com.qq.control.Manager;
import com.qq.control.ManagerNetSource;
import com.qq.model.MsgBody;
import com.qq.util.RespondProtocol;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ServerThread implements Runnable {

    private Socket socket;//保存此账户的临时socket
    private Scanner scan;//保存此账户的临时输出
    private OutputStream outputStream;//此账户的临时回传流

    public ServerThread(Socket socket) {
        try {
            this.socket = socket;
            this.scan = new Scanner(socket.getInputStream());
            this.outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            if (scan.hasNext()) {
                //持续接收前台发来的数据
                String request = read();
                //将前台发来的数据交给处理方法
                Manager manager = new Manager(request);
                //获取处理方法返回的数据
                String respond = manager.doManager();
                //获取解析后的请求参数
                MsgBody msgBody = manager.getMsgBody();
                //回传信息给前台
                boolean isSuccession = respond(respond, msgBody);
                //是否下线
                if(!isSuccession){
                    break;
                }
            }
        }
        close();
    }

    /**
     * 此线程的socket的读方法
     */
    private String read() {
        return scan.nextLine();
    }

    /**
     * 通过处理方法反馈信息，向前台发送回传信息
     * @param respond 处理方法返回的数据
     * @param msgBody 解析后的请求参数
     * @return 此线程是否执行下去（账户是否要下线）
     */
    private boolean respond(String respond, MsgBody msgBody){
        //网络资源操作类
        ManagerNetSource netSource = ManagerNetSource.getInstance();
        //若返回null，则代表此前台账户要退出
        if (respond == null) {
            //客户端退出，删除服务器存储的此用户socket信息
            netSource.deleteSource(msgBody.getSubmitName());
            return false;
            //若返回addFriend，则代表此前台账户要添加好友
        }else if ("addFalse".equals(respond)||"delFalse".equals(respond)){
            //暂未处理添加好友失败和删除好友失败的回传信息
            return true;
        }else if ("addFriend".equals(respond)){
            //判断目标账户是否在线
            if(netSource.isContainsTargetName(msgBody.getTargetName())) {
                //将此客户端账户发送加好友请求给目标账户，告诉他你有新好友了
                write(new RespondProtocol().respondAddFriend(msgBody.getSubmitName()),netSource.getSource(msgBody.getTargetName()));
            }
            //若返回delFriend，则代表此前台账户要删除好友
        }else if ("delFriend".equals(respond)){
            //判断目标账户是否在线
            if(netSource.isContainsTargetName(msgBody.getTargetName())) {
                //将此客户端账户发送删好友请求给目标账，告诉他你已经被删除了
                write(new RespondProtocol().respondDelFriend(msgBody.getSubmitName()),netSource.getSource(msgBody.getTargetName()));
            }
            //若返回submit，则代表此前台账户要发送信息给好友
        }else if ("submit".equals(respond)) {
            //判断目标账户是否在线
            if (!netSource.isContainsTargetName(msgBody.getTargetName())) {
                //目标用户未在线，将信息储存到服务器数据库，待目标客户登陆后再次发送
               new  Manager().insertMsg(msgBody.getSubmitName(), msgBody.getTargetName(), msgBody.getContext());
            } else {
                //目标用户在线，直接发送
                write(new RespondProtocol().respondSessionSubmit(msgBody.getSubmitName(),msgBody.getContext()), netSource.getSource(msgBody.getTargetName()));
                System.out.println(new RespondProtocol().respondSessionSubmit(msgBody.getSubmitName(),msgBody.getContext()));
            }
            //登陆或注册功能
        } else {
            //登陆或注册回传信息
            if (!"false".equals(respond)) {
                //登陆或注册成功，将此账户的回传流，放入网络资源中，以供其他线程调用
                netSource.addSource(msgBody.getSubmitName(), outputStream);
                System.out.println(socket.getInetAddress().getHostAddress()
                        +"//"+socket.getPort()+":"+"登陆或注册成功！");
            }
            System.out.println(respond);
            write(respond, outputStream);
        }
        return true;
    }

    /**
     * 此线程的socket的写方法
     */
    private void write(String respond, OutputStream outputStream) {
        try {
            outputStream.write(respond.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭此线程的socket
     */
    private void close() {
        if (socket != null) {
            try {
                socket.close();
                System.out.println(socket.getInetAddress().getHostAddress()+"已关闭");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
