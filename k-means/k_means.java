import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class k_means extends Centroid {

    List<Double> simList = new ArrayList<>();
    List<List<Double>> simLists = new ArrayList<>();
    List<Double> orderList = new ArrayList<>();
    List<Partition> orderLists = new ArrayList<Partition>();
    List<Double> maxList = new ArrayList<>();
    List<Double> clusterList = new ArrayList<>();
    List<Double> clusterDocList = new ArrayList<>();

    List<Partition> clusterDocLists = new ArrayList<>();

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
        kmeans();
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

    public void test123() {
        Partition docVectors = new Partition(readDocVectorList, totalWords);
        System.out.println(docVectors.size());
        //System.out.println(totalWords);
    }

    public void kmeans() {
        //readDocVectorList <---- partition this
        Partition docVectors = new Partition(docVector, total_words.size());
        setRndCentroids(docVectors);

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

        //ASSIGNMENT OF DOCS TO CLUSTERS
        for (int i = 0; i < docVectors.size(); i++) {
            double val = maxList.get(i);
            double index = orderLists.get(0).get(i).indexOf(val) + 1;
            System.out.println("Document " + docList.get(i) + " is assigned to cluster " + index);
            clusterList.add(index);
            for (int j = 0; j < total_words.size(); j++) {
                clusterDocList.add(Double.valueOf((Double) docVectors.get(i).get(j)));
            }
            clusterDocList.add(index);
        }
        System.out.println(clusterList);
        Partition clusterDocListPart = new Partition(clusterDocList, total_words.size() + 1);
        System.out.println(clusterDocListPart);
        //System.out.println(clusterDocListPart.get(28).get(total_words.size())); //------>>>>> HOW TO WORK WITH THIS
        // PARTITION: 2nd get retruns the TFIDF values and cluster number that the doc is associated with
        //System.out.println(clusterDocListPart.get(28));
        //System.out.println(docVectors.get(0).size());
        //System.out.println(clusterDocListPart.get(0).get(total_words.size()));
        boolean equal = false;
        int iterator = 0;
        int oddNumber = 1;
        double sum = 0;
        double docsInCluster = 0;
        double avg;
        double sim = 0.0;
        double maximum = 0.0;
        List<List<Double>> oldClusterList = new ArrayList<>();
        List<List<Double>> oldClusterDocList = new ArrayList<>();
        oldClusterList.add(clusterList);
            do {
                List<Double> newClusterList = new ArrayList<>();
                List<Double> newCentroid = new ArrayList<>();
                List<Double> simTemp = new ArrayList<>();
                List<Double> orderTemp = new ArrayList<>();
                List<Double> maximumList = new ArrayList<>();
                List<Double> newClusterDocList = new ArrayList<>();

                /*if(iterator >= 1) {
                    newClusterList.clear();
                    newCentroid.clear();
                }*/
                for (int cluster = 1; cluster < getClusters()+1; cluster++) {
                            if(iterator == 0) {
                                if((double) clusterDocListPart.get(i).get(total_words.size()) == (double) cluster) {
                                    for(int i = 0; i < docList.size(); i++) {
                                        for(int j = 0; j < total_words.size(); j++) {
                                            sum =+ (double) clusterDocListPart.get(i).get(j);
                                            docsInCluster++;
                                        }
                                    }
                                }


                            } else if (iterator == 1) {
                                if(oldClusterDocList.get(i).get(total_words.size()) == (double) cluster) {
                                    sum =+ oldClusterDocList.get(i).get(j);
                                    docsInCluster++;
                                }
                            } else {
                                if(oldClusterDocList.get(i + (iterator - 1)*docList.size()).get(total_words.size()) == (double) cluster) {
                                    sum =+ oldClusterDocList.get(i + (iterator - 1)*docList.size()).get(j);
                                    docsInCluster++;
                                }
                            }
                        avg = sum/docsInCluster;
                        newCentroid.add(avg);

                }
                Partition newCentroids = new Partition(newCentroid, total_words.size());
                //recalculation of cosine similarity between new centroids and datapoints
                for (int i = 0; i < newCentroids.size(); i++) {
                    for (int j = 0; j < docVectors.size(); j++) {
                        sim = cosineSimilarity(newCentroids.get(i), docVectors.get(j));
                        simTemp.add(sim);
                    }
                }
                Partition simPart = new Partition(simTemp, docList.size());
                //System.out.println(simPart);
                for(int i = 0; i < docVectors.size(); i++) {
                    for(int j = 0; j < getClusters(); j++) {
                        orderTemp.add((Double) simPart.get(j).get(i));
                    }
                }
                Partition orderedSimValues = new Partition(orderTemp, getClusters());
                System.out.println(orderList);
                for(int i = 0; i < docVectors.size(); i++) {
                    maximum = (double) Collections.max(orderedSimValues.get(i));
                    maximumList.add(maximum);
                    double val = maximumList.get(i);
                    double index = orderedSimValues.get(i).indexOf(val) + 1;
                    System.out.println("Document " + docList.get(i) + " is assigned to cluster " + index);
                    newClusterList.add(index);
                    for(int j = 0; j < total_words.size(); j++) {
                        newClusterDocList.add((Double) docVectors.get(i).get(j));
                    }
                    newClusterDocList.add(index);
                    //add directly from here
                    oldClusterDocList.add(newClusterDocList);
                }
                Partition newClusterDocListPart = new Partition(newClusterDocList, total_words.size() + 1);
                System.out.println(oldClusterDocList);

                //System.out.println(oldClusterDocList.get(0).get(0).get(total_words.size()));
                oldClusterList.add(newClusterList);

                if(iterator == 0) {
                    equal = newClusterList.equals(oldClusterList.get(0));
                } else {
                    equal = newClusterList.equals(oldClusterList.get(iterator - 1));
                }
                oldClusterList.add(newClusterList);
                System.out.println(equal);
                System.out.println(oldClusterList);
                iterator++;
                /*if(iterator >= 2) {
                    oddNumber += 2;
                }*/
            } while(equal == false);
    }
}
/*List<Double> newCentroid = new ArrayList<>();
            for(int j = 0; j < total_words.size() - 1; j++) {
                sum = 0.0;
                avg = 0.0;
                docsInCluster = 0.0;
                for(int i = 0; i < docList.size(); i++) {
                    if((double) clusterDocListPart.get(i).get(total_words.size()) == (double) cluster) {
                        sum =+ (double) clusterDocListPart.get(i).get(j);
                        docsInCluster++;
                    }
                }
                avg = sum/docsInCluster;
                newCentroid.add(avg);
                ArrayList<Double> copy = new ArrayList<>(newCentroid);
                centroids.add(copy);
                System.out.println(centroids.get(0));
            }*/

/*clearCentroids();
            double sum = 0.0;
            double avg = 0.0;
            double docsInCluster = 0.0;
            for (int cluster = 1; cluster < getClusters()+1; cluster++) {
                for(int j = 0; j < total_words.size(); j++) {
                    sum = 0.0;
                    avg = 0.0;
                    docsInCluster = 0.0;
                    for(int i = 0; i < docList.size(); i++) {
                        if((double) clusterDocListPart.get(i).get(total_words.size()) == (double) cluster) {
                            sum =+ (double) clusterDocListPart.get(i).get(j);
                            docsInCluster++;
                        }
                    }
                    avg = sum/docsInCluster;
                    newCentroid.add(avg);
                    ArrayList<Double> copy = new ArrayList<>(newCentroid);
                }
            }
            Partition newCentroids = new Partition(newCentroid, total_words.size());
            System.out.println(newCentroids.get(0));
            equal = newClusterList.equals(oldClusterList);*/