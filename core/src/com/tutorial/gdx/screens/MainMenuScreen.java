package com.tutorial.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.tutorial.gdx.SpaceGame;
import com.tutorial.gdx.tools.ScrollingBackground;

public class MainMenuScreen implements Screen {
    private static final int EXIT_BUTTON_WIDTH = 250;
    private static final int EXIT_BUTTON_HEIGHT = 120;
    private static final int PLAY_BUTTON_WIDTH = 300;
    private static final int PLAY_BUTTON_HEIGHT = 120;
    private static final int EXIT_BUTTON_Y = 100;
    private static final int PLAY_BUTTON_Y = 230;
    private static final int LOGO_WIDTH = 400;
    private static final int LOGO_HEIGHT = 250;
    private static final int LOGO_Y = 450;

    final SpaceGame GAME;

    Texture playButtonActive;
    Texture playButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture logo;

    public MainMenuScreen(final SpaceGame GAME) {
        this.GAME = GAME;

        playButtonActive = new Texture("play_button_active.png");
        playButtonInactive = new Texture("play_button_inactive.png");
        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");
        logo = new Texture("logo.png");

        GAME.scrollingBackground.setSpeedFixed(true);
        GAME.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);

        final MainMenuScreen mainMenuScreen = this;

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                int x = SpaceGame.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;

                if (    GAME.camera.getInputInGameWorld().x < x + EXIT_BUTTON_WIDTH &&
                        GAME.camera.getInputInGameWorld().x > x &&
                        SpaceGame.HEIGHT - GAME.camera.getInputInGameWorld().y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT &&
                        SpaceGame.HEIGHT - GAME.camera.getInputInGameWorld().y > EXIT_BUTTON_Y) {
                    mainMenuScreen.dispose();
                    Gdx.app.exit();
                }

                x = SpaceGame.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2;
                if (GAME.camera.getInputInGameWorld().x < x + PLAY_BUTTON_WIDTH &&
                        GAME.camera.getInputInGameWorld().x > x &&
                        SpaceGame.HEIGHT - GAME.camera.getInputInGameWorld().y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT &&
                        SpaceGame.HEIGHT - GAME.camera.getInputInGameWorld().y > PLAY_BUTTON_Y) {
                    mainMenuScreen.dispose();
                    GAME.setScreen(new MainGameScreen(GAME));
                }

                return super.touchUp(screenX, screenY, pointer, button);
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GAME.batch.begin();

        GAME.scrollingBackground.updateAndRender(delta, GAME.batch);

        int x = SpaceGame.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;
        if (    GAME.camera.getInputInGameWorld().x < x + EXIT_BUTTON_WIDTH &&
                GAME.camera.getInputInGameWorld().x > x &&
                SpaceGame.HEIGHT - GAME.camera.getInputInGameWorld().y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT &&
                SpaceGame.HEIGHT - GAME.camera.getInputInGameWorld().y > EXIT_BUTTON_Y)
            GAME.batch.draw(exitButtonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        else
            GAME.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);

        x = SpaceGame.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2;
        if (    GAME.camera.getInputInGameWorld().x < x + PLAY_BUTTON_WIDTH &&
                GAME.camera.getInputInGameWorld().x > x &&
                SpaceGame.HEIGHT - GAME.camera.getInputInGameWorld().y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT &&
                SpaceGame.HEIGHT - GAME.camera.getInputInGameWorld().y > PLAY_BUTTON_Y)
            GAME.batch.draw(playButtonActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        else
            GAME.batch.draw(playButtonInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);

        GAME.batch.draw(logo, SpaceGame.WIDTH / 2f - LOGO_WIDTH / 2f, LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT);

        GAME.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }
}
