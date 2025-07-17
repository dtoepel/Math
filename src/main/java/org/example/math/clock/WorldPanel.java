package org.example.math.clock;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class WorldPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        WorldPanel w = new WorldPanel();
        JScrollPane s = new JScrollPane(w);
        frame.setContentPane(s);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 600);
        frame.setLocation(100, 100);

        frame.setVisible(true);
    }

    private BufferedImage day = null;
    private BufferedImage night = null;
    private long t = System.currentTimeMillis();
    private boolean var1 = false;
    private int yMax = var1?72:512;
    private int xMax = yMax * 2;

    public WorldPanel() {
        try {
            day = ImageIO.read(new File(WorldPanel.class.getResource("/images/day1024-2.jpg").getFile()));
            night = ImageIO.read(new File(WorldPanel.class.getResource("/images/night1024-2.jpg").getFile()));

            //day = ImageIO.read(new File(var1?"src/day144.png":"src/day1024-2.jpg"));
            //night = ImageIO.read(new File(var1?"src/night144.png":"src/night1024.jpg"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                t += 1000 * (e.isControlDown()?1:24) * 60 * 60 * e.getWheelRotation();
                repaint();
            }
        });
    }

    public void paint(Graphics g) {

//        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
//		long t = System.currentTimeMillis();// + 1000l * 60 * 60 * 24 * (55 + 0);//+3*60*60*24*30*1000;
        double timeSinceMidnight = (t % (60 * 60 * 24 * 1000)) / (60. * 60 * 24 * 1000);
//		double timeSinceEquinox = getTimeSinceEqinox(t);
        double timeSinceWinterSolstice = getTimeSinceWinterSolstice(t);
        System.err.println("timeSinceWinterSolstice: " + timeSinceWinterSolstice);
        System.err.println("timeSinceMidnight: " + timeSinceMidnight);
        for(int y = 0; y < yMax; y++) {for(int x = 0; x < xMax; x++) {
            double daylight = getDaylight(1.*x/xMax*2*Math.PI-Math.PI, (Math.PI - 1.*y/yMax*2*Math.PI)/2, timeSinceWinterSolstice, timeSinceMidnight);
            BufferedImage img = daylight > 0*Math.PI / 2 ? day : night;

            int color = img.getRGB(x, y);
            Color c = new Color(color);
//				Color c = Color.getHSBColor(1.f, 0.f, (float)daylight);
            g.setColor(c);
            g.drawLine(x, y, x, y);
        }
        }
    }

    private double getTimeSinceWinterSolstice(long t2) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date(t));
        System.out.println(new SimpleDateFormat().format(cal.getTime()));
        cal.set(cal.get(Calendar.YEAR), Calendar.DECEMBER, 21, 12, 0, 0);
        long lastEquinox = cal.getTimeInMillis();
        if(lastEquinox > t) {
            cal.set(cal.get(Calendar.YEAR)-1, Calendar.DECEMBER, 21, 12, 0, 0);
            lastEquinox = cal.getTimeInMillis();
        }
//		System.out.println(new SimpleDateFormat().format(cal.getTime()));
        cal.set(cal.get(Calendar.YEAR)+1, Calendar.DECEMBER, 21, 12, 0, 0);
//		System.out.println(new SimpleDateFormat().format(cal.getTime()));
        long nextEquinox = cal.getTimeInMillis();
        long y = nextEquinox - lastEquinox;
        long a = t - lastEquinox;
//		System.out.println((1.*a/y));
        return (2. * Math.PI * a) / y;
    }

    private double getTimeSinceEqinox(long t) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date(t));
        System.out.println(new SimpleDateFormat().format(cal.getTime()));
        cal.set(cal.get(Calendar.YEAR), Calendar.MARCH, 21, 12, 0, 0);
        long lastEquinox = cal.getTimeInMillis();
        if(lastEquinox > t) {
            cal.set(cal.get(Calendar.YEAR)-1, Calendar.MARCH, 21, 12, 0, 0);
            lastEquinox = cal.getTimeInMillis();
        }
//		System.out.println(new SimpleDateFormat().format(cal.getTime()));
        cal.set(cal.get(Calendar.YEAR)+1, Calendar.MARCH, 21, 12, 0, 0);
//		System.out.println(new SimpleDateFormat().format(cal.getTime()));
        long nextEquinox = cal.getTimeInMillis();
        long y = nextEquinox - lastEquinox;
        long a = t - lastEquinox;
        System.out.println((1.*a/y));
        return (2. * Math.PI * a) / y;
    }

    private double getDaylight2(double lon, double lat, double timeSinceEquinox, double timeSinceMidnight) {
        double tilt = - 23.5/180.*Math.PI * Math.sin(timeSinceEquinox);
        System.out.println("tilt = " + tilt / Math.PI * 180);
        System.out.println("lat = " + lat / Math.PI * 180);
//		double midNight = (lat < 0?-1:1)*Math.PI - lat + tilt;
        double midNight =  - lat + tilt;
        double midDay = lat + tilt;
//		midNight = (midNight+Math.PI) % (2*Math.PI) - Math.PI;
//		midDay   = (midDay+  Math.PI) % (2*Math.PI) - Math.PI;
        midNight = midNight % (2*Math.PI);
        while(midNight < -Math.PI) midNight += 2*Math.PI;
        midDay = midDay % (2*Math.PI);
        while(midDay < -Math.PI) midDay += 2*Math.PI;
        System.out.println("midNight = " + midNight / Math.PI * 180);
        System.out.println("midDay   = " + midDay / Math.PI * 180);
//		System.out.println("timeSinceMidnight   = " + timeSinceMidnight);
        double localTime = timeSinceMidnight * 2 * Math.PI + lon;
        double a = -Math.cos(localTime);
//		System.out.println("a   = " + a);
//		double avg = midNight + midDay/2;
//		double diff = midDay-midNight)
        double b = (midNight + midDay + (midDay-midNight) * a) /2;


//		long n_ = new Date(100, 0, 1).getTime();
//		double n = (System.currentTimeMillis() - n_) / 1000. / 60. / 60. / 24.;
//		double L = Math.PI / 180. * (280.46 + n * 0.9856474);
//		double g = Math.PI / 180. * (357.528 + n * 0.9856003);
//		double Λ = L + (1.915*Math.PI/180.) * Math.sin(g) + (0.01997*Math.PI/180.) * Math.sin(2*g);
//		double ε = Math.PI / 180. * (23.439 - 0.0000004 * n);
        return Math.abs(b);
    }

    private double getDaylight(double lon, double lat, double timeSinceWinterSolstice, double timeSinceMidnight) {
        double K = Math.PI/180;
//		double timeSinceWinterSolstice = timeSinceEquinox
        double deklination = -23.45*Math.cos(timeSinceWinterSolstice)*K;
//		System.out.println("tilt = " + deklination / K);
//		System.out.println("lat = " + lat / K);
//		System.out.println("lon = " + lon / K);
//        System.out.println("timeSinceMidnight = " + timeSinceMidnight);
        double stundenwinkel = 15*(timeSinceMidnight*24 - (15.0-lon/K)/15.0 - 12)*K;
//        System.out.println("stundenwinkel = " + stundenwinkel / K);

        double x = Math.sin(lat)*Math.sin(deklination) + Math.cos(lat)*Math.cos(deklination)*Math.cos(stundenwinkel);
//	    double y = -(Math.sin(lat)*x - Math.sin(deklination)) / (Math.cos(lat)*Math.sin(Math.acos(x)));
        double hoehe = Math.asin(x);
//        double azimut = Math.acos(y);
//        System.out.println("hoehe = " + hoehe / K);
        return hoehe;

    }
}

