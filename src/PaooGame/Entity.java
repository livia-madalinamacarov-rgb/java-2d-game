package PaooGame;

import java.awt.Rectangle;


/*
 * Clasa abstractă Entity este baza pentru obiectele importante din joc.
 * Ea păstrează poziția, dimensiunile și definește metodele comune pentru actualizare, desenare și coliziune.
 */
public abstract class Entity {

    protected int x, y;
    protected int width, height;

    /*
     * Constructorul inițializează poziția și dimensiunile entității.
     * Este apelat de clasele copil pentru a seta datele comune.
     */
    public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    /*
     * Metodă abstractă pentru actualizarea logicii entității.
     * Fiecare clasă copil trebuie să definească propriul comportament.
     */
    public abstract void update();

    /*
     * Metodă abstractă pentru desenarea entității.
     * Clasele copil stabilesc cum se afișează obiectul pe ecran, ținând cont de cameră și zoom.
     */
    public abstract void draw(java.awt.Graphics g, int camX, int camY, double zoom);

    /*
     * Returnează dreptunghiul de coliziune al entității.
     * Acesta este calculat pe baza poziției și dimensiunilor obiectului.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }


    /*
     * Verifică dacă entitatea curentă se intersectează cu o altă entitate.
     * Folosește dreptunghiurile de coliziune și tratează cazul în care entitatea primită este null.
     */
    public boolean intersects(Entity other) {
        if (other == null) {
            return false;
        }

        return this.getBounds().intersects(other.getBounds());
    }

    /* Returnează coordonata X a entității. */
    public int getX() {
        return x;
    }

    /* Returnează coordonata Y a entității. */
    public int getY() {
        return y;
    }

    /* Returnează lățimea entității. */
    public int getWidth() {
        return width;
    }

    /* Returnează înălțimea entității. */
    public int getHeight() {
        return height;
    }
}
