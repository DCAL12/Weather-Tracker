package util;
import com.google.gson.Gson;

public class BuildJSON {

    public static String toJSON(Object object) {
        Gson gson = new Gson();
        System.out.println(object);
        return gson.toJson(object);
    }
}
