package PaooGame.Tiles;

import java.awt.image.BufferedImage;

public class VagonTile extends Tile {

    public VagonTile(BufferedImage image, int id) {
        super(image, id);
    }
    @Override
    public boolean IsSolid() {
        return true; // Jucătorul nu poate trece prin vagoane
    }
}