/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lan_test;

import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author snb70
 */
public class useJFileChooser {
    /**
     * ファイル選択ダイアログを表示する
     * @return 選択されたファイルのパス（キャンセルされた場合はnull）
     */
    public String selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        } else {
            System.out.println("File selection cancelled.");
            return null;
        }
    }

    /**
     * ディレクトリ選択ダイアログを表示する
     * @return 選択されたディレクトリのパス（キャンセルされた場合はnull）
     */
    public String selectDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            return selectedDirectory.getAbsolutePath();
        } else {
            System.out.println("Directory selection cancelled.");
            return null;
        }
    }
}
