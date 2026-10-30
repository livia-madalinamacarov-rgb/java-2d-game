package PaooGame;

import PaooGame.Graphics.Assets;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/*
 * Clasa Door reprezintă ușa de final sau de trecere din joc.
 * Ea are o animație de deschidere și păstrează starea care indică dacă ușa este închisă, în curs de deschidere sau complet deschisă.
 */
public class Door extends Entity {
    private int currentFrame = 0;
    private boolean isOpening = false;
    private boolean isFullyOpen = false;
    private int animationDelay = 0;
    private static final int ANIMATION_SPEED = 10;


    /*
     * Constructorul creează ușa la poziția și dimensiunea primite.
     * Apelează constructorul clasei Entity pentru inițializarea coordonatelor și dimensiunilor.
     */
    public Door(int x, int y, int width, int height) {
        super(x,y,width,height);
    }


    @Override
    /*
     * Actualizează animația ușii.
     * Dacă deschiderea a fost declanșată, schimbă cadrele animației până când ușa ajunge complet deschisă.
     */
    public void update() {

        if (isOpening && !isFullyOpen) {
            animationDelay++;
            if (animationDelay >= ANIMATION_SPEED) {
                currentFrame++;

                if (currentFrame >= Assets.doorAnimation.length) {
                    currentFrame = Assets.doorAnimation.length - 1;
                    isFullyOpen = true;
                    isOpening = false;
                }
                animationDelay = 0;
            }
        }
    }



    /*
     * Desenează cadrul curent al ușii pe ecran.
     * Poziția și dimensiunea sunt calculate folosind camera și zoom-ul.
     */
    public void draw(Graphics g, int camX, int camY, double zoom) {

        BufferedImage frame = Assets.doorAnimation[currentFrame];


        int drawX = (int)((x - camX) * zoom);
        int drawY = (int)((y - camY) * zoom);
        int drawW = (int)(width * zoom);
        int drawH = (int)(height * zoom);


        g.drawImage(frame, drawX, drawY, drawW, drawH, null);
    }


    /*
     * Pornește animația de deschidere a ușii.
     * Metoda are efect doar dacă ușa nu este deja deschisă și nu se află deja în animație.
     */
    public void trigger()
    {

        if (!isOpening && !isFullyOpen)
        {
            isOpening = true;
        }
    }


    /*
     * Returnează dacă ușa este complet deschisă.
     * Această informație poate fi folosită pentru logica de final de nivel.
     */
    public boolean isFullyOpen() {
        return isFullyOpen;
    }
}