import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class GamePanel extends JPanel {
    private Vector<Scroll> scrolls = new Vector<>();
    private ScoreManager scoreManager = null;
    private FallingThread fallingThread = null;
    private GeneratorThread generatorThread = null;
    private GroundPanel groundPanel = null;
    private GameFrame gameFrame = null;
    private TextStore textStore = null;
    private int fallingSpeed;
    private final int maxPlayerHp = 20;
    private int[] maxBossHpList = {100, 150, 200};
    private int maxBossHp;
    private String playerName;
    private String level;
    private boolean isWin;
    private boolean isGameOver = false;

    public GamePanel(GameFrame gameFrame, ScoreManager scoreManager, TextStore textStore) {
        this.gameFrame = gameFrame;
        this.scoreManager = scoreManager;
        this.textStore = textStore;
        groundPanel = new GroundPanel();
        setLayout(new BorderLayout());
        add(new InputPanel(), BorderLayout.SOUTH);
        add(groundPanel, BorderLayout.CENTER);


    }

    class GroundPanel extends JPanel {
        private ImageIcon lv1Icon = new ImageIcon("images/level1.png");
        private Image level1 =lv1Icon.getImage();
        private ImageIcon lv2Icon = new ImageIcon("images/level2.png");
        private Image level2 =lv2Icon.getImage();
        private ImageIcon lv3Icon = new ImageIcon("images/level3.png");
        private Image level3 =lv3Icon.getImage();
        private Image currentStage;
        EntityPanel player = new EntityPanel(true);
        EntityPanel boss = new EntityPanel(false);



        public GroundPanel() {
            this.setBackground(Color.WHITE);
            this.setLayout(null); //레이아웃 없이 배치
            //엔티티 그림들 위치, 크기 설정
            boss.setBounds(GameFrame.frameWidth /2-75, 50, 150, 150);
            player.setBounds(GameFrame.frameWidth /2-75, 350, 150, 150);

            // 패널에 추가
            add(boss);
            add(player);

        }


        public void paintComponent(Graphics g){
            super.paintComponent(g);

            if (level.equals("easy")) {
                currentStage = level1;
            } else if (level.equals("normal")) {
                currentStage = level2;
            } else {
                currentStage = level3;
            }
            g.drawImage(currentStage,0, 0, getWidth(),getHeight(),this);
            g.setColor(Color.GREEN);
            Font defaultF = g.getFont();
            Font f = new Font("", Font.BOLD, 20);
            g.setFont(f);
            g.drawString("점수: " + scoreManager.getScore(), getWidth() - 100, 30);
            g.setFont(defaultF);
            g.setColor(Color.YELLOW);
            g.fillRect(0,204,getWidth(),2);
            g.drawString("크리티컬 라인", getWidth()-80,220);
            g.setColor(Color.BLACK);


        }

        class EntityPanel extends JPanel{
            private enum State{IDLE, HIT}
            private boolean isPlayer;
            private int maxHp,hp;
            private BufferedImage playerIcon;
            private BufferedImage bossIcon;
            private BufferedImage[] idleImages;
            private BufferedImage[] hitImages;
            private int tick=0;
            private int hitTick=0;
            private AnimationThread animationThread;
            private HealthBar healthBar = new HealthBar();
            private State state;

            public EntityPanel(boolean isPlayer){ // true면 플레이어, false면 보스
                this.isPlayer=isPlayer;
                this.maxHp = this.hp = maxHp;
                setLayout(new BorderLayout());
                setOpaque(false);
                if(isPlayer) {
                    state=State.IDLE;
                    loadPlayerImages();
                }
                else
                    loadBossImages();

                //수정
                healthBar.setSize(getWidth(),15); // 체력바 사이즈 조정
                this.add(healthBar, BorderLayout.SOUTH);
            }

            public void setMaxHp(int maxHp) {
                this.maxHp = this.hp = maxHp;
            }

            public void createThread(){
                animationThread = new AnimationThread();
            }
            private void damage(int amount){
                hp-=amount;
                if(hp>=maxHp){ // 힐로쓰는경우 최대체력 넘어가면
                    hp=maxHp; // 최대체력으로 만듦
                }
                if(hp<=0){
                    gameOver(isPlayer);
                }
                healthBar.repaint();
            }
            private void loadPlayerImages(){ // 플레이어 이미지를 로드하는 함수
                try {
                    playerIcon = ImageIO.read(new File("images/playerIdle.png")); // 파일을 연다
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 이미지를 잘라서 이미지 배열에 저장한다
                idleImages = new BufferedImage[22];
                idleImages[0]=playerIcon.getSubimage(590,0,90,75); //1
                idleImages[1]=playerIcon.getSubimage(590,78,90,75); //2
                idleImages[2]=playerIcon.getSubimage(0,108,90,75); //3
                idleImages[3]=playerIcon.getSubimage(90,108,90,75); //4
                idleImages[4]=playerIcon.getSubimage(180,108,90,75); //5
                idleImages[5]=playerIcon.getSubimage(270,108,90,75); //6
                idleImages[6]=playerIcon.getSubimage(360,108,90,75); //7
                idleImages[7]=playerIcon.getSubimage(450,108,90,75); //8
                idleImages[8]=playerIcon.getSubimage(540,156,87,75); //9
                idleImages[9]=playerIcon.getSubimage(630,156,87,75); //10
                idleImages[10]=playerIcon.getSubimage(0,186,90,75); //11
                idleImages[11]=playerIcon.getSubimage(90,186,90,75); //12

                idleImages[12]=playerIcon.getSubimage(0,186,90,75); //11
                idleImages[13]=playerIcon.getSubimage(630,156,87,75); //10
                idleImages[14]=playerIcon.getSubimage(540,156,87,75); //9
                idleImages[15]=playerIcon.getSubimage(450,108,90,75); //8
                idleImages[16]=playerIcon.getSubimage(360,108,90,75); //7
                idleImages[17]=playerIcon.getSubimage(270,108,90,75); //6
                idleImages[18]=playerIcon.getSubimage(180,108,90,75); //5
                idleImages[19]=playerIcon.getSubimage(90,108,90,75); //4
                idleImages[20]=playerIcon.getSubimage(0,108,90,75); //3
                idleImages[21]=playerIcon.getSubimage(590,78,90,75); //2

            }
            private void loadBossImages(){ // 보스 이미지를 로드하는 함수
                try {
                    bossIcon = ImageIO.read(new File("images/boss.png")); // 파일을 연다
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // 이미지를 잘라서 종류별 이미지 배열에 저장한다
                idleImages = new BufferedImage[14];
                idleImages[0]=bossIcon.getSubimage(1260,420 ,180 ,140);
                idleImages[1]=bossIcon.getSubimage(1439,420 ,180 ,140);
                idleImages[2]=bossIcon.getSubimage(1617,420 ,180 ,140);
                idleImages[3]=bossIcon.getSubimage(1795,420 ,180 ,140);
                idleImages[4]=bossIcon.getSubimage(1973,420 ,180 ,140);
                idleImages[5]=bossIcon.getSubimage(2151,420 ,180 ,140);
                idleImages[6]=bossIcon.getSubimage(0,560 ,180 ,140);
                idleImages[7]=bossIcon.getSubimage(179,560 ,180 ,140);
                idleImages[8]=bossIcon.getSubimage(359,560 ,180 ,140);
                idleImages[9]=bossIcon.getSubimage(539,560 ,180 ,140);
                idleImages[10]=bossIcon.getSubimage(719,560 ,180 ,140);
                idleImages[11]=bossIcon.getSubimage(899,560 ,180 ,140);
                idleImages[12]=bossIcon.getSubimage(1079,560 ,180 ,140);
                idleImages[13]=bossIcon.getSubimage(1259,560 ,180 ,140);
                
                hitImages = new BufferedImage[6];
                hitImages[0]=bossIcon.getSubimage(0,420 ,180 ,140);
                hitImages[1]=bossIcon.getSubimage(179,420 ,180 ,140);
                hitImages[2]=bossIcon.getSubimage(359,420 ,180 ,140);
                hitImages[3]=bossIcon.getSubimage(539,420 ,180 ,140);
                hitImages[4]=bossIcon.getSubimage(719,420 ,180 ,140);
                hitImages[5]=bossIcon.getSubimage(899,420 ,180 ,140);
            }
            public void hit(){
                state = State.HIT; // 피격 상태로 변경
                hitTick = 0; // 재생 도중에 다시 함수가 불리면 처음부터 재생하기 위해 틱을 초기화한다
            }
            private void update(){ // 틱을 업데이트하는 함수
                if (isPlayer) { // 플레이어면
                        tick = (tick + 1) % idleImages.length; // 이미지 수만큼 틱 반복
                }
                else { // 보스면
                    if (state == State.HIT) { // 피격 상태일때
                        if (hitTick < hitImages.length - 1) { // 애니메이션 장수보다 작으면 틱 증가
                            hitTick++;
                        } else { // 끝났으면
                            state = State.IDLE; // 대기 상태로 전환
                            hitTick = 0; // 히트 틱 초기화
                        }
                    } else {
                        tick = (tick + 1) % idleImages.length; // 이미지 수만큼 틱 반복
                    }
                }
                repaint();
            }
            class HealthBar extends JPanel{ // 체력바를 그리는 패널
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);


                    // 빈 체력 그리기
                    g.setColor(Color.GRAY); // 회색으로
                    g.fillRect(0, 0, getWidth(), getHeight()); // 패널 크기만큼 그린다

                    // 현재 체력 바 그리기
                    if(isPlayer)  // 플레이어면
                        g.setColor(Color.GREEN); // 초록색으로 그리기
                    else  // 보스면
                        g.setColor(Color.RED); // 빨간색으로 그리기
                    int barWidth = getWidth() * hp / maxHp; //체력바의 길이 계산
                    g.fillRect(0, 0, barWidth, getHeight()); // 길이만큼 그린다

                    // 테두리 그리기
                    g.setColor(Color.BLACK);
                    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                }
            }

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage currentImage = null; // 현재 그려야 할 이미지

                if (isPlayer) {
                        currentImage = idleImages[tick]; // 대기 이미지
                }
                else{
                    if (state == State.HIT) { // 피격 상태면
                        currentImage = hitImages[hitTick]; //피격 이미지
                    }
                    else {// 아니면
                        currentImage = idleImages[tick]; // 대기 이미지
                    }
                }
                g.drawImage(currentImage, // 현재 이미지를
                        getWidth()/2-idleImages[0].getWidth()/2, // 패널의 중앙
                        getHeight()-idleImages[0].getHeight()-13, // 체력바와 안겹치는 적당한 높이로 그린다
                        this);
            }

            public void startThread(){
                animationThread.start();
            } // 애니매이션 스레드 실행 함수
            class AnimationThread extends Thread{ // 애니메이션을 그리는 스레드
                @Override
                public void run() {
                    while(!isGameOver){ // 게임이 끝나지 않았으면
                        try {
                            sleep(200); // 200ms 마다
                            update(); // 업데이트를 호출한다
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }



    private void gameOver(boolean isPlayer) {
        isWin= !isPlayer; // 플레이어가 아니면 이기게
        isGameOver = true; // 게임 끝남
        String message; // 게임 종료 메시지
        if (isWin) {
            message = "승리! ";
        } else {
            message = "패배.. ";
        }
        message += "최종 점수는 " + scoreManager.getScore() + "\n 다시 하시겠습니까?";
        scoreManager.addScore(playerName, scoreManager.getScore(), isWin, level); // 스코어 저장
        int result = JOptionPane.showConfirmDialog(this, message, "Game over", JOptionPane.YES_NO_OPTION); // 재시작 여부 메시지를 띄우고
        if (result == JOptionPane.YES_OPTION) {
            resetAndStart(); // yes면 다시시작
        } else {
            gameFrame.returnToMain(); // 아니면 메인메뉴로
        }
    }

    private void resetAndStart() { // 게임을 초기화하고 시작하는 함수
        isGameOver=false; // 게임이 끝나지 않은 상태로 함
        for (Scroll scroll : scrolls) { // 스크롤을 돌면서
            scroll.remove(); // 화면에 남아있는 스크롤들 지우기
        }
        scrolls.clear(); // 벡터 비우기
        repaint(); // 화면 다시 그림

        scoreManager.resetScore(); //점수 초기화
        // 레벨별로 난이도 설정
        if (level.equals("easy")) {
            fallingSpeed = 6;
            maxBossHp = maxBossHpList[0];
        } else if (level.equals("normal")) {
            fallingSpeed = 9;
            maxBossHp = maxBossHpList[1];
        } else {
            fallingSpeed = 12;
            maxBossHp = maxBossHpList[2];
        }
        // 플레이어 / 보스 체력 설정한다
        groundPanel.player.setMaxHp(maxPlayerHp);
        groundPanel.boss.setMaxHp(maxBossHp);
        // 스레드들 새로 만들기
        generatorThread = new GeneratorThread();
        fallingThread = new FallingThread();
        groundPanel.player.createThread();
        groundPanel.boss.createThread();
        // 스레드들 시작
        generatorThread.start();
        fallingThread.start();
        groundPanel.player.startThread();
        groundPanel.boss.startThread();
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
                scrolls.add(new Scroll(groundPanel, textStore)); // 새 스크롤을 만들어 벡터에 저장한다
                try {
                    sleep(3200);
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
                        groundPanel.player.damage(3); // 패널티를 주고
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
        private JTextField inputField = new JTextField(10); // 사용자의 입력을 받을 곳

        public InputPanel() {
            this.setBackground(Color.GRAY);
            add(inputField);
            inputField.addActionListener(new ActionListener() { // 액션이발생(엔터 눌림)하면
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 입력된 값 받아오기
                    JTextField tf = (JTextField) (e.getSource());
                    String userText = tf.getText();

                    Scroll correctScroll = null; // 정답인 스크롤을 저장
                    for (Scroll scroll : scrolls) { // 벡터를 순회하며
                        if (scroll.isCorrect(userText)) { // 정답인 스크롤을 찾으면
                            correctScroll = scroll; // 저장하고 나온다
                            break;
                        }
                    }
                    if (correctScroll != null) { // 정답인 스크롤이 있으면
                        correctAnswer(correctScroll); // 함수로 넘겨 처리
                    } else // 없으면
                        groundPanel.player.damage(2);// 패널티 주기
                    tf.setText(""); // 인풋필드 초기화
                }
            });
        }

        private void correctAnswer(Scroll scroll) { // 정답인 스크롤을 처리하는 함수
            Scroll.ScrollType scrollType = scroll.getScrollType(); // 타입을 받아와서
            switch (scrollType) { // 타입별로 처리
                case Scroll.ScrollType.DAMAGE -> {// 데미지 스크롤
                    groundPanel.boss.hit(); // 보스 피격모션 재생
                    scoreManager.increase(); // 점수 증가
                    if(scroll.getY() < 200){ // 일정 y좌표 이하에서 맞추면
                        groundPanel.boss.damage(15); // 데미지를 더 많이 준다
                    }
                    else{
                        groundPanel.boss.damage(10);
                    }
                }
                case Scroll.ScrollType.HEAL -> { // 힐 스크롤
                    scoreManager.increase(15);
                    groundPanel.player.damage(-4); // 마이너스 데미지를 줘서 체력을 회복시킨다
                }
                case Scroll.ScrollType.TRUTH -> { // 진실 스크롤
                    scoreManager.increase(20);
                    for (Scroll s : scrolls) { // 스크롤을 돌며
                        s.revealTruth(); // 내부에 저장되어있는 단어로 바꾼다
                    }
                }
            }
            scroll.remove(); // 화면에서 삭제
            scrolls.remove(scroll); // 벡터에서 삭제
        }
    }
}
