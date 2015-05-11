package models;

import java.sql.Date;

public class Measurement {
    private Date timeStamp;
    private double value;

    public Measurement(Date timeStamp, double value) {
        this.timeStamp = timeStamp;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}
