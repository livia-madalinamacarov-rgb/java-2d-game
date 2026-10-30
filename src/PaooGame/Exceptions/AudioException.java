package PaooGame.Exceptions;

/*
 * Excepție folosită pentru probleme legate de sunetele și muzica jocului.
 *
 * Se poate folosi atunci când un fișier audio nu este găsit, nu este într-un
 * format suportat sau nu poate fi redat din cauza unei probleme cu sistemul audio.
 *
 * Această clasă este potrivită pentru AudioPlayer, unde se încarcă muzica de
 * fundal și efectele sonore.
 */
public class AudioException extends GameException {

    /*
     * Creează o excepție audio folosind doar un mesaj explicativ.
     * Mesajul ar trebui să indice ce fișier audio a produs problema.
     */
    public AudioException(String message) {
        super(message);
    }

    /*
     * Creează o excepție audio folosind un mesaj și cauza inițială.
     * Cauza poate fi o eroare precum UnsupportedAudioFileException,
     * IOException sau LineUnavailableException.
     */
    public AudioException(String message, Throwable cause) {
        super(message, cause);
    }
}
