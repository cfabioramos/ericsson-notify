package br.com.ericsson.smartnotification.enrichment.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.kafka.CampaignEnrichmentObject;
import br.com.ericsson.smartnotification.domain.kafka.EnrichmentObject;
import br.com.ericsson.smartnotification.domain.kafka.EventEnrichmentObject;
import br.com.ericsson.smartnotification.entities.EventDefinitionField;
import br.com.ericsson.smartnotification.entities.Message;
import br.com.ericsson.smartnotification.entities.MessageEnriched;
import br.com.ericsson.smartnotification.entities.MessageEnrichedObject;
import br.com.ericsson.smartnotification.entities.TemplateMessage;
import br.com.ericsson.smartnotification.enums.EnrichmentApi;
import br.com.ericsson.smartnotification.exceptions.ApplicationException;
import br.com.ericsson.smartnotification.interfaces.enrichment.EnrichmentCampaingFields;
import br.com.ericsson.smartnotification.interfaces.enrichment.EnrichmentGenericApi;
import br.com.ericsson.smartnotification.interfaces.enrichment.EnrichmentEventFields;
import br.com.ericsson.smartnotification.repository.TemplateMessageRepository;

@Component
public class EnrichComponent {

    private static final Logger LOG = LoggerFactory.getLogger(EnrichComponent.class);

    @Autowired
    private TemplateMessageRepository templateMessageRepository;

    @Autowired
    private EnrichmentEventFields enrichmentEventFields;

    @Autowired
    private EnrichmentCampaingFields enrichmentCampaingFields;

    @Autowired
    private EnrichmentGenericApi enrichmentGenericApi;

    public EnrichComponent() {
        super();
    }

    public MessageEnrichedObject enrich(EnrichmentObject enrichmentbject) throws ApplicationException {
        LOG.info("Obtendo valores do Enriquecimento enriquecimento : {}", enrichmentbject);
        return buildEventMessageEnriched(enrichmentbject);
    }

    protected MessageEnrichedObject buildEventMessageEnriched(EnrichmentObject enrichmentObject)
            throws ApplicationException {

        TemplateMessage templateMessage = this.getTemplateMessage(enrichmentObject.getTemplateId());

        List<MessageEnriched> messagesEnriched = this.enrichTemplatesMessage(templateMessage, enrichmentObject);

        return new MessageEnrichedObject(messagesEnriched, templateMessage.getShippingRestriction(), enrichmentObject);
    }

    protected TemplateMessage getTemplateMessage(String templateId) throws ApplicationException {
        LOG.info("Obtendo Template : {}", templateId);
        Optional<TemplateMessage> optional = templateMessageRepository.findById(templateId);
        if (optional.isPresent()) {
            LOG.info("Template recuperado: {}", optional.get());
            return optional.get();
        } else {
            LOG.info("Template não encontrado para id {}", templateId);
            throw new ApplicationException(String.format("Template não encontrado para id %s", templateId));
        }
    }

    protected List<MessageEnriched> enrichTemplatesMessage(TemplateMessage templateMessage,
            EnrichmentObject enrichmentObject) throws ApplicationException {

        LOG.info("Iniciando processo de enriquecimento para template - {}", templateMessage);

        List<MessageEnriched> messagesEnriched = new ArrayList<>();
        for (Message message : templateMessage.getMessages()) {
            LOG.info("Enriquecendo a partir da mensagem do template - {}", message);
            LOG.info("Variáveis da mensagem do template - {}", Arrays.toString(message.getEnrichmentTemplateFields()));
            LOG.info("Variáveis da mensagem de outras apis - {}", Arrays.toString(message.getEnrichmentOtherFields()));

            Map<String, String> mapEnriched = new HashMap<>();
            if (enrichmentObject instanceof EventEnrichmentObject) {
                EventEnrichmentObject eventEnrichmentObject = ((EventEnrichmentObject) enrichmentObject);
                mapEnriched.putAll(enrichEvent(message, eventEnrichmentObject.getEvent()));
                mapEnriched.putAll(enrichMsisdnApi(message, eventEnrichmentObject.getEvent()
                        .getField(EventDefinitionField.EVENT_FIELD_MSISDN.getName()).getValue().toString()));
            } else if (enrichmentObject instanceof CampaignEnrichmentObject) {
                CampaignEnrichmentObject campaignEnrichmentObject = ((CampaignEnrichmentObject) enrichmentObject);
                mapEnriched.putAll(enrichCampaign(message, campaignEnrichmentObject));
                mapEnriched.putAll(enrichMsisdnApi(message, campaignEnrichmentObject.getMsisdn()));
            }

            MessageEnriched messageEnriched = message.getMessageEnriched(mapEnriched);
            messageEnriched.setChannel(message.getChannel());

            LOG.info("Mensagem Eriquecida - {}", messageEnriched);
            messagesEnriched.add(messageEnriched);
        }

        return messagesEnriched;
    }

    protected Map<String, String> enrichEvent(Message message, Event event) throws ApplicationException {
        String[] fields = message.getEnrichmentTemplateFields();

        Map<String, String> mapEnriched = new HashMap<>();
        if (!StringUtils.isEmpty(fields)) {
            mapEnriched.putAll(enrichmentEventFields.enrich(fields, event));
        }

        LOG.info("Valores do evento - {}", mapEnriched);

        return mapEnriched;
    }

    protected Map<String, String> enrichCampaign(Message message, CampaignEnrichmentObject campaignEnrichmentObject)
            throws ApplicationException {
        String[] fields = message.getEnrichmentTemplateFields();

        Map<String, String> mapEnriched = new HashMap<>();
        if (!StringUtils.isEmpty(fields)) {
            mapEnriched.putAll(enrichmentCampaingFields.enrich(fields, campaignEnrichmentObject));
        }

        LOG.info("Valores da campanha - {}", mapEnriched);

        return mapEnriched;
    }

    protected Map<String, String> enrichMsisdnApi(Message message, String msisdn) throws ApplicationException {
        // TODO isso é um MOCK implementar outras APIS
        String[] fields = message.getEnrichmentApiFields(EnrichmentApi.CLARO_API);

        LOG.info("Valores a serem obtidos para enriquecimento - {}", Arrays.toString(fields));

        Map<String, String> mapEnriched = new HashMap<>();
        if (!StringUtils.isEmpty(fields)) {
            mapEnriched.putAll(enrichmentGenericApi.enrich(msisdn));
        }

        LOG.info("Valores obtidos para enriquecimento - {}", mapEnriched);

        return mapEnriched;
    }

    protected void setEnrichmentEventFields(EnrichmentEventFields enrichmentEventFields) {
        this.enrichmentEventFields = enrichmentEventFields;
    }

    protected void setEnrichmentCampaingFields(EnrichmentCampaingFields enrichmentCampaingFields) {
        this.enrichmentCampaingFields = enrichmentCampaingFields;
    }

    protected void setEnrichmentGenericApi(EnrichmentGenericApi enrichmentGenericApi) {
        this.enrichmentGenericApi = enrichmentGenericApi;
    }

    
    
}
