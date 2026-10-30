package PaooGame;

/*
 * Clasa simplă de transfer de date pentru progresul salvat. Păstrează nivelul, scorul,
 * viețile, peștii colectați și checkpoint-ul jucătorului.
 */
public class SaveData {
    public int level;
    public int score;
    public int lives;
    public String collectedFish;
    public int checkpointX;
    public int checkpointY;

    /*
     * Constructorul clasei SaveData. Primește valorile salvate și le pune în câmpurile
     * publice pentru a fi folosite la încărcarea progresului.
     */
    public SaveData(int level, int score, int lives, String collectedFish, int checkpointX, int checkpointY) {
        this.level = level;
        this.score = score;
        this.lives = lives;
        this.collectedFish = collectedFish;
        this.checkpointX = checkpointX;
        this.checkpointY = checkpointY;
    }
}
