package insertnamehere.com.github.datagatherer.util;

import java.util.ArrayList;
import java.util.stream.DoubleStream;

public class DataMath {

    public static float[] getMeanArray(ArrayList<float[]> values) {
        return new float[] {
                (float)getMean(values, 0),
                (float)getMean(values, 1),
                (float)getMean(values, 2)
        };
    }

    public static float[] getVarianceArray(ArrayList<float[]> values, float[] mean) {
        return new float[] {
                (float)getVariance(values, 0, mean[0]),
                (float)getVariance(values, 1, mean[1]),
                (float)getVariance(values, 2, mean[2])
        };
    }

    public static float[] getStandardDeviationArray(float[] variance) {
        return new float[] {
                (float)getStandardDeviation(variance[0]),
                (float)getStandardDeviation(variance[1]),
                (float)getStandardDeviation(variance[2])
        };
    }

    public static double getMean(ArrayList<float[]> valueList, int index) {
        DoubleStream values = valueList.stream().mapToDouble(f -> f[index]);
        return values.average().orElse(0);
    }

    public static double getMean(ArrayList<Float> valueList) {
        DoubleStream values = valueList.stream().mapToDouble(f -> f);
        return values.average().orElse(0);
    }

    public static double getVariance(ArrayList<float[]> valueList, int index, double mean) {
        ArrayList<Double> squaredDiffs = new ArrayList<>();
        for (float[] value : valueList) {
            double diff = mean - value[index];
            squaredDiffs.add(Math.pow(diff, 2));
        }
        double sum = squaredDiffs.stream().mapToDouble(d -> d).sum();
        return sum / (squaredDiffs.size()-1);
    }

    public static double getVariance(ArrayList<Float> valueList, double mean) {
        ArrayList<Double> squaredDiffs = new ArrayList<>();
        for (float value : valueList) {
            double diff = mean - value;
            squaredDiffs.add(Math.pow(diff, 2));
        }
        double sum = squaredDiffs.stream().mapToDouble(d -> d).sum();
        return sum / (squaredDiffs.size()-1);
    }

    public static double getStandardDeviation(double variance) {
        return Math.sqrt(variance);
    }
}
