/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lan_test;

import static com.mycompany.lan_test.New_lanJFrame.jTextField2;
import static com.mycompany.lan_test.New_lanJFrame.jTextField3;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author snb70
 */
public class LAN_Receive {
    public void TestThread() {
        int n = 1; // カウンター変数を初期化
            while (true) {
                System.out.println("TestThread"+n);
                n++;
                try {
                    Thread.sleep(1000); // 1秒（1000ミリ秒）待機
                } catch (InterruptedException e) {
                    System.err.println("スリープ中に割り込みが発生しました: " + e.getMessage());
                    Thread.currentThread().interrupt(); // 割り込みステータスを復元
                }
            }
    }
            /*
            受信ポートの設定
            receivePort に受信するポート番号を指定します。送信側と一致させてください。
            ソケットの作成
            DatagramSocket を指定ポートでバインドします。これにより、そのポートでデータを受け取れるようになります。
            受信処理
            socket.receive(packet) メソッドを使ってUDPパケットを受信します。
            DatagramPacket を使って受信データを管理します。
            送信元情報の取得
            packet.getAddress() で送信元のIPアドレス、packet.getPort() で送信元ポートを取得します。
            受信データの処理
            packet.getData() で受信データのバイト配列を取得し、文字列に変換します。
            無限ループで待機
            常に受信を待ち受けるため、while(true) を使用しています。停止する場合は適宜条件を追加してください。
        
    System.setOut(new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"), true));
            
    
    
    */
    public void w5500_UDP_Receive(){
//        System.setOut(new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"), true));
        //int port = 5555; // 受信するポート番号
        String portText = jTextField3.getText(); // ポート番号を文字列で取得
        int  port = Integer.parseInt(portText); // 整数に変換
        try (DatagramSocket socket = new DatagramSocket(port)) {
            //socket.setSoTimeout(5000); // 5秒のタイムアウトを設定
            byte[] receiveData = new byte[1024];  // 受信データ用のバッファ
            DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);

            System.out.println("UDP Receiver started on port " + port);

            while (true) {
                //try {
                    System.out.println("w5500_UDP_Receive");
                    // 受信データを待機 (タイムアウトを考慮)
                    socket.receive(packet);  // タイムアウト後に受信

                    // 受信したデータの処理
                    String senderAddress = packet.getAddress().getHostAddress();
                    int senderPort = packet.getPort();
                    int length = packet.getLength();
                    byte[] data = packet.getData();

                    // 受信データを文字列に変換
                    String receivedDataString = new String(data, 0, length);
                    System.out.println("receive: " + receivedDataString);
                    System.out.println("senderAddress: " + senderAddress);
                    System.out.println("senderPort: " + senderPort);

                //} catch (IOException e) {
                    // タイムアウトやその他のエラー時にエラーメッセージを表示
                //    System.out.println("time out OR Error" + e.getMessage());
                    // ここで再試行を行うこともできます
                //}
            }
        } catch (SocketException e) {
            System.err.println("Socket error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("i/o error: " + e.getMessage());
        }       
        
        
        
        
        
        
         /*int port = 5353; // 受信するポート番号

        try (DatagramSocket socket = new DatagramSocket(port)) {
            byte[] receiveData = new byte[1024];
            DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);

            while (true) {
                System.out.println("r");
                socket.receive(packet);
                String senderAddress = packet.getAddress().getHostAddress();
                int senderPort = packet.getPort();
                int length = packet.getLength();
                byte[] data = packet.getData();

                String receivedDataString = new String(data, 0, length);
                System.out.println("受信データ: " + receivedDataString);
                System.out.println("送信元アドレス: " + senderAddress);
                System.out.println("送信元ポート: " + senderPort);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    //}
        
       
        DatagramSocket socket = null;
        try {
            // 受信するポート番号
            int port = 5550;
            socket = new DatagramSocket(port); // 指定したポートでソケットを開く
            System.out.println("UDP Receiver started on port " + port);

            while (true) {
                // 受信データを格納するバッファ
                byte[] buffer = new byte[1024];
                // 受信パケットの作成
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                // データを受信
                socket.receive(packet);
                // 受信データの表示
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received message: " + message);
                // 受信元情報
                System.out.println("From: " + packet.getAddress().getHostAddress() + ":" + packet.getPort());
            }
        } catch (SocketException e) {
            System.err.println("Socket error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } finally {
                System.out.println("finally");
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
        */
    }
            /*
            ポート番号の設定
            serverPort にサーバーが待機するポート番号を指定します。クライアント側と一致させる必要があります。
            ServerSocket の作成
            ServerSocket クラスを使用して指定ポートでサーバーを開始し、クライアントからの接続を待機します。
            クライアントの接続
            serverSocket.accept() を呼び出すと、クライアントが接続してくるまで処理がブロックされます。接続が確立すると Socket オブジェクトが返されます。
            データの受信
            BufferedReader を使用して、クライアントから送信される文字列データを行単位で読み取ります。
            接続の終了
            クライアントが接続を閉じると、reader.readLine() は null を返します。
            リソースの解放
            finally ブロックで、ServerSocket、Socket、BufferedReader を確実に閉じます。
            実行手順
            NetBeansで新しいプロジェクトを作成し、このコードを貼り付けます。
            実行すると、指定したポートでTCPサーバーが待機します。
            クライアント（別のプログラムやツール）から接続し、データを送信します。
            例: Telnet や他のTCPクライアントツールを使って送信。
            サーバーで受信したデータがコンソールに表示されます。
            注意点
            サーバーを起動する際には、指定ポートが他のプロセスで使用されていないことを確認してください。
            複数のクライアントを同時に接続したい場合はスレッド処理を追加する必要があります。
            */
public void w5500_TCP_Receive() {
    }

}
