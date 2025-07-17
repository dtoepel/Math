package org.example.math.mandelbrot;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

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
        f.setLocation(350, 100);
        f.setSize(1200, 800);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public MandelbrotFrame() {
        super();

        JPanel p = new JPanel();

        JPanel o = new JPanel() {
            private static final long serialVersionUID = 1L;
            public void paint(Graphics g) {

                //BufferedImage buf = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);

                //Graphics2D G = (Graphics2D) (buf.getGraphics());
                Graphics2D G=(Graphics2D)g;
                //G.setColor(new Color(0x880088));
                //G.fillRect(0,0,100,100);

                G.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                G.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                G.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                for(int i = 0; i < W; i++) {
                    double x = getX(i);
                    for(int j = 0; j < H; j++) {
                        double y = getY(j);

                        int iterations = getIterations(0,0,x,y);
                        G.setColor(getColor(iterations));
                        G.drawRect(i, j, 1, 1);
                    }}


//				G.setColor(Color.GRAY);
//				G.drawRect(0, 0, W-1, H-1);
//
//
//	            G.setRenderingHint(
//	                    RenderingHints.KEY_ANTIALIASING,
//	                    RenderingHints.VALUE_ANTIALIAS_ON);
//				G.setColor(new Color(0x11_00_00_00,true));
//
//				for(int i = 0; i < W; i++) {
//					double x = getX(i);
//					double a = 0.5;
//					for(int n = 0; n < 1000; n++) {
//						a = x * a * (1-a);
//						double j = (a-minY) / (maxY - minY) * H;
//						double size = 1;
//						Ellipse2D e = new Ellipse2D.Double(i-size/2, j-size/2, size, size);
//						if(n > 860) G.draw(e);
//					}
//				}

                G.setColor(Color.WHITE);
                G.drawLine(currentX, 0, currentX, H);
                G.drawLine(0, currentY, W, currentY);

                G.drawString("re=" + getX(currentX), currentX+10, currentY-13);
                G.drawString("im=" + getY(currentY), currentX+10, currentY);

                //buf.flush();

                //g.drawImage(buf, 0, 0, this);
            }


            private double getX(int i) {
                return (maxX - minX) * i / W + minX;
            }

            private double getY(int i) {
                return (maxY - minY) * i / H + minY;
            }
        };

        JPanel u = new JPanel()  {
            private static final long serialVersionUID = 1L;
            public void paint(Graphics g) {

                BufferedImage buf = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);

                Graphics2D G = (Graphics2D) (buf.getGraphics());
                G.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                G.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                G.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                double reC = getRe(currentX);
                double imC = getIm(currentY);

                for(int i = 0; i < W; i++) {
                    double reX = getReU(i);
                    for(int j = 0; j < H; j++) {
                        double imX = getImU(j);

                        int iterations = getIterations(reX,imX,reC,imC);
                        G.setColor(getColor(iterations));
                        G.drawRect(i, j, 1, 1);
                    }}
//
//
//				G.setColor(Color.GRAY);
//				G.drawRect(0, 0, W-1, H-1);
//
//
//	            G.setRenderingHint(
//	                    RenderingHints.KEY_ANTIALIASING,
//	                    RenderingHints.VALUE_ANTIALIAS_ON);
//				G.setColor(new Color(0xff_00_00_00,true));
//
//				double a = 0.5;
//
//				for(int i = 0; i < W; i++) {
//					double x = (maxX - minX) * currentX / W + minX;
//					a =  x * a * (1-a);
//					double j = (a-minY) / (maxY - minY) * H;
//					double size = 1;
//					Ellipse2D e = new Ellipse2D.Double(i-size/2, j-size/2, size, size);
////						if(n > 160)
//					G.draw(e);
////					}
//				}
//
//				G.setColor(Color.BLACK);
////				G.drawLine(currentX, 0, currentX, H);
//
                buf.flush();

                g.drawImage(buf, 0, 0, W, H,this);
            }
        };

        o.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                currentX = e.getX();
                currentY = e.getY();
                repaint();
            }

        });

        o.addMouseWheelListener(new MouseWheelListener() {

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

    private double getRe(int x) {
        return (maxX - minX) * x / W + minX;
    }

    private double getIm(int y) {
        return (maxY - minY) * y / H + minY;
    }

    private double getReU(int x) {
        return 6. * x / W - 3.;
    }

    private double getImU(int y) {
        return 2. * y / H - 1.;
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
}

