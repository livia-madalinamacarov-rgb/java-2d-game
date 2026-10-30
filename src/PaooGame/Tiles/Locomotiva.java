package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

public class Locomotiva extends Tile {

    public Locomotiva(int id) {
        super(Assets.locomotiva, id);
    }
    @Override
    public boolean IsSolid() {
        return true;
    }
}