package de.studienstiftung.ftan;

import java.util.List;

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

    public static CompositeObservable maximize(Observable dependentObservable, List<Observable> s, int maxTimesteps){
        int observables = s.size();
        int timesteps = Math.min(dependentObservable.getLength(), maxTimesteps);
        int states = 1 << observables;

        boolean[] dependent = dependentObservable.getValues();

        int buchhaltung[][] = new int[states][2];

        for(int timestep = 0; timestep < timesteps; timestep++) {
            int state = stateAt(s, timestep);
            buchhaltung[state][dependent[timestep] ? 1 : 0]++;
        }
        boolean[] function = new boolean[states];
        for(int i = 0; i < states; i++) {
            function[i] = buchhaltung[i][1] > buchhaltung[i][0];
        }
        return new CompositeObservable(s, function);
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

    public static int stateAt(List<Observable> s, int timestep) {
        int state = 0;
        for(int i = 0; i < s.size(); i++) {
            state += s.get(i).getValues()[timestep] ? 1 : 0;
            state <<= 1;
        }
        state >>= 1;
        return state;
    }
}
