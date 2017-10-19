package com.techhub.chatadminnodejs.OBJ;

import java.io.Serializable;

/**
 * Created by thiennhat on 05/10/2017.
 */

public class Message {
    public String key,lastmessage,name,seen,usermess,url,time;

    public Message() {
    }

    public Message(String key, String lastmessage, String name, String seen,String usermess,String url,String time) {
        this.key = key;
        this.lastmessage = lastmessage;
        this.name = name;
        this.seen = seen;
        this.usermess=usermess;
        this.url=url;
        this.time=time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsermess() {
        return usermess;
    }

    public void setUsermess(String usermess) {
        this.usermess = usermess;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }
}
