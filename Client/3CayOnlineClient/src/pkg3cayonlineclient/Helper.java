/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3cayonlineclient;

import BaseComponents.View;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author huynguyen
 */
public final class Helper {
    public static void showMessage(View inView, String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(inView, message);
        });
    }
}
