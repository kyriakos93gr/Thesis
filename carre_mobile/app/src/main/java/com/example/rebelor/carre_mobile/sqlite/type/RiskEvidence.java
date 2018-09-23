package com.example.rebelor.carre_mobile.sqlite.type;


public class RiskEvidence {
    private long id;
    private String risk_evidence;
    private String condition;
    private float confidence_interval_min;
    private float confidence_interval_max;
    private float ratio_value;
    private String ratio_type;
    private String risk_factor;
    private String risk_factor_source;
    private String risk_factor_target;
    private String risk_factor_association_type;

    public RiskEvidence(){

    }

    public RiskEvidence(long id, String risk_evidence, String condition, float confidence_interval_min,
                        float confidence_interval_max, long ratio_value, String ratio_type,
                        String risk_factor, String risk_factor_source, String risk_factor_target,
                        String risk_factor_association_type){
        this.id = id;
        this.risk_evidence = risk_evidence;
        this.condition = condition;
        this.confidence_interval_min = confidence_interval_min;
        this.confidence_interval_max = confidence_interval_max;
        this.ratio_value = ratio_value;
        this.ratio_type = ratio_type;
        this.risk_factor = risk_factor;
        this.risk_factor_source = risk_factor_source;
        this.risk_factor_target = risk_factor_target;
        this.risk_factor_association_type = risk_factor_association_type;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getRiskEvidence() {
        return risk_evidence;
    }

    public void setRiskEvidence(String risk_evidence) {
        this.risk_evidence = risk_evidence;
    }

    public String getCondition() { return condition; }

    public void setCondition(String condition) { this.condition = condition; }

    public float getConfidenceIntervalMin() { return confidence_interval_min; }

    public void setConfidenceIntervalMin(float confidence_interval_min) { this.confidence_interval_min = confidence_interval_min; }

    public float getConfidenceIntervalMax() { return confidence_interval_max; }

    public void setConfidenceIntervalMax(float confidence_interval_max) { this.confidence_interval_max = confidence_interval_max; }

    public float getRatioValue() { return ratio_value; }

    public void setRatioValue(float ratio_value) { this.ratio_value = ratio_value; }

    public String getRatioType() { return ratio_type; }

    public void setRatioType(String ratio_type) { this.ratio_type = ratio_type; }

    public String getRiskFactor() { return risk_factor; }

    public void setRiskFactor(String risk_factor) { this.risk_factor = risk_factor; }

    public String getRiskFactorSource() { return risk_factor_source; }

    public void setRiskFactorSource(String risk_factor_source) { this.risk_factor_source = risk_factor_source; }

    public String getRiskFactorTarget() { return risk_factor_target; }

    public void setRiskFactorTarget(String risk_factor_target) { this.risk_factor_target = risk_factor_target; }

    public String getRiskFactorAssociationType() { return risk_factor_association_type; }

    public void setRiskFactorAssociationType(String risk_factor_association_type) { this.risk_factor_association_type = risk_factor_association_type; }
}
