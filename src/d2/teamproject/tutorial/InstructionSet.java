package d2.teamproject.tutorial;

import java.util.ArrayList;

public class InstructionSet {
    private ArrayList<Instruction> set;
    private int tracker;

    public InstructionSet(ArrayList<Instruction> set)
    {
        tracker = 0;
        this.set = set;
    }

    public Instruction getNext() {
        //get the nexdt element in the array
        tracker++;
        return set.get(tracker+1);
    }

    public Instruction getPrev() {
        //get the previous element in the array
        tracker--;
        return set.get(tracker-1);
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