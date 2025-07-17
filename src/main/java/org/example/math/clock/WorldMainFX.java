package org.example.math.clock;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WorldMainFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        //btn.setOnAction(new EventHandler<ActionEvent>() {

        //    @Override
        //    public void handle(ActionEvent event) {
        //        System.out.println("Hello World!");
        //    }
        //});

        VBox bottomPane = new VBox();
        WorldPanelFX clockPanel = new WorldPanelFX();
        ControlPanel controlPanel = new ControlPanel(clockPanel);

        VBox root = new VBox(clockPanel, controlPanel);


        primaryStage.setScene(new Scene(root, 1030, 750));
        primaryStage.setWidth(1030);
        primaryStage.setHeight(750);
        primaryStage.show();
    }
}