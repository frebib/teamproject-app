package d2.teamproject.tutorial;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;

/**
 * @author Parth Chandratreya
 * @author Gulraj Bariah
 * @author Luke Taher
 *
 * These are the get methods produced to allow us to use the instruction set we have created. This insruction set is directly used for the
 * tutorial aspect of our application.
 */
public class InstructionSet {
    private ArrayList<Instruction> set;
    private int tracker;

    /**
     * @param jsonSet this is the array of the json tutorial instructions
     */
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

    /**
     * @return this returns the next element in the array
     */
    public Instruction getNext() {
        return set.get(tracker++);
    }

    /**
     * @return this returns the previous element in the array
     */
    public Instruction getPrev() {
        //get the previous element in the array
        return set.get(tracker--);
    }

    /**
     * @return returns the array element that we're on
     */
    public int count() {
        //the array element we're on
        return set.size();
    }

    /**
     * @return returns boolean value based on if the current element is the last in the list
     */
    public boolean hasNext() {
        //check if we are at the end of the array
        return tracker < set.size();
    }

    /**
     * @return returns boolean value based on if the current element is the first in the list
     */
    public boolean hasPrev() {
        //to check if we are at the beginning of the array
        return tracker > 0;
    }
}