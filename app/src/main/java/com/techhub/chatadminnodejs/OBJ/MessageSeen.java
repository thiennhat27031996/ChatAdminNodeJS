package com.techhub.chatadminnodejs.OBJ;

/**
 * Created by thiennhat on 11/10/2017.
 */

public class MessageSeen {
    private String tenUser;
    private String lastMess;
    private boolean Seen;

    public MessageSeen(String tenUser, String lastMess, boolean seen) {
        this.tenUser = tenUser;
        this.lastMess = lastMess;
        this.Seen = seen;
    }

    public String getTenUser() {
        return tenUser;
    }

    public void setTenUser(String tenUser) {
        this.tenUser = tenUser;
    }

    public String getLastMess() {
        return lastMess;
    }

    public void setLastMess(String lastMess) {
        this.lastMess = lastMess;
    }

    public boolean isSeen() {
        return Seen;
    }

    public void setSeen(boolean seen) {
        Seen = seen;
    }
}
