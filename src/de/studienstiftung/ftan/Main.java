package de.studienstiftung.ftan;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Main {

    public static final int TIMESTEPS = 10000000;
    public static final int PREHISTORY_STEPS = 0;

    public static void main(String[] args) throws Exception {

        LorentzAttractor x = new LorentzAttractor(PREHISTORY_STEPS, TIMESTEPS);

        boolean[] s1 = new boolean[TIMESTEPS];
        boolean[] s2 = new boolean[TIMESTEPS];
        boolean[] s3 = new boolean[TIMESTEPS];
        boolean[][] S = new boolean[20][TIMESTEPS];


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

        for(int i = 0; i < TIMESTEPS-4; i++) {
            S[16][i+4] = x.xyzs[i][1] > 0;
            S[17][i+4] = x.xyzs[i][1] > -10;
            S[18][i+4] = x.xyzs[i][1] > 5;
            S[19][i+4] = x.xyzs[i][1] < -5;
        }

        for (int i = 0; i < 20; i++) {
            System.out.println(i + ": " + Stats.mutualInformation(S[i], x.dependentVariable,500000, TIMESTEPS) + ", " + Stats.mutualInformation(S[i], S[i],500000, TIMESTEPS));
        }

        for (int i = 0; i < 20; i++) {
            System.out.println(Stats.mutualInformation(S[i], x.dependentVariable,500000, TIMESTEPS));
        }



        boolean[] max = Maximator.maximize(x.dependentVariable, S, 500000);
        System.out.println("Max: " + Stats.mutualInformation(x.dependentVariable, max,500000, TIMESTEPS));
        System.out.println("H(Dep): " + Stats.mutualInformation(x.dependentVariable, x.dependentVariable,500000, TIMESTEPS));

    }
}
