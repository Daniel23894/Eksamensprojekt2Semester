package model;

public enum Role {
    ADMIN(0, "Administrator"),
    DEVELOPER(1, "Developer"),
    PRODUCT_OWNER(2, "Product Owner");

    private final int value;
    private final String description;

    Role(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    /** Convert from integer value to Role **/
    public static Role fromValue(int value) {
        for (Role role : Role.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role value: " + value + ". Valid values are: 0, 1, 2");
    }

}
