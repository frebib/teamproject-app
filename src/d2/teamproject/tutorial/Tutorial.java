package d2.teamproject.tutorial;

import com.eclipsesource.json.JsonArray;
import javafx.scene.layout.StackPane;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Parth Chandratreya
 * @author Gulraj Bariah
 * @author Luke Taher
 */
public class Tutorial extends InstructionSet {

    public StackPane TutorialPane = new StackPane();
    public Map<String, Instruction> tutorialMap;

    /**
     * @param jsonSet this is the array of the json tutorial instructions
     */
    public Tutorial(JsonArray jsonSet) {
        super(jsonSet);
        tutorialMap = new LinkedHashMap<>();
        initialise();
    }

    /**
     *
     */
    public void initialise() {
        while (hasNext()) {
            Instruction instruction = getNext();
            tutorialMap.put(instruction.getKey(), instruction);
        }
    }

    /**
     * @param key this is the unique key of an element in the array of instructions
     * @return this returns the instruction that matches the key
     */
    public Instruction getInstruction(String key) {
        return tutorialMap.get(key);
    }
}
