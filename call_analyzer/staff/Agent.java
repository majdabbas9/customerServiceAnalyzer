package call_analyzer.staff;

public class Agent extends Employee {
    private double rating;
    private int totalCallsHandled;
    private String specialization;

    public Agent(String name, String email, String department, String specialization) {
        super(name, email, department);
        this.specialization = specialization;
        this.rating = 0.0;
        this.totalCallsHandled = 0;
    }

    // Getters and Setters
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getTotalCallsHandled() {
        return totalCallsHandled;
    }

    public void setTotalCallsHandled(int totalCallsHandled) {
        this.totalCallsHandled = totalCallsHandled;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void incrementCalls() {
        this.totalCallsHandled++;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", specialization='" + specialization + '\'' +
                ", rating=" + rating +
                ", totalCallsHandled=" + totalCallsHandled +
                '}';
    }
}
