package br.com.ericsson.smartnotification.enums;

public enum EnrichmentApi {
    CLARO_API("claro_api", new String[] { "name", "data_adesao", "url_info" });

    private String prefix;

    private String[] fields;

    private EnrichmentApi(String prefix, String[] fields) {
        this.prefix = prefix;
        this.fields = fields;
    }

    public String[] getFields() {
        return fields;
    }

    public String getPrefix() {
        return prefix;
    }

}
