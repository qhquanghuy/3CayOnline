/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BaseComponents;


/**
 *
 * @author huynguyen
 * @param <T>
 */
public class ViewController<T extends View> {
    protected T view;
    protected Router router;

    public ViewController(T view) {
        this.view = view;
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }
    
}
