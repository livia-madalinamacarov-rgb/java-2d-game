package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

public class SemaforTile extends Tile {

    public SemaforTile(int id) {
        super(Assets.semafor, id);
    }
    @Override
    public boolean IsSolid() {
        return true; // Semaforul este un obstacol
    }
}