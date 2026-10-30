package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

public class UsaTile extends Tile {

    public UsaTile(int id) {
        super(Assets.usa, id);
    }

    @Override
    public boolean IsSolid() {
        return false; // sau true, dacă vrei să blocheze
    }

}