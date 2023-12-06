package com.tutorial.gdx.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameCamera {
    private final OrthographicCamera CAMERA;
    private final StretchViewport VIEWPORT;

    public GameCamera(int width, int height) {
        CAMERA = new OrthographicCamera();
        VIEWPORT = new StretchViewport(width, height, CAMERA);
        VIEWPORT.apply();
        CAMERA.position.set(width / 2f, height / 2f, 0);
        CAMERA.update();
    }

    public Matrix4 combined() {
        return CAMERA.combined;
    }

    public void update(int width, int height) {
        VIEWPORT.update(width, height);
    }

    public Vector2 getInputInGameWorld() {
        Vector3 inputScreen = new Vector3(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 0);
        Vector3 unprojected = CAMERA.unproject(inputScreen);
        return new Vector2(unprojected.x, unprojected.y);
    }
}
