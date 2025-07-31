package org.example.elections.stv.graphics;

import org.example.JPanelUnScaled;
import org.example.elections.stv.Candidate;
import org.example.elections.stv.Election;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class STVResultPanel extends JPanelUnScaled {
    private final Election election;

    public STVResultPanel(Election election) {
        this.election = election;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                STVResultPanel.this.election.proceed();
                repaint();
            }
        });
    }

    public void paint(Graphics g) {
        super.paint(g);

        int W = getCanvasWidth();
        int H = getCanvasHeight();
        int BASE = (H*4)/5;
        int DEFAULT_HEIGHT=3*H/5;
        double quota = election.getFinalQuota();

        BufferedImage buf = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D G = (Graphics2D) (buf.getGraphics());
        FontMetrics metrics = g.getFontMetrics(G.getFont());

        int maxCandidates = election.getCandidates().size();
        double widthPerCandidate = 1. * W / maxCandidates;
        for(int cNo=0; cNo<maxCandidates; cNo++) {
            Candidate c = election.getCandidates().get(cNo);
            G.setColor(c.getParty().getColor());
            G.fillRect(
                    (int)((cNo+.5) * widthPerCandidate - metrics.stringWidth(c.getParty().getShortName())/2.)-2,
                    BASE + (cNo%2!=0?9:23),
                    metrics.stringWidth(c.getParty().getShortName())+4,
                    14);
            G.setColor(Color.BLACK);
            G.drawString(c.name,
                    (int)((cNo+.5) * widthPerCandidate - metrics.stringWidth(c.name)/2.),
                    BASE + (cNo%2==0?20:34));
            G.drawString(c.getParty().getShortName(),
                    1+(int)((cNo+.5) * widthPerCandidate - metrics.stringWidth(c.getParty().getShortName())/2.),
                    1+BASE + (cNo%2!=0?20:34));
            G.setColor(Color.WHITE);
            G.drawString(c.getParty().getShortName(),
                    (int)((cNo+.5) * widthPerCandidate - metrics.stringWidth(c.getParty().getShortName())/2.),
                    BASE + (cNo%2!=0?20:34));

            Election.VoteByFirstChoiceResult votesByFirstChoice = election.getVotesForCandidateByFirstChoice(c);
            double currentY = BASE;
            for(int i = 0; i < votesByFirstChoice.candidates().length; i++) {
                double votes = votesByFirstChoice.votes()[i];
                if(votes>.001*quota) {
                    // if there is a significant amount of votes
                    // then calculate the height
                    double height = DEFAULT_HEIGHT / quota * votes;
                    //System.err.println(votes+"-->"+height);
                    int x = (int)((cNo+.2) * widthPerCandidate);
                    int xx = (int)((cNo+.8) * widthPerCandidate);
                    int y = (int)(currentY - height);
                    int yy = (int)height;
                    currentY -= height;

                    G.setColor(votesByFirstChoice.candidates()[i].getColor());
                    G.fillRect(x, y, xx-x, yy);
                    G.setColor(Color.BLACK);
                    G.drawRect(x, y, xx-x, yy);
                }
            }
        }

        G.setColor(Color.BLACK);
        G.drawLine(0, BASE-DEFAULT_HEIGHT, W, BASE-DEFAULT_HEIGHT);

        buf.flush();
        g.drawImage(buf, 0, 0, this);
    }

    public void showInFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setContentPane(this);
        frame.pack();

        frame.setLocation(400,100);
        frame.setSize(800,600);
        frame.setVisible(true);
    }

}
