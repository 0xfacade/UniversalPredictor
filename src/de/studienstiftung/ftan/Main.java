package de.studienstiftung.ftan;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Main {

    public static final int TIMESTEPS = 2500000;

    public static Observable dependent;

    public static void main(String[] args) throws Exception {

        LorentzAttractor x = new LorentzAttractor(TIMESTEPS);
        dependent = new ArrayObservable(x.dependentVariable);

        boolean[] random = new boolean[TIMESTEPS];
        for (int i = 0; i < TIMESTEPS; i++) {
            random[i] = Math.random() < 0.5;
        }

        List<Observable> superComposites = new ArrayList<>();

        for(int n = 0; n < 7; n++) {
            List<Observable> composites = new ArrayList<>();
            for(int k = 0; k < 10; k++) {
                List<ArrayObservable> generated = ArrayObservable.generateRandom(x.xyzs, 4);
                List<Observable> observables = new ArrayList<>(generated);
                for(int i = 0; i < 4; i++) {
                    ArrayObservable o = generated.get(i);
                    for(int j = 1; j < 4; j++) {
                        ArrayObservable shifted = o.timeshift(j);
                        observables.add(shifted);
                    }
                }

                CompositeObservable r = observables.get(1).toCompositeObservable();
                for(int i = 0; i < 6; i++) {
                    r = r.expandHeavy(observables);
                }
                r = r.compressHeavy(observables);
                r = r.compressHeavy(observables);
                composites.add(r);
                System.out.println("Generated composite with " + r.getMutualInformationWithDependent());
            }

            Observable max = Maximator.maximize(dependent, composites, 500000);
            System.out.println(max.getMutualInformationWithDependent());
            System.out.println(max.getCorrectnessPercent());

            superComposites.add(max);
        }

        Observable superMax = Maximator.maximize(dependent, superComposites, 500000);
        System.out.println(superMax.getMutualInformationWithDependent());
        System.out.println(superMax.getCorrectnessPercent());


        /*
        CompositeObservable max = Maximator.maximize(dependent, observables, 500000);
        System.out.println("Max: " + Stats.mutualInformation(dependent, max, 500000, TIMESTEPS));


        CompositeObservable compressed = max;
        for(int i = 0; i < 15; i++) {
            compressed = compressed.compressHeavy();
            System.out.println(i + "th compression step: " + compressed.getMutualInformationWithDependent());
        }


        System.out.println("H(Dep): " + Stats.mutualInformation(x.dependentVariable, x.dependentVariable,500000, TIMESTEPS));

        Comparator<Observable> comparator = new Comparator<Observable>() {
            @Override
            public int compare(Observable o1, Observable o2) {
                double m1 = o1.getMutualInformationWithDependent();
                double m2 = o2.getMutualInformationWithDependent();
                return Double.compare(m2, m1);
            }
        };

        PriorityQueue<Observable> pq = new PriorityQueue<>(comparator);
        pq.addAll(observables);

        CompositeObservable expanded = pq.remove().toCompositeObservable();
        for(int i = 0; i < 15; i++) {
            expanded = expanded.expandHeavy(observables);
            System.out.println(i + "th expansion step: " + expanded.getMutualInformationWithDependent());
        }

        System.out.println(expanded.getCorrectnessPercent());
        */

    }

}
