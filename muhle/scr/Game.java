package gameplay;

import enums.PieceStatus;
import ui.Panel;
import ui.UI;
import ui.helper.PieceAction;

public class Game {

    /**
     * Gibt die drei Spielzustände an.
     */
    public enum State {START, GAME, END}

    /**
     * Gibt an, ob die aktuelle Spielsession aktiv ist.
     */
    private boolean sessionActive;
    /**
     * Instanz der Gameplay-Klasse
     */
    private Gameplay gameplay;
    /**
     * Gibt den Spielzustand an.
     */
    private State state = State.START;
    /**
     * Gibt an, ob das aktuelle Spiel vorbei ist.
     */
    private boolean gameDone = true;
    /**
     * Speichert die Startfarbe
     */
    private PieceStatus startColor;

    /**
     * Instanz des Game-Objekts, weil dieses als Singleton implementiert ist.
     */
    private static Game instance;

    /**
     * Gibt Instanz des Game-Objekts zurück. Falls noch kein Game erzeugt wurde, wird ein neues erstellt.
     * @return Instanz des Game-Objekts
     */
    public static Game getInstance() {
        if(instance == null) new Game();
        return instance;
    }

    /**
     * Konstruktor für die Game-Klasse.
     * Aktiviert aktuelle Session, speichert die Game-Instanz, zeichnet das Spielfeld und startet den gameLoop.
     */
    private Game() {
        sessionActive = true;
        instance = this;

        draw();
        gameLoop();
    }

    /**
     * GameLoop welches Updates ausführt und Spiel zeichnet, bis die Session nicht mehr aktiv ist.
     */
    public void gameLoop() {
        while (sessionActive) {
            update();
            draw();
        }
        System.exit(0);
    }

    /**
     * Managed Spielupdates.
     * Wenn das Spiel gestartet wurde, dann wird das alte Gameplay gelöscht.
     * Wenn die Spielphase aktiv ist, wird ein neues Gameplay erzeugt falls es noch keins gibt und anschließend geprüft ob der Spieler einen Input getätigt hat.
     */
    private void update() {
        if (state == State.START && gameDone) {
            gameplay = null;
            Panel.getInstance().setGameplay(null);
            gameDone = false;
        }

        if (state == State.GAME) {
            if (gameplay == null) {
                if (startColor != null) gameplay = new Gameplay(startColor);
                else gameplay = new Gameplay();

                Panel.getInstance().setGameplay(gameplay);
                PieceAction.clearInput();
            }
            gameplay.handleInput();
        }
    }

    public State getState() {
        return state;
    }

    /**
     * Startet das Spiel neu.
     */
    public void restart() {
        this.state = State.START;
        this.startColor = null;
        gameDone = true;
    }

    /**
     * Startet die Spielphase.
     */
    public void startGame() {
        state = State.GAME;
    }

    /**
     * Startet die Spielphase mit einer Startfarbe.
     * @param startColor Startfarbe
     */
    public void startGame(PieceStatus startColor) {
        this.startColor = startColor;
        state = State.GAME;
    }

    /**
     * Wechselt zum nächsten Spielzustand.
     */
    public void nextState() {
        switch (state) {
            case START -> startGame();
            case GAME -> state = State.END;
            case END -> restart();
        }
    }

    /**
     * Beendet die aktuelle Spiel-Session.
     */
    public void gameOver() {
        sessionActive = false;
    }

    /**
     * Zeichnet das Spielfeld.
     */
    private void draw() {
        UI.getInstance().repaint();
    }
}
