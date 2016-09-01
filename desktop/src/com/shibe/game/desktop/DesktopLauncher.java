package com.shibe.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.shibe.game.Managers.Game;

class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Shibe";
		config.width = 1920;
		config.height = 1080;
		new LwjglApplication(new Game(), config);
	}
}
