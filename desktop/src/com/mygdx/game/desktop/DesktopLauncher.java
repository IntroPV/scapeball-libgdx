package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import adictive.games.MainGame;
import adictive.games.analytics.IAnalyticsManager;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new MainGame(new IAnalyticsManager() {
            @Override
			public void winLevel(int level) {
				System.out.println("Passed level " +  level);
			}

			@Override
			public void loseLevel(int level) {
				System.out.println("Lost level " +  level);
			}
		}), config);
	}
}
