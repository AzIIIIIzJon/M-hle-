package ui;

import javax.swing.*;
import java.awt.*;

//TODO: GIVEN (AND ALL OF THE OTHER UI CLASSES)
public class UI extends JFrame {
    private static final UI instance = new UI();

    public UI() throws HeadlessException {
        if (GraphicsEnvironment.isHeadless())
            throw new HeadlessException();

        Dimension size = new Dimension(470 + 2000/7, 720);

        setTitle("Mühle");
        setSize(size);
        setMinimumSize(new Dimension(350 + 2000/7, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);

        add(Panel.getInstance());
        setLocationRelativeTo(null);

        setVisible(true);
    }

    public static UI getInstance() {
        return instance;
    }

}
