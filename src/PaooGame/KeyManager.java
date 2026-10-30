package PaooGame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Clasa care se ocupă de input-ul de la tastatură
/*
 * Clasa care gestionează input-ul de la tastatură. Reține ce taste sunt apăsate și oferă
 * jocului informații despre mișcare, interacțiune, Enter și Escape.
 */
public class KeyManager implements KeyListener {

    /*Variabile booleene care rețin starea tastelor
     * true = tasta este apăsată
     * false = tasta nu este apăsată
     */
    public boolean up, down, left, right;
    public boolean enterPressed;
    private boolean enterWasPressed = false;
    public boolean escPressed;
    private boolean escWasPressed = false;
    public boolean interactPressed = false;

    @Override
    /*
     * Este apelată automat când o tastă este apăsată. Activează variabilele de mișcare sau
     * acțiune corespunzătoare tastei apăsate.
     */
    public void keyPressed(KeyEvent e) {
        // Metoda este apelată automat când se apasă o tastă

        // Dacă se apasă W sau săgeata sus → activăm mișcarea în sus
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP)
            up = true;

        // Dacă se apasă S sau săgeata jos → activăm mișcarea în jos
        if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN)
            down = true;

        // Dacă se apasă A sau săgeata stânga → activăm mișcarea la stânga
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT)
            left = true;

        // Dacă se apasă D sau săgeata dreapta → activăm mișcarea la dreapta
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)
            right = true;

        // Dacă se apasă ENTER → schimbăm starea de ascundere (ascuns / vizibil)
        if (e.getKeyCode() == KeyEvent.VK_ENTER && !enterWasPressed) {
            enterPressed = true;
            enterWasPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !escWasPressed) {
            escPressed = true;
            escWasPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            interactPressed = true;
        }
    }

    @Override
    /*
     * Este apelată automat când o tastă este eliberată. Dezactivează mișcarea sau resetează
     * stările speciale pentru Enter, Escape și interacțiune.
     */
    public void keyReleased(KeyEvent e) {
        // Metoda este apelată când tasta este eliberată

        // Oprirea mișcării în sus
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP)
            up = false;

        // Oprirea mișcării în jos
        if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN)
            down = false;

        // Oprirea mișcării la stânga
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT)
            left = false;

        // Oprirea mișcării la dreapta
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT)
            right = false;

        // Resetăm ENTER când tasta este eliberată
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            enterPressed = false;
            enterWasPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            escPressed = false;
            escWasPressed = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_E) {
            interactPressed = false;
        }
    }

    @Override
    /*
     * Metodă obligatorie din KeyListener pentru caractere tastate. În acest joc nu este
     * folosită pentru logica principală de input.
     */
    public void keyTyped(KeyEvent e) {
        // Metodă obligatorie din interfața KeyListener
        // Se folosește pentru input de text (nu este necesară în joc)
    }
}
