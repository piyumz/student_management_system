import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Scanner;

// --- Helper Classes ---

class Student {
    private String id;
    private String name;
    private String course;

    public Student(String id, String name, String course) {
        this.id = id;
        this.name = name;
        this.course = course;
    }

    // Copy constructor used to take snapshots for the undo Stack
    public Student(Student other) {
        this.id = other.id;
        this.name = other.name;
        this.course = other.course;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Course: " + course;
    }
}

// Represents an action we can undo
class UndoAction {
    enum ActionType { DELETE, UPDATE }

    private ActionType type;
    private Student studentState; // The state of the student *before* the action occurred

    public UndoAction(ActionType type, Student studentState) {
        this.type = type;
        this.studentState = studentState;
    }

    public ActionType getType() { return type; }
    public Student getStudentState() { return studentState; }
}

// --- Main Application Class ---

public class StudentManagementSystem {
    // 1. ArrayList: Primary storage for active students
    private static ArrayList<Student> studentDatabase = new ArrayList<>();

    // 2. Queue: Holds incoming registration requests (FIFO)
    private static Queue<Student> registrationQueue = new LinkedList<>();

    // 3. Stack: Stores historical actions to allow undos (LIFO)
    private static Stack<UndoAction> undoStack = new Stack<>();

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Pre-populate with a few students for a quick start
        studentDatabase.add(new Student("S101", "Alice Vance", "Computer Science"));
        studentDatabase.add(new Student("S102", "Bob Miller", "Data Science"));

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAllStudents();
                    break;
                case "2":
                    enqueueRegistration();
                    break;
                case "3":
                    processNextRegistration();
                    break;
                case "4":
                    updateStudent();
                    break;
                case "5":
                    deleteStudent();
                    break;
                case "6":
                    undoLastAction();
                    break;
                case "7":
                    searchStudent();
                    break;
                case "8":
                    running = false;
                    System.out.println("\nExiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n==================================");
        System.out.println("    STUDENT MANAGEMENT SYSTEM     ");
        System.out.println("==================================");
        System.out.println("1. View Active Students (ArrayList)");
        System.out.println("2. Submit Registration Request (Queue Enqueue)");
        System.out.println("3. Process Next Registration (Queue Dequeue -> ArrayList)");
        System.out.println("4. Update Student Details (ArrayList & Stack Save)");
        System.out.println("5. Delete Student (ArrayList & Stack Save)");
        System.out.println("6. Undo Last Delete/Update (Stack Pop)");
        System.out.println("7. Search Student (Linear Search)");
        System.out.println("8. Exit");
        System.out.println("----------------------------------");
        System.out.print("Queue Status: " + registrationQueue.size() + " pending | ");
        System.out.println("Undo Stack: " + undoStack.size() + " available");
    }

    // --- Core Database Methods (ArrayList) ---

    private static void viewAllStudents() {
        System.out.println("\n--- Active Student Records ---");
        if (studentDatabase.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        for (Student s : studentDatabase) {
            System.out.println(s);
        }
    }

    private static void searchStudent() {
        System.out.print("\nEnter Student ID to search: ");
        String id = scanner.nextLine();
        
        // Linear Search Algorithm
        for (Student s : studentDatabase) {
            if (s.getId().equalsIgnoreCase(id)) {
                System.out.println("\nStudent Found: " + s);
                return;
            }
        }
        System.out.println("Student with ID " + id + " not found.");
    }

    private static void updateStudent() {
        System.out.print("\nEnter Student ID to update: ");
        String id = scanner.nextLine();

        for (Student s : studentDatabase) {
            if (s.getId().equalsIgnoreCase(id)) {
                // Save state to stack BEFORE making the update
                undoStack.push(new UndoAction(UndoAction.ActionType.UPDATE, new Student(s)));

                System.out.print("Enter new Name (Leave blank to keep '" + s.getName() + "'): ");
                String newName = scanner.nextLine();
                if (!newName.trim().isEmpty()) {
                    s.setName(newName);
                }

                System.out.print("Enter new Course (Leave blank to keep '" + s.getCourse() + "'): ");
                String newCourse = scanner.nextLine();
                if (!newCourse.trim().isEmpty()) {
                    s.setCourse(newCourse);
                }

                System.out.println("Student details updated successfully!");
                return;
            }
        }
        System.out.println("Student not found.");
    }

    private static void deleteStudent() {
        System.out.print("\nEnter Student ID to delete: ");
        String id = scanner.nextLine();

        for (int i = 0; i < studentDatabase.size(); i++) {
            Student s = studentDatabase.get(i);
            if (s.getId().equalsIgnoreCase(id)) {
                // Save state to stack BEFORE deleting
                undoStack.push(new UndoAction(UndoAction.ActionType.DELETE, new Student(s)));
                
                studentDatabase.remove(i);
                System.out.println("Student '" + s.getName() + "' deleted successfully.");
                return;
            }
        }
        System.out.println("Student not found.");
    }

    // --- Queue Management Methods ---

    private static void enqueueRegistration() {
        System.out.println("\n--- New Registration Request ---");
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Course Name: ");
        String course = scanner.nextLine();

        Student newStudent = new Student(id, name, course);
        registrationQueue.add(newStudent);
        System.out.println("Request queued! " + name + " is waiting in line.");
    }

    private static void processNextRegistration() {
        System.out.println("\n--- Processing Next Registration ---");
        if (registrationQueue.isEmpty()) {
            System.out.println("No pending registration requests in the Queue.");
            return;
        }

        // Dequeue: FIFO processing
        Student nextStudent = registrationQueue.poll();
        studentDatabase.add(nextStudent);
        System.out.println("Successfully processed and registered: " + nextStudent.getName());
    }

    // --- Undo Management Methods (Stack) ---

    private static void undoLastAction() {
        System.out.println("\n--- Attempting Undo Operation ---");
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo! The stack is empty.");
            return;
        }

        // Pop last action: LIFO processing
        UndoAction lastAction = undoStack.pop();
        Student historicStudent = lastAction.getStudentState();

        if (lastAction.getType() == UndoAction.ActionType.DELETE) {
            // To undo a delete, we add the student back to our database
            studentDatabase.add(historicStudent);
            System.out.println("Undo successful: Restored deleted student '" + historicStudent.getName() + "'.");
        } 
        else if (lastAction.getType() == UndoAction.ActionType.UPDATE) {
            // To undo an update, we find the current version and revert its variables
            boolean restored = false;
            for (Student s : studentDatabase) {
                if (s.getId().equalsIgnoreCase(historicStudent.getId())) {
                    s.setName(historicStudent.getName());
                    s.setCourse(historicStudent.getCourse());
                    restored = true;
                    break;
                }
            }
            if (restored) {
                System.out.println("Undo successful: Reverted details for '" + historicStudent.getName() + "'.");
            } else {
                // If they deleted the student after updating them
                System.out.println("Undo failed: The student you updated has since been deleted.");
            }
        }
    }
}