package PaooGame.Exceptions;

/*
 * Excepție folosită pentru probleme apărute la baza de date a jocului.
 *
 * Se poate folosi atunci când conexiunea la SQLite eșuează, când tabela nu poate
 * fi creată sau când salvarea/încărcarea progresului produce o eroare SQL.
 *
 * Această clasă este potrivită pentru DatabaseManager, deoarece acolo se fac
 * operațiile de creare a tabelelor, salvare, încărcare și afișare leaderboard.
 */
public class DatabaseException extends GameException {

    /*
     * Creează o excepție pentru baza de date folosind doar un mesaj explicativ.
     * Mesajul ar trebui să spună ce operație a eșuat.
     */
    public DatabaseException(String message) {
        super(message);
    }

    /*
     * Creează o excepție pentru baza de date folosind un mesaj și cauza reală.
     * Cauza este de obicei un SQLException sau o problemă la încărcarea driverului.
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
