import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.Vector;

public class GameFrame extends JFrame {
    private ScoreManager scoreManager = new ScoreManager(); // 점수를 관리하는 객체
    private TextStore textStore = new TextStore();
    private MainPanel mainPanel = new MainPanel(this, scoreManager , textStore); // 메인화면 패널
    private GamePanel gamePanel = new GamePanel(this, scoreManager, textStore); // 본 게임이 진행되는 패널
    public static final int frameWidth = 900; // 게임프레임 너비
    public static final int frameHeight = 650; // 게임프레임 높이

    public GameFrame() {
        super("Magic Typing Academy");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 끄면 종료되게함
        setSize(frameWidth, frameHeight); // 화면 크기 설정
        setContentPane(mainPanel); // 메인화면 불러옴
        setVisible(true); // 보이게 하기
    }


    public void gameStart(String level, String name){ // 메인화면에서 게임을 시작하는 함수
        setContentPane(gamePanel); // 패널을 gamePanel로 전환
        // 컨탠트팬을 바꿨으므로 다시 배치하고 그리게 한다
        revalidate();
        repaint();
        gamePanel.start(level,name); // 게임 시작

    }

    public void returnToMain(){ // 메인화면으로 돌아가는 함수
        setContentPane(mainPanel);
        revalidate();
        repaint();
    }

}
