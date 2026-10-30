package PaooGame;

import java.util.ArrayList;
import java.util.List;


/*
 * Clasa EventManager implementează un sistem simplu de evenimente de tip Observer.
 * Ea păstrează o listă de ascultători și îi notifică atunci când apare un eveniment în joc.
 */
public class EventManager {

    private List<GameEventListener> listeners;

    /*
     * Constructorul inițializează lista de ascultători.
     * La început, lista este goală și poate primi listeneri prin addListener.
     */
    public EventManager() {
        listeners = new ArrayList<>();
    }


    /*
     * Adaugă un ascultător în lista de notificare.
     * Verifică să nu fie null înainte de adăugare.
     */
    public void addListener(GameEventListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }


    /*
     * Elimină un ascultător din listă.
     * După eliminare, acel listener nu va mai primi evenimente.
     */
    public void removeListener(GameEventListener listener) {
        listeners.remove(listener);
    }


    /*
     * Notifică toți ascultătorii despre un eveniment.
     * Trimite tipul evenimentului și o valoare numerică asociată fiecărui listener.
     */
    public void notify(GameEventType type, int value) {
        for (GameEventListener listener : listeners) {
            listener.onEvent(type, value);
        }
    }
}