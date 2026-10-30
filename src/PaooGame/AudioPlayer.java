package PaooGame;

import PaooGame.Exceptions.AudioException;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/*
 * Clasa AudioPlayer se ocupă de redarea sunetelor din joc.
 * Ea folosește modelul Singleton, astfel încât în tot jocul să existe o singură instanță care controlează muzica de fundal, efectele sonore și volumul.
 */
public class AudioPlayer {

    private Clip musicClip;
    private Clip soundClip;
    private FloatControl volume;
    private static AudioPlayer instance;
    private String currentMusicPath = "";

    /*
     * Constructor privat folosit pentru Singleton.
     * Nu permite crearea directă a obiectelor AudioPlayer din alte clase, obligând folosirea metodei getInstance().
     */
    private AudioPlayer(){}


    /*
     * Returnează instanța unică a clasei AudioPlayer.
     * Dacă instanța nu există încă, aceasta este creată, iar apoi aceeași instanță este reutilizată în tot jocul.
     */
    public static synchronized AudioPlayer getInstance()
    {
        if(instance==null)
        {

            instance=new AudioPlayer();
        }
        return instance
                ;

    }



    /*
     * Pornește redarea unui fișier audio primit prin cale.
     * Dacă parametrul loop este true, sunetul este tratat ca muzică de fundal și se repetă continuu; altfel este redat ca efect sonor scurt, fără să oprească muzica deja pornită.
     */
    public synchronized void play(String path, boolean loop)
    {
        try {
            URL url = getClass().getResource(path);

            if (url == null) {
                throw new AudioException("Nu am gasit fisierul audio: " + path);
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);


            if (loop) {
                if (musicClip != null && musicClip.isRunning() && path.equals(currentMusicPath)) {
                    return;
                }

                if (musicClip != null) {
                    musicClip.stop();
                    musicClip.close();
                }

                musicClip = AudioSystem.getClip();
                musicClip.open(audioInputStream);

                volume = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-20.0f);

                currentMusicPath = path;

                musicClip.loop(Clip.LOOP_CONTINUOUSLY);
                musicClip.start();

            } else {

                soundClip = AudioSystem.getClip();
                soundClip.open(audioInputStream);

                FloatControl soundVolume =
                        (FloatControl) soundClip.getControl(FloatControl.Type.MASTER_GAIN);
                soundVolume.setValue(-15.0f);

                soundClip.start();
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new AudioException("Nu s-a putut reda fisierul audio: " + path, e);
        }
    }


    /*
     * Oprește muzica de fundal care rulează în acel moment.
     * Închide clipul audio și resetează informația despre melodia curentă.
     */
    public synchronized void stop() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip.close();
            musicClip = null;
            currentMusicPath = "";
        }
    }

    /*
     * Mărește volumul muzicii de fundal cu un pas fix.
     * Verifică limita maximă permisă de FloatControl pentru a nu depăși valoarea acceptată.
     */
    public void volumeUp() {
        if (volume != null) {
            float current = volume.getValue();
            float max = volume.getMaximum();

            float newValue = current + 5;

            if (newValue > max) {
                newValue = max;
            }

            volume.setValue(newValue);
        }
    }

    /*
     * Micșorează volumul muzicii de fundal cu un pas fix.
     * Verifică limita minimă permisă de FloatControl pentru a nu coborî sub valoarea acceptată.
     */
    public void volumeDown() {
        if (volume != null) {
            float current = volume.getValue();
            float min = volume.getMinimum();

            float newValue = current - 5;

            if (newValue < min) {
                newValue = min;
            }

            volume.setValue(newValue);
        }
    }
}


