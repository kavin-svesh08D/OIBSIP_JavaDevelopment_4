import java.util.*;

class User {
    private final String id;
    private String password;
    private String name;

    public User(String id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Question {
    String questionText;
    String[] options;
    int correctAnswer;

    public Question(String questionText, String[] options, int correctAnswer) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
}

public class OnlineExamSystem {
    private static final Map<String, User> users = new HashMap<>();
    private static User currentUser = null;
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Question> questions = new ArrayList<>();
    private static int score = 0;
    private static boolean isExamActive = false;

    public static void main(String[] args) {
        // Sample data
        users.put("kavin", new User("kavin", "Hello@2006", "Kavin"));

        loadQuestions();

        System.out.println("=== Welcome to Online Examination System ===");

        while (true) {
            if (currentUser == null) {
                login();
            } else {
                mainMenu();
            }
        }
    }

    private static void loadQuestions() {
        questions.add(new Question("What is the size of int in Java?",
                new String[] { "16-bit", "32-bit", "64-bit", "8-bit" }, 2));
        questions.add(new Question("Which keyword is used to inherit a class?",
                new String[] { "implements", "extends", "this", "super" }, 2));
        questions.add(new Question("Java is a ______ language.",
                new String[] { "Compiled", "Interpreted", "Both Compiled and Interpreted", "None" }, 3));
    }

    private static void login() {
        System.out.print("\nEnter User ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Password: ");
        String pass = scanner.nextLine();

        if (users.containsKey(id) && users.get(id).getPassword().equals(pass)) {
            currentUser = users.get(id);
            System.out.println("Login successful! Welcome, " + currentUser.getName());
        } else {
            System.out.println("Invalid credentials. Try again.");
        }
    }

    private static void mainMenu() {
        System.out.println("\n1. Update Profile and Password");
        System.out.println("2. Start Examination");
        System.out.println("3. Logout");
        System.out.println("4. Exit");
        System.out.print("Select an option: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                updateProfile();
                break;
            case "2":
                startExam();
                break;
            case "3":
                logout();
                break;
            case "4":
                System.out.println("Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void updateProfile() {
        System.out.print("Enter new name (current: " + currentUser.getName() + "): ");
        String newName = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPass = scanner.nextLine();

        currentUser.setName(newName);
        currentUser.setPassword(newPass);
        System.out.println("Profile updated successfully!");
    }

    private static void startExam() {
        isExamActive = true;
        score = 0;
        int timeInSeconds = 30;

        System.out.println("\n--- Exam Started! Time: " + timeInSeconds + " seconds ---");

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isExamActive) {
                    System.out.println("\n\nTime's up! Auto-submitting...");
                    isExamActive = false;
                    System.out.println("Press Enter to see your results.");
                }
            }
        }, timeInSeconds * 1000);

        for (int i = 0; i < questions.size(); i++) {
            if (!isExamActive)
                break;

            Question q = questions.get(i);
            System.out.println("\nQ" + (i + 1) + ": " + q.questionText);
            for (int j = 0; j < q.options.length; j++) {
                System.out.println((j + 1) + ". " + q.options[j]);
            }

            System.out.print("Your answer (1-4): ");

            // This is a simple wait for input. In a real console app,
            // the timer thread might interrupt this if time's up.
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (!isExamActive)
                    break; // If timer finished while typing

                try {
                    int ans = Integer.parseInt(input);
                    if (ans == q.correctAnswer) {
                        score++;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Question skipped.");
                }
            }
        }

        timer.cancel();
        if (isExamActive) {
            System.out.println("\nExam completed.");
        }
        isExamActive = false;
        System.out.println("Your Score: " + score + "/" + questions.size());
    }

    private static void logout() {
        System.out.println("Logging out, session closed.");
        currentUser = null;
    }
}
