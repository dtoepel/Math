package org.example.math.feigenbaum;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class FeigenbaumFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final static int GAP = 5;
    private final static int H = 369;
    private final static int W = 1171;

    private double minX = 2.9;
    private double maxX = 4.0;
    private final double minY = 1.0;
    private final double maxY = 0.0;
    private int currentX = -1;
    private int currentY = -1;

    public static void main(String[] args) {
        FeigenbaumFrame f = new FeigenbaumFrame();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        f.pack();
        f.setLocation(350, 100);
        f.setSize(1200, 800);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public FeigenbaumFrame() {
        super();



        JPanel p = new JPanel();

        JPanel o = new JPanel() {
            private static final long serialVersionUID = 1L;
            public void paint(Graphics g) {

                double dpiScalingFactor = ((Graphics2D)g).getTransform().getScaleX();

                int WW = (int)(W*dpiScalingFactor);
                int HH = (int)(H*dpiScalingFactor);

                BufferedImage buf = new BufferedImage(
                        WW, HH, BufferedImage.TYPE_INT_ARGB);

                Graphics2D G = (Graphics2D) (buf.getGraphics());

                G.setColor(Color.GRAY);
                G.drawRect(0, 0, WW-1, HH-1);


                G.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                G.setColor(new Color(0x11_00_00_00,true));

                for(int i = 0; i < WW; i++) {
                    double x = getX(i);
                    double a = 0.5;
                    for(int n = 0; n < 1000; n++) {
                        a = x * a * (1-a);
                        double j = (a-minY) / (maxY - minY) * HH;
                        double size = 1;
                        Ellipse2D e = new Ellipse2D.Double(i-size/2, j-size/2, size, size);
                        if(n > 860) G.draw(e);
                    }
                }

                G.setColor(Color.BLACK);
                G.drawLine(currentX, 0, currentX, HH);

                G.drawString("x=" + getX(currentX), currentX+10, currentY-13);
                G.drawString("y=" + getY(currentY), currentX+10, currentY);

                buf.flush();

                ((Graphics2D)g).setTransform(new AffineTransform());
                g.drawImage(buf, 0, 0, this);
            }

            private double getX(int i) {
                return ((maxX - minX) * i / W + minX);
            }

            private double getY(int i) { return ((maxY - minY) * i / H + minY);
            }
        };

        o.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                currentX = e.getX()*5/4;
                currentY = e.getY()*5/4;
                repaint();
            }

        });

        o.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double factor = e.getWheelRotation()>0?2:.5;
                double mouseX = e.getX()*(maxX-minX)/W+minX;
                minX = mouseX - (mouseX-minX)*factor;
                maxX = mouseX + (maxX-mouseX)*factor;;
                repaint();
            }
        });

        JPanel u = new JPanel()  {
            private static final long serialVersionUID = 1L;
            public void paint(Graphics g) {

                BufferedImage buf = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);

                Graphics2D G = (Graphics2D) (buf.getGraphics());


                G.setColor(Color.GRAY);
                G.drawRect(0, 0, W-1, H-1);


                G.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                G.setColor(new Color(0xff_00_00_00,true));

                double a = 0.5;

                for(int i = 0; i < W; i++) {
                    double x = (maxX - minX) * currentX / W + minX;
                    a =  x * a * (1-a);
                    double j = (a-minY) / (maxY - minY) * H;
                    double size = 1;
                    Ellipse2D e = new Ellipse2D.Double(i-size/2, j-size/2, size, size);
//						if(n > 160)
                    G.draw(e);
//					}
                }

                G.setColor(Color.BLACK);
//				G.drawLine(currentX, 0, currentX, H);

                buf.flush();

                g.drawImage(buf, 0, 0, this);
            }
        };

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
}
