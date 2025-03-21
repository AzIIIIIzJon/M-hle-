package player;

import enums.PieceStatus;

/**
 * Subklasse von Player die einen Player mit der Steinfarbe Schwarz erstellt
 */
public class Black extends Player {
    public Black() {
        super(PieceStatus.BLACK);
    }
}
