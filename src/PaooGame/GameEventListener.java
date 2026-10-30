package PaooGame;

/**
 * Interfață pentru clasele care vor să asculte evenimente din joc.
 */
/*
 * Interfață folosită pentru clasele care ascultă evenimente din joc. Permite notificarea
 * obiectelor atunci când apare un eveniment important, cum ar fi damage sau actualizare
 * de scor.
 */
public interface GameEventListener {

    /**
     * Metodă apelată când se produce un eveniment.
     *
     * @param type tipul evenimentului
     * @param value valoarea asociată evenimentului
     */
    /*
     * Primește și tratează evenimentele trimise de EventManager. Actualizează scorul sau
     * aplică damage player-ului, în funcție de tipul evenimentului.
     */
    void onEvent(GameEventType type, int value);
}
