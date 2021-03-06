package https;

import javafx.scene.control.Alert;
import product.Category;
import product.DBConnection;
import product.SQLOperations;
import product.User;
import product.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.sql.Connection;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import static controllers.ProductsController.showAlert;

public class HTTPSServer {

    private int port = 9999;
    private boolean isServerDone = false;

    private SQLOperations sql;
    private Connection con;

    public static void main(String[] args) {
        HTTPSServer server = new HTTPSServer();
        server.run();
    }

    HTTPSServer() {
        new DBConnection("WarehouseDB", false);
        this.con = DBConnection.getConnection();
        sql = new SQLOperations(con);
    }

    HTTPSServer(int port) {
        this.port = port;
    }

    // Create the and initialize the SSLContext
    private SSLContext createSSLContext() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream("mykey.jks"), "password".toCharArray());

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
            sslContext.init(km, tm, null);

            return sslContext;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Start to run the server
    public void run() {
        SSLContext sslContext = this.createSSLContext();

        try {
            // Create server socket factory
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

            // Create server socket
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(this.port);

            System.out.println("SSL server started");
            while (!isServerDone) {
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                //System.out.println("lol");
                // Start the server thread
                new ServerThread(sslSocket, sql).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Thread handling the socket from client
    static class ServerThread extends Thread {
        private SSLSocket sslSocket = null;
        private SQLOperations sql;

        ServerThread(SSLSocket sslSocket, SQLOperations sql) {
            this.sslSocket = sslSocket;
            this.sql = sql;
        }

        public void run() {
            sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());

            try {
                // Start handshake
                sslSocket.startHandshake();

                // Get session after the connection is established
                SSLSession sslSession = sslSocket.getSession();

                System.out.println("SSLSession :");
                System.out.println("\tProtocol : " + sslSession.getProtocol());
                System.out.println("\tCipher suite : " + sslSession.getCipherSuite());

                // Start handling application content
                InputStream inputStream = sslSocket.getInputStream();
                OutputStream outputStream = sslSocket.getOutputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println("Input : " + line);

                    //all categories
                    if (line.trim().equals("1")) {
                        //System.out.println("URAAAAAAAAAAAAAAAAAA");
                        List<Category> categories = this.sql.showCategories();
                        for (Category c : categories) {
                            System.out.println(c);
                            printWriter.println(c.parseCategory());
//                            printWriter.println(Tools.mySerialize(c));
                        }
                    }

                    //deleting category with id X
                    if (line.trim().equals("2")) {
                        line = bufferedReader.readLine();
                        sql.deleteCategory(Integer.parseInt(line));
                    }
                    //login
                    if (line.trim().equals("5")) {
                        line = bufferedReader.readLine();
                        String usernameField = line.split(",")[0];
                        String passField = line.split(",")[1];
                        if (sql.login(usernameField, passField)) {
                            printWriter.println(1);
                        } else {
                            printWriter.println(0);
                        }
                    }
                    if (line.trim().equals("3")) {
                        line = bufferedReader.readLine();
                        Category cat = sql.getCategory(line);
                        printWriter.println(cat.parseCategory());
//                        printWriter.println(Tools.mySerialize(cat));
                    }
                    if (line.trim().equals("4")) {
                        line = bufferedReader.readLine();
                        Category cat = sql.getCategory(line);
                        printWriter.println(cat.parseCategory());
                    }
                    if (line.trim().equals("6")) {
                        line = bufferedReader.readLine();
                        System.out.println("RECEIVED: " + line);
//                        Integer id = Integer.parseInt(line.trim().split(",")[0]);
                        String description = line.trim().split(",")[1];
                        String title = line.trim().split(",")[2];
                        Category insCat = new Category(title, description);
                        try {
                            sql.insertCategory(insCat);
                            printWriter.println(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            printWriter.println(0);
                        }
                    }
                    if (line.trim().equals("7")) {
                        line = bufferedReader.readLine();
                        String nameCvc = line.trim().split(",")[0];
                        String descCvc = line.trim().split(",")[1];
                        String itemTitle = line.trim().split(",")[2];
                        int id = sql.getCategoryId(itemTitle);
                        if(nameCvc.equals("null")) nameCvc = null;
                        if(descCvc.equals("null")) descCvc= null;
                        sql.updateCategory(id, nameCvc, descCvc);
                    }
                    if (line.trim().equals("8")) {
                        line = bufferedReader.readLine();
                        if (!sql.isLoginFree(line)) {
                            printWriter.println(0);
                        } else {
                            printWriter.println(1);
                        }
                    }
                    if (line.trim().equals("9")) {
                        line = bufferedReader.readLine();
                        String login = line.trim().split(",")[0];
                        String password = line.trim().split(",")[1];
                        System.out.println("userInsert: " + login + " " + password);
                        sql.insertUser(new User(login, password));
                    }
                    //fetch all products
                    if (line.trim().equals("10")) {
//                        line = bufferedReader.readLine();
                        List<Product> products = sql.getAllProducts();
                        for (Product p : products) {
                            System.out.println(p);
                            printWriter.println(p.toFormat());
                        }
                    }
                    // category titles
                    if (line.trim().equals("11")) {
//                        line = bufferedReader.readLine();
                        List<String> titles = sql.getCategoryTitles();
                        for (String s : titles) printWriter.println(s);
                    }
                    if (line.trim().equals("12")) {
//                        line = bufferedReader.readLine();
                        List<String> titles = sql.getProducersTitles();
                        for (String s : titles) printWriter.println(s);
                    }
                    if (line.trim().equals("13")) {
                        int id = Integer.parseInt(bufferedReader.readLine());
                        boolean res = sql.deleteProduct(id);
                        int i = res ? 1 : 0;
                        printWriter.println(i);
                    }
                    if (line.trim().equals("14")) {
                        String title = bufferedReader.readLine();
                        Integer id = sql.getCategoryId(title);
                        printWriter.println(id);
                    }
                    if (line.trim().equals("15")) {
                        ProductFilter filter = (ProductFilter) Tools.myDeserialize(bufferedReader.readLine());
                        System.out.println(filter);
                        List<Product> resultList = sql.getByCriteria(filter);
                        for (Product p : resultList) {
                            System.out.println(Tools.mySerialize(p));
                            printWriter.println(Tools.mySerialize(p));
                        }
                    }
                    if (line.trim().equals("16")) {
                        List<String> list = sql.getCategoryTitles();
                        for (String s : list) {
                            System.out.println(s);
                            printWriter.println(s);
                        }
                    }
                    if (line.trim().equals("17") || line.trim().equals("20")) {
                        Product p = sql.getProduct(Integer.parseInt(bufferedReader.readLine().trim()));
                        System.out.println(p);
                        printWriter.println(p.toFormat());
                    }
                    if (line.trim().equals("18")) {
                        String s = bufferedReader.readLine();
                        System.out.println(s);
                        Product p = (Product) Tools.myDeserialize(s);
                        sql.updateProduct(p);
                    }
                    if (line.trim().equals("19")) {
                        Product p = (Product) Tools.myDeserialize(bufferedReader.readLine());
                        System.out.println(p);
                        sql.insertProductData(p);
                    }
                    if (line.trim().isEmpty()) {
                        break;
                    }
                }

                // Write data
                printWriter.println();
                printWriter.flush();

                sslSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}