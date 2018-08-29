package br.com.ericsson.smartnotification.util.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;

import org.junit.Test;
import org.springframework.util.StringUtils;

import br.com.ericsson.smartnotification.domain.Event;
import br.com.ericsson.smartnotification.domain.EventField;
import br.com.ericsson.smartnotification.enums.FieldType;
import br.com.ericsson.smartnotification.utils.JsonUtil;


public final class JsonUtilTest {
    
    @Test
    public void parseToJsonStringTest() {
        Event event = new Event(1, "test", LocalDateTime.now());
        event.getFields().add(new EventField("teste", "teste", FieldType.STRING, true, null));
        String eventJsonString = JsonUtil.parseToJsonString(event);
        assertFalse(StringUtils.isEmpty(eventJsonString));
    }
    
    @Test
    public void parseToObjetcTest() {
        String json = "{\"eventType\":1,\"description\":\"test\",\"dateTimeReceived\":{\"date\":{\"year\":2018,\"month\":7,\"day\":20},\"time\":{\"hour\":14,\"minute\":56,\"second\":15,\"nano\":563943000}},\"fields\":[{\"name\":\"teste\",\"description\":\"teste\",\"type\":\"STRING\",\"required\":true}]}";
        Event event = JsonUtil.getGson().fromJson(json, Event.class);
        assertNotNull(event);
        assertFalse(event.getFields().isEmpty());
    }
    
    @Test
    public void parseToJsonStringToObjectTest() {
        Event event = new Event(1, "test", LocalDateTime.now());
        event.getFields().add(new EventField("teste", "teste", FieldType.STRING, true, null));
        String eventJsonString = JsonUtil.parseToJsonString(event);
        Event eventJsonObject = JsonUtil.getGson().fromJson(eventJsonString, Event.class);
        assertFalse(StringUtils.isEmpty(eventJsonString));
        assertFalse(StringUtils.isEmpty(eventJsonObject));
    }
    
    
}
