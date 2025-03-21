package gameplay;

import enums.Position;
import enums.PieceStatus;
import player.Black;
import player.Player;
import player.White;
import playground.Playground;
import playground.Playground.Phase;
import ui.helper.PieceAction;

import static gameplay.Game.State.GAME;

public class Gameplay {

    /**
     * Dieses Enum sagt aus, was der nächste Zug des aktuellen Spielers ist.
     */
    public enum InputState {
        PLACING, MOVING, REMOVING
    }

    /**
     * Instanz der Game-Klasse
     */
    private final Game game;
    /**
     * Instanz der Playground-Klasse
     */
    private final Playground playground;
    /**
     * Repräsentiert den nächsten erwarteten Input des aktuellen Spielers.
     */
    private InputState expectedInputState = InputState.PLACING;
    /**
     * Speichert die Position eines ausgewählten Steins, welches für die move-Operation genutzt werden soll.
     */
    private Position prevPosition = null;
    /**
     * Speichert den Gewinner. Solange dieser noch nicht existiert, wird dieses Attribut den Wert null haben.
     */
    private Player winner = null;

    /**
     * Speichert wie viele Steine entfernt werden können
     */
    private int piecesToRemove = 0;

    /**
     * Konstruktor um ein Gameplay-Objekt zu erzeugen, wobei die Startfarbe zufällig ist
     */
    public Gameplay() {
        this.game = Game.getInstance();
        this.playground = new Playground(new White(), new Black());
    }

    /**
     * Konstruktor um ein Gameplay-Objekt zu erzeugen, wobei die Startfarbe ausgewählt ist
     * @param color Startfarbe für den playground
     */
    public Gameplay(PieceStatus color) {
        this.game = Game.getInstance();
        this.playground = new Playground(new White(), new Black(), color);
    }

    public InputState getExpectedInputState() {
        return expectedInputState;
    }
    public PieceStatus[][] getMatrix() {
        return playground.getMatrix().getGameMatrix();
    }
    public Player getActivePlayer() {
        return playground.getActivePlayer();
    }
    public Player getPlayerByColor(PieceStatus color) {
        return playground.getPlayerFromColor(color);
    }
    public Player getWinner() {
        return winner;
    }
    public Phase getCurrentPhase() {
        return playground.getCurrentPhase();
    }
    public Position getPrevPosition() {
        return prevPosition;
    }

    /**
     * Verarbeitet den aktuellen Input und ruft die jeweiligen Input-Methoden auf.
     */
    public void handleInput() {
        Position selectedPos = PieceAction.getInput();

        if (selectedPos == null || game.getState() != GAME) return;

        switch (expectedInputState) {
            case PLACING    -> handlePlacing(selectedPos);
            case MOVING     -> handleMoving(selectedPos);
            case REMOVING   -> handleRemoving(selectedPos);
        }

        PieceAction.clearInput();
    }

    /**
     * Setzt den Gewinner und geht in den nächsten Spielstatus über.
     * @param player Spieler der gewinnen soll
     */
    private void handlePlayerWin(Player player) {
        winner = player;
        game.nextState();
    }

    /**
     * Platziert einen Spieler an der selektierten Position.
     * <p>
     * Wenn dies erfolgreich war, wird überprüft ob Steine weggenommen werden können und wenn ja wird die Methode beendet.
     * Wenn keine Steine entfernt werden können, wird der Spieler gewechselt und der expectedInputState verändert, wenn sich die Phase des playgrounds zu MOVING ändert.
     * @param selectedPos Ausgewählte Position
     */
    private void handlePlacing(Position selectedPos) {
        boolean result = playground.setPiece(selectedPos);

        if (!result || arePiecesRemovable()) return;

        if (playground.getCurrentPhase() == Phase.PLACING) {
            playground.switchPlayer();
            return;
        }

        expectedInputState = InputState.MOVING;
        switchPlayerIfPlayerCanMove();
    }

    /**
     * Bewegt einen Stein von einer selektierenden Position zu einer anderen.
     * <p>
     * Wenn ein Stein der gleichen Farbe ausgewählt wird, dann wird diese in prevPosition gespeichert.
     * Wenn es eine prevPosition gibt und das selektierte Feld ein anders ist, wird versucht den Stein zu verschieben.
     * <p>
     * Falls der Stein verschoben werden konnte, wird geschaut, ob ein Stein entfernt werden kann und wenn ja, wird die Methode beendet.
     * Falls es kein Stein zum Entfernen gibt, wird der Spieler gewechselt, sofern der Gegner sich bewegen kann, ansonsten gewinnt der aktuelle Spieler.
     * @param selectedPos Ausgewählte Position
     */
    private void handleMoving(Position selectedPos) {
        boolean result = false;

        if (getActivePlayer().getPieceColor() == playground.getMatrix().getPieceStatus(selectedPos)) {
            prevPosition = selectedPos;
        } else if (prevPosition != null && prevPosition != selectedPos) {
            result = playground.movePiece(prevPosition, selectedPos);
            if (result) prevPosition = null;
        }

        if (!result || arePiecesRemovable()) return;

        switchPlayerIfPlayerCanMove();
    }

    /**
     * Entfernt ein Stein an der gegebenen Position.
     * <p>
     * Versucht einen Stein zu entfernen und wenn diese möglich war, wird piecesToRemove um eins reduziert.
     * Falls der Gegner anschließend weniger als 3 Steine hat, gewinnt der aktuelle Spieler und die Methode wird beendet.
     * Wenn alle möglichen Steine entfernt wurden, wird expectedInputState auf die jeweilige Phase zurückgesetzt und der Player gewechselt, sofern der Gegner sich bewegen kann, ansonsten gewinnt der aktuelle Spieler.
     * @param selectedPos Ausgewählte Position
     */
    private void handleRemoving(Position selectedPos) {
        boolean isColorOfEnemy = playground.getMatrix().getPieceStatus(selectedPos) == getInactivePlayer().getPieceColor();

        if (piecesToRemove > 0 && isColorOfEnemy && playground.removePiece(selectedPos)) piecesToRemove--;

        if (getInactivePlayer().getAvailablePieces() < 3) {
            handlePlayerWin(getActivePlayer());
            return;
        }

        if (piecesToRemove == 0) {
            expectedInputState = playground.getCurrentPhase() == Phase.PLACING ? InputState.PLACING : InputState.MOVING;
            if(expectedInputState == InputState.PLACING){
                playground.switchPlayer();
            } else {
                switchPlayerIfPlayerCanMove();
            }
        }
    }

    /**
     * Überprüft ob Steine eines Players entfernt werden können.
     * Wenn Steine entfernt werden können, wird der Status auf REMOVING gesetzt.
     * @return ob Steine entfernt werden können
     */
    private boolean arePiecesRemovable(){
        piecesToRemove = playground.getNewMills();
        if (piecesToRemove > 0 && !playground.allPositionsPartOfMill(getInactivePlayer())) {
            expectedInputState = InputState.REMOVING;
            return true;
        }
        return false;
    }

    /**
     * Überprüft ob gegnerischer Spieler sich bewegen kann.
     * Wenn ja, dann wird der Spieler gewechselt, ansonsten gewinnt der aktive Spieler.
     */
    private void switchPlayerIfPlayerCanMove() {
        if (!playground.canPlayerMove(getInactivePlayer())) {
            handlePlayerWin(getActivePlayer());
        } else {
            playground.switchPlayer();
        }
    }

    /**
     * Gibt das Player-Objekt des gegnerischen Spielers zurück
     * @return gegnerischer Spieler
     */
    public Player getInactivePlayer() {
        return playground.getPlayerFromColor(getActivePlayer().getPieceColor() == PieceStatus.WHITE ? PieceStatus.BLACK : PieceStatus.WHITE);
    }
}
