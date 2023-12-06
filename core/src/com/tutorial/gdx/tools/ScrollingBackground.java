package com.tutorial.gdx.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tutorial.gdx.SpaceGame;

public class ScrollingBackground {
    public static final int DEFAULT_SPEED = 80;
    public static final int ACCELERATION = 50;
    public static final int GOAL_REACH_ACCELERATION = 200;

    private final Texture IMAGE;
    private final float IMAGE_SCALE;

    private float y1, y2;
    private int speed, goalSpeed;
    private boolean speedFixed;

    public ScrollingBackground() {
        IMAGE = new Texture("stars_background.png");

        y1 = 0;
        y2 = IMAGE.getHeight();
        speed = 0;
        goalSpeed = DEFAULT_SPEED;
        IMAGE_SCALE = (float) SpaceGame.WIDTH / IMAGE.getWidth();
        speedFixed = true;
    }

    public void updateAndRender(float deltaTime, SpriteBatch batch) {
        if(speed < goalSpeed) {
            speed += (int) (GOAL_REACH_ACCELERATION * deltaTime);
            if (speed > goalSpeed)
                speed = goalSpeed;
        } else if(speed > goalSpeed) {
            speed -= (int) (GOAL_REACH_ACCELERATION * deltaTime);
            if(speed < goalSpeed)
                speed = goalSpeed;
        }

        if(!speedFixed)
            speed += (int) (ACCELERATION * deltaTime);

        y1 -= speed * deltaTime;
        y2 -= speed * deltaTime;

        if(y1 + IMAGE.getHeight() * IMAGE_SCALE <= 0)
            y1 = y2 + IMAGE.getHeight() * IMAGE_SCALE;
        if(y2 + IMAGE.getHeight() * IMAGE_SCALE <= 0)
            y2 = y1 + IMAGE.getHeight() * IMAGE_SCALE;

        batch.draw(IMAGE, 0, y1, SpaceGame.WIDTH, IMAGE.getHeight() * IMAGE_SCALE);
        batch.draw(IMAGE, 0, y2, SpaceGame.WIDTH, IMAGE.getHeight() * IMAGE_SCALE);
    }

    public void setSpeed(int goalSpeed) {
        this.goalSpeed = goalSpeed;
    }

    public void setSpeedFixed(boolean speedFixed) {
        this.speedFixed = speedFixed;
    }
}
