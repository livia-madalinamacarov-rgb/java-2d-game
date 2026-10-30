package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

public class PlantaTile extends Tile {

    public PlantaTile(int id) {
        super(Assets.planta, id);
    }

    @Override
    public boolean IsSolid() {
        return true;
    }
}