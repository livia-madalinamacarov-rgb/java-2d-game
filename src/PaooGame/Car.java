package PaooGame;

import java.awt.*;
import java.awt.image.BufferedImage;

/*
 * Clasa Car reprezintă o mașină din joc.
 * Mașina se deplasează automat pe orizontală, se redesenează când iese din hartă și are o zonă de coliziune ajustată.
 */
public class Car extends Entity {
    private int speed;
    private BufferedImage image;
    private int mapWidth;

    /*
     * Constructorul creează o mașină cu poziție, viteză, imagine și limita lățimii hărții.
     * Dimensiunea mașinii este setată la 50x50 pixeli.
     */
    public Car(int x, int y, int speed, BufferedImage image, int mapWidth) {
        super(x, y, 50, 50);
        this.speed = speed;
        this.image = image;
        this.mapWidth = mapWidth;
    }

    @Override
    /*
     * Actualizează poziția mașinii în fiecare frame.
     * Mașina se mișcă în funcție de viteză, iar când iese din hartă reapare din partea opusă.
     */
    public void update() {
        x += speed;

        if (speed > 0 && x > mapWidth) {
            x = -width;
        }

        if (speed < 0 && x < -width) {
            x = mapWidth;
        }
    }

    @Override
    /*
     * Desenează mașina pe ecran.
     * Coordonatele și dimensiunile sunt adaptate în funcție de cameră și zoom.
     */
    public void draw(Graphics g, int camX, int camY, double zoom) {
        int drawX = (int)((x - camX) * zoom);
        int drawY = (int)((y - camY) * zoom);
        int drawW = (int)(width * zoom);
        int drawH = (int)(height * zoom);

        g.drawImage(image, drawX, drawY, drawW, drawH, null);
    }

    @Override
    /*
     * Returnează dreptunghiul folosit pentru coliziunea mașinii.
     * Zona este micșorată față de imagine pentru ca interacțiunile să fie mai corecte vizual.
     */
    public Rectangle getBounds() {
        int marginX = width / 4;
        int marginY = height / 4;

        return new Rectangle(
                x + marginX,
                y + marginY,
                width - 2 * marginX,
                height - 2 * marginY
        );
    }
}