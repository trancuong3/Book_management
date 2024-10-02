package org.example.Controller;

import org.example.Swing.LoginGUI;

import javax.swing.*;

public class Process {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new); // user :admin
        // password : admin123
    }
}
