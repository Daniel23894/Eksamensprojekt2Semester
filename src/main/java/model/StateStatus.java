package model;

public enum StateStatus {
    NOT_STARTED(0),
    IN_PROGRESS(1),
    COMPLETED(2),
    PAUSED(3),
    CANCELLED(4);

    private final int value;

    StateStatus(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static StateStatus fromValue(int value){
        for (StateStatus aStatus : StateStatus.values()){
            if (aStatus.getValue() == value){
                return aStatus;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value + " Valid values are: 0, 1, 2, 3, 4.");
    }

}
