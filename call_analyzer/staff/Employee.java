package call_analyzer.staff;

public class Employee {
    private static int nextId = 1;
    private final int id;
    private String name;
    private String email;
    private String department;

    public Employee(String name, String email, String department) {
        this.id = nextId++;
        this.name = name;
        this.email = email;
        this.department = department;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
