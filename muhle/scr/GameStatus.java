package ui.status;

import enums.PieceStatus;
import gameplay.Game;
import gameplay.Game.State;
import gameplay.Gameplay;
import ui.helper.ButtonGridPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GameStatus extends ButtonGridPanel {
    private static final Font NORMAL = new Font(Font.MONOSPACED, Font.BOLD, 17);
    private static final Font TITLE = new Font(Font.MONOSPACED, Font.BOLD, 28);
    private final JButton whiteButton;
    private final JButton blackButton;
    private final JButton randomButton;
    private Gameplay gameplay;
    private double layerHeight;

    public GameStatus(int height) {
        super(height);

        whiteButton  = new JButton("White");
        blackButton  = new JButton("Black");
        randomButton = new JButton("Random");

        whiteButton.addActionListener( e -> Game.getInstance().startGame(PieceStatus.WHITE));
        blackButton.addActionListener( e -> Game.getInstance().startGame(PieceStatus.BLACK));
        randomButton.addActionListener(e -> Game.getInstance().startGame());

        createGridPanel(0, 0, 56, 4);

        createGridPanel(0, 4, 16, 1);
        createGridButton(16, 4, 6, 1, whiteButton);
        createGridPanel(22, 4, 3, 1);

        createGridButton(25, 4, 6, 1, blackButton);

        createGridPanel(31, 4, 3, 1);
        createGridButton(34, 4, 6, 1, randomButton);
        createGridPanel(40, 4, 16, 1);
    }

    public void setGameplay(Gameplay gameplay) {
        this.gameplay = gameplay;
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);

        if (Game.getInstance() == null) return;
        State state = Game.getInstance().getState();
        if (state != State.START && Arrays.asList(getGridPanel().getComponents()).contains(whiteButton)) {
            getGridPanel().remove(whiteButton);
            getGridPanel().remove(blackButton);
            getGridPanel().remove(randomButton);
        }

        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        layerHeight = getHeight() / 5d;

        drawTitle(g2D);

        switch (state) {
            case START  -> drawStart(g2D);
            case GAME   -> drawGame(g2D);
            case END    -> drawEnd(g2D);
        }
    }

    private void drawTitle(Graphics2D g2D) {
        drawText(g2D, "NINE MENS MORRIS", 0, 3, TITLE);
    }

    private void drawStart(Graphics2D g2D) {
        drawText(g2D, " CHOOSE STARTING COLOR:", layerHeight * 2.5, 1.5, NORMAL);
    }

    private void drawGame(Graphics2D g2D) {
        if(gameplay == null || gameplay.getCurrentPhase() == null){
            System.err.println("getCurrentPhase is not allowed to be null!");
            return;
        }

        drawText(g2D, " CURRENT PHASE:", layerHeight * 2.5, 1.5, NORMAL);
        drawText(g2D, gameplay.getCurrentPhase().name() + " PHASE", layerHeight * 3, 2, NORMAL);
    }

    private void drawEnd(Graphics2D g2D) {
        drawText(g2D, "GAME HAS ENDED!", layerHeight * 2.5, 1.5, NORMAL);
        drawText(g2D, "CLICK RESTART FOR A NEW GAME", layerHeight * 3, 2, NORMAL);
    }

    private void drawText(Graphics2D g, String text, double heightPosition, double layerProportion, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);

        double textX = (getWidth() - metrics.stringWidth(text)) / 2d;
        double textY = heightPosition + (layerHeight * layerProportion - metrics.getHeight()) / 2 + metrics.getAscent();

        g.setFont(font);
        g.drawString(text, (int) textX, (int) textY);
    }
}

