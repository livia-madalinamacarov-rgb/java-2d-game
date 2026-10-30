package Maps;

public class MapParser {

    public static GameMap loadMap(String path) {
        return new GameMap(path);
    }
}