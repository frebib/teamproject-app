package d2.teamproject.tutorial;

import com.eclipsesource.json.JsonArray;
import javafx.scene.layout.StackPane;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Parth Chandratreya
 */
public class Tutorial extends InstructionSet {

    public StackPane TutorialPane = new StackPane();
    public Map<String, Instruction> tutorialMap;

    public Tutorial(JsonArray jsonSet) {
        super(jsonSet);
        tutorialMap = new LinkedHashMap<>();
        initialise();
    }

    public void initialise() {
        while (hasNext()) {
            Instruction instruction = getNext();
            tutorialMap.put(instruction.getKey(), instruction);
        }
    }

    public Instruction getInstruction(String key) {
        return tutorialMap.get(key);
    }
}
