import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class k_medoids extends Medoid{
    List<Double> simList = new ArrayList<>();
    List<List<Double>> simLists = new ArrayList<>();
    List<Double> orderList = new ArrayList<>();
    List<Partition> orderLists = new ArrayList<Partition>();
    List<Double> maxList = new ArrayList<>();
    List<Double> clusterList = new ArrayList<>();
    List<Double> clusterDocList = new ArrayList<>();
    List<Integer> lastRandom = new ArrayList<>();

    List<List<Double>> docCentroidList = new ArrayList<>();

    List<Partition> clusterDocLists = new ArrayList<>();

    double OriginalCost = 0.0;

    public void runProcess() throws IOException {
        process();
        totalWords();
        addToVector();
        //writeToFile();
        //readFile();
        //addToDocList(); //activate this method after all documents have been processed (tfidf values have been saved)
        //(adds names of files to list)
        //writeTotalWordsToFile();
        //readTotalWordsFile();
        //test123();
        //clearDocVectorList();
        clusters();
        kmedoids();
    }

    // SIMILARITY MEASURE THAT CHECKS HOW SIMILAR A DOCUMENT IS TO ANOTHER
    // IF THE VALUE = 1, THEN THEY'RE MOST LIKELY THE SAME DOCUMENT OR HAVE THE SAME SIMILARITY MEASURE
    public static double cosineSimilarity(List<Double> vectorA, List<Double> vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.size(); i++) {
            dotProduct += vectorA.get(i) * vectorB.get(i);
            normA += Math.pow(vectorA.get(i), 2);
            normB += Math.pow(vectorB.get(i), 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    //method that begins to determine how similar documents are to each other and proceeds to cluster
    // the documents

    /*public void test123() {
        Partition docVectors = new Partition(readDocVectorList, totalWords);
        System.out.println(docVectors.size());
        //System.out.println(totalWords);
    }*/

    public void kmedoids() {
        //readDocVectorList <---- partition this
        Partition docVectors = new Partition(docVector, total_words.size());
        setRndCentroids(docVectors);

        //System.out.println(docVectors);
        //System.out.println(centroids.size());

        for(int i = 0; i < centroids.size(); i++) {
            docCentroidList.add(centroids.get(i));
        }

        for(int i = 0; i < docVectors.size(); i++) {
            docCentroidList.add(docVectors.get(i));
        }

        double max = 0.0;
        //similarity between documents and centroids
        double similarity = 0.0;
        for (int i = 0; i < centroids.size(); i++) {
            for (int j = 0; j < docVectors.size(); j++) {
                similarity = cosineSimilarity(centroids.get(i), docVectors.get(j));
                System.out.println("For document " + docList.get(j) + " cosine similarity between centroid " + docList.get(lastNum.get(i)) + " " +
                        "is: " + similarity);
                simList.add(similarity);
            }
            System.out.println();
            Partition part = new Partition(simList, docList.size());
            simLists.add(part.get(i));
            System.out.println(simLists.get(i));
            System.out.println();
        }
        for (int i = 0; i < docVectors.size(); i++) {
            for (int j = 0; j < centroids.size(); j++) {
                orderList.add(simLists.get(j).get(i));
            }
        }
        Partition orderPart = new Partition(orderList, centroids.size());
        orderLists.add(orderPart);
        System.out.println(orderLists);
        System.out.println();
        for (int i = 0; i < docVectors.size(); i++) {
            max = (double) Collections.max(orderPart.get(i));
            maxList.add(max);
        }
        System.out.println(simLists);


        //ASSIGNMENT OF DOCS TO CLUSTERS
        for (int i = 0; i < docVectors.size(); i++) {
            double val = maxList.get(i);
            double index = orderLists.get(0).get(i).indexOf(val) + 1;
            System.out.println("Document " + docList.get(i) + " is assigned to cluster " + index);
            clusterList.add(index);
        }
        for (int i = 0; i < centroids.size(); i++) {
            for (int j = 0; j < docVectors.size(); j++) {
                if(simLists.get(i).get(j) <= 0.98) {
                    OriginalCost =+ simLists.get(i).get(j);
                } else {
                    OriginalCost =+ 0;
                }

            }
        }
        System.out.println(OriginalCost);
        System.out.println(clusterList);

        int increment = 0;
        double lastCost = 0.0;
        int it = 1;
        do {

            lastCost = 0.0;

            double sim = 0.0;
            double Cost = 0.0;

            List<List<Double>> tempCentroids = new ArrayList<>();
            List<List<Double>> tempDocList = new ArrayList<>();
            List<Double> simTemp = new ArrayList<>();
            List<List<Double>> simListsTemp = new ArrayList<>();
            List<Double> tempOrderList = new ArrayList<>();
            List<Partition> orderListsTemp = new ArrayList<Partition>();
            List<Double> maximumList = new ArrayList<>();
            List<Double> clusterListTemp = new ArrayList<>();

            Random rand = new Random();
            int rnd = rand.nextInt(((docVectors.size() + 2) - centroids.size()) + 1) + centroids.size();
            if(lastRandom.contains(rnd)) {
                do {
                    rnd = rand.nextInt(((docVectors.size() + 2) - centroids.size()) + 1) + centroids.size();
                } while(!lastNum.contains(rnd));
            }
                Collections.swap(docCentroidList, increment, rnd);

                for(int i = 0; i < centroids.size(); i++) {
                    tempCentroids.add(docCentroidList.get(i));
                }
                for(int i = 3; i < docVectors.size()+3; i++) {
                    tempDocList.add(docCentroidList.get(i));
                }

                Collections.swap(docCentroidList, rnd, increment);

                for (int i = 0; i < centroids.size(); i++) {
                    for (int j = 0; j < docVectors.size(); j++) {
                        sim = cosineSimilarity(tempCentroids.get(i), tempDocList.get(j));
                        simTemp.add(sim);
                    }
                    System.out.println();
                    Partition part = new Partition(simTemp, docList.size());
                    simListsTemp.add(part.get(i));
                }
                for (int i = 0; i < docVectors.size(); i++) {
                    for (int j = 0; j < tempCentroids.size(); j++) {
                        tempOrderList.add(simListsTemp.get(j).get(i));
                    }
                }
                Partition orderListPart = new Partition(tempOrderList, centroids.size());
                orderListsTemp.add(orderListPart);
                System.out.println(orderListsTemp.get(0));
                for (int i = 0; i < docVectors.size(); i++) {
                    double maximum = (double) Collections.max(orderListPart.get(i));
                    maximumList.add(maximum);
                }
                for (int i = 0; i < tempDocList.size(); i++) {
                    double val = maximumList.get(i);
                    double index = orderListsTemp.get(0).get(i).indexOf(val) + 1;
                    System.out.println("Document " + docList.get(i) + " is assigned to cluster " + index);
                    clusterListTemp.add(index);
                }
                System.out.println(clusterListTemp);
                System.out.println();
                for (int i = 0; i < centroids.size(); i++) {
                    for (int j = 0; j < docVectors.size(); j++) {
                        if(simListsTemp.get(i).get(j) <= 0.98) {
                            Cost =+ simListsTemp.get(i).get(j);
                        } else {
                            Cost =+ 0;
                        }
                        lastCost = Cost;
                    }
                }
                System.out.println();
                System.out.println("The original cost of swapping at iteration " +it+ " is  " +OriginalCost);
                System.out.println("The cost of swapping at iteration " +it+ " is  " +Cost);
                System.out.println();
                lastRandom.add(rnd);
            System.out.println("Number for increment var "+increment);
                if(increment < centroids.size()-1) {
                    increment++;
                } else {
                    increment = 0;
                }
                it++;
        }while(lastCost >= OriginalCost);
    }
}

/*for(int i = 0; i < centroids.size(); i++) {
                for(int j = 0; j < docList.size(); j++) {

                }
  }*/