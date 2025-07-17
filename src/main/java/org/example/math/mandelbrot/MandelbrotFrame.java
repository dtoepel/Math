package org.example.math.mandelbrot;

import org.example.JPanelUnScaled;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MandelbrotFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final static int GAP = 5;
    private final static int H = 369;
    private final static int W = H*3;//1171;

    private double minX = -4.;
    private double maxX = 2;
    private double minY = -1.;
    private double maxY = 1.;
    private int currentX = -1;
    private int currentY = -1;

    public static void main(String[] args) {
        MandelbrotFrame f = new MandelbrotFrame();

//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        f.pack();
        f.setLocation(150, 50);
        f.setSize(1150, 800);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private UpperPanel o;
    private LowerPanel u;

    public MandelbrotFrame() {
        super();

        JPanel p = new JPanel();

        o = new UpperPanel();

        u = new LowerPanel();

        GroupLayout layout = new GroupLayout(p);
        p.setLayout(layout);

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(GAP)
                .addComponent(o,H,H,H)
                .addGap(GAP)
                .addComponent(u)
                .addGap(GAP));

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(GAP)
                .addGroup(layout.createParallelGroup(Alignment.CENTER)
                        .addComponent(o,W,W,W)
                        .addComponent(u))
                .addGap(GAP));

        layout.linkSize(o,u);
        o.setBackground(Color.BLACK);
        u.setBackground(Color.BLUE);
        this.setContentPane(p);
    }

    private void repaintLower() {
        u.repaint();
    }

    final int LIMIT = 1000;
    final int DARK_LIMIT = 20;

    private Color getColor(int iterations) {
        if(iterations >= LIMIT) return Color.BLACK;
        double bri = iterations > DARK_LIMIT ? 1. : (1. / DARK_LIMIT * iterations);
        double log = Math.log(iterations);
        double maxLog = Math.log(LIMIT);
        double hue = 10*log / maxLog;
        return Color.getHSBColor((float)hue, 1.f, (float)bri);
    }

    private int getIterations(double reX, double imX, double reC, double imC) {
        int result = 0;
        while(reX*reX+imX*imX<4 && result <= LIMIT) {
            result++;
            double reX1 = reC + reX * reX - imX * imX;
            double imX1 = imC + reX * imX * 2;
            reX = reX1;
            imX = imX1;
        }

        return result;
    }

    private class UpperPanel extends JPanelUnScaled {
        private static final long serialVersionUID = 1L;

        public UpperPanel() {

            addMouseMotionListener(new MouseAdapter() {

                @Override
                public void mouseMoved(MouseEvent e) {
                    super.mouseMoved(e);
                    Point2D p = transformMouse(e);
                    currentX = (int)p.getX();
                    currentY = (int)p.getY();
                    repaint();
                    repaintLower();
                }

            });

            addMouseWheelListener(new MouseWheelListener() {

                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    double factor = e.getWheelRotation()>0?2:.5;

                    double mouseX = e.getX()*(maxX-minX)/W+minX;
                    double mouseY = e.getY()*(maxY-minY)/H+minY;

                    minX = mouseX - (mouseX-minX)*factor;
                    maxX = mouseX + (maxX-mouseX)*factor;
                    minY = mouseY - (mouseY-minY)*factor;
                    maxY = mouseY + (maxY-mouseY)*factor;
                    repaint();
                }
            });
        }

        private double getX(int i) { return (maxX - minX) * i / getCanvasWidth() + minX; }
        private double getY(int i) { return (maxY - minY) * i / getCanvasHeight() + minY; }

        public void paint(Graphics g) {
            super.paint(g);
            int WW = getCanvasWidth();
            int HH = getCanvasHeight();
            BufferedImage buf = new BufferedImage(WW, HH, BufferedImage.TYPE_INT_ARGB);

            Graphics2D G = (Graphics2D) (buf.getGraphics());

            G.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            G.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            G.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            for(int i = 0; i < WW; i++) {
                double x = getX(i);
                for(int j = 0; j < HH; j++) {
                    double y = getY(j);

                    int iterations = getIterations(0,0,x,y);
                    G.setColor(getColor(iterations));
                    G.drawRect(i, j, 1, 1);
                }
            }

            G.setColor(Color.WHITE);
            G.drawLine(currentX, 0, currentX, HH);
            G.drawLine(0, currentY, WW, currentY);

            G.drawString("re=" + getX(currentX), currentX+10, currentY-13);
            G.drawString("im=" + getY(currentY), currentX+10, currentY);

            buf.flush();

            g.drawImage(buf, 0, 0, this);
        }
    }

    private class LowerPanel extends JPanelUnScaled {
        private static final long serialVersionUID = 1L;

        private double getRe(int x) {
            return (maxX - minX) * x / getCanvasWidth() + minX;
        }
        private double getIm(int y) {
            return (maxY - minY) * y / getCanvasHeight() + minY;
        }
        private double getReU(int x) {
            return 6. * x / getCanvasWidth() - 3.;
        }
        private double getImU(int y) {
            return 2. * y / getCanvasHeight() - 1.;
        }

        public void paint(Graphics g) {
            super.paint(g);
            int WW = getCanvasWidth();
            int HH = getCanvasHeight();

            BufferedImage buf = new BufferedImage(WW, HH, BufferedImage.TYPE_INT_ARGB);

            Graphics2D G = (Graphics2D) (buf.getGraphics());
            G.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            G.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            G.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            double reC = getRe(currentX);
            double imC = getIm(currentY);

            for(int i = 0; i < WW; i++) {
                double reX = getReU(i);
                for(int j = 0; j < HH; j++) {
                    double imX = getImU(j);

                    int iterations = getIterations(reX,imX,reC,imC);
                    G.setColor(getColor(iterations));
                    G.drawRect(i, j, 1, 1);
                }
            }

            buf.flush();

            g.drawImage(buf, 0, 0, WW, HH,this);
        }
    }
}

