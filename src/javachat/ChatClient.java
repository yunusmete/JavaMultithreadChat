/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javachat;

/**
 *
 * @author YUNUS
 */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient implements Runnable {

    private static Socket clientSocket = null;
    private static PrintStream ps = null;
    private static DataInputStream dis = null;
    private static BufferedReader giris = null;
    private static boolean kapaliMi = false;

    public static void main(String[] args) {

        int portNo = 3333;
        String host = "localhost";

        if (args.length < 2) {
            System.out.println("Port Numarası: " + portNo);
        } else {
            host = args[0];
            portNo = Integer.valueOf(args[1]).intValue();
        }

        /*
         * Girilen host ve port numaraları ile Client açılması
         */
        try {
            clientSocket = new Socket(host, portNo);
            giris = new BufferedReader(new InputStreamReader(System.in));
            ps = new PrintStream(clientSocket.getOutputStream());
            dis = new DataInputStream(clientSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Bağlantı sağlanamadı");
        }

        /*
         * Bağlantının başarılı şekilde gerçekleşmesi halinde mesaj yazma
         */
        if (clientSocket != null && ps != null && dis != null) {
            try {

                /* Serverdan okuma için Thread */
                new Thread(new ChatClient()).start();
                while (!kapaliMi) {
                    ps.println(giris.readLine().trim());
                }

                ps.close();
                dis.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }

    @Override
    public void run() {
        String cevap;
        try {
            while ((cevap = dis.readLine()) != null) {
                System.out.println(cevap);
                if (cevap.indexOf("Gule") != -1) {
                    break;
                }
            }
            kapaliMi = true;
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
}
