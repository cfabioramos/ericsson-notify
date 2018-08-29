package br.com.ericsson.smartnotification.domain.log;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMethod;

import br.com.ericsson.smartnotification.enums.Interfaces;
import br.com.ericsson.smartnotification.enums.LogType;
import br.com.ericsson.smartnotification.utils.Util;

public final class LogPropertiesBuilder {

	private LogProperties logProperties;
	
	private LogPropertiesBuilder(LogProperties logProperties) {
		this.logProperties = logProperties;
	}

	public static LogPropertiesBuilder getBuilder() {
		return new LogPropertiesBuilder(new LogProperties());
	}
	
	public LogProperties getLogProperties() {
		return logProperties;
	}

	public LogPropertiesBuilder setSent(String sent) {
		this.logProperties.setSent(sent);
		return this;
	}


	public LogPropertiesBuilder setKeyword(String keyword) {
		this.logProperties.setKeyword(keyword);
		return this;
	}

	public LogPropertiesBuilder setChannel(String channel) {
		this.logProperties.setChannel(channel);
		return this;
	}

	public LogPropertiesBuilder setApp(String app) {
		this.logProperties.setApp(app);
		return this;
	}

	public LogPropertiesBuilder setTries(Long tries) {
		this.logProperties.setTries(tries);
		return this;
	}

	public LogPropertiesBuilder setErrorType(String errorType) {
		this.logProperties.setErrorType(errorType);
		return this;
	}

	public LogPropertiesBuilder setCampaign(String campaign) {
		this.logProperties.setCampaign(campaign);
		return this;
	}

	public LogPropertiesBuilder setEventId(String eventId) {
		this.logProperties.setEventId(eventId);
		return this;
	}

	public LogPropertiesBuilder setEventName(String eventName) {
		this.logProperties.setEventName(eventName);
		return this;
	}

	public LogPropertiesBuilder setTemplate(String template) {
		this.logProperties.setTemplate(template);
		return this;
	}

	public LogPropertiesBuilder setRule(String rule) {
		this.logProperties.setRule(rule);
		return this;
	}

	public LogPropertiesBuilder setChannelType(String channelType) {
		this.logProperties.setChannelType(channelType);
		return this;
	}

	public LogPropertiesBuilder setMsisdn(String msisdn) {
		this.logProperties.setMsisdn(msisdn);
		return this;
	}

	public LogPropertiesBuilder setMsgId(String msgId) {
		this.logProperties.setMsgId(msgId);
		return this;
	}

	public LogPropertiesBuilder setMsgDetail(String msgDetail) {
		this.logProperties.setMsgDetail(msgDetail);
		return this;
	}

	public LogPropertiesBuilder setCreated(LocalDateTime created) {
		this.logProperties.setCreated(created);
		return this;
	}

	public LogPropertiesBuilder setNotificationId(String notificationId) {
		this.logProperties.setNotificationId(notificationId);
		return this;
	}

	public LogPropertiesBuilder setLogType(LogType logType) {
		this.logProperties.setLogType(logType);
		return this;
	}

	public LogPropertiesBuilder setInterfaceRequest(Interfaces interfaceRequest) {
		this.logProperties.setInterfaceRequest(interfaceRequest);
		return this;
	}

	public LogPropertiesBuilder setEndpoint(String endpoint) {
		this.logProperties.setEndpoint(endpoint);
		return this;
	}

	public LogPropertiesBuilder setPort(String port) {
		this.logProperties.setPort(port);
		return this;
	}

	public LogPropertiesBuilder setMethod(RequestMethod method) {
		this.logProperties.setMethod(method);
		return this;
	}

	public LogPropertiesBuilder setProcess(String process) {
		this.logProperties.setProcess(process);
		return this;
	}

	public LogPropertiesBuilder setNtype(Integer ntype) {
		this.logProperties.setNtype(ntype);
		return this;
	}

	public LogPropertiesBuilder setTemplateId(String templateId) {
		this.logProperties.setTemplateId(templateId);
		return this;
	}

	public LogPropertiesBuilder setHeader(String header) {
		this.logProperties.setHeader(header);
		return this;
	}
	
	public LogPropertiesBuilder setRequest(HttpServletRequest request) {
		this.setEndpoint(request.getRequestURL().toString())
			.setPort(String.valueOf(request.getLocalPort()))
			.setMethod(RequestMethod.valueOf(request.getMethod()))
			.setProcess(request.getServletPath())
			.setHeader(this.getHeader(request))
			.setCdata(this.getCdata(request))
			.setRequestId(Util.gerateId());
		
		return this;
	}

	private String getHeader(HttpServletRequest request) {
		StringBuilder header = new StringBuilder("headers : ").append("{");
		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String key = names.nextElement();
			String value = request.getHeader(key);
			header.append("\"".concat(key).concat("\"")).append(" : ").append("\"".concat(value).concat("\""));
			if(names.hasMoreElements()) {
				header.append(", ");
			}
		}
		header.append("}");
		return header.toString();
	}
	
	private String getCdata(HttpServletRequest request) {
		StringBuilder cdata = new StringBuilder();
		try {
	        BufferedReader reader = request.getReader();
	        char[] charBuffer = new char[128];
	        int bytesRead = -1;
	        while ((bytesRead = reader.read(charBuffer)) > 0) {
	        	cdata.append(charBuffer, 0, bytesRead);
	        }
	        reader.close();
	        return cdata.toString();
		} catch (IOException e) {
			return e.getMessage();
		}
	}
	
	public LogPropertiesBuilder setCdata(String cdata) {
		this.logProperties.setCdata(cdata);
		return this;
	}

	public LogPropertiesBuilder setRequestId(String requestId) {
		this.logProperties.setRequestId(requestId);;
		return this;
	}

	public LogPropertiesBuilder setResponseCode(String responseCode) {
		this.logProperties.setResponseCode(responseCode);
		return this;
	}

	public LogPropertiesBuilder setResponseDesc(String responseDesc) {
		this.logProperties.setResponseDesc(responseDesc);
		return this;
	}
	
	public LogPropertiesBuilder setOpened(String opened) {
		this.logProperties.setOpened(opened);
		return this;
	}
	
	public LogPropertiesBuilder setDateOpen(LocalDateTime dateOpen) {
		this.logProperties.setDateOpen(dateOpen);
		return this;
	}

}
