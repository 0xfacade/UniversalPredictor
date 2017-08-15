package de.studienstiftung.ftan;

/**
 * Created by ocius on 14.08.17.
 */
public class Stats {

    public static double mutualInformation(boolean[] X, boolean[] Y, int timeFrom, int timeTo) {
        double length = X.length;
        timeTo=(int)Math.min(timeTo,length);
        timeFrom=(int)Math.max(Math.min(timeTo,timeFrom),0);
        length=timeTo-timeFrom;
        int xs = 0, ys = 0, ff = 0, ft = 0, tf = 0, tt = 0;
        for (int i = timeFrom; i < timeTo; i++) {
            if(X[i]) xs++;
            if(Y[i]) ys++;
            if(!X[i] && !Y[i]) ff++;
            if(!X[i] && Y[i]) ft++;
            if(X[i] && !Y[i]) tf++;
            if(X[i] && Y[i]) tt++;
        }
        return plogp(ff / length, (length - xs) / length, (length - ys) / length)
                + plogp(ft / length, (length - xs) / length, ys / length)
                + plogp(tf / length, xs / length, (length - ys) / length)
                + plogp(tt / length, xs / length, ys / length);
    }

    private static double plogp(double pxy, double px, double py) {
        if (pxy == 0) {
            return 0;
        }
        return pxy * Math.log(pxy / px / py) / Math.log(2);
    }
}
