package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

public class TopoganTile extends Tile {

    public TopoganTile(int id) {
        super(Assets.topogan, id);
    }

    @Override
    public boolean IsSolid() {
        return true;
    }
}