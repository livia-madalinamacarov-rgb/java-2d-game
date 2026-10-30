package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

public class ButurugaTile extends Tile {

    public ButurugaTile(int id) {
        super(Assets.buturuga, id);
    }

    @Override
    public boolean IsSolid() {
        return true;
    }
}