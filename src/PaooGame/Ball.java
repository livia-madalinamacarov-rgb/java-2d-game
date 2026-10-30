package PaooGame;

import java.awt.*;
import java.awt.image.BufferedImage;

/*
 * Clasa Ball reprezintă mingea folosită în joc.
 * Ea poate fi aruncată către o poziție țintă, se deplasează treptat până acolo și poate fi desenată pe ecran în funcție de cameră și zoom.
 */
public class Ball extends Entity {
    private BufferedImage image;

    private boolean thrown = false;
    private boolean arrived = false;

    private int targetX;
    private int targetY;
    private int speed = 5;

    /*
     * Constructorul inițializează mingea cu poziția, dimensiunea și imaginea ei.
     * Apelează constructorul clasei Entity pentru a seta coordonatele și dimensiunile obiectului.
     */
    public Ball(int x, int y, int size, BufferedImage image) {
        super(x, y, size, size);
        this.image = image;
    }

    /*
     * Setează poziția țintă către care trebuie să se deplaseze mingea.
     * După apelarea metodei, mingea este marcată ca fiind aruncată.
     */
    public void throwTo(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
        thrown = true;
    }

    /*
     * Returnează dacă mingea a fost deja aruncată.
     * Această informație poate fi folosită de joc pentru a decide dacă mingea trebuie actualizată sau urmărită.
     */
    public boolean isThrown() {
        return thrown;
    }

    /*
     * Returnează dacă mingea a ajuns la poziția țintă.
     * Este utilă pentru a ști când se termină mișcarea mingii.
     */
    public boolean hasArrived() {
        return arrived;
    }

    @Override
    /*
     * Actualizează poziția mingii în fiecare frame.
     * Dacă mingea este aruncată și nu a ajuns încă la destinație, se apropie treptat de coordonatele țintă.
     */
    public void update() {
        if (!thrown || arrived) {
            return;
        }

        if (x < targetX) x += speed;
        if (x > targetX) x -= speed;
        if (y < targetY) y += speed;
        if (y > targetY) y -= speed;

        if (Math.abs(x - targetX) < speed && Math.abs(y - targetY) < speed) {
            x = targetX;
            y = targetY;
            arrived = true;
        }
    }

    @Override
    /*
     * Desenează mingea pe ecran.
     * Poziția este calculată în funcție de coordonatele camerei, iar dimensiunea este adaptată după nivelul de zoom.
     */
    public void draw(Graphics g, int camX, int camY, double zoom) {
        int drawX = (int)((x - camX) * zoom);
        int drawY = (int)((y - camY) * zoom);
        int drawSize = (int)(width * zoom);

        g.drawImage(image, drawX, drawY, drawSize, drawSize, null);
    }

    /*
     * Resetează starea mingii.
     * După apelare, mingea nu mai este considerată aruncată și poate fi folosită din nou.
     */
    public void reset() {
        thrown = false;
        arrived = false;
    }
}