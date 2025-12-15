import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class TextStore {

    private static HashMap<String, String> dictionary = new HashMap<>(); // <영어, 한글>을 저장할 해시맵
    private static ArrayList<String> keys = null;
    public TextStore(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("wordlist.txt")); // 파일을 연다
            String s;
            while((s = br.readLine()) != null){
                String eng, kor;
                StringTokenizer st = new StringTokenizer(s, ",");
                eng = st.nextToken();
                kor = st.nextToken();
                dictionary.put(eng, kor);
            }
        }
        catch (IOException e){
            System.out.println("입출력 오류");
        }
        keys = new ArrayList<>(dictionary.keySet());
    }

    public boolean addWord(String eng, String kor){ // 단어를 추가하는 함수
        if(dictionary.containsKey(eng)) // 단어가 이미 있으면
            return false; // false 리턴

        // 맵과 리스트에 추가하고 true를 리턴한다
        dictionary.put(eng, kor);
        keys.add(eng);
        return true;
    }
    public boolean deleteWord(String eng){ // 단어를 삭제하는 함수
        if(!dictionary.containsKey(eng)) // 입력한 단어가 없으면
            return false; // false 리턴

        // 맵과 리스트에서 삭제하고 true를 리턴한다
        dictionary.remove(eng);
        keys.remove(eng);
        return true;
    }
    public String get(){ // 랜덤한 영어단어를 리턴하는 함수
        int index = (int)(Math.random()*keys.size()); // 리스트 사이즈 안에서 랜덤한 숫자를 뽑는다
        return keys.get(index); // 그 인덱스의 단어를 리턴한다
    }

    public String getMean(String key){ // 단어를 입력하면 뜻을 리턴하는 함수
        return dictionary.get(key);
    }

    public static void saveWordList(){ // 단어장을 파일에 저장하는 함수

    }
}
