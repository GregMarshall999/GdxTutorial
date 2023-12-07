package com.tutorial.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.tutorial.gdx.SpaceGame;
import com.tutorial.gdx.tools.ScrollingBackground;

public class GameOverScreen implements Screen {
    private static final int BANNER_WIDTH = 350;
    private static final int BANNER_HEIGHT = 100;

    private final SpaceGame GAME;
    private final int SCORE, HIGH_SCORE;
    private final Texture GAME_OVER_BANNER;
    private final BitmapFont SCORE_FONT;

    public GameOverScreen(SpaceGame game, int score) {
        this.GAME = game;
        this.SCORE = score;

        Preferences prefs = Gdx.app.getPreferences("spacegame");
        this.HIGH_SCORE = prefs.getInteger("highscore", 0);

        if (score > HIGH_SCORE) {
            prefs.putInteger("highscore", score);
            prefs.flush();
        }

        GAME_OVER_BANNER = new Texture("game_over.png");
        SCORE_FONT = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));

        game.scrollingBackground.setSpeedFixed(true);
        game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
    }

    @Override
    public void show () {

    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GAME.batch.begin();

        GAME.scrollingBackground.updateAndRender(delta, GAME.batch);

        GAME.batch.draw(GAME_OVER_BANNER,
                SpaceGame.WIDTH / 2f - BANNER_WIDTH / 2f,
                SpaceGame.HEIGHT - BANNER_HEIGHT - 15,
                BANNER_WIDTH, BANNER_HEIGHT);

        GlyphLayout scoreLayout = new GlyphLayout(SCORE_FONT, "Score: \n" + SCORE, Color.WHITE, 0, Align.left, false);
        GlyphLayout highScoreLayout = new GlyphLayout(SCORE_FONT, "Highscore: \n" + HIGH_SCORE, Color.WHITE, 0, Align.left, false);

        SCORE_FONT.draw(GAME.batch, scoreLayout,
                SpaceGame.WIDTH / 2f - scoreLayout.width / 2,
                SpaceGame.HEIGHT - BANNER_HEIGHT - 15 * 2);
        SCORE_FONT.draw(GAME.batch, highScoreLayout,
                SpaceGame.WIDTH / 2f - highScoreLayout.width / 2,
                SpaceGame.HEIGHT - BANNER_HEIGHT - scoreLayout.height - 15 * 3);

        float touchX = GAME.camera.getInputInGameWorld().x, touchY = SpaceGame.HEIGHT - GAME.camera.getInputInGameWorld().y;

        GlyphLayout tryAgainLayout = new GlyphLayout(SCORE_FONT, "Try Again");
        GlyphLayout mainMenuLayout = new GlyphLayout(SCORE_FONT, "Main Menu");

        float tryAgainX = SpaceGame.WIDTH / 2f - tryAgainLayout.width /2;
        float tryAgainY = SpaceGame.HEIGHT / 2f - tryAgainLayout.height / 2;
        float mainMenuX = SpaceGame.WIDTH / 2f - mainMenuLayout.width /2;
        float mainMenuY = SpaceGame.HEIGHT / 2f - mainMenuLayout.height / 2 - tryAgainLayout.height - 15;

        if (    touchX >= tryAgainX &&
                touchX < tryAgainX + tryAgainLayout.width &&
                touchY >= tryAgainY - tryAgainLayout.height &&
                touchY < tryAgainY)
            tryAgainLayout.setText(SCORE_FONT, "Try Again", Color.YELLOW, 0, Align.left, false);

        if (    touchX >= mainMenuX &&
                touchX < mainMenuX + mainMenuLayout.width &&
                touchY >= mainMenuY - mainMenuLayout.height &&
                touchY < mainMenuY)
            mainMenuLayout.setText(SCORE_FONT, "Main Menu", Color.YELLOW, 0, Align.left, false);

        if (Gdx.input.isTouched()) {
            if (    touchX > tryAgainX &&
                    touchX < tryAgainX + tryAgainLayout.width &&
                    touchY > tryAgainY - tryAgainLayout.height &&
                    touchY < tryAgainY) {
                this.dispose();
                GAME.batch.end();
                GAME.setScreen(new MainGameScreen(GAME));
                return;
            }

            if (    touchX > mainMenuX &&
                    touchX < mainMenuX + mainMenuLayout.width &&
                    touchY > mainMenuY - mainMenuLayout.height &&
                    touchY < mainMenuY) {
                this.dispose();
                GAME.batch.end();
                GAME.setScreen(new MainMenuScreen(GAME));
                return;
            }
        }

        SCORE_FONT.draw(GAME.batch, tryAgainLayout, tryAgainX, tryAgainY);
        SCORE_FONT.draw(GAME.batch, mainMenuLayout, mainMenuX, mainMenuY);

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

    }
}
