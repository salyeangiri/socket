package socketprogramming;
//@author Salyean Giri
//11602099

import java.io.*;
import java.net.Socket;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Client extends Application {

    private final TextArea msgListingBox = new TextArea();
    private final TextArea msgBox_Typing = new TextArea();

    private Socket socket_Client;
    OutputStream outputStream;
    PrintWriter printWriter;
    InputStream inputStream;
    BufferedReader bufferReader;

    @Override
    public void start(Stage mainStage) {

        msgListingBox.setWrapText(true);
        msgListingBox.setFont(new Font(STYLESHEET_MODENA, 15));
        msgListingBox.setEditable(false);

        HBox hbox = new HBox(msgListingBox);
        hbox.setPadding(new Insets(10, 5, 5, 22));
        msgListingBox.setMaxSize(440, 350);

        msgBox_Typing.setWrapText(true);
        msgBox_Typing.setFont(new Font(STYLESHEET_MODENA, 15));
        msgBox_Typing.setEditable(true);
        msgBox_Typing.setMaxSize(350, 70);

        Button sendBtn = new Button("Send");
        sendBtn.setMinSize(90, 70);
        sendBtn.setOnAction((ActionEvent e) -> {
            //Action executed
            if (!msgBox_Typing.getText().trim().isEmpty()) {
                //writing message
                printWriter.println(msgBox_Typing.getText().trim());
                msgListingBox.appendText("From Client: " + msgBox_Typing.getText().trim() + "\n");
                msgBox_Typing.setText("");
            }

        });

        Thread chatThread_client = new Thread(() -> {
            ////starting daemon thread to prevent freezing of GUI         
            serverConnection();
        });
        chatThread_client.setDaemon(true);
        chatThread_client.start();

        GridPane grid_Pane = new GridPane();
        grid_Pane.setHgap(15);
        grid_Pane.setPadding(new Insets(10, 5, 10, 8));
        grid_Pane.add(msgBox_Typing, 1, 1);
        grid_Pane.add(sendBtn, 2, 1);

        VBox mainLayout = new VBox(hbox, grid_Pane);

        Scene scene = new Scene(mainLayout);

        mainStage.setTitle("Client");
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }

    //opening port in Client for communication to start
    private void serverConnection() {
        try {

            //establishing communication with given address and port
            //port should be in the range of 1024-65535
            socket_Client = new Socket("127.0.0.1", 1900);
            msgListingBox.appendText("Connection established with server. Start Chatting now!\n");

            //reading and writing
            //initailization of IO instances
            outputStream = socket_Client.getOutputStream();
            printWriter = new PrintWriter(outputStream, true);
            inputStream = socket_Client.getInputStream();
            bufferReader = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                msgListingBox.appendText("From Server: " + bufferReader.readLine() + "\n");
            }
        } catch (IOException ex) {
            System.out.println("Exception: " + ex.getLocalizedMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
