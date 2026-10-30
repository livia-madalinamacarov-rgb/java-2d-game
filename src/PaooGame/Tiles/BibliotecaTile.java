package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

public class BibliotecaTile extends Tile
{

    public BibliotecaTile(int id) {
        super(Assets.biblioteca, id);
    }

    @Override
    public boolean IsSolid() {
        return true; // nu poți trece prin ea
    }
}