package de.studienstiftung.ftan;

public class Maximator {

    public static boolean[] maximize(boolean[] dependent, boolean[][] S, int maxTimesteps) {
        int observables = S.length;
        int timesteps = Math.min(dependent.length, maxTimesteps);

        int states = 1 << observables;
        int buchhaltung[][] = new int[states][2];

        for(int timestep = 0; timestep < timesteps; timestep++) {
            int state = stateAt(S, timestep);
            buchhaltung[state][dependent[timestep]?1:0]++;
        }
        boolean[] output = new boolean[dependent.length];
        for(int i = 0; i < dependent.length; i++) {
            int state = stateAt(S, i);
            output[i] = buchhaltung[state][1] > buchhaltung[state][0];
        }
        return output;
    }

    public static int stateAt(boolean[][] S, int timestep) {
        int state=0;
        for(int i=0; i<S.length; i++) {
            state+=S[i][timestep]?1:0;
            state=state << 1;
        }
        state=state >> 1;
        return state;
    }
}
