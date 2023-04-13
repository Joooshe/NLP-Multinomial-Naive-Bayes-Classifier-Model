package nlps_hw7_code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MultiNB {
    public static void main(String[] args) {
        String fileDirectory = "C:\\Users\\Joshua G-K\\Documents\\College\\Junior Year\\NLPs\\hw7\\data\\";
        String trainingData = fileDirectory + "movies.data";
        String testSentences = fileDirectory + "simple.data";
        Double lambda = 0.0;

        MultiNB classifier = new MultiNB(trainingData, testSentences, lambda);
    }

    // What denotes a positive label
    public static final String POSITIVE_LABEL = "positive";
    // What denotes a negative label
    public static final String NEGATIVE_LABEL = "negative";

    // Hashmap for the total number of times we have seen a label for any # words
    // (so # times label y appeared times length of sentence)?
    private HashMap<String, Double> countLabel;
    // Hashmap for total number of times the positive label (label 5) has been seen
    // with a specific word
    private HashMap<String, HashMap<String, Double>> countW_Label;
    // Hashmap for probability value of a given word associated with positive or
    // negative
    // Hashmap layering should go positive/negative -> word
    private HashMap<String, HashMap<String, Double>> probabilityW_Label;
    // Set for vocabulary
    private HashSet<String> vocabulary;
    // Vocabulary size
    private int vocabSize;

    private String testSentences;

    private Double lambda;

    private Double probPositiveLabel;

    private Double probNegativeLabel;

    public MultiNB(String trainingData, String testSentences, Double lambda) {
        this.testSentences = testSentences;
        this.lambda = lambda;
        // Initialize hashmaps
        this.countLabel = new HashMap<>();
        this.countLabel.put(POSITIVE_LABEL, 0.0);
        this.countLabel.put(NEGATIVE_LABEL, 0.0);
        // Intialize countW_Label map
        this.countW_Label = new HashMap<>();
        this.countW_Label.put(POSITIVE_LABEL, new HashMap<>());
        this.countW_Label.put(NEGATIVE_LABEL, new HashMap<>());
        // Initialize probabilityW_Label
        this.probabilityW_Label = new HashMap<>();
        this.probabilityW_Label.put(POSITIVE_LABEL, new HashMap<>());
        this.probabilityW_Label.put(NEGATIVE_LABEL, new HashMap<>());
        // Initialize vocabulary
        this.vocabulary = new HashSet<>();

        // Populate the counts for the training data
        this.createCounts(trainingData);

        // Get probability for each label
        this.vocabSize = this.vocabulary.size();
        Double denominator = this.countLabel.get(POSITIVE_LABEL) + this.countLabel.get(NEGATIVE_LABEL);
        this.probPositiveLabel = Math.log10(this.countLabel.get(POSITIVE_LABEL) / denominator);
        this.probNegativeLabel = Math.log10(this.countLabel.get(NEGATIVE_LABEL) / denominator);
        // Create the log probabilities for each word's appearance for the positive
        // label
        this.createLogProbabilities(POSITIVE_LABEL);

        // Create the log probabilities for each word's appearance for the negative
        // label
        this.createLogProbabilities(NEGATIVE_LABEL);

        // Determine labels for each sentence
        this.determineLabels(testSentences);
    }

    public void determineLabels(String testSentences) {
        // Read through file
        File file = new File(testSentences);
        // For pre-processing and creating stopList set
        try {
            // Where we store whether reviews as positive or negative
            ArrayList<ReviewType> reviewTypeList = new ArrayList<>();
            String whiteSpaceDelimiter = "\\s+";
            // Get everything in stop list and put it in a set
            Scanner reader = new Scanner(file, "UTF-8").useDelimiter(whiteSpaceDelimiter);

            // Adds all stop list items ot a stop list hashset
            while (reader.hasNextLine()) {
                // Get then ext line
                String line = reader.nextLine();

                // Makes sure there is no white space in the stop list and makes an array
                String[] words = line.split(whiteSpaceDelimiter);

                // Loop through words and add to the count of that word in respective hashmaps
                String word;
                Double positiveProbability = this.probPositiveLabel;
                Double negativeProbability = this.probNegativeLabel;
                for (int i = 0; i < words.length; i++) {
                    word = words[i];
                    if (this.vocabulary.contains(word)) {
                        positiveProbability += this.probabilityW_Label.get(POSITIVE_LABEL).get(word);
                        negativeProbability += this.probabilityW_Label.get(NEGATIVE_LABEL).get(word);
                    }
                }

                // Determine whether word is positive or negative and add that to output
                ReviewType lineReviewType;
                if (positiveProbability > negativeProbability) {
                    lineReviewType = new ReviewType(POSITIVE_LABEL, line, positiveProbability);
                } else {
                    lineReviewType = new ReviewType(NEGATIVE_LABEL, line, negativeProbability);
                }
                reviewTypeList.add(lineReviewType);
            }
            // System.out.println(reviewTypeList);
            // // print out output
            for (ReviewType reviewType : reviewTypeList) {
                System.out.println(reviewType);
            }

            // Once we are done we make a
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createLogProbabilities(String labelType) {
        Double labelCount;
        HashMap<String, Double> countWord;
        HashMap<String, Double> probabilityWord;
        if (labelType.equals(POSITIVE_LABEL) || labelType.equals(NEGATIVE_LABEL)) {
            // get the label count integer
            labelCount = this.countLabel.get(labelType);
            // Get the label count hashmap
            countWord = this.countW_Label.get(labelType);
            // Get probability
            probabilityWord = this.probabilityW_Label.get(labelType);
        } else {
            System.out.println("ERROR, labelType does not match either positive or negative");
            return;
        }
        // For each word in the label count hashmap get the probability and make it log
        // base ten
        for (String word : countWord.keySet()) {
            // do math and add to probability word
            Double prob = (countWord.get(word) + lambda) / (labelCount + lambda * this.vocabSize);
            prob = Math.log10(prob);
            probabilityWord.put(word, prob);
        }

        // System.out.println(probabilityWord);
    }

    public void createCounts(String trainingDataPath) {
        // Read through file
        File file = new File(trainingDataPath);

        // For pre-processing and creating stopList set
        try {
            String whiteSpaceDelimiter = "\\s+";
            // Get everything in stop list and put it in a set
            Scanner reader = new Scanner(file, "UTF-8").useDelimiter(whiteSpaceDelimiter);

            // Adds all stop list items ot a stop list hashset
            while (reader.hasNextLine()) {
                // Get then ext line
                String line = reader.nextLine();
                // Makes sure there is no white space in the stop list and makes an array
                String[] words = line.split(whiteSpaceDelimiter);

                // Declare the hashmap we will use to add counts to
                HashMap<String, Double> countWord;
                Double countSpecificLabel;
                // If the label at the front of the line is 1 or 5 then we make this the hashmap
                // for 1 or 5 respectively
                String label = words[0];
                if (label.equals(POSITIVE_LABEL) || label.equals(NEGATIVE_LABEL)) {
                    // We also keep track of whether we are using the ocuntmap for 5 or for 1
                    countSpecificLabel = this.countLabel.get(label);
                    countWord = this.countW_Label.get(label);
                    // else if it is 5 we make this the hashMap for 5
                    // else we error
                } else {
                    System.out.println("ERROR, WRONG LABEL");
                    break;
                }

                // Loop through words and add to the count of that word in respective hashmaps
                String word;
                for (int i = 1; i < words.length; i++) {
                    word = words[i];
                    // Increment the count of countSpecific label by 1
                    countSpecificLabel += 1;
                    // if word is in countW, increment its value
                    if (countWord.containsKey(word)) {
                        countWord.put(word, countWord.get(word) + 1.0);
                        // else make its value 1
                    } else {
                        countWord.put(word, 1.0);
                    }

                    if (!this.vocabulary.contains(word)) {
                        this.vocabulary.add(word);
                    }
                }
                this.countLabel.put(label, countSpecificLabel);

            }

            // Once we are done we make a
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Check to ensure
        // System.out.println(this.countW_Label);
    }

}