/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import pkg3cayonlinesharedmodel.Account;
import pkg3cayonlinesharedmodel.Common;
import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.Response;

/**
 *
 * @author huynguyen
 */
public class Router {
    public static Response parse(Request request) {
        Common.RequestURI uri = (Common.RequestURI) request.getHeader();
        switch (uri) {
            case Login:
            case Register:
            default:
                return new Response(404, "Not Found");
        }
    }
}
