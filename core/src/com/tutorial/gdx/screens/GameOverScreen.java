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

    SpaceGame game;

    int score, highScore;

    Texture gameOverBanner;
    BitmapFont scoreFont;

    public GameOverScreen (SpaceGame game, int score) {
        this.game = game;
        this.score = score;

        Preferences prefs = Gdx.app.getPreferences("spacegame");
        this.highScore = prefs.getInteger("highscore", 0);

        if (score > highScore) {
            prefs.putInteger("highscore", score);
            prefs.flush();
        }

        gameOverBanner = new Texture("game_over.png");
        scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));

        game.scrollingBackground.setSpeedFixed(true);
        game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
    }

    @Override
    public void show () {}

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        game.scrollingBackground.updateAndRender(delta, game.batch);

        game.batch.draw(gameOverBanner, SpaceGame.WIDTH / 2f - BANNER_WIDTH / 2f, SpaceGame.HEIGHT - BANNER_HEIGHT - 15, BANNER_WIDTH, BANNER_HEIGHT);

        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "Score: \n" + score, Color.WHITE, 0, Align.left, false);
        GlyphLayout highScoreLayout = new GlyphLayout(scoreFont, "Highscore: \n" + highScore, Color.WHITE, 0, Align.left, false);
        scoreFont.draw(game.batch, scoreLayout, SpaceGame.WIDTH / 2f - scoreLayout.width / 2, SpaceGame.HEIGHT - BANNER_HEIGHT - 15 * 2);
        scoreFont.draw(game.batch, highScoreLayout, SpaceGame.WIDTH / 2f - highScoreLayout.width / 2, SpaceGame.HEIGHT - BANNER_HEIGHT - scoreLayout.height - 15 * 3);

        float touchX = game.camera.getInputInGameWorld().x, touchY = SpaceGame.HEIGHT - game.camera.getInputInGameWorld().y;

        GlyphLayout tryAgainLayout = new GlyphLayout(scoreFont, "Try Again");
        GlyphLayout mainMenuLayout = new GlyphLayout(scoreFont, "Main Menu");

        float tryAgainX = SpaceGame.WIDTH / 2f - tryAgainLayout.width /2;
        float tryAgainY = SpaceGame.HEIGHT / 2f - tryAgainLayout.height / 2;
        float mainMenuX = SpaceGame.WIDTH / 2f - mainMenuLayout.width /2;
        float mainMenuY = SpaceGame.HEIGHT / 2f - mainMenuLayout.height / 2 - tryAgainLayout.height - 15;

        if (touchX >= tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY >= tryAgainY - tryAgainLayout.height && touchY < tryAgainY)
            tryAgainLayout.setText(scoreFont, "Try Again", Color.YELLOW, 0, Align.left, false);

        if (touchX >= mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY >= mainMenuY - mainMenuLayout.height && touchY < mainMenuY)
            mainMenuLayout.setText(scoreFont, "Main Menu", Color.YELLOW, 0, Align.left, false);

        if (Gdx.input.isTouched()) {
            if (touchX > tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY > tryAgainY - tryAgainLayout.height && touchY < tryAgainY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new MainGameScreen(game));
                return;
            }

            if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }

        scoreFont.draw(game.batch, tryAgainLayout, tryAgainX, tryAgainY);
        scoreFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);

        game.batch.end();
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
