package br.com.ericsson.smartnotification.enums;

public enum TopicPriority {

    HIGH("_high", 5), MEDIUM("_medium", 3) , LOW("_low", 1);
    
    private String desc;
    
    private int loop;

    private TopicPriority(String desc, int loop) {
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
