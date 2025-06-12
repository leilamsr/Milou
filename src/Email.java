import java.util.List;

public class Email {
    private String code;
    private String sender;
    private List<String> recipients;
    private String subject;
    private String body;
    private boolean read;

    public Email(String code, String sender, List<String> recipients, String subject, String body) {
        this.code = code;
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.body = body;
        this.read = false;
    }

    public String getCode() {
        return code;
    }

    public String getSender() {
        return sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
