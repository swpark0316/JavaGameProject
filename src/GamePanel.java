import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class GamePanel extends JPanel {
    private Vector<Scroll> scrolls = new Vector<>();
    private ScoreManager scoreManager = null;
    private FallingThread fallingThread = null;
    private GeneratorThread generatorThread = null;
    private GroundPanel groundPanel = null;
    private GameFrame gameFrame = null;
    private int fallingSpeed;
    private final int initialPlayerHp = 20;
    private int playerHp;
    private int bossHp;
    private String playerName;
    private String level;
    private boolean isWin;
    private boolean isGameOver = false;

    public GamePanel(ScoreManager scoreManager, GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.scoreManager = scoreManager;
        groundPanel = new GroundPanel();
        setLayout(new BorderLayout());
        add(new InputPanel(), BorderLayout.SOUTH);
        add(groundPanel, BorderLayout.CENTER);


    }

    class GroundPanel extends JPanel {
        private ImageIcon lv3Icon = new ImageIcon("images/level3.png");
        private Image level3 =lv3Icon.getImage();
        public GroundPanel() {
            this.setBackground(Color.WHITE);
            this.setLayout(null);
            JLabel scoreLabel = scoreManager.getScoreLabel();
            scoreLabel.setBounds(400, 10, 100, 30);
            add(scoreLabel);
        }
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(level3,0, 0, getWidth(),getHeight(),this);
            g.drawLine(0,200,this.getWidth(),200);
        }
    }

    private void playerDamage(int damage) {
        playerHp -= damage;
        if (playerHp <= 0) {
            isWin = false;
            gameOver();
        }
    }

    private void bossDamage(int damage) {
        bossHp -= damage;
        if (bossHp <= 0) {
            isWin = true;
            gameOver();
        }
    }

    private void gameOver() {
        isGameOver = true;
        String message;
        if (isWin) {
            message = "승리! ";
        } else {
            message = "패배.. ";
        }
        message += "최종 점수는 " + scoreManager.getScore() + "\n 다시 하시겠습니까?";
        gameFrame.addScore(playerName, scoreManager.getScore(), isWin, level); // 스코어 저장
        int result = JOptionPane.showConfirmDialog(this, message, "Game over", JOptionPane.YES_NO_OPTION); // 재시작 여부 메시지를 띄우고
        if (result == JOptionPane.YES_OPTION) {
            resetAndStart(); // yes면 다시시작
        } else {
            gameFrame.returnToMain(); // 아니면 메인메뉴로
        }
    }

    private void resetAndStart() {
        isGameOver=false;
        for (Scroll scroll : scrolls) {
            scroll.remove(); // 화면에 남아있는 스크롤들 지우기
        }
        scrolls.clear(); // 벡터 비우기
        repaint(); // 화면 다시 그림

        scoreManager.resetScore();
        playerHp = initialPlayerHp;
        if (level.equals("easy")) {
            fallingSpeed = 7;
            bossHp = 100;
        } else if (level.equals("normal")) {
            fallingSpeed = 10;
            bossHp = 150;
        } else {
            fallingSpeed = 13;
            bossHp = 200;
        }
        generatorThread = new GeneratorThread();
        fallingThread = new FallingThread();
        generatorThread.start();
        fallingThread.start();
    }

    public void start(String level, String name) {// 게임 시작시키는 함수
        this.level = level;
        playerName = name; // 이름을 저장한다
        resetAndStart(); // 게임을 초기화 시키고 시작한다
    }

    class GeneratorThread extends Thread { // 스크롤을 생성하는 스레드
        @Override
        public void run() {
            while (!isGameOver) {
                scrolls.add(new Scroll(groundPanel)); // 새 스크롤을 만들어 벡터에 저장한다
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class FallingThread extends Thread { // 스크롤들을 화면 아래로 떨어지게 하는 스레드
        @Override
        public void run() {
            while (!isGameOver) {
                for (int i = 0; i < scrolls.size(); i++) { // 스크롤을 순회하며
                    Scroll scroll = scrolls.get(i);
                    scroll.fall(fallingSpeed); // 아래로 내린다
                    if (scroll.getY() > groundPanel.getHeight() - 10) { // 가장 아래까지 떨어지면
                        playerDamage(3); // 패널티를 주고
                        scroll.remove(); // 화면에서 삭제
                        scrolls.remove(i); // 벡터에서 삭제
                        i--; // i를 감소시켜 다시 다음 벡터부터 탐색
                    }
                }
                groundPanel.repaint(); // 다 내린 후 화면을 다시 그린다
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    class InputPanel extends JPanel {
        private JTextField inputField = new JTextField(10);

        public InputPanel() {
            this.setBackground(Color.GRAY);
            add(inputField);
            inputField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 입력된 값 받아오기
                    JTextField tf = (JTextField) (e.getSource());
                    String userText = tf.getText();

                    Scroll correctScroll = null;
                    for (Scroll scroll : scrolls) { // 벡터를 순회하며
                        if (scroll.isCorrect(userText)) { // 정답인 스크롤을 찾으면
                            correctScroll = scroll; // 저장하고 나온다
                            break;
                        }
                    }
                    if (correctScroll != null) { // 정답인 스크롤이 있으면
                        correctAnswer(correctScroll); // 함수로 넘겨 처리
                    } else // 없으면
                        playerDamage(2); // 패널티 주기
                    tf.setText(""); // 인풋필드 초기화
                }
            });
        }

        private void correctAnswer(Scroll scroll) { // 정답인 스크롤을 처리하는 함수
            Scroll.ScrollType scrollType = scroll.getScrollType(); // 타입을 받아와서
            switch (scrollType) { // 타입별로 처리
                case Scroll.ScrollType.DAMAGE -> {
                    if(scroll.getY() < 200){
                        bossDamage(10);
                    }
                    else{
                        bossDamage(15);
                    }
                    scoreManager.increase();
                }
                case Scroll.ScrollType.HEAL -> {
                    scoreManager.increase(15);
                    playerDamage(-4);
                }
                case Scroll.ScrollType.TRUTH -> {
                    scoreManager.increase(20);
                    for (Scroll s : scrolls) {
                        s.revealTruth();
                    }
                }
            }
            scroll.remove(); // 화면에서 삭제
            scrolls.remove(scroll); // 벡터에서 삭제
        }
    }
}
