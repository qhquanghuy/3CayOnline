/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BaseComponents;

import java.util.function.Consumer;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author huynguyen
 */
public class View extends JPanel implements Identifiable {
    public void showAlert(String message) {
         SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, message);
        });
    }
    
    public void presentConfirm(ViewController vc, Consumer<Boolean> onDismiss) {
        SwingUtilities.invokeLater(() -> {
            int dismissVal = JOptionPane.showConfirmDialog(this,vc.view, "Edit Customer:"
                                        ,JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            onDismiss.accept(dismissVal == 0);
            
        });
        
    }
}
