package com.qq.control;

import com.qq.model.MsgBody;
import com.qq.model.User;
import com.qq.util.Parser;

public class Manager {
    private ManagerDateBase dateBase = new ManagerDateBase();
    private MsgBody msgBody;

    public Manager() {
    }

    public Manager(String str) {
        Parser parser = new Parser(str);
        this.msgBody = parser.myParser();
    }

    /**
     * 通过获取前台发送来的字符串，调用解析方法，调用相应处理方法，并返回处理结果
     *
     * @return 处理结果
     */
    public String doManager() {
        String respond = null;
        switch (msgBody.getMethod()) {
            case "login":
                respond = login(msgBody.getUser());
                break;
            case "register":
                respond = register(msgBody.getUser());
                break;
            case "exit":
                respond = null;
                break;
            case "submit":
                respond = "submit";
                break;
            case "delFriend":
                respond = delFriend(msgBody.getSubmitName(), msgBody.getTargetName());
                break;
            case "addFriend":
                respond = addFriend(msgBody.getSubmitName(), msgBody.getTargetName());
        }
        return respond;
    }

    /**
     * @return 获取解析后的前台请求体
     */
    public MsgBody getMsgBody() {
        return msgBody;
    }


    /**
     * 添加好友
     *
     * @param submitName 添加方账户
     * @param targetName 被添加方账户
     * @return 添加结果
     */
    private String addFriend(String submitName, String targetName) {
        if (dateBase.addFriend(submitName, targetName)) {
            return "addFriend";
        }
        return "addFalse";
    }

    /**
     * 删除好友
     *
     * @param submitName 删除方账户
     * @param targetName 被删除方账户
     * @return 删除结果
     */
    private String delFriend(String submitName, String targetName) {
        if (dateBase.delFriend(submitName, targetName)) {
            return "delFriend";
        }
        return "delFalse";
    }

    /**
     * 账户注册
     *
     * @param user 账户信息，包括账户名和密码
     * @return 注册结果
     */
    private String register(User user) {
        if (dateBase.addUser(user)) {
            return "true\n";
        }
        return "false\n";
    }

    /**
     * 账户登陆
     *
     * @param user 账户信息，包括账户名和密码
     * @return 登陆结果，若登陆成功，将返回此账户的未读信息和好友列表
     */
    private String login(User user) {
        if (dateBase.isUser(user)) {
            //登陆成功
            String msg = dateBase.getMsg(user.getName());
            if (msg != null) {
                dateBase.deleteMsg(user.getName());
            }
            return msg + dateBase.getFriend(user.getName()) + "\n";
        }
        return "false\n";
    }


    /**
     * 向数据库插入一条未读信息
     *
     * @param submitName 未读信息发送账户
     * @param targetName 未读信息接收账户
     * @param context    未读信息内容
     */
    public void insertMsg(String submitName, String targetName, String context) {
        dateBase.insertMsg(submitName, targetName, context);
    }
}
