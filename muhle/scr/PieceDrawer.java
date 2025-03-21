package ui.helper;

import enums.PieceStatus;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class PieceDrawer {

    private static final Color[] WHITE_PIECE_COLORS = {
            Color.WHITE, new Color(200, 200, 200)
    };

    private static final Color[] BLACK_PIECE_COLORS = {
            new Color(100, 100, 100), Color.BLACK
    };

    private PieceDrawer() {}

    public static Ellipse2D drawPiece(Graphics2D g2, float x, float y, float diameter, PieceStatus pieceStatus) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] colors = pieceStatus == PieceStatus.WHITE ? WHITE_PIECE_COLORS : BLACK_PIECE_COLORS;

        RadialGradientPaint gradientPaint = new RadialGradientPaint(x, y, diameter / 2, new float[]{0.05f, 1f}, colors);
        g2.setPaint(gradientPaint);

        Ellipse2D circle = getCircle(x, y, diameter*1.4f);

        g2.fill(circle);
        return circle;
    }

    private static Ellipse2D getCircle(float x, float y, float diameter) {
        return new Ellipse2D.Double(x - diameter / 2f, y - diameter / 2f, diameter, diameter);
    }

}
