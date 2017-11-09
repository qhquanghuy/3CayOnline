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
public class ViewController {
    protected View view;
    protected Router router;

    public ViewController(View view) {
        this.view = view;
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }
    
}
