package com.techhub.chatadminnodejs.OBJ;

import java.io.Serializable;

/**
 * Created by thiennhat on 05/10/2017.
 */

public class Message {

    public String tenUser;
    public String tinNhan;
    public boolean isSend;

    public Message(String tenUser, String tinNhan,boolean isSend) {
        this.tenUser = tenUser;
        this.tinNhan = tinNhan;
        this.isSend=isSend;

    }

    public String getTenUser() {
        return tenUser;
    }
    public boolean isSend(){
        return isSend;
    }
    public  void setSend(boolean send){
        this.isSend=send;
    }

    public void setTenUser(String tenUser) {
        this.tenUser = tenUser;
    }

    public String getTinNhan() {
        return tinNhan;
    }

    public void setTinNhan(String tinNhan) {
        this.tinNhan = tinNhan;
    }
}
