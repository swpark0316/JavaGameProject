import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.Vector;

public class GameFrame extends JFrame {
    private Vector<GameResult> leaderboard = new Vector<>();
    private TextStore tStore = new TextStore();
    private ScoreManager scoreManager = new ScoreManager();
    private GamePanel gamePanel = new GamePanel(scoreManager, this);
    private MainPanel mainPanel = new MainPanel(this);

    // 이미지 로딩
//    ImageIcon normalIcon = new ImageIcon("images/normal.png");
//    ImageIcon pressedIcon = new ImageIcon("images/pressed.png");
//    ImageIcon rolloverIcon = new ImageIcon("images/rollover.png");

    public GameFrame() {
        super("게임");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setContentPane(mainPanel);
        setVisible(true);
    }


    public void gameStart(String level, String name){
        setContentPane(gamePanel);
        revalidate();
        repaint();
        gamePanel.start(level,name);

    }

    public void returnToMain(){
        setContentPane(mainPanel);
    }
    public void addScore(String name, int score, boolean isWin, String level){
        leaderboard.add(new GameResult(name, score, isWin, level));
    }
    public Vector<GameResult> getLeaderboard(){
        return leaderboard;
    }
}
