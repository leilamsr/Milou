import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Manager {
    private List<Email> inbox;
    private List<Email> sent;
    private int email_count;

    public Manager() {
        inbox = new ArrayList<>();
        sent = new ArrayList<>();
        email_count = 1000;
    }

    public String generateCode() {
        email_count++;
        String code = Integer.toString(email_count, 36);
        while (code.length() < 6) {
            code = "0" + code;
        }
        return code;
    }

    public Email sendEmail(String sender, List<String> recipients, String subject, String body) {
        String code = generateCode();
        Email email = new Email(code, sender, recipients, subject, body);
        sent.add(email);

        for (String recipient : recipients) {
            if (!recipient.equals(sender)) {
                Email copy = new Email(code, sender, Arrays.asList(recipient), subject, body);
                inbox.add(copy);
            }
        }

        return email;
    }

    public List<Email> getInbox() {
        return inbox;
    }

    public List<Email> getOutbox() {
        return sent;
    }

    public List<Email> getUnreadEmails() {
        List<Email> unread = new ArrayList<>();
        for (Email email : inbox) {
            if (!email.isRead()) {
                unread.add(email);
            }
        }

        return unread;
    }

    public Email findEmailByCode(String code) {
        for (Email email : inbox) {
            if (email.getCode().equals(code)) {
                return email;
            }
        }

        for (Email email : sent) {
            if (email.getCode().equals(code)) {
                return email;
            }
        }

        return null;
    }
}
