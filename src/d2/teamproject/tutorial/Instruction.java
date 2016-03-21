package d2.teamproject.tutorial;

import java.util.Arrays;
import java.util.List;

/**
 * @author Parth Chandratreya
 * @author Gulraj Bariah
 * @author Luke Taher
 */
public class Instruction {
    private String key;
    private String title;
    private String desc;
    private List<Pair<Integer, Integer>> visChanges;

    public Instruction(String title, String desc, String key, Pair<Integer, Integer>... visChanges) {
        this.title = title;
        this.desc = desc;
        this.key = key;
        this.visChanges = Arrays.asList(visChanges);
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getKey() {
        return key;
    }

    public Boolean isNextVisChange() {
        return visChanges.size() > 0;
    }

    public Pair<Integer, Integer> getNextVisChange() {
        Pair<Integer, Integer> next = visChanges.get(visChanges.size() - 1);
        visChanges.remove(visChanges.size() - 1);
        return next;
    }

}
