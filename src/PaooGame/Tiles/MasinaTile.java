package PaooGame.Tiles;

import java.awt.image.BufferedImage;

public class MasinaTile extends Tile {

    public MasinaTile(BufferedImage image, int id) {
        super(image, id);
    }
    @Override
    public boolean IsSolid() {
        return true; // Jucătorul nu poate trece prin mașini
    }
}