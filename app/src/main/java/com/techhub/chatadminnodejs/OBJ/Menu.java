package com.techhub.chatadminnodejs.OBJ;

/**
 * Created by thiennhat on 20/10/2017.
 */

public class Menu {
    String name;
    String numbercart;

    public Menu(String name, String numbercart) {
        this.name = name;
        this.numbercart = numbercart;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumbercart() {
        return numbercart;
    }

    public void setNumbercart(String numbercart) {
        this.numbercart = numbercart;
    }
}
