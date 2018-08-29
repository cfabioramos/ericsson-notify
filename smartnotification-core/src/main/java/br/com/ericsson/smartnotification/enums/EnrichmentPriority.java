package br.com.ericsson.smartnotification.enums;

public enum EnrichmentPriority {
    
    HIGH("enrichment_high", 5), MEDIUM("enrichment_medium", 3) , LOW("enrichment_low", 1)
    ;
    
    private String desc;
    
    private int loop;

    private EnrichmentPriority(String desc, int loop) {
        this.desc = desc;
        this.loop = loop;
    }

    public String getDesc() {
        return desc;
    }

    public int getLoop() {
        return loop;
    }
    
    @Override
    public String toString() {
        return this.desc;
    }
    
}
