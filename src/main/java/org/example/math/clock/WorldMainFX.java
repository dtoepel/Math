package org.example.math.clock;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WorldMainFX extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");

        WorldPanelFX clockPanel = new WorldPanelFX();
        ControlPanel controlPanel = new ControlPanel(clockPanel);

        VBox root = new VBox(clockPanel, controlPanel);

        primaryStage.setScene(new Scene(root, 1030, 750));
        primaryStage.setWidth(1030);
        primaryStage.setHeight(750);
        primaryStage.show();
    }
}