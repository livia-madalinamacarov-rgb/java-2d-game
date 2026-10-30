package PaooGame.Tiles;
import PaooGame.Graphics.Assets;
import java.awt.*;
import java.awt.image.BufferedImage;

/*
 * Clasa Tile reprezinta un tip de dala din harta jocului.
 *
 * Fiecare tile are o imagine, un id unic si metode pentru desenare, actualizare
 * si verificarea proprietatii de soliditate. Clasa pastreaza toate tipurile de
 * tile-uri intr-un vector static, astfel incat ele pot fi accesate rapid dupa id
 * atunci cand harta este desenata sau cand se verifica anumite tipuri de teren.
 */
/*! \class public class Tile
    \brief Retine toate dalele intr-un vector si ofera posibilitatea regasirii dupa un id.
 */
public class Tile
{
    private static final int NO_TILES   = 32;
    public static Tile[] tiles          = new Tile[NO_TILES];       /*!< Vector de referinte de tipuri de dale.*/

    /// De remarcat ca urmatoarele dale sunt statice si publice. Acest lucru imi permite sa le am incarcate
    /// o singura data in memorie

    public static Tile bibliotecaTile = new BibliotecaTile(0);
    public static Tile cutieTile = new CutieTile(1);
    public static Tile plantaTile = new PlantaTile(2);
    public static Tile parchetTile = new ParchetTile(3);
    public static Tile caramidaTile = new CaramidaTile(4);
    public static Tile usaTile = new UsaTile(5);

    public static Tile copacTile = new CopacTile(6);
    public static Tile fantanaTile = new FantanaTile(7);
    public static Tile floriTile = new FloriTile(8);
    public static Tile gardVerticalTile = new GardTile(Assets.gardVertical, 9);
    public static Tile gardOrizontalTile = new GardTile(Assets.gardOrizontal, 10);

    public static Tile tufis1Tile = new Tufis1Tile(11);
    public static Tile iarbaSimplaTile = new IarbaSimplaTile(12);
    public static Tile iarbaDecorTile = new IarbaDecorTile(13);
    public static Tile paveleTile = new PaveleTile(14);
    public static Tile copac2Tile = new Copac2Tile(15);
    public static Tile buturugaTile = new ButurugaTile(16);
    public static Tile topoganTile = new TopoganTile(17);

    public static Tile locomotivaTile = new Locomotiva(18);
    public static Tile soseaTile = new Sosea(19);

    public static Tile masinaVerdeTile = new MasinaTile(Assets.masinaVerde, 20);
    public static Tile masinaRozTile = new MasinaTile(Assets.masinaRoz, 21);
    public static Tile masinaRosieTile = new MasinaTile(Assets.masinaRosie, 22);
    public static Tile masinaAlbastraTile = new MasinaTile(Assets.masinaAlbastra, 23);

    public static Tile sinaTrenTile = new SineTile(24);
    public static Tile trecerePietoniTile = new TrecerePietoni(25);
    public static Tile trotuarTile = new TrotuarTile(26);

    public static Tile vagonMovTile = new VagonTile(Assets.vagonMov, 27);
    public static Tile vagonAlbastruTile = new VagonTile(Assets.vagonAlbastru, 28);
    public static Tile vagonRosuTile = new VagonTile(Assets.vagonRosu, 29);
    public static Tile vagonPortocaliuTile = new VagonTile(Assets.vagonPortocaliu, 30);

    public static Tile semaforTile = new SemaforTile(31);
    public static final int TILE_WIDTH  = 48;                       /*!< Latimea unei dale.*/
    public static final int TILE_HEIGHT = 48;                       /*!< Inaltimea unei dale.*/

    protected BufferedImage img;                                    /*!< Imaginea aferenta tipului de dala.*/
    protected final int id;                                         /*!< Id-ul unic aferent tipului de dala.*/

    /*! \fn public Tile(BufferedImage texture, int id)
        \brief Constructorul aferent clasei.

        \param image Imaginea corespunzatoare dalei.
        \param id Id-ul dalei.
     */

    /*
     * Constructorul creeaza un tile nou, asociindu-i imaginea primita si id-ul
     * unic. Imaginea este folosita la desenarea dalei, iar id-ul permite
     * identificarea ei in matricea hartii.
     *
     * La final, tile-ul este salvat in vectorul static tiles, pe pozitia id-ului
     * sau, pentru a putea fi accesat ulterior de restul jocului.
     */
    public Tile(BufferedImage image, int idd)
    {
        img = image;
        id = idd;

        tiles[id] = this;
    }

    /*! \fn public void Update()
        \brief Actualizeaza proprietatile dalei.
     */
    /*
     * Metoda este destinata actualizarii proprietatilor unui tile.
     *
     * In forma actuala nu executa nicio actiune, deoarece tile-urile sunt statice
     * si nu isi schimba starea de la un frame la altul. Metoda ramane totusi
     * disponibila pentru extinderi viitoare, de exemplu tile-uri animate.
     */
    public void Update()
    {

    }

    /*! \fn public void Draw(Graphics g, int x, int y)
        \brief Deseneaza in fereastra dala.

        \param g Contextul grafic in care sa se realizeze desenarea
        \param x Coordonata x in cadrul ferestrei unde sa fie desenata dala
        \param y Coordonata y in cadrul ferestrei unde sa fie desenata dala
     */
    /*
     * Metoda deseneaza imaginea tile-ului pe ecran.
     *
     * Primeste contextul grafic, pozitia la care trebuie desenata dala si
     * dimensiunea finala. Dimensiunea poate fi diferita de dimensiunea originala
     * a imaginii, deoarece jocul foloseste zoom si pozitionare in functie de
     * camera.
     */
    public void Draw(Graphics g, int x, int y, int width, int height) {
        g.drawImage(img, x, y, width, height, null);
    }

    /*! \fn public boolean IsSolid()
        \brief Returneaza proprietatea de dala solida (supusa coliziunilor) sau nu.
     */
    /*
     * Metoda indica daca tile-ul este solid, adica daca poate bloca miscarea
     * personajului sau a altor entitati.
     *
     * In clasa de baza returneaza false, iar tipurile speciale de tile pot
     * suprascrie aceasta metoda daca trebuie sa fie considerate obstacole.
     */
    public boolean IsSolid()
    {
        return false;
    }

    /*! \fn public int GetId()
        \brief Returneaza id-ul dalei.
     */
    /*
     * Metoda returneaza id-ul unic al tile-ului.
     *
     * Acest id este folosit pentru identificarea dalei in harta si pentru
     * accesarea tipului corect de tile din vectorul static tiles.
     */
    public int GetId()
    {
        return id;
    }
}