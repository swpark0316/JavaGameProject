import javax.swing.*;
import java.awt.*;

class Scroll {
    public enum ScrollType {DAMAGE, HEAL, TRUTH} // 스크롤의 타입

    private enum HintType {MEAN, LENGTH, VOWEL} // 힌트의 타입

    private ScrollType scrollType = null; // 스크롤의 타입을 저장
    private JLabel text = new JLabel(); // 스크롤의 내용을 보여줄 레이블
    private TextStore textStore = null; // 단어를 받아오기 위한 객체
    private JPanel groundPanel = null; // 스크롤을 표시할 패널
    private String tempWord = null; // 받아온 단어를 저장
    private char firstChar = 0; // 단어의 첫 글자를 저장
    private HintType hintType = null; // 힌트 타입을 저장
    private Object hintValue = null; // 힌트 내용을 저장



    private JPanel scrollPanel = null;

    public Scroll(JPanel groundPanel, TextStore textStore) {
        this.groundPanel = groundPanel;
        this.textStore = textStore;

        setScroll(); // 스크롤의 내용을 정한다

        scrollPanel = new ScrollPanel();
        scrollPanel.setOpaque(false); // 배경 투명하게
        scrollPanel.setLayout(new BorderLayout()); // 보더레이아웃으로 설정
        text.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 가운데 정렬
        scrollPanel.add(text, BorderLayout.CENTER); // 텍스트 추가
        scrollPanel.setSize(120, 60); // 크기 설정
        int x = (int) (Math.random() * (groundPanel.getWidth() - 120)); // 랜덤한 x 생성
        int y = 50;
        scrollPanel.setLocation(x, y); // 위치설정
        scrollPanel.setVisible(true);
        groundPanel.add(scrollPanel);
        groundPanel.setComponentZOrder(scrollPanel, 0); // 스크롤을 화면 맨 앞으로 가져옴

    }

    class ScrollPanel extends JPanel{
        // 스크롤 이미지 받아오기
        private static final ImageIcon damageScrollIcon = new ImageIcon("images/defaultScroll.png");
        private static final Image damageScrollImage = damageScrollIcon.getImage();
        private static final ImageIcon healScrollIcon = new ImageIcon("images/greenScroll.png");
        private static final Image healScrollImage = healScrollIcon.getImage();
        private static final ImageIcon truthScrollIcon = new ImageIcon("images/purpleScroll.png");
        private static final Image truthScrollImage = truthScrollIcon.getImage();
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g; // 투명도를 조절하기위해 Graphics2D로 변환
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f)); // 투명도를 0.7로 설정
            switch (scrollType) { // 스크롤 타입에 따라 다른 이미지로 그린다
                case DAMAGE -> g.drawImage(damageScrollImage, 0, 0, getWidth(), getHeight(), this);
                case HEAL -> g.drawImage(healScrollImage, 0, 0, getWidth(), getHeight(), this);
                case TRUTH -> g.drawImage(truthScrollImage, 0, 0, getWidth(), getHeight(), this);
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // 투명도를 다시 복구
        }
    }

    private void setScroll() { // 스크롤의 내용을 정하는 함수
        text.setSize(100, 40); //
        tempWord = textStore.get(); // 조건을 정할 임시 단어를 불러온다
        firstChar = tempWord.charAt(0); // 첫 글자 저장
        String problem = "<html>첫 글자: " + firstChar + "<br>"; // 문제를 problem에 저장해둔다

        // 랜덤으로 스크롤의 타입을 정한다
        int rand = (int) (Math.random() * 100 + 1);
        if (rand < 85) {
            scrollType = ScrollType.DAMAGE;
        } else if (rand < 95) {
            scrollType = ScrollType.HEAL;
        } else {
            scrollType = ScrollType.TRUTH;
        }
        // 랜덤으로 문제의 내용을 정해 problem에 추가한다
        rand = (int) (Math.random() * 100 + 1);
        if (rand < 50) {
            String wordMean = textStore.getMean(tempWord);
            problem += "뜻: " + wordMean;
            hintType = HintType.MEAN;
            hintValue = wordMean;
        } else if (rand < 80) {
            int wordLength = tempWord.length();
            problem += "글자 수:" + wordLength;
            hintType = HintType.LENGTH;
            hintValue = wordLength;
        } else {
            int vowelCount = getVowelCount(tempWord);
            problem += "모음의 개수: " + vowelCount;
            hintType = HintType.VOWEL;
            hintValue = vowelCount;
        }
        problem += "</html>";
        text.setText(problem);
    }

    private static int getVowelCount(String word) { // 모음 개수를 세는 함수
        int vowelCount = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == 'a' ||
                    word.charAt(i) == 'e' ||
                    word.charAt(i) == 'i' ||
                    word.charAt(i) == 'o' ||
                    word.charAt(i) == 'u') {
                vowelCount++;
            }
        }
        return vowelCount;
    }

    public void remove() {
        groundPanel.remove(scrollPanel);
    } // 패널에서 스크롤을 지우는 함수

    public int getY() {
        return scrollPanel.getY();
    }

    public void fall(int n) { // 입력한 값만큼 스크롤을 내려가게 하는 함수
        scrollPanel.setLocation(scrollPanel.getX(), scrollPanel.getY() + n);
    }

    public boolean isCorrect(String answer) { // 정답을 확인하는 함수
        answer = answer.toLowerCase(); // 모두 소문자로 바꾼다
        String answerMean = textStore.getMean(answer); // 입력한 답의 뜻을 찾는다
        if (answerMean == null || answer.charAt(0) != firstChar) // 입력한 답의 뜻이 없으면 사전에 등록되어있지 않은 단어. 또는 첫글자가 다르면
            return false; // false 리턴

        // 힌트에 따라 답을 검사하고 맞으면 true를 리턴한다
        switch (hintType) {
            case HintType.MEAN:
                if (answerMean.equals(hintValue))
                    return true;
                break;
            case HintType.LENGTH:
                if (answer.length() == (int) hintValue)
                    return true;
                break;
            case HintType.VOWEL:
                if (getVowelCount(answer) == (int) hintValue)
                    return true;
                break;
        }
        return false; // 맞추지 못했으면 false 리턴
    }

    public ScrollType getScrollType() { return scrollType; }

    public void revealTruth() { text.setText(tempWord); } // 스크롤에 저장되어있는 임시 단어로 내용을 바꾸는 함수
}