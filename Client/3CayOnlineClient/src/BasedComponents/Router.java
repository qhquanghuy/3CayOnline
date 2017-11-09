/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BasedComponents;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.function.Consumer;

/**
 *
 * @author huynguyen
 */
public class Router {
    protected ViewController currentViewController;
    private final Container rootContainer;
    private final CardLayout cardLayout;


    public Router(Container rootContainer, ViewController rootViewController) {
        this.rootContainer = rootContainer;
        this.currentViewController = rootViewController;
        
        this.cardLayout = new CardLayout();
        this.rootContainer.setLayout(this.cardLayout);
        
        this.add(rootViewController);   
    }
    
    private void add(ViewController vc) {
        this.rootContainer.add(vc.view,vc.view.getIdentifier());
        vc.setRouter(this);
    }

    public ViewController getCurrentController() {
        return currentViewController;
    }

    public void show(ViewController vc) {
        this.currentViewController = vc;      
        Component[] components = this.rootContainer.getComponents();
        
        Consumer<ViewController> addIfNeeded = (v) -> this.add(vc);
        
        for (Component component : components) {
            View view = (View) component;
            if (view.getIdentifier().equals(vc.view.getIdentifier())) {
                addIfNeeded =  (v) -> {};
                break;
            }
        }
        
        addIfNeeded.accept(vc);
        this.cardLayout.show(this.rootContainer, vc.view.getIdentifier());
    }
    
}
