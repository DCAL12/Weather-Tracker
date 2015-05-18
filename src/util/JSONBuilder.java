package util;

import models.Observation;
import models.Sensor;

import java.util.List;

public class JSONBuilder {

    public static String sensorToJSON(Sensor sensor) {
        StringBuilder JSON = new StringBuilder();

        JSON.append("{\n");
        JSON.append("'id': '" + sensor.getId() + "',\n");
        JSON.append("'label': '" + sensor.getLabel() + "',\n");
        JSON.append("'enabled': '" + sensor.isEnabled() + "'\n"); // ensure last element has no comma
        JSON.append("}\n");

        return JSON.toString();
    }

    public static String sensorToJSON(Sensor sensor, List<Observation> observations) {
        StringBuilder JSON = new StringBuilder();

        JSON.append("{\n");
        JSON.append("'id': '" + sensor.getId() + "',\n");
        JSON.append("'label': '" + sensor.getLabel() + "',\n");
        JSON.append("'enabled': '" + sensor.isEnabled() + "',\n");

        JSON.append("{'observations': [");
        observations.forEach(observation -> {
            JSON.append("{\n");
            JSON.append("'timestamp': '" + observation.getTimeStamp() + "',\n");
            JSON.append("'value': '" + observation.getValue() + "'\n"); // ensure last element has no comma
            JSON.append("},\n");
        });
        JSON.deleteCharAt(JSON.lastIndexOf(",")); // ensure last element has no comma
        JSON.append("]}");
        JSON.append("}\n");

        return JSON.toString();
    }

    public static String sensorListToJSON(List<Sensor> sensors) {
        StringBuilder JSON = new StringBuilder();

        JSON.append("{'sensors': [");
        sensors.forEach(sensor -> JSON.append(sensorToJSON(sensor) + ","));
        JSON.deleteCharAt(JSON.lastIndexOf(",")); // ensure last element has no comma
        JSON.append("]}");

        return JSON.toString();
    }
}
