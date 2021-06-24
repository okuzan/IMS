package https;

import controllers.CategoryController;
import product.Category;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class HTTPSClient {
    private String host = "127.0.0.1";
    private int port = 9999;

    // CategoryController
    private int query = 0;
    private CategoryController cc;
    private Integer item;

    public static void main(String[] args){
        HTTPSClient client = new HTTPSClient();
        client.run();
    }

    HTTPSClient(){
    }

    HTTPSClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    // CategoryController show
    public HTTPSClient(int query, CategoryController cc) {
        this.query = query;
        this.cc = cc;
        this.run();
    }

    // CategoryController delete item
    public HTTPSClient(int query, Integer item) {
        this.query = query;
        this.item = item;
        this.run();
    }

    // Create the and initialize the SSLContext
    private SSLContext createSSLContext(){
        try{
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("testkey.jks"),"password".toCharArray());

            // Create key manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, "password".toCharArray());
            KeyManager[] km = keyManagerFactory.getKeyManagers();

            // Create trust manager
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);
            TrustManager[] tm = trustManagerFactory.getTrustManagers();

            // Initialize SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(km,  tm, null);

            return sslContext;
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    // Start to run the server
    public void run(){
        SSLContext sslContext = this.createSSLContext();

        try{
            // Create socket factory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Create socket
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(this.host, this.port);

            System.out.println("SSL client started");
            new ClientThread(sslSocket, query, cc, item).start();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Thread handling the socket to server
    static class ClientThread extends Thread {
        private SSLSocket sslSocket = null;

        // CategoryController
        private int query = 0;
        private CategoryController cc;
        private Integer item;

        ClientThread(SSLSocket sslSocket, int query, CategoryController cc, Integer item){
            this.sslSocket = sslSocket;
            this.query = query;
            this.cc = cc;
            this.item = item;
        }

        public void run(){
            sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());

            try{
                // Start handshake
                sslSocket.startHandshake();

                // Get session after the connection is established
                SSLSession sslSession = sslSocket.getSession();

                System.out.println("SSLSession :");
                System.out.println("\tProtocol : "+sslSession.getProtocol());
                System.out.println("\tCipher suite : "+sslSession.getCipherSuite());

                // Start handling application content
                InputStream inputStream = sslSocket.getInputStream();
                OutputStream outputStream = sslSocket.getOutputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));

                // Write data
                printWriter.println(this.query);
                if (this.query == 2) {
                    printWriter.println(item);
                }
                printWriter.println();
                printWriter.flush();

                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    System.out.println("Input : "+line.trim());
                    if(line.trim().isEmpty()){
                        //System.out.println("HERE");
                        break;
                    }
                    if (query == 1) {
                        Integer id = Integer.parseInt(line.trim().split(",")[0]);
                        String description = line.trim().split(",")[1];
                        String title = line.trim().split(",")[2];
                        cc.table.getItems().add(new Category(id, title, description));
                    }
                }

                //System.out.println("here");
                sslSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}