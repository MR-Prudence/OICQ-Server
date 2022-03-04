package com.qq.util;

public class RespondProtocol {
    private StringBuffer respondLoginFriendStr;
    private StringBuffer respondLoginSessionStr;

    public String respondRegister(String isSuccession){
        return isSuccession + "\n";
    }

    public String respondDelFriend(String submitName){
        return "delFriend,「系统提示」你已经被删除了&" + submitName + ",\n";
    }

    public String respondAddFriend(String submitName){
        return "newFriend, &" + submitName + ",\n";
    }

    public String respondSessionSubmit(String submitName,String context){
        addSession(submitName,context);
        return respondLoginSessionStr.toString() + "\n";
    }

    public void addSession(String submitName,String context){
        if(respondLoginSessionStr == null){
            respondLoginSessionStr = new StringBuffer("session,");
        }
        respondLoginSessionStr.append(context + "&" + submitName + ",");
    }

    public String getSession(){
        if(respondLoginSessionStr == null){
            respondLoginSessionStr = new StringBuffer("session,");
        }
        return respondLoginSessionStr.toString();
    }

    public void addFriend(String friendName){
        if(respondLoginFriendStr == null){
            respondLoginFriendStr = new StringBuffer("friend,");
        }
        respondLoginFriendStr.append(friendName+ ",");
    }

    public String getFriends(){
        if(respondLoginFriendStr == null){
            respondLoginFriendStr = new StringBuffer("friend,");
        }
        return respondLoginFriendStr.toString();
    }
}
