package ui;

import gameplay.Game;
import ui.helper.ButtonGridPanel;

import javax.swing.*;
import java.awt.*;


public class CloseRestartPanel extends ButtonGridPanel {

    public CloseRestartPanel(int height) {
        super(height);
        setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));

        JButton restartBtn = new JButton("Restart");
        JButton closeBtn   = new JButton("Close");

        restartBtn.addActionListener(e -> Game.getInstance().restart());
        closeBtn.addActionListener(  e -> Game.getInstance().gameOver());

        createGridPanel(0, 0, 28, 1);
        createGridPanel(0, 1, 6, 1);

        createGridButton(6, 1, 5, 1, restartBtn);
        createGridPanel(11, 1, 6, 1);
        createGridButton(17, 1, 5, 1, closeBtn);

        createGridPanel(22, 1, 6, 1);
        createGridPanel(0, 2, 28, 1);
    }
}
