package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

public class CutieTile extends Tile {

    public CutieTile(int id) {
        super(Assets.cutie, id);
    }

    @Override
    public boolean IsSolid() {

        return true; // blochează
    }
}