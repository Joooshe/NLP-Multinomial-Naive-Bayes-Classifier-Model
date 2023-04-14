package nlps_hw7_code;

public class FeatureProbability {
    private String feature;

    private Double positiveProb;

    private Double negativeProb;

    private Double predictivity;

    /**
     * Used to map features to their predicivity for negative and positive determine labels
     * @param feature the feature/token we are mapping to a predicivity
     * @param predicivity the predicivity of that feature or token either for a positive labe or a negative label 
     */
    public FeatureProbability(String feature, Double positiveProb, Double negativeProb) {
        this.feature = feature;
        this.positiveProb = positiveProb;
        this.negativeProb = negativeProb;
        this.predictivity = positiveProb/negativeProb;
    }

    public FeatureProbability(String feature, Double predictivity) {
        this.feature = feature;
        this.predictivity = predictivity;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public Double getPositiveProb() {
        return positiveProb;
    }

    public void setPositiveProb(Double positiveProb) {
        this.positiveProb = positiveProb;
    }

    public Double getNegativeProb() {
        return negativeProb;
    }

    public void setNegativeProb(Double negativeProb) {
        this.negativeProb = negativeProb;
    }

    public Double getPredictivity() {
        return predictivity;
    }

    public void setPredictivity(Double predictivity) {
        this.predictivity = predictivity;
    }

    public String toString() {
        return this.feature;
    }
}
