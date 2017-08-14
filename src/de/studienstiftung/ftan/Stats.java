package de.studienstiftung.ftan;

/**
 * Created by ocius on 14.08.17.
 */
public class Stats {

    public static double mutualInformation(boolean[] X, boolean[] Y) {
        double length = X.length;
        int xs = 0, ys = 0, ff = 0, ft = 0, tf = 0, tt = 0;
        for (int i = 0; i < length; i++) {
            if(X[i]) xs++;
            if(Y[i]) ys++;
            if(!X[i] && !Y[i]) ff++;
            if(!X[i] && Y[i]) ft++;
            if(X[i] && !Y[i]) tf++;
            if(X[i] && Y[i]) tt++;
        }
        return ff / length * Math.log((ff / length) / (((length - xs) / length) * ((length - ys) / length)) ) / Math.log(2)
                + ft / length * Math.log((ft / length) / (((length - xs) / length) * ((ys) / length)) ) / Math.log(2)
                + tf / length * Math.log((tf / length) / (((xs) / length) * ((length - ys) / length)) ) / Math.log(2)
                + tt / length * Math.log((tt / length) / (((xs) / length) * ((ys) / length)) ) / Math.log(2);
    }
}
