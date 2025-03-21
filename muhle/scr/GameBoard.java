package ui.gameboard;

import enums.PieceStatus;
import enums.Position;
import gameplay.Gameplay;
import playground.Matrix;
import ui.gameboard.LineSegment.Orientation;
import ui.gameboard.PieceButton.Direction;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameBoard extends JPanel {

    private static final Color BOARD_COLOR = new Color(255, 240, 193);
    private static final Color BOARD_BOARDER_COLOR = Color.WHITE;
    private static final Font NORMAL = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
    private final JComponent[][] components = new JComponent[SIZE][SIZE];
    private transient Gameplay gp;
    private static final int SIZE = 7;

    public GameBoard() {
        this.setBackground(BOARD_BOARDER_COLOR);

        GridLayout gl = new GridLayout(SIZE, SIZE);
        this.setLayout(gl);

        buildMatrix();
    }

    public void setGameplay(Gameplay gp) {
        this.gp = gp;
    }

    private void buildMatrix() {
        for (int y = 0; y < (SIZE / 2) + 1; y++) {
            for (int x = 0; x < (SIZE / 2) + 1; x++) {
                if (((x == 3 || x == y) && y != 3) || (x != 3 && y == 3)) {
                    Set<Direction> leftUpperCornerDirections = new HashSet<>();

                    if (x == y) leftUpperCornerDirections.addAll(List.of(Direction.RIGHT, Direction.BOTTOM));

                    if (x == (SIZE / 2)) {
                        leftUpperCornerDirections.addAll(List.of(Direction.LEFT, Direction.RIGHT));
                        if (y != 0) leftUpperCornerDirections.add(Direction.TOP);
                        if (y != 2) leftUpperCornerDirections.add(Direction.BOTTOM);
                    }

                    if (y == (SIZE / 2)) {
                        leftUpperCornerDirections.addAll(List.of(Direction.TOP, Direction.BOTTOM));
                        if (x != 0) leftUpperCornerDirections.add(Direction.LEFT);
                        if (x != 2) leftUpperCornerDirections.add(Direction.RIGHT);
                    }
                    // Top Left
                    components[y][x] = new PieceButton(leftUpperCornerDirections, Position.valueOf(String.format("%s%s", Character.toString('A' + x), 7 - y)));
                    // Bottom Right
                    components[SIZE - y - 1][SIZE - x - 1] = new PieceButton(rotateLines(leftUpperCornerDirections, 2), Position.valueOf(String.format("%s%s", Character.toString('G' - x), 1 + y)));
                    if (x != (SIZE / 2) && y != (SIZE / 2)){
                        // Top Right
                        components[y][SIZE - x - 1] = new PieceButton(rotateLines(leftUpperCornerDirections, 1), Position.valueOf(String.format("%s%s", Character.toString('G' - x), 7 - y)));
                        // Bottom Left
                        components[SIZE - y - 1][x] = new PieceButton(rotateLines(leftUpperCornerDirections, 3), Position.valueOf(String.format("%s%s", Character.toString('A' + x), 1 + y)));
                    }
                } else {
                    Orientation orientation = switch (y) {
                        case 0 -> Orientation.HORIZONTAL;
                        case 1 -> x == 0 ? Orientation.VERTICAL : Orientation.HORIZONTAL;
                        case 2 -> Orientation.VERTICAL;
                        case 3 -> null;
                        default -> throw new IndexOutOfBoundsException(String.format("Y: %s should not be here!", y));
                    };

                    if (orientation == null) {
                        components[y][x] = new JLabel();
                    } else {
                        components[y][x] = new LineSegment(orientation);
                        components[y][SIZE - x - 1] = new LineSegment(orientation);
                        components[SIZE - y - 1][x] = new LineSegment(orientation);
                        components[SIZE - y - 1][SIZE - x - 1] = new LineSegment(orientation);
                    }
                }
            }
        }

        for (JComponent[] yComponents : components) {
            for (JComponent component : yComponents) {
                this.add(component);
            }
        }
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);

        // Prevent rendering if gameplay is null
        if (gp == null) return;

        PieceStatus[][] matrix = gp.getMatrix();

        // Prevent rendering if matrix is null or length is not the same
        if (matrix == null || matrix.length != components.length){
            System.err.println("There should always be a created matrix with the correct format inside the class 'Matrix'!");
            return;
        }

        for (int i = 0; i < matrix.length; i++) {
            if(components[i].length != matrix[i].length){
                System.err.println("There should always be a correctly formatted matrix inside the class 'Matrix'!");
                return;
            }
        }

        for (int y = 0; y < gp.getMatrix().length; y++) {
            for (int x = 0; x < gp.getMatrix()[y].length; x++) {
                if (components[y][x] instanceof PieceButton button) {
                    button.setPieceStatus(gp.getMatrix()[y][x]);
                    if(button.getGameplay() == null) button.setGameplay(gp);
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = this.getWidth() / 7;
        int y = this.getHeight() / 7;

        drawBoard(g, x, y);
        drawNumbers(g, x, y);
        drawLetters(g, x, y);
    }

    private void drawBoard(Graphics g, int x, int y) {
        g.setColor(BOARD_COLOR);
        g.fillRect(x / 2, y / 2, this.getWidth() - x, this.getHeight() - y);
    }

    private void drawNumbers(Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        g.setFont(NORMAL);
        for (int i = 0; i < 7; i++) {
            g.drawString(String.valueOf(7 - i), x / 4, y / 2 + y * i + ((int) Math.round(y * 0.1) / 2));
        }
    }

    private void drawLetters(Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        g.setFont(NORMAL);
        for (int i = 0; i < 7; i++) {
            g.drawString(Character.toString('A' + i), x / 2 + x * i - ((int) Math.round(x * 0.05) / 2), this.getHeight() - y / 4);
        }
    }

    private Set<Direction> rotateLines(Set<Direction> directions, int degree) {
        Direction[] rotation = {Direction.TOP, Direction.RIGHT, Direction.BOTTOM, Direction.LEFT};
        Set<Direction> newDirections = new HashSet<>();

        for (Direction direction : directions) {
            int index = 0;

            for (; index < rotation.length; index++) {
                if (rotation[index] == direction) break;
            }

            index += degree;
            while (index >= 4) {
                index -= 4;
            }
            newDirections.add(rotation[index]);
        }

        return newDirections;
    }

}
