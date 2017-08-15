package de.studienstiftung.ftan;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int TIMESTEPS = 2500000;
    public static final int PREHISTORY_STEPS = 0;

    public static Observable dependent;

    public static void main(String[] args) throws Exception {

        LorentzAttractor x = new LorentzAttractor(PREHISTORY_STEPS, TIMESTEPS);
        dependent = new ArrayObservable(x.dependentVariable);

        boolean[] random = new boolean[TIMESTEPS];
        for (int i = 0; i < TIMESTEPS; i++) {
            random[i] = Math.random() < 0.5;
        }

        boolean[] s1 = new boolean[TIMESTEPS];
        boolean[] s2 = new boolean[TIMESTEPS];
        boolean[] s3 = new boolean[TIMESTEPS];
        boolean[][] S = new boolean[16][TIMESTEPS];

        for(int i = 0; i < TIMESTEPS; i++) {
            s1[i] = x.xyzs[i][0] > 0;
            s2[i] = x.xyzs[i][1] > 0;
            S[4][i] = x.xyzs[i][1] > 0;
            S[5][i] = x.xyzs[i][1] > -10;
            S[6][i] = x.xyzs[i][1] > 5;
            S[7][i] = x.xyzs[i][1] < -5;

        }

        for(int i = 0; i < TIMESTEPS-1; i++) {
            S[0][i+1] = x.xyzs[i][1] > 0;
            S[1][i+1] = x.xyzs[i][1] > -10;
            S[2][i+1] = x.xyzs[i][1] > 5;
            S[3][i+1] = x.xyzs[i][1] < -5;
        }

        for(int i = 0; i < TIMESTEPS-2; i++) {
            S[8][i+2] = x.xyzs[i][1] > 0;
            S[9][i+2] = x.xyzs[i][1] > -10;
            S[10][i+2] = x.xyzs[i][1] > 5;
            S[11][i+2] = x.xyzs[i][1] < -5;
        }

        for(int i = 0; i < TIMESTEPS-3; i++) {
            S[12][i+3] = x.xyzs[i][1] > 0;
            S[13][i+3] = x.xyzs[i][1] > -10;
            S[14][i+3] = x.xyzs[i][1] > 5;
            S[15][i+3] = x.xyzs[i][1] < -5;
        }


        List<Observable> observables = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            observables.add(new ArrayObservable(S[i]));
        }
        Observable randomObservable = new ArrayObservable(random);
        System.out.println("Random observable id is " + randomObservable.getId());
        observables.add(randomObservable);


        for (int i = 0; i < 16; i++) {
            System.out.println(i + ": " + Stats.mutualInformation(observables.get(i), dependent,500000, TIMESTEPS) + ", " + Stats.mutualInformation(S[i], S[i],500000, TIMESTEPS));
        }

        CompositeObservable max = Maximator.maximize(dependent, observables, 500000);
        System.out.println("Max: " + Stats.mutualInformation(dependent, max, 500000, TIMESTEPS));

        CompositeObservable compressed = max;
        for(int i = 0; i < 15; i++) {
            compressed = compressed.compressHeavy();
            System.out.println(i + "th compression step: " + compressed.getMutualInformationWithDependent());
        }


        System.out.println("H(Dep): " + Stats.mutualInformation(x.dependentVariable, x.dependentVariable,500000, TIMESTEPS));

    }
}
