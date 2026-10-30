package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

public class Copac2Tile extends Tile {
    public Copac2Tile(int id) {
        super(Assets.copac2, id);
    }

    @Override
    public boolean IsSolid() {
        return true;
    }
}