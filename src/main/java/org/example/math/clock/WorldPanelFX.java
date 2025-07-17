package org.example.math.clock;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;

import javafx.scene.paint.Color;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class WorldPanelFX extends Pane {
    private Image day = null;
    private Image night = null;
    private long t = System.currentTimeMillis();
    private final int yMax = 512;
    private final int xMax = yMax * 2;
    private final Canvas canvas;
    private boolean flatEarthMode = false;
    private boolean flatSouth = true;
    private boolean showSubSolar;
    private boolean showTwilight;
    private double speed = 0.0;
    private ControlPanel controlPanel;
    private final int ANIM_SPEED = 500;
    private long lastRedraw = System.currentTimeMillis();
    private boolean busyPainting = false;

    public WorldPanelFX() {
        try {
            day = new Image(new File(WorldPanel.class.getResource("/images/day1024.jpg").getFile()).toURI().toString());
            night = new Image(new File(WorldPanel.class.getResource("/images/night1024.jpg").getFile()).toURI().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setOnScroll(e -> {
            t += (long) (1000 * (e.isControlDown() ? 1 : 24) * 60 * 60 * Math.signum(e.getDeltaY()));
            WorldPanelFX.this.redraw();
            if(controlPanel != null) controlPanel.updateFields();
        });

        canvas = new Canvas();
        getChildren().add(canvas);
		canvas.widthProperty().bind(this.widthProperty());
		canvas.heightProperty().bind(this.heightProperty());

        this.setMinHeight(yMax);
        this.setMinWidth(xMax);

        Platform.runLater(() -> {redraw();});

        setOnMouseMoved(e->{mouseMoved();});
    }

    public void mouseMoved() {
        if(busyPainting) return;
        busyPainting = true;

        long now = System.currentTimeMillis();
        long diff = now - lastRedraw;
        lastRedraw = now;
        System.err.println("diff: " + diff);
        t += (int) (Math.pow(2,speed) * diff);
        redraw();
        if(controlPanel != null) controlPanel.updateFields();

    }

    public void redraw() {
        final GraphicsContext g = canvas.getGraphicsContext2D();
        final PixelWriter pixelWriter = g.getPixelWriter();
        final PixelReader pixelReaderDay = day.getPixelReader();
        final PixelReader pixelReaderNight = night.getPixelReader();

        double timeSinceMidnight = (t % (60 * 60 * 24 * 1000)) / (60. * 60 * 24 * 1000);
        double timeSinceWinterSolstice = getTimeSinceWinterSolstice(t);

        if(flatEarthMode) {
            for(int y = 0; y < yMax; y++) for(int x = 0; x < xMax; x++) {
                /*
                yMax/2 und xMax/2 sind Ursprung,
                radius ist yMax/2, weil kleiner als xMax;
                */
                double radius =  yMax/2.;
                double xFlat = (x - xMax/2.) / radius;
                double yFlat = - (y - yMax/2.) / radius;

                if(xFlat*xFlat + yFlat*yFlat < 1) {
                    double flatLat = // nord = 1, süd = -1;
                        1-Math.sqrt(Math.pow(xFlat, 2) + Math.pow(yFlat, 2))*2;
                    double flatLon = // 0° = 0 bzw 2PI
                        Math.atan2(yFlat, xFlat) + 3*Math.PI/2 + .2;
                    if(flatLon < 0) flatLon += Math.PI*2;
                    if(flatLon >= Math.PI*2) flatLon -= Math.PI*2;
                    double xTranslated = (flatLon / (2*Math.PI) * xMax) % xMax;
                    double yTranslated = -(flatLat-1)/2 * yMax ;

                    if(flatSouth) {
                        xTranslated = xMax - xTranslated - 1;
                        yTranslated = yMax - yTranslated - 1;
                    }

                    /*if(xTranslated >= xMax || yTranslated >= yMax || xTranslated < 0 || yTranslated < 0) {
                        System.err.println(flatLat + "/" + flatLon);
                        System.err.println((int)xTranslated +"/"+ (int)yTranslated);
                    } else {*/
                    drawPixel((int) xTranslated, (int) yTranslated,
                            x, y,
                            pixelReaderDay, pixelReaderNight, pixelWriter,
                            timeSinceWinterSolstice, timeSinceMidnight);
                    //}
                } else {
                    pixelWriter.setColor(x,y,Color.BLACK);
                }
            }
        } else {
            for(int y = 0; y < yMax; y++) for(int x = 0; x < xMax; x++) {
                drawPixel(x,y,x,y,
                        pixelReaderDay,pixelReaderNight,pixelWriter,
                        timeSinceWinterSolstice, timeSinceMidnight);
            }
        }
        busyPainting = false;
    }

    private void drawPixel(int xs, int ys, int xt, int yt, PixelReader pixelReaderDay, PixelReader pixelReaderNight, PixelWriter pixelWriter, double timeSinceWinterSolstice, double timeSinceMidnight) {
        double daylight = getDaylight(
                1. * xs / xMax * 2 * Math.PI - Math.PI,
                (Math.PI - 1. * ys / yMax * 2 * Math.PI) / 2,
                timeSinceWinterSolstice,
                timeSinceMidnight);

        Color dayColor = pixelReaderDay.getColor(xs, ys);
        Color nightColor = pixelReaderNight.getColor(xs, ys);
        Color paintColor =    daylight > 0 ? dayColor :
        /* weniger als -18°:*/(daylight < -Math.PI / 10 || !showTwilight)? nightColor:
                              getTwilight(-daylight / (Math.PI / 10), dayColor, nightColor);
        if(showSubSolar) {
            if(daylight > 88.5 * Math.PI/180.) paintColor = Color.BLACK;
            if(daylight > 89. * Math.PI/180.) paintColor = Color.ORANGE;
        }
        pixelWriter.setColor(xt, yt, paintColor);
    }

    private Color getTwilight(double night, Color dayColor, Color nightColor) {
        night = 1 - (1-night)*(1-night)*(1-night);
        return new Color(
                dayColor.getRed()*(1-night)+nightColor.getRed()*night,
                dayColor.getGreen()*(1-night)+nightColor.getGreen()*night,
                dayColor.getBlue()*(1-night)+nightColor.getBlue()*night,
                1);
    }

    private double getTimeSinceWinterSolstice(long t2) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date(t));
        cal.set(cal.get(Calendar.YEAR), Calendar.DECEMBER, 21, 12, 0, 0);
        long lastEquinox = cal.getTimeInMillis();
        if(lastEquinox > t) {
            cal.set(cal.get(Calendar.YEAR)-1, Calendar.DECEMBER, 21, 12, 0, 0);
            lastEquinox = cal.getTimeInMillis();
        }
        cal.set(cal.get(Calendar.YEAR)+1, Calendar.DECEMBER, 21, 12, 0, 0);
        long nextEquinox = cal.getTimeInMillis();
        long y = nextEquinox - lastEquinox;
        long a = t - lastEquinox;
        return (2. * Math.PI * a) / y;
    }

    private double getDaylight(double lon, double lat, double timeSinceWinterSolstice, double timeSinceMidnight) {
        double K = Math.PI/180;
        double deklination = -23.45*Math.cos(timeSinceWinterSolstice)*K;
        double stundenwinkel = 15*(timeSinceMidnight*24 - (15.0-lon/K)/15.0 - 12)*K;
        double x = Math.sin(lat)*Math.sin(deklination) + Math.cos(lat)*Math.cos(deklination)*Math.cos(stundenwinkel);
        return Math.asin(x);
    }

    public ZonedDateTime getTime() {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(t), ZoneId.systemDefault());
    }

    public void setControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    public void addTime(long t) {
        this.t += t;
        redraw();
        if(controlPanel != null) {controlPanel.updateFields();}
    }

    public void setMode(
            boolean showSubSolar,
            boolean showTwilight,
            boolean flatEarthMode, 
            boolean flatSouth) {
        this.showSubSolar = showSubSolar;
        this.showTwilight = showTwilight;
        this.flatEarthMode = flatEarthMode;
        this.flatSouth = flatSouth;
        redraw();
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
