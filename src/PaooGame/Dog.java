package PaooGame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import PaooGame.Graphics.Assets;

/*
 * Clasa Dog reprezintă un inamic de tip câine.
 * Câinele patrulează pe o distanță fixă, poate bloca o zonă și poate fi atras către mingea aruncată de jucător.
 */
public class Dog extends Entity {

    private int speed = 2;
    private int direction = 1;
    private int startX;
    private int patrolDistance = 150;

    private BufferedImage[] frames;
    private int currentFrame;
    private int frameDelay;
    private int speedAnimation = 8;

    private boolean blockingDog = false;
    private boolean attractedToBall = false;
    private int ballX;
    private int ballY;

    private Handler handler;

    /*
     * Constructorul inițializează câinele la poziția primită.
     * Setează punctul de start al patrulei și animația inițială spre dreapta.
     */
    public Dog(Handler handler, int x, int y) {
        super(x, y, 50, 50);

        this.handler = handler;
        this.startX = x;

        frames = Assets.dogRight;
        currentFrame = 0;
        frameDelay = 0;
    }

    @Override
    /*
     * Actualizează comportamentul câinelui în fiecare frame.
     * Dacă este câine blocant și a fost atras de minge, se deplasează către aceasta; altfel patrulează normal.
     */
    public void update() {
        if (blockingDog) {
            if (attractedToBall) {
                if (x < ballX) x += 2;
                if (x > ballX) x -= 2;
                if (y < ballY) y += 2;
                if (y > ballY) y -= 2;
            }

            animate();
            return;
        }

        patrol();
        animate();
    }

    /*
     * Realizează mișcarea de patrulare a câinelui.
     * Câinele se deplasează stânga-dreapta între limitele calculate față de poziția inițială.
     */
    private void patrol() {
        x += speed * direction;

        if (x <= startX - patrolDistance) {
            direction = 1;
            currentFrame = 0;
        } else if (x >= startX + patrolDistance) {
            direction = -1;
            currentFrame = 0;
        }
    }

    /*
     * Actualizează animația câinelui.
     * Alege setul de imagini în funcție de direcție și schimbă frame-ul după un anumit delay.
     */
    private void animate() {
        if (direction == 1) {
            frames = Assets.dogRight;
        } else {
            frames = Assets.dogLeft;
        }

        frameDelay++;

        if (frameDelay >= speedAnimation) {
            currentFrame++;

            if (currentFrame >= frames.length) {
                currentFrame = 0;
            }

            frameDelay = 0;
        }
    }

    @Override
    /*
     * Desenează câinele pe ecran.
     * Poziția și dimensiunea sunt ajustate în funcție de cameră și zoom.
     */
    public void draw(Graphics g, int camX, int camY, double zoom) {
        int drawX = (int) ((x - camX) * zoom);
        int drawY = (int) ((y - camY) * zoom);
        int drawWidth = (int) (width * zoom);
        int drawHeight = (int) (height * zoom);

        if (frames != null) {
            g.drawImage(frames[currentFrame], drawX, drawY, drawWidth, drawHeight, null);
        }
    }

    /*
     * Setează dacă acest câine are rol de obstacol/blocant.
     * Când devine blocant, direcția și animația sunt setate spre stânga.
     */
    public void setBlockingDog(boolean blockingDog) {
        this.blockingDog = blockingDog;

        if (blockingDog) {
            direction = -1;
            frames = Assets.dogLeft;
        }
    }
    /*
     * Face câinele să fie atras către poziția mingii.
     * Coordonatele mingii sunt memorate, iar câinele începe să se deplaseze spre ele.
     */
    public void goToBall(int ballX, int ballY) {
        this.ballX = ballX;
        this.ballY = ballY;
        attractedToBall = true;
    }
    /*
     * Setează direcția de deplasare a câinelui.
     * Actualizează și animația corespunzătoare direcției alese.
     */
    public void setDirection(int direction) {
        this.direction = direction;

        if (direction == 1) {
            frames = Assets.dogRight;
        } else {
            frames = Assets.dogLeft;
        }
    }

    /* Setează viteza de deplasare a câinelui. */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /* Returnează direcția curentă de deplasare a câinelui. */
    public int getDirection() {
        return direction;
    }

}