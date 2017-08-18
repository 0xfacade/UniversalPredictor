package de.studienstiftung.ftan;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int TIMESTEPS = 2500000;

    public static Observable dependent;

    public static void main(String[] args) throws Exception {

        System.out.println("Generating dynamics..");
        LorentzAttractor x = new LorentzAttractor(TIMESTEPS);
        x.dump();
        dependent = new ArrayObservable(x.dependentVariable);

        System.out.println("Dynamics generated, starting to generate comoposite observables!");

        List<Observable> superComposites = new ArrayList<>();
        List<Thread> threads = new ArrayList<>(7);

        for(int n = 0; n < 7; n++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
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
                        System.out.println("Generated single composite with " + r.getMutualInformationWithDependent());
                    }

                    System.out.println("Now generating a meta composite..");
                    Observable max = Maximator.maximize(dependent, composites, 500000);
                    System.out.println(max.getMutualInformationWithDependent() + " mutual information");
                    System.out.println((max.getCorrectnessPercent() * 100) + "% correct prediction");

                    synchronized (superComposites) {
                        superComposites.add(max);
                    }
                }
            });
            threads.add(t);
            t.start();
        }

        for(Thread t : threads) {
            t.join();
        }

        System.out.println("All composites generated. Will now generate super composite.");
        Observable superMax = Maximator.maximize(dependent, superComposites, 500000);
        System.out.println("Mutual information: " + superMax.getMutualInformationWithDependent());
        System.out.println("Correctness: " + (superMax.getCorrectnessPercent() * 100) + "%");
    }

}
