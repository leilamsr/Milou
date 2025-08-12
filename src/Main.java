import java.util.*;

public class Main {

    private static Scanner scn = new Scanner(System.in);
    private static String currentUserName = "leila.m@milou.com";
    private static Manager manager = new Manager();

    public static void main(String[] args) {
        System.out.println("Welcome!\nSelect your desired number.");
        printCommands();

        while (true) {
            System.out.println("Desired number: ");
            String number = scn.nextLine();

            switch (number) {
                case "1":sendEmail(); break;
                case "2":viewEmail(); break;
                case "3":replyEmail(); break;
                case "4":forwardEmail(); break;
                case "5":
                    System.out.println("Goodbye!");return;
                default:
                    System.out.println("You have entered the wrong number!Please try again.");
                    printCommands();
            }
        }
    }

    static List<String> parseRecipients(String line) {
        List<String> list = new ArrayList<>();
        for (String part : line.split(",")) {
            part = part.trim();
            if (!part.isEmpty()) {
                list.add(part.toLowerCase());
            }
        }
        return list;
    }

    static void printCommands() {
        System.out.println("1.send\n2.view\n3.reply\n4.forward\n5.exit");
    }

    static void sendEmail() {
        String rec = scn.nextLine().trim();
        List<String> recipients = parseRecipients(rec);
        if (recipients.isEmpty()) {
            System.out.println("No valid recipients!");
            return;
        }

        System.out.println("Subject: ");
        String subject = scn.nextLine().trim();

        System.out.println("Body: ");
        String Body = scn.nextLine().trim();

        Email email = manager.sendEmail(currentUserName, recipients, subject, Body);

        System.out.println("Email sent!");
        System.out.println("code : " + email.getCode());
    }

    static void viewEmail() {
        System.out.println("which one :\n1.all emails\n2.unread emails\n3.send emails\n4.code\n5.exit");
        String choice = scn.nextLine().trim();

        switch (choice) {
            case "1":showAllEmails(); break;
            case "2":showUnreadEmails(); break;
            case "3":showSendEmails(); break;
            case "4":Code(); break;
            case "5": break;
            default:
                System.out.println("You have entered the wrong choice!Please try again."); break;
        }
    }

    static void showAllEmails() {
        List<Email> inbox = manager.getInbox();
        System.out.println("All emails: ");
        if (inbox.isEmpty()) {
            System.out.println("No emails!");
            return;
        }
        for (Email email : inbox) {
            System.out.println(email.getSender() + " - " + email.getSubject() + " - " + email.getCode());
        }
    }

    static void showUnreadEmails() {
        List<Email> unread = manager.getUnreadEmails();
        System.out.println("Unread emails: ");
        if (unread.isEmpty()) {
            System.out.println("No emails!");
            return;
        }
        for (Email email : unread) {
            System.out.println(email.getSender() + " - " + email.getSubject() + " - " + email.getCode());
        }
    }

    static void showSendEmails() {
        List<Email> send = manager.getOutbox();
        System.out.println("Send emails: ");
        if (send.isEmpty()) {
            System.out.println("No emails!");
            return;
        }
        for (Email email : send) {
            System.out.println(String.join(", ", email.getRecipients()) + " - " + email.getSubject() + " - " + email.getCode());
        }
    }

    static void Code() {
        System.out.println("Code: ");
        String code = scn.nextLine().trim();

        Email email = manager.findEmailByCode(code);
        if (email == null) {
            System.out.println("No email with that code!");
            return;
        }

        boolean isRecipient = email.getRecipients().contains(currentUserName);
        boolean isSender = email.getSender().equals(currentUserName);
        if (!isRecipient && !isSender) {
            System.out.println("You cannot read this email!");
            return;
        }
        System.out.println("\nCode: " + email.getCode());
        System.out.println("Recipients: " + String.join(", ", email.getRecipients()));
        System.out.println("Subject: " + email.getSubject());
        System.out.println("Body: " + email.getBody());

        email.setRead(true);
    }

    static void replyEmail() {
        System.out.println("Code: ");
        String code = scn.nextLine().trim();

        Email email = manager.findEmailByCode(code);
        if (email == null) {
            System.out.println("No email with that code!");
            return;
        }

        if (!email.getRecipients().contains(currentUserName) && !email.getSender().equals(currentUserName)) {
            System.out.println("You cannot reply this email!");
            return;
        }

        System.out.println("Body: ");
        String body = scn.nextLine().trim();
        Set<String> recipients = new HashSet<>(email.getRecipients());
        recipients.add(email.getSender());
        recipients.remove(currentUserName);

        if (recipients.isEmpty()) {
            System.out.println("No one to reply!");
            return;
        }

        String subject = "[Re]" + email.getSubject();
        Email reply = manager.sendEmail(currentUserName, new ArrayList<>(recipients), subject, body);

        System.out.println("Successfully sent your reply to email" + code);
        System.out.println("Code : ");
        System.out.println(reply.getCode());
    }
    static void forwardEmail() {
        System.out.println("Code: ");
        String code = scn.nextLine().trim();

        Email email = manager.findEmailByCode(code);
        if (email == null) {
            System.out.println("No email with that code!");
            return;
        }

        if (!email.getRecipients().contains(currentUserName) && !email.getSender().equals(currentUserName)) {
            System.out.println("You cannot forward this email!");
            return;
        }

        System.out.println("Recipients: ");
        String rec = scn.nextLine().trim();
        List<String> re = parseRecipients(rec);
        if (re.isEmpty()) {
            System.out.println("No valid recipients!");
            return;
        }

        String sub = "[Fw]" + email.getSubject();
        Email fwd = manager.sendEmail(currentUserName, re, sub, email.getBody());

        System.out.println("Successfully forwarded your email!\nCode: " + fwd.getCode());
    }
}