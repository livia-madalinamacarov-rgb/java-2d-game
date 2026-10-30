
package PaooGame.Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import PaooGame.Exceptions.AssetLoadException;
import java.net.URL;
import static java.lang.System.exit;

/*
 * Clasa ImageLoader este folosita pentru incarcarea imaginilor din folderul
 * de resurse al jocului.
 *
 * Ea contine o metoda statica, astfel incat imaginile pot fi incarcate usor
 * fara a crea obiecte ImageLoader. Imaginile incarcate sunt returnate sub forma
 * de BufferedImage si sunt folosite ulterior de clase precum Assets, Tile,
 * Cat, Dog sau alte elemente grafice din joc.
 */
/*! \class public class ImageLoader
    \brief Clasa ce contine o metoda statica pentru incarcarea unei imagini in memorie.
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import PaooGame.Exceptions.AssetLoadException;

public class ImageLoader {
    /*
     * Metoda incarca o imagine din resursele proiectului, folosind calea primita
     * ca parametru.
     *
     * Daca imaginea este gasita si poate fi citita, metoda returneaza obiectul
     * BufferedImage corespunzator. Daca apare o problema la citire, exceptia este
     * afisata in consola, iar metoda intoarce null.
     *
     * Aceasta metoda centralizeaza incarcarea imaginilor, astfel incat restul
     * jocului sa nu repete codul de citire pentru fiecare textura in parte.
     */
    public static BufferedImage LoadImage(String path) throws AssetLoadException {
        try {
            return ImageIO.read(ImageLoader.class.getResource(path));
        } catch (IOException e) {
            throw new AssetLoadException("Nu s-a putut incarca imaginea: " + path, e);
        }

    }
}


//    public static BufferedImage LoadImage(String path)

//    {
//        /// Avand in vedere exista situatii in care fisierul sursa sa nu poate fi accesat
//        /// metoda read() arunca o excpetie ce trebuie tratata
//        try
//        {
//            /// Clasa ImageIO contine o serie de metode statice pentru file IO.
//            /// Metoda read() are ca argument un InputStream construit avand ca referinta
//            /// directorul res, director declarat ca director de resurse in care se gasesc resursele
//            /// proiectului sub forma de fisiere sursa.
//            return ImageIO.read(ImageLoader.class.getResource(path));
//        }
//        catch(IOException e)
//        {
//            /// Afiseaza informatiile necesare depanarii.
//            e.printStackTrace();
//        }
//        return null;
//    }}
