package insertnamehere.com.github.datagatherer.util.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.DoubleStream;

public class Data {
    String name;
    double[][] data;
    long[][] time;

    public Data(String name, double[][] data, long[][] time){
        this.name = name;
        this.data = data;
        this.time = time;
    }

    public double[] getMeanArray() {
        return new double[] {
                getMean(data[0]),
                getMean(data[1]),
                getMean(data[2])
        };
    }

    public double[] getVarianceArray(double[] mean) {
        return new double[] {
                getVariance(data[0], mean[0]),
                getVariance(data[1], mean[1]),
                getVariance(data[2], mean[2])
        };
    }

    public double[] getStandardDeviationArray(double[] variance) {
        return new double[] {
                getStandardDeviation(variance[0]),
                getStandardDeviation(variance[1]),
                getStandardDeviation(variance[2])
        };
    }

    private static double getMean(double[] valuesArray) {
        DoubleStream values = Arrays.stream(valuesArray);
        return values.average().orElse(0);
    }

    private static double getVariance(double[] valuesArray, double mean) {
        ArrayList<Double> squaredDiffs = new ArrayList<>();
        for (double value : valuesArray) {
            double diff = mean - value;
            squaredDiffs.add(Math.pow(diff, 2));
        }
        double sum = squaredDiffs.stream().mapToDouble(d -> d).sum();
        return sum / (squaredDiffs.size()-1);
    }

    private static double getStandardDeviation(double variance) {
        return Math.sqrt(variance);
    }
}
