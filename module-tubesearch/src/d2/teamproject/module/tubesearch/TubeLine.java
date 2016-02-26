package d2.teamproject.module.tubesearch;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import javafx.scene.paint.Color;

public class TubeLine {
    private String id, name;
    private Color colour;

    public TubeLine(String id, String name, Color colour) {
        this.id = id;
        this.name = name;
        this.colour = colour;
    }

    public static TubeLine fromJson(JsonValue val) {
        JsonObject obj = val.asObject();
        return new TubeLine(
                obj.get("id").asString(),
                obj.get("name").asString(),
                Color.web(obj.get("colour").asString())
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getColour() {
        return colour;
    }

    @Override
    public String toString() {
        return "TubeLine{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", colour=" + colour +
                '}';
    }
}