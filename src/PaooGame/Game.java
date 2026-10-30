package PaooGame;

import Maps.MapParser;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import Maps.GameMap;
import PaooGame.Graphics.ImageLoader;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import PaooGame.Exceptions.InvalidGameStateException;


public class Game implements Runnable, GameEventListener {

    private GameWindow wnd;        /*!< Fereastra in care se va desena tabla jocului*/
    private boolean runState;   /*!< Flag ce starea firului de executie.*/
    private Thread gameThread; /*!< Referinta catre thread-ul de update si draw al ferestrei*/
    private BufferStrategy bs;         /*!< Referinta catre un mecanism cu care se organizeaza memoria complexa pentru un canvas.*/

    private GameState gameState = GameState.MENU;
    private boolean showMenu;
    private boolean showGameOver = false;
    private Rectangle settingsButton;
    private boolean showSettings = false;
    private Graphics g;
    private boolean showLeaderboard = false;
    private Rectangle retryButton;
    private Rectangle gameOverSettingsButton;
    private Rectangle gameOverExitButton;
    private int gameOverLevel = 1;

    private int checkpointX = -1;
    private int checkpointY = -1;
    private Rectangle checkpoint1;
    private Rectangle checkpoint2;

    private String playerName = "";
    private AudioPlayer musicPlayer;
    private GameMap gameMap;
    private boolean showMenuInfo = false;
    private boolean showStory = false;
    private Rectangle storyBackButton;

    private boolean showLevelInfo = false;
    private BufferedImage levelInfo;

    private Rectangle menuSoundUpButton;
    private Rectangle menuSoundDownButton;
    private Rectangle menuBackButton;
    private Rectangle storySoundUp;
    private Rectangle storySoundDown;
    private boolean hoverStorySoundUp = false;
    private boolean hoverStorySoundDown = false;
    private boolean hoverStoryBack = false;

    private boolean pressedStorySoundUp = false;
    private boolean pressedStorySoundDown = false;
    private boolean pressedStoryBack = false;

    private boolean showLifeLostMessage = false;
    private long lifeLostMessageStartTime;
    private boolean pendingGameOver = false;
    private Menu menu;
    private SettingsUI settingsUI;

    private int currentLevel = 1;
    private boolean levelChanging = false;

    private boolean hoverRetry = false;
    private boolean hoverGameOverSettings = false;
    private boolean hoverGameOverExit = false;

    private boolean pressedRetry = false;
    private boolean pressedGameOverSettings = false;
    private boolean pressedGameOverExit = false;

    private boolean hoverProgressYes = false;
    private boolean hoverProgressNo = false;
    private boolean pressedProgressYes = false;
    private boolean pressedProgressNo = false;
    private boolean showProgressFound = false;
    private boolean showWinScreen = false;
    private SaveData pendingSave = null;
    private Rectangle progressYesButton;
    private Rectangle progressNoButton;
    private long lastDamageTime = 0;
    private final long DAMAGE_COOLDOWN = 1500;

    private Dog blockingDog;
    private boolean showDogDistractedMessage = false;
    private long dogDistractedMessageStartTime;
    private Ball distractionBall;
    private Dog blockingDog2;
    private Ball distractionBall2;

    private ArrayList<Train> trains = new ArrayList<>();

    private int score = 0;
    private long levelStartTime;
    private final int LEVEL_TIME = 120; // 120 secunde = 2 minute

    private Cat catPlayer;// obiectul pisică (player-ul)
    private KeyManager keyManager;
    private Camera camera;          // camera jocului: urmărește pisica și aplică zoom

    private Door exitDoor;
    private java.util.ArrayList<Car> cars = new java.util.ArrayList<>();

    private Handler handler; // Handler-ul gestionează central entitățile jocului: player, inamici și obiecte
    private EventManager eventManager;
    /*
     * Adaugă un pește pe hartă la o poziție exprimată în coordonate de tile.
     * Metoda primește id-ul peștelui, coloana, rândul și imaginea folosită pentru desenare.
     * Coordonatele de tile sunt transformate în pixeli prin înmulțirea cu dimensiunea unui tile,
     * apoi obiectul Fish este creat și adăugat în Handler, pentru ca acesta să fie gestionat
     * împreună cu celelalte entități ale nivelului.
     */
    private void addFishOnTile(int id,int col, int row, BufferedImage img) {
        int tileSize = 50;

        // Adăugăm peștele prin Handler, nu direct într-o listă din Game
        handler.addFish(new Fish(id,col * tileSize, row * tileSize, tileSize, img));
    }


    /*
     * Constructorul clasei Game inițializează elementele de bază ale jocului.
     * Creează fereastra principală, setează starea inițială pe meniu, pregătește sistemul audio,
     * creează obiectele pentru interfața meniului și setări, inițializează managerul de evenimente
     * și creează Handler-ul care va administra player-ul, inamicii și obiectele colectabile.
     * Parametrii title, width și height sunt folosiți pentru configurarea ferestrei jocului.
     */
    public Game(String title, int width, int height) {
        /// Obiectul GameWindow este creat insa fereastra nu este construita
        wnd = new GameWindow(title, width, height);

        /// Resetarea flagului thread-ului
        runState = false;

        /// Pornim din meniu
        showMenu = true;

        gameState = GameState.MENU;

        /// Audio
        musicPlayer = AudioPlayer.getInstance();

        /// UI (FOARTE IMPORTANT)
        menu = new Menu();
        settingsUI = new SettingsUI();
        eventManager = new EventManager();
        eventManager.addListener(this);

        handler = new Handler(eventManager);
    }

    /*
     * Încarcă și configurează nivelul primit ca parametru.
     * Metoda resetează entitățile nivelului curent, recreează camera și apoi pregătește harta,
     * player-ul, ușa de ieșire, inamicii, obiectele colectabile, mașinile, trenurile și checkpoint-urile,
     * în funcție de nivelul selectat. De asemenea, păstrează numărul de vieți atunci când jucătorul
     * trece de la un nivel la altul și folosește datele salvate pentru a evita reapariția peștilor
     * deja colectați.
     */
    private void loadLevel(int level) {
        int livesToKeep = 3;

        if (catPlayer != null) {
            livesToKeep = catPlayer.getVieti();
        }
        currentLevel = level;
        levelChanging = false;
        camera = new Camera();

        // Resetăm entitățile când se schimbă nivelul
        handler.clearLevel();

        if (level == 1) {

            // MapParser încarcă harta corespunzătoare nivelului 1
            gameMap = MapParser.loadMap("/maps/level1.txt");

            String collectedFish = "";

            SaveData save = DatabaseManager.loadGame(playerName);

            if (save != null && save.collectedFish != null) {
                collectedFish = save.collectedFish;
            }

            catPlayer = new Cat(1 * 50, 6 * 50, 40);
            // Setăm player-ul în Handler pentru ca celelalte entități să îl poată accesa
            handler.setPlayer(catPlayer);

            score = 0;
            levelStartTime = System.currentTimeMillis();

            exitDoor = new Door(15 * 50, 7 * 50, 48, 48);

            if (!collectedFish.contains("0,")) {
                addFishOnTile(0,2, 9, Assets.fish1);
            }
            if (!collectedFish.contains("1,")) {
                addFishOnTile(1,5, 4, Assets.fish2);
            }

            if (!collectedFish.contains("2,")) {
                addFishOnTile(2,12, 8, Assets.fish1);
            }

            if (!collectedFish.contains("3,")) {
                addFishOnTile(3,1, 8, Assets.fish2);
            }
            if (!collectedFish.contains("4,")) {
                addFishOnTile(4,1, 1, Assets.fish2);
            }

            if (!collectedFish.contains("5,")) {
                addFishOnTile(5,4, 1, Assets.fish1);
            }

            if (!collectedFish.contains("6,")) {
                addFishOnTile(6,7, 8, Assets.fish2);
            }

            if (!collectedFish.contains("7,")) {
                addFishOnTile(7,13, 3, Assets.fish2);
            }

            if (!collectedFish.contains("8,")) {
                addFishOnTile(8,14, 6, Assets.fish1);
            }

            if (!collectedFish.contains("9,")) {
                addFishOnTile(9,9, 10, Assets.fish1);
            }

            if (!collectedFish.contains("10,")) {
                addFishOnTile(10,11, 4, Assets.fish2);
            }

            if (!collectedFish.contains("11,")) {
                addFishOnTile(11,9, 1, Assets.fish1);
            }

            if (!collectedFish.contains("12,")) {
                addFishOnTile(12,9, 3, Assets.fish1);
            }
        } else if (level == 2) {

            showLevelInfo = true;
            levelInfo = ImageLoader.LoadImage("/textures/level2_info.png");

            camera.setZoom(0.9);

            // MapParser permite încărcarea unui alt fișier de hartă pentru nivelul 2
            gameMap = MapParser.loadMap("/maps/level2.txt");


            // aici alegi poziția de start pentru nivelul 2
            catPlayer = new Cat(0 * 50, 15 * 50, 40);
            catPlayer.setVieti(livesToKeep);

            // Setăm pisica (jucătorul) în Handler, ca să poată fi accesată de alte entități (ex: câinii)
            handler.setPlayer(catPlayer);
            // aici alegi unde este ușa finală din nivelul 2
            exitDoor = new Door(39 * 50, 8 * 50, 48, 48);

            // Creează un inamic de tip "dog" folosind EnemyFactory.
            // Rezultatul este un obiect Dog, care este apoi adăugat în lista de inamici din Handler.
            Dog dog1 = new Dog(handler, 10 * 50, 15 * 50);
            dog1.setDirection(1);
            dog1.setSpeed(2);
            handler.addEnemy(dog1);

            Dog dog2 = new Dog(handler, 12 * 50, 6 * 50);
            dog2.setDirection(-1);
            dog2.setSpeed(3);
            handler.addEnemy(dog2);

            Dog dog3 = new Dog(handler, 12 * 50, 26 * 50);
            dog3.setDirection(1);
            dog3.setSpeed(1);
            handler.addEnemy(dog3);

            Dog dog4 = new Dog(handler, 8 * 50, 11 * 50);
            dog4.setDirection(-1);
            dog4.setSpeed(2);
            handler.addEnemy(dog4);

            Dog dog5 = new Dog(handler, 14 * 50, 20 * 50);
            dog5.setDirection(1);
            dog5.setSpeed(3);
            handler.addEnemy(dog5);

            Dog dog6 = new Dog(handler, 8 * 50, 34 * 50);
            dog6.setDirection(-1);
            dog6.setSpeed(2);
            handler.addEnemy(dog6);

            Dog dog7 = new Dog(handler, 23 * 50, 2 * 50);
            dog7.setDirection(1);
            dog7.setSpeed(1);
            handler.addEnemy(dog7);

            blockingDog = new Dog(handler, 26 * 50, 12 * 50);
            blockingDog.setBlockingDog(true);
            handler.addEnemy(blockingDog);

            distractionBall = new Ball(22 * 50, 13 * 50, 35, Assets.ball);

            blockingDog2 = new Dog(handler, 35 * 50, 8 * 50);
            blockingDog2.setBlockingDog(true);
            handler.addEnemy(blockingDog2);
            distractionBall2 = new Ball(31 * 50, 9 * 50, 35, Assets.ball);

        } else if (level == 3) {
            showLevelInfo = true;
            levelInfo = ImageLoader.LoadImage("/textures/level3_info.png");
            camera.setZoom(0.9);
            gameMap = MapParser.loadMap("/maps/level3.txt");

            cars.clear();

            int mapWidth = gameMap.getWidth() * 50;

            cars.add(new Car(0, 3 * 50, 3, Assets.masinaRosie, mapWidth));
            cars.add(new Car(450, 3 * 50, 3, Assets.masinaAlbastra, mapWidth));
            cars.add(new Car(900, 3 * 50, 3, Assets.masinaVerde, mapWidth));

// Banda 2
            cars.add(new Car(150, 4 * 50, 4, Assets.masinaRoz, mapWidth));
            cars.add(new Car(650, 4 * 50, 4, Assets.masinaVerde, mapWidth));
            cars.add(new Car(1100, 4 * 50, 4, Assets.masinaRosie, mapWidth));

// Banda 3
            cars.add(new Car(250, 6 * 50, 2, Assets.masinaAlbastra, mapWidth));
            cars.add(new Car(800, 6 * 50, 2, Assets.masinaRoz, mapWidth));

// Banda 4
            cars.add(new Car(50, 7 * 50, 5, Assets.masinaVerde, mapWidth));
            cars.add(new Car(600, 7 * 50, 5, Assets.masinaRosie, mapWidth));
            cars.add(new Car(1150, 7 * 50, 5, Assets.masinaAlbastra, mapWidth));

            // Banda 5 (rand 14)
            cars.add(new Car(0, 14 * 50, 3, Assets.masinaRosie, mapWidth));
            cars.add(new Car(500, 14 * 50, 3, Assets.masinaAlbastra, mapWidth));
            cars.add(new Car(1000, 14 * 50, 3, Assets.masinaVerde, mapWidth));

// Banda 6 (rand 15)
            cars.add(new Car(200, 15 * 50, 4, Assets.masinaRoz, mapWidth));
            cars.add(new Car(700, 15 * 50, 4, Assets.masinaVerde, mapWidth));
            cars.add(new Car(1200, 15 * 50, 4, Assets.masinaRosie, mapWidth));

// Banda 7 (rand 16)
            cars.add(new Car(100, 16 * 50, 2, Assets.masinaAlbastra, mapWidth));
            cars.add(new Car(800, 16 * 50, 2, Assets.masinaRoz, mapWidth));


            // Banda 8 (rand 19)
            cars.add(new Car(0, 19 * 50, 3, Assets.masinaRosie, mapWidth));
            cars.add(new Car(500, 19 * 50, 3, Assets.masinaAlbastra, mapWidth));
            cars.add(new Car(1000, 19 * 50, 3, Assets.masinaVerde, mapWidth));

// Banda 9 (rand 20)
            cars.add(new Car(200, 20 * 50, 4, Assets.masinaRoz, mapWidth));
            cars.add(new Car(700, 20 * 50, 4, Assets.masinaVerde, mapWidth));
            cars.add(new Car(1200, 20 * 50, 4, Assets.masinaRosie, mapWidth));

// Banda 10 (rand 21)
            cars.add(new Car(100, 21 * 50, 2, Assets.masinaAlbastra, mapWidth));
            cars.add(new Car(800, 21 * 50, 2, Assets.masinaRoz, mapWidth));
            cars.add(new Car(1300, 21 * 50, 2, Assets.masinaVerde, mapWidth));

// Banda 11 (rand 28) - sens stânga
            cars.add(new Car(0, 28 * 50, 3, Assets.masinaRosie, mapWidth));
            cars.add(new Car(500, 28 * 50, 3, Assets.masinaAlbastra, mapWidth));
            cars.add(new Car(1000, 28 * 50, 3, Assets.masinaVerde, mapWidth));

// Banda 12 (rand 29) - sens stânga
            cars.add(new Car(200, 29 * 50, 4, Assets.masinaRoz, mapWidth));
            cars.add(new Car(700, 29 * 50, 4, Assets.masinaVerde, mapWidth));
            cars.add(new Car(1200, 29 * 50, 4, Assets.masinaRosie, mapWidth));

// Banda 13 (rand 31) - sens dreapta
            cars.add(new Car(100, 31 * 50, 3, Assets.masinaAlbastra, mapWidth));
            cars.add(new Car(800, 31 * 50, 3, Assets.masinaRoz, mapWidth));
            cars.add(new Car(1300, 31 * 50, 3, Assets.masinaVerde, mapWidth));

// Banda 14 (rand 32) - sens dreapta
            cars.add(new Car(50, 32 * 50, 5, Assets.masinaVerde, mapWidth));
            cars.add(new Car(600, 32 * 50, 5, Assets.masinaRosie, mapWidth));
            cars.add(new Car(1150, 32 * 50, 5, Assets.masinaAlbastra, mapWidth));

            trains.clear();


            trains.add(new Train(0, 10 * 50, -4, mapWidth));

            trains.add(new Train(0, 24 * 50, -4, mapWidth));
            trains.add(new Train(800, 24 * 50, -4, mapWidth));

            catPlayer = new Cat(20 * 50, 2 * 50, 40);

            checkpointX = catPlayer.getX();
            checkpointY = catPlayer.getY();

            checkpoint1 = new Rectangle(20 * 50, 2 * 50, 50, 50);
            checkpoint2 = new Rectangle( 15 * 50, 17 * 50, 100, 100);
            catPlayer.setVieti(livesToKeep);
            handler.setPlayer(catPlayer);
            exitDoor = new Door(31 * 50, 34 * 50, 48, 48);
        } else {
            throw new InvalidGameStateException("Nivel invalid: " + level);
        }



    }

    /*
     * Inițializează efectiv jocul înainte ca bucla principală să înceapă.
     * Construiește fereastra, încarcă resursele grafice, creează tabelele din baza de date,
     * pornește primul nivel și muzica de fundal. Tot aici sunt definite zonele butoanelor,
     * este atașat KeyManager-ul pentru tastatură și sunt adăugați listenerii de mouse pentru
     * meniuri, setări, ecranul de Game Over, poveste, leaderboard și alegerea progresului salvat.
     */
    private void InitGame() {
        wnd.BuildGameWindow();
        Assets.Init();
        DatabaseManager.createTables();
        levelInfo = ImageLoader.LoadImage("/textures/level1_info.png");
        //incarc harta din fisier

        loadLevel(1);
        musicPlayer.play("/sound/home.wav", true);
        int storyX = 150;
        int storyY = 100;

        storySoundUp = new Rectangle(166, 140, 200, 80);
        storySoundDown = new Rectangle(410, 140, 200, 80);
        storyBackButton = new Rectangle(295, 205, 210, 65);

        storySoundUp = new Rectangle(175, 125, 190, 75);
        storySoundDown = new Rectangle(420, 130, 190, 70);
        storyBackButton = new Rectangle(275, 205, 210, 70);

        settingsButton = new Rectangle(755, 10, 30, 30);
        retryButton = new Rectangle(430, 435, 450, 140);
        gameOverSettingsButton = new Rectangle(430, 610, 450, 140);
        gameOverExitButton = new Rectangle(430, 770, 450, 140);
        progressYesButton = new Rectangle(190, 405, 200, 80);
        progressNoButton = new Rectangle(415, 405, 200, 80);

        Canvas canvas = wnd.GetCanvas(); // ia zona de desen a ferestrei, pe care afișez jocul și pun input-ul de la tastatură/mouse

        //cod pt KeyManager
        keyManager = new KeyManager();
        canvas.addKeyListener(keyManager);

        canvas.setFocusable(true); // permite canvas-ului să primească input de la tastatură
        canvas.requestFocus();     // mută focusul pe canvas ca tastele să funcționeze

        canvas.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                int mx = e.getX();
                int my = e.getY();
                if (showGameOver && !showSettings) {
                    pressedRetry = retryButton.contains(mx, my);
                    pressedGameOverSettings = gameOverSettingsButton.contains(mx, my);
                    pressedGameOverExit = gameOverExitButton.contains(mx, my);
                    return;
                }

                if (showProgressFound) {
                    pressedProgressYes = progressYesButton.contains(mx, my);
                    pressedProgressNo = progressNoButton.contains(mx, my);
                    return;
                }

                if (showStory) {
                    pressedStorySoundUp = storySoundUp.contains(mx, my);
                    pressedStorySoundDown = storySoundDown.contains(mx, my);
                    pressedStoryBack = storyBackButton.contains(mx, my);
                    return;
                }


                if (showMenu) {
                    menu.updatePressed(mx, my);
                } else if (showSettings) {
                    settingsUI.updatePressed(mx, my);
                }
            }

            public void mouseReleased(MouseEvent e) {
                int mx = e.getX();
                int my = e.getY();
                if (showProgressFound) {
                    if (progressYesButton.contains(mx, my)) {
                        pressedProgressYes = false;
                        pressedProgressNo = false;

                        showProgressFound = false;

                        if (pendingSave != null) {
                            loadLevel(pendingSave.level);
                            score = pendingSave.score;
                            catPlayer.setVieti(pendingSave.lives);

                            if (pendingSave.level == 3 &&
                                    pendingSave.checkpointX != -1 &&
                                    pendingSave.checkpointY != -1) {

                                checkpointX = pendingSave.checkpointX;
                                checkpointY = pendingSave.checkpointY;

                                catPlayer.setX(checkpointX);
                                catPlayer.setY(checkpointY);
                            }
                        }

                        pendingSave = null;
                        showLevelInfo = true;
                        gameState = GameState.LEVEL_INFO;

                        canvas.setFocusable(true);
                        canvas.requestFocus();
                        return;
                    }

                    if (progressNoButton.contains(mx, my)) {
                        pressedProgressYes = false;
                        pressedProgressNo = false;

                        showProgressFound = false;
                        pendingSave = null;

                        loadLevel(1);
                        score = 0;
                        catPlayer.setVieti(3);

                        DatabaseManager.saveOrUpdateGame(playerName, currentLevel, score, catPlayer.getVieti(), "", checkpointX, checkpointY);

                        showLevelInfo = true;
                        gameState = GameState.LEVEL_INFO;

                        canvas.setFocusable(true);
                        canvas.requestFocus();
                        return;
                    }

                    pressedProgressYes = false;
                    pressedProgressNo = false;
                    return;
                }
                if (showGameOver) {

                    // SETTINGS DESCHIS PESTE GAME OVER
                    if (showSettings) {

                        if (settingsUI.resumeButton.contains(mx, my)) {
                            showSettings = false;
                            return;
                        }
                        if (settingsUI.homeButton.contains(mx, my)) {
                            showSettings = false;
                            showGameOver = false;
                            showMenu = true;
                            return;
                        }

                        if (settingsUI.soundUpButton.contains(mx, my)) {
                            musicPlayer.volumeUp();
                            return;
                        }

                        if (settingsUI.soundDownButton.contains(mx, my)) {
                            musicPlayer.volumeDown();
                            return;
                        }

                        if (settingsUI.exitButton.contains(mx, my)) {
                            System.exit(0);
                        }
                    }

                    // BUTOANE GAME OVER
                    if (retryButton.contains(mx, my)) {
                        pressedRetry = false;
                        pressedGameOverSettings = false;
                        pressedGameOverExit = false;

                        showGameOver = false;
                        showSettings = false;
                        score = 0;
                        currentLevel = 1;
                        checkpointX = -1;
                        checkpointY = -1;

                        loadLevel(1);
                        catPlayer.setVieti(3);

                        DatabaseManager.saveOrUpdateGame(
                                playerName,
                                1,
                                score,
                                catPlayer.getVieti(),
                                "",
                                checkpointX,
                                checkpointY
                        );

                        return;
                    }
                    if (gameOverSettingsButton.contains(mx, my)) {
                        pressedRetry = false;
                        pressedGameOverSettings = false;
                        pressedGameOverExit = false;

                        showSettings = true;
                        gameState = GameState.SETTINGS;
                        return;
                    }

                    if (gameOverExitButton.contains(mx, my)) {
                        pressedRetry = false;
                        pressedGameOverSettings = false;
                        pressedGameOverExit = false;
                        System.exit(0);
                    }
                    pressedRetry = false;
                    pressedGameOverSettings = false;
                    pressedGameOverExit = false;

                    return;
                }
                if (showStory) {

                    if (storySoundUp.contains(mx, my)) {
                        musicPlayer.volumeUp();
                    }

                    if (storySoundDown.contains(mx, my)) {
                        musicPlayer.volumeDown();
                    }

                    if (storyBackButton.contains(mx, my)) {
                        showStory = false;
                        gameState = GameState.MENU;
                    }
                    pressedStorySoundUp = false;
                    pressedStorySoundDown = false;
                    pressedStoryBack = false;

                    return;
                }

                if (showMenuInfo) {
                    if (menuSoundUpButton.contains(mx, my)) {
                        musicPlayer.volumeUp();
                    }

                    if (menuSoundDownButton.contains(mx, my)) {
                        musicPlayer.volumeDown();
                    }

                    if (menuBackButton.contains(mx, my)) {
                        showMenuInfo = false;
                    }

                    return;
                }

                if (showMenu) {

                    if (menu.playButton.contains(mx, my)) {
                        String name = javax.swing.JOptionPane.showInputDialog(
                                wnd.GetWndFrame(),
                                "Introdu numele jucatorului:"
                        );

                        if (name != null && !name.trim().isEmpty()) {

                            playerName = name.trim().toLowerCase();
                            SaveData save = DatabaseManager.loadGame(playerName);

                            if (save != null) {
                                pendingSave = save;
                                showProgressFound = true;
                                showMenu = false;
                                gameState = GameState.PROGRESS_FOUND;
                                canvas.setFocusable(true);
                                canvas.requestFocus();
                                return;
                            } else {
                                loadLevel(1);
                                score = 0;
                                catPlayer.setVieti(3);

                                DatabaseManager.saveOrUpdateGame(playerName, currentLevel, score, catPlayer.getVieti(), "",checkpointX, checkpointY);

                                showMenu = false;
                                showLevelInfo = true;
                                gameState = GameState.LEVEL_INFO;
                                canvas.setFocusable(true);
                                canvas.requestFocus();
                            }
                        }
                    }


                    if (menu.settingsButton.contains(mx, my)) {
                        showStory = true;
                        gameState = GameState.STORY;
                    }

                    if (menu.leaderboardButton.contains(mx, my)) {
                        showLeaderboard = true;
                        showMenu = false;
                        gameState = GameState.LEADERBOARD;
                        menu.resetPressed();
                        return;
                    }

                    if (menu.exitButton.contains(mx, my)) {
                        System.exit(0);
                    }

                    menu.resetPressed();
                } else {
                    if (settingsButton.contains(mx, my)) {
                        showSettings = !showSettings;
                        if (showSettings) {
                            gameState = GameState.SETTINGS;
                        } else {
                            gameState = GameState.PLAYING;
                        }
                        return;
                    }

                    if (showSettings) {

                        if (settingsUI.resumeButton.contains(mx, my)) {
                            showSettings = false;
                            gameState = GameState.PLAYING;
                        }

                        if (settingsUI.homeButton.contains(mx, my)) {
                            showSettings = false;
                            showGameOver = false;
                            showMenu = true;
                            gameState = GameState.MENU;
                        }

                        if (settingsUI.soundUpButton.contains(mx, my)) {
                            musicPlayer.volumeUp();
                        }

                        if (settingsUI.soundDownButton.contains(mx, my)) {
                            musicPlayer.volumeDown();
                        }

                        if (settingsUI.exitButton.contains(mx, my)) {
                            System.exit(0);
                        }

                        settingsUI.resetPressed();
                    }
                }
            }
        });


        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                int mx = e.getX();
                int my = e.getY();

                if (showGameOver && !showSettings) {
                    hoverRetry = retryButton.contains(mx, my);
                    hoverGameOverSettings = gameOverSettingsButton.contains(mx, my);
                    hoverGameOverExit = gameOverExitButton.contains(mx, my);
                    return;
                }

                if (showProgressFound) {
                    hoverProgressYes = progressYesButton.contains(mx, my);
                    hoverProgressNo = progressNoButton.contains(mx, my);
                    return;
                }
                if (showStory) {
                    hoverStorySoundUp = storySoundUp.contains(mx, my);
                    hoverStorySoundDown = storySoundDown.contains(mx, my);
                    hoverStoryBack = storyBackButton.contains(mx, my);
                    return;
                }

                if (showMenu) {
                    menu.updateHover(mx, my);
                } else if (showSettings) {
                    settingsUI.updateHover(mx, my);
                }
            }
        });
    }


    //    /*! \fn public void run()
//        \brief Functia ce va rula in thread-ul creat.
//
//        Aceasta functie va actualiza starea jocului si va redesena tabla de joc (va actualiza fereastra grafica)
//     */
    /*
     * Reprezintă metoda executată automat de thread-ul jocului.
     * Ea pornește inițializarea și apoi rulează bucla principală, care se repetă cât timp runState
     * este true. În interiorul buclei se controlează timpul dintre frame-uri, astfel încât jocul
     * să fie actualizat și desenat aproximativ de 60 de ori pe secundă. Metoda apelează Update()
     * pentru logică și Draw() pentru afișare.
     */
    public void run() {
        /// Initializeaza obiectul game
        InitGame();
        long oldTime = System.nanoTime();   /*!< Retine timpul in nanosecunde aferent frame-ului anterior.*/
        long curentTime;                    /*!< Retine timpul curent de executie.*/

        /// Apelul functiilor Update() & Draw() trebuie realizat la fiecare 16.7 ms
        /// sau mai bine spus de 60 ori pe secunda.

        final int framesPerSecond = 60; /*!< Constanta intreaga initializata cu numarul de frame-uri pe secunda.*/
        final double timeFrame = 1000000000 / framesPerSecond; /*!< Durata unui frame in nanosecunde.*/

        /// Atat timp timp cat threadul este pornit Update() & Draw()
        while (runState == true) {
            /// Se obtine timpul curent
            curentTime = System.nanoTime();
            /// Daca diferenta de timp dintre curentTime si oldTime mai mare decat 16.6 ms
            if ((curentTime - oldTime) > timeFrame) {
                /// Actualizeaza pozitiile elementelor
                Update();
                /// Deseneaza elementele grafica in fereastra.
                Draw();
                oldTime = curentTime;
            }
        }

    }

    //
//    /*! \fn public synchronized void start()
//        \brief Creaza si starteaza firul separat de executie (thread).
//
//        Metoda trebuie sa fie declarata synchronized pentru ca apelul acesteia sa fie semaforizat.
//     */
    /*
     * Pornește jocul prin crearea și lansarea thread-ului principal.
     * Metoda verifică dacă jocul nu rulează deja, setează runState pe true, construiește un thread
     * folosind obiectul Game curent și îl pornește. Este synchronized pentru a evita porniri multiple
     * simultane ale aceluiași joc.
     */
    public synchronized void StartGame() {
        if (runState == false) {
            /// Se actualizeaza flagul de stare a threadului
            runState = true;
            /// Se construieste threadul avand ca parametru obiectul Game. De retinut faptul ca Game class
            /// implementeaza interfata Runnable. Threadul creat va executa functia run() suprascrisa in clasa Game.
            gameThread = new Thread(this);
            /// Threadul creat este lansat in executie (va executa metoda run())
            gameThread.start();
        } else {
            /// Thread-ul este creat si pornit deja
            return;
        }
    }

    //    /*! \fn public synchronized void stop()
//        \brief Opreste executie thread-ului.
//
//        Metoda trebuie sa fie declarata synchronized pentru ca apelul acesteia sa fie semaforizat.
//     */
    /*
     * Oprește execuția jocului și închide în siguranță thread-ul principal.
     * Dacă jocul rulează, runState devine false, iar join() așteaptă terminarea thread-ului.
     * Metoda tratează InterruptedException pentru cazul în care oprirea thread-ului este întreruptă.
     */
    public synchronized void StopGame() {
        if (runState == true) {
            /// Actualizare stare thread
            runState = false;
            /// Metoda join() arunca exceptii motiv pentru care trebuie incadrata intr-un block try - catch.
            try {
                /// Metoda join() pune un thread in asteptare panca cand un altul isi termina executie.
                /// Totusi, in situatia de fata efectul apelului este de oprire a threadului.
                gameThread.join();
            } catch (InterruptedException ex) {
                /// In situatia in care apare o exceptie pe ecran vor fi afisate informatii utile pentru depanare.
                ex.printStackTrace();
            }
        } else {
            /// Thread-ul este oprit deja.
            return;
        }
    }

    @Override
    /*
     * Primește și tratează evenimentele transmise prin EventManager.
     * Pentru SCORE_UPDATE, modifică scorul și salvează progresul. Pentru DAMAGE, aplică un cooldown
     * ca jucătorul să nu piardă vieți prea rapid, scade viața pisicii, salvează progresul și decide
     * dacă trebuie afișat mesajul de viață pierdută sau dacă jocul trebuie să treacă în Game Over.
     */
    public void onEvent(GameEventType type, int value) {
        if (type == GameEventType.SCORE_UPDATE) {
            score += value;
            DatabaseManager.saveOrUpdateGame(playerName, currentLevel, score, catPlayer.getVieti(), "", checkpointX, checkpointY);
        }

        if (type == GameEventType.DAMAGE) {
            long now = System.currentTimeMillis();

            if (now - lastDamageTime < DAMAGE_COOLDOWN) {
                return;
            }

            lastDamageTime = now;

            if (catPlayer != null) {
                catPlayer.pierdeViata();
                DatabaseManager.saveOrUpdateGame(playerName, currentLevel, score, catPlayer.getVieti(), "", checkpointX, checkpointY);

                System.out.println("Viata pierduta prin Observer. Vieti ramase: " + catPlayer.getVieti());

                if (catPlayer.getVieti() <= 0) {
                    pendingGameOver = true;
                    return;
                }

                showLifeLostMessage = true;
                lifeLostMessageStartTime = System.currentTimeMillis();
            }
        }
    }

    /*
     * Activează starea de Game Over atunci când jucătorul rămâne fără vieți.
     * Metoda salvează progresul corespunzător, pregătește ecranul de Game Over, dezactivează celelalte
     * ecrane temporare, resetează nivelul și scorul pentru o eventuală reluare și pornește muzica
     * specifică meniului principal.
     */
    private void gameOver() {
        System.out.println("GAME OVER!");

        pendingGameOver = false;

        gameOverLevel = currentLevel;
        DatabaseManager.saveOrUpdateGame(playerName, gameOverLevel, score, 3, "", checkpointX, checkpointY);
        showGameOver = true;
        gameState = GameState.GAME_OVER;

        showMenu = false;
        showSettings = false;
        showStory = false;
        showLevelInfo = false;
        showMenuInfo = false;
        showLifeLostMessage = false;

        currentLevel = 1;
        score = 0;
        levelChanging = false;

        musicPlayer.play("/sound/home.wav", true);
    }

    /*
     * Actualizează logica jocului la fiecare frame.
     * Metoda gestionează stările speciale precum win screen, leaderboard și informațiile de nivel,
     * apoi, în timpul jocului efectiv, verifică input-ul de la tastatură, mișcarea pisicii, coliziunile
     * cu harta, ascunderea în tufișuri, interacțiunea cu mingile care distrag câinii, ușa de ieșire,
     * colectarea peștilor, timer-ul nivelului 1, inamicii, mașinile, trenurile, checkpoint-urile,
     * damage-ul, Game Over-ul și actualizarea camerei.
     */
    private void Update() {

        if (showWinScreen) {

            if (keyManager.enterPressed) {

                showWinScreen = false;

                showMenu = true;
                gameState = GameState.MENU;

                currentLevel = 1;
                score = 0;

                checkpointX = -1;
                checkpointY = -1;

                loadLevel(1);

                keyManager.enterPressed = false;

                musicPlayer.play("/sound/home.wav", true);
            }

            return;
        }

        if (showLeaderboard) {
            if (keyManager.escPressed) {
                showLeaderboard = false;
                showMenu = true;
                gameState = GameState.MENU;
                keyManager.escPressed = false;
            }
            return;
        }


        if (showLevelInfo) {
            if (keyManager.enterPressed) {
                showLevelInfo = false;
                gameState = GameState.PLAYING;
                keyManager.enterPressed = false;

                if (currentLevel == 1) {
                    levelStartTime = System.currentTimeMillis();
                }
            }
            return;
        }

        if (!showMenu && !showSettings && !showStory && !showLevelInfo) {

            // poziția curentă a pisicii
            int nextX = catPlayer.getX();
            int nextY = catPlayer.getY();

            // dimensiunea pisicii
            int size = catPlayer.getSize();

            // pasul cu care se verifică mișcarea
            int step = 4;

            if (keyManager.left) {
                // calculez unde ar ajunge pisica dacă s-ar mișca la stânga
                nextX = catPlayer.getX() - step;

                // verific colțul stânga-sus și stânga-jos
                boolean lovesteStangaSus = gameMap.isSolidAt(nextX, catPlayer.getY());
                boolean lovesteStangaJos = gameMap.isSolidAt(nextX, catPlayer.getY() + size - 1);

                // dacă nu lovește nimic solid, se poate mișca
                if (!lovesteStangaSus && !lovesteStangaJos) {
                    catPlayer.moveLeft();
                }
            }

            if (keyManager.right) {
                // calculez marginea din dreapta unde ar ajunge pisica
                nextX = catPlayer.getX() + step + size - 1;

                // verific colțul dreapta-sus și dreapta-jos
                boolean lovesteDreaptaSus = gameMap.isSolidAt(nextX, catPlayer.getY());
                boolean lovesteDreaptaJos = gameMap.isSolidAt(nextX, catPlayer.getY() + size - 1);

                // dacă nu lovește nimic solid, se poate mișca
                if (!lovesteDreaptaSus && !lovesteDreaptaJos) {
                    catPlayer.moveRight();
                }
            }


            if (keyManager.up) {
                // calculez unde ar ajunge pisica dacă merge în sus
                nextY = catPlayer.getY() - step;

                // verific colțul sus-stânga și sus-dreapta
                boolean lovesteSusStanga = gameMap.isSolidAt(catPlayer.getX(), nextY);
                boolean lovesteSusDreapta = gameMap.isSolidAt(catPlayer.getX() + size - 1, nextY);

                // dacă nu lovește nimic solid, se poate mișca
                if (!lovesteSusStanga && !lovesteSusDreapta) {
                    catPlayer.moveUp();
                }
            }
            if (keyManager.down) {
                // calculez marginea de jos unde ar ajunge pisica
                nextY = catPlayer.getY() + step + size - 1;

                // verific colțul jos-stânga și jos-dreapta
                boolean lovesteJosStanga = gameMap.isSolidAt(catPlayer.getX(), nextY);
                boolean lovesteJosDreapta = gameMap.isSolidAt(catPlayer.getX() + size - 1, nextY);

                // dacă nu lovește nimic solid, se poate mișca
                if (!lovesteJosStanga && !lovesteJosDreapta) {
                    catPlayer.moveDown();
                }
            }
            // Ascundere în tufiș cu Enter, doar în nivelul 2
            // toggle ascundere (apas o dată)
            if (currentLevel == 2 && keyManager.enterPressed) {
                int centerX = catPlayer.getX() + catPlayer.getSize() / 2;
                int centerY = catPlayer.getY() + catPlayer.getSize() / 2;

                if (gameMap.isBushAt(centerX, centerY)) {
                    catPlayer.setHidden(!catPlayer.isHidden());
                    System.out.println("Ascuns: " + catPlayer.isHidden());
                }

                keyManager.enterPressed = false;
            }

            if (currentLevel == 2 && catPlayer.isHidden()) {
                int centerX = catPlayer.getX() + catPlayer.getSize() / 2;
                int centerY = catPlayer.getY() + catPlayer.getSize() / 2;

                if (!gameMap.isBushAt(centerX, centerY)) {
                    catPlayer.setHidden(false);
                }
            }

            if (currentLevel == 2 && distractionBall != null && blockingDog != null) {
                distractionBall.update();

                if (keyManager.interactPressed && !distractionBall.isThrown()) {
                    if (catPlayer.intersects(distractionBall)) {
                        distractionBall.throwTo(19 * 50, 12 * 50);                        keyManager.interactPressed = false;
                    }
                }

                if (distractionBall.hasArrived()) {
                    blockingDog.goToBall(distractionBall.getX() + 20, distractionBall.getY());

                    showDogDistractedMessage = true;
                    dogDistractedMessageStartTime = System.currentTimeMillis();

                    distractionBall.reset();
                }
            }

            if (currentLevel == 2 && distractionBall2 != null && blockingDog2 != null) {

                distractionBall2.update();
                if (keyManager.interactPressed && !distractionBall2.isThrown()) {

                    if (catPlayer.intersects(distractionBall2)) {
                        distractionBall2.throwTo(27 * 50, 8 * 50);
                        keyManager.interactPressed = false;
                    }
                }
                if (distractionBall2.hasArrived()) {
                    blockingDog2.goToBall(distractionBall2.getX() + 20, distractionBall2.getY());

                    showDogDistractedMessage = true;
                    dogDistractedMessageStartTime = System.currentTimeMillis();

                    distractionBall2.reset();
                }
            }

            //actualizează ușa de ieșire (logica de animație)
            if (exitDoor != null) {
                exitDoor.update();
            }

            // 2. Detectarea Coliziunilor: Pisica atinge ușa?
            if (catPlayer != null && exitDoor != null && catPlayer.intersects(exitDoor)) {
                if (currentLevel == 1) {
                    if (score >= 100) {
                        exitDoor.trigger();
                    }
                } else {
                    exitDoor.trigger();
                }
            }

            // 3. Logica pentru Nivel Completat: S-a deschis ușa complet?
            if (exitDoor != null && exitDoor.isFullyOpen()) {
                if (currentLevel == 1 && score >= 100) {
                    DatabaseManager.saveOrUpdateGame(playerName, 2, score, catPlayer.getVieti(), getCollectedFishString(), checkpointX, checkpointY);
                    loadLevel(2);
                }

                else if (currentLevel == 2) {
                    DatabaseManager.saveOrUpdateGame(playerName, 3, score, catPlayer.getVieti(), "", checkpointX, checkpointY);
                    loadLevel(3);
                }
                else if (currentLevel == 3) {
                    showWinScreen = true;
                    gameState = GameState.WIN;
                }
            }

            //colectarea pestilor
            for (Fish fish : handler.getFishList()) {
                if (!fish.isCollected() && catPlayer.intersects(fish)) {

                    fish.collect();

                    if (fish.getImage() == Assets.fish1) {
                        score += 10;
                    } else {
                        score += 20;
                    }

                    DatabaseManager.saveOrUpdateGame(
                            playerName,
                            currentLevel,
                            score,
                            catPlayer.getVieti(),
                            getCollectedFishString(), checkpointX, checkpointY
                    );

                    musicPlayer.play("/sound/collect.wav", false);
                }
            }

            if (currentLevel == 1) {
                long elapsedTime = (System.currentTimeMillis() - levelStartTime) / 1000;
                long remainingTime = LEVEL_TIME - elapsedTime;

                if (remainingTime <= 0 && score < 100) {
                    eventManager.notify(GameEventType.DAMAGE, 1);
                    if (pendingGameOver) {
                        gameOver();
                        return;
                    }
                    levelStartTime = System.currentTimeMillis();
                }
            }
            // actualizare inamici prin Handler
            handler.updateEnemies();

            if (currentLevel == 3) {
                if (checkpoint1.intersects(catPlayer.getBounds())) {
                    checkpointX = checkpoint1.x;
                    checkpointY = checkpoint1.y;

                    DatabaseManager.saveOrUpdateGame(playerName, currentLevel, score,
                            catPlayer.getVieti(), getCollectedFishString(), checkpointX, checkpointY);
                }


                if (checkpoint2.intersects(catPlayer.getBounds())) {
                    checkpointX = checkpoint2.x;
                    checkpointY = checkpoint2.y;

                    DatabaseManager.saveOrUpdateGame(playerName, currentLevel, score,
                            catPlayer.getVieti(), getCollectedFishString(), checkpointX, checkpointY);
                    System.out.println("Checkpoint 2 activat!");
                }

                for (Car car : cars) {
                    car.update();

                    if (catPlayer.intersects(car)) {
                        eventManager.notify(GameEventType.DAMAGE, 1);
                        catPlayer.setX(checkpointX);
                        catPlayer.setY(checkpointY);
                    }
                }
            }
            if (currentLevel == 3) {
                for (Train t : trains) {
                    t.update();

                    if (catPlayer.intersects(t)) {
                        eventManager.notify(GameEventType.DAMAGE, 1);

                        if (!pendingGameOver) {
                            catPlayer.setX(checkpointX);
                            catPlayer.setY(checkpointY);
                        }
                    }
                }
            }

            // Reguli speciale pentru nivelul 2:
            // câinii au rază de vizibilitate și Mitzi are 5 secunde să se îndepărteze.
            if (currentLevel == 2) {
                handler.checkEnemyVision();
            }
            if (pendingGameOver) {
                gameOver();
                return;
            }
            // actualizez camera ca să urmărească pisica
            camera.update(
                    catPlayer,
                    wnd.GetWndWidth(),
                    wnd.GetWndHeight(),
                    gameMap.getWidth() * 50,
                    gameMap.getHeight() * 50
            );

        }
    }
//        \brief Actualizeaza starea elementelor din joc.
//
//        Metoda este declarata privat deoarece trebuie apelata doar in metoda run()
//     */

    //
//    /*! \fn private void Draw()
//        \brief Deseneaza elementele grafice in fereastra coresponzator starilor actualizate ale elementelor.
//
//        Metoda este declarata privat deoarece trebuie apelata doar in metoda run()
////     */
//    private void Draw() {
//        /// Returnez bufferStrategy pentru canvasul existent
//        bs = wnd.GetCanvas().getBufferStrategy();
//        /// Verific daca buffer strategy a fost construit sau nu
//        if (bs == null) {
//            /// Se executa doar la primul apel al metodei Draw()
//            try {
//                /// Se construieste tripul buffer
//                wnd.GetCanvas().createBufferStrategy(3);
//                return;
//            } catch (Exception e) {
//                /// Afisez informatii despre problema aparuta pentru depanare.
//                e.printStackTrace();
//            }
//        }
////        /// Se obtine contextul grafic curent in care se poate desena.
//        g = bs.getDrawGraphics();
////        /// Se sterge ce era
//        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());
//        if(showMenu)
//        {
//            DrawMenu();
//        }
//        else
//        {
//            DrawGame();
//        }
////
////        /// operatie de desenare
////        // ...............
////        Tile.grassTile.Draw(g, 0 * Tile.TILE_WIDTH, 0);
////        Tile.soilTile.Draw(g, 1 * Tile.TILE_WIDTH, 0);
////        Tile.waterTile.Draw(g, 2 * Tile.TILE_WIDTH, 0);
////        Tile.mountainTile.Draw(g, 3 * Tile.TILE_WIDTH, 0);
////        Tile.treeTile.Draw(g, 4 * Tile.TILE_WIDTH, 0);
////
////        g.drawRect(1 * Tile.TILE_WIDTH, 1 * Tile.TILE_HEIGHT, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
////
////
////        // end operatie de desenare
////        /// Se afiseaza pe ecran
//        bs.show();

    /// /
    /// /        /// Elibereaza resursele de memorie aferente contextului grafic curent (zonele de memorie ocupate de
    /// /        /// elementele grafice ce au fost desenate pe canvas).
//        g.dispose();
    //}

    /*
     * Construiește un text care conține id-urile peștilor deja colectați.
     * Metoda parcurge lista de pești din Handler și adaugă în șir doar id-urile celor marcați
     * ca fiind colectați. Rezultatul este folosit la salvarea progresului, pentru ca peștii deja
     * luați să nu mai apară din nou la încărcarea jocului.
     */
    private String getCollectedFishString() {
        String collected = "";

        for (Fish f : handler.getFishList()) {
            if (f.isCollected()) {
                collected += f.getId() + ",";
            }
        }

        return collected;
    }


    /*
     * Desenează frame-ul curent al jocului pe ecran.
     * Metoda folosește BufferStrategy pentru desenare eficientă, curăță ecranul și alege ce trebuie
     * afișat în funcție de starea jocului: ecran de victorie, progres găsit, leaderboard, meniu sau joc.
     * De asemenea, suprapune ecranele de poveste, informații de nivel, Game Over și setări atunci când
     * acestea sunt active, apoi afișează bufferul și eliberează resursele grafice.
     */
    private void Draw() {
        bs = wnd.GetCanvas().getBufferStrategy();

        if (bs == null) {
            wnd.GetCanvas().createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();
        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

        if (showWinScreen) {
            g.drawImage(Assets.winScreen, 0, 0,
                    wnd.GetWndWidth(),
                    wnd.GetWndHeight(),
                    null);
        }
        else
            // 🔹 fundal (joc sau meniu)
            if (showProgressFound) {
                DrawProgressFound();
            } else if (showLeaderboard) {
                DrawLeaderboard();
            } else if (showMenu) {
                DrawMenu();
            } else {
                DrawGame();
            }

        if (showStory) {
            g.drawImage(Assets.storyScreen, 150, 100, 500, 400, null);

            Graphics2D g2d = (Graphics2D) g;

            // hover = alb transparent
            g2d.setColor(new Color(255, 255, 255, 50));

            if (hoverStorySoundUp)
                g2d.fillRoundRect(storySoundUp.x, storySoundUp.y, storySoundUp.width, storySoundUp.height, 30, 30);

            if (hoverStorySoundDown)
                g2d.fillRoundRect(storySoundDown.x, storySoundDown.y, storySoundDown.width, storySoundDown.height, 30, 30);

            if (hoverStoryBack)
                g2d.fillRoundRect(storyBackButton.x, storyBackButton.y, storyBackButton.width, storyBackButton.height, 30, 30);

            // pressed = roz transparent
            g2d.setColor(new Color(255, 192, 203, 90));

            if (pressedStorySoundUp)
                g2d.fillRoundRect(storySoundUp.x, storySoundUp.y, storySoundUp.width, storySoundUp.height, 30, 30);

            if (pressedStorySoundDown)
                g2d.fillRoundRect(storySoundDown.x, storySoundDown.y, storySoundDown.width, storySoundDown.height, 30, 30);

            if (pressedStoryBack)
                g2d.fillRoundRect(storyBackButton.x, storyBackButton.y, storyBackButton.width, storyBackButton.height, 30, 30);

        }
        if (showLevelInfo) {
            g.drawImage(levelInfo, 0, 0, wnd.GetWndWidth(), wnd.GetWndHeight(), null);
        }

        if (showGameOver) {
            int W = wnd.GetWndWidth();
            int H = wnd.GetWndHeight();

            g.drawImage(Assets.gameOverScreen, 0, 0, W, H, null);

            // Butoanele centrate proporțional
            int btnW  = (int)(W * 0.39);   // lățime buton ~39% din ecran
            int btnH  = (int)(H * 0.13);   // înălțime buton ~13% din ecran
            int btnX  = (W - btnW) / 2;    // centrat pe orizontală

            retryButton            = new Rectangle(btnX, (int)(H * 0.44), btnW, btnH);
            gameOverSettingsButton = new Rectangle(btnX, (int)(H * 0.63), btnW, btnH);
            gameOverExitButton     = new Rectangle(btnX, (int)(H * 0.81), btnW, btnH);

            // debug - sterge după ce funcționează
            g.setColor(Color.RED);
            Graphics2D g2d = (Graphics2D) g;

// HOVER - alb transparent
            g2d.setColor(new Color(255, 255, 255, 45));

            if (hoverRetry) {
                g2d.fillRoundRect(retryButton.x, retryButton.y,
                        retryButton.width, retryButton.height, 70, 70);
            }

            if (hoverGameOverSettings) {
                g2d.fillRoundRect(gameOverSettingsButton.x, gameOverSettingsButton.y,
                        gameOverSettingsButton.width, gameOverSettingsButton.height, 70, 70);
            }

            if (hoverGameOverExit) {
                g2d.fillRoundRect(gameOverExitButton.x, gameOverExitButton.y,
                        gameOverExitButton.width, gameOverExitButton.height, 70, 70);
            }

// PRESSED - roz transparent
            g2d.setColor(new Color(255, 192, 203, 90));

            if (pressedRetry) {
                g2d.fillRoundRect(retryButton.x, retryButton.y,
                        retryButton.width, retryButton.height, 70, 70);
            }

            if (pressedGameOverSettings) {
                g2d.fillRoundRect(gameOverSettingsButton.x, gameOverSettingsButton.y,
                        gameOverSettingsButton.width, gameOverSettingsButton.height, 70, 70);
            }

            if (pressedGameOverExit) {
                g2d.fillRoundRect(gameOverExitButton.x, gameOverExitButton.y,
                        gameOverExitButton.width, gameOverExitButton.height, 70, 70);
            }
        }

        if (showGameOver && showSettings) {
            g.drawImage(Assets.settingsMenu, 150, 80, null);
            settingsUI.draw((Graphics2D) g);
        }
        bs.show();

        g.dispose();
    }

    /*
     * Desenează meniul principal al jocului.
     * Dacă imaginea de fundal a meniului este încărcată, aceasta este afișată pe întreaga fereastră.
     * În caz contrar, se folosește un fundal simplu roz. La final, metoda apelează obiectul Menu
     * pentru a desena efectele vizuale ale butoanelor, precum hover și pressed.
     */
    private void DrawMenu() {
        if (Assets.menuBackground != null) {
            g.drawImage(Assets.menuBackground, 0, 0, wnd.GetWndWidth(), wnd.GetWndHeight(), null);
        } else {
            g.setColor(Color.PINK);
            g.fillRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());
        }

        menu.draw((Graphics2D) g);
    }

    /*
     * Desenează ecranul de poveste al jocului, dacă imaginea necesară există.
     * Metoda este separată pentru a izola afișarea poveștii de restul desenării meniului sau jocului.
     * Imaginea este desenată direct pe contextul grafic curent.
     */
    private void DrawStory() {
        if (Assets.storyScreen != null) {
            g.drawImage(Assets.storyScreen, 0, 0, null);
        }
    }

    /*
     * Desenează toate elementele vizibile în timpul gameplay-ului.
     * Metoda ia poziția camerei și zoom-ul, desenează harta, checkpoint-urile, mingile, trenurile,
     * mașinile, peștii, ușa, inamicii și pisica. Tot aici sunt desenate elementele HUD, cum ar fi
     * viețile, scorul, timpul rămas, avertizarea când câinele vede player-ul, mesajele temporare
     * și meniul de setări suprapus peste joc.
     */
    private void DrawGame() {


        // ia poziția curentă a camerei și zoom-ul
        int camX = camera.getX();
        int camY = camera.getY();
        double zoom = camera.getZoom();

        // desenează harta ținând cont de cameră și zoom
        if (gameMap != null) {
            gameMap.draw(g, camX, camY, zoom);
        }

        if (currentLevel == 3) {
            drawCheckpoint(g, checkpoint1, camX, camY, zoom);
            drawCheckpoint(g, checkpoint2, camX, camY, zoom);
        }

        if (currentLevel == 2 && distractionBall != null) {
            distractionBall.draw(g, camX, camY, zoom);
        }

        if (currentLevel == 2 && distractionBall2 != null) {
            distractionBall2.draw(g, camX, camY, zoom);
        }

        if (currentLevel == 3) {
            for (Train t : trains) {
                t.draw(g, camX, camY, zoom);
            }
        }
        if (currentLevel == 3) {
            for (Car car : cars) {
                car.draw(g, camX, camY, zoom);
            }
        }

        // Desenăm peștii prin Handler
        handler.drawFish(g, camX, camY, zoom);

        if (exitDoor != null) {
            exitDoor.draw(g, camX, camY, zoom);
        }

        // Desenăm inamicii prin Handler
        handler.drawEnemies(g, camX, camY, zoom);

        // desenează pisica
        catPlayer.draw(g, camX, camY, zoom);

        if (currentLevel == 1) {
            drawFogOfWar(g, camX, camY, zoom);
        }

        // desenează butonul de settings
        g.setColor(new Color(80, 50, 30));
        g.fillRoundRect(settingsButton.x, settingsButton.y, settingsButton.width, settingsButton.height, 10, 10);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.drawString("≡", settingsButton.x + 8, settingsButton.y + 20);

        if (currentLevel == 1) {
            long elapsedTime = (System.currentTimeMillis() - levelStartTime) / 1000;
            long remainingTime = LEVEL_TIME - elapsedTime;

            if (remainingTime < 0) {
                remainingTime = 0;
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Monospaced", Font.BOLD, 26));

            g.drawString("Scor: " + score + "/100", 20, 30);
            g.drawString("Timp: " + remainingTime + "s", 20, 55);
            int heartSize = 85;
            int heartX = 10;
            int heartY = 40;

            for (int i = 0; i < catPlayer.getVieti(); i++) {
                g.drawImage(Assets.inima, heartX + i * 35, heartY, heartSize, heartSize, null);
            }
        }
        if (currentLevel == 2) {
            int heartSize = 85;
            int heartX = 10;
            int heartY = 10;

            for (int i = 0; i < catPlayer.getVieti(); i++) {
                g.drawImage(Assets.inima, heartX + i * 35, heartY, heartSize, heartSize, null);
            }

            if (handler.isPlayerInEnemyVision() && !catPlayer.isHidden()) {
                String warning = "Te-a vazut cainele! Pleaca in "
                        + handler.getVisionSecondsRemaining()
                        + "s";

                g.setFont(new Font("DialogInput", Font.BOLD, 24));
                FontMetrics fm = g.getFontMetrics();

                g.setColor(new Color(0, 0, 0, 170));
                g.fillRoundRect(15, 55, fm.stringWidth(warning) + 25, 40, 20, 20);

                g.setColor(Color.RED);
                g.drawString(warning, 25, 83);
            }
        }
        if (currentLevel == 3) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("DialogInput", Font.BOLD, 26));

            int heartSize = 85;
            int heartX = 10;
            int heartY = 10;

            for (int i = 0; i < catPlayer.getVieti(); i++) {
                g.drawImage(Assets.inima, heartX + i * 35, heartY, heartSize, heartSize, null);
            }
        }

        if (showLifeLostMessage) {
            long messageTime = System.currentTimeMillis() - lifeLostMessageStartTime;

            if (messageTime <= 2000) {
                String msg = "Ai pierdut o viata!";

                g.setFont(new Font("Monospaced", Font.BOLD, 36)); // mai mare + pixel look
                g.setColor(Color.RED);

                // centrăm textul pe ecran
                FontMetrics fm = g.getFontMetrics();
                int x = (wnd.GetWndWidth() - fm.stringWidth(msg)) / 2;
                int y = wnd.GetWndHeight() / 2;

                g.drawString(msg, x, y);
            } else {
                showLifeLostMessage = false;
            }
        }
        if (showDogDistractedMessage) {
            long t = System.currentTimeMillis() - dogDistractedMessageStartTime;

            if (t <= 2000) {
                String msg = "Cainele a fost distras!";

                g.setFont(new Font("DialogInput", Font.BOLD, 26));
                FontMetrics fm = g.getFontMetrics();

                int x = (wnd.GetWndWidth() - fm.stringWidth(msg)) / 2;
                int y = 120;

                g.setColor(new Color(0, 0, 0, 170));
                g.fillRoundRect(x - 15, y - 30, fm.stringWidth(msg) + 30, 45, 20, 20);

                g.setColor(Color.BLACK);
                g.drawString(msg, x + 2, y + 2);

                g.setColor(Color.YELLOW);
                g.drawString(msg, x, y);
            } else {
                showDogDistractedMessage = false;
            }
        }

        // desenează meniul de settings peste joc dacă este deschis
        if (showSettings && !showGameOver) {
            g.drawImage(Assets.settingsMenu, 150, 80, null);
            settingsUI.draw((Graphics2D) g);
        }



    }

    /*
     * Desenează vizual un checkpoint pe hartă.
     * Coordonatele checkpoint-ului sunt transformate din poziție în lume în poziție pe ecran,
     * folosind camera și zoom-ul. Checkpoint-ul este reprezentat printr-un cerc galben cu contur
     * și litera C, astfel încât jucătorul să poată observa punctele de salvare din nivelul 3.
     */
    private void drawCheckpoint(Graphics g, Rectangle checkpoint, int camX, int camY, double zoom) {

        int x = (int) ((checkpoint.x - camX) * zoom);
        int y = (int) ((checkpoint.y - camY) * zoom);

        int size = (int) (24 * zoom);

        g.setColor(new Color(255, 215, 0));
        g.fillOval(x + 13, y + 13, size, size);

        g.setColor(Color.BLACK);
        g.drawOval(x + 13, y + 13, size, size);

        g.setFont(new Font("DialogInput", Font.BOLD, (int)(12 * zoom)));
        g.drawString("C", x + 20, y + 30);
    }

    /*
     * Desenează ecranul de clasament.
     * Metoda afișează imaginea de fundal pentru leaderboard, citește textul clasamentului din
     * DatabaseManager și îl desenează linie cu linie peste imagine. Astfel, jucătorul poate vedea
     * cele mai bune scoruri salvate în baza de date.
     */
    private void DrawLeaderboard() {
        Graphics2D g2d = (Graphics2D) g;

        // 1. Desenează imaginea PNG de leaderboard
        g2d.drawImage(Assets.leaderboardScreen, 0, 0,
                wnd.GetWndWidth(), wnd.GetWndHeight(), null);

        // 2. Desenează clasamentul peste imagine
        g2d.setColor(new Color(170, 70, 130));
        g2d.setFont(new Font("Monospaced", Font.BOLD, 18));

        String leaderboardText = DatabaseManager.getLeaderboardText();
        String[] lines = leaderboardText.split("\n");

        int y = 155;

        for (String line : lines) {
            g2d.drawString(line, 105, y);
            y += 36;
        }


    }
    /*
     * Desenează ecranul care apare atunci când există un progres salvat pentru numele introdus.
     * Metoda afișează fundalul corespunzător și marchează vizual butoanele Yes/No în funcție de
     * starea mouse-ului: hover sau pressed. Acest ecran permite jucătorului să continue salvarea
     * existentă sau să înceapă un joc nou.
     */
    private void DrawProgressFound() {
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(Assets.progressFoundScreen, 0, 0,
                wnd.GetWndWidth(), wnd.GetWndHeight(), null);

        // HOVER - alb transparent
        g2d.setColor(new Color(255, 255, 255, 45));

        if (hoverProgressYes) {
            g2d.fillRoundRect(
                    progressYesButton.x,
                    progressYesButton.y,
                    progressYesButton.width,
                    progressYesButton.height,
                    70,
                    70
            );
        }

        if (hoverProgressNo) {
            g2d.fillRoundRect(
                    progressNoButton.x,
                    progressNoButton.y,
                    progressNoButton.width,
                    progressNoButton.height,
                    70,
                    70
            );
        }

        // PRESSED - roz transparent
        g2d.setColor(new Color(255, 192, 203, 90));

        if (pressedProgressYes) {
            g2d.fillRoundRect(
                    progressYesButton.x,
                    progressYesButton.y,
                    progressYesButton.width,
                    progressYesButton.height,
                    70,
                    70
            );
        }

        if (pressedProgressNo) {
            g2d.fillRoundRect(
                    progressNoButton.x,
                    progressNoButton.y,
                    progressNoButton.width,
                    progressNoButton.height,
                    70,
                    70
            );
        }
    }

    /*
     * Desenează efectul de întuneric din jurul pisicii pentru nivelul 1.
     * Se creează o zonă neagră care acoperă tot ecranul, apoi se decupează un cerc în jurul player-ului,
     * lăsând vizibilă doar zona apropiată de pisică. Efectul folosește Area și Ellipse2D pentru a obține
     * senzația de vizibilitate limitată, ca un fel de lanternă sau ceață de război.
     */
    private void drawFogOfWar(Graphics g, int camX, int camY, double zoom) {
        Graphics2D g2d = (Graphics2D) g.create();

        int screenW = wnd.GetWndWidth();
        int screenH = wnd.GetWndHeight();

        int catCenterX = (int) ((catPlayer.getX() + catPlayer.getWidth() / 2 - camX) * zoom);
        int catCenterY = (int) ((catPlayer.getY() + catPlayer.getHeight() / 2 - camY) * zoom);

        int radius = (int) (70 * zoom); /* raza cercului vizibil din jurul pisicii */
        Area fog = new Area(new Rectangle(0, 0, screenW, screenH));   /*creeaza o zona neagra cat tot ecranul */
        Area visibleCircle = new Area(new Ellipse2D.Double(  /* creeaza cercul care trebuie sa ramana vizibil in jurul pisicii */
                catCenterX - radius,
                catCenterY - radius,
                radius * 2,
                radius * 2
        ));

        fog.subtract(visibleCircle); /*Scoate cercul din zona neagră.*/

        g2d.setColor(new Color(0, 0, 0, 255));  /*Desenează ceața neagră rămasă pe ecran.*/
        g2d.fill(fog);

        g2d.dispose();
    }

}