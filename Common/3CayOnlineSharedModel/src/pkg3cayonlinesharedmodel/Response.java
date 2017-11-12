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
public class Response<D> extends Message<Common.ResponseHeader, D> {
    
    public Response(Common.ResponseHeader header, D data) {
        super(header, data);
    }
    
    public static Response resourceNotFound() {
        return new Response(Common.ResponseHeader.Error, "Resource Not Found");
    }
    
    public static Response systemError() {
        return new Response(Common.ResponseHeader.Error, "System error");
    }
    
}
