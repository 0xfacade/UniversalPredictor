package de.studienstiftung.ftan;

public class ArrayObservable extends Observable {

    private boolean[] values;

    public ArrayObservable(boolean[] values) {
        super();
        this.values = values;
    }

    @Override
    protected boolean[] computeValues() {
        return this.values;
    }
}
