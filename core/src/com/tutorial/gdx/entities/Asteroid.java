package com.tutorial.gdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tutorial.gdx.SpaceGame;
import com.tutorial.gdx.tools.CollisionRect;

public class Asteroid {
    public static final int SPEED = 250;
    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;

    private static Texture texture;

    private final float X;
    private final CollisionRect RECT;

    public boolean remove = false;

    private float y;

    public Asteroid(float x) {
        this.X = x;
        y = SpaceGame.HEIGHT;
        RECT = new CollisionRect(x, y, WIDTH, HEIGHT);

        if(texture == null)
            texture = new Texture("asteroid.png");
    }

    public void update(float deltaTime) {
        y -= SPEED * deltaTime;
        if(y < -HEIGHT)
            remove =  true;

        RECT.move(X, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, X, y, WIDTH, HEIGHT);
    }

    public CollisionRect getCollisionRect() {
        return RECT;
    }

    public float getX() {
        return X;
    }

    public float getY() {
        return y;
    }
}
