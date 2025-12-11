// SimpleStudentRankManager.java
// Console-based Java project to manage student ranks using MySQL
// Features: Add, View, Rank, Search by Marks

import java.sql.*;
import java.util.*;

// Student class to store student data
class Student {
    int id;
    String name;
    String rollNo;
    int marks;

    public Student(int id, String name, String rollNo, int marks) {
        this.id = id;
        this.name = name;
        this.rollNo = rollNo;
        this.marks = marks;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Roll No: " + rollNo + ", Marks: " + marks;
    }
}

public class SimpleStudentRankManager {
    // MySQL connection details (update as needed)
    static final String URL = "jdbc:mysql://localhost:3306/studentdb";
    static final String USER = "root";
    static final String PASS = "QAZmlp123!@#";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            // Menu-driven interaction
            System.out.println("\n--- Student Rank Manager ---");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Rank Students");
            System.out.println("4. Search by Marks");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1: addStudent(sc); break;
                case 2: viewStudents(); break;
                case 3: rankStudents(); break;
                case 4: searchByMarks(sc); break;
                case 5: System.out.println("Exiting."); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // Add a student to the database
    static void addStudent(Scanner sc) {
        System.out.print("Enter Name: ");
        String name = sc.next();
        System.out.print("Enter Roll No: ");
        String rollNo = sc.next();
        System.out.print("Enter Marks: ");
        int marks = sc.nextInt();
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "INSERT INTO students(name, roll_no, marks) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, rollNo);
            ps.setInt(3, marks);
            ps.executeUpdate();
            System.out.println("Student added.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // View all students
    static void viewStudents() {
        List<Student> students = fetchStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    // Rank students by marks (descending)
    static void rankStudents() {
        List<Student> students = fetchStudents();
        students.sort((a, b) -> b.marks - a.marks); // Comparator for descending marks
        System.out.println("--- Ranked Students ---");
        for (Student s : students) {
            System.out.println(s);
        }
    }

    // Search students by specific marks
    static void searchByMarks(Scanner sc) {
        System.out.print("Enter marks to search: ");
        int marks = sc.nextInt();
        List<Student> students = fetchStudents();
        boolean found = false;
        for (Student s : students) {
            if (s.marks == marks) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No student found with marks: " + marks);
        }
    }

    // Fetch all students from DB
    static List<Student> fetchStudents() {
        List<Student> list = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                list.add(new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("roll_no"),
                    rs.getInt("marks")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return list;
    }
}
