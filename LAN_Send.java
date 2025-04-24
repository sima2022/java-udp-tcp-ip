/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lan_test;

import static com.mycompany.lan_test.New_lanJFrame.jTextField1;
import static com.mycompany.lan_test.New_lanJFrame.jTextField3;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author snb70
 */
public class LAN_Send {
/*
送信先IPアドレスとポート番号
targetIP と targetPort に送信先のIPアドレスとポートを設定します。
送信データ
message に送信したいデータを文字列として設定します。
DatagramSocket と DatagramPacket
DatagramSocket はUDP通信のソケットを作成します。
DatagramPacket は送信するデータ、宛先アドレス、ポートを設定します。
エラーハンドリング
try-catch を使用して例外を処理し、エラー時にスタックトレースを出力します。
リソースの解放
finally ブロックで、socket を必ず閉じるようにしています。
実行方法
NetBeansでプロジェクトをビルドして実行します。
正常に実行されれば、送信先のデバイスがデータを受信します。
受信側の設定が正しいことを確認してください。
*/
public void w5500_UDP_Send() {
    String destinationIP = "192.168.11.3"; // 宛先IP
    String portText = jTextField3.getText(); // ポート番号を文字列で取得
    int destinationPort = Integer.parseInt(portText); // 整数に変換

    String message = jTextField1.getText(); // 送信メッセージを取得

    JOptionPane.showMessageDialog(null, message, "送信Test", JOptionPane.INFORMATION_MESSAGE);
        
        try {
            // ソケット作成
            DatagramSocket socket = new DatagramSocket();
            // 宛先のアドレス取得
            InetAddress address = InetAddress.getByName(destinationIP);
            // メッセージをバイト配列に変換
            byte[] buffer = message.getBytes("UTF-8");
            // パケットを作成
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, destinationPort);
            // パケットを送信
            socket.send(packet);
            System.out.println("Send OK: " + message);
            // ソケットを閉じる
            socket.close();
        } catch (Exception e) {
            System.err.println("Error!!: " + e.getMessage());
            e.printStackTrace();
        }
    }

/*
送信先のIPアドレスとポート番号
targetIP と targetPort を使用して接続先を指定します。
Socketの作成
Socket クラスを使い、指定されたIPアドレスとポートに接続します。
データ送信
OutputStream を取得し、データ送信用の PrintWriter を作成します。
writer.println(message); で送信データを送ります。
エラーハンドリング
接続エラーや送信エラーが発生した場合に備え、try-catch を使用しています。
リソースの解放
finally ブロックで Socket や PrintWriter を必ず閉じるようにしています。
実行手順
プロジェクトをビルドして実行します。
サーバー（受信側）が適切にセットアップされていることを確認してください。
正常に接続されれば、送信先でデータが受信されます。
*/
public void w5500_TCP_Send() {
        // インスタンスにアクセス
        New_lanJFrame p = New_lanJFrame.instance;
        Socket socket = null;
        PrintWriter writer = null;
        try {
            // 送信先のIPアドレスとポート
            String targetIP = "192.168.11.4"; // 送信先のIPアドレス STM32+W5500
            //int targetPort = 5353;            // 送信先のポート番号
            String portText = jTextField3.getText(); // ポート番号を文字列で取得
            int  targetPort = Integer.parseInt(portText); // 整数に変換

            // サーバーに接続
            socket = new Socket();
            socket.connect(new InetSocketAddress(targetIP, targetPort), 5000);  // タイムアウトを設定（例: 5000ms）
            Thread.sleep(1000);//無いと動かなかった。500では動かなかった。 
            if (socket.isConnected()) {
                System.out.println("Connected to the server: " + targetIP + ":" + targetPort);
                // データ送信の準備
                OutputStream outputStream = socket.getOutputStream();
                writer = new PrintWriter(new OutputStreamWriter(outputStream), true); // バッファ付き
                // 送信するデータ
                String message = "Hello, TCP!";
                // インスタンスからテキストを取得
                message = p.jTextField1.getText();
                JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("TextField Content: " + p.jTextField1.getText());
                
                writer.println(message); // データを送信
                writer.flush(); // 明示的にflushを呼び出す
                System.out.println("Sent data: " + message);
            } else {
                System.out.println("Failed to connect to the server.");
            }
        } catch (Exception e) {
            System.err.println("Attempt failed. Retrying...");
            e.printStackTrace();
        } finally {
            // リソースを解放
            try {
                if (writer != null) {
                    writer.close();
                }
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }    
    }
}
