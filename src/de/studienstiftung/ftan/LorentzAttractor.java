package de.studienstiftung.ftan;


import java.io.IOException;
import java.io.PrintWriter;

public class LorentzAttractor {

    private static final int TRANS = 10000;
    private static final double SIGMA = 10;
    private static final double RHO = 28;
    private static final double BETA = 8/3.;
    private static final double DELTA = 10e-4;
    private static final int DELTAS_PER_STEP = 500;

    private int timesteps;
    double[][] xyzs;
    boolean[] dependentVariable;

    public LorentzAttractor(int timesteps) {
        this.timesteps = timesteps;
        computeAll();
    }

    private void computeAll() {

        this.xyzs = new double[3][this.timesteps];
        dependentVariable = new boolean[this.timesteps];

        double x = 1;
        double y = 1;
        double z = 1;

        for(int step = 0; step < TRANS; step++) {
            for(int deltaStep = 0; deltaStep < DELTAS_PER_STEP; deltaStep++) {
                double xNew = x + DELTA * (SIGMA * (y - x));
                double yNew = y + DELTA * (x * (RHO - z) - y);
                double zNew = z + DELTA * (x * y - BETA * z);
                x = xNew;
                y = yNew;
                z = zNew;
            }
        }

        for(int step = 0; step < this.timesteps; step++) {
            xyzs[0][step] = x;
            xyzs[1][step] = y;
            xyzs[2][step] = z;
            for(int deltaStep = 0; deltaStep < DELTAS_PER_STEP; deltaStep++) {
                double xNew = x + DELTA * (SIGMA * (y - x));
                double yNew = y + DELTA * (x * (RHO - z) - y);
                double zNew = z + DELTA * (x * y - BETA * z);
                x = xNew;
                y = yNew;
                z = zNew;
            }
            dependentVariable[step] = x > 0;
        }
    }

    public void dump() throws IOException {
        PrintWriter out = new PrintWriter("output.csv");
        for (int step = 0; step < timesteps; step++){
            out.println(xyzs[0][step] + "," + xyzs[1][step] + ","
                    + xyzs[2][step] + "," + (dependentVariable[step] ? '1' : '0'));
        }
        out.close();
    }
}
