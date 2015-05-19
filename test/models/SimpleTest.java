package models;

import org.junit.Test;
import util.BuildJSON;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SimpleTest {

    @Test
    public void testWriteJSON() throws Exception {
        Sensor sensor = new Sensor(1, "test", "1");
        List<Observation> observations = new ArrayList<>();
        observations.add(new Observation(new Timestamp(System.currentTimeMillis()), 11.1f));
        observations.add(new Observation(new Timestamp(System.currentTimeMillis()), 12.2f));
        observations.add(new Observation(new Timestamp(System.currentTimeMillis()), 131.1f));
        sensor.setObservations(observations);
        System.out.println(BuildJSON.toJSON(sensor));
        assertTrue(true);
    }
}