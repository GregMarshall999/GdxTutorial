package com.tutorial.gdx.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {
    public static final float FRAME_LENGTH = 0.2f;
    public static final int OFFSET = 8;
    public static final int SIZE = 64;
    public static final int IMAGE_SIZE = 32;

    private static Animation<TextureRegion> anim = null;

    float x, y;
    float stateTime;

    public boolean remove = false;

    public Explosion(float x, float y) {
        this.x = x - OFFSET;
        this.y = y - OFFSET;
        stateTime = 0;

        if(anim == null)
            anim = new Animation<>(FRAME_LENGTH,
                    TextureRegion.split(new Texture("explosion.png"), IMAGE_SIZE, IMAGE_SIZE)[0]);
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        if(anim.isAnimationFinished(stateTime))
            remove = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(anim.getKeyFrame(stateTime), x, y, SIZE, SIZE);
    }
}
