/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BasedComponents;

/**
 *
 * @author huynguyen
 */
public interface Identifiable {
    default public String getIdentifier() {
        return this.getClass().toString();
    }
}
