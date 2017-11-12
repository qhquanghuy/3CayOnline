/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineserver;

import pkg3cayonlinesharedmodel.Request;
import pkg3cayonlinesharedmodel.Response;

/**
 *
 * @author huynguyen
 */
public interface Responseable {
    public Response getResponseForRequest(UserHandler client, Request request);
    public void clientUnreachable(UserHandler client);
}
