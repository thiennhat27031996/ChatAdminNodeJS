package com.techhub.chatadminnodejs.OBJ;

import java.io.Serializable;

/**
 * Created by thiennhat on 05/10/2017.
 */

public class Message {
    public String key,lastmessage,name,seen,usermess;

    public Message() {
    }

    public Message(String key, String lastmessage, String name, String seen,String usermess) {
        this.key = key;
        this.lastmessage = lastmessage;
        this.name = name;
        this.seen = seen;
        this.usermess=usermess;
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
