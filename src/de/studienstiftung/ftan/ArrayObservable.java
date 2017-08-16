package de.studienstiftung.ftan;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayObservable extends Observable {

    private boolean[] values;
    private double[] avg = null;
    private double[] std = null;
    private double[] gyr = null;
    private double threshold = Double.MIN_VALUE;
    private int timeshift = 0;

    public ArrayObservable(boolean[] values) {
        super();
        this.values = values;
    }

    @Override
    protected boolean[] computeValues() {
        return this.values;
    }

    public void addCreationInfo(double[] avg, double[] std, double[] gyr, double threshold){
        this.avg = avg;
        this.std = std;
        this.gyr = gyr;
        this.threshold = threshold;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public static List<ArrayObservable> generateRandom (double[][] preObservables, int n) {
        int dim = preObservables.length;
        int ts = preObservables [0].length;
        double [] [] normPreObservables = new double [dim] [ts];
        double [] avg = new double[dim];
        double [] std = new double[dim];


        for ( int j=0; j<dim; j++){
            for (int k=0; k<ts; k++){
                normPreObservables[j][k]=preObservables[j][k];
                avg[j]+=preObservables[j][k]/ts;
            }
            for (int k=0; k<ts; k++){
                std[j]+=(preObservables[j][k]-avg[j])*(preObservables[j][k]-avg[j])/ts;
            }
            std[j]=Math.sqrt(std[j]);
            for (int k=0; k<ts; k++){
                normPreObservables[j][k]= (normPreObservables[j][k] - avg[j]) / std[j];
            }
        }

        double[] gyr = new double[dim];
        for(int i = 0; i< dim; i++) gyr[i]=Math.random()*2-1;


        double temp = 0.0;
        for(int j=0; j<ts; j++) {
            temp=0;
            for(int i=0; i<dim; i++) temp+=gyr[i]*normPreObservables[i][j];
            normPreObservables[0][j]=temp;
        }

        double[] sorted = new double[ts];
        for (int i = 0; i < ts; i++) {
            sorted[i] = normPreObservables[0][i];
        }
        Arrays.sort(sorted);

        List<ArrayObservable> observables = new ArrayList<>(n);
        for(int i = 0; i < n; i++) {
            boolean[] values = new boolean[ts];
            double threshold = sorted[(ts / (n+1)) * (i+1)];
            for(int j = 0; j < ts; j++) {
                values[j] = normPreObservables[0][j] < threshold;
            }
            ArrayObservable o = new ArrayObservable(values);
            o.addCreationInfo(avg, std, gyr, threshold);
            observables.add(o);
        }

        return observables;
    }


    public ArrayObservable timeshift(int t) {
        t = Math.max(t, -this.timeshift);
        boolean[] shifted = new boolean[this.values.length];
        if(t >= 0) {
            for(int i = t; i < this.values.length; i++) {
                shifted[i] = this.values[i-t];
            }
        } else {
            for(int i = 0; i < this.values.length + t; i++) {
                shifted[i] = this.values[i-t];
            }
        }
        ArrayObservable shiftedObservable = new ArrayObservable(shifted);
        shiftedObservable.avg = this.avg;
        shiftedObservable.std = this.std;
        shiftedObservable.gyr = this.gyr;
        shiftedObservable.threshold = this.threshold;
        shiftedObservable.timeshift = this.timeshift + t;

        return shiftedObservable;
    }
}
