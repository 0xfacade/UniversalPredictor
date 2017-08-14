package de.studienstiftung.ftan;

abstract public class DynamicSystem {

    protected int timesteps;
    protected int dimension;

    private double[][] preObservables;
    private boolean[] dependentVariable;

    public DynamicSystem(int timesteps, int dimension){
        this.timesteps = timesteps;
        this.dimension = dimension;
        this.preObservables = computePreObservables();
        this.dependentVariable = computeDependentVariable();
    }

    /**
     * Array of hidden overservables.
     * @return 2-D Array: x[t][n] where t is the
     * timestep and n the nth observable at that timestep
     */
    abstract protected double[][] computePreObservables();

    abstract protected boolean[] computeDependentVariable();

    public double[][] getPreObservables() {
        return preObservables;
    }

    public boolean[] getDependentVariable() {
        return dependentVariable;
    }

}
