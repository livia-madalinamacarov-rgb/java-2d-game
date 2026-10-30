package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

public class FantanaTile extends Tile {

    public FantanaTile(int id) {
        super(Assets.fantana, id);
    }

    @Override
    public boolean IsSolid() {
        return true;
    }
}