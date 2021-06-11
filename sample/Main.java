package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/sample.fxml"));

        Parent root =  loader.load();

        //Now we have access to getController() through the instance... don't forget the type cast
        Controller myController = (Controller)loader.getController();

        primaryStage.setTitle("Trader App");
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                myController.saveData();
                Platform.exit();
                System.exit(0);
            }
        });

    }



    public static void main(String[] args) {

        launch(args);

    }
}
