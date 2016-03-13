package d2.teamproject.tutorial;

import com.eclipsesource.json.JsonArray;
import d2.teamproject.PARTH;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Parth Chandratreya
 */
public class Tutorial extends InstructionSet {

    public StackPane TutorialPane = new StackPane();
    public Map<String,Instruction> tutorialMap;

    public Tutorial(JsonArray jsonSet) {
        super(jsonSet);

        tutorialMap = new LinkedHashMap<>();
        initialise();
    }

    public void initialise(){
        while (hasNext()){
            Instruction instruction = getNext();
            tutorialMap.put(instruction.getKey(),instruction);
        }
    }

    // LOAD PLANETS
    // new Tutorial
    // loadAllInformation
    // isTutorial(false)
    // ---
    // Every button press -> changeTextFlow
    // ---
    // Turn on mode -> isTutorial(Boolean button)

    public Instruction getInstruction(String key){
        return tutorialMap.get(key);
    }

    public void isTutorial(Boolean bool){
        TutorialPane.setVisible(bool);
    }

    @Deprecated
    public StackPane loadAllInformation(){
        while(hasNext()){
            Instruction current = getNext();
            Text title = new Text(current.getTitle());
            PARTH.LOG.info(current.getKey());
            PARTH.LOG.info(current.getTitle());
            Text desc = new Text(current.getDesc());
            TextFlow textFlow = new TextFlow(title,desc);
            textFlow.setVisible(false);
            TutorialPane.getChildren().add(textFlow);
        }
        return TutorialPane;
    }

    public void changeTextFlow(int changeStep){
        TutorialPane.getChildren().get(count()).setVisible(false);
        TutorialPane.getChildren().get(count()+changeStep).setVisible(true);
    }
}
