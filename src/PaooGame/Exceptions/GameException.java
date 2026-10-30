package PaooGame.Exceptions;

/*
 * Clasa de bază pentru excepțiile personalizate ale jocului.
 *
 * Această clasă este folosită ca părinte pentru toate erorile importante
 * care pot apărea în proiect: probleme la încărcarea hărților, imaginilor,
 * sunetelor sau bazei de date.
 *
 * Avantajul folosirii unei clase de bază este că toate excepțiile jocului
 * pot fi tratate împreună, dar fiecare tip de problemă poate avea și propria
 * clasă mai specifică.
 */
public class GameException extends RuntimeException {

    /*
     * Creează o excepție generală pentru joc folosind doar un mesaj.
     * Mesajul explică pe scurt ce problemă a apărut.
     */
    public GameException(String message) {
        super(message);
    }

    /*
     * Creează o excepție generală pentru joc folosind un mesaj și cauza reală.
     * Parametrul cause păstrează eroarea inițială, astfel încât problema să fie
     * mai ușor de urmărit la depanare.
     */
    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}
