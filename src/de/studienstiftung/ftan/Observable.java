package de.studienstiftung.ftan;

public abstract class Observable {

    protected boolean[] values = null;
    private int id;
    private double mutualInformationWithDependent = -1;

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
}
