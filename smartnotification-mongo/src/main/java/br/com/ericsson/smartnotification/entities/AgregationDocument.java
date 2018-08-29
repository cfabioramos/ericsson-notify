package br.com.ericsson.smartnotification.entities;

class AgregationDocument extends AbstractDocument{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    
    public AgregationDocument() {
        this.setId(randomAlphaNumeric(10));
    }
    
    public AgregationDocument(String id, boolean active) {
        super(id, active);
    }
    
    private static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString().toLowerCase();
    }
    
    
    
}
