package com.tutorial.gdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tutorial.gdx.SpaceGame;
import com.tutorial.gdx.tools.CollisionRect;

public class Bullet {
    public static final int SPEED = 500;
    public static final int DEFAULT_Y = 40;
    public static final int WIDTH = 3;
    public static final int HEIGHT = 12;

    private static Texture texture;

    private final float X;
    private final CollisionRect RECT;

    public boolean remove = false;

    private float y;

    public Bullet(float x) {
        this.X = x;
        y = DEFAULT_Y;
        RECT = new CollisionRect(x, y, WIDTH, HEIGHT);

        if(texture == null)
            texture = new Texture("bullet.png");
    }

    public void update(float deltaTime) {
        y += SPEED * deltaTime;
        if (y > SpaceGame.HEIGHT)
            remove = true;

        RECT.move(X, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, X, y);
    }

    public CollisionRect getCollisionRect() {
        return RECT;
    }
}
