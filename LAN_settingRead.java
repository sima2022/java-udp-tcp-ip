/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lan_test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 *パソコン側のIPアドレス取得。
 *パソコン側のMACアドレス取得。
 *パソコン側のPortNo取得。
 *取得のつもり?未検証
 */
public class LAN_settingRead {
 // 条件コンパイルの例 #if 0~#endifが使えない為
boolean deb_lan_setting = true;
//boolean deb_lan_setting = false;
// メソッド内に移動
    public void printDebugInfo() {
        if (deb_lan_setting) {
            System.out.println("デバッグTest");
        }
    }
    public void Getpc_ip_address() {//パソコン側のIPアドレス取得。
/*        if (deb_lan_setting) {
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                System.out.println("IP Address: " + localHost.getHostAddress());
                } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
*/
        try {
            // すべてのネットワークインターフェースを取得
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                // ループバックインターフェースや無効なインターフェースをスキップ
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }

                // インターフェースに紐づくIPアドレスを取得
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();

                    // IPv4アドレスのみを表示 (IPv6を除外)
                    if (address.getHostAddress().contains(".")) {
                        System.out.println("Interface: " + networkInterface.getDisplayName());
                        System.out.println("IP: " + address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }
    public void Getpc_mac_address() {//パソコン側のMACアドレス取得。
        if (deb_lan_setting) {
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
                byte[] macAddress = networkInterface.getHardwareAddress();

                if (macAddress != null) {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : macAddress) {
                        sb.append(String.format("%02X:", b));
                    }
                    sb.setLength(sb.length() - 1); // 最後のコロンを削除
                    System.out.println("MAC Address: " + sb.toString());
                } else {
                    System.out.println("MAC Address not found.");
                }
            } catch (UnknownHostException | SocketException e) {
                e.printStackTrace();
            }
        }
    }
    public void Getpc_PortNo() {//パソコン側のPortNo取得。
        if (deb_lan_setting) {
            try (ServerSocket serverSocket = new ServerSocket(0)) { // 0 を指定するとシステムがポートを割り当てる
                System.out.println("Assigned Port: " + serverSocket.getLocalPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}   

