package PaooGame;

import PaooGame.Graphics.Assets;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;

/*
 * Clasa Cat reprezintă personajul principal al jocului, pisica Mitzi.
 * Ea gestionează mișcarea, animațiile, viețile, ascunderea în tufișuri și reacția la coliziuni cu inamici.
 */
public class Cat extends Entity
{

    private BufferedImage[] frames;
    private int currentFrame;

    private int frameDelay;
    private int speed;

    private int vieti = 3;
    private boolean ascunsa = false;

    /*
     * Constructorul inițializează pisica la o poziție dată și cu o anumită dimensiune.
     * Setează animația inițială, frame-ul curent, viteza și numărul de vieți.
     */
    public Cat(int x, int y, int size) {

        super(x, y, size, size);
        frames = Assets.catDown;
        currentFrame = 0;
        frameDelay = 0;
        speed = 2;
    }
    @Override
    /*
     * Metodă de actualizare pentru pisică.
     * În această implementare, logica de mișcare este realizată direct în metodele moveLeft, moveRight, moveUp și moveDown.
     */
    public void update() {

    }

    /*
     * Mută pisica spre stânga.
     * Schimbă animația pe direcția stânga și avansează frame-ul animației.
     */
    public void moveLeft() {
        x = x - speed;
        frames = Assets.catLeft;
        animate();
    }

    /*
     * Mută pisica spre dreapta.
     * Schimbă animația pe direcția dreapta și actualizează animația.
     */
    public void moveRight() {
        x = x + speed;
        frames = Assets.catRight;
        animate();
    }

    /*
     * Mută pisica în sus.
     * Folosește animația corespunzătoare mersului în sus.
     */
    public void moveUp() {

        y = y - speed;
        frames = Assets.catUp;
        animate();
    }

    /*
     * Mută pisica în jos.
     * Folosește animația corespunzătoare mersului în jos.
     */
    public void moveDown() {
        y = y + speed;
        frames = Assets.catDown;
        animate();
    }


    /*
     * Controlează schimbarea frame-urilor pentru animația pisicii.
     * După un anumit delay, trece la următorul cadru și revine la început când ajunge la final.
     */
    private void animate() {

        frameDelay = frameDelay + 1;


        if (frameDelay >= 8) {

            currentFrame = currentFrame + 1;


            if (currentFrame >= frames.length) {
                currentFrame = 0;
            }

            frameDelay = 0;
        }
    }


    @Override
    /*
     * Desenează pisica pe ecran în funcție de cameră și zoom.
     * Dacă pisica este ascunsă, imaginea este desenată semi-transparent pentru a arăta starea de ascundere.
     */
    public void draw(Graphics g, int camX, int camY, double zoom) {

        int drawX = (int) ((x - camX) * zoom);
        int drawY = (int) ((y - camY) * zoom);
        int drawSize = (int) (width * zoom);

        Graphics2D g2d = (Graphics2D) g;


        if (ascunsa) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2d.drawImage(frames[currentFrame], drawX, drawY, drawSize, drawSize, null);


        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }


    /*
     * Împinge pisica în direcția opusă unui inamic.
     * Metoda compară diferența pe axele X și Y pentru a decide direcția principală a împingerii.
     */
    public void knockbackFrom(Entity enemy, int force) {
        if (enemy == null) {
            return;
        }

        int dx = this.x - enemy.getX();
        int dy = this.y - enemy.getY();

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx >= 0) {
                x += force;
            } else {
                x -= force;
            }
        } else {
            if (dy >= 0) {
                y += force;
            } else {
                y -= force;
            }
        }
    }



    /*
     * Returnează imaginea curentă a pisicii.
     * Poate fi folosită de alte componente, de exemplu pentru afișare sau logică legată de animație.
     */
    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }

    /*
     * Returnează dimensiunea pisicii.
     * În această clasă, dimensiunea este reprezentată prin lățime.
     */
    public int getSize()
    {
        return width;
    }


    /*
     * Returnează zona de coliziune a pisicii.
     * Dreptunghiul este folosit pentru verificarea interacțiunilor cu obiecte, inamici sau iteme.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }


    /*
     * Returnează dacă pisica este ascunsă.
     * Această stare poate influența detectarea ei de către inamici.
     */
    public boolean isHidden() {
        return ascunsa;
    }

    /*
     * Setează starea de ascundere a pisicii.
     * Când este ascunsă, pisica este desenată semi-transparent.
     */
    public void setHidden(boolean ascunsa) {
        this.ascunsa = ascunsa;
    }


    /*
     * Scade o viață din totalul pisicii.
     * Afișează în consolă câte vieți au rămas după ce pisica este prinsă.
     */
    public void pierdeViata() {
        vieti--;
        System.out.println("Mitzi a fost prinsă! Vieți rămase: " + vieti);
    }

    /* Returnează numărul curent de vieți ale pisicii. */
    public int getVieti() {
        return vieti;
    }

    /*
     * Setează numărul de vieți ale pisicii.
     * Este utilă la încărcarea progresului sau la resetarea jocului.
     */
    public void setVieti(int vieti) {
        this.vieti = vieti;
    }

    /* Setează viteza de deplasare a pisicii. */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /* Setează coordonata X a pisicii. */
    public void setX(int x) {
        this.x = x;
    }

    /* Setează coordonata Y a pisicii. */
    public void setY(int y) {
        this.y = y;
    }

}