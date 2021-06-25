package https;

import controllers.*;
import javafx.scene.control.Alert;
import product.*;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

import static controllers.ProductsController.showAlert;

public class HTTPSClient {

    private String host = "127.0.0.1";
    private int port = 9999;

    // CategoryController
    private int query = 0;
    private CategoryController cc;
    private Integer item;
    private String search;

    // CategoryViewController
    private CategoryViewController cvc;
    public Category category;
    public boolean checkCvc = false;
    private String nameCvc;
    private String descCvc;
    private String itemTitle;

    // RegistrationController
    private String loginRc;
    public boolean checkRc = false;
    private User userRc;

    // LoginController
    public LoginController lc;
    public boolean checkLc = false;

    // ProductsController
    private ProductsController pc;
    private ProductFilter filter;
    private int idToDelete;
    private String categoryTitle;

    //ProductView
    private ProductViewController pvc;
    private Integer idToGet;
    private Product updProduct;

    //orderController
    private OrderController oc;
    public int prodId;

    public HTTPSClient(int i, ProductsController productsController, int id) {
        this.query = i;
        this.pc = productsController;
        this.idToDelete = id;
        run();
    }

    public HTTPSClient(int i, ProductsController pc, String category) {
        this.query = i;
        this.pc = pc;
        this.categoryTitle = category;
        run();
    }

    public HTTPSClient(int i, ProductViewController pc, String category) {
        this.query = i;
        this.pvc = pc;
        this.categoryTitle = category;
        run();
    }

    public HTTPSClient(int i, ProductsController productsController, ProductFilter filter) {
        this.query = i;
        this.pc = productsController;
        this.filter = filter;
        run();
    }

    public HTTPSClient(int i, ProductViewController productViewController) {
        this.query = i;
        this.pvc = productViewController;
        run();
    }

    public HTTPSClient(int i, ProductViewController productViewController, Integer itemId) {
        this.query = i;
        this.pvc = productViewController;
        this.idToGet = itemId;
        run();
    }

    public HTTPSClient(int i, ProductViewController productViewController, Product p) {
        this.query = i;
        this.pvc = productViewController;
        this.updProduct = p;
        run();
    }

    public HTTPSClient(int i, OrderController orderController, int prodId) {
        this.query = i;
        this.prodId = prodId;
        this.oc = orderController;
        run();
    }

    public HTTPSClient(int i, OrderController orderController, Product p) {
        this.query = i;
        this.oc = orderController;
        this.updProduct = p;
        run();
    }

    public static void main(String[] args) {
        HTTPSClient client = new HTTPSClient();
        client.run();
    }

    public HTTPSClient() {
    }

    HTTPSClient(String host, int port) {
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

    // CategoryController getCategory
    public HTTPSClient(int query, CategoryController cc, String search) {
        this.query = query;
        this.cc = cc;
        this.search = search;
        this.run();
    }

    // CategoryViewController getCategory(4)
    public HTTPSClient(int query, CategoryViewController cvc, String search) {
        this.query = query;
        this.cvc = cvc;
        this.search = search;
        this.run();
    }

    // CategoryViewController insertCategory(6)
    public HTTPSClient(int query, CategoryViewController cvc, Category category) {
        this.query = query;
        this.cvc = cvc;
        this.category = category;
        this.run();
    }

    // CategoryViewController updateCategory(7)
    public HTTPSClient(int query, CategoryViewController cvc, String nameCvc, String descCvc, String itemTitle) {
        this.query = query;
        this.cvc = cvc;
        this.nameCvc = nameCvc;
        this.descCvc = descCvc;
        this.itemTitle = itemTitle;
        this.run();
    }

    // RegistrationController login (8)
    public HTTPSClient(int query, String loginRc) {
        this.query = query;
        this.loginRc = loginRc;
        this.run();
    }

    // RegistrationController insertUser (9)
    public HTTPSClient(int query, User userRc) {
        this.query = query;
        this.userRc = userRc;
        this.run();
    }

    //ProductsController fetch all products
    public HTTPSClient(int query, ProductsController pc) {
        this.query = query;
        this.pc = pc;
        this.run();
    }

    // LoginController
    public HTTPSClient(int query, LoginController lc) {
        this.query = query;
        this.lc = lc;
        this.checkLc = false;
        this.run();
    }

    // Thread handling the socket to server
    class ClientThread extends Thread {
        private SSLSocket sslSocket = null;

        // CategoryController
        private int query = 0;
        private final CategoryController cc;
        private final Integer item;
        private final String search;

        // CategoryViewController
        private final CategoryViewController cvc;
        public Category category;
        public boolean checkCvc;
        private final String nameCvc;
        private final String descCvc;
        private final String itemTitle;

        // RegistraionController
        private final String loginRc;
        private final User userRc;

        // LoginController
        private final LoginController lc;
        public boolean checkLc;

        //ProductsContoller
        private final ProductsController pc;
        private ProductFilter filter;
        Integer idToDelete;
        String categoryTitle;
        ProductViewController pvc;
        Integer idToGet;
        Product updProduct;
        OrderController oc;
        int prodId;

        //        ClientThread(SSLSocket sslSocket, int query, CategoryController cc, Integer item, LoginController lc, ProductsController pc, Integer idToDelete, String categoryTitle, ProductViewController pvc, Integer idToGet, Product p, boolean checkLc) {
        ClientThread(SSLSocket sslSocket, int query, CategoryController cc, Integer item, LoginController lc,
                     boolean checkLc, String search, CategoryViewController cvc, Category category, boolean checkCvc,
                     String nameCvc, String descCvc, String itemTitle, String loginRc, User userRc,
                     ProductsController pc, ProductFilter filter, Integer idToDelete, String categoryTitle, ProductViewController pvc, Integer idToGet, Product updProduct, OrderController oc, int prodId) {
            this.sslSocket = sslSocket;


            this.pc = pc;
            this.filter = filter;
            this.idToDelete = idToDelete;
            this.categoryTitle = categoryTitle;
            this.pvc = pvc;
            this.idToGet = idToGet;
            this.updProduct = updProduct;
            this.oc = oc;
            this.prodId = prodId;


            // CategoryController
            this.query = query;
            this.cc = cc;
            this.item = item;
            this.search = search;

            // CategoryViewController
            this.cvc = cvc;
            this.category = category;
            this.checkCvc = checkCvc;
            this.nameCvc = nameCvc;
            this.descCvc = descCvc;
            this.itemTitle = itemTitle;

            // RegistrationController
            this.loginRc = loginRc;
            this.userRc = userRc;

            // LoginController
            this.lc = lc;
            this.checkLc = checkLc;
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

                // Write data
                printWriter.println(this.query);
                if (this.query == 2) {
                    printWriter.println(item);
                }
                if (this.query == 5) {
                    printWriter.println(lc.usernameField.getText() + "," + lc.passField.getText());
                }
                if (this.query == 3) {
                    printWriter.println(search);
                }
                if (this.query == 4) {
                    printWriter.println(search);
                }
                if (this.query == 6) {
                    printWriter.println(category.parseCategory());
                }
                if (this.query == 7) {
                    printWriter.println(nameCvc + "," + descCvc + "," + itemTitle);
                }
                if (this.query == 8) {
                    printWriter.println(loginRc);
                }
                if (this.query == 9) {
                    printWriter.println(userRc.getLogin() + "," + userRc.getPassword());
                }
                if (this.query == 13) {
                    printWriter.println(idToDelete);
                }
                if (this.query == 14) {
                    printWriter.println(categoryTitle);
                }
                if (this.query == 15) {
                    this.pc.filteredProducts.clear();
                    printWriter.println(Tools.mySerialize(filter));
                }
                if (this.query == 17) {
                    printWriter.println(idToGet);
                }
                if (this.query == 18 || this.query == 19) {
                    System.out.println("SENDING TO SERVER (update):");
                    System.out.println(updProduct);
                    printWriter.println(Tools.mySerialize(updProduct));
                }
                if (this.query == 20) {
                    printWriter.println(prodId);
                }
                printWriter.println();
                printWriter.flush();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println("Input : " + line.trim());
                    if (line.trim().isEmpty()) {
                        //System.out.println("HERE");
                        break;
                    }
                    if (query == 1 || query == 3) {
                        System.out.println("THAT LINE: " + line);
                        Integer id = Integer.parseInt(line.trim().split(",")[0]);
                        String description = line.trim().split(",")[1];
                        String title = line.trim().split(",")[2];
                        cc.table.getItems().add(new Category(id, title, description));
                    }
                    if (query == 5) {
                        if (line.trim().equals("1")) {
                            HTTPSClient.this.checkLc = true;
                            System.out.println("OKAY");
                        } else {
                            HTTPSClient.this.checkLc = false;
                            System.out.println("NOT OKAY");
                        }
                    }
                    //get all products
                    if (query == 10) pc.table.getItems().add(Product.fromFormat(line.trim()));
                    if (query == 11) pc.categoryList.getItems().add(line.trim());
                    if (query == 12) pc.producerList.getItems().add(line.trim());
                    if (query == 13) {
                        int res = Integer.parseInt(line.trim());
//                        if (res == 0) {
//                            showAlert(Alert.AlertType.ERROR, "DB error", "Can't delete product!");
//                        } else if (res == 1) {
//                            showAlert(Alert.AlertType.CONFIRMATION, "DB operation success", "Product deleted!");
//                        }
                    }
                    if (query == 14) {
                        if (pc != null) pc.selectedCategoryId = Integer.parseInt(line.trim());
                        else if (pvc != null) pvc.categoryId = Integer.parseInt(line.trim());
                    }
                    if (query == 15) {
//                        System.out.println(Tools.myDeserialize(line));
                        pc.filteredProducts.add((Product) Tools.myDeserialize(line));
                    }
                    if (query == 16) pvc.categoryBox.getItems().add(line.trim());
                    if (query == 17) pvc.product = Product.fromFormat(line.trim());
                    if (query == 20) oc.amount = Product.fromFormat(line.trim()).getAmount();
                    if (query == 4) {
                        Integer id = Integer.parseInt(line.trim().split(",")[0]);
                        String description = line.trim().split(",")[1];
                        String title = line.trim().split(",")[2];
                        cvc.category = new Category(title, description);
//                        cvc.nameField.setPromptText(title);
//                        cvc.descArea.setPromptText(description);
                    }
                    if (query == 6) {
                        if (line.trim().equals("1")) {
                            HTTPSClient.this.checkCvc = true;
                            System.out.println("OKAY");
                        } else {
                            System.out.println("NOT OKAY");
                            HTTPSClient.this.checkCvc = false;
                        }
                    }
                    if (query == 8) {
                        if (line.trim().equals("1")) {
                            System.out.println("NORM");
                            HTTPSClient.this.checkRc = true;
                        } else {
                            System.out.println("NOT NORM");
                            HTTPSClient.this.checkRc = false;
                        }
                    }
                }
                sslSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
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
            // Create socket factory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            // Create socket
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(this.host, this.port);

            System.out.println("SSL client started");
            new ClientThread(sslSocket, query, cc, item, lc, checkLc, search, cvc, category, checkCvc, nameCvc, descCvc,
                    itemTitle, loginRc, userRc, pc, filter, idToDelete, categoryTitle, pvc, idToGet, updProduct, oc, prodId).start();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "DB", "Couldn't connect to the database!");
            System.exit(0);
        }
    }
}