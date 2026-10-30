package PaooGame.Exceptions;

/*
 * Excepție folosită atunci când jocul ajunge într-o stare invalidă sau neașteptată.
 *
 * Se poate folosi dacă GameState are o valoare nepotrivită pentru o anumită acțiune,
 * dacă se încearcă pornirea unui nivel care nu există sau dacă logica jocului ajunge
 * într-un caz care nu ar trebui să se întâmple.
 *
 * Această clasă este utilă pentru validarea fluxului principal din Game.
 */
public class InvalidGameStateException extends GameException {

    /*
     * Creează o excepție pentru stare invalidă folosind doar un mesaj explicativ.
     */
    public InvalidGameStateException(String message) {
        super(message);
    }

    /*
     * Creează o excepție pentru stare invalidă folosind un mesaj și cauza reală.
     */
    public InvalidGameStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
