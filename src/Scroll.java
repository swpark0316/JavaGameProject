import javax.swing.*;
import java.awt.*;

class Scroll {
    public enum ScrollType {DAMAGE, HEAL, TRUTH} // 스크롤의 타입

    private enum HintType {MEAN, LENGTH, VOWEL} // 힌트의 타입

    private ScrollType scrollType = null; // 스크롤의 타입을 저장
    private JLabel text = new JLabel(); // 스크롤의 내용을 보여줄 레이블
    private TextStore textStore = new TextStore(); // 단어를 받아오기 위한 객체
    private JPanel groundPanel = null; // 스크롤을 표시할 패널
    private String tempWord = null; // 받아온 단어를 저장
    private char firstChar = 0; // 단어의 첫 글자를 저장
    private HintType hintType = null; // 힌트 타입을 저장
    private Object hintValue = null; // 힌트 내용을 저장

    private ImageIcon scrollIcon = null;
    private Image scrollImage = null;
    private JPanel scrollPanel = null;

    public Scroll(JPanel groundPanel) {
        this.groundPanel = groundPanel;

        scrollIcon = new ImageIcon("images/Scroll.png");
        scrollImage = scrollIcon.getImage();
        scrollPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(scrollImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        scrollPanel.setOpaque(false);
        scrollPanel.setLayout(new BorderLayout());
        setScrollText(); // 스크롤의 내용을 정해서
        text.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 가운데 정렬
        scrollPanel.add(text, BorderLayout.CENTER);
        int width = 120;
        int height = 60;
        scrollPanel.setSize(width, height);
        int x = (int) (Math.random() * (groundPanel.getWidth() - 100)) + 50; // 랜덤한 x 생성
        int y = 50;
        //text.setLocation(x, y); // 위에서 정한 위치로 보내고
        //text.setVisible(true); //보이게한다
        scrollPanel.setLocation(x, y);
        scrollPanel.setVisible(true);
        groundPanel.add(scrollPanel);

    }

    private void setScrollText() { // 스크롤의 내용을 정하는 함수
        text.setSize(100, 40); //
        tempWord = textStore.get();
        tempWord = tempWord.toLowerCase();
        firstChar = tempWord.charAt(0);
        String problem = "<html>첫 글자: " + firstChar + "<br>";

        int rand = (int) (Math.random() * 100 + 1);
        if (rand < 85) {
            scrollType = ScrollType.DAMAGE;
            text.setForeground(Color.red);
        } else if (rand < 95) {
            scrollType = ScrollType.HEAL;
            text.setForeground(Color.green);
        } else {
            scrollType = ScrollType.TRUTH;
            text.setForeground(Color.magenta);
        }
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
        //groundPanel.add(text);
        scrollPanel.repaint();
    }

    private static int getVowelCount(String word) {
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
    }

    public int getY() {
        return scrollPanel.getY();
    }

    public void fall(int n) {
        scrollPanel.setLocation(scrollPanel.getX(), scrollPanel.getY() + n);
    }

    public boolean isCorrect(String answer) {
        answer = answer.toLowerCase();
        String answerMean = textStore.getMean(answer);
        if (answerMean == null || answer.charAt(0) != firstChar) // 사전에 등록되어있지 않거나 첫글자가 다르면
            return false; // false 리턴

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
        return false;
    }

    public ScrollType getScrollType() {
        return scrollType;
    }

    public void revealTruth() {
        text.setText(tempWord);
    }
}