import javax.swing.*;

public class GameResult {
    private String name;
    private int score;
    private boolean isWin;
    private String level;

    public GameResult(String name, int score, boolean isWin, String level) {
        this.name = name;
        this.score = score;
        this.isWin = isWin;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public boolean isWin() {
        return isWin;
    }

    public String getLevel() {
        return level;
    }
    public int getLevelIndex(){
        if(level.equals("easy")) return 0;
        else if(level.equals("normal")) return 1;
        else return 2;
    }
}

