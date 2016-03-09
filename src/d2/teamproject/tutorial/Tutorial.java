package d2.teamproject.tutorial;

import com.eclipsesource.json.JsonArray;
import d2.teamproject.PARTH;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


public class Tutorial extends InstructionSet {

    public StackPane TutorialPane = new StackPane();

    public Tutorial(JsonArray jsonSet) {
        super(jsonSet);
    }

    // LOAD PLANETS
    // new Tutorial
    // loadAllInformation
    // isTutorial(false)
    // ---
    // Every button press -> changeTextFlow
    // ---
    // Turn on mode -> isTutorial(Boolean button)

    public void isTutorial(Boolean bool){
        TutorialPane.setVisible(bool);
    }

    public StackPane loadAllInformation(){
        while(hasNext()){
            Instruction current = getNext();
            Text title = new Text(current.getTitle());
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
