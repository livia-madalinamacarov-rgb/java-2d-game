
package PaooGame.Graphics;

import java.awt.image.BufferedImage;// pt a lucra cu imagini in Java

/*
 * Clasa SpriteSheet reprezinta o imagine mare care contine mai multe sprite-uri
 * asezate intr-o grila.
 *
 * In loc ca fiecare textura sa fie incarcata dintr-un fisier separat, jocul
 * foloseste sprite sheet-uri, iar aceasta clasa permite extragerea unei singure
 * imagini mici pe baza coordonatelor din grila. Astfel, clasele precum Assets
 * pot obtine rapid imaginile pentru tile-uri, personaje, obiecte si animatii.
 */
/*! \class public class SpriteSheet
    \brief Clasa retine o referinta catre o imagine formata din dale (sprite sheet)

    Metoda crop() returneaza o dala de dimensiuni fixe (o subimagine) din sprite sheet
    de la adresa (x * latimeDala, y * inaltimeDala)
 */
public class SpriteSheet

{
    private BufferedImage       spriteSheet; // Referinta catre obiectul BufferedImage ce contine sprite sheet-ul.
    private static final int    tileWidth   = 48;   /*!< Latimea unei dale din sprite sheet.*/
    private static final int    tileHeight  = 48;   /*!< Inaltime unei dale din sprite sheet.*/


    /*
     * Constructorul primeste imaginea completa a sprite sheet-ului si o salveaza
     * in variabila membra a clasei.
     *
     * Imaginea primita va fi folosita mai tarziu de metoda crop(), care extrage
     * subimagini individuale din ea. Constructorul nu modifica imaginea, ci doar
     * pastreaza referinta catre aceasta.
     */
    public SpriteSheet(BufferedImage buffImg)

    {
        /// Retine referinta catre BufferedImage object.
        spriteSheet = buffImg;
    }

    /*
     * Metoda extrage o subimagine din sprite sheet, pe baza pozitiei din grila.
     *
     * Parametrii x si y nu reprezinta pixeli direct, ci pozitia sprite-ului in
     * grila: coloana x si randul y. Metoda inmulteste aceste valori cu latimea
     * si inaltimea unei dale pentru a afla coordonatele reale in pixeli.
     *
     * Rezultatul este un BufferedImage care contine doar sprite-ul dorit si care
     * poate fi folosit ulterior la desenare sau animatie.
     */
    /*! \fn public BufferedImage crop(int x, int y)
        \brief Returneaza un obiect BufferedImage ce contine o subimage (dala).

        Subimaginea este localizata avand ca referinta punctul din stanga sus.

        \param x numarul dalei din sprite sheet pe axa x.
        \param y numarul dalei din sprite sheet pe axa y.
     */
    public  BufferedImage crop(int x, int y)
    {
        /// Subimaginea (dala) este regasita in sprite sheet specificad coltul stanga sus
        /// al imaginii si apoi latimea si inaltimea (totul in pixeli). Coltul din stanga sus al imaginii
        /// se obtine inmultind numarul de ordine al dalei cu dimensiunea in pixeli a unei dale.
        return spriteSheet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
    }
}
