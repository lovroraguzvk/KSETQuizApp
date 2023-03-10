import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class SetUpForm extends JFrame {
    private JTextField quizNameTextField;
    private JButton uploadFolderButton;
    private JButton uploadFileButton;
    private JLabel folderLabel;
    private JLabel fileLabel;
    private JPanel mainPanel;
    private JButton cancelButton;
    private JButton OKButton;
    private JLabel welcome;
    private JLabel instructionsLabel;
    private JLabel folderError;
    private JLabel fileError;
    private File folderFile;
    private File excelFile;

    public SetUpForm() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(560, 200);
        setLocationRelativeTo(null);
        setVisible(true);

        instructionsLabel.setText("""
                <html>
                Please enter the name of the Quiz below.<br/>
                You have to have a folder of pictures ready that will serve as questions<br/>
                (best practice is to make them in Powerpoint and export as pictures).<br/>
                Also you can either upload scores below or create a new .xlsx file that<br/>
                will be served as a score file holder.<br/>
                </html>
                """);

        setTitle("Set up Quiz");
        setContentPane(mainPanel);
        pack();

        uploadFolderButton.addActionListener(e -> {
            JFileChooser folderChooser = new JFileChooser(new File(System.getProperty("user.dir")));
            folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = folderChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                folderFile = folderChooser.getSelectedFile();
                folderLabel.setText("Folder Selected: " + folderFile.getName());
            }
            folderError.setText("");
        });
        uploadFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("EXCEL FILES", "xlsx"));
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                excelFile = fileChooser.getSelectedFile();
                fileLabel.setText("File Selected: " + excelFile.getName());
            }
            fileError.setText("");
        });
        OKButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            try {
                if (folderFile != null && excelFile != null) {
                    this.setVisible(false);
                    new Scoreboard(quizNameTextField.getText(), folderFile, excelFile).setVisible(true);
                }

                if (folderFile == null) {
                    folderError.setForeground(Color.RED);
                    folderError.setText("Please select a folder.");
                } else {
                    folderError.setText("");
                }
                if (excelFile == null) {
                    fileError.setForeground(Color.RED);
                    fileError.setText("Please select a file.");
                } else {
                    fileError.setText("");
                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }));
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 8, new Insets(5, 5, 5, 5), -1, -1));
        mainPanel.setPreferredSize(new Dimension(660, 270));
        final JLabel label1 = new JLabel();
        label1.setText("Quiz name:");
        mainPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        quizNameTextField = new JTextField();
        mainPanel.add(quizNameTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 7, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Folder containing questions:");
        mainPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Excel file name for score saving:");
        mainPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        uploadFolderButton = new JButton();
        uploadFolderButton.setText("Upload folder");
        mainPanel.add(uploadFolderButton, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        uploadFileButton = new JButton();
        uploadFileButton.setText("Upload file");
        mainPanel.add(uploadFileButton, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        folderLabel = new JLabel();
        folderLabel.setText("None selected");
        mainPanel.add(folderLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileLabel = new JLabel();
        fileLabel.setText("None selected");
        mainPanel.add(fileLabel, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        mainPanel.add(cancelButton, new com.intellij.uiDesigner.core.GridConstraints(5, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        OKButton = new JButton();
        OKButton.setText("OK");
        mainPanel.add(OKButton, new com.intellij.uiDesigner.core.GridConstraints(5, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        welcome = new JLabel();
        welcome.setText("Welcome to the Quiz App 1.0");
        mainPanel.add(welcome, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 8, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        instructionsLabel = new JLabel();
        instructionsLabel.setText("Please enter the name of the Quiz below. You have to have a folder of pictures as questions ready (best practice is to make them in Powerpoint and export as pictures)");
        mainPanel.add(instructionsLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 6, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        folderError = new JLabel();
        folderError.setText("");
        mainPanel.add(folderError, new com.intellij.uiDesigner.core.GridConstraints(3, 4, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileError = new JLabel();
        fileError.setText("");
        mainPanel.add(fileError, new com.intellij.uiDesigner.core.GridConstraints(4, 4, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        label1.setLabelFor(quizNameTextField);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
