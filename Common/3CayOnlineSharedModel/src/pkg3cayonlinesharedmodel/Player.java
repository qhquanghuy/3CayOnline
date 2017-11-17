/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlinesharedmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author huynguyen
 */
public class Player implements Serializable {
    private int id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private int score;
    private List<Card> cards = new ArrayList<>();
    private static Player _empty = new Player();
    static Player empty() {
        return _empty;
    }
    
    public Player() {
        this.username = "";
        this.score = -1;
    }
    
    public Player(String username) {
        this.username = username;
    }
    

    public Player(int id, String username, String firstname, String lastname, int score) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.score = score;
    }

    public Player(String firstname, String lastname, String username, String password) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }



    public List<Card> getDeck() {
        return cards;
    }

    
    public boolean isEmpty() {
        return this.equals(Player._empty);
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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Player) {
            return this.username.equals(((Player) obj).username);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.username);
        return hash;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
}
