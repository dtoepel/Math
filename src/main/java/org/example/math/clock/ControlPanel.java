package org.example.math.clock;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class ControlPanel extends VBox {
    private final WorldPanelFX worldPanel;
    private final TextField dateField = new TextField();
    private final Label speedLabel = new Label("Speed xxxxxxxx");
    private final Slider slider = new Slider(0,10,0);

    public ControlPanel(WorldPanelFX worldPanel) {
        super();
        this.worldPanel = worldPanel;
        this.worldPanel.setControlPanel(this);

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.add(new Label("Datum"), 0,0);
        dateField.setMinWidth(100);
        dateField.setEditable(false);
        grid.add(dateField, 1,0);
        Button b1 = new Button("-10 Tage");
        Button b2 = new Button("-1 Stunde");
        Button b3 = new Button("+1 Stunde");
        Button b4 = new Button("+10 Tage");
        CheckBox flatEarthBox = new CheckBox("Flat Earth Mode");
        CheckBox flatSouth = new CheckBox("Flat South Centered");
        CheckBox showSubSolar = new CheckBox("Show Subsolar Point");
        CheckBox showTwilight = new CheckBox("Show Twilight");
        //flatEarthBox.setSelected(false);

        grid.add(b1,2,0);
        grid.add(b2,2,1);
        grid.add(b3,2,2);
        grid.add(b4,2,3);

        grid.add(showSubSolar,3,0);
        grid.add(showTwilight,3,1);
        grid.add(flatEarthBox,3,2);
        grid.add(flatSouth,3,3);

        grid.add(speedLabel, 4,0);
        grid.add(slider, 4,1);

        Button exit = new Button("EXIT");
        grid.add(exit,4,3);
        exit.setOnAction(evt -> {System.exit(303);});

        slider.valueProperty().addListener(n -> {
            worldPanel.setSpeed(slider.getValue());
            updateFields();
        });

        b1.setOnAction(e -> {worldPanel.addTime(-3600_000*240);});
        b2.setOnAction(e -> {worldPanel.addTime(-3600_000);});
        b3.setOnAction(e -> {worldPanel.addTime(3600_000);});
        b4.setOnAction(e -> {worldPanel.addTime(3600_000*240);});

        EventHandler<ActionEvent> e = evt -> {
            worldPanel.setMode(
                    showSubSolar.isSelected(),
                    showTwilight.isSelected(),
                    flatEarthBox.isSelected(),
                    flatSouth.isSelected());
        };
        showTwilight.setOnAction(e);
        showSubSolar.setOnAction(e);
        flatEarthBox.setOnAction(e);
        flatSouth.setOnAction(e);

        getChildren().add(grid);
        getChildren().add(new Label("based on https://visibleearth.nasa.gov/images/57752/blue-marble-land-surface-shallow-water-and-shaded-topography"));
        getChildren().add(new Label("and https://viirsland.gsfc.nasa.gov/Products/NASA/BlackMarble.html"));

        updateFields();
    }

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MMM-yyy HH:mm:ss");
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public void updateFields() {
        dateField.setText(dateFormat.format(worldPanel.getTime()));

        speedLabel.setText("Speed " + decimalFormat.format(Math.pow(2,slider.getValue())) + "x");
    }

}
