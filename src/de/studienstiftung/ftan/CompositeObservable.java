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
        for (Observable o : this.subObservables) {
            o.setIsFree(false);
        }
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

    public CompositeObservable compressHeavy(List<Observable> addRemovedObservableTo) {
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
        Observable removedObservable = this.subObservables.get(removedIndex);
        addRemovedObservableTo.add(removedObservable);
        return max;
    }

    public CompositeObservable expandHeavy(List<Observable> candidates) {
        candidates.removeAll(this.subObservables);
        if (candidates.size() <= 0) {
            return null;
        }
        int addedIndex = -1;
        double maxValue = -1;
        CompositeObservable result = null;
        for(int i = 0; i < candidates.size(); i++) {
            List<Observable> subObservables = new ArrayList<>(this.subObservables);
            Observable candidate = candidates.get(i);
            subObservables.add(candidate);
            CompositeObservable max = Maximator.maximize(Main.dependent, subObservables, 500000);
            if (max.getMutualInformationWithDependent() > maxValue) {
                maxValue = max.getMutualInformationWithDependent();
                result = max;
                addedIndex = i;
            }
        }
        candidates.remove(addedIndex);
        return result;
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

    public CompositeObservable includeBest(List<Observable> candidates) {
        CompositeObservable max = null;
        double maxValue = -1;
        for(int i = 0; i < candidates.size(); i++) {
            List<Observable> testobservables= new ArrayList<>();
            testobservables.add(this);
            testobservables.add(candidates.get(i));
            CompositeObservable result=Maximator.maximize(Main.dependent, testobservables, 500000);


            if (result.getMutualInformationWithDependent() > maxValue) {
                maxValue = result.getMutualInformationWithDependent();
                max = result;
            }

        }
        return max;
    }

    public List<Observable> getSubObservables() {
        return this.subObservables;
    }

    @Override
    public int getOrder() {
        int maxSubs = -1;
        for (int i = 0; i < this.subObservables.size(); i++) {
            int childOrder = this.subObservables.get(i).getOrder();
            if (childOrder > maxSubs) {
                maxSubs = childOrder;
            }
        }
        return maxSubs + 1;
    }
}
