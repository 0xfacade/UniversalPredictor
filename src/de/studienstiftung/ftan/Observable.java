package de.studienstiftung.ftan;

import org.omg.CORBA.TIMEOUT;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {

    protected boolean[] values = null;
    private int id;
    private double mutualInformationWithDependent = -1;
    private boolean isFree = true;

    private static volatile int nextId = 0;

    public Observable() {
        this.id = nextId++;
    }

    public int getTimeshift() {
        return 0;
    }

    public int getLength() {
        ensureValuesComputed();
        return this.values.length;
    }

    private void ensureValuesComputed() {
        if (this.values == null) {
            this.values = computeValues();
        }
    }

    protected abstract boolean[] computeValues();

    public final boolean[] getValues() {
        ensureValuesComputed();
        return this.values;
    }

    public final int getId() {
        return this.id;
    }

    public CompositeObservable toCompositeObservable() {
        if (this instanceof CompositeObservable) {
            return (CompositeObservable) this;
        }
        List<Observable> subobservable = new ArrayList<>();
        subobservable.add(this);
        boolean[] function = {false, true};

        return new CompositeObservable(subobservable, function);
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Observable) {
            return ((Observable) obj).id == this.id;
        }
        return false;
    }

    public final double getMutualInformationWithDependent() {
        if (this.mutualInformationWithDependent < 0) {
            this.mutualInformationWithDependent = Stats.mutualInformation(Main.dependent, this, 500000, Main.TIMESTEPS);
        }
        return this.mutualInformationWithDependent;
    }

    public final double getCorrectnessPercent() {
        int correct = 0;
        boolean[] d = Main.dependent.getValues();
        for(int i = 500000; i < Main.TIMESTEPS; i++) {
            correct += d[i] == this.values[i] ? 1 : 0;
        }
        return correct / (double) (Main.TIMESTEPS - 500000);
    }

    public boolean isFree() {
        return this.isFree;
    }

    public void setIsFree(boolean free) {
        this.isFree = free;
    }

    public abstract int getOrder();
}
