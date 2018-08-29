package br.com.ericsson.smartnotification.learningtests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import br.com.ericsson.smartnotification.domain.Event;

/**
 * <h1>Evaluating Expressions Using Spring Expression Language (SpEL)</h1>
 * <a href="https://dzone.com/articles/evaluating-expressions-using"/>
 */
public class SpringExpressionLanguagePocTest {

    private ExpressionParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new SpelExpressionParser();
    }

    @Test
    public void expressionLanguageLiterals() {
        Expression exp = parser.parseExpression("'Hello World'");
        String message = (String) exp.getValue();

        assertThat(message).isEqualTo("Hello World");
    }

    @Test
    public void expressionLanguageMethodInvocation() {
        Expression exp = parser.parseExpression("'Hello World'.concat('!')");
        String message = (String) exp.getValue();

        assertThat(message).isEqualTo("Hello World!");


        exp = parser.parseExpression("'Hello World'.length()");
        Integer size = exp.getValue(Integer.class);

        assertThat(size).isEqualTo(11);

        exp = parser.parseExpression("'Hello World'.split(' ')[0]");
        message = (String)exp.getValue();

        assertThat(message).isEqualTo("Hello");
    }

    @Test
    public void operators() {
        User user = new User("John", "Doe",
                "john.doe@acme.com", true, 30);

        Expression exp = parser.parseExpression("age > 18");
        assertThat(exp.getValue(user, Boolean.class)).isTrue();

        exp = parser.parseExpression("isAdmin()");
        assertThat(exp.getValue(user, Boolean.class)).isTrue();

        exp = parser.parseExpression("age < 18 and isAdmin()");
        assertThat(exp.getValue(user, Boolean.class)).isFalse();
    }
    
    @Test
    public void variables() {
        User user = new User("John", "Doe",
                "john.doe@acme.com", true, 30);
        Application app = new Application("Facebook", false);

        StandardEvaluationContext context = new StandardEvaluationContext();

        context.setVariable("user", user);
        context.setVariable("app", app);

        Expression exp = parser.parseExpression("#user.isAdmin() and #app.isActive()");

        boolean result = exp.getValue(context,Boolean.class);

        assertThat(result).isFalse();
    }

    @Test
    public void evaluateEventRule() {   
        Map<String, String> eventData = new HashMap<>();
        eventData.put("promoId", "10");
        eventData.put("gender", "M");
        eventData.put("expiryDate", "2018-07-05");
        eventData.put("promoCode", "1000");
        
        Event event = Event.buildEvent(eventData);
        
        assertThat(booleanForEventExpression(event,
                "#event.getField('promoId').value == '10' and #event.getField('gender').value== 'M'")).isTrue();
        
        assertThat(booleanForEventExpression(event,
                "#event.getField('promoId').value == '10' and #event.getField('gender').value != 'M'")).isFalse();
        
        assertThat(booleanForEventExpression(event,
                "#event.getField('promoId').value == '10' or #event.getField('gender').value != 'M'")).isTrue();
        
        assertThat(booleanForEventExpression(event,
                "#event.getField('promoId').value == '10'" 
                    + " and " +
                    "#event.getField('gender').value != 'M'" 
                        + " or " +
                    "#event.getField('promoCode').value == '1000'" 
                    + " and " + 
                "#event.getField('expiryDate').value == '2018-07-05'"))
            .isTrue();
    }

    private boolean booleanForEventExpression(Event event, String expressionString) {
        StandardEvaluationContext context = new StandardEvaluationContext();

        context.setVariable("event", event);

        Expression exp = 
                parser.parseExpression(expressionString);

        boolean result = exp.getValue(context, Boolean.class);
        return result;
    }
    
    private class User {
        private String firstName;
        private String lastName;
        private String email;
        private boolean admin;
        private int age;

        public User(String firstName, String lastName, String email, boolean admin, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.admin = admin;
            this.age = age;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isAdmin() {
            return admin;
        }

        public void setAdmin(boolean admin) {
            admin = admin;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    private class Application {
        private String name;
        private boolean active;

        public Application(String name, boolean active) {
            this.name = name;
            this.active = active;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
