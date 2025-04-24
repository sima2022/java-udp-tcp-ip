/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lan_test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
/**
 *
 * @author snb70
 */
public class LAN_ini {
   // 関数の定義（staticを付けることで直接呼び出し可能に）
    // 関数の定義（staticを付けることで直接呼び出し可能に）
    //public static void connect(String ipAddress) {
   public static void connect() {
        // 実際の処理をここに記述
        System.out.println("A function in another file was executed");
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                //System.out.println("Network Interface: " + networkInterface.getName());
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.getHostAddress().contains(":")) {
                        System.out.println("IP Address: " + inetAddress.getHostAddress());
                        // JLabel5にIPアドレスを表示
                        //New_lanJFrame frame = new New_lanJFrame();
                        //frame.setVisible(true);  // フレームを表示

                        // jLabel5にアクセスしてテキストを変更
                        //New_lanJFrame.jLabel5.setText(inetAddress.getHostAddress());
                        //jLabel5.setText("IP Address: " + inetAddress.getHostAddress());

                        // Get the hardware address (MAC address)
                        byte[] macBytes = networkInterface.getHardwareAddress();
                        if (macBytes != null) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < macBytes.length; i++) {
                                sb.append(String.format("%02X%s", macBytes[i] & 0xFF, (i < macBytes.length - 1) ? "-" : ""));
                            }
                            System.out.println("MAC Address: " + sb.toString());
                        }
                        return;//break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
