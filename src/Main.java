import java.util.*;

public class Main {

    private static Scanner scn = new Scanner(System.in);
    private static String currentUserName = "leila.m@milou.com";
    private static Manager manager = new Manager();

    public static void main(String[] args) {

        ArrayList<User> users = new ArrayList<>();

        users.add(new User("Leila", "Leila.m@milou.com", "password123"));

        System.out.println("Welcome to Milou Email Service!");

        while(true) {
            System.out.print("[L]ogin, [S]ign up: ");
            String choice = scn.nextLine().trim().toLowerCase();

            if (choice.equals("l")) {
                System.out.print("Email: ");
                String email = scn.nextLine().trim().toLowerCase();
                if (!email.contains("@")) {
                    email = email + "@milou.com";
                }

                System.out.print("Password: ");
                String password = scn.nextLine().trim();

                boolean loggedIn = false;
                for (User user : users) {
                    if (user.email.equalsIgnoreCase(email) && user.password.equals(password)) {
                        currentUserName = user.email;
                        System.out.printf("\nWelcome back, %s!\n", user.name);

                        System.out.println("\nUnread Emails:");
                        System.out.println("3 unread emails:");
                        System.out.println("+ Faegheh@milou.com - Our new meeting (1bc170)");
                        System.out.println("+ Nastaran@milou.com - Book suggestions (fnjd1o)");
                        System.out.println("+ Narges@milou.com - New feature (12dsb1)");

                        loggedIn = true;
                        System.out.println("Select your desired number.");
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
                }

                if (!loggedIn) {
                    System.out.println("Invalid email or password!");
                }

            } else if (choice.equals("s")) {
                System.out.print("Name: ");
                String name = scn.nextLine().trim();

                System.out.print("Email: ");
                String email = scn.nextLine().trim();
                if (!email.contains("@")) {
                    email = email + "@milou.com";
                }

                boolean emailExists = false;
                for (User user : users) {
                    if (user.email.equals(email)) {
                        emailExists = true;
                        break;
                    }
                }

                if (emailExists) {
                    System.out.println("This email is already registered!");
                    continue;
                }

                System.out.print("Password: ");
                String password = scn.nextLine().trim();

                if (password.length() < 8) {
                    System.out.println("Password must be at least 8 characters!");
                    continue;
                }

                users.add(new User(name, email, password));
                System.out.println("\nYour new account is created.");
                System.out.println("Go ahead and login!");

            } else {
                System.out.println("Please enter 'L' for Login or 'S' for Sign up.");
            }
        }
    }

    static class User {
        String name;
        String email;
        String password;

        public User(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
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