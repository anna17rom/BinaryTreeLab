
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa aplikacji klienta
 */
public class TreeClientApplication extends Application {
    /**
     * Miejsce dla wydruku drzewa
     */
    private ScrollPane treeArea;
    /**
     * Miejsce dla wydruku informacji o elementach drzewa
     */
    private static TextArea outputArea;
    /**
     * Miejsce dla zapisywania poleceń i elementów
     */
    private TextField commandField;
    /**
     * Wysyłanie danych przez gniazdo
     */
    private PrintWriter out;
    /**
     * Położenie wszystkich elementów na płaszczyźnie
     */
    private List<String> coordinates = new ArrayList<String>();
    /**
     * Typ komendy
     */
    private String commType;
    /**
     * Kontener dla drzewa
     */
    private  Pane newpane = new Pane();
    /**
     * Metoda start wystartuje aplikację JavaFX
     *
     * @param stage Główne okno aplikacji
     * @throws IOException Problem z uruchomieniem
     */
    @Override
    public void start(Stage stage) throws Exception {
        try {
            /**
             * Nowa treeArea
             */
            treeArea = new ScrollPane();
            treeArea.setPrefSize(200,200);
            treeArea.setStyle("-fx-background-color: gray;");
            /**
             * Nowa outputArea
             */
            outputArea = new TextArea();
            outputArea.setPromptText("let's go...");
            commandField = new TextField();

            /**
             * Przecisk dla odszukiwania elementu
             */
            Button searchButton = new Button("Search");
            searchButton.setOnAction(e -> search());
            /**
             * Przecisk dla wstawiania elementu
             */
            Button insertButton = new Button("Insert");
            insertButton.setOnAction(e -> insert());
            /**
             * Przecisk dla usuwania elementu
             */
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e-> delete());
            /**
             * Przecisk dla wydruku drzewa
             */
            Button drawButton = new Button("Draw");
            drawButton.setOnAction(e -> draw());
            /**
             * Przecisk dla wysyłania poleceń na serwer
             */
            Button okButton = new Button("Okay");
            okButton.setOnAction(e -> sendCommand());
            /**
             * Przecisk dla oczyszczenia treeArea,outputArea i commandField
             */
            Button removeButton = new Button("Remove");
            removeButton.setOnAction(e-> remove());
            /**
             * Pasek menu
             */
            MenuBar types = new MenuBar();
            /**
             * Menu dla wybierania typu drzewa
             */
            Menu type = new Menu("Types");
            /**
             * Typ integer
             */
            MenuItem typeInteger = new MenuItem("Integer");
            typeInteger.setOnAction(e -> typeInteger());
            /**
             * Typ double
             */
            MenuItem typeDouble = new MenuItem("Double");
            typeDouble.setOnAction(e -> typeDouble());
            /**
             * Typ string
             */
            MenuItem typeString = new MenuItem("String");
            typeString.setOnAction(e -> typeString());

            type.getItems().addAll(typeInteger, typeDouble, typeString);
            types.getMenus().addAll(type);

            /**
             * Główny kontener
             */
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(new VBox(10, treeArea, outputArea, commandField));
            borderPane.setRight(new VBox(10.0, searchButton, insertButton, deleteButton, drawButton, okButton,removeButton, types));
            borderPane.setPadding(new Insets(10));

            Scene scene = new Scene(borderPane, 400, 400);
            stage.setTitle("Client");
            stage.setScene(scene);
            stage.show();


            connection("localhost", 1234);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Metoda connection dołącza klienta do serwera
     * @param host Adres IP
     * @param port Numer portu, na którym serwer łączy się serwer i klient 
     */
    public void connection(String host, int port) {
        try {
        //Sieciowe połączenie
            Socket socket = new Socket(host, port);
            //Wysyłanie danych przez gniazdo (wysyłanie danych z klienta do serwera)
            out = new PrintWriter(socket.getOutputStream(), true);
            //Odczyt danych przychodzących z gniazda (odczyt danych wejściowych z serwera)
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //Wątek główny klienta
            new Thread(() -> {
                try {
                    String resFromSerwer;
                    while ((resFromSerwer = in.readLine()) != null) {
                        display(resFromSerwer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();


        } catch (IOException ex) {
            outputArea.appendText("I/O error: " + ex.getMessage());
        }

    }

    /**
     * Metoda display wyświetla informację, którą dostaliśmy z serwera zgodnie z komendą
     * @param resFromSerwer Wynik przetwarzania informacji na serwerze
     */
    private void display(String resFromSerwer) {

        if (commType.equals("draw") || commType.equals("insert") || commType.equals("delete")) {
            Platform.runLater(() -> {
                newpane.getChildren().clear();
                coordinates = List.of(paramsParser(resFromSerwer));
                drawOnPanel();});
        } else
            Platform.runLater(() -> {
                outputArea.appendText(resFromSerwer + "\n");});
    }

    /**
     * Metoda sendCommand wysyła komendę do serwera, zapisuje dane z klienta w strumień wyjściowy
     */
    private void sendCommand() {
        String command = commandField.getText();
        if (!command.isEmpty()) {
            out.println(command);
        }
    }

    /**
     * Metoda typeInteger informuje, że wybrany został typ integer
     */
    private void typeInteger() {
        outputArea.clear();
        commandField.setText("integer");
        commType = "integer";
    }

    /**
     * Metoda typeDouble informuje, że wybrany został typ double
     */
    private void typeDouble() {
        outputArea.clear();
        commandField.setText("double");
        commType = "double";

    }

    /**
     * Metoda typeString informuje, że wybrany został typ string
     */
    private void typeString() {
        outputArea.clear();
        commandField.setText("string");
        commType = "string";

    }

    /**
     * Metoda search informuje, że wybrana została komenda odszukiwaniu elementu
     */
    private void search() {
        outputArea.clear();
        commandField.setText("search");
        commType = "search";
    }
    /**
     * Metoda insert informuje, że wybrana została komenda wstawiania elementu
     */
    private void insert() {
        outputArea.clear();
        commandField.setText("insert");
        commType = "insert";
    }
    /**
     * Metoda draw informuje, że wybrana została komenda rysowania drezwa
     */
    private void draw() {
        outputArea.clear();
        commandField.setText("draw");
        commType = "draw";
    }
    /**
     * Metoda delete informuje, że wybrana została komenda usuwania elementu
     */
    private void delete(){
        outputArea.clear();
        commandField.setText("delete");
        commType = "delete";
    }
    /**
     * Metoda remove oczyszcza klienta
     */
    private  void remove(){
        newpane.getChildren().clear();
        outputArea.clear();
        commandField.clear();
    }

    /**
     * Metoda drawOnPanel wczytuje współrzędne, które były wysłane z serwera i tworzy odpowiednie elementy na newpane
     */
    private void drawOnPanel() {
        int i = 0;
        while (!coordinates.get(i).equals("_")) {
            int param1 = Integer.parseInt(coordinates.get(i));
            int param2 = Integer.parseInt(coordinates.get(i+1));
            int param3 = Integer.parseInt(coordinates.get(i+2));
            int param4 = Integer.parseInt(coordinates.get(i+3));
            Line line = new Line(param1, param2, param3, param4);
            i += 4;
            newpane.getChildren().addAll(line);
        }
        i++;
        while (!coordinates.get(i).equals("_")) {
            int param1 = Integer.parseInt(coordinates.get(i));
            int param2 = Integer.parseInt(coordinates.get(i+1));
            Circle circle = new Circle(param1, param2, 15, Color.GOLD);
            i += 2;
            newpane.getChildren().addAll(circle);
        }
        i++;
        int j=0;
        while (i<coordinates.size()) {
            int param1 = Integer.parseInt(coordinates.get(i));
            int param2 = Integer.parseInt(coordinates.get(i+1));
            String param3 = coordinates.get(i+2);
            Text text = new Text(param1, param2, param3);
            text.setStyle("-fx-font-size: 8px");
            i += 3;
            newpane.getChildren().addAll(text);
            j++;

        }
        treeArea.setContent(newpane);

    }

    /**
     * Podzielenie linii ze współrzędnymi dostanymi od serwera
     * @param coordinates Linii ze współrzędnymi
     * @return Lista ze współrzędnymi
     */
    public static String[] paramsParser(String coordinates) {
        String[] params = coordinates.split(" ");
        return params;
    }

    /**
     * Metoda main to metoda główna
     * @param args argumenty, które były przekazane do programu
     */
    public static void main(String[] args) {
        launch(args);
    }
}