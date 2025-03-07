


import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Klasa aplikacji serwera
 */
public class TreeServerApplication extends Application {
    /**
     * Miejsce dla informacji o serwerze
     */
    public static TextArea txtArea;

    /**
     * Metoda paramsParser podziela parametry, które pochodzą od klienta
     *
     * @param command Komenda, jaką dostaliśmy od klienta
     * @return Osobne parametry węc operacja do wykonania i elementy
     */
    public static String[] paramsParser(String command) {
        String[] params = command.split(" ");
        return params;
    }

    /**
     * Metoda main to metoda główna
     * @param args argumenty, które były przekazane do programu
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Metoda start wystartuje aplikację JavaFX
     *
     * @param stage Główne okno aplikacji
     * @throws IOException Problem z uruchomieniem
     */
    @Override
    public void start(Stage stage) throws IOException {

        /**
         *  Utworzenie nowej TextArea
         */
        txtArea = new TextArea();
        txtArea.setEditable(false);
        /**
         * Przecisk, który uruchamia server
         */
        Button start = new Button("Start");
        start.setOnAction(e -> startServer());

        /**
         * Kontener główny, który zawiera TextArea "txtArea" i Button "Start"
         */
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(txtArea);
        borderPane.setRight(start);
        borderPane.setPadding(new Insets(10));
        /**
         * Scena główna
         */
        Scene scene = new Scene(borderPane, 400, 400);
        stage.setTitle("Main Server");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Metoda startServer która uruchamia server
     */
    public void startServer() {

        //Wątek glówny serwera
        new Thread(() -> {
            //Gniazdo serwera
            ServerSocket serverSocket;
            try {
                //Utworzenie serwera
                serverSocket = new ServerSocket(1234);
                txtArea.appendText("Server started");
                while (true) {
                    //Połączenie z klientem i utworzenie nowego połączenia sieciowego
                    Socket clientSocket = serverSocket.accept();
                    //Utworzenie i startowanie nowego wątku klienta
                    ServerThread clientThread = new ServerThread(clientSocket);
                    clientThread.start();

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    /**
     * Klasa wątków serwera dla klienta
     */
    public class ServerThread extends Thread {
        /**
         * Typ drzewa, na którym pracujemy
         */
        TreeType typeOfCurrTree;
        /**
         * Informacja, którą oddajemy klientowi po wykonaniu komendy
         */
        public String result;
        /**
         * Drzewo typu integer
         */
        public Tree<Integer> intTree;
        /**
         * Drzewo typu double
         */
        public Tree<Double> dblTree;
        /**
         * Drzewo typu string
         */
        public Tree<String> strTree;
        /**
         * Parametry, które dostaliśmy od klienta
         */
        public String[] parameters;
        /**
         * Wynik, który oddajemy klientowi
         */
        public String getResult = "";
        /**
         * Lista z kreskami
         */
        public List<String> lines = new ArrayList<>();
        /**
         * Lista z kołami
         */
        public List<String> circles = new ArrayList<>();
        /**
         * Lista z wartościami węzłów
         */
        public List<String> texts = new ArrayList<>();
        /**
         * Głębokość drzewa
         */
        public int depth;
        /**
         * Odległość między węzłami ostatniego pozioma drzewa
         */
        public final int xDelta = 90;
        /**
         * Połączenie sieciowe pomiędzy klientem a serwerem
         */
        public Socket socket;

        /**
         * Konstruktor klasy wątku serwera dla klienta
         * @param socket Połączenie sieciowe
         */
        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        /**
         * Kod wykonany przez wątek
         */
        @Override
        public void run() {
            try {
                //Odczyt danych przychodzących z gniazda (odczyt danych wejściowych z klienta)
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //Wysyłanie danych przez gniazdo (wysyłanie danych z serwera do klienta)
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                Message("New client was connected... ");

                String command;
                while ((command = in.readLine()) != null) {
                    result = commandHandler(command);
                    out.println(result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Metoda Message przesyła do txtArea wiadomość
         * @param message Wiadomość
         */
        public void Message(String message) {
            Platform.runLater(() -> txtArea.appendText("\n"+ message));
        }

        /**
         * Metoda integerTree tworzy drzewo typu integer
         * @return Informacja, że drzewo zostało utworzone
         */
        public String integerTree() {
            typeOfCurrTree = TreeType.INTEGER;
            intTree = new Tree<>();
            return "Integer tree was created";
        }
        /**
         * Metoda doubleTree tworzy drzewo typu double
         * @return Informacja, że drzewo zostało utworzone
         */
        public String doubleTree() {
            typeOfCurrTree = TreeType.DOUBLE;
            dblTree = new Tree<>();
            return "Double tree was created";
        }
        /**
         * Metoda stringTree tworzy drzewo typu string
         * @return Informacja, że drzewo zostało utworzone
         */
        public String stringTree() {
            typeOfCurrTree = TreeType.STRING;
            strTree = new Tree<>();
            return "String tree was created";
        }

        /**
         * Metoda searchTree szuka element na drzewie 
         * @param parameters Elementy, których szukamy
         * @return Informacja czy elementy są w drzewie 
         */
        public String searchTree(String[] parameters) {
            getResult = "";
            switch (typeOfCurrTree) {
                case INTEGER: {
                    for (int i = 1; i < parameters.length; i++) {
                        String parameter = parameters[i];
                        try {
                            int prmI = Integer.parseInt(parameter);
                            getResult += prmI + "-->" + intTree.elementSearch(prmI) + "\n";
                        } catch (Exception e) {
                            getResult += "Element " + parameter + " isn't integer" + "\n";
                        }
                    }
                    return getResult;
                }
                case DOUBLE: {
                    double prmD;
                    for (int i = 1; i < parameters.length; i++) {
                        String parameter = parameters[i];
                        try {
                            prmD = Double.parseDouble(parameter);
                            getResult += parameter + "-->" + dblTree.elementSearch(prmD) + "\n";
                        } catch (Exception e) {
                            getResult += "Element" + parameter + "isn't double";
                        }
                    }
                    return getResult;
                }
                case STRING: {
                    for (int i = 1; i < parameters.length; i++) {
                        String parameter = parameters[i];
                        //Sprawdzamy, czy zawiera tylko litery
                        if (parameter.matches("[a-zA-Z]+"))
                            getResult += parameter + "-->" + strTree.elementSearch(parameter) + "\n";
                        else
                            getResult += parameter + "-->" + "isn't contain letters" + "\n";
                    }
                    return getResult;
                }
                default:
                    return "Element's type doesn't match the tree type. Change type of tree";

            }
        }

        /**
         * Metoda insertTree wstawia elementy drzewa
         * @param parameters Elementy, które wstawiamy
         */
        public void insertTree(String[] parameters) {
            switch (typeOfCurrTree) {
                case INTEGER: {
                    for (int i = 1; i < parameters.length; i++) {
                        String parameter = parameters[i];
                        try {
                            int prmI = Integer.parseInt(parameter);
                            intTree.treeInsert(prmI);
                        } catch (Exception e) {
                        }
                    }
                    break;
                }
                case DOUBLE: {
                    for (int i = 1; i < parameters.length; i++) {
                        String parameter = parameters[i];
                        try {
                            double prmD = Double.parseDouble(parameter);
                            dblTree.treeInsert(prmD);
                        } catch (Exception e) {
                        }
                    }
                    break;
                }
                case STRING: {
                    for (int i = 1; i < parameters.length; i++) {
                        String parameter = parameters[i];
                        if (parameter.matches("[a-zA-Z]+"))
                            strTree.treeInsert(parameter);
                    }
                    break;
                }

            }

        }

        /**
         * Metoda deleteTree usuwa elementy z drzewa
         * @param parameters Elementy, które usuwamy
         */
        public void deleteTree(String[] parameters) {
            switch (typeOfCurrTree) {
                case INTEGER: {
                    for (int i = 1; i < parameters.length; i++) {
                        String parameter = parameters[i];
                        try {
                            int prmI = Integer.parseInt(parameter);
                            intTree.treeDelete(prmI);
                        } catch (Exception e) {
                        }
                    }
                    break;
                }
                case DOUBLE: {
                    for (int i = 1; i < parameters.length; i++) {
                        String parameter = parameters[i];
                        try {
                            double prmD = Double.parseDouble(parameter);
                            dblTree.treeDelete(prmD);
                        } catch (Exception e) {
                        }
                    }
                    break;
                }
                case STRING: {
                    for (int i = 1; i < parameters.length; i++) {
                        String parameter = parameters[i];
                        if (parameter.matches("[a-zA-Z]+"))
                            strTree.treeDelete(parameter);
                    }
                    break;
                }

            }

        }

        /**
         * Metoda drawTree drukuje całe drzewo
         * @return Położenie kresek, koł, tekstów na płaszczyznie
         */
        public String drawTree() {

            getResult = "";
            depth = 0;

            switch (typeOfCurrTree) {
                case INTEGER -> {
                   //Głębokość drzewa
                    depth = intTree.treeDepth(intTree.root);
                    //Polowa maksymalnej szerokości ostatniego rzędu 
                    int width = (int) ((xDelta * Math.pow(2, depth - 1)) / 2);
                    coordinatesOfTree(width, 15, 0, 0, true, intTree.root, 1);

                }
                case DOUBLE -> {
                    depth = dblTree.treeDepth(dblTree.root);
                    int width = (int) ((xDelta * Math.pow(2, depth - 1)) / 2);
                    coordinatesOfTree(width, 15, 0, 0, true, dblTree.root, 1);
                }
                case STRING -> {
                    depth = strTree.treeDepth(strTree.root);
                    int width = (int) ((xDelta * Math.pow(2, depth - 1)) / 2);
                    coordinatesOfTree(width, 15, 0, 0, true, strTree.root, 1);
                }
            }
            for (int i = 0; i < lines.size(); i++) {
                getResult += lines.get(i) + " ";
            }
            getResult += "_ ";
            for (int i = 0; i < circles.size(); i++) {
                getResult += circles.get(i) + " ";
            }
            getResult += "_ ";
            for (int i = 0; i < texts.size(); i++) {
                getResult += texts.get(i) + " ";
            }
            lines.clear();
            circles.clear();
            texts.clear();
            return getResult;
        }

        /**
         * Metoda coordinatesOfTree oblicza położenie obiektów
         * @param x1 Początek po x
         * @param y1 Początek po y
         * @param x Koniec po x
         * @param y Koniec po y
         * @param root Sprawdza, czy zaczynamy od korzenia drzewa
         * @param node Węzeł, który w dany moment obsługujemy
         * @param level Poziom w drzewa, na którym się znajdujemy
         */
        public void coordinatesOfTree(int x1, int y1, int x, int y, boolean root, NodeOfTree node, int level) {
            int currXDelta = 0;
            currXDelta = (int) ((xDelta * depth) / Math.pow(2, level + 1));
            String[] line = new String[4];
            if (!root) {
                line[0] = String.valueOf(x1);
                line[1] = String.valueOf(y1);
                line[2] = String.valueOf(x);
                line[3] = String.valueOf(y);
                lines.addAll(Arrays.asList(line));
            }
            String[] circle = new String[2];
            circle[0] = String.valueOf(x1);
            circle[1] = String.valueOf(y1);
            circles.addAll(Arrays.asList(circle));
            String[] text = new String[3];
            text[0] = String.valueOf(x1 - 4);
            text[1] = String.valueOf(y1 + 4);
            text[2] = String.valueOf(node.key);
            texts.addAll(Arrays.asList(text));

            if (node.left != null) {
                coordinatesOfTree(x1 - currXDelta, y1 + 40, x1, y1, false, node.left, level + 1);
            }

            if (node.right != null) {
                coordinatesOfTree(x1 + currXDelta, y1 + 40, x1, y1, false, node.right, level + 1);
            }
        }

        /**
         * Metoda commandHandler obsługuje polecenia klienta
         * @param command Polecenie, które serwer dostał od klienta
         * @return Informacja do klienta
         */
        public String commandHandler(String command) {
            parameters = paramsParser(command);
            if (command.equals("integer")) {
                for (int i = 0; i < parameters.length; i++)
                    parameters[i] = null;
                return integerTree();
            } else if (command.equals("double")) {
                for (int i = 0; i < parameters.length; i++)
                    parameters[i] = null;
                return doubleTree();
            } else if (command.equals("string")) {
                for (int i = 0; i < parameters.length; i++)
                    parameters[i] = null;
                return stringTree();
            } else if (parameters[0].equals("search")) {
                String res = searchTree(parameters);
                for (int i = 0; i < parameters.length; i++)
                    parameters[i] = null;
                return res;
            } else if (parameters[0].equals("insert")) {
                insertTree(parameters);
                for (int i = 0; i < parameters.length; i++)
                    parameters[i] = null;
                return drawTree();
            } else if (parameters[0].equals("delete")) {
                deleteTree(parameters);
                for (int i = 0; i < parameters.length; i++)
                    parameters[i] = null;
                return drawTree();
            } else if (command.equals("draw")) {
                for (int i = 0; i < parameters.length; i++)
                    parameters[i] = null;
                return drawTree();
            } else
                return "Something went wrong";


        }

        /**
         * Typy drzew binarnych
         */
        public enum TreeType {
            INTEGER,
            DOUBLE,
            STRING
        }

    }
}

