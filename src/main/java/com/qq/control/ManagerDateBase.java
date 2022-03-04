package com.qq.control;

import com.qq.database.MyConnection;
import com.qq.model.User;
import com.qq.util.RespondProtocol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerDateBase {

    /**
     * 删除好友
     *
     * @param submitName 删除方账户
     * @param targetName 被删除方账户
     * @return 删除结果
     */
    public boolean delFriend(String submitName, String targetName) {
        if (getFriend(submitName) == null && !getFriend(submitName).contains(targetName)) {
            return false;
        }
        Connection connection = null;
        int i = -1;
        try {
            connection = MyConnection.getConnection();
            String sql = "delete from `friend` where (`nameleft` = ? and `nameright` = ?) or ( `nameleft` = ? and `nameright` = ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, submitName);
            statement.setObject(2, targetName);
            statement.setObject(3, targetName);
            statement.setObject(4, submitName);
            i = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
        return i > 0;
    }

    /**
     * 添加好友
     *
     * @param submitName 添加方账户
     * @param targetName 被添加方账户
     * @return 添加结果
     */
    public boolean addFriend(String submitName, String targetName) {
        if (getFriend(submitName) != null && getFriend(submitName).contains(targetName)) {
            return false;
        }
        Connection connection = null;
        try {
            connection = MyConnection.getConnection();
            String sql = "INSERT INTO `friend`(`nameleft`,`nameright`) VALUES(?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, submitName);
            statement.setObject(2, targetName);
            int i = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
        return true;
    }

    /**
     * 获取此账户名的所有好友
     *
     * @param name 账户名
     * @return 好友列表
     */
    public String getFriend(String name) {
        RespondProtocol protocol = new RespondProtocol();
        Connection connection = null;
        try {
            connection = MyConnection.getConnection();
            String sql = "SELECT nameleft FROM `friend` WHERE `nameright` = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String nameLeft = resultSet.getString("nameleft");
                protocol.addFriend(nameLeft);
            }
            sql = "SELECT nameright FROM `friend` WHERE `nameleft` = ?";
            statement = connection.prepareStatement(sql);
            statement.setObject(1, name);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String nameRight = resultSet.getString("nameright");
                if (!name.equals(nameRight)) {
                   protocol.addFriend(nameRight);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
        return protocol.getFriends();
    }

    /**
     * 判断添加账户信息
     *
     * @param user 账户信息
     * @return true：添加成功；false：添加失败，此账户名已存在
     */
    public boolean addUser(User user) {
        boolean addUser = false;
        Connection connection = null;
        try {
            addUser = false;
            connection = MyConnection.getConnection();
            String sql = "INSERT INTO `user`(`name`,`password`) VALUES(?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, user.getName());
            statement.setObject(2, user.getPassword());
            if (statement.executeUpdate() > 0) {
                addUser = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
        return addUser;
    }

    /**
     * 判断是否为已注册账户
     *
     * @param user 账户信息
     * @return true：此账户名已存在；false：此账户名不存在
     */
    public boolean isUser(User user) {
        Connection connection = null;
        boolean isUser = false;
        try {
            connection = MyConnection.getConnection();
            isUser = false;
            String sql = "SELECT * FROM `USER` WHERE `name` = ? AND `password` = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, user.getName());
            statement.setObject(2, user.getPassword());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isUser = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
        return isUser;
    }

    /**
     * 获取此账户的未读信息
     *
     * @param name 账户名
     * @return 此账户的未读信息
     */
    public String getMsg(String name) {
        Connection connection = null;
        RespondProtocol protocol = new RespondProtocol();
        try {
            connection = MyConnection.getConnection();
            String sql = "SELECT context,submitName FROM msg WHERE myName = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String submitName = resultSet.getString("submitName");
                String context = resultSet.getString("context");
                protocol.addSession(submitName, context);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
        return protocol.getSession();
    }

    /**
     * 插入一条未读信息
     *
     * @param submitName 发送方账户名
     * @param myName     发送信息内容
     * @param context    接收方账户名
     */
    public void insertMsg(String submitName, String myName, String context) {
        Connection connection = null;
        try {
            connection = MyConnection.getConnection();
            String sql = "INSERT INTO `msg`(submitName,myName,context) VALUES(?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, submitName);
            statement.setObject(2, myName);
            statement.setObject(3, context);
            int i = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
    }

    /**
     * 删除一条未读信息
     *
     * @param name 接收方账户名
     */
    public void deleteMsg(String name) {
        Connection connection = null;
        try {
            connection = MyConnection.getConnection();
            String sql = "DELETE FROM msg WHERE myName = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, name);
            int i = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
    }

    /**
     * 关闭一个数据库连接
     *
     * @param connection 需关闭的数据库连接
     */
    private void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
