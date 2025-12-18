public class GameResult { // 게임 결과를 담는 클래스
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

    // 게터들
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

    public String getElementsString(){ // 요소들을 ,로 구분한 문자열을 리턴하는 함수
        return name+","+score+","+isWin+","+level;
    }

    public int getLevelIndex(){ // 정렬을 위해 레벨에따라 숫자를 리턴하는 함수
        if(level.equals("easy")) return 0;
        else if(level.equals("normal")) return 1;
        else return 2;
    }
}

