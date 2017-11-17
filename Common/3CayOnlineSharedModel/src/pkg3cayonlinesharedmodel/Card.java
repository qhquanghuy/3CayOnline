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
public class Card implements Comparable<Card>, Serializable{
    private final int value;
    private final Common.CardType type;

    public Card(int value, Common.CardType type) {
        this.value = value;
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public Common.CardType getType() {
        return type;
    }

    @Override
    public int compareTo(Card o) {
        int typeComparedResult = new Integer(this.type.getIntVal()).compareTo(o.type.getIntVal());
        if(typeComparedResult == 0) {
            return new Integer(this.value).compareTo(o.value);
        } else {
            return typeComparedResult;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(this.value) + " " + this.type.toString();
    }
    
    
    
    
}
