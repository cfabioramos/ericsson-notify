package br.com.ericsson.smartnotification.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import br.com.ericsson.smartnotification.enums.Channel;
import br.com.ericsson.smartnotification.enums.EnrichmentApi;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;

public class Message extends AgregationDocument {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String DELIMITER_START = "{";

    public static final String DELIMITER_END = "}";

    public static final String DELIMITER = ".";

    private String title;

    private String text;

    private Channel channel;

    private int order;

    private String appId;

    public Message() {
        this.setActive(null);
    }

    public Message(String title, String text, Channel channel, int order, String appId) {
        super();
        this.title = title;
        this.text = text;
        this.channel = channel;
        this.order = order;
        this.appId = appId;
        this.setActive(null);
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel typeApp) {
        this.channel = typeApp;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private String[] getEnrichmentFields(String message) {
        String messageTemplatePart = message;
        List<String> fields = new ArrayList<>();

        while (messageTemplatePart.contains(DELIMITER_START) && messageTemplatePart.contains(DELIMITER_END)) {
            int positionStart = messageTemplatePart.indexOf(DELIMITER_START);
            int positionEnd = messageTemplatePart.indexOf(DELIMITER_END);
            String field = messageTemplatePart.substring(positionStart + 1, positionEnd);
            messageTemplatePart = messageTemplatePart.substring(positionEnd + 1);
            fields.add(field);
        }

        String[] fieldsArray = new String[fields.size()];

        return fields.toArray(fieldsArray);
    }

    public String[] getEnrichmentTemplateFields() {
        List<String> eventFields = new ArrayList<>();
        for (String field : this.getEnrichmentFields(this.text)) {
            if (!field.contains(DELIMITER)) {
                eventFields.add(field);
            }
        }
        String[] fields = new String[eventFields.size()];

        return eventFields.toArray(fields);
    }

    public String[] getEnrichmentOtherFields() {
        List<String> otherFields = new ArrayList<>();
        for (String field : this.getEnrichmentFields(this.text)) {
            if (field.contains(DELIMITER)) {
                otherFields.add(field);
            }
        }
        String[] fields = new String[otherFields.size()];

        return otherFields.toArray(fields);
    }

    public String[] getEnrichmentApiFields(EnrichmentApi api) {
        List<String> otherFields = new ArrayList<>();
        for (String field : this.getEnrichmentFields(this.text)) {
            if (field.contains(DELIMITER) && field.contains(api.getPrefix())) {
                otherFields.add(field);
            }
        }
        String[] fields = new String[otherFields.size()];

        return otherFields.toArray(fields);
    }

    public MessageEnriched getMessageEnriched(Map<String, String> values) throws ApplicationException {

        String msg = this.text;

        for (Map.Entry<String, String> entry : values.entrySet()) {
            msg = msg.replace(DELIMITER_START.concat(entry.getKey()).concat(DELIMITER_END), entry.getValue());
        }

        if (msg.contains(DELIMITER_START) && msg.contains(DELIMITER_END)) {
            throw new ApplicationException(String.format("Não foi possivel substituir parâmetro(s) %s",
                    Arrays.toString(this.getEnrichmentFields(msg))));
        }

        return new MessageEnriched(this.title, msg, this.channel, this.appId);
    }

}
