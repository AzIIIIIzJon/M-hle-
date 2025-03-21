package ui.helper;

import javax.swing.*;
import java.awt.*;

public abstract class ButtonGridPanel extends JPanel {
    private static final Color BACKGROUND = Color.WHITE;
    private static final Color BUTTON_COLOR = new Color(255, 240, 193);
    private final JPanel gridPanel;
    private final GridBagConstraints gbc;

    protected ButtonGridPanel(int height) {
        setLayout(new BorderLayout(-2, -2));

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(BACKGROUND);
        leftPanel.setPreferredSize(new Dimension(1000 / 7, height));
        add(leftPanel, BorderLayout.LINE_START);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(BACKGROUND);
        rightPanel.setPreferredSize(new Dimension(1000 / 7, height));
        add(rightPanel, BorderLayout.LINE_END);

        gridPanel = new JPanel();
        gridPanel.setLayout(new GridBagLayout());
        gridPanel.setBackground(BACKGROUND);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;

        add(gridPanel, BorderLayout.CENTER);
    }

    public JPanel getGridPanel() {
        return gridPanel;
    }

    protected void createGridPanel(int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND);
        createGridComponent(x, y, width, height, panel);
    }

    protected void createGridButton(int x, int y, int width, int height, JButton button) {
        button.setBackground(BUTTON_COLOR);
        button.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
        createGridComponent(x, y, width, height, button);
    }

    private void createGridComponent(int x, int y, int width, int height, JComponent component) {
        component.setPreferredSize(new Dimension(width, height));

        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = width;
        gbc.weighty = height;
        gbc.gridwidth = width;
        gbc.gridheight = height;

        gridPanel.add(component, gbc);
    }
}
