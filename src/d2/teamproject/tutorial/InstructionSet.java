package d2.teamproject.tutorial;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;

/**
 * @author Parth Chandratreya
 * @author Luke Taher
 */
public class InstructionSet {
    private ArrayList<Instruction> set;
    private int tracker;

    public InstructionSet(JsonArray jsonSet) {
        tracker = 0;
        set = new ArrayList<>();
        for (JsonValue jsonValue : jsonSet) {
            JsonObject obj = jsonValue.asObject();
            set.add(new Instruction(obj.get("title").asString(),
                    obj.get("content").asString(), obj.get("key").asString()));
        }
    }

    public Instruction getCurrent() {
        return set.get(tracker);
    }

    public Instruction getNext() {
        //get the next element in the array
        return set.get(tracker++);
    }

    public Instruction getPrev() {
        //get the previous element in the array
        return set.get(tracker--);
    }

    public int count() {
        //the array element we're on
        return set.size();
    }

    public boolean hasNext() {
        //check if we are at the end of the array
        return tracker < set.size();
    }

    public boolean hasPrev() {
        //to check if we are at the beginning of the array
        return tracker > 0;
    }
}