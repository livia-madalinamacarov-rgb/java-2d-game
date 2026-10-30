package PaooGame;


/*
 * Clasa Camera controlează zona din hartă care este vizibilă pe ecran.
 * Camera urmărește pisica, aplică zoom și se asigură că imaginea nu iese în afara limitelor hărții.
 */
public class Camera {


    private int x;
    private int y;
    private double zoom;

    /*
     * Constructorul setează poziția inițială a camerei și nivelul de zoom.
     * La început, camera pornește din colțul stânga-sus al hărții.
     */
    public Camera() {
        x = 0;
        y = 0;
        zoom = 1.5;
    }


    /*
     * Actualizează poziția camerei în funcție de poziția pisicii.
     * Încearcă să centreze pisica pe ecran, apoi limitează camera astfel încât să nu afișeze zone din afara hărții.
     */
    public void update(Cat cat, int screenWidth, int screenHeight, int mapWidth, int mapHeight) {


        x = (int)(cat.getX() - screenWidth / (2 * zoom));
        y = (int)(cat.getY() - screenHeight / (2 * zoom));


        int maxX = (int)(mapWidth - screenWidth / zoom);
        int maxY = (int)(mapHeight - screenHeight / zoom);


        if (x < 0) x = 0;
        if (y < 0) y = 0;


        if (x > maxX) x = maxX;
        if (y > maxY) y = maxY;
    }

    /*
     * Returnează coordonata X a camerei.
     * Această valoare este folosită la desenarea obiectelor pentru a calcula poziția lor pe ecran.
     */
    public int getX()
    {
        return x;

    }

    /*
     * Returnează coordonata Y a camerei.
     * Această valoare indică deplasarea verticală a camerei pe hartă.
     */
    public int getY() {
        return y;
    }

    /*
     * Returnează valoarea curentă a zoom-ului.
     * Zoom-ul influențează cât de mari apar obiectele și harta pe ecran.
     */
    public double getZoom() {
        return zoom;
    }

    /*
     * Setează o nouă valoare pentru zoom.
     * Metoda permite apropierea sau depărtarea vizuală față de hartă.
     */
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

}