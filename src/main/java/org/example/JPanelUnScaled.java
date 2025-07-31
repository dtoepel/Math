package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class JPanelUnScaled extends JPanel{
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new JPanelUnScaled());
        frame.pack();
        frame.setSize(800,600);
        frame.setLocation(500,200);
        frame.setVisible(true);
    }

    private Point2D p = new Point2D.Double(0,0);
    private transient AffineTransform screenScale = new AffineTransform();

    public JPanelUnScaled() {
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                p = transformMouse(e);
                repaint();
            }
        });
    }

    protected final Point2D transformMouse(MouseEvent e) {
        return screenScale.transform(new Point2D.Double(e.getX(),e.getY()), null);
    }

    protected final Point2D transformPoint(Point2D e) {
        return screenScale.transform(new Point2D.Double(e.getX(),e.getY()), null);
    }

    protected final int getCanvasWidth() {
        return (int)(screenScale.getScaleX() * getWidth());
    }

    protected final int getCanvasHeight() {
        return (int)(screenScale.getScaleY() * getHeight());
    }

    @Override
    public void paint(Graphics g1) {
        super.paint(g1);
        Graphics2D g = (Graphics2D) g1;
        screenScale = g.getTransform();
        g.setTransform(new AffineTransform());
        //g.setColor(Color.BLACK);
        //g.drawLine(0,0, (int)p.getX(), (int)p.getY());
        //g.drawLine(0, getCanvasHeight(), (int)p.getX(), (int)p.getY());
        //g.drawLine(getCanvasWidth(),0, (int)p.getX(), (int)p.getY());
        //g.drawLine(getCanvasWidth(), getCanvasHeight(), (int)p.getX(), (int)p.getY());
    }
}
