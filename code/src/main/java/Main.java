import org.apache.poi.EmptyFileException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Scoreboard sc = null;
            try {
                sc = new Scoreboard();
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println();

            for(int i = 1; new File("Podaci" + i + ".xlsx").exists(); i++) {
                sc.fileNum = i + 1;
            }
        });
    }

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
