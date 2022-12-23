import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SetUpForm extends JFrame{
    private JTextField quizNameTextField;
    private JButton uploadFolderButton;
    private JButton uploadFileButton;
    private JLabel folderLabel;
    private JLabel fileLabel;
    private JPanel mainPanel;
    private JButton cancelButton;
    private JButton OKButton;
    private File folderFile;
    private File excelFile;

    public SetUpForm() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(560, 200);
        setLocationRelativeTo(null);
        setVisible(true);

        setTitle("Set up Quiz");
        setContentPane(mainPanel);
        pack();

        uploadFolderButton.addActionListener(e -> {
            JFileChooser folderChooser = new JFileChooser(new File(System.getProperty("user.dir")));
            folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = folderChooser.showOpenDialog(this);
            if(option == JFileChooser.APPROVE_OPTION){
                folderFile = folderChooser.getSelectedFile();
                folderLabel.setText("Folder Selected: " + folderFile.getName());
            }
        });
        uploadFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("EXCEL FILES", "xlsx"));
            int option = fileChooser.showOpenDialog(this);
            if(option == JFileChooser.APPROVE_OPTION){
                excelFile = fileChooser.getSelectedFile();
                fileLabel.setText("File Selected: " + excelFile.getName());
            }
        });
        OKButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            try {
                this.setVisible(false);
                new Scoreboard(quizNameTextField.getText(), folderFile, excelFile).setVisible(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new SetUpForm();
            frame.setVisible(true);
        });
    }
}
