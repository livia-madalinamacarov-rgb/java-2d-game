package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

public class CopacTile extends Tile {
    public CopacTile(int id) {
        super(Assets.copac, id);
    }

    @Override
    public boolean IsSolid() {
        return true;
    }
}