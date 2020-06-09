import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DocProcessing {

    List<List<String>> listsOfWords = new ArrayList<List<String>>();
    List<String> docList = new ArrayList<>();
    List<String> words;

    File folder = new File("D:/studies/Subjects/Third_year/Second sem/Thesis/Papers"); // <== directory from which
    // the program reads the files
    //@@@PAPERS@@@
    public List<List<String>> process() throws IOException {
        String text;
        String lowerText;

        File stopWordFile = new File("D:/studies/Subjects/Third_year/Second sem/Thesis/Java code/stopwords_NEW.txt");
        Scanner scan = new Scanner(stopWordFile);
        String stopText = scan.nextLine();
        String[] stopWords = stopText.split(",");

        List<String> terms = new ArrayList<String>();

        for (File file : folder.listFiles()) {
            docList.add(file.getName());
            if (file.isFile() && file.getName().endsWith(".pdf")) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                PDDocument document = PDDocument.load(file);
                //Retrieving text from PDF document
                text = pdfStripper.getText(document);
                //Lower-casing the content of the documents
                lowerText = text.toLowerCase();
                //System.out.println(lowerText); //<== TESTING PURPOSES
                //Tokenizing text and removing any special characters
                String[] wordsArray = lowerText.split("\\W+");
                words = new ArrayList<String>(Arrays.asList(wordsArray));
                //Limiting the words to a count of 200
                try {
                    words = words.subList(0, 200);
                } catch (Exception e) {
                    System.out.println("Too few words in document " +file.getName()+ ": " + e);
                }
                //Using an iterator to safely remove any stop words from the word lists
                Iterator<String> it = words.iterator();
                while (it.hasNext()) {
                    String word = it.next();
                    boolean contains = Arrays.stream(stopWords).anyMatch(word::equals);
                    if(contains) {
                        it.remove();
                    }
                }
                listsOfWords.add(words);
            }
        }
        System.out.println("Amount of documents processed: " + listsOfWords.size());
        System.out.println();
        System.out.println(listsOfWords.get(0));
        return listsOfWords;
    }
}