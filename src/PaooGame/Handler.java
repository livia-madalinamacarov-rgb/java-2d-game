package PaooGame;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/*
 * Clasa care gestionează entitățile active din nivel, cum ar fi player-ul, inamicii și
 * peștii. Centralizează actualizarea, desenarea și verificarea logicii speciale pentru
 * inamici.
 */
public class Handler {

    private Cat player;
    private List<Entity> enemies;
    private List<Fish> fishList;

    private EventManager eventManager;

    private final int KNOCKBACK_FORCE = 40;

    // Reguli nivel 2
    private final int DOG_VISIBILITY_RADIUS = 210;   // câinele o vede de la 220 px
    private final int DOG_ESCAPE_RADIUS = 250;       // scapă doar dacă se îndepărtează mult
    private final long VISION_WARNING_TIME = 5000;   // 5 secunde

    private boolean playerInEnemyVision = false;
    private long visionStartTime = 0;
    private Entity enemyThatSeesPlayer = null;

    /*
     * Metoda Handler realizează o parte din logica acestei clase și este apelată în funcție
     * de starea jocului.
     */
    public Handler(EventManager eventManager) {
        enemies = new ArrayList<>();
        fishList = new ArrayList<>();
        this.eventManager = eventManager;
    }

    /*
     * Curăță datele nivelului curent. Elimină inamicii și peștii, resetează player-ul și
     * anulează starea de detectare a câinilor.
     */
    public void clearLevel() {
        enemies.clear();
        fishList.clear();
        player = null;
        resetEnemyVision();
    }

    /*
     * Returnează obiectul player curent. Este folosit de alte clase pentru acces la poziția
     * și starea pisicii.
     */
    public Cat getPlayer() {
        return player;
    }

    /*
     * Setează player-ul curent gestionat de Handler. Permite entităților și verificărilor
     * interne să folosească aceeași referință la pisică.
     */
    public void setPlayer(Cat player) {
        this.player = player;
    }

    /*
     * Adaugă un inamic în lista de entități ostile. Verifică să nu fie null înainte de a-l
     * salva.
     */
    public void addEnemy(Entity enemy) {
        if (enemy != null) {
            enemies.add(enemy);
        }
    }

    /*
     * Adaugă un pește în lista obiectelor colectabile. Verifică să nu fie null înainte de a-l
     * salva.
     */
    public void addFish(Fish fish) {
        if (fish != null) {
            fishList.add(fish);
        }
    }

    /*
     * Returnează lista inamicilor gestionați de Handler. Lista este folosită pentru update,
     * desenare și coliziuni.
     */
    public List<Entity> getEnemies() {
        return enemies;
    }

    /*
     * Returnează lista peștilor din nivel. Este folosită pentru desenare, colectare și
     * salvarea progresului.
     */
    public List<Fish> getFishList() {
        return fishList;
    }

    /*
     * Actualizează toți inamicii existenți. Apelează metoda update pentru fiecare entitate
     * din lista de inamici.
     */
    public void updateEnemies() {
        for (Entity enemy : enemies) {
            enemy.update();
        }
    }

    /*
     * Regula nivel 2:
     * Dacă Mitzi intră în raza vizuală, are 5 secunde să se îndepărteze.
     * Dacă rămâne în pericol 5 secunde, pierde o viață.
     * Dacă se ascunde în tufiș, câinii nu o văd.
     */
    /*
     * Verifică regula de vizibilitate a câinilor din nivelul 2. Dacă player-ul este văzut
     * prea mult timp și nu scapă sau nu se ascunde, trimite eveniment de damage.
     */
    public void checkEnemyVision() {
        if (player == null) {
            resetEnemyVision();
            return;
        }

        // Dacă Mitzi se ascunde în tufiș, câinele nu o mai vede
        if (player.isHidden()) {
            if (playerInEnemyVision) {
                System.out.println("Mitzi s-a ascuns. Cainele nu o mai vede.");
            }

            resetEnemyVision();
            return;
        }

        long now = System.currentTimeMillis();

        // Dacă încă nu este în pericol, verificăm dacă un câine o vede acum
        if (!playerInEnemyVision) {
            Entity dog = getEnemyThatSeesPlayer();

            if (dog != null) {
                playerInEnemyVision = true;
                visionStartTime = now;
                enemyThatSeesPlayer = dog;

                System.out.println("Cainele a vazut-o pe Mitzi! Ai 5 secunde!");
            }

            return;
        }

        // Dacă este deja în pericol, verificăm dacă s-a îndepărtat suficient
        if (enemyThatSeesPlayer != null && isPlayerFarEnoughFromEnemy(enemyThatSeesPlayer)) {
            System.out.println("Mitzi a scapat!");
            resetEnemyVision();
            return;
        }

        // Dacă este deja în pericol, verificăm dacă au trecut cele 5 secunde
        long timeInDanger = now - visionStartTime;

        if (timeInDanger >= VISION_WARNING_TIME) {
            System.out.println("Mitzi a stat 5 secunde in raza vizuala. Pierde o viata!");

            if (eventManager != null) {
                eventManager.notify(GameEventType.DAMAGE, 1);
            } else {
                System.out.println("EROARE: eventManager este null!");
            }

            if (enemyThatSeesPlayer != null) {
                player.knockbackFrom(enemyThatSeesPlayer, KNOCKBACK_FORCE);
            }

            // După damage, resetăm ca să înceapă alt interval de 5 secunde dacă rămâne aproape
            resetEnemyVision();
        }
    }

    /*
     * Caută primul inamic care îl vede pe player. Returnează inamicul respectiv sau null dacă
     * player-ul este în siguranță.
     */
    private Entity getEnemyThatSeesPlayer() {
        for (Entity enemy : enemies) {
            if (isPlayerInsideEnemyVision(enemy)) {
                return enemy;
            }
        }

        return null;
    }

    /*
     * Calculează dacă player-ul se află în raza și direcția de vedere a unui inamic. Ține
     * cont de distanță și de direcția câinelui.
     */
    private boolean isPlayerInsideEnemyVision(Entity enemy) {
        if (enemy == null || player == null) {
            return false;
        }

        int playerCenterX = player.getX() + player.getWidth() / 2;
        int playerCenterY = player.getY() + player.getHeight() / 2;

        int enemyCenterX = enemy.getX() + enemy.getWidth() / 2;
        int enemyCenterY = enemy.getY() + enemy.getHeight() / 2;

        int dx = playerCenterX - enemyCenterX;
        int dy = playerCenterY - enemyCenterY;

        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > DOG_VISIBILITY_RADIUS) {
            return false;
        }

        if (enemy instanceof Dog) {
            Dog dog = (Dog) enemy;

            int direction = dog.getDirection();

            // câinele se uită spre dreapta
            if (direction == 1 && playerCenterX < enemyCenterX - 20) {
                return false;
            }

            // câinele se uită spre stânga
            if (direction == -1 && playerCenterX > enemyCenterX + 20) {
                return false;
            }
        }

        return true;

    }

    /*
     * Verifică dacă player-ul s-a îndepărtat suficient de inamicul care îl urmărea. Este
     * folosit pentru a opri starea de pericol.
     */
    private boolean isPlayerFarEnoughFromEnemy(Entity enemy) {
        if (enemy == null || player == null) {
            return true;
        }

        int playerCenterX = player.getX() + player.getWidth() / 2;
        int playerCenterY = player.getY() + player.getHeight() / 2;

        int enemyCenterX = enemy.getX() + enemy.getWidth() / 2;
        int enemyCenterY = enemy.getY() + enemy.getHeight() / 2;

        int dx = playerCenterX - enemyCenterX;
        int dy = playerCenterY - enemyCenterY;

        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance >= DOG_ESCAPE_RADIUS;
    }

    /*
     * Resetează toate datele legate de detectarea player-ului de către inamici. Starea de
     * pericol începe din nou de la zero.
     */
    private void resetEnemyVision() {
        playerInEnemyVision = false;
        visionStartTime = 0;
        enemyThatSeesPlayer = null;
    }

    /*
     * Returnează dacă player-ul este momentan în raza vizuală a unui inamic. Poate fi folosit
     * pentru afișarea avertismentelor.
     */
    public boolean isPlayerInEnemyVision() {
        return playerInEnemyVision;
    }

    /*
     * Calculează câte secunde mai are player-ul până primește damage dacă rămâne în raza
     * vizuală a câinelui.
     */
    public int getVisionSecondsRemaining() {
        if (!playerInEnemyVision) {
            return 5;
        }

        long elapsed = System.currentTimeMillis() - visionStartTime;
        long remaining = VISION_WARNING_TIME - elapsed;

        if (remaining < 0) {
            remaining = 0;
        }

        return (int) Math.ceil(remaining / 1000.0);
    }

    /*
     * Desenează toți inamicii gestionați de Handler. Aplică poziția camerei și zoom-ul pentru
     * fiecare entitate.
     */
    public void drawEnemies(Graphics g, int camX, int camY, double zoom) {
        for (Entity enemy : enemies) {
            enemy.draw(g, camX, camY, zoom);
        }
    }

    /*
     * Desenează toți peștii gestionați de Handler. Peștii colectați nu mai sunt afișați de
     * clasa Fish.
     */
    public void drawFish(Graphics g, int camX, int camY, double zoom) {
        for (Fish fish : fishList) {
            fish.draw(g, camX, camY, zoom);
        }
    }
}
