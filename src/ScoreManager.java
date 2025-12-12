import javax.swing.*;

public class ScoreManager {
    private static int score = 0;
    private JLabel scoreLabel = new JLabel("점수: " + Integer.toString(score));

    public void increase(int amount){
        score += amount;
        scoreLabel.setText("점수: " + Integer.toString(score));
    }

    public JLabel getScoreLabel(){
        return scoreLabel;
    }
    public void increase(){
        increase(10);
    }
    public int getScore(){
        return score;
    }
    public void resetScore(){
        score = 0;
        scoreLabel.setText("점수: " + Integer.toString(score));
    }
}
