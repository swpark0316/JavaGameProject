import javax.swing.*;
import java.util.Vector;

public class ScoreManager {
    private static Vector<GameResult> leaderboard = new Vector<>(); // 게임 결과들을 저장할 벡터
    private int score = 0;
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
    public void addScore(String name, int score, boolean isWin, String level){ // 리더보드에 결과를 저장하는 함수
        leaderboard.add(new GameResult(name, score, isWin, level));
    }
    public Vector<GameResult> getLeaderboard(){
        return leaderboard;
    } // 리더보드 게터

    public static void saveGame(){ // 리더보드를 파일에 저장하는 함수

    }
}
