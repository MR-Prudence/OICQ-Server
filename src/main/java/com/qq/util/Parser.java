package com.qq.util;

import com.qq.model.MsgBody;

public class Parser {
    public String[] strings;

    public Parser(String str){
        this.strings = str.split("&");
    }

    /**
     * 解析前台发来的数据字符串（前台数据字符串为「method&submitName&context&targetName&」，其中method不可省略）
     * @return 解析后的
     */
    public MsgBody myParser(){
        MsgBody msgBody = null;
        String method = strings[0];
        if("login".equals(method)||"register".equals(method)){
            String submitName = strings[1];
            String password = strings[2];
            msgBody = new MsgBody(method,submitName,password);
        }else if ("addFriend".equals(method)||"delFriend".equals(method)){
            String submitName = strings[1];
            String targetName = strings[2];
            msgBody = new MsgBody(method,submitName,targetName,null);
        }else if ("submit".equals(method)){
            String submitName = strings[1];
            String context = strings[2];
            String targetName = strings[3];
            msgBody = new MsgBody(method,submitName,targetName,context);
        }else {
            String submitName = strings[1];
            msgBody = new MsgBody(method,submitName,null,null);
        }
        return msgBody;
    }
}
