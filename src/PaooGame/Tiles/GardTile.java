package PaooGame.Tiles;

import java.awt.image.BufferedImage;

public class GardTile extends Tile {

    public GardTile(BufferedImage image, int id) {
        super(image, id);
    }

    @Override
    public boolean IsSolid() {
        return true; // NU poți trece prin gard
    }
}