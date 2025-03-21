package player;

import enums.PieceStatus;

/**
 * Subklasse von Player die einen Player mit der Steinfarbe Weiß erstellt
 */
public class White extends Player {
    public White() {
        super(PieceStatus.WHITE);
    }
}
