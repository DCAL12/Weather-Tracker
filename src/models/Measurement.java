package models;

import java.sql.Date;
import java.sql.Timestamp;

public class Measurement {
    private Timestamp timeStamp;
    private float value;

    public Measurement(Timestamp timeStamp, float value) {
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
