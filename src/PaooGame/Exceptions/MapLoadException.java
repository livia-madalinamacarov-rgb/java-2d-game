package PaooGame.Exceptions;

/*
 * Excepție folosită pentru probleme apărute la încărcarea hărților jocului.
 *
 * Se poate folosi atunci când fișierul unei hărți nu este găsit, nu poate fi
 * citit corect sau conține valori invalide pentru tile-uri.
 *
 * Această excepție este utilă în clase precum GameMap sau MapParser, unde jocul
 * încearcă să transforme un fișier text într-o matrice de tile-uri.
 */
public class MapLoadException extends GameException {

    /*
     * Creează o excepție pentru hartă folosind doar un mesaj explicativ.
     * Exemplu de mesaj: "Nu s-a putut încărca harta: /maps/level1.txt".
     */
    public MapLoadException(String message) {
        super(message);
    }

    /*
     * Creează o excepție pentru hartă folosind un mesaj și cauza inițială.
     * Este utilă când eroarea vine dintr-un IOException, NumberFormatException
     * sau altă problemă produsă în timpul citirii fișierului.
     */
    public MapLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}