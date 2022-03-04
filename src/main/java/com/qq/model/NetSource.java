package com.qq.model;

import java.io.OutputStream;
import java.util.HashMap;

public class NetSource {

    private HashMap<String, OutputStream> source;//保存所有在线的账户回传流

    public NetSource(HashMap<String, OutputStream> source) {
        this.source = source;
    }

    public HashMap<String, OutputStream> getSource() {
        return source;
    }

    public void setSource(HashMap<String, OutputStream> source) {
        this.source = source;
    }
}
