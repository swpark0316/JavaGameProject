import javax.swing.*;
import java.io.*;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.Vector;

public class ScoreManager {
    private static Vector<GameResult> leaderboard = new Vector<>(); // 게임 결과들을 저장할 벡터
    private int score = 0;
    private JLabel scoreLabel = new JLabel("점수: " + Integer.toString(score));

    public ScoreManager(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("leaderboard.txt")); // 파일을 읽기로 연다
            String s; // 읽어온 줄을 받을 곳
            while((s = br.readLine()) != null){ // 읽은 줄이 비어있을때까지 반복
                // 읽어온 줄을 , 기준으로 나눠 벡터에 저장한다
                String[] elements = new String[5];
                StringTokenizer st = new StringTokenizer(s, ",");
                // 타입에 맞춰 변환
                String name = st.nextToken();
                int score = Integer.parseInt(st.nextToken());
                boolean isWin = Boolean.parseBoolean(st.nextToken());
                String level = st.nextToken();
                addScore(name,score,isWin,level); // 리더보드에 추가한다
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void increase(int amount){ // 어마운트만큼 스코어 증가
        score += amount;
        scoreLabel.setText("점수: " + Integer.toString(score));
    }

    public void increase(){ increase(10); } // 매개변수 없이 호출했을 때
    public JLabel getScoreLabel(){
        return scoreLabel;
    }
    public int getScore(){
        return score;
    }

    public void resetScore(){
        score = 0;
        scoreLabel.setText("점수: " + Integer.toString(score));
    }
    public void addScore(String name, int score, boolean isWin, String level){ // 리더보드에 결과를 추가하는 함수
        leaderboard.add(new GameResult(name, score, isWin, level));
    }
    public Vector<GameResult> getLeaderboard(){
        return leaderboard;
    } // 리더보드 게터

    public static void saveGame(){ // 리더보드를 파일에 저장하는 함수
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("leaderboard.txt")); // 파일을 쓰기로 연다

            for (GameResult gameResult : leaderboard) { // 리더보드를 돌며
                bw.write(gameResult.getElementsString()); // 정보를 받아온다
                bw.newLine(); // 줄바꿈
            }

            bw.flush(); // 버퍼에 남아 있던 문자 모두 출력
            bw.close(); // 파일 닫기

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
