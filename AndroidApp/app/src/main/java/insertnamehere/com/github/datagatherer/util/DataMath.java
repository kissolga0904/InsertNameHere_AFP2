package insertnamehere.com.github.datagatherer.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class DataMath {

    static Map<String, Data> DATA = new HashMap<>();

    public static void clearDataList(){
        DATA.clear();
    }

    public static void addData(String name, Data data){
        DATA.put(name, data);
    }

    public static Data createSensorData(ArrayList<float[]> values,
                                        ArrayList<Long> timestamps, float[] bias){
        List<float[]> dataValues = new ArrayList<>();
        List<Long> dataTimestamps = timestamps.stream().distinct().collect(Collectors.toList());
        for (int i = 0; i < dataTimestamps.size(); i++) {
            float[] summed = new float[3];
            int startIndex = timestamps.indexOf(dataTimestamps.get(i));
            int endIndex = 0;
            for (int j = startIndex; j < values.size(); j++) {
                if (timestamps.get(j).equals(dataTimestamps.get(i))) {
                    summed[0] += values.get(j)[0];
                    summed[1] += values.get(j)[1];
                    summed[2] += values.get(j)[2];
                }
                else {
                    endIndex = j;
                    break;
                }
            }
            int size = endIndex-startIndex+1;
            summed[0] /= size;
            summed[1] /= size;
            summed[2] /= size;
            dataValues.add(summed);
        }
        long first = dataTimestamps.get(0);
        dataTimestamps = dataTimestamps.stream().map(l -> l-first).collect(Collectors.toList());
        return new Data(dataValues, dataTimestamps, bias);
    }

    public static Map<String, Data> getData() {
        return DATA;
    }

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
