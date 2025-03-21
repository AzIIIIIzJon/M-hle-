package player;

import enums.PieceStatus;

/**
 * Abstrakte Klasse, damit die Player-Klasse nicht direkt genutzt werden kann.
 */
public abstract class Player {
    /**
     * Startanzahl der Steine. Dürfte funktionieren mit Werten über 3, wurde aber nur mit 9 getestet.
     */
    public static final int START_PIECES = 9;

    /**
     * Speichert die Farbe des Spielers
     */
    private final PieceStatus pieceStatus;

    //
    /**
     * Speichert wie viele Steine der Spieler platziert hat. Unabhängig davon ob Steine entfernt werden.
     * Beispiel: Wenn der Spieler in der Setzphase 6 Steine platziert, aber 2 Steine entfernt wurden, ist der Wert immer noch 6.
     */
    private int piecesPlaced = 0;

    //stores how many pieces the player still have
    /**
     * Speichert auf wie viele Steine der Spieler noch Zugriff hat. Unabhängig davon wie viele platziert wurden.
     * Beispiel: Wenn 6 Steine platziert wurden und 1 entfernt, dann hat dieses Attribut den Wert 8, wenn der Spieler mit 9 Steinen startet.
     */
    private int availablePieces = START_PIECES;

    /**
     * Sagt aus ob der Spieler springen darf in der Verschiebungsphase
     */
    private boolean freeMovement = false;

    /**
     * Konstruktor um ein Player-Objekt zu erzeugen
     * @param pieceStatus Farbe des Spielers
     */
    protected Player(PieceStatus pieceStatus) {
        this.pieceStatus = pieceStatus;
    }


    public PieceStatus getPieceColor() {
        return pieceStatus;
    }

    public int getAvailablePieces() {
        return availablePieces;
    }

    public int getPiecesPlaced() {
        return piecesPlaced;
    }

    /**
     * Erhöht piecesPlaced um eins
     */
    public void placePiece() {
        if (piecesPlaced < START_PIECES) piecesPlaced++;
    }

    /**
     * Verringert availablePieces um eins
     * Wenn es nur noch 3 availablePieces gibt, dann wird freeMovement auf true gesetzt
     */
    public void removePiece() {
        if (availablePieces > 0) availablePieces--;
        if (availablePieces <= 3) freeMovement = true;
    }

    public boolean canMoveFreely() {
        return freeMovement;
    }

    public boolean allPiecesPlaced() {
        return piecesPlaced == START_PIECES;
    }
}
