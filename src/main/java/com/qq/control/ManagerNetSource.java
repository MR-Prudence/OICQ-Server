package com.qq.control;

import com.qq.model.NetSource;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManagerNetSource {
    private NetSource source;
    private final static ManagerNetSource MANAGER_NET_SOURCE = new ManagerNetSource(new NetSource(new HashMap<>()));

    private ManagerNetSource(NetSource source){
        this.source = source;
    }

    /**
     * 获取一个操作网络资源的对象（单例）
     * @return 一个操作网络资源的对象（单例）
     */
    public static ManagerNetSource getInstance(){
        return MANAGER_NET_SOURCE;
    }

    /**
     * 线程安全的获取网络回传流对象
     * @return 获取网络回传流对象（线程安全）
     */
    private synchronized NetSource getSource(){
        return source;
    }

    /**
     * 获取一批账户的回传流，群聊使用
     * @param submitNames 接收方的账户名
     * @return 全部账户的回传流
     */
    public List<OutputStream> getSources(String[] submitNames){
        ArrayList<OutputStream> outputStreams = new ArrayList<>(submitNames.length);
        for (int i = 0; i < submitNames.length; i++) {
            outputStreams.add(getSource().getSource().get(submitNames[i]));
        }
        return outputStreams;
    }

    /**
     * 获取单个账户的回传流，私聊使用
     * @param submitName 接收方的账户名
     * @return 接收方的账户的回传流
     */
    public OutputStream getSource(String submitName){
        return getSource().getSource().get(submitName);
    }

    /**
     * 添加一个账户的回传流，登陆或注册（上线）使用
     * @param submitName 前台的账户名
     * @param outputStream 前台的回传流
     */
    public void addSource(String submitName,OutputStream outputStream){
        getSource().getSource().put(submitName,outputStream);
    }

    /**
     * 删除一个账户的回传流，退出（下线）使用
     * @param submitName 前台的账户名
     */
    public void deleteSource(String submitName){
        getSource().getSource().remove(submitName);
    }

    public boolean isContainsTargetName(String targetName){
        return getSource().getSource().containsKey(targetName);
    }
}
