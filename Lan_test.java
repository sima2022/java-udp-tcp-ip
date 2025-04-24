/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.lan_test;

/**
 *
 * @author snb70
 */
public class Lan_test {

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Lan_test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            //追加のクラス
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new New_lanJFrame().setVisible(true);
                //追加のクラス
                //new NewJFrame().setVisible(true);
            }
        });
//        System.out.println("Hello World!");
    }
}
