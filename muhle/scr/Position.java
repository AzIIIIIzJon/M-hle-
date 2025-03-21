package enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Speichert alle möglichen Positionen eines Mühle-Spiels
 */
public enum Position {
    A1,         A4,         A7,
        B2,     B4,     B6,
            C3, C4, C5,
    D1, D2, D3,     D5, D6, D7,
            E3, E4, E5,
        F2,     F4,     F6,
    G1,         G4,         G7;

    private final int x;
    private final int y;

    /**
     * Erzeugt Position und generiert Koordinaten
     */
    Position(){
        y = '7' - name().charAt(1);
        x = name().charAt(0) - 'A';
    }

    /**
     * Gibt die Position anhand String-Names zurück
     * @param name Position Name
     * @return Position, wenn es Position mit dem Namen gibt, ansonsten null
     */
    public static Position get(String name) {
        Position[] positions = Position.values();
        for (Position position : positions) {
            if (Objects.equals(position.name(), name)) {
                return position;
            }
        }
        return null;
    }

    /**
     * Gibt eine Position anhand der Koordinaten zurück.
     * Dafür wird ein String gebaut und dieses für die Methode Position get(String name) genutzt.
     * @param y 7(0) - 1(6) - Koordinate
     * @param x A(0) - G(6) - Koordinate
     * @return Position, wenn es eine Position an den Koordinaten gibt, ansonsten null
     */
    public static Position get(int y, int x){
        return get(String.format("%s%s", Character.toString('A' + x), 7 - y));
    }

    public int y(){ return y;}
    public int x(){ return x;}

    /**
     * Überprüft, ob gegebene Position Nachbar ist zu einer anderen Position.
     * @param position Position für den Nachbarsvergleich
     * @return ob beide Positionen Nachbarn sind
     */
    public boolean isPositionNeighbour(Position position) {
        int pX = position.x();
        int pY = position.y();

        if ((x == pX) == (y == pY)) return false;

        return y == pY ? areCoordinatesNeighbours(x, pX, y, true) : areCoordinatesNeighbours(y, pY, x, false);
    }

    /**
     * Gibt alles Positionen zurück, welche von einer Position Nachbarn sind.
     * @return alle Nachbarn einer Position
     */
    public Position[] getNeighbours() {
        List<Position> positions = new ArrayList<>();

        for (int iX = x - 3; iX <= x + 3; iX++) {
            Position position = get(y, iX);
            if (position == null) continue;
            if (areCoordinatesNeighbours(x, iX, y, true)) positions.add(position);
        }

        for (int iY = y - 3; iY <= y + 3; iY++) {
            Position position = get(iY, x);
            if (position == null) continue;
            if (areCoordinatesNeighbours(y, iY, x, false)) positions.add(position);
        }

        return positions.toArray(new Position[0]);
    }

    /**
     * Überprüft, ob zwei Punkte in einer 1-dimensionalen Ebene Nachbarn sind.
     * @param p1 Erster Punkt
     * @param p2 Zweiter Punkt
     * @param sharedPos Geteilter Punkt in der zweiten Dimension
     * @param yDimension Ist die zweite Dimension die y Dimension.
     * @return ob beide Punkte Nachbarn sind
     */
    private boolean areCoordinatesNeighbours(int p1, int p2, int sharedPos, boolean yDimension){
        if (p1 > p2) {
            int tmp = p2;
            p2 = p1;
            p1 = tmp;
        }

        if ((p1 == 2 && p2 == 4)) return false;

        int piecePositionsCount = 0;

        for (int i = p1 + 1; i < 7; i++) {
            Position position = yDimension ? get(sharedPos, i) : get(i, sharedPos);

            if (position != null) piecePositionsCount++;
            if (i == p2) return piecePositionsCount == 1;
        }

        return false;
    }
}