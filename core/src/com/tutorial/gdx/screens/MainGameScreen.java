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
    public static final int SHIP_WIDTH_PIXEL = 17;
    public static final int SHIP_HEIGHT_PIXEL = 32;
    public static final int SHIP_WIDTH = SHIP_WIDTH_PIXEL * 3;
    public static final int SHIP_HEIGHT = SHIP_HEIGHT_PIXEL * 3;

    public static final float ROLL_TIMER_SWITCH_TIME = 0.25f;
    public static final float SHOOT_WAIT_TIME = 0.3f;

    public static final float MIN_ASTEROID_SPAWN_TIME = 0.05f;
    public static final float MAX_ASTEROID_SPAWN_TIME = 0.1f;

    List<Animation<TextureRegion>> rolls;

    float x;
    float y;
    int roll;
    float rollTimer;
    float stateTime;
    float shootTimer;
    float asteroidSpawnTimer;

    Random random;

    SpaceGame game;

    ArrayList<Bullet> bullets;
    ArrayList<Asteroid> asteroids;
    ArrayList<Explosion> explosions;

    Texture blank;

    BitmapFont scoreFont;

    CollisionRect playerRect;

    float health = 1;

    int score;

    boolean showControls = true;

    public MainGameScreen(SpaceGame game) {
        this.game = game;

        y = 15;
        x = SpaceGame.WIDTH / 2f - SHIP_WIDTH / 2f;
        bullets = new ArrayList<>();
        asteroids = new ArrayList<>();
        explosions = new ArrayList<>();
        scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));

        playerRect = new CollisionRect(0, 0, SHIP_WIDTH, SHIP_HEIGHT);

        blank = new Texture("blank.png");

        score = 0;

        random = new Random();
        asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;

        shootTimer = 0;

        roll = 2;
        rollTimer = 0;
        rolls = new ArrayList<>();

        TextureRegion[][] rollSpriteSheet = TextureRegion.split(new Texture("ship.png"), SHIP_WIDTH_PIXEL, SHIP_HEIGHT_PIXEL);

        rolls.add(new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[2]));
        rolls.add(new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[1]));
        rolls.add(new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[0]));
        rolls.add(new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[3]));
        rolls.add(new Animation<>(SHIP_ANIMATION_SPEED, rollSpriteSheet[4]));

        game.scrollingBackground.setSpeedFixed(false);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        shootTimer += delta;
        if ((isRight() || isLeft()) && shootTimer >= SHOOT_WAIT_TIME) {
            shootTimer = 0;

            showControls = false;

            int offset = 4;
            if (roll == 1 || roll == 3)
                offset = 8;

            if (roll == 0 || roll == 4)
                offset = 16;

            bullets.add(new Bullet(x + offset));
            bullets.add(new Bullet(x + SHIP_WIDTH - offset));
        }

        asteroidSpawnTimer -= delta;
        if (asteroidSpawnTimer <= 0) {
            asteroidSpawnTimer = random.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
            asteroids.add(new Asteroid(random.nextInt(SpaceGame.WIDTH - Asteroid.WIDTH)));
        }

        ArrayList<Asteroid> asteroidsToRemove = new ArrayList<>();
        for (Asteroid asteroid : asteroids) {
            asteroid.update(delta);
            if (asteroid.remove)
                asteroidsToRemove.add(asteroid);
        }

        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.update(delta);
            if (bullet.remove)
                bulletsToRemove.add(bullet);
        }

        ArrayList<Explosion> explosionsToRemove = new ArrayList<>();
        for (Explosion explosion : explosions) {
            explosion.update(delta);
            if (explosion.remove)
                explosionsToRemove.add(explosion);
        }
        explosions.removeAll(explosionsToRemove);

        if (isLeft()) {
            x -= SPEED * Gdx.graphics.getDeltaTime();

            if (x < 0)
                x = 0;

            if (isJustLeft() && !isRight() && roll > 0) {
                rollTimer = 0;
                roll--;
            }

            rollTimer -= Gdx.graphics.getDeltaTime();
            if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll > 0) {
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll--;
            }
        } else {
            if (roll < 2) {
                rollTimer += Gdx.graphics.getDeltaTime();
                if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll < 4) {
                    rollTimer -= ROLL_TIMER_SWITCH_TIME;
                    roll++;
                }
            }
        }

        if (isRight()) {
            x += SPEED * Gdx.graphics.getDeltaTime();

            if (x + SHIP_WIDTH > SpaceGame.WIDTH)
                x = SpaceGame.WIDTH - SHIP_WIDTH;

            if (isJustRight() && !isLeft() && roll > 0) {
                rollTimer = 0;
                roll--;
            }

            rollTimer += Gdx.graphics.getDeltaTime();
            if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll < 4) {
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll++;
            }
        } else {
            if (roll > 2) {
                rollTimer -= Gdx.graphics.getDeltaTime();
                if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll > 0) {
                    rollTimer -= ROLL_TIMER_SWITCH_TIME;
                    roll--;
                }
            }
        }

        playerRect.move(x, y);

        for (Bullet bullet : bullets) {
            for (Asteroid asteroid : asteroids) {
                if (bullet.getCollisionRect().collidesWith(asteroid.getCollisionRect())) {
                    bulletsToRemove.add(bullet);
                    asteroidsToRemove.add(asteroid);
                    explosions.add(new Explosion(asteroid.getX(), asteroid.getY()));
                    score += 100;
                }
            }
        }
        bullets.removeAll(bulletsToRemove);

        for (Asteroid asteroid : asteroids) {
            if (asteroid.getCollisionRect().collidesWith(playerRect)) {
                asteroidsToRemove.add(asteroid);
                health -= 0.1F;

                if (health <= 0) {
                    this.dispose();
                    game.setScreen(new GameOverScreen(game, score));
                    return;
                }
            }
        }
        asteroids.removeAll(asteroidsToRemove);

        stateTime += delta;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        game.scrollingBackground.updateAndRender(delta, game.batch);

        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "" + score);
        scoreFont.draw(game.batch, scoreLayout, SpaceGame.WIDTH / 2f - scoreLayout.width / 2, SpaceGame.HEIGHT - scoreLayout.height - 10);

        for (Bullet bullet : bullets) {
            bullet.render(game.batch);
        }

        for (Asteroid asteroid : asteroids) {
            asteroid.render(game.batch);
        }

        for (Explosion explosion : explosions) {
            explosion.render(game.batch);
        }

        if (health > 0.6f)
            game.batch.setColor(Color.GREEN);
        else if (health > 0.2f)
            game.batch.setColor(Color.ORANGE);
        else
            game.batch.setColor(Color.RED);

        game.batch.draw(blank, 0, 0, SpaceGame.WIDTH * health, 5);
        game.batch.setColor(Color.WHITE);

        game.batch.draw(rolls.get(roll).getKeyFrame(stateTime, true), x, y, SHIP_WIDTH, SHIP_HEIGHT);

        if (showControls) {
            GlyphLayout instructionsLayout = new GlyphLayout(scoreFont, "Press Left/Right Arrowkeys to Shoot!", Color.WHITE, SpaceGame.WIDTH - 50, Align.center, true);
            scoreFont.draw(game.batch, instructionsLayout, SpaceGame.WIDTH / 2f - instructionsLayout.width / 2, 150);
        }

        game.batch.end();
    }

    private boolean isRight () {
        return Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isTouched() && game.camera.getInputInGameWorld().x >= SpaceGame.WIDTH / 2f);
    }

    private boolean isLeft () {
        return Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isTouched() && game.camera.getInputInGameWorld().x < SpaceGame.WIDTH / 2f);
    }

    private boolean isJustRight () {
        return Gdx.input.isKeyJustPressed(Keys.RIGHT) || (Gdx.input.justTouched() && game.camera.getInputInGameWorld().x >= SpaceGame.WIDTH / 2f);
    }

    private boolean isJustLeft () {
        return Gdx.input.isKeyJustPressed(Keys.LEFT) || (Gdx.input.justTouched() && game.camera.getInputInGameWorld().x < SpaceGame.WIDTH / 2f);
    }

    @Override
    public void resize (int width, int height) {

    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {

    }

    @Override
    public void hide () {

    }

    @Override
    public void dispose () {

    }
}
