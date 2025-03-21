package ui.status;

import enums.PieceStatus;
import gameplay.Game;
import gameplay.Gameplay;
import player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import static ui.helper.PieceDrawer.drawPiece;

public class PlayerStatus extends JPanel {
    private static final Font NORMAL = new Font(Font.MONOSPACED, Font.BOLD, 17);
    private static final Font ACTIVE_DISPLAY = new Font(Font.MONOSPACED, Font.BOLD, 20);
    private static final int TEXT_BOX_HEIGHT = 50;
    private static Gameplay gameplay;
    private final PieceStatus color;
    private transient Player player;

    private static final Color   ACTIVE = new Color(122, 237, 141);
    private static final Color INACTIVE = new Color(241, 130, 122);

    public PlayerStatus(PieceStatus color) {
        this.color = color;
        setBackground(Color.WHITE);
    }

    public static void setGameplay(Gameplay gameplay) {
        PlayerStatus.gameplay = gameplay;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameplay == null) return;
        if (player == null) player = gameplay.getPlayerByColor(color);

        // Preventing component from rendering if "getPlayerByColor" returns null to prevent errors
        if(player == null){
            System.err.println("getPlayerByColor should not return null!");
            return;
        }

        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        drawPlayerName(graphics2D, 0);
        drawActive(graphics2D, 1);

        if (Game.getInstance().getState() == Game.State.END) return;

        drawMoveStatus(graphics2D, 2);
        drawPieceStatus(graphics2D, 3.5);
    }

    private void drawActive(Graphics2D g, double heightPosition) {
        Player gameplayActivePlayer = gameplay.getActivePlayer();

        // Preventing component from rendering if "getActivePlayer" returns null to prevent errors
        if(gameplayActivePlayer == null){
            System.err.println("getActivePlayer should not return null!");
            return;
        }

        boolean activePlayer = gameplayActivePlayer == player;

        String text = activePlayer ? "active" : "inactive";

        if (gameplay.getWinner() != null) {
            text = gameplay.getWinner() == player ? "WON!" : "LOST!";
        }

        Rectangle2D rectangle = getStatusRectangle(heightPosition);

        g.setColor(activePlayer ? ACTIVE : INACTIVE);

        g.fill(rectangle);

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.draw(rectangle);

        drawText(g, text, heightPosition, ACTIVE_DISPLAY);
    }

    private void drawPlayerName(Graphics2D g, double heightPosition) {
        String text = color.name() + " PLAYER";

        drawText(g, text, heightPosition, NORMAL);
    }

    private void drawMoveStatus(Graphics2D g, double heightPosition) {
        Player gameplayActivePlayer = gameplay.getActivePlayer();

        // Preventing component from rendering if "getActivePlayer" returns null to prevent errors
        if(gameplayActivePlayer == null){
            System.err.println("getActivePlayer should not return null!");
            return;
        }

        String next = "next move:";
        String move = player.canMoveFreely() ? "JUMP PIECE" : "MOVE PIECE";

        if (player.getPiecesPlaced() != Player.START_PIECES) move = "PLACE PIECE";
        if (gameplay.getExpectedInputState() == Gameplay.InputState.REMOVING && gameplay.getActivePlayer() == player)
            move = "REMOVE PIECE";

        drawText(g, next, heightPosition, NORMAL);
        drawText(g, move, heightPosition + 0.5, NORMAL);
    }

    private void drawPieceStatus(Graphics2D g, double heightPosition) {
        if (player.getPiecesPlaced() == Player.START_PIECES) return;

        int piecesLeftToPlace = Player.START_PIECES - player.getPiecesPlaced();



        drawText(g, "PIECES", heightPosition, NORMAL);
        drawText(g, "TO PLACE (" + piecesLeftToPlace + "):", heightPosition + 0.5, NORMAL);



        float diameter = getWidth() * 0.2f;
        for (int i = 0; i < piecesLeftToPlace; i++) {
            if(i%2==0)
                drawPiece(g, getWidth() / 2f - diameter*0.7f, TEXT_BOX_HEIGHT / 2f + (float) getY(heightPosition + 1.5f + i/2.4), diameter, color);
            else
                drawPiece(g, getWidth() / 2f + diameter*0.7f, TEXT_BOX_HEIGHT / 2f + (float) getY(heightPosition + 1.5f + i/2.4), diameter, color);
        }
    }

    private Rectangle2D getStatusRectangle(double heightPosition) {
        return new Rectangle2D.Double(getWidth() * 0.1, getY(heightPosition), getWidth() * 0.8, TEXT_BOX_HEIGHT * 0.8);
    }

    private void drawText(Graphics2D g, String text, double heightPosition, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);

        double textX = (getWidth() - metrics.stringWidth(text)) / 2.;
        double textY = getY(heightPosition) + (TEXT_BOX_HEIGHT * 0.8 - metrics.getHeight()) / 2 + metrics.getAscent();

        g.setFont(font);
        g.drawString(text, (int) textX, (int) textY);
    }

    private double getY(double heightPosition) {
        return TEXT_BOX_HEIGHT * (0.1 + heightPosition) + getHeight() / 14.;
    }
}
