package org.example.math.newton;

import org.example.math.utils.Complex;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class NewtonFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final static int H = 800;
    private final static int W = 1200;//1171;

    private double minX = -15.;
    private double maxX = 15;
    private double minY = -10.;
    private double maxY = 10.;
    private int currentX = -1;
    private int currentY = -1;

    private Vector<Complex> nullstellen = new Vector<Complex>();
    private Vector<Color> colors = new Vector<Color>();

    private Polynom p0;
    private Polynom p1;

    public static void main(String[] args) {
        NewtonFrame f = new NewtonFrame();

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

    public NewtonFrame() {
        super();

        nullstellen.add(new Complex(3, 4));
        nullstellen.add(new Complex(3, -4));
        nullstellen.add(new Complex(-5, 0));
        nullstellen.add(new Complex(-6, 3));
        nullstellen.add(new Complex(0, 3));
//		nullstellen.add(Complex.NULL);

        p0 = Polynom.EINS;
        for(Complex nullstelle : nullstellen) {
            p0 = p0.mul(new Polynom(nullstelle));
            System.err.println(p0);
        }
        p1 = p0.ableiten();
        System.err.println(p1);


        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.GRAY);
        colors.add(new Color(0x4444bb));
        colors.add(new Color(0x4488bb));
        colors.add(new Color(0x8844bb));
        colors.add(new Color(0x2255bb));
        colors.add(new Color(0x5522bb));

        JPanel o = new JPanel() {
            private static final long serialVersionUID = 1L;
            public void paint(Graphics g) {

                BufferedImage buf = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);

                Graphics2D G = (Graphics2D) (buf.getGraphics());

//				int größenordung = (int) Math.log10(getX(W) - getX(0));
//				double zentrumH =
//				System.err.println(größenordung);

                for(int x = 0; x < W; x++) {
                    double re = getRe(x);
                    for(int y = 0; y < H; y++) {
                        double im = getIm(y);

                        Complex c = new Complex(re, im);

                        for(int i = 0; i < 15; i++) {
                            c = newton(p0, p1, c);
                        }

//						System.err.println(c);
                        int nn = nächsteNullstelle(c);

//						int iterations = getIterations(0,0,x,y);
                        if(nn != -1) {
                            G.setColor(colors.get(nn));//getColor(iterations));
                            G.drawRect(x, y, 1, 1);
                        }
                    }}

                for(int n = 0; n < nullstellen.size(); n++) {
                    Complex nullstelle = nullstellen.get(n);
                    G.setColor(Color.BLACK);
                    G.drawOval(getX(nullstelle.r())-3, getY(nullstelle.i())-3, 6, 6);
                }

                G.setColor(Color.WHITE);
                G.drawLine(currentX, 0, currentX, H);
                G.drawLine(0, currentY, W, currentY);

                G.drawString("re=" + getRe(currentX), currentX+10, currentY-13);
                G.drawString("im=" + getIm(currentY), currentX+10, currentY);

                buf.flush();

                g.drawImage(buf, 0, 0, this);
            }

            private Complex newton(Polynom p0, Polynom p1, Complex c) {
                return c.sub(p0.at(c).div(p1.at(c)));
            }

            private int nächsteNullstelle(Complex c) {
                double dist = Double.MAX_VALUE;
                int bestN = -1;
                for(int n = 0; n < nullstellen.size(); n++) {
                    double distNeu = c.dist(nullstellen.get(n));
                    if(distNeu < dist) {
                        dist = distNeu;
                        bestN = n;
                    }
                }
                return bestN;
            }


            private double getRe(int px) {
                return (maxX - minX) * px / W + minX;
            }

            private double getIm(int px) {
                return (maxY - minY) * px / H + minY;
            }

            private int getX(double re) {
                return (int) ((re - minX) * W / (maxX - minX));
            }

            private int getY(double im) {
                return (int) ((im - minY) * H / (maxY - minY));
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


        this.setContentPane(o);
    }
//
//	private double getRe(int x) {
//		return (maxX - minX) * x / W + minX;
//	}
//
//	private double getIm(int y) {
//		return (maxY - minY) * y / H + minY;
//	}
//
//	private double getReU(int x) {
//		return 6. * x / W - 3.;
//	}
//
//	private double getImU(int y) {
//		return 2. * y / H - 1.;
//	}


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
