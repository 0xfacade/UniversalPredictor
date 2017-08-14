package de.studienstiftung.ftan;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Main {

    public static final int TIMESTEPS = 10000;
    public static final int PREHISTORY_STEPS = 0;

    public static void main(String[] args) throws Exception {

        LorentzAttractor x = new LorentzAttractor(PREHISTORY_STEPS, TIMESTEPS);

        boolean[] s1 = new boolean[TIMESTEPS];
        boolean[] s2 = new boolean[TIMESTEPS];

        for(int i = 0; i < TIMESTEPS; i++) {
            s1[i] = x.xyzs[i][0] > 0;
            s2[i] = x.xyzs[i][1] > 0;
        }

        System.out.println(Stats.mutualInformation(x.dependentVariable, s1));
        System.out.println(Stats.mutualInformation(x.dependentVariable, s2));
        System.out.println(Stats.mutualInformation(s1, s2));
        System.out.println(Stats.mutualInformation(s2, s1));
        System.out.println(Stats.mutualInformation(s2, s2));

        x.dump();

    }
}
