package socketprogramming;

// @author Salyean Giri
//11602099
import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Server extends Application {

    private final TextArea msgListingBox = new TextArea();
    private final TextArea msgBox_Typing = new TextArea();

    private ServerSocket socket_Server;
    private Socket socket;
    OutputStream outputStream;
    PrintWriter printWriter;
    InputStream inputStream;
    BufferedReader bufferReader;

    @Override
    public void start(Stage mainStage) {

        msgListingBox.setWrapText(true);
        msgListingBox.setFont(new Font(STYLESHEET_MODENA, 15));
        msgListingBox.setEditable(false);

        HBox horizontalBox = new HBox(msgListingBox);
        horizontalBox.setPadding(new Insets(10, 5, 5, 22));
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
                msgListingBox.appendText("From Server: " + msgBox_Typing.getText().trim() + "\n");
                msgBox_Typing.setText("");
            }
        });

        Thread chatThread_Server = new Thread(() -> {
            //starting daemon thread to prevent freezing of GUI
            openPortForComunncation();
        });
        chatThread_Server.setDaemon(true);
        chatThread_Server.start();

        GridPane grid_Pane = new GridPane();
        grid_Pane.setHgap(15);
        grid_Pane.setPadding(new Insets(10, 5, 10, 8));
        grid_Pane.add(msgBox_Typing, 1, 1);
        grid_Pane.add(sendBtn, 2, 1);

        VBox outLook = new VBox(horizontalBox, grid_Pane);

        Scene scene = new Scene(outLook);

        mainStage.setTitle("Server");
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();

    }

    public static void main(String[] args) {
        launch(args);

    }

    //opening port in server for communication to start
    private void openPortForComunncation() {
        try {
            socket_Server = new ServerSocket(1900);
            msgListingBox.appendText("Waiting.... for connection to be established by Client!\n");
            socket = socket_Server.accept();
            msgListingBox.appendText("Connection established,Start Chatting now!\n");

            outputStream = socket.getOutputStream();
            printWriter = new PrintWriter(outputStream, true);
            inputStream = socket.getInputStream();
            bufferReader = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                msgListingBox.appendText("From Client: " + bufferReader.readLine() + "\n");
            }

        } catch (IOException ex) {
            System.out.println("Exception: " + ex.getLocalizedMessage());
        }
    }
}
