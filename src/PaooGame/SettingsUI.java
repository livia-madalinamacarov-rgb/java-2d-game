package PaooGame;

import java.awt.*;

/*
 * Clasa care gestionează interfața meniului de setări. Reține butoanele, detectează
 * hover/click și desenează efectele vizuale pentru opțiuni.
 */
public class SettingsUI {

    //
    //  BUTOANE (zonele unde dai click)
    public Rectangle resumeButton = new Rectangle(270, 165, 260, 55);
    public Rectangle homeButton = new Rectangle(270, 230, 260, 55);
    public Rectangle soundUpButton = new Rectangle(270, 295, 260, 55);
    public Rectangle soundDownButton = new Rectangle(270, 360, 260, 55);
    public Rectangle exitButton = new Rectangle(270, 425, 260, 55);

    //  HOVER = mouse-ul este peste buton
    public boolean hoverResume, hoverHome, hoverUp, hoverDown, hoverExit;

    //  PRESSED = buton apăsat
    public boolean pressedResume, pressedHome, pressedUp, pressedDown, pressedExit;


    //
    // Verifică dacă mouse-ul e peste butoane
    /*
     * Actualizează starea de hover a butoanelor. Verifică dacă poziția mouse-ului se află
     * peste fiecare zonă de buton.
     */
    public void updateHover(int mx, int my) {
        hoverResume = resumeButton.contains(mx, my);
        hoverHome = homeButton.contains(mx, my);
        hoverUp = soundUpButton.contains(mx, my);
        hoverDown = soundDownButton.contains(mx, my);
        hoverExit = exitButton.contains(mx, my);
    }


    //  Verifică dacă ai apăsat pe butoane
    /*
     * Actualizează starea de apăsare a butoanelor. Marchează butonul pe care s-a dat click
     * pentru a putea fi desenat efectul vizual.
     */
    public void updatePressed(int mx, int my) {
        pressedResume = resumeButton.contains(mx, my);
        pressedHome = homeButton.contains(mx, my);
        pressedUp = soundUpButton.contains(mx, my);
        pressedDown = soundDownButton.contains(mx, my);
        pressedExit = exitButton.contains(mx, my);
    }


    //  Reset după click
    /*
     * Resetează stările de apăsare ale butoanelor. Se folosește după procesarea click-ului
     * pentru ca efectul să nu rămână blocat.
     */
    public void resetPressed() {
        pressedResume = pressedHome = pressedUp = pressedDown = pressedExit = false;
    }


    //  Desenează efectele (hover + pressed)
    /*
     * Desenează elementele vizuale ale clasei. În funcție de clasă, afișează efecte de
     * interfață sau componente de joc pe ecran.
     */
    public void draw(Graphics2D g2d) {

        //  hover (alb)
        g2d.setColor(new Color(255, 255, 255, 50));

        if (hoverResume)
            g2d.fillRoundRect(resumeButton.x+20, resumeButton.y-8, resumeButton.width-40, resumeButton.height+5, 40,50);

        if (hoverHome)
            g2d.fillRoundRect(homeButton.x+20, homeButton.y-8, homeButton.width-40, homeButton.height+10, 60,50);

        if (hoverUp)
            g2d.fillRoundRect(soundUpButton.x+20, soundUpButton.y-8, soundUpButton.width-50, soundUpButton.height+5, 25,25);

        if (hoverDown)
            g2d.fillRoundRect(soundDownButton.x+20, soundDownButton.y-8, soundDownButton.width-50, soundDownButton.height+5, 25,25);

        if (hoverExit)
            g2d.fillRoundRect(exitButton.x+30, exitButton.y-8, exitButton.width-55, exitButton.height+5, 25,25);


        //  pressed (roz)
        g2d.setColor(new Color(255,192,203,90));

        if (pressedResume)
            g2d.fillRoundRect(resumeButton.x+20, resumeButton.y-4, resumeButton.width-45, resumeButton.height+5, 25,25);

        if (pressedHome)
            g2d.fillRoundRect(homeButton.x+20, homeButton.y-4, homeButton.width-40, homeButton.height+5, 25,25);

        if (pressedUp)
            g2d.fillRoundRect(soundUpButton.x+30, soundUpButton.y-8, soundUpButton.width-55, soundUpButton.height+5, 25,25);

        if (pressedDown)
            g2d.fillRoundRect(soundDownButton.x+30, soundDownButton.y-8, soundDownButton.width-55, soundDownButton.height+5, 25,25);

        if (pressedExit)
            g2d.fillRoundRect(exitButton.x+30, exitButton.y-8, exitButton.width-55, exitButton.height+5, 25,25);
    }
}
