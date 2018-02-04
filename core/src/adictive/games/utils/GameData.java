package adictive.games.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameData {
    private static final String PREFS_NAME = "GameData";
    private static final String DATA_LEVEL = "level";

    public static final String JOYSTICK_POSITION = "joystickPosition";

    public static int getCurrentLevel() {

        final Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        return prefs.getInteger(DATA_LEVEL, 1);
    }

    public static int incrementCurrentLevel() {
        final Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        int level = prefs.getInteger(DATA_LEVEL, 1) + 1;
        prefs.putInteger(DATA_LEVEL, level);
        prefs.flush();
        return level;
    }

    public static int decrementCurrentLevel() {
        final Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        int level = Math.max(prefs.getInteger(DATA_LEVEL, 1) - 1, 1);
        prefs.putInteger(DATA_LEVEL, level);
        prefs.flush();
        return level;
    }

    public static int getJoystickPosition() {
        return Gdx.app.getPreferences(PREFS_NAME).getInteger(JOYSTICK_POSITION, 0);
    }

    public static void setJoystickPosition(int position) {
        final Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        prefs.putInteger(JOYSTICK_POSITION, position);
        prefs.flush();
    }
}
