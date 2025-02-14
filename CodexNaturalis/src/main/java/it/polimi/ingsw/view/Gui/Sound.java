package it.polimi.ingsw.view.Gui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Utility class for playing sound from resources.
 */
public class Sound {

    /**
     * Plays a sound from the specified resource path.
     *
     * @param soundName the name of the sound file to play (relative path from "/sound/")
     */
    public static synchronized void playSound(final String soundName) {
            /*
            new Thread(() -> {
                try {
                    Clip clip = AudioSystem.getClip();


                    InputStream in = Sound.class.getResourceAsStream("/sound/"+ soundName);


                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while (true) {
                        assert in != null;
                        if ((bytesRead = in.read(buffer)) == -1) break;
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    byte[] audioData = outputStream.toByteArray();

                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioData);


                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(byteArrayInputStream);


                    clip.open(inputStream);
                    FloatControl gainControl =
                            (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(-10.0f);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }).start();
            */
    }


}
