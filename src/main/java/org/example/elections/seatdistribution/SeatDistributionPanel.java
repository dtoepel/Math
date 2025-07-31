package org.example.elections.seatdistribution;

import org.example.JPanelUnScaled;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class SeatDistributionPanel extends JPanelUnScaled {
    private DistributionMethod currentMethod = DistributionMethod.D_HONDT;
    private int availableSeats = 5;
    private int mouseX;
    private int mouseY;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Seat Distribution");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        GroupLayout layout = new GroupLayout(mainPanel);
        mainPanel.setLayout(layout);

        final JComboBox<DistributionMethod> methodBox = new JComboBox<>(DistributionMethod.values());
        final SeatDistributionPanel seatDistributionPanel = new SeatDistributionPanel();
        final JSlider seatSlider = new JSlider(1,20, seatDistributionPanel.availableSeats);
        final JLabel seatLabel = new JLabel("Available Seats: "+ seatDistributionPanel.availableSeats);
        final JLabel methodLabel = new JLabel("Method:");

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(seatDistributionPanel)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(methodLabel).addComponent(seatLabel))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(methodBox).addComponent(seatSlider))));

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(seatDistributionPanel)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(methodLabel).addComponent(methodBox))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(seatLabel).addComponent(seatSlider)));

        seatSlider.addChangeListener(e -> {
            seatLabel.setText("Available Seats: " + seatSlider.getValue());
            seatDistributionPanel.availableSeats = seatSlider.getValue();
            seatDistributionPanel.repaint();
        });

        methodBox.addActionListener(e -> {
            seatDistributionPanel.currentMethod = (DistributionMethod) methodBox.getSelectedItem();
            seatDistributionPanel.repaint();
        });

        frame.setContentPane(mainPanel);
        frame.pack();

        frame.setLocation(400,100);
        frame.setSize(800,600);
        frame.setVisible(true);
    }

    public SeatDistributionPanel() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                Point2D p = transformMouse(e);
                mouseX = (int) p.getX();
                mouseY = (int) p.getY();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int W = getCanvasWidth();
        int H = getCanvasHeight();
        BufferedImage buf = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D G = (Graphics2D) (buf.getGraphics());

        /*
        oben: W/2,0
        höhe = H
        B (=seitenlänge) = höhe*2/W3
        links = -B/2,H
        rechts = B/2,H

        stimmen oben = (H-y) / H
        breite bei y (w) = B * y / H

        Stimmen links+rechts = 1-oben
        anteil rechts = (x - W/2 + w/2) / w
         */

        double baseWidth = H * 2 / Math.sqrt(3);
        Point2D oben = new Point2D.Double(W/2., 0);
        Point2D links = new Point2D.Double(W/2. - baseWidth/2, H);
        Point2D rechts = new Point2D.Double(W/2. + baseWidth/2, H);

        for(int x = 0; x < W; x++) {
            for(int y = 0; y < H; y++) {
                Point2D p = transformPoint(new Point(x,y));
                double[] voteShares = getVoteShares(x,y,W,H);
                if(voteShares != null) {
                    int[] seats = getSeats(voteShares, availableSeats);
                    int red =200*seats[0]/availableSeats;
                    int green = 200*seats[1]/availableSeats;
                    int blue = 255*seats[2]/availableSeats;
                    Color c = new Color(red, green, blue);
                    G.setColor(c);
                    G.drawLine(x, y, x, y);
                }
            }
        }

        G.setColor(Color.BLACK);
        double[] votes = getVoteShares(mouseX, mouseY, W, H);/*
        G.drawString("x: " + mouseX + "/" + W, mouseX, mouseY);
        G.drawString("y: " +mouseY + "/" + H, mouseX, mouseY+12);
        int y = mouseY;
        int x = mouseX;
        double localWidth = baseWidth * y / H;
        G.drawString("w: " + localWidth, mouseX, mouseY+24);
        double votesTop = 1. * (H-y) / H;
        G.drawString("O: " + votesTop, mouseX, mouseY+36);
        double shareRight = (x - W/2 + localWidth/2) / localWidth;
        G.drawString("R vs L: " + shareRight, mouseX, mouseY+48);*/
        G.drawString((votes==null?"null": Arrays.toString(votes)), mouseX, mouseY+10);
        G.drawString((votes==null?"null": Arrays.toString(getSeats(votes, availableSeats))), mouseX, mouseY+22);


        buf.flush();
        g.drawImage(buf, 0, 0, this);

    }

    private int[] getSeats(double[] voteShares, int availableSeats) {
        return distribute(voteShares, availableSeats, currentMethod);
    }

    private double[] getVoteShares(double x, double y, int W, int H) {

        double baseWidth = H * 2 / Math.sqrt(3);
        double votesTop = 1. * (H-y) / H;
        double localWidth = baseWidth * y / H;
        if(localWidth > 0) {
            double shareRight = (x - W/2 + localWidth/2) / localWidth;
            double votesRight = (1 - votesTop) * shareRight;
            double votesLeft = 1 - votesTop - votesRight;
            if(votesTop>=0 && votesTop<=1
                    && votesLeft>=0 && votesLeft<=1
                    && votesRight>=0 && votesRight<=1) {
                return new double[]{votesTop, votesLeft + 1/1000000., votesRight + 1/3700000.};
            } else {
                return null;
            }
        }
        return null;
    }

    private int[] distribute(double[] votes, int totalSeats, DistributionMethod method) {
        if(method == DistributionMethod.HARE_NIEMEYER) {
            return distributeHN(votes, totalSeats);
        } else if(method == DistributionMethod.HUNTINGTON_HILL) {
            return distributeHH(votes, totalSeats);
        } else {
            int[] result = new int[votes.length];
            for(int i = 0; i < result.length; i++) {result[i] = 0;}
            double minQ = 1/100.;
            double maxQ = 2.;
            double q = 1./totalSeats;
            while(sum(result) != totalSeats) {
               // System.out.println(q + "->" + Arrays.toString(result) + " " + Arrays.toString(votes));
                for(int i = 0; i < result.length; i++) {
                    result[i] = (int) (votes[i] / q + (method == DistributionMethod.D_HONDT?0.0:0.5));
                }
                if(sum(result) > totalSeats) {minQ = q; q = Math.sqrt(q * maxQ);}
                if(sum(result) < totalSeats) {maxQ = q; q = Math.sqrt(q * minQ);}
            }
            return result;
        }
    }

    private int[] distributeHH(double[] votes, int totalSeats) {
        int[] result = new int[votes.length];
        double next[] = new double[votes.length];
        for(int i = 0; i < result.length; i++) {result[i] = 1;}
        while(sum(result) < totalSeats) {
            for(int i = 0; i < result.length; i++) {
                next[i] = votes[i] / Math.sqrt(result[i]) / Math.sqrt(result[i]+1);
            }
            int g = getGreatestIndex(next);
            result[g] += 1;
        }
        return result;
    }

    private int[] distributeHN(double[] votes, int totalSeats) {
        int[] result = new int[votes.length];
        for(int i = 0; i < result.length; i++) {
            result[i] = (int) (votes[i] / totalSeats);
            votes[i] -= result[i];
        }
        while (sum(result) < totalSeats) {
            int g = getGreatestIndex(votes);
            result[g] += 1;
            votes[g] -= 1. / totalSeats;
        }
        return result;
    }

    private int getGreatestIndex(double[] a) {
        double max = Double.NEGATIVE_INFINITY;
        int x = -1;
        for(int i = 0; i < a.length; i++) {
            if(a[i] > max) {
                x = i;
                max = a[i];
            }
        }
        return x;
    }

    private int sum(int[] a) {
        int sum = 0;
        for(int i = 0; i < a.length; i++) {sum += a[i];}
        return sum;
    }

    private enum DistributionMethod {D_HONDT, SAINTE_LAGUË, HARE_NIEMEYER, HUNTINGTON_HILL}

}
