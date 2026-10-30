package PaooGame.Exceptions;

/*
 * Excepție folosită pentru probleme la încărcarea imaginilor și resurselor grafice.
 *
 * Se poate folosi atunci când o imagine lipsește din folderul resources, când
 * path-ul este greșit sau când fișierul nu poate fi citit ca BufferedImage.
 *
 * Această clasă este utilă în ImageLoader, Assets și SpriteSheet, deoarece acolo
 * se încarcă imaginile, sprite sheet-urile și frame-urile pentru animații.
 */
public class AssetLoadException extends GameException {

    /*
     * Creează o excepție pentru resurse grafice folosind doar un mesaj explicativ.
     * Mesajul poate conține calea imaginii care nu s-a încărcat.
     */
    public AssetLoadException(String message) {
        super(message);
    }

    /*
     * Creează o excepție pentru resurse grafice folosind un mesaj și cauza reală.
     * Cauza poate fi o eroare produsă de ImageIO sau de accesarea unei resurse inexistente.
     */
    public AssetLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
