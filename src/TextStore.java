import java.util.ArrayList;
import java.util.HashMap;

public class TextStore {
    private HashMap<String, String> dictionary = new HashMap<>(); // <영어, 한글>을 저장할 해시맵
    ArrayList<String> keys = null;
    public TextStore(){
        dictionary.put("java", "자바");
        dictionary.put("hansung", "한성");
        dictionary.put("apple", "사과");
        dictionary.put("banana", "바나나");
        dictionary.put("computer", "컴퓨터");
        dictionary.put("sunwoo", "선우");
        keys = new ArrayList<>(dictionary.keySet());
    }

    public String get(){
        int index = (int)(Math.random()*keys.size());
        return keys.get(index);
    }

    public String getMean(String key){
        return dictionary.get(key);
    }
}
