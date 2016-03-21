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

    /**
     * @param title this is the header of each instruction
     * @param desc this is the description of each instruction
     * @param key this is the unique key for the element inside the array
     * @param visChanges changes that have been made in the visualisation that need to be displayed
     */
    public Instruction(String title, String desc, String key, Pair<Integer, Integer>... visChanges) {
        this.title = title;
        this.desc = desc;
        this.key = key;
        this.visChanges = Arrays.asList(visChanges);
    }

    /**
     * @return returns the title of the the instruction
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return returns the description of the instruction
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @return returns the key of the insruction
     */
    public String getKey() {
        return key;
    }

    /**
     * @return returns boolean based on whether or not the visualisation needs to change
     */
    public Boolean isNextVisChange() {
        return visChanges.size() > 0;
    }

    /**
     * @return returns the next visualisation change we need to do
     */
    public Pair<Integer, Integer> getNextVisChange() {
        Pair<Integer, Integer> next = visChanges.get(visChanges.size() - 1);
        visChanges.remove(visChanges.size() - 1);
        return next;
    }

}
