package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

public class CaramidaTile extends Tile
{
    public CaramidaTile(int id)
    {
        super(Assets.caramida, id);
    }

    @Override
    public boolean IsSolid()
    {

        return true;
    }
}