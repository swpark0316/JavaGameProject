import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Vector;

public class MainPanel extends JPanel {
    private GameFrame gameFrame = null;
    private ScoreManager scoreManager = null;
    private TextStore textStore = null;
    private BufferedImage menuIcon;
    private JButton startButton = new JButton("게임 시작");
    private JButton rankButton = new JButton("랭킹 보기");
    private JButton manageWordButton = new JButton(" 단어 관리");
    private JButton exitButton = new JButton("게임 종료");
    private String[] levels = {"easy", "normal", "hard"};
    private JComboBox selectLevelBox = new JComboBox(levels);
    private JTextField inputNameField = new JTextField();
    private JLabel levelLabel = new JLabel("난이도 선택");
    private JLabel inputLabel = new JLabel("이름 입력");
    private ImageIcon mainIcon = new ImageIcon("images/main.png");
    private Image mainImage = mainIcon.getImage();

    public MainPanel(GameFrame gameFrame, ScoreManager scoreManager, TextStore textStore) {
        this.gameFrame = gameFrame;
        this.scoreManager = scoreManager;
        this.textStore = textStore;
        setLayout(null);

        // 위치와 크기 설정
        selectLevelBox.setLocation(50, 510);
        selectLevelBox.setSize(180, 40);
        levelLabel.setLocation(55,550);
        levelLabel.setSize(100,30);
        inputNameField.setLocation(250, 510);
        inputNameField.setSize(180, 40);
        inputLabel.setLocation(255,550);
        inputLabel.setSize(100,30);
        startButton.setLocation(540, 150);
        startButton.setSize(200, 60);
        rankButton.setLocation(540, 240);
        rankButton.setSize(200, 60);
        manageWordButton.setLocation(540, 330);
        manageWordButton.setSize(200,60);
        exitButton.setLocation(540, 420);
        exitButton.setSize(200, 60);

        loadMenuImages();

        // 패널에 추가
        add(selectLevelBox);
        add(levelLabel);
        add(inputNameField);
        add(inputLabel);
        add(startButton);
        add(rankButton);
        add(manageWordButton);
        add(exitButton);

        startButton.addActionListener(new ActionListener() { // 시작 버튼이 눌렸을 때
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputNameField.getText().isEmpty()) { // 이름이 비어있으면
                    JOptionPane.showMessageDialog(MainPanel.this, "이름을 입력하세요", "warning", JOptionPane.WARNING_MESSAGE); // 오류메시지 출력
                    return;
                }
                gameFrame.gameStart((String) selectLevelBox.getSelectedItem(), inputNameField.getText()); // 게임 시작
            }
        });
        exitButton.addActionListener(new ActionListener() { // 종료 버튼이 눌렸을 때
            @Override
            public void actionPerformed(ActionEvent e) {
                // 파일에 저장한 후 종료한다
                ScoreManager.saveGame();
                TextStore.saveWordList();
                System.exit(0);
            }
        });
        rankButton.addActionListener(new ActionListener() { // 랭킹 버튼이 눌렸을 때
            @Override
            public void actionPerformed(ActionEvent e) {
                new Leaderboard();
            } // 리더보드를 띄운다
        });

        manageWordButton.addActionListener(new ActionListener() { // 단어 관리 버튼이 눌렸을 때
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageWord(); // 단어 관리 창을 띄운다
            }
        });
    }

    private void loadMenuImages(){ // 메뉴 버튼들에 이미지를 설정하는 함수
        try {
            menuIcon = ImageIO.read(new File("images/menuImage.png")); // 스프라이트를 불러오고
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 버튼에 맞는 이미지를 할당해준다
        setButtonImage(startButton,20, 167, 370, 107, 0);
        setButtonImage(startButton,443, 167, 364, 107, 1);
        setButtonImage(startButton,856, 167, 364, 107, 2);
        setButtonImage(rankButton,20, 315, 370, 107, 0);
        setButtonImage(rankButton,443, 315, 364, 107, 1);
        setButtonImage(rankButton,856, 315, 364, 107, 2);
        setButtonImage(manageWordButton,20, 466, 370, 107, 0);
        setButtonImage(manageWordButton,443, 466, 364, 107, 1);
        setButtonImage(manageWordButton,856, 466, 364, 107, 2);
        setButtonImage(exitButton,20, 620, 370, 107, 0);
        setButtonImage(exitButton,443, 620, 364, 107, 1);
        setButtonImage(exitButton,856, 620, 364, 107, 2);
    }

    private void setButtonImage(JButton button, int x, int y, int w, int h, int type) { // 버튼에 이미지를 할당하는 함수. 타입 0->기본, 1->마우스 올렸을때, 2->눌렸을때
        BufferedImage buttonImage = menuIcon.getSubimage(x, y, w, h); // 이미지를 자르고

        Image scaled = buttonImage.getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH); // 버튼 크기에 맞춘다
        if(type == 0) { // 기본 아이콘 지정
            button.setIcon(new ImageIcon(scaled)); // 아이콘 적용
            button.setBorderPainted(false); // 테두리 지우기
            button.setContentAreaFilled(false); // 배경 지우기
            button.setFocusPainted(false); // 마우스 올렸을 때 테두리 지우기
        }
        else if (type == 1) // 롤오버 아이콘 지정
            button.setRolloverIcon(new ImageIcon(scaled));
        else  // 프레스드 아이콘 지정
            button.setPressedIcon(new ImageIcon(scaled));
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(mainImage, 0,0, getWidth(), getHeight(), null);
    }

    class Leaderboard extends JFrame { // 리더보드를 확인할 수 있는 창
        public Leaderboard(){
            super("leaderboard");
            setLocation(gameFrame.getLocation());
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 이 창만 닫히게 한다
            String[] columnNames = {"순위", "클리어", "이름", "점수", "난이도"}; // 열 이름
            DefaultTableModel model = new DefaultTableModel(columnNames, 0); // 데이터들을 저장할 모델을 만듦
            Vector<GameResult> leaderboard = scoreManager.getLeaderboard(); // 리더보드를 받아온다
            leaderboard.sort(Comparator.comparing(GameResult::isWin) // 클리어 우선 정렬
                    .thenComparing(GameResult::getLevelIndex) // 다음으로 난이도 어려운순 정렬
                    .thenComparing(GameResult::getScore).reversed()); // 다음으로 점수 높은순 정렬
            int rank = 1; // 등수
            for (GameResult gameResult : leaderboard) {
                Object[] rowData = { // 데이터를 Object 배열로 만듦
                        rank++,
                        gameResult.isWin() ? "성공" : "실패",
                        gameResult.getName(),
                        gameResult.getScore(),
                        gameResult.getLevel()
                };
                model.addRow(rowData); // 새로운 행 추가
            }

            JTable table = new JTable(model); // 모델로 테이블 생성
            table.setRowHeight(30);

            JScrollPane scrollPane = new JScrollPane(table); // JTable을 가진 스크롤팬 생성
            add(scrollPane); // 화면에 추가

            setVisible(true); // 창 보이기
        }
    }

    class ManageWord extends JFrame{ // 단어를 추가하거나 삭제할 수 있는 창

        public ManageWord(){
            super("단어 관리");
            setLocation(gameFrame.getLocation());
            setSize(600, 400);
            setContentPane(new WordPanel());
            setVisible(true);
        }
        class WordPanel extends JPanel{
            JLabel infoLabel = new JLabel("<html> <h3> 단어 추가: 영어와 한글을 모두 입력하고 추가 버튼을 누르세요. <br><br>" +
                    "단어 삭제: 삭제할 영어 단어를 입력하고 삭제 버튼을 누르세요.</h3> </html>");
            JLabel engLabel = new JLabel("영어: ");
            JLabel korLabel = new JLabel("한글: ");
            JTextField engField = new JTextField();
            JTextField korField = new JTextField();
            JLabel alertLabel = new JLabel("");
            JButton addButton = new JButton("추가");
            JButton deleteButton = new JButton("삭제");
            public WordPanel(){
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 이 창만 닫히게 한다
                setLayout(null);

                // 위치와 크기 설정
                infoLabel.setLocation(100, 40);
                infoLabel.setSize(400,100 );

                engLabel.setLocation(100, 160);
                engLabel.setSize(50,20);
                engField.setLocation(140, 160);
                engField.setSize(90, 25);

                korLabel.setLocation(300, 160);
                korLabel.setSize(50,20);
                korField.setLocation(340, 160);
                korField.setSize(90, 25);

                alertLabel.setLocation(230, 230);
                alertLabel.setSize(150,30);
                addButton.setLocation(150,270);
                addButton.setSize(100,40);
                deleteButton.setLocation(300, 270);
                deleteButton.setSize(100,40);

                // 패널에 추가
                add(infoLabel);
                add(engLabel);
                add(korLabel);
                add(engField);
                add(korField);
                add(alertLabel);
                add(addButton);
                add(deleteButton);


                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 텍스트필드에서 문자열을 받아온다
                        String engText = engField.getText();
                        String korText = korField.getText();
                        if(engText.isEmpty() || korText.isEmpty()){ // 둘 중 하나라도 비어있으면
                            JOptionPane.showMessageDialog(MainPanel.this, "단어를 모두 입력해주세요.", "warning", JOptionPane.WARNING_MESSAGE); // 오류메시지 출력
                            return;
                        }
                        if(textStore.addWord(engText,korText)) // 단어를 추가하는 함수를 호출하고, 성공 여부에 따라 메시지를 띄운다
                            alertLabel.setText("단어 추가 성공!");
                        else
                            alertLabel.setText("이미 있는 단어입니다.");
                        repaint();
                    }
                });

                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 텍스트필드에서 문자열을 받아온다
                        String engText = engField.getText();
                        if(engText.isEmpty()){ // 비어있으면
                            JOptionPane.showMessageDialog(MainPanel.this, "삭제할 단어를 입력해주세요.", "warning", JOptionPane.WARNING_MESSAGE); // 오류메시지 출력
                            return;
                        }
                        if(textStore.deleteWord(engText)) // 단어를 삭제하는 함수를 호출하고, 성공 여부에 따라 메시지를 띄운다
                            alertLabel.setText("단어 삭제 성공!");
                        else
                            alertLabel.setText("없는 단어입니다.");
                        //repaint();
                    }
                });
            }
        }
    }
}
