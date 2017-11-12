/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlinesharedmodel;

import java.io.Serializable;

/**
 *
 * @author huynguyen
 */
public class UserInfo implements Serializable {
    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private int score;
    
    public UserInfo(String username) {
        this.username = username;
    }

    public UserInfo(int id, String username, String firstname, String lastname, int score) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public boolean equals(UserInfo obj) {
        return this.username.equals(obj.username);
    }
    
    
    
}
