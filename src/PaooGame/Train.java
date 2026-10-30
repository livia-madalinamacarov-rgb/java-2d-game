package PaooGame;

import PaooGame.Graphics.Assets;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/*
 * Clasa care reprezintă trenul din joc. Construiește trenul din locomotivă și vagoane, îl
 * deplasează pe hartă și îl redesenează în funcție de cameră și zoom.
 */
public class Train extends Entity {

    private int speed;          // viteza trenului
    private int mapWidth;       // lățimea hărții, folosită pentru reapariție
    private BufferedImage[] parts; // locomotiva + vagoanele

    /*
     * Constructorul clasei Train. Setează poziția, viteza, lățimea hărții și creează lista de
     * imagini care formează locomotiva și vagoanele.
     */
    public Train(int x, int y, int speed, int mapWidth) {
        // 11 bucăți: 1 locomotivă + 10 vagoane
        super(x, y, 11 * 50, 50);

        this.speed = speed;
        this.mapWidth = mapWidth;

        // Aici construim trenul dintr-o locomotivă și 10 vagoane
        parts = new BufferedImage[]{
                Assets.locomotiva,
                Assets.vagonMov,
                Assets.vagonAlbastru,
                Assets.vagonRosu,
                Assets.vagonPortocaliu,
                Assets.vagonMov,
                Assets.vagonAlbastru,
                Assets.vagonRosu,
                Assets.vagonPortocaliu,
                Assets.vagonMov,
                Assets.vagonAlbastru
        };
    }

    @Override
    /*
     * Actualizează starea obiectului pentru cadrul curent. În cazul trenului, îl deplasează
     * pe axa X și îl repoziționează când iese din hartă.
     */
    public void update() {
        // Mutăm trenul pe axa X
        x += speed;

        // Dacă trenul merge spre dreapta și iese din hartă,
        // reapare din partea stângă
        if (speed > 0 && x > mapWidth) {
            x = -width;
        }

        // Dacă trenul merge spre stânga și iese din hartă,
        // reapare din partea dreaptă
        if (speed < 0 && x < -width) {
            x = mapWidth;
        }
    }

    @Override
    /*
     * Desenează elementele vizuale ale clasei. În funcție de clasă, afișează efecte de
     * interfață sau componente de joc pe ecran.
     */
    public void draw(Graphics g, int camX, int camY, double zoom) {
        // Desenăm fiecare bucată a trenului una după alta
        for (int i = 0; i < parts.length; i++) {

            // Fiecare vagon este la 50 px după cel anterior
            int drawX = (int)(((x + i * 50) - camX) * zoom);
            int drawY = (int)((y - camY) * zoom);

            int drawSize = (int)(50 * zoom);

            g.drawImage(parts[i], drawX, drawY, drawSize, drawSize, null);
        }
    }
}
