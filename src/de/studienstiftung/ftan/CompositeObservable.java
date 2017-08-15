package de.studienstiftung.ftan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 15.08.2017.
 */
public class CompositeObservable extends Observable {

    private List<Observable> subObservables;
    private boolean[] function;

    public CompositeObservable(List<Observable> subObservables, boolean[] function) {
        this.subObservables = subObservables;
        this.function = function;
    }

    private CompositeObservable removeSubobservable(int index) {
        if (this.getNumberOfSubobservables() <= 1) {
            throw new IndexOutOfBoundsException("Cannot remove last observable!");
        }
        ArrayList<Observable> clone = new ArrayList<>(this.subObservables);
        clone.remove(index);
        return Maximator.maximize(Main.dependent, clone, 500000);
    }

    public CompositeObservable compressHeavy() {
        CompositeObservable max = null;
        int removedIndex = -1;
        double maxValue = -1;
        for(int i = 0; i < this.subObservables.size(); i++) {
            CompositeObservable reduced = this.removeSubobservable(i);
            if (reduced.getMutualInformationWithDependent() > maxValue) {
                maxValue = reduced.getMutualInformationWithDependent();
                max = reduced;
                removedIndex = i;
            }
        }
        System.out.println("Removed variable " + this.subObservables.get(removedIndex).getId()
                + " with mutual info of "
                + this.subObservables.get(removedIndex).getMutualInformationWithDependent());
        return max;
    }

    @Override
    protected boolean[] computeValues() {
        int length = subObservables.get(0).getLength();
        boolean[] values = new boolean[length];
        for (int i = 0; i < length; i++) {
            int state = Maximator.stateAt(this.subObservables, i);
            values[i] = this.function[state];
        }
        return values;
    }

    public final int getNumberOfSubobservables() {
        return this.subObservables.size();
    }
}
