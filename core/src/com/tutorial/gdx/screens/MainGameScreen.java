package com.tutorial.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.tutorial.gdx.SpaceGame;
import com.tutorial.gdx.entities.Asteroid;
import com.tutorial.gdx.entities.Bullet;
import com.tutorial.gdx.entities.Explosion;
import com.tutorial.gdx.tools.CollisionRect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.Input.*;

public class MainGameScreen implements Screen {
    public static final float SPEED = 300;
    public static final float SHIP_ANIMATION_SPEED = 0.5f;
    public static final float ROLL_TIMER_SWITCH_TIME = 0.25f;
    public static final float SHOOT_WAIT_TIME = 0.3f;
    public static final float MIN_ASTEROID_SPAWN_TIME = 0.05f;
    public static final float MAX_ASTEROID_SPAWN_TIME = 0.1f;
    public static final int SHIP_WIDTH_PIXEL = 17;
    public static final int SHIP_HEIGHT_PIXEL = 32;
    public static final int SHIP_WIDTH = SHIP_WIDTH_PIXEL * 3;
    public static final int SHIP_HEIGHT = SHIP_HEIGHT_PIXEL * 3;

    private final List<Animation<TextureRegion>> ROLLS;
    private final List<Bullet> BULLETS;
    private final List<Asteroid> ASTEROIDS;
    private final List<Explosion> EXPLOSIONS;

    private final float Y;
    private final Random RANDOM;
    private final SpaceGame GAME;
    private final Texture BLANK;
    private final BitmapFont SCORE_FONT;
    private final CollisionRect PLAYER_RECT;

    private float x;
    private float rollTimer;
    private float stateTime;
    private float shootTimer;
    private float asteroidSpawnTimer;
    private float health = 1;
    private int roll;
    private int score;
    private boolean showControls = true;

    public MainGameScreen(SpaceGame game) {
        this.GAME = game;

        Y = 15;
        x = SpaceGame.WIDTH / 2f - SHIP_WIDTH / 2f;

        BULLETS = new ArrayList<>();
        ASTEROIDS = new ArrayList<>();
        EXPLOSIONS = new ArrayList<>();
        SCORE_FONT = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
        PLAYER_RECT = new CollisionRect(0, 0, SHIP_WIDTH, SHIP_HEIGHT);
        BLANK = new Texture("blank.png");

        score = 0;

        RANDOM = new Random();
        asteroidSpawnTimer = RANDOM.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;

        shootTimer = 0;

        roll = 2;
        rollTimer = 0;
        ROLLS = new ArrayList<>();

        TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("ship.png"), SHIP_WIDTH_PIXEL, SHIP_HEIGHT_PIXEL);

        ROLLS.add(new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[2]));
        ROLLS.add(new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[1]));
        ROLLS.add(new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[0]));
        ROLLS.add(new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[3]));
        ROLLS.add(new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[4]));

        game.scrollingBackground.setSpeedFixed(false);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        shootTimer += delta;
        if((isRight() || isLeft()) && shootTimer >= SHOOT_WAIT_TIME) {
            shootTimer = 0;

            showControls = false;

            int offset = 4;
            if(roll == 1 || roll == 3)
                offset = 8;

            if(roll == 0 || roll == 4)
                offset = 16;

            BULLETS.add(new Bullet(x + offset));
            BULLETS.add(new Bullet(x + SHIP_WIDTH - offset));
        }

        asteroidSpawnTimer -= delta;
        if(asteroidSpawnTimer <= 0) {
            asteroidSpawnTimer = RANDOM.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
            ASTEROIDS.add(new Asteroid(RANDOM.nextInt(SpaceGame.WIDTH - Asteroid.WIDTH)));
        }

        ArrayList<Asteroid> asteroidsToRemove = new ArrayList<>();
        for(Asteroid asteroid : ASTEROIDS) {
            asteroid.update(delta);
            if(asteroid.remove)
                asteroidsToRemove.add(asteroid);
        }

        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for(Bullet bullet : BULLETS) {
            bullet.update(delta);
            if(bullet.remove)
                bulletsToRemove.add(bullet);
        }

        ArrayList<Explosion> explosionsToRemove = new ArrayList<>();
        for(Explosion explosion : EXPLOSIONS) {
            explosion.update(delta);
            if(explosion.remove)
                explosionsToRemove.add(explosion);
        }
        EXPLOSIONS.removeAll(explosionsToRemove);

        if(isLeft()) {
            x -= SPEED * Gdx.graphics.getDeltaTime();

            if(x < 0)
                x = 0;

            if(isJustLeft() && !isRight() && roll > 0) {
                rollTimer = 0;
                roll--;
            }

            rollTimer -= Gdx.graphics.getDeltaTime();
            if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll > 0) {
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll--;
            }
        } else {
            if(roll < 2) {
                rollTimer += Gdx.graphics.getDeltaTime();
                if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll < 4) {
                    rollTimer -= ROLL_TIMER_SWITCH_TIME;
                    roll++;
                }
            }
        }

        if(isRight()) {
            x += SPEED * Gdx.graphics.getDeltaTime();

            if(x + SHIP_WIDTH > SpaceGame.WIDTH)
                x = SpaceGame.WIDTH - SHIP_WIDTH;

            if(isJustRight() && !isLeft() && roll > 0) {
                rollTimer = 0;
                roll--;
            }

            rollTimer += Gdx.graphics.getDeltaTime();
            if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll < 4) {
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll++;
            }
        } else {
            if(roll > 2) {
                rollTimer -= Gdx.graphics.getDeltaTime();
                if(Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll > 0) {
                    rollTimer -= ROLL_TIMER_SWITCH_TIME;
                    roll--;
                }
            }
        }

        PLAYER_RECT.move(x, Y);

        for(Bullet bullet : BULLETS) {
            for(Asteroid asteroid : ASTEROIDS) {
                if(bullet.getCollisionRect().collidesWith(asteroid.getCollisionRect())) {
                    bulletsToRemove.add(bullet);
                    asteroidsToRemove.add(asteroid);
                    EXPLOSIONS.add(new Explosion(asteroid.getX(), asteroid.getY()));
                    score += 100;
                }
            }
        }
        BULLETS.removeAll(bulletsToRemove);

        for(Asteroid asteroid : ASTEROIDS) {
            if(asteroid.getCollisionRect().collidesWith(PLAYER_RECT)) {
                asteroidsToRemove.add(asteroid);
                health -= 0.1F;

                if(health <= 0) {
                    this.dispose();
                    GAME.setScreen(new GameOverScreen(GAME, score));
                    return;
                }
            }
        }
        ASTEROIDS.removeAll(asteroidsToRemove);

        stateTime += delta;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GAME.batch.begin();

        GAME.scrollingBackground.updateAndRender(delta, GAME.batch);

        GlyphLayout scoreLayout = new GlyphLayout(SCORE_FONT, "" + score);
        SCORE_FONT.draw(GAME.batch, scoreLayout, SpaceGame.WIDTH / 2f - scoreLayout.width / 2, SpaceGame.HEIGHT - scoreLayout.height - 10);

        for(Bullet bullet : BULLETS) {
            bullet.render(GAME.batch);
        }

        for(Asteroid asteroid : ASTEROIDS) {
            asteroid.render(GAME.batch);
        }

        for(Explosion explosion : EXPLOSIONS) {
            explosion.render(GAME.batch);
        }

        if(health > 0.6f)
            GAME.batch.setColor(Color.GREEN);
        else if(health > 0.2f)
            GAME.batch.setColor(Color.ORANGE);
        else
            GAME.batch.setColor(Color.RED);

        GAME.batch.draw(BLANK, 0, 0, SpaceGame.WIDTH * health, 5);
        GAME.batch.setColor(Color.WHITE);

        GAME.batch.draw(ROLLS.get(roll).getKeyFrame(stateTime, true), x, Y, SHIP_WIDTH, SHIP_HEIGHT);

        if(showControls) {
            GlyphLayout instructionsLayout = new GlyphLayout(SCORE_FONT, "Press Left/Right Arrowkeys to Shoot!", Color.WHITE, SpaceGame.WIDTH - 50, Align.center, true);
            SCORE_FONT.draw(GAME.batch, instructionsLayout, SpaceGame.WIDTH / 2f - instructionsLayout.width / 2, 150);
        }

        GAME.batch.end();
    }

    private boolean isRight() {
        return Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isTouched() && GAME.camera.getInputInGameWorld().x >= SpaceGame.WIDTH / 2f);
    }

    private boolean isLeft() {
        return Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isTouched() && GAME.camera.getInputInGameWorld().x < SpaceGame.WIDTH / 2f);
    }

    private boolean isJustRight() {
        return Gdx.input.isKeyJustPressed(Keys.RIGHT) || (Gdx.input.justTouched() && GAME.camera.getInputInGameWorld().x >= SpaceGame.WIDTH / 2f);
    }

    private boolean isJustLeft() {
        return Gdx.input.isKeyJustPressed(Keys.LEFT) || (Gdx.input.justTouched() && GAME.camera.getInputInGameWorld().x < SpaceGame.WIDTH / 2f);
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
