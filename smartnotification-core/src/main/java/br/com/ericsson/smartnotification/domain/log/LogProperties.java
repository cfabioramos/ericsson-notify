package br.com.ericsson.smartnotification.domain.log;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.annotations.SerializedName;

import br.com.ericsson.smartnotification.constants.Constants;
import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.enums.ErrorType;
import br.com.ericsson.smartnotification.enums.Interfaces;
import br.com.ericsson.smartnotification.enums.LogType;
import br.com.ericsson.smartnotification.enums.Project;

public class LogProperties implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sent;

	private String channel;

	private String app;

	private String keyword;

	private Long tries;

	@SerializedName("error_type")
	private String errorType;

	private String campaign;

	@SerializedName("event_id")
	private String eventId;

	@SerializedName("event_name")
	private String eventName;

	private String template;

	private String rule;

	@SerializedName("channel_type")
	private String channelType;

	@SerializedName("MSISDN")
	private String msisdn;

	@SerializedName("msg_id")
	private String msgId;

	@SerializedName("msg_detail")
	private String msgDetail;

	@SerializedName("notificationID")
	private String notificationId;

	private LogType logType;

	@SerializedName("interface")
	private Interfaces interfaceRequest;

	private String endpoint;

	private String port;

	private RequestMethod method;

	private String process;

	private Integer ntype;

	@SerializedName("template_id")
	private String templateId;

	private String header;

	private String cdata;

	@SerializedName("request_id")
	private String requestId;

	@SerializedName("response_code")
	private String responseCode;

	@SerializedName("response_desc")
	private String responseDesc;

	private LocalDateTime created;

	private String opened;
	
	private LocalDateTime dateOpen;
	
	public LogProperties() {
		this.setCreated(LocalDateTime.now());
	}

	@Deprecated
	public LogProperties(Class<?> classe, String keyword, Project project, Event event, ErrorType errorType) {
		this.sent = classe.getSimpleName();
		this.keyword = keyword;
		this.channel = project.name();
		this.errorType = errorType.toString();
		this.eventId = event.getId();
		this.eventName = event.getDescription();
		this.msisdn = event.getField(Constants.MSISDN).getValue().toString();
		this.msgId = event.getId();
		this.msgDetail = errorType.name();
		this.setCreated(LocalDateTime.now());
	}

	@Deprecated
	public LogProperties(Class<?> classe, String keyword, Project project, ErrorType errorType, String campaign,
			String msisdn) {
		this.sent = classe.getSimpleName();
		this.keyword = keyword;
		this.channel = project.name();
		this.errorType = errorType.toString();
		this.msisdn = msisdn;
		this.msgDetail = errorType.name();
		this.campaign = campaign;
		this.setCreated(LocalDateTime.now());
	}

	@Deprecated
	public LogProperties(String sent, String channel, String app, String keyword, Long tries, String errorType,
			String campaign, String eventId, String eventName, String template, String rule, String channelType,
			String msisdn, String msgId, String msgDetail, String notificationId) {
		super();
		this.sent = sent;
		this.channel = channel;
		this.app = app;
		this.keyword = keyword;
		this.tries = tries;
		this.errorType = errorType;
		this.campaign = campaign;
		this.eventId = eventId;
		this.eventName = eventName;
		this.template = template;
		this.rule = rule;
		this.channelType = channelType;
		this.msisdn = msisdn;
		this.msgId = msgId;
		this.msgDetail = msgDetail;
		this.notificationId = notificationId;
		this.setCreated(LocalDateTime.now());
	}

	public String getSent() {
		return sent;
	}

	public void setSent(String sent) {
		this.sent = sent;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public Long getTries() {
		return tries;
	}

	public void setTries(Long tries) {
		this.tries = tries;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getCampaign() {
		return campaign;
	}

	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgDetail() {
		return msgDetail;
	}

	public void setMsgDetail(String msgDetail) {
		this.msgDetail = msgDetail;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public LogType getLogType() {
		return logType;
	}

	public void setLogType(LogType logType) {
		this.logType = logType;
	}

	public Interfaces getInterfaceRequest() {
		return interfaceRequest;
	}

	public void setInterfaceRequest(Interfaces interfaceRequest) {
		this.interfaceRequest = interfaceRequest;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public RequestMethod getMethod() {
		return method;
	}

	public void setMethod(RequestMethod method) {
		this.method = method;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public Integer getNtype() {
		return ntype;
	}

	public void setNtype(Integer ntype) {
		this.ntype = ntype;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getCdata() {
		return cdata;
	}

	public void setCdata(String cdata) {
		this.cdata = cdata;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDesc() {
		return responseDesc;
	}

	public void setResponseDesc(String responseDesc) {
		this.responseDesc = responseDesc;
	}

	public String getOpened() {
		return opened;
	}

	public void setOpened(String opened) {
		this.opened = opened;
	}

	public LocalDateTime getDateOpen() {
		return dateOpen;
	}

	public void setDateOpen(LocalDateTime dateOpen) {
		this.dateOpen = dateOpen;
	}

}
