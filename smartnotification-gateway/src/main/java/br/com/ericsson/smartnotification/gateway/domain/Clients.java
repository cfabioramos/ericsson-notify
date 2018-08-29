package br.com.ericsson.smartnotification.gateway.domain;

import java.util.ArrayList;
import java.util.List;

import br.com.ericsson.smartnotification.constants.Constants;

public class Clients {

    private String value;

    public Clients() {
        super();
    }

    public Clients(String value) {
        super();
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMsisdn() {
        if (this.value.contains("||")) return this.value.split(Constants.PIPES)[0];
        return this.value;
    }

    public List<String> getEnrichments() {
        List<String> enrichments = new ArrayList<>();
        String[] values = this.value.split(Constants.PIPES);
        if (!this.value.isEmpty() && values.length > 1) {
            for (int i = 1; i < values.length; i++) {
                enrichments.add(values[i]);
            }
        }
        return enrichments;
    }

}
