package models;

import java.sql.Timestamp;

public class Observation {
    private Timestamp timeStamp;
    private float value;

    public Observation(Timestamp timeStamp, float value) {
        this.timeStamp = timeStamp;
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }
}
