import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TFIDF extends DocProcessing{
    List<Double> docVector = new ArrayList<>();
    List<String> total_words = new ArrayList<>();
    //counts total words in all documents
    public List<String> totalWords() throws IOException {
        for (int i = 0; i < listsOfWords.size(); i++) {
            for (int j = 0; j < listsOfWords.get(i).size(); j++) {
                if (!total_words.contains(listsOfWords.get(i).get(j))) {
                    total_words.add(listsOfWords.get(i).get(j));
                }
            }
        }
        return total_words;
    }

    public double tf(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result / doc.size();
    }

    public double idf(List<List<String>> docs, String term) {
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(docs.size() / n);
    }

    public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
        return tf(doc, term) * idf(docs, term);
    }

    public void addToVector() {
        for(List<String> list : listsOfWords) {
            for(String str : total_words) {
                docVector.add(tfIdf(list, listsOfWords, str));
            }
        }
    }
}


//counts how many times a word appears in a document
    /*public double countOccurrences(String get, int i) {
        int count = 0;
        for (int x = 0; x < listsOfWords.get(i).size(); x++) {
            if (get.equals(listsOfWords.get(i).get(x))) {
                count++;
            }
        }
        return count;
    }
    //counts how many documents have a specific word in them
    public double No_doc_with_terms(String get) {
        int count = 0;
        for (int i = 0; i < listsOfWords.size(); i++) {
            if (listsOfWords.get(i).contains(get)) {
                count++;
            }
        }
        return count;
    }*/

    /*tf-idf ( t, d, D ) = tf ( t, d ) * idf ( t, D )
      where tf-idf is the term frequency - inverse document frequency function
      t is the term/ word
      d is the document
      D is total number of documents
      tf ( t, d ) is the term frequency (signifies how commonly the term occurs in a document)
      idf ( t, D ) is the inverse document frequency (signifies how commonly the term occurs in all the documents) */

// !!!!!!!!!!!!!! HERE EACH docVector IS SUPPOSED TO HAVE DIFFERENT VALUES INSIDE BUT INSTEAD THEY ARE ALL THE
// SAME !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    /*public List<List<Double>> calculateTFIDF() throws IOException {
        double TF = 0.0;
        double IDF = 0.0;
        double TF_IDF = 0.0;
        for (int i = 0; i < listsOfWords.size(); i++) {
            for (int j = 0; j < total_words.size(); j++) {
                //tf = # of times a term appears in doc / total # of terms in doc
                TF = (double) countOccurrences(total_words.get(j), i) / listsOfWords.get(i).size();
                //idf = log (total # of docs / # of docs with t in it)
                IDF = (double)Math.log(listsOfWords.size() / No_doc_with_terms(total_words.get(j)));
                TF_IDF = TF * IDF;

                docVector.add(TF_IDF);
            }
            docVectors.add(docVector);
        }
        return docVectors;
    }
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
*/
