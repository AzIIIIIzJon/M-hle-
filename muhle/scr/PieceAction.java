package ui.helper;

import enums.Position;
import ui.gameboard.PieceButton;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PieceAction extends AbstractAction {

    private static Position input = null;
    private final PieceButton pieceButton;

    public PieceAction(PieceButton pieceButton) {
        this.pieceButton = pieceButton;
    }

    public static Position getInput() {
        return input;
    }

    public static void clearInput() {
        input = null;
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        if (input == null) input = pieceButton.getPlacement();
    }
}
