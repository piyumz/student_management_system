## Setup Instructions

### Prerequisites

Before running the project, make sure you have:

- Java Development Kit (JDK) 8 or later installed
- A Java IDE (IntelliJ IDEA, Eclipse, or VS Code) or a terminal/command prompt
- Git (optional, if cloning from GitHub)

### 1. Clone the Repository (Optional)

```bash
git clone https://github.com/piyumz/student_management_system.git
cd student_management_system
```

Or download the project as a ZIP file and extract it.

### 2. Open the Project

Open the project folder in your preferred Java IDE, or navigate to the project folder using the terminal.

### 3. Compile the Program

```bash
javac StudentManagementSystem.java
```

### 4. Run the Program

```bash
java StudentManagementSystem
```

### 5. Using the Application

When the application starts, it automatically loads two sample student records:

- S101 – John Silva
- S102 – Nimal Perera

You will then see the main menu:

```
1. View Active Students (ArrayList)
2. Submit Registration Request (Queue Enqueue)
3. Process Next Registration (Queue Dequeue -> ArrayList)
4. Update Student Details (ArrayList & Stack Save)
5. Delete Student (ArrayList & Stack Save)
6. Undo Last Delete/Update (Stack Pop)
7. Search Student (Linear Search)
8. Exit
```

Simply enter the number of the operation you want to perform and follow the prompts displayed in the console.

### Project Structure

```
StudentManagementSystem/
│
├── StudentManagementSystem.java    # Main application (contains Student, UndoAction, and controller)
└── README.md                       # Project documentation
```

### Requirements

- JDK 8 or later
- Windows, macOS, or Linux
- Console/Terminal or Java IDE

### Expected Output

After running the application, the console displays the Student Management System menu where you can:

- View student records
- Submit registration requests
- Process registrations
- Update student details
- Delete students
- Undo the last update/delete operation
- Search for students by ID
- Exit the application
