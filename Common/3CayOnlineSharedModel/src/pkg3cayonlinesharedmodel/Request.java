/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlinesharedmodel;

/**
 *
 * @author huynguyen
 */
public class Request<D> extends Message<Common.RequestURI,D> {
    
    public Request(Common.RequestURI header, D data) {
        super(header, data);
    }
    
}
