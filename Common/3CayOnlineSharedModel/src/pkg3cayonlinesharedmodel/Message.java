/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlinesharedmodel;

import java.io.Serializable;

/**
 *
 * @author HuyNguyen
 */
public class Message<H,D> implements Serializable {
    private H header;
    private D data;

    public Message(H header, D data) {
        this.header = header;
        this.data = data;
    }

    public H getHeader() {
        return header;
    }

    public void setHeader(H header) {
        this.header = header;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
    
    
}
