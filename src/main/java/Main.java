import org.apache.poi.EmptyFileException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new SetUpForm();
            frame.setVisible(true);
        });
    }

    /**
     * Static function that spawns a frame on a specific screen
     * @param screen which screen to show on
     * @param frame what Frame to show on it
     */
    public static void showOnScreen( int screen, JFrame frame ) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        GraphicsDevice[] gs = ge.getScreenDevices();

        if(screen > -1 && screen < gs.length) {
            gs[screen].setFullScreenWindow(frame);
        }else if(gs.length > 0) {
            gs[0].setFullScreenWindow(frame);
        }else {
            throw new RuntimeException("No Screens Found");
        }
    }
}
