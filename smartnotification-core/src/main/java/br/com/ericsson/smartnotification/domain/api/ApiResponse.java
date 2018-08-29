package br.com.ericsson.smartnotification.domain.api;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerMapping;

import br.com.ericsson.smartnotification.utils.JsonUtil;

public final class ApiResponse {

    private HttpStatus status;

    private Object response;

    private String message;

    private LocalDateTime timestamp;

    private String path;

    public ApiResponse(HttpStatus status, Object response, String message, HttpServletRequest request) {
        this.status = status;
        this.response = response;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Object getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return JsonUtil.parseToJsonString(this);
    }

}
