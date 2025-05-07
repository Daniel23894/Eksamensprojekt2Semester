package model;

public enum StateStatus {
    NOT_STARTED(0, "Ikke startet"),
    IN_PROGRESS(1, "I gang"),
    COMPLETED(2, "Afsluttet"),
    PAUSED(3, "Pauset"),
    CANCELLED(4, "Annulleret");

    private final int value;
    private final String description;

    StateStatus(int value, String description){
        this.value = value;
        this.description = description;
    }

    public int getValue(){
        return value;
    }

    public String getDescription(){
        return description;
    }

    public static StateStatus fromValue(int value){
        for (StateStatus aStatus : StateStatus.values()){
            if (aStatus.getValue() == value){
                return aStatus;
            }
        }
        throw new IllegalArgumentException("Ugyldigt statusværdi: "+ value + " Gyldige værdier er: 0, 1, 2, 3, 4.");
    }

}
