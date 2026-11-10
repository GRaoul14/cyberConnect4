package audio;

import java.net.URL;

import javafx.scene.media.AudioClip;

public class SoundManager {

    private static AudioClip buttonSound;
    private static AudioClip dropSound;
    private static AudioClip victorySound;

    static {
        buttonSound = loadSound("/sounds/button.mp3");
        dropSound = loadSound("/sounds/drop.mp3");
        victorySound = loadSound("/sounds/victory.mp3");
    }

    private static AudioClip loadSound(String path) {

        URL resource = SoundManager.class.getResource(path);

        if (resource == null) {
            System.err.println("Audio not found : " + path);
            return null;
        }

        return new AudioClip(resource.toExternalForm());
    }

    public static void playButton() {
        play(buttonSound, 0.4);
    }

	public static void playDrop() {
		dropSound.stop();
		play(dropSound, 0.6);

	}

    public static void playVictory() {
        play(victorySound, 0.5);
    }

    private static void play(AudioClip sound, double volume) {

        if (sound != null) {
            sound.play(volume);
        }
    }
}