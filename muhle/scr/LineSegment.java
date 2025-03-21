package ui.gameboard;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class LineSegment extends JLabel {
    private final Orientation orientation;

    public LineSegment(Orientation orientation) {
        this.orientation = orientation;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int height = this.getHeight();
        int width = this.getWidth();

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.BLACK);

        if (orientation == Orientation.VERTICAL) {
            g2.draw(new Line2D.Float(width / 2f, 0, width / 2f, height));
        } else {
            g2.draw(new Line2D.Float(0, height / 2f, width, height / 2f));
        }
    }

    public enum Orientation {HORIZONTAL, VERTICAL}
}
