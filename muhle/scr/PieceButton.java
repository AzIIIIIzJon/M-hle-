package ui.gameboard;

import enums.Position;
import enums.PieceStatus;
import gameplay.Gameplay;
import ui.helper.PieceAction;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Set;

import static ui.helper.PieceDrawer.drawPiece;

public class PieceButton extends JButton {

    private final Set<Direction> directions;
    private final Position placement;
    private PieceStatus pieceStatus = PieceStatus.EMPTY;
    private transient Gameplay gameplay;
    private static final Color   ACTIVE = new Color(122, 237, 141);

    public PieceButton(Set<Direction> directions, Position placement) {
        this.directions = directions;
        this.placement = placement;

        this.setOpaque(false);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);

        this.addActionListener(new PieceAction(this));
    }

    public Gameplay getGameplay() {
        return gameplay;
    }

    public void setGameplay(Gameplay gameplay) {
        this.gameplay = gameplay;
    }

    private static Ellipse2D getCircle(int width, int height, float radius) {
        return new Ellipse2D.Double((width - radius) / 2f, (height - radius) / 2f, radius, radius);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int height = this.getHeight();
        int width = this.getWidth(); 

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawLineSegments(g2, width, height);

        if (pieceStatus == PieceStatus.EMPTY) drawSmallCircle(g2, width, height);
        else{
            Ellipse2D piece = drawPiece(g2, width / 2f, height / 2f, Math.min(width, height) * 0.25f, pieceStatus);
            if(gameplay != null && gameplay.getPrevPosition() == placement){
                g2.setColor(ACTIVE);
                g2.setStroke(new BasicStroke(4));
                g2.draw(piece);
            }
        }


    }

    private void drawLineSegments(Graphics2D g2, int width, int height) {
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));

        // full and half width or height
        float fullWidth = width;
        float fullHeight = height;
        float halfWidth = fullWidth/2;
        float halfHeight = fullHeight/2;

        for (Direction direction : directions) {
            Line2D line = switch (direction) {
                case LEFT   -> new Line2D.Float(        0, halfHeight, halfWidth, halfHeight);
                case TOP    -> new Line2D.Float(halfWidth,          0, halfWidth, halfHeight);
                case RIGHT  -> new Line2D.Float(halfWidth, halfHeight, fullWidth, halfHeight);
                case BOTTOM -> new Line2D.Float(halfWidth, halfHeight, halfWidth, fullHeight);
            };
            g2.draw(line);
        }
    }

    private void drawSmallCircle(Graphics2D g2, int width, int height) {
        g2.setPaint(Color.BLACK);

        float radius = Math.max(height,width) * 0.075f;

        Ellipse2D circle = getCircle(width, height, radius);
        g2.fill(circle);
    }

    public Position getPlacement() {
        return placement;
    }

    public void setPieceStatus(PieceStatus pieceStatus) {
        this.pieceStatus = pieceStatus;
    }

    public enum Direction {TOP, BOTTOM, LEFT, RIGHT}
}
