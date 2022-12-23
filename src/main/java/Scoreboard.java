import org.apache.poi.EmptyFileException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Scoreboard extends JFrame{

    final String slidePath = new File("slides").getAbsolutePath().concat("\\");
    public static final int NUM_OF_SLIDES = 20;
    private static final String SLIDE_FILE_EXTENSION = ".PNG";
    private static final String DATA_FILE_NAME = "ExampleData";
    private static final int QUESTIONS_BOX_WIDTH = 800;

    JPanel panel;
    JPanel scoresBox;
    JPanel scores;
    JScrollPane scoresScroll;
    JPanel header;
    JPanel questionsBox;
    JPanel questions;
    JPanel center;
    JLabel slideLabel;
    JLabel lbName = new JLabel("Ime");
    JLabel lbScore = new JLabel("Bodovi");
    JLabel lbMail = new JLabel("Mail");
    JButton btnOk = new JButton("OK");
    JButton btnCancel = new JButton("Otkaži");
    JTextField txName = new JTextField();
    JTextField txMail = new JTextField();
    JTextField txScore = new JTextField();

    JPanel inputsAndLabels = new JPanel();
    JPanel inputsAndLabelsBox = new JPanel(); // Components
    Map<Integer, Set<String>> mapOfScores = new TreeMap<>(Collections.reverseOrder());
    LinkedList<Integer> lastFive = new LinkedList<>();
    int lastFiveCounter = 0;
    boolean devModeOn = false;
    boolean scorePanelOn = false;

    public List<Info> infoList = new ArrayList<>();

    int fileNum = 1;
    int numOfPeople = 0;

    private String quizTitle;
    private File folderWithSlides;
    private File scoreFile;

    public Scoreboard(String quizTitle, File folderWithSlides, File scoreFile) throws IOException {

        this.quizTitle = quizTitle;
        this.folderWithSlides = folderWithSlides;
        this.scoreFile = scoreFile;

        createHeader();
        createScoresBox();
        createQuestionsBox();
        createInputBox();
        createCenterPanel();

        createContentPaneAndFill();
        inputsAndLabelsBox.setVisible(false);

        buttonsAndActions();

        try{
            loadFromExcel();
        } catch(EmptyFileException e) {
            System.out.println("Empty file");
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        loadQuestions();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }


    public Scoreboard() throws IOException {

    }

    private void buttonsAndActions() {
        //107,219,154
        Action nextSlideAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Random rand = new Random();
                int nextSlide = rand.nextInt(NUM_OF_SLIDES) + 1;

                while(lastFive.contains(nextSlide)) nextSlide = rand.nextInt(NUM_OF_SLIDES) + 1;
                switch (lastFiveCounter) {
                    case 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 -> {lastFive.add(nextSlide);lastFiveCounter++;}
                    case 10 -> {lastFive.removeFirst();lastFive.add(nextSlide);}
                }
                System.out.println(lastFive);

                questionsBox.remove(slideLabel);

                BufferedImage slide = null;
                try {
                    slide = ImageIO.read(new File(folderWithSlides.getAbsolutePath() + "\\Slide" + nextSlide + SLIDE_FILE_EXTENSION));
                    slideLabel = new JLabel(new ImageIcon(slide.getScaledInstance((QUESTIONS_BOX_WIDTH - 125), (int) ((QUESTIONS_BOX_WIDTH - (float)3/4*125) * ((float)3/4)),Image.SCALE_SMOOTH)));
                    slideLabel.setBorder(BorderFactory.createMatteBorder(3,3,3,3,Color.BLACK));
                } catch (IOException ioException) {
                    System.out.println("No more slides!");;
                }

                questionsBox.add(slideLabel);
                refresh();

            }
        };

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"),
                "next_slide");
        panel.getActionMap().put("next_slide",
                nextSlideAction);

        Action devModeAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(devModeOn) {
                    updateScores();
                } else {
                    developerMode();
                }
                devModeOn = !devModeOn;
            }
        };

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK),
                "dev_mode");
        panel.getActionMap().put("dev_mode",
                devModeAction);

        Action resetAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try {
                    resetInfo();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (InvalidFormatException ex) {
                    throw new RuntimeException(ex);
                }
                refresh();
            }
        };

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK),
                "reset");
        panel.getActionMap().put("reset",
                resetAction);

        Action enterScoreAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                scoresBox.setVisible(scorePanelOn);
                questionsBox.setVisible(scorePanelOn);
                inputsAndLabelsBox.setVisible(!scorePanelOn);

                scorePanelOn = !scorePanelOn;
            }
        };

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK),
                "score");
        panel.getActionMap().put("score",
                enterScoreAction);
    }

    private void createInputBox() {
        inputsAndLabelsBox.add(inputsAndLabels, BorderLayout.NORTH);

        inputsAndLabels.setLayout(new GridLayout(0, 2, 10, 5));
        inputsAndLabels.setPreferredSize(new Dimension(500,100));
        inputsAndLabelsBox.setBackground(new Color(94, 200, 242));
        inputsAndLabels.setBackground(new Color(94, 200, 242));

        lbMail.setHorizontalAlignment(SwingConstants.RIGHT);
        lbName.setHorizontalAlignment(SwingConstants.RIGHT);
        lbScore.setHorizontalAlignment(SwingConstants.RIGHT);
        lbMail.setFont(new Font("Arial", Font.PLAIN, 20));
        lbName.setFont(new Font("Arial", Font.PLAIN, 20));
        txScore.setFont(new Font("Arial", Font.PLAIN, 20));
        lbScore.setFont(new Font("Arial", Font.PLAIN, 20));
        txName.setFont(new Font("Arial", Font.PLAIN, 20));
        txMail.setFont(new Font("Arial", Font.PLAIN, 20));

        inputsAndLabels.add(lbName);
        inputsAndLabels.add(txName);
        inputsAndLabels.add(lbMail);
        inputsAndLabels.add(txMail);
        inputsAndLabels.add(lbScore);
        inputsAndLabels.add(txScore);
        inputsAndLabels.add(txScore);
        inputsAndLabels.add(btnOk);
        inputsAndLabels.add(btnCancel);

        btnOk.addActionListener((e) -> {
            saveInfo(txName.getText(), txMail.getText(), Double.parseDouble(txScore.getText()));
            try {
                saveToExcel();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            scoresBox.setVisible(scorePanelOn);
            questionsBox.setVisible(scorePanelOn);
            inputsAndLabelsBox.setVisible(!scorePanelOn);

            scorePanelOn = !scorePanelOn;
            updateScores();
            refresh();
            inputsAndLabelsBox.setVisible(false);

        });
    }

    private void createContentPaneAndFill() {
        panel = (JPanel) getContentPane();

        panel.setLayout(new BorderLayout(10,10));
        panel.setBackground(new Color(94, 200, 242));

        panel.add(center, BorderLayout.CENTER);
        panel.add(header, BorderLayout.NORTH);
    }

    private void createCenterPanel() {
        center = new JPanel();

        center.setLayout(new BorderLayout());
        center.setBorder(BorderFactory.createEmptyBorder(40, 10, 60, 60));
        center.setBackground(new Color(94, 200, 242));

        center.add(scoresBox, BorderLayout.CENTER);
        center.add(questionsBox, BorderLayout.EAST);
        center.add(inputsAndLabelsBox, BorderLayout.NORTH);
    }

    private void createQuestionsBox() throws IOException {
        System.out.println(folderWithSlides.getAbsolutePath() + "\\Slide" + "1" + SLIDE_FILE_EXTENSION);
        BufferedImage slide = ImageIO.read(new File(folderWithSlides.getAbsolutePath() + "\\Slide" + "1" + SLIDE_FILE_EXTENSION));
        slideLabel = new JLabel(new ImageIcon(slide.getScaledInstance((QUESTIONS_BOX_WIDTH - 125), (int) ((QUESTIONS_BOX_WIDTH - (float)3/4*125) * ((float)3/4)),Image.SCALE_SMOOTH)));
        slideLabel.setBorder(BorderFactory.createMatteBorder(3,3,3,3,Color.BLACK));

        questions = new JPanel();

        questions.setBorder(BorderFactory.createEmptyBorder(20,50,20,0));
        questions.setBackground(new Color(176, 168, 158));
        questions.setLayout(new GridLayout(0,1));

        questionsBox = new JPanel();
        questionsBox.setBackground(new Color(94, 200, 242));
        questionsBox.setPreferredSize(new Dimension((QUESTIONS_BOX_WIDTH - 50), (int) ((QUESTIONS_BOX_WIDTH - (float)3/4*50) * ((float)3/4))));
        questionsBox.add(slideLabel);
    }

    private void createScoresBox() {
        scoresBox = new JPanel();
        scores = new JPanel();
        scoresScroll = new JScrollPane(scores);

        scoresBox.setLayout(new BorderLayout());
        scores.setLayout(new BoxLayout(scores, BoxLayout.Y_AXIS));

        scoresBox.setBorder(BorderFactory.createEmptyBorder(20,70,20,40));
        scores.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));

        JLabel scoresTitle = new JLabel("Tablica uspješnosti:");
        scoresTitle.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
        scoresTitle.setFont(new Font("Arial", Font.ITALIC, 35));

        scoresBox.add(scoresScroll, BorderLayout.CENTER);
        scoresBox.add(scoresTitle, BorderLayout.NORTH);

        scoresScroll.setBorder(BorderFactory.createEmptyBorder());
        scoresBox.setBackground(new Color(94, 200, 242));
        scores.setBackground(new Color(94, 200, 242));
    }

    private void createHeader() throws IOException {
        header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBorder(BorderFactory.createMatteBorder(0,0,5,0,Color.BLACK));

        BufferedImage myPicture = ImageIO.read(new File("D:\\MyIdeaProjects\\QuizzApp\\header.jpg"));
        JLabel picLabel = new JLabel(new ImageIcon(myPicture.getScaledInstance(120,120,Image.SCALE_FAST)));
        picLabel.setBorder(BorderFactory.createMatteBorder(5,5,5,5,Color.BLACK));

        JLabel title = new JLabel(quizTitle);
        title.setBorder(BorderFactory.createEmptyBorder(0,100,0,0));
        title.setFont(new Font("Cambria", Font.BOLD, 75));

        header.add(picLabel);
        header.add(title);
        header.setBackground(new Color(255, 140, 0));
    }

    private void saveInfo(String name, String mail, Double score) {
        infoList.add(new Info(name, mail, score));
        infoList.sort(Comparator.comparing(Info::getScore).reversed().thenComparing(Info::getName));
        numOfPeople++;
        updateScores();
    }

    private void saveToExcel() throws IOException{
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Scores");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Ime");
        header.createCell(1).setCellValue("Mail");
        header.createCell(2).setCellValue("Bodovi");

        int i = 1;
        for (Info info : infoList){
            Row row = sheet.createRow(i);

            row.createCell(0).setCellValue(info.getName());
            row.createCell(1).setCellValue(info.getMail());
            row.createCell(2).setCellValue(info.getScore());
            i++;
        }

        try (FileOutputStream outputStream = new FileOutputStream(scoreFile)) {
            workbook.write(outputStream);
        }
    }

    private void loadFromExcel() throws IOException, InvalidFormatException {
        XSSFWorkbook wb = new XSSFWorkbook(scoreFile);
        XSSFSheet sheet = wb.getSheetAt(0);

        sheet.forEach((row) -> {
            if(row.getRowNum() != 0){
                saveInfo(row.getCell(0).toString(), row.getCell(1).toString(), Double.parseDouble(row.getCell(2).toString()));
                updateScores();
            }
        });

/*
        int i = 1;
        for (Info info : infoList){
            Row row = sheet.createRow(i);

            row.createCell(0).setCellValue(info.getName());
            row.createCell(1).setCellValue(info.getMail());
            row.createCell(2).setCellValue(info.getScore());
            i++;
        }

        try (FileOutputStream outputStream = new FileOutputStream("F:\\MyIdeaProjects\\QuizzApp\\info\\Podaci" + fileNum + ".xlsx")) {
            workbook.write(outputStream);
        }

 */
    }

    public void enterScore() {

    }

    public void refresh() {
        SwingUtilities.updateComponentTreeUI(this);}

    private void loadQuestions() {
        JLabel question1 = new JLabel("1. Koliko su Vinkovci bolji od ostalih sela?");
        JLabel question2 = new JLabel("2. Lovro je navijac najboljeg kluba, kojeg?");
        JLabel question3 = new JLabel("3. Majoneza ili ketchup?");
        JLabel question4 = new JLabel("4. Tko je sef Motokluba pijandura?");
        JLabel question5 = new JLabel("5. Predsjednik Motokluba pijandura*?");
        JLabel question6 = new JLabel("6. Kako se zovu nove tri članice Videosekcije?");
        JLabel question7 = new JLabel("7. Lorem ipsum dolor sit amet, consectetur adipiscing elit?");
        JLabel question8 = new JLabel("8. Quisque justo turpis, iaculis non maximus efficitur, ultricies ut?");
        JLabel question9 = new JLabel("9. Consectetur elementum sapien in hendrerit?");
        JLabel question10 = new JLabel("10. Egestas a pulvinar at?");

        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        questions.add(question4);
        questions.add(question5);
        questions.add(question6);
        questions.add(question7);
        questions.add(question8);
        questions.add(question9);
        questions.add(question10);

        for(Component question : questions.getComponents()) {
            question.setFont(new Font("Comic Sans MS", Font.BOLD, 40));
            //Cambria
        }
    }

    public void addScore(String name, int score) {
        Set<String> temp = new TreeSet<>();
        temp.add(name);
        mapOfScores.merge(score, temp, (oldValue, newValue) -> {
            oldValue.addAll(newValue);
            return oldValue;
        });

        scores.removeAll();

        AtomicInteger counter = new AtomicInteger(1);
        mapOfScores.forEach((k, v) -> v.forEach((v1) -> {


            JPanel labelPanel = new JPanel();
            labelPanel.setLayout(new FlowLayout());
            //labelPanel.setPreferredSize(new Dimension(1000,80));

            JLabel label = new JLabel(counter + ". " + v1 + " - " + k);
            label.setFont(new Font("Arial", Font.PLAIN, 35));
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setVerticalAlignment(SwingConstants.CENTER);
            labelPanel.setBorder(BorderFactory.createMatteBorder(4,0,0,0, Color.BLACK));
            labelPanel.setBackground(new Color(94, 200, 242));
            label.setPreferredSize(new Dimension(600,100));
            labelPanel.setPreferredSize(new Dimension(700,100));

            if(counter.toString().equals("1")) {
                labelPanel.setBorder(BorderFactory.createMatteBorder(0,0,0,0, Color.BLACK));
                label.setFont(new Font("Arial", Font.BOLD, 35));
            }

            labelPanel.add(label);
            scores.add(labelPanel);
            counter.getAndIncrement();
        }));

        SwingUtilities.updateComponentTreeUI(this);
    }

    public void updateScores() {
        scores.removeAll();
        scoresBox.remove(scoresScroll);

        AtomicInteger counter = new AtomicInteger(1);
        infoList.forEach((info) -> {
            JPanel labelPanel = new JPanel();
            labelPanel.setLayout(new FlowLayout());
            labelPanel.setPreferredSize(new Dimension(1000,80));

            JLabel label = new JLabel(counter + ". " + info.getName() + " - " + info.getScore());
            label.setFont(new Font("Arial", Font.PLAIN, 27));
            label.setHorizontalAlignment(SwingConstants.LEFT);
            label.setVerticalAlignment(SwingConstants.CENTER);
            labelPanel.setBorder(BorderFactory.createMatteBorder(4,0,0,0, Color.BLACK));
            labelPanel.setBackground(new Color(94, 200, 242));
            label.setPreferredSize(new Dimension(500,70));
            labelPanel.setPreferredSize(new Dimension(20,70));

            if(counter.toString().equals("1")) {
                labelPanel.setBorder(BorderFactory.createMatteBorder(0,0,0,0, Color.BLACK));
                label.setFont(new Font("Arial", Font.BOLD, 27));
            }

            labelPanel.add(label);
            scores.add(labelPanel);
            counter.getAndIncrement();

        });
        scoresBox.add(scoresScroll, BorderLayout.CENTER);
        scoresScroll.setBorder(BorderFactory.createEmptyBorder());

        scoresScroll.getVerticalScrollBar().setUnitIncrement(16);

        SwingUtilities.updateComponentTreeUI(this);
    }

    public void resetInfo() throws IOException, InvalidFormatException {
        scores.removeAll();
        infoList = new ArrayList<>();
        removeAllFromExcel();
    }

    public void removeFromExcel(String name) throws IOException {
        /*
        XSSFWorkbook wb = new XSSFWorkbook("F:\\MyIdeaProjects\\QuizzApp\\info\\Podaci" + fileNum + ".xlsx");
        XSSFSheet sheet = wb.getSheetAt(0);
        Row row = null;
        for(int i = 1; i < sheet.getLastRowNum(); i++) {
            row = sheet.getRow(i);
            if(row.getCell(0).toString().equals(name)){
                break;
            }
        }
        if(row != null) {
            sheet.removeRow(row);
        }

        try (FileOutputStream outputStream = new FileOutputStream("F:\\MyIdeaProjects\\QuizzApp\\info\\Podaci" + "2" + ".xlsx")) {
            wb.write(outputStream);
        }
        File old = new File("F:\\MyIdeaProjects\\QuizzApp\\info\\Podaci" + "1" + ".xlsx");
        old.delete();
        File newX = new File("F:\\MyIdeaProjects\\QuizzApp\\info\\Podaci" + "2" + ".xlsx");
        newX.renameTo(old);

         */

    }

    public void removeAllFromExcel() throws IOException, InvalidFormatException {
        XSSFWorkbook wb = new XSSFWorkbook(scoreFile);
        XSSFSheet sheet = wb.getSheetAt(0);

        sheet.forEach((row) -> row.getSheet().removeRow(row));
    }

    public void developerMode() {
        for(Component scoreTmp : scores.getComponents()) {
            JLabel score = (JLabel)((JPanel)scoreTmp).getComponents()[0];
            JButton scoreBtn = new JButton(score.getText());
            scoreBtn.setFont(new Font("Arial", Font.ITALIC, 25));

            scores.remove(scoreTmp);
            scores.add(scoreBtn);
            SwingUtilities.updateComponentTreeUI(this);

            scoreBtn.addActionListener((e) -> {
                double scoreInt = Double.parseDouble(scoreBtn.getText().split(" ")[scoreBtn.getText().split(" ").length - 1]);
                String user = scoreBtn.getText().split(" ")[1];


                infoList.removeIf((info) -> info.getScore() == scoreInt && info.getName().equals(user));

                scores.remove(scoreBtn);
                numOfPeople--;
                SwingUtilities.updateComponentTreeUI(this);

                try {
                    saveToExcel();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
        }
    }
}
