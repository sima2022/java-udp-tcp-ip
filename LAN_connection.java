package com.mycompany.lan_test;

import com.mycompany.lan_test.New_lanJFrame;
import static com.mycompany.lan_test.New_lanJFrame.jTextField2;
import static com.mycompany.lan_test.New_lanJFrame.jTextField3;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author snb70
 */
/*
// 推奨された使い方？　New_lanJFrame.java
public class New_lanJFrame extends JFrame {
    private static final New_lanJFrame INSTANCE = new New_lanJFrame();
    private New_lanJFrame() {
        // コンストラクタの処理
    }
    public static New_lanJFrame getInstance() {
        return INSTANCE;
    }
    // getter/setterメソッドの例
    public String getTextFieldValue() {
        return textField.getText();
    }
    public void setTextFieldValue(String value) {
        textField.setText(value);
    }
}

// 他のクラスからのアクセス例
public class AnotherClass {
    public void updateJFrame() {
        SwingUtilities.invokeLater(() -> {
            New_lanJFrame pf = New_lanJFrame.getInstance();
            pf.setTextFieldValue("新しい値");
        });
    }
}        
*/
/*
// インスタンスにアクセス間違った使い方?? 一応動いたが警告が出て後々問題が出るらしい。
//他から直接コントロールにアクセスは、非推奨
        New_lanJFrame pf = New_lanJFrame.instance;
        String text1 = pf.jTextField2.getText();
        String text2 = pf.jTextField3.getText();
        text1 = "192.168."+text1;    
            JOptionPane.showMessageDialog(null,
            "接続分岐" , "test", JOptionPane.INFORMATION_MESSAGE);
            if (pf.UDPRadioButton.isSelected()) {
                JOptionPane.showMessageDialog(null, "UDP "+text1+" port"+text2, "選択結果", JOptionPane.INFORMATION_MESSAGE);
            } else if (pf.TCPRadioButton.isSelected()) {
                if(pf.TCPRadioButton_s.isSelected()){
                    JOptionPane.showMessageDialog(null, "TCP サーバー"+text1+" port"+text2, "選択結果", JOptionPane.INFORMATION_MESSAGE);
                }
                if(pf.TCPRadioButton_c.isSelected()){
                    JOptionPane.showMessageDialog(null, "TCP クライアント"+ text1+" port"+text2, "選択結果", JOptionPane.INFORMATION_MESSAGE);
                }
            }
       }
*/

public class LAN_connection {
//    private  ServerSocket serverSocket;

    public void connect() {

    //推奨されている。getter/setterを使う。
       SwingUtilities.invokeLater(() -> {
           
            New_lanJFrame pf = New_lanJFrame.getInstance();
            String text1 = pf.getIP_text();
            String text2 = pf.getPort_text();
            if (pf.getUDP_Button()) {
                JOptionPane.showMessageDialog(null, "UDP "+text1+"/ Port でTest開始"+text2, "選択結果", JOptionPane.INFORMATION_MESSAGE);
                //UDP_mode();
                UDP_receiveAndSend();
            } else if (pf.getTCP_Button()) {
                if(pf.getTCP_S_Button()){
                    JOptionPane.showMessageDialog(null, "TCP サーバー Port"+ text1+"/ "+text2+" でTest開始", "選択結果", JOptionPane.INFORMATION_MESSAGE);
                    //TCP_server();
                    //TCP_server_cutting_corners();//
                    TCP_server_receiveAndSend();
                }
                if(pf.getTCP_C_Button()){
                    JOptionPane.showMessageDialog(null, "TCP クライアント"+ text1+"/ Port"+text2+" でTest開始", "選択結果", JOptionPane.INFORMATION_MESSAGE);
                    //TCP_client_every();//連続送信
                    //TCP_client_receiveOnly();
                    TCP_client_receiveAndSend();//2025/04/20
                }
            }
        });
    }
    //2025/04/22 送信port=jTextField3,受信port=jTextField3+1
    //
    public void UDP_receiveAndSend() {
    JOptionPane.showMessageDialog(null, "Test UDP_client (Receive and Send)");
    New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得
    String targetIP = pf.jTextField2.getText(); // 送信先IP
    final int targetPort = Integer.parseInt(pf.jTextField3.getText()); // 送信先ポート
    final int receivePort = targetPort + 1; // 受信用ポート（送信ポート+1）

    System.out.println("IP: " + targetIP + "\nSend Port: " + targetPort + "\nReceive Port: " + receivePort);
    SwingUtilities.invokeLater(() -> {
        pf.jTextArea1.append("IP: " + targetIP + "\nSend Port: " + targetPort + "\nReceive Port: " + receivePort + " \r\nstart \r\n");
    });

    try {
        // 送信用ソケットを作成
        pf.udp_socket = new DatagramSocket(); // 送信用は通常ポート指定なし（OSが自動で割り当て）

        // 受信用ソケットを作成
        pf.udp_receiveSocket = new DatagramSocket(receivePort); // 新しく受信用ソケット
        System.out.println("Listening on receive port " + receivePort);
    } catch (SocketException ex) {
        System.err.println("Failed to create DatagramSocket: " + ex.getMessage());
        ex.printStackTrace();
        return;
    }

    byte[] receiveBuffer = new byte[1024];

    new Thread(() -> {
        try {
            // jButton4（送信ボタン）のリスナーをいったん全削除
            for (ActionListener al : pf.jButton4.getActionListeners()) {
                pf.jButton4.removeActionListener(al);
            }

            // 送信ボタンのアクションリスナーを設定
            pf.jButton4.addActionListener(e -> {
                try {
                    String message = pf.jTextField1.getText();
                    byte[] sendData = message.getBytes();
                    InetAddress address = InetAddress.getByName(targetIP);
                    pf.udp_sendPacket = new DatagramPacket(sendData, sendData.length, address, targetPort);
                    pf.udp_socket.send(pf.udp_sendPacket);

                    System.out.println("SendData: " + message);
                    SwingUtilities.invokeLater(() -> {
                        pf.jTextArea1.append("SendData: " + message + " \r\n");
                    });
                } catch (Exception ex) {
                    System.err.println("Error during data sending: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });

            // 受信処理
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                pf.udp_receiveSocket.receive(receivePacket); // 受信用ソケットから受信！

                String senderAddress = receivePacket.getAddress().getHostAddress();
                int senderPort = receivePacket.getPort();
                int length = receivePacket.getLength();
                byte[] data = receivePacket.getData();

                String receivedDataString = new String(data, 0, length, StandardCharsets.UTF_8);
                System.out.println("Received: " + receivedDataString);
                System.out.println("Sender Address: " + senderAddress);
                System.out.println("Sender Port: " + senderPort);

                SwingUtilities.invokeLater(() -> {
                    if (pf.jTextArea1 != null) {
                        pf.jTextArea1.append("Received: " + receivedDataString + " \r\n");
                    } else {
                        System.err.println("jTextArea1 is null or not properly initialized.");
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Connection attempt failed.");
            e.printStackTrace();
        }
    }).start();
}
     public void UDP_receiveAndSend_OLD() {
        JOptionPane.showMessageDialog(null, "Test UDP_client (Receive and Send)");
        New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得
        // 受信元のIPアドレスとポート
        String targetIP = pf.jTextField2.getText(); // 例: "192.168.11.2"
        final int targetPort = Integer.parseInt(pf.jTextField3.getText()); // 例: 5353

        System.out.println("IP: " + targetIP + "\nPort number: " + targetPort);
        SwingUtilities.invokeLater(() -> {
            pf.jTextArea1.append("IP: " + targetIP + "\nPort number: " + targetPort + " \r\nstart \r\n");
        });

        // UDPソケット作成
        int receivePort = Integer.parseInt(pf.jTextField3.getText()); // 例: 5353
        try {
            pf.udp_socket = new DatagramSocket(targetPort); // receivePort);
            System.out.println("Listening on port " + targetPort);
        } catch (SocketException ex) {
            System.err.println("Failed to create DatagramSocket on port " + targetPort + ": " + ex.getMessage());
            ex.printStackTrace();
            return; // 必要なら処理を中断
        }
        byte[] receiveBuffer = new byte[1024];
        new Thread(() -> {
            try {
                // リスナーを一度削除してから再登録
                for (ActionListener al : pf.jButton4.getActionListeners()) {
                    pf.jButton4.removeActionListener(al);
                }

                // ボタンのアクションリスナー設定
                pf.jButton4.addActionListener(e -> {
                    try {
                        //一回だけしか出来ないので追加2025/04/21                        
                       if (pf.udp_socket == null) { // ←ここ追加！
                            pf.udp_socket = new DatagramSocket(); // 必要ならポート指定もOK
                        }
                        //一回だけしか出来ないので追加2025/04/21                        
                        // 送信するメッセージを取得
                        String message = pf.jTextField1.getText();
                        byte[] sendData = message.getBytes();
                        InetAddress address = InetAddress.getByName(targetIP);
                        pf.udp_sendPacket = new DatagramPacket(sendData, sendData.length, address, targetPort);

                        pf.udp_socket.send(pf.udp_sendPacket); // データ送信
                        
                        System.out.println("SendData: " + message);
                        // テキストエリアに送信内容を表示
                        SwingUtilities.invokeLater(() -> {
                            pf.jTextArea1.append("SendData: " + message + " \r\n");
                        });
                    } catch (Exception ex) {
                        System.err.println("Error during data sending: " + ex.getMessage());
                    }
                });

                // 継続的なデータ受信
                while (true) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    pf.udp_socket.receive(receivePacket); // データ受信

                    // 受信したデータの処理
                    String senderAddress = receivePacket.getAddress().getHostAddress();
                    int senderPort = receivePacket.getPort();
                    int length = receivePacket.getLength();
                    byte[] data = receivePacket.getData();

                    // 受信データを文字列に変換
                    String receivedDataString = new String(data, 0, length, StandardCharsets.UTF_8);
                    System.out.println("receive: " + receivedDataString);
                    System.out.println("senderAddress: " + senderAddress);
                    System.out.println("senderPort: " + senderPort);

                    SwingUtilities.invokeLater(() -> {
                        if (pf.jTextArea1 != null) {
                            pf.jTextArea1.append("Received: " + receivedDataString + " \r\n");
                        } else {
                            System.err.println("jTextArea1 is null or not properly initialized.");
                        }
                    });
                }
            } catch (Exception e) {
                System.err.println("Connection attempt failed.");
                e.printStackTrace();
            }
        }).start();
    }
     public void TCP_server_receiveAndSend(){
    JOptionPane.showMessageDialog(null, "Test TCP_server (Receive and Send)");
    New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得

    // サーバー待ち受けポート
    String portText = pf.jTextField3.getText(); // ポート番号
    final int serverPort = Integer.parseInt(portText);

    System.out.println("Port number: " + serverPort);
    SwingUtilities.invokeLater(() -> {
        pf.jTextArea1.append("Port number: " + serverPort + " \r\nstart TCP Server\r\n");
    });

    // サーバーソケット作成とクライアント接続処理を別スレッドで実行
    new Thread(() -> {
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("TCP Server started on port " + serverPort);

            while (true) { // 複数のクライアント接続に対応するためループ
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());

                SwingUtilities.invokeLater(() -> {
                    pf.jTextArea1.append("Client connected from " + clientSocket.getInetAddress().getHostAddress() + " \r\n");
                });

                // クライアントとの通信処理を行う新しいスレッドを開始
                new Thread(() -> handleClient(clientSocket, pf)).start();
            }

        } catch (IOException e) {
            System.err.println("TCP Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }).start();

    }
private void handleClient(Socket clientSocket, New_lanJFrame pf) {
    try (InputStream inStream = clientSocket.getInputStream();
         OutputStream outStream = clientSocket.getOutputStream()) {

        // 送信ボタン（jButton4）アクション設定（クライアントごとに設定）
        for (ActionListener al : pf.jButton4.getActionListeners()) {
            pf.jButton4.removeActionListener(al);
        }
        pf.jButton4.addActionListener(e -> {
            try {
                String message = pf.jTextField1.getText();
                byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
                outStream.write(messageBytes);
                outStream.flush();
                System.out.println("SendData to " + clientSocket.getInetAddress().getHostAddress() + ": " + message);
                SwingUtilities.invokeLater(() -> {
                    pf.jTextArea1.append("SendData to " + clientSocket.getInetAddress().getHostAddress() + ": " + message + " \r\n");
                });
            } catch (IOException ex) {
                System.err.println("Error during data sending to " + clientSocket.getInetAddress().getHostAddress() + ": " + ex.getMessage());
            }
        });

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inStream.read(buffer)) != -1) {
            String receivedDataString = new String(buffer, 0, length, StandardCharsets.UTF_8);
            System.out.println("Received from " + clientSocket.getInetAddress().getHostAddress() + ": " + receivedDataString);
            SwingUtilities.invokeLater(() -> {
                pf.jTextArea1.append("Received from " + clientSocket.getInetAddress().getHostAddress() + ": " + receivedDataString + " \r\n");
            });
        }

        System.out.println("Client " + clientSocket.getInetAddress().getHostAddress() + " disconnected.");
        SwingUtilities.invokeLater(() -> {
            pf.jTextArea1.append("Client " + clientSocket.getInetAddress().getHostAddress() + " disconnected.\r\n");
        });

    } catch (IOException e) {
        System.err.println("Error handling client " + clientSocket.getInetAddress().getHostAddress() + ": " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing client socket: " + e.getMessage());
        }
    }
}      

     public void TCP_server_receiveAndSend_OLD() {
        JOptionPane.showMessageDialog(null, "Test TCP_server (Receive and Send)");
        New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得

        // サーバー待ち受けポート
        String portText = pf.jTextField3.getText(); // ポート番号
        final int serverPort = Integer.parseInt(portText);

        System.out.println("Port number: " + serverPort);
        SwingUtilities.invokeLater(() -> {
            pf.jTextArea1.append("Port number: " + serverPort + " \r\nstart TCP Server\r\n");
        });

        // サーバーソケット作成
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("TCP Server started on port " + serverPort);

            // クライアント接続を待つ
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());

            SwingUtilities.invokeLater(() -> {
                pf.jTextArea1.append("Client connected from " + clientSocket.getInetAddress().getHostAddress() + " \r\n");
            });

            // 入出力ストリーム準備
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);

            // リスナーを一度削除してから再登録
            for (ActionListener al : pf.jButton4.getActionListeners()) {
                pf.jButton4.removeActionListener(al);
            }

            // 送信ボタン（jButton4）アクション設定
            pf.jButton4.addActionListener(e -> {
                try {
                    String message = pf.jTextField1.getText();
                    out.println(message); // TCPでメッセージ送信
                    System.out.println("SendData: " + message);

                    SwingUtilities.invokeLater(() -> {
                        pf.jTextArea1.append("SendData: " + message + " \r\n");
                    });
                } catch (Exception ex) {
                    System.err.println("Error during data sending: " + ex.getMessage());
                }
            });

            // 継続的にデータ受信
            InputStream inStream = clientSocket.getInputStream();
            byte[] buffer = new byte[1024];

            int length;
            while ((length = inStream.read(buffer)) != -1) {
                String receivedDataString = new String(buffer, 0, length, StandardCharsets.UTF_8);
                System.out.println("Received: " + receivedDataString);

                SwingUtilities.invokeLater(() -> {
                pf.jTextArea1.append("Received: " + receivedDataString + " \r\n");
                });
            }
            System.out.println("Client disconnected.");

        } catch (IOException e) {
            System.err.println("TCP Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }).start();    
}
     public void TCP_client_receiveAndSend() {
        JOptionPane.showMessageDialog(null, "Test TCP_client (Connect and Send/Receive)");
        New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得

        // 接続先サーバーIPアドレスとポート
        String serverIP = pf.jTextField2.getText(); // サーバーIPアドレス
        String portText = pf.jTextField3.getText(); // サーバーポート
        final int serverPort = Integer.parseInt(portText);

        System.out.println("Server IP: " + serverIP);
        System.out.println("Port number: " + serverPort);
        SwingUtilities.invokeLater(() -> {
            pf.jTextArea1.append("Server IP: " + serverIP + "\r\nPort number: " + serverPort + " \r\nstart TCP Client\r\n");
        });

        new Thread(() -> {
            try (Socket socket = new Socket(serverIP, serverPort)) {
                System.out.println("Connected to server " + serverIP + ":" + serverPort);

                SwingUtilities.invokeLater(() -> {
                    pf.jTextArea1.append("Connected to server " + serverIP + ":" + serverPort + " \r\n");
                });

                // 入出力ストリーム準備
                InputStream inStream = socket.getInputStream();
                OutputStream outStream = socket.getOutputStream();

                // リスナーを一度削除してから再登録
                for (ActionListener al : pf.jButton4.getActionListeners()) {
                    pf.jButton4.removeActionListener(al);
                }

                // 送信ボタン（jButton4）アクション設定
                pf.jButton4.addActionListener(e -> {
                    try {
                        String message = pf.jTextField1.getText();
                        byte[] sendData = message.getBytes(StandardCharsets.UTF_8);
                        outStream.write(sendData);
                        outStream.flush();
                        System.out.println("SendData: " + message);

                        SwingUtilities.invokeLater(() -> {
                            pf.jTextArea1.append("SendData: " + message + " \r\n");
                        });
                    } catch (Exception ex) {
                        System.err.println("Error during data sending: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                });

                // 継続的にデータ受信
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inStream.read(buffer)) != -1) {
                    String receivedDataString = new String(buffer, 0, length, StandardCharsets.UTF_8);
                    System.out.println("Received: " + receivedDataString);

                    SwingUtilities.invokeLater(() -> {
                        pf.jTextArea1.append("Received: " + receivedDataString + " \r\n");
                    });
                }

                System.out.println("Server disconnected.");

            } catch (IOException e) {
                System.err.println("TCP Client error: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();         
    }

    public void UDP_stop(){
        New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得
            try {
                if (pf.udp_socket != null && !pf.udp_socket.isClosed()) {
                    pf.udp_socket.close();
                    pf.udp_socket=null;
                    System.out.println("pf.udp_socket.close();");
                }
            } catch (Exception ex) {
                System.err.println("Error closing socket: " + ex.getMessage());
            }
    }
    public void TCP_server_stop(){//closeボタンで閉じる。
        New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得
        try {
            if (pf.serverSocket != null && !pf.serverSocket.isClosed()) {
                pf.serverSocket.close(); // サーバーソケットを閉じる
                System.out.println("Server socket closed.");
            }
            if (pf.clientSocket != null && !pf.clientSocket.isClosed()) {
                pf.clientSocket.close(); // クライアントソケットを閉じる
                System.out.println("Client socket closed.");
            }
        } catch (IOException e) {
            System.out.println("Error while stopping server.");
            e.printStackTrace();
        } finally {
            // サーバースレッド停止の通知をUIなどで行う場合
            SwingUtilities.invokeLater(() -> {
                pf.update_receive_Area("Server stopped.");
            });
            System.out.println("Server stopped cleanup completed.");
        }
    }
    public void TCP_client_stop() {
        New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得
        try {
            if (pf.writer != null) {
                pf.writer.close(); // Writerを閉じる
                pf.writer = null;
            }
            if (pf.socket != null && !pf.socket.isClosed()) {
                pf.socket.close(); // ソケットを閉じる
                pf.socket = null;
                System.out.println("Disconnected from the server.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }
    
    
    
//以下
     public void UDP_mode() {
        JOptionPane.showMessageDialog(null, "Test UDP ");
           New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得
//         int port = 5555; // 受信するポート番号
       int port = Integer.parseInt(pf.jTextField3.getText());//

        try (DatagramSocket socket = new DatagramSocket(port)) {
            //socket.setSoTimeout(5000); // 5秒のタイムアウトを設定
            byte[] receiveData = new byte[1024];  // 受信データ用のバッファ
            DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);

            System.out.println("UDP Receiver started on port " + port);
                // ボタンのアクションリスナー設定
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
    }
     
    public void UDP_stop_receive(){
        New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得
            try {
                if (pf.udp_receiveSocket != null && !pf.udp_receiveSocket.isClosed()) {
                    pf.udp_receiveSocket.close();
                    pf.udp_receiveSocket=null;
                    System.out.println("pf.udp_receiveSocket.close();");
                }
            } catch (Exception ex) {
                System.err.println("Error closing socket: " + ex.getMessage());
            }
    }
    public void TCP_server() {
        SwingUtilities.invokeLater(() -> {
            New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得
            int port = 5353; // サーバーのポート番号
            pf.update_receive_Area("Server starting on port " + port);
            System.out.println("Server starting on port " + port);
            new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (ServerSocket serverSocket = new ServerSocket(port)) {
                    pf.update_receive_Area("Server is listening on port " + port);
                    System.out.println("Server is listening on port " + port);

                    // クライアントからの接続を待機
                    Socket clientSocket = serverSocket.accept();
                    pf.update_receive_Area("Client connected: " + clientSocket.getInetAddress());
                    System.out.println("Client connected: ");

                    try (
                        BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)
                    ) {
                        output.println("Server send");
                        System.out.println("Server send");
                        output.println("start while loop");
                        System.out.println("start while loop");
                        String message;
                        while ((message = input.readLine()) != null) {
                            System.out.println(message);
                            publish(message); // メッセージを SwingWorker に渡す
                            output.println("Echo: " + message); // クライアントにエコー返信
                            if ("END".equals(message)) {
                                pf.update_receive_Area("Ending connection as 'END' received.");
                                break;
                            }
                        }
                    } finally {
                        pf.update_receive_Area("Client connection closed.");
                        clientSocket.close();
                        System.out.println("stop while loop");
                    }
                } catch (IOException e) {
                    publish("Error: " + e.getMessage());
                }
                return null;
            }

                @Override
                protected void process(List<String> chunks) {
                    for (String message : chunks) {
                        pf.update_receive_Area(message); // 受信したメッセージを jTextArea1 に追加
                    }
                }

                @Override
                protected void done() {
                    pf.update_receive_Area("Server stopped.");
                    System.out.println("Server stopped.");
                }
            }.execute();
        });
    }
    public void TCP_server_cutting_corners() {
        New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得
        //int port = 5353; // サーバーのポート番号
        int port = Integer.parseInt(pf.jTextField3.getText());
        //JOptionPane.showMessageDialog(null,
        //    "port Numbers: " + String.valueOf(port), "test", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("Port number: " + port);
        pf.update_receive_Area("Server starting on port " + port);
        System.out.println("Server starting on port " + port);
        new Thread(() -> {
            //try (ServerSocket serverSocket = new ServerSocket(port)) {
            try {
                //serverSocket = new ServerSocket(port);//25行目へ
                pf.serverSocket = new ServerSocket(port);//25行目へ
                //pf.serverSocket.setReuseAddress(true); // ポート再利用を有効化 ys 1214
                pf.update_receive_Area("Server is listening on port " + port);
                System.out.println("Server is listening on port " + port);
                // クライアントからの接続を待機
                //Socket clientSocket = serverSocket.accept();
                pf.clientSocket = pf.serverSocket.accept();
                SwingUtilities.invokeLater(() -> {
                    pf.update_receive_Area("Client connected: " + pf.clientSocket.getInetAddress());
                });    
                System.out.println("Client connected: ");

                try (
                    BufferedReader input = new BufferedReader(new InputStreamReader(pf.clientSocket.getInputStream()));
                    PrintWriter output = new PrintWriter(pf.clientSocket.getOutputStream(), true)
                ) {
                    output.println("Server send");
                    System.out.println("Server send");
                    //pf.update_receive_Area("Server send");
                    output.println("start while loop");
                    System.out.println("start while loop");
                    String message;
                    while ((message = input.readLine()) != null) {
                        String binaryOutput = convertToBinary(message);
                        String hexOutput = convertToHex(message);
                        System.out.println(message);
                        final String message1=message;
                        SwingUtilities.invokeLater(() -> {
                            //pf.update_receive_Area(message1);
                            pf.jTextArea1.append("str="+message1+ " \r\n");
                            pf.jTextArea1.append("bin="+binaryOutput+ " \r\n");
                            pf.jTextArea1.append("hex="+hexOutput+ " \r\n");
                       });
                        if ("END".equals(message)) {
                            break;
                        }
                    }
                } finally {
                    pf.update_receive_Area("Client connection closed.");
                    pf.serverSocket.close();//ys 1214++
                    pf.clientSocket.close();
                    System.out.println("stop while loop");
                }
            } catch (IOException e) {//エラーが出るが動いてしまう？
                String errorMessage = e.getMessage();
                pf.update_receive_Area("Error"+errorMessage);
            }
        }).start();            
    }

    public void TCP_client_old() {
        JOptionPane.showMessageDialog(null, "Test TCP_client");
        New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得
//        Socket socket = null;
//        PrintWriter writer = null;
        new Thread(() -> {
            try {
                // 送信先のIPアドレスとポート
                //String targetIP = "192.168.11.4"; // 送信先のIPアドレス STM32+W5500
                String targetIP = pf.jTextField2.getText();//"192.168.11.2"; // 
                int targetPort = Integer.parseInt(pf.jTextField3.getText());//5353;            // 送信先のポート番号
            
                System.out.println("IP:"+targetIP+ "/r/n"+"Port number: " + targetPort);

                // サーバーに接続
                pf.socket = new Socket();
                pf.socket.connect(new InetSocketAddress(targetIP, targetPort), 5000);  // タイムアウトを設定（例: 5000ms）
                Thread.sleep(1000);//無いと動かなかった。500では動かなかった。 
                if (pf.socket.isConnected()) {
                    System.out.println("Connected to the server: " + targetIP + ":" + targetPort);
                    // データ送信の準備
                    OutputStream outputStream = pf.socket.getOutputStream();
                    pf.writer = new PrintWriter(new OutputStreamWriter(outputStream), true); // バッファ付き
                    // 送信するデータ
                    String message = "Hello, TCP!";
                    // インスタンスからテキストを取得
                    message = pf.jTextField1.getText();
                    JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("TextField Content: " + pf.jTextField1.getText());
                
                    pf.writer.println(message); // データを送信
                    pf.writer.flush(); // 明示的にflushを呼び出す
                    System.out.println("Send data: " + message);
                } else {
                    System.out.println("Failed to connect to the server.");
                }
            } catch (Exception e) {
                System.err.println("Attempt failed. Retrying...");
                e.printStackTrace();
            } /*finally {
                // リソースを解放
                try {
                    if (pf.writer != null) {
                        pf.writer.close();
                    }
                    if (pf.socket != null && !pf.socket.isClosed()) {
                        pf.socket.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/  
        }).start();            
    }
    //send every
    public void TCP_client_every() {
        JOptionPane.showMessageDialog(null, "Test TCP_client");
        New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(); // 定期的な送信のためのスケジューラー

        new Thread(() -> {
            try {
                // 送信先のIPアドレスとポート
                String targetIP = pf.jTextField2.getText(); // 例: "192.168.11.2"
                int targetPort = Integer.parseInt(pf.jTextField3.getText()); // 例: 5353

                System.out.println("IP: " + targetIP + "\nPort number: " + targetPort);

                // サーバーに接続
                pf.socket = new Socket();
                pf.socket.connect(new InetSocketAddress(targetIP, targetPort), 5000); // タイムアウトを設定（例: 5000ms）
                Thread.sleep(1000); // 接続安定待ち

                if (pf.socket.isConnected()) {
                    System.out.println("Connected to the server: " + targetIP + ":" + targetPort);

                    // データ送信の準備
                    OutputStream outputStream = pf.socket.getOutputStream();
                    pf.writer = new PrintWriter(new OutputStreamWriter(outputStream), true);

                    // 継続的なデータ送信をスケジュール
                    executor.scheduleAtFixedRate(() -> {
                        try {
                            String message = pf.jTextField1.getText(); // テキストフィールドから取得
                            pf.writer.println(message); // データ送信
                            pf.writer.flush();
                            System.out.println("Send data: " + message);
                            final String message1=message;
                            SwingUtilities.invokeLater(() -> {
                                //pf.update_receive_Area(message1);
                                pf.jTextArea1.append("str="+message1+ " \r\n");
                            });

                        } catch (Exception e) {
                            System.err.println("Error during data sending: " + e.getMessage());
                        }
                    }, 0, 3, TimeUnit.SECONDS); // 初回0秒後、以降3秒ごとに送信
                } else {
                    System.out.println("Failed to connect to the server.");
                }
            } catch (Exception e) {
                System.err.println("Connection attempt failed.");
                e.printStackTrace();
            }
        }).start();
    }
    public void TCP_client_receiveOnly() {
    JOptionPane.showMessageDialog(null, "Test TCP_client (Receive Only)");
    New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得

    new Thread(() -> {
        try {
            // 受信元のIPアドレスとポート
            String targetIP = pf.jTextField2.getText(); // 例: "192.168.11.2"
            int targetPort = Integer.parseInt(pf.jTextField3.getText()); // 例: 5353

            System.out.println("IP: " + targetIP + "\nPort number: " + targetPort);

            // サーバーに接続
            pf.socket = new Socket();
            pf.socket.connect(new InetSocketAddress(targetIP, targetPort), 5000); // タイムアウトを設定（例: 5000ms）
            Thread.sleep(1000); // 接続安定待ち

            if (pf.socket.isConnected()) {
                System.out.println("Connected to the server: " + targetIP + ":" + targetPort);

                // データ受信の準備
                InputStream inputStream = pf.socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                // 継続的なデータ受信
                while (true) {
                    String receivedMessage = reader.readLine(); // 1行ずつ受信
                    if (receivedMessage != null) {
                        System.out.println("Received data: " + receivedMessage);
                        SwingUtilities.invokeLater(() -> {
                            pf.jTextArea1.append("Received: " + receivedMessage + " \r\n");
                        });
                    } else {
                        System.out.println("Connection closed by server.");
                        break;
                    }
                }
            } else {
                System.out.println("Failed to connect to the server.");
            }
        } catch (Exception e) {
            System.err.println("Connection attempt failed.");
            e.printStackTrace();
        } finally {
            try {
                if (pf.socket != null && !pf.socket.isClosed()) {
                    pf.socket.close();
                }
            } catch (Exception ex) {
                System.err.println("Error closing socket: " + ex.getMessage());
            }
        }
    }).start();
}
    public void TCP_client_sendOnly() {
        JOptionPane.showMessageDialog(null, "Test TCP_client (Send Only)");
        New_lanJFrame pf = New_lanJFrame.getInstance(); // メインフレームのインスタンス取得

        try {
            // 送信先のIPアドレスとポート
            String targetIP = pf.jTextField2.getText(); // 例: "192.168.11.2"
            int targetPort = Integer.parseInt(pf.jTextField3.getText()); // 例: 5353

            System.out.println("IP: " + targetIP + "\nPort number: " + targetPort);

            // サーバーに接続
            pf.socket = new Socket();
            pf.socket.connect(new InetSocketAddress(targetIP, targetPort), 5000); // タイムアウトを設定（例: 5000ms）
            Thread.sleep(1000); // 接続安定待ち

            if (pf.socket.isConnected()) {
                System.out.println("Connected to the server: " + targetIP + ":" + targetPort);

                // データ送信の準備
                OutputStream outputStream = pf.socket.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream), true);

                // ボタンのアクションリスナー設定
                pf.jButton4.addActionListener(e -> {
                    try {
                        // 送信するメッセージを取得
                        String message = pf.jTextField1.getText();
                        writer.println(message); // データ送信
                        writer.flush();
                        System.out.println("Send data: " + message);

                        // テキストエリアに送信内容を表示
                        SwingUtilities.invokeLater(() -> {
                            pf.jTextArea1.append("Sent: " + message + " \r\n");
                        });
                    } catch (Exception ex) {
                        System.err.println("Error during data sending: " + ex.getMessage());
                    }
                });

            } else {
                System.out.println("Failed to connect to the server.");
            }
        } catch (Exception e) {
            System.err.println("Connection attempt failed.");
            e.printStackTrace();
        }
    }

    
    // 入力文字列をバイナリ形式に変換する関数
    //String binaryOutput = convertToBinary(message); // 関数を呼び出して変換
    //System.out.println("Binary: " + binaryOutput); // 結果を表示
    public static String convertToBinary(String message) {
        byte[] bytes = message.getBytes(); // 文字列をバイト配列に変換
        StringBuilder binary = new StringBuilder();

        for (byte b : bytes) {
            binary.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0')); // バイナリ形式に変換
        }

        return binary.toString(); // バイナリ形式の文字列を返す
    }
    // 入力文字列をHEX形式に変換する関数
    //String hexOutput = convertToHex(message); // 関数を呼び出して変換
    //System.out.println("Hexadecimal: " + hexOutput); // 結果を表示

    public static String convertToHex(String message) {
        byte[] bytes = message.getBytes(); // 文字列をバイト配列に変換
        StringBuilder hex = new StringBuilder();

        for (byte b : bytes) {
            hex.append(String.format("%02X", b)); // HEX形式に変換
        }

        return hex.toString(); // HEX形式の文字列を返す
    }
    
 }
//受信メッセージを青、送信メッセージを緑に設定。
