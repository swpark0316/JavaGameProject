import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.Vector;

public class MainPanel extends JPanel {
    GameFrame gameFrame = null;
    JButton startButton = new JButton("게임 시작");
    JButton exitButton = new JButton("게임 종료");
    JButton rankButton = new JButton("랭킹 보기");
    String[] levels = {"easy", "normal", "hard"};
    JComboBox selectLevelBox = new JComboBox(levels);
    JTextField inputNameField = new JTextField();

    public MainPanel(GameFrame gameFrame){
        this.gameFrame=gameFrame;
        setLayout(null);
        selectLevelBox.setLocation(260, 270);
        selectLevelBox.setSize(180,40);
        inputNameField.setLocation(460,270 );
        inputNameField.setSize(180, 40);
        startButton.setLocation(350,350);
        startButton.setSize(200,60);
        exitButton.setLocation(350,430);
        exitButton.setSize(200,60);
        rankButton.setLocation(350, 510);
        rankButton.setSize(200, 60);

        add(inputNameField);
        add(selectLevelBox);
        add(startButton);
        add(exitButton);
        add(rankButton);
        // 테스트용 더미 데이터 (실제 게임에서는 지우셔도 됩니다)
        gameFrame.getLeaderboard().add(new GameResult("Test1", 1000, true, "easy"));
        gameFrame.getLeaderboard().add(new GameResult("Test2", 4000, true, "hard"));
        gameFrame.getLeaderboard().add(new GameResult("Test2", 5000, true, "normal"));
        gameFrame.getLeaderboard().add(new GameResult("Test3", 3000, false, "normal"));

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(inputNameField.getText().isEmpty()){
                    JOptionPane.showMessageDialog(MainPanel.this, "이름을 입력하세요", "warning",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                gameFrame.gameStart((String)selectLevelBox.getSelectedItem(), inputNameField.getText());
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        rankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Leaderboard();
            }
        });
    }

    class Leaderboard extends JFrame {
        public Leaderboard(){
            super("leaderboard");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            String[] columnNames = {"순위", "클리어", "이름", "점수", "난이도"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            Vector<GameResult> leaderboard = gameFrame.getLeaderboard();
            leaderboard.sort(Comparator.comparing(GameResult::isWin)
                    .thenComparing(GameResult::getLevelIndex)
                    .thenComparing(GameResult::getScore).reversed());
            int rank = 1;
            for (GameResult gameResult : leaderboard) {
                Object[] rowData = {
                        rank++,
                        gameResult.isWin() ? "성공" : "실패",
                        gameResult.getName(),
                        gameResult.getScore(),
                        gameResult.getLevel()
                };
                model.addRow(rowData);
            }

            JTable table = new JTable(model);
            table.setRowHeight(30);

            JScrollPane scrollPane = new JScrollPane(table);

            // 창에 스크롤판 추가
            add(scrollPane);

            // 창 보여주기
            setVisible(true);
        }
    }
}
