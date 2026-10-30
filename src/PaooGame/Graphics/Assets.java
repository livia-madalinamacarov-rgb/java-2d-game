package PaooGame.Graphics;

import java.awt.image.BufferedImage; // Pentru lucrul cu imagini
import PaooGame.Exceptions.AssetLoadException;

/*
 * Clasa Assets are rolul de a incarca si pastra in memorie toate imaginile
 * folosite in joc: fundaluri, meniuri, tile-uri, obiecte, personaje si animatii.
 *
 * Campurile sunt statice pentru ca imaginile sa fie accesibile usor din restul
 * proiectului, fara sa fie nevoie de crearea mai multor obiecte Assets. Metoda
 * Init() trebuie apelata la pornirea jocului, astfel incat toate resursele
 * grafice sa fie pregatite inainte de desenare.
 */
//încărcarea tuturor imaginilor din joc
public class Assets {

    public static BufferedImage playerLeft;
    public static BufferedImage playerRight;
    public static BufferedImage biblioteca;  // bibliotecă (obstacol)
    public static BufferedImage cutie;       // cutie (obstacol)
    public static BufferedImage planta;      // plantă
    public static BufferedImage parchet;     // podea
    public static BufferedImage caramida;    // perete
    public static BufferedImage fish1;
    public static BufferedImage fish2 ;

    public static BufferedImage buturuga;
    public static BufferedImage copac, copac2;
    public static BufferedImage fantana, flori;
    public static BufferedImage gardVertical;
    public static BufferedImage tufis1;
    public static BufferedImage iarbaSimpla, iarbaDecor, gardOrizontal;
    public static BufferedImage pavele, topogan;

    public static BufferedImage locomotiva, sosea;
    public static BufferedImage masinaVerde, masinaRoz, masinaRosie, masinaAlbastra;
    public static BufferedImage sinaTren, trecerePietoni, trotuar;
    public static BufferedImage vagonMov, vagonAlbastru, vagonRosu, vagonPortocaliu;
    public static BufferedImage semafor;

    public static BufferedImage settingsMenu;
    public static BufferedImage storyScreen;
    public static BufferedImage menuBackground;
    public static BufferedImage gameOverScreen;
    public static BufferedImage cat;
    public static BufferedImage ball;

    public static BufferedImage[] dogRight; // 6 cadre mers dreapta
    public static BufferedImage[] dogLeft;  // 6 cadre mers stânga

    //  Animația pisicii (frame-uri pentru fiecare direcție)
    public static BufferedImage[] catDown;
    public static BufferedImage[] catLeft;
    public static BufferedImage[] catRight;
    public static BufferedImage[] catUp;


    public static BufferedImage inima;
    //animatia usii
    public static BufferedImage[] doorAnimation;
    public static BufferedImage usa;
    public static BufferedImage leaderboardScreen;
    public static BufferedImage progressFoundScreen;

    public static BufferedImage winScreen;
    /*
     * Metoda initializeaza toate resursele grafice ale jocului.
     *
     * Incarca imaginile mari din folderul de texturi, creeaza obiecte SpriteSheet
     * pentru imaginile care contin mai multe sprite-uri si decupeaza fiecare
     * element grafic necesar jocului. Aici sunt pregatite tile-urile, pestii,
     * animatiile pisicii, animatia usii, animatiile cainelui, ecranele de meniu
     * si imaginile folosite pentru interfata.
     *
     * Aceasta metoda este apelata o singura data la initializarea jocului, pentru
     * ca toate imaginile sa poata fi folosite ulterior de clasele care deseneaza
     * harta, personajele si meniurile.
     */
    // încarcă toate imaginile
    public static void Init() {

        menuBackground = ImageLoader.LoadImage("/textures/menu.png");
        storyScreen = ImageLoader.LoadImage("/textures/story.png");
        settingsMenu = ImageLoader.LoadImage("/textures/settings.png");
        gameOverScreen = ImageLoader.LoadImage("/textures/game_over.png");
        leaderboardScreen = ImageLoader.LoadImage("/textures/leaderboard_screen.png");
        progressFoundScreen = ImageLoader.LoadImage("/textures/progress_found.png");
        progressFoundScreen = ImageLoader.LoadImage("/textures/progress_found.png");
        winScreen = ImageLoader.LoadImage("/textures/you_win.png");
        SpriteSheet sheet = new SpriteSheet(ImageLoader.LoadImage("/textures/spritesheet_transparent.png")
        );

        SpriteSheet fishSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/fish_spritesheet.png")
        );

        SpriteSheet catSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/spriteFinal.png")
        );

        SpriteSheet doorSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/usaaa.png"));

        SpriteSheet dogSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/catel.png"));

        //  Taie imaginile pentru tile-uri din sprite sheet
        biblioteca = sheet.crop(0, 0);
        cutie = sheet.crop(1, 0);
        planta = sheet.crop(2, 0);
        parchet = sheet.crop(3, 0);
        caramida = sheet.crop(4, 0);
        usa = sheet.crop(5, 0);

        buturuga = sheet.crop(0, 1);
        copac = sheet.crop(1, 1);
        copac2 = sheet.crop(2, 1);
        fantana = sheet.crop(3, 1);
        flori = sheet.crop(4, 1);

        iarbaDecor = sheet.crop(5, 1);
        gardVertical = sheet.crop(0, 2);
        gardOrizontal = sheet.crop(1, 2);
        iarbaSimpla= sheet.crop(2, 2);

        pavele = sheet.crop(3, 2);

        topogan = sheet.crop(4, 2);
        tufis1 = sheet.crop(5, 2);

        locomotiva = sheet.crop(0, 3);
        sosea = sheet.crop(1, 3);
        masinaVerde = sheet.crop(2, 3);
        masinaRoz = sheet.crop(3, 3);
        masinaRosie = sheet.crop(4, 3);
        masinaAlbastra = sheet.crop(5, 3);

        sinaTren = sheet.crop(0, 4);
        trecerePietoni = sheet.crop(1, 4);
        trotuar = sheet.crop(2, 4);
        vagonMov = sheet.crop(3, 4);
        vagonAlbastru = sheet.crop(4, 4);
        vagonRosu = sheet.crop(5, 4);

        vagonPortocaliu = sheet.crop(0, 5);
        semafor = sheet.crop(1, 5);
        inima=sheet.crop(2,5);
        ball=sheet.crop(3,5);

        fish1 = fishSheet.crop(0, 0);
        fish2 = fishSheet.crop(1, 0);

        //animatia pisicii
        catDown = new BufferedImage[4]; // 4 frame-uri
        catDown[0] = catSheet.crop(0, 0);
        catDown[1] = catSheet.crop(1, 0);
        catDown[2] = catSheet.crop(2, 0);
        catDown[3] = catSheet.crop(3, 0);

        catUp = new BufferedImage[4];
        catUp[0] = catSheet.crop(0, 1);
        catUp[1] = catSheet.crop(1, 1);
        catUp[2] = catSheet.crop(2, 1);
        catUp[3] = catSheet.crop(3, 1);

        catRight = new BufferedImage[4];
        catRight[0] = catSheet.crop(0, 2);
        catRight[1] = catSheet.crop(1, 2);
        catRight[2] = catSheet.crop(2, 2);
        catRight[3] = catSheet.crop(3, 2);

        catLeft = new BufferedImage[4];

        catLeft[0] = catSheet.crop(1, 3);
        catLeft[1] = catSheet.crop(2, 3);
        catLeft[2] = catSheet.crop(3, 3);
        catLeft[3] = catSheet.crop(0, 4);

        //4 cadre de animatie pt usa
        doorAnimation = new BufferedImage[3];
        doorAnimation[0] = doorSheet.crop(0, 0); // Ușa Închisă
        doorAnimation[1] = doorSheet.crop(1, 0); // Ușa în curs de Deschidere
        doorAnimation[2] = doorSheet.crop(2, 0); // Ușa complet Deschisă

        int frameW = 50; // lățime cadru
        int frameH = 50; // înălțime cadru

        // Inițializăm array-urile cu 6 elemente fiecare
        dogRight = new BufferedImage[6];
        dogLeft = new BufferedImage[6];

        // Decupăm rândul de sus (y=0) -> Mers Dreapta
        for(int i = 0; i < 6; i++) {
            // Folosim doar (coloana i, rândul 0)
            dogRight[i] = dogSheet.crop(i, 0);
        }

        // Decupăm rândul de jos (y=1) -> Mers Stânga
        for(int i = 0; i < 6; i++) {
            // Folosim doar (coloana i, rândul 1)
            dogLeft[i] = dogSheet.crop(i, 1);
        }

    }
}