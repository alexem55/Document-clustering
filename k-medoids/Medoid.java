import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Medoid extends TFIDF{

    int clusters = 0;
    List<List<Double>> centroids = new ArrayList<>();
    List<List<Double>> newCentroidsTemp = new ArrayList<>();
    List<Integer> lastNum = new ArrayList<>();

    public void clusters() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the number of clusters: ");
        clusters = in.nextInt();
        if(clusters <= 1) {
            System.out.println("Number of clusters should be more than 1. Try again. ");
            clusters();
        }
        //System.out.println(process().get(0).get(0) + totalWords().get(1));
    }

    public int getClusters() {
        return clusters;
    }

    public void setRndCentroids(Partition part) {
        int i = 0;

        while (i < getClusters()) {
            int r = (int) (Math.random() * (docList.size()));
            if(lastNum.contains(r)) {
                do {
                    r = (int) (Math.random() * (docList.size()));
                } while(!lastNum.contains(r));
            } else {
                centroids.add(part.get(r));
                System.out.println(r);
                i++;
                lastNum.add(r);
            }
        }
        //System.out.println(centroids.get(0));
    }

    public void clearCentroids() {
        for(int i = 0; i < getClusters(); i++) {
            centroids.get(i).clear();
        }
    }
}
