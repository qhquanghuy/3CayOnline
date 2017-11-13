/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BaseComponents;

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
}
