package PaooGame;

/*
 * Enum care reține stările posibile ale jocului. Ajută clasa Game să știe dacă trebuie
 * afișat meniul, jocul, setările, leaderboard-ul, game over-ul sau ecranul de final.
 */
public enum GameState {
    MENU,
    PLAYING,
    LEVEL_INFO,
    SETTINGS,
    STORY,
    LEADERBOARD,
    PROGRESS_FOUND,
    GAME_OVER,
    WIN
}
