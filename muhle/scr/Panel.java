package ui;

import enums.PieceStatus;
import gameplay.Gameplay;
import ui.gameboard.GameBoard;
import ui.status.GameStatus;
import ui.status.PlayerStatus;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {

    private static final Panel instance = new Panel();
    private transient Gameplay gameplay;
    private GameBoard gameBoard;
    private GameStatus gameStatus;

    private Panel() {
        setLayout(new BorderLayout());
        this.gameplay = null;
        placeNewComponents();
    }

    public static Panel getInstance() {
        return instance;
    }

    public void setGameplay(Gameplay gameplay) {
        if ((gameplay == null) == (this.gameplay == null)) return;

        this.gameplay = gameplay;
        PlayerStatus.setGameplay(gameplay);

        if (gameplay == null) {
            placeNewComponents();
        } else {
            gameBoard.setGameplay(gameplay);
            gameStatus.setGameplay(gameplay);
        }
    }

    private void placeNewComponents() {
        gameBoard = new GameBoard();
        gameStatus = new GameStatus(150);
        CloseRestartPanel closeRestartPanel = new CloseRestartPanel(100);

        PlayerStatus player1 = new PlayerStatus(PieceStatus.WHITE);
        PlayerStatus player2 = new PlayerStatus(PieceStatus.BLACK);

        player1.setPreferredSize(new Dimension(1000 / 7, 470));
        player2.setPreferredSize(new Dimension(1000 / 7, 470));

        removeAll();

        add(gameStatus, BorderLayout.PAGE_START);
        add(gameBoard, BorderLayout.CENTER);
        add(closeRestartPanel, BorderLayout.PAGE_END);
        add(player1, BorderLayout.LINE_START);
        add(player2, BorderLayout.LINE_END);

        revalidate();
    }
}
