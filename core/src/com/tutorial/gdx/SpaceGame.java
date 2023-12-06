package com.tutorial.gdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tutorial.gdx.screens.MainMenuScreen;
import com.tutorial.gdx.tools.GameCamera;
import com.tutorial.gdx.tools.ScrollingBackground;

public class SpaceGame extends Game {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 720;

	public SpriteBatch batch;
	public ScrollingBackground scrollingBackground;
	public GameCamera camera;

	@Override
	public void create() {
		batch = new SpriteBatch();
		camera = new GameCamera(WIDTH, HEIGHT);

		scrollingBackground = new ScrollingBackground();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		batch.setProjectionMatrix(camera.combined());
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		camera.update(width, height);
		super.resize(width, height);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
