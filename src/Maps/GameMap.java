package Maps;

import PaooGame.Tiles.Tile;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import PaooGame.Exceptions.MapLoadException;

/*! \class GameMap
    \brief Gestionează harta jocului.

    Clasa citește harta dintr-un fișier text, o salvează într-o matrice de tile-uri,
    o desenează pe ecran și oferă metode pentru verificarea coliziunilor și a tufișurilor.
 */
public class GameMap {

    /*! \brief Dimensiunea unui tile în pixeli. */
    private static final int TILE_DRAW_SIZE = 50;

    /*! \brief Matricea care reține valorile tile-urilor din hartă. */
    private int[][] map;

    /*! \fn public GameMap(String path)
        \brief Constructorul clasei GameMap.

        Primește calea către fișierul hărții și apelează metoda de încărcare.

        \param path Calea către fișierul text al hărții.
     */
    public GameMap(String path) {
        loadMap(path);
    }

    /*! \fn private void loadMap(String path)
        \brief Încarcă harta dintr-un fișier text.

        Citește fișierul linie cu linie, ignoră liniile goale,
        transformă valorile text în numere întregi și le salvează în matricea hărții.

        \param path Calea către fișierul hărții.
        \throws MapLoadException Dacă fișierul nu este găsit sau nu poate fi citit corect.
     */
    private void loadMap(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);

            if (is == null) {
                throw new MapLoadException("Nu gasesc harta: " + path);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            List<String> lines = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }

            int rows = lines.size();
            int cols = lines.get(0).trim().split("\\s+").length;

            map = new int[rows][cols];

            for (int y = 0; y < rows; y++) {
                String[] values = lines.get(y).trim().split("\\s+");

                for (int x = 0; x < cols; x++) {
                    map[y][x] = Integer.parseInt(values[x]);
                }
            }

        } catch (Exception e) {
            throw new MapLoadException("Eroare la incarcarea hartii: " + path, e);
        }
    }

    /*! \fn public void draw(Graphics g, int camX, int camY, double zoom)
        \brief Desenează harta pe ecran.

        Parcurge matricea hărții și desenează tile-ul corespunzător fiecărei valori,
        ținând cont de poziția camerei și de nivelul de zoom.

        \param g Contextul grafic folosit pentru desenare.
        \param camX Coordonata X a camerei.
        \param camY Coordonata Y a camerei.
        \param zoom Nivelul de zoom aplicat la desenare.
     */
    public void draw(Graphics g, int camX, int camY, double zoom) {
        if (map == null) return;

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {

                int tile = map[y][x];

                int drawX = (int)((x * TILE_DRAW_SIZE - camX) * zoom);
                int drawY = (int)((y * TILE_DRAW_SIZE - camY) * zoom);
                int drawSize = (int)(TILE_DRAW_SIZE * zoom);

                if (tile == 4) {
                    Tile.caramidaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 3) {
                    Tile.parchetTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 0) {
                    Tile.parchetTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.bibliotecaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 1) {
                    Tile.parchetTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.cutieTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 2) {
                    Tile.parchetTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.plantaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 5) {
                    Tile.caramidaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.usaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 6) {
                    Tile.iarbaSimplaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.copacTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 7) {
                    Tile.iarbaSimplaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.fantanaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 8) {
                    Tile.floriTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 9) {
                    Tile.iarbaSimplaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.gardVerticalTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 10) {
                    Tile.iarbaSimplaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.gardOrizontalTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 11) {
                    Tile.tufis1Tile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 12) {
                    Tile.iarbaSimplaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 13) {
                    Tile.iarbaDecorTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 14) {
                    Tile.paveleTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 15) {
                    Tile.iarbaSimplaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.copac2Tile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 16) {
                    Tile.iarbaSimplaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.buturugaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 17) {
                    Tile.iarbaSimplaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.topoganTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 18) {
                    Tile.paveleTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.sinaTrenTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.locomotivaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 19) {
                    Tile.soseaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 20) {
                    Tile.soseaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.masinaVerdeTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 21) {
                    Tile.soseaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.masinaRozTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 22) {
                    Tile.soseaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.masinaRosieTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 23) {
                    Tile.soseaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.masinaAlbastraTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 24) {
                    Tile.paveleTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.sinaTrenTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 25) {
                    Tile.trecerePietoniTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 26) {
                    Tile.trotuarTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 27) {
                    Tile.paveleTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.sinaTrenTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.vagonMovTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 28) {
                    Tile.paveleTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.sinaTrenTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.vagonAlbastruTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 29) {
                    Tile.paveleTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.sinaTrenTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.vagonRosuTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 30) {
                    Tile.paveleTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.sinaTrenTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.vagonPortocaliuTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
                else if (tile == 31) {
                    Tile.iarbaSimplaTile.Draw(g, drawX, drawY, drawSize, drawSize);
                    Tile.semaforTile.Draw(g, drawX, drawY, drawSize, drawSize);
                }
            }
        }
    }

    /*! \fn public boolean isSolidAt(int pixelX, int pixelY)
        \brief Verifică dacă o poziție este blocată.

        Transformă coordonatele din pixeli în coordonate de tile și verifică
        dacă tile-ul respectiv este solid.

        \param pixelX Coordonata X în pixeli.
        \param pixelY Coordonata Y în pixeli.
        \return true dacă poziția este blocată, false altfel.
     */
    public boolean isSolidAt(int pixelX, int pixelY) {
        if (map == null) {
            return true;
        }

        int tileX = pixelX / TILE_DRAW_SIZE;
        int tileY = pixelY / TILE_DRAW_SIZE;

        if (tileY < 0 || tileY >= map.length || tileX < 0 || tileX >= map[0].length) {
            return true;
        }

        int tile = map[tileY][tileX];

        return tile == 4 || tile == 0 || tile == 1 || tile == 2 || tile == 5 || tile == 6 || tile==7 || tile == 9 || tile == 10 || tile == 15 ||tile == 18 || tile == 20 || tile == 21 || tile == 22 || tile == 23 ||
                tile == 27 || tile == 28 || tile == 29 || tile == 30 || tile == 31 || tile==16;
    }

    /*! \fn public int getWidth()
        \brief Returnează lățimea hărții.

        \return Numărul de coloane ale hărții.
     */
    public int getWidth() {
        return map[0].length;
    }

    /*! \fn public int getHeight()
        \brief Returnează înălțimea hărții.

        \return Numărul de rânduri ale hărții.
     */
    public int getHeight() {
        return map.length;
    }

    /*! \fn public boolean isBushAt(int pixelX, int pixelY)
        \brief Verifică dacă la poziția dată există un tufiș.

        Transformă coordonatele din pixeli în coordonate de tile-uri și verifică
        dacă tile-ul corespunzător este de tip tufiș.

        \param pixelX Coordonata X în pixeli.
        \param pixelY Coordonata Y în pixeli.
        \return true dacă poziția este pe un tufiș, false altfel.
     */
    public boolean isBushAt(int pixelX, int pixelY) {
        if (map == null) {
            return false;
        }

        int tileX = pixelX / TILE_DRAW_SIZE;
        int tileY = pixelY / TILE_DRAW_SIZE;

        if (tileY < 0 || tileY >= map.length || tileX < 0 || tileX >= map[0].length) {
            return false;
        }

        int tile = map[tileY][tileX];

        return tile == 11;
    }
}