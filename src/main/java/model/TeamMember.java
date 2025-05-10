package model;


public class TeamMember {
    private int memberId;
    private String name;
    private String email;
    private String role;
    private int hoursPerDay;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public int getHoursPerDay() {
        return hoursPerDay;
    }

    public void setHoursPerDay(int hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
    }

    // Metoder til at tjekke om teammedlemmet har en bestemt rolle
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
    public boolean isDeveloper() {
        return "DEVELOPER".equalsIgnoreCase(role);
    }
    public boolean isPO() {
        return "PO".equalsIgnoreCase(role) || "PRODUCT_OWNER".equalsIgnoreCase(role);
    }

    // ToString metode for at vise objektet som tekst
    @Override
    public String toString() {
        return "TeamMember{" +
                "memberId=" + memberId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", hoursPerDay=" + hoursPerDay +
                '}';
    }
}