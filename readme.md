Data:
    The data we used to train is in movie.data. The other data was used for testing
nlps_hw7_code:
    MultiNB.java:
        The file MultiNB stands for multinomial naive bays model and is the type of model we use here. 
    FeatureProbability.java
        This file is used to keep track of every feature/token (the words we train on in movies.data) and their predicivity score.
        The predicivity score determines whether that token better predicts positive or negative labels.
    ReviewType.java
        This file documents a review (a line in our training data), the label that review received by our model, and the log probability the review received for that specific label. This lets us easily keep track of the results of running our MultiNB model on a test set.