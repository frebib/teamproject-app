package d2.teamproject.tutorial;

import com.eclipsesource.json.JsonArray;


public class Tutorial extends InstructionSet {

    public Tutorial(JsonArray jsonSet) {
        super(jsonSet);
    }

    @Override
    public Instruction getCurrent() {
        return super.getCurrent();
    }

    @Override
    public Instruction getNext() {
        return super.getNext();
    }

    @Override
    public Instruction getPrev() {
        return super.getPrev();
    }

    @Override
    public int count() {
        return super.count();
    }

    @Override
    public boolean hasNext() {
        return super.hasNext();
    }

    @Override
    public boolean hasPrev() {
        return super.hasPrev();
    }

    public void getStepInfomation() {
        if (hasNext()) {
            Instruction current = getCurrent();
            String title = current.getTitle();
            String desc = current.getDesc();
        }
    }
}
