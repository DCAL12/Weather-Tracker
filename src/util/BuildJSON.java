package util;
import com.google.gson.Gson;

public class BuildJSON {

    public static String toJSON(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
