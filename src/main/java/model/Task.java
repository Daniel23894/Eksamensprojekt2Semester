package model;

public class Task {
    private static int idCounter = 1;

    private int id;
    private String title;
    private String description;

    // Constructor with auto-generated ID
    public Task(String title, String description) {
        this.id = idCounter++;
        this.title = title;
        this.description = description;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    // Setters (no setId to prevent manual ID changes)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
