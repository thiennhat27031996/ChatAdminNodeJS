package com.techhub.chatadminnodejs.OBJ;

/**
 * Created by NTN on 14/10/2017.
 */

public class MessageSeenModel {
   public String key,lastmessage,name,seen,online;
    public MessageSeenModel(){

    }

    public MessageSeenModel(String key, String lastmessage, String name, String seen,String online) {
        this.key = key;
        this.lastmessage = lastmessage;
        this.name = name;
        this.seen = seen;
        this.online=online;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
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
