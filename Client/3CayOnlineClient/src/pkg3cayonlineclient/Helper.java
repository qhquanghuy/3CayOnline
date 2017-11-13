/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import pkg3cayonlinesharedmodel.Response;
import pkg3cayonlinesharedmodel.Result;

/**
 *
 * @author huynguyen
 */
public final class Helper {
    public static <I,O> Result<O> parse(Response<I> res, Class<O> outputType) {
        
        switch (res.getHeader()) {
            case Error:
                return Result.error((String) res.getData());
            default:
                I value = res.getData();
                if(outputType.isInstance(value)) {
                    return Result.ok((O) value);
                }
                return Result.error("Parsing error");
        }
    }
}
