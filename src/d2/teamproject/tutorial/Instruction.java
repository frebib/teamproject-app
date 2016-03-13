package d2.teamproject.tutorial;

import java.util.ArrayList;

/**
 * @author Parth Chandratreya
 */
public class Instruction {
    private String key;
    private String title;
    private String desc;
    private ArrayList<Pair<Integer, Integer>> visChanges;

    public Instruction(String title, String desc, String key, ArrayList<Pair<Integer, Integer>> visChanges) {
        this.title = title;
        this.desc = desc;
        this.key = key;
        this.visChanges = visChanges;
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
