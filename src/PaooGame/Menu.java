package PaooGame;

import java.awt.*;

/*
 * Clasa care gestionează butoanele din meniul principal. Reține zonele de click, stările
 * de hover/apăsare și desenează efectele vizuale ale butoanelor.
 */
public class Menu

{

    public Rectangle playButton = new Rectangle(248, 220, 285, 65);
    public Rectangle settingsButton = new Rectangle(248, 315, 285, 65);
    public Rectangle leaderboardButton = new Rectangle(248, 410, 285, 65);
    public Rectangle exitButton = new Rectangle(248, 505, 285, 65);
    //variabile de stareda,
    public boolean hoverPlay, hoverSettings, hoverLeaderboard, hoverExit;
    public boolean pressedPlay, pressedSettings, pressedLeaderboard, pressedExit;//butonul este apasat

    /*
     * Actualizează starea de hover a butoanelor. Verifică dacă poziția mouse-ului se află
     * peste fiecare zonă de buton.
     */
    public void updateHover(int mx, int my) {
        hoverPlay = playButton.contains(mx, my);
        hoverSettings = settingsButton.contains(mx, my);
        hoverLeaderboard = leaderboardButton.contains(mx, my);
        hoverExit = exitButton.contains(mx, my);
    }

    /*
     * Actualizează starea de apăsare a butoanelor. Marchează butonul pe care s-a dat click
     * pentru a putea fi desenat efectul vizual.
     */
    public void updatePressed(int mx, int my) {
        pressedPlay = playButton.contains(mx, my);
        pressedSettings = settingsButton.contains(mx, my);
        pressedLeaderboard = leaderboardButton.contains(mx, my);
        pressedExit = exitButton.contains(mx, my);
    }

    //
    //dupa ce apas click se reseteaza starea
    /*
     * Resetează stările de apăsare ale butoanelor. Se folosește după procesarea click-ului
     * pentru ca efectul să nu rămână blocat.
     */
    public void resetPressed() {
        pressedPlay = false;
        pressedSettings = false;
        pressedLeaderboard = false;
        pressedExit = false;
    }

    //efectele vizuale pt butoane
    /*
     * Desenează elementele vizuale ale clasei. În funcție de clasă, afișează efecte de
     * interfață sau componente de joc pe ecran.
     */
    public void draw(Graphics2D g2d) {

        // HOVER
        g2d.setColor(new Color(255, 255, 255, 40));//alb semi transparent
        int extraWidth = 25;

        if (hoverPlay)
            g2d.fillRoundRect(playButton.x - extraWidth / 2, playButton.y,
                    playButton.width + extraWidth, playButton.height, 70, 70);

        if (hoverSettings)
            g2d.fillRoundRect(settingsButton.x - extraWidth / 2, settingsButton.y,
                    settingsButton.width + extraWidth, settingsButton.height, 70, 70);

        if (hoverLeaderboard)
            g2d.fillRoundRect(leaderboardButton.x - extraWidth / 2, leaderboardButton.y,
                    leaderboardButton.width + extraWidth, leaderboardButton.height, 70, 70);

        if (hoverExit)
            g2d.fillRoundRect(exitButton.x - extraWidth / 2, exitButton.y,
                    exitButton.width + extraWidth, exitButton.height, 70, 70);
// PRESSED
        g2d.setColor(new Color(255, 192, 203, 90));//butonul devine roz cand e apasat

        if (pressedPlay)
            g2d.fillRoundRect(playButton.x - extraWidth / 2, playButton.y,
                    playButton.width + extraWidth, playButton.height, 70, 70);

        if (pressedSettings)
            g2d.fillRoundRect(settingsButton.x - extraWidth / 2, settingsButton.y,
                    settingsButton.width + extraWidth, settingsButton.height, 70, 70);

        if (pressedLeaderboard)
            g2d.fillRoundRect(leaderboardButton.x - extraWidth / 2, leaderboardButton.y,
                    leaderboardButton.width + extraWidth, leaderboardButton.height, 70, 70);

        if (pressedExit)
            g2d.fillRoundRect(exitButton.x - extraWidth / 2, exitButton.y,
                    exitButton.width + extraWidth, exitButton.height, 70, 70);

    }


}
