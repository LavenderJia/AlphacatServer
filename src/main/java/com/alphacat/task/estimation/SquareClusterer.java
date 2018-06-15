package com.alphacat.task.estimation;

import com.alphacat.pojo.SquareTag;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SquareClusterer {

    /**
     * Apply k-means algorithm on rectangles of each picture(denoted by picture id).
     * @param tags the original data set without being clustered
     * @return a map recording the clusters of each picture, with its keys representing
     *          picture id and its values representing the clusters.
     */
    public Map<Integer, List<List<SquareTag>>> kMeans(List<SquareTag> tags) {
        return tags.stream().collect(Collectors.groupingBy(
                SquareTag::getPicIndex,
                Collectors.collectingAndThen(Collectors.toList(), this::singleKMeans)
        ));
    }

    /**
     * @param tags Square tags on the same picture of the same task. Their identification is the (workerId, squareIndex)
     *             tuple.
     */
    private List<List<SquareTag>> singleKMeans(List<SquareTag> tags) {
        final int k = (int) tags.stream().collect(Collectors.groupingBy(SquareTag::getWorkerId, Collectors.counting()))
                .entrySet().stream().mapToInt(e -> e.getValue().intValue()).average().getAsDouble();
        List<SquareTag> centers = new ArrayList<>(k);
        List<List<SquareTag>> clusters = new ArrayList<>(k);
        // initialization
        for(int i = 0; i < k; i++) {
            clusters.add(new ArrayList<>());
        }
        if(tags.isEmpty()) {
            return clusters;
        }
        for(int j= 0; j < tags.size() && centers.size() < k; j++) {
            SquareTag s = tags.get(j);
            if(! this.contains(centers, s)) {
                centers.add(s);
            }
        }
        if(centers.size() < k) {
            System.out.println("Cannot initialize clusters. Data too similar.");
            return clusters;
        }
        // iteration
        boolean coveraged = false;
        final double stdDiff = 1.0;
        while(!coveraged) {
            clusters.forEach(List::clear);
            tags.forEach(t -> {
                int index = 0;
                double min = this.distance(t, centers.get(index));
                for(int i = 1; i < k; i++) {
                    double tmp = this.distance(t, centers.get(i));
                    if(tmp < min) {
                        min = tmp;
                        index = i;
                    }
                }
                clusters.get(index).add(t);
            });
            coveraged = true;
            for(int i = 0; i < k; i++) {
                SquareTag t = this.getCenter(clusters.get(i));
                if(this.distance(t, centers.get(i)) > stdDiff) {
                    coveraged = false;
                }
                centers.set(i, t);
            }
        }
        return clusters;
    }

    private SquareTag getCenter(List<SquareTag> cluster) {
        int size = cluster.size();
        int[] center = {
                cluster.stream().mapToInt(SquareTag::getX).sum() / size,
                cluster.stream().mapToInt(SquareTag::getY).sum() / size,
                cluster.stream().mapToInt(SquareTag::getW).sum() / size,
                cluster.stream().mapToInt(SquareTag::getH).sum() / size
        };
        return cluster.stream().min(Comparator.comparingDouble(e -> this.distance(e, center))).get();
    }

    private double distance(SquareTag a, int[] b) {
        return new Vector4D(a).diff(new Vector4D(b[0], b[1], b[2], b[3])).mag();
    }

    private double distance(SquareTag a, SquareTag b) {
        return new Vector4D(a).diff(new Vector4D(b)).mag();
    }

    private boolean contains(List<SquareTag> cluster, SquareTag tag) {
        for(SquareTag t: cluster) {
            if(this.distance(t, tag) == 0.0) {
                return true;
            }
        }
        return false;
    }

}
