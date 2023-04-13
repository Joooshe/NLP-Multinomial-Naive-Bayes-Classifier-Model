package nlps_hw7_code;

public class ReviewType {
    private String label;

    private String review;

    private Double logProbability;

    public ReviewType(String label, String review, Double logProbability) {
        this.label = label;
        this.review = review;
        this.logProbability = logProbability;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Double getLogProbability() {
        return logProbability;
    }

    public void setLogProbability(Double logProbability) {
        this.logProbability = logProbability;
    }

    public String toString() {
        // String rep = String.format("%s \t %s \t %f", this.label, this.review, this.logProbability);
        String rep = String.format("%s \t %f", this.label, this.logProbability);
        return rep;
    }
}
