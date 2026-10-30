package PaooGame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

/*
 * Clasa Fish reprezintă un pește colectabil din joc.
 * Peștele are un id, o imagine, o stare de colectare și poate fi desenat doar cât timp nu a fost colectat.
 */
public class Fish extends Entity{
    private BufferedImage image;
    private boolean collected;
    private int id;

    /*
     * Constructorul creează un pește la poziția dată, cu dimensiunea și imaginea primite.
     * Setează id-ul peștelui și marchează inițial obiectul ca necolectat.
     */
    public Fish(int id,int x, int y, int size, BufferedImage image) {

        super(x,y,size,size);
        this.id=id;
        this.image = image;
        this.collected = false;
    }

    @Override
    /*
     * Metodă de actualizare pentru pește.
     * În forma actuală nu conține logică, deoarece peștele este un obiect static colectabil.
     */
    public void update()
    {

    }

    /*
     * Desenează peștele pe ecran dacă nu a fost colectat.
     * Poziția și dimensiunea sunt ajustate în funcție de cameră și zoom.
     */
    public void draw(Graphics g, int camX, int camY, double zoom) {


        if (!collected) {



            int drawX = (int)((x - camX) * zoom);
            int drawY = (int)((y - camY) * zoom);


            int drawSize = (int)(width * zoom);


            g.drawImage(image, drawX, drawY, drawSize, drawSize, null);
        }
    }

    /*
     * Marchează peștele ca fiind colectat.
     * După colectare, acesta nu mai este desenat pe ecran.
     */
    public void collect() {
        collected = true;
    }

    /* Returnează dacă peștele a fost colectat. */
    public boolean isCollected() {
        return collected;
    }


    /* Returnează dimensiunea peștelui. */
    public int getSize() {
        return width;
    }

    /* Returnează imaginea asociată peștelui. */
    public BufferedImage getImage() {
        return image;
    }

    /* Returnează id-ul peștelui, util pentru salvarea sau verificarea colectării. */
    public int getId() {
        return id;
    }

}