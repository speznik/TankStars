package com.sample.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    MyGdxGame game;

    OrthographicCamera camera;
    Viewport viewport;

    float bgWidth, bgHeight, btnY, btn1X, btn2X, btn3X;
    Rectangle btn1Bounds, btn2Bounds, btn3Bounds;

    Texture bg, saveBtn, pauseBtn, resumeBtn, exitBtn;

    Preferences preferences;
    String saved;

    private Texture prepareTexture(String file) {
        Texture texture = new Texture(file);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return texture;
    }

    public GameScreen(MyGdxGame game, String gameId) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();


        saveBtn = prepareTexture("TankStarsFeature/save.png");
        pauseBtn = prepareTexture("TankStarsFeature/pause.png");
        resumeBtn = prepareTexture("TankStarsFeature/resume.png");
        exitBtn = prepareTexture("TankStarsFeature/exit.png");

        btnY = Gdx.graphics.getHeight() - saveBtn.getHeight() - 50;
        btn1X = 50;
        btn2X = btn1X + 50 + saveBtn.getWidth();
        btn3X = btn2X + 50 + saveBtn.getWidth();

        btn1Bounds = new Rectangle(btn1X, btnY, saveBtn.getWidth(), saveBtn.getHeight());
        btn2Bounds = new Rectangle(btn2X, btnY, saveBtn.getWidth(), saveBtn.getHeight());
        btn3Bounds = new Rectangle(btn3X, btnY, saveBtn.getWidth(), saveBtn.getHeight());

        preferences = Gdx.app.getPreferences("preferences");
        saved = preferences.getString("saved", "");

        if (gameId.equals("")) System.out.println("Start New Game");
        else {
            // p1 position , p2 position , p1 health , p2 health
            String gameData = preferences.getString("game" + gameId, "0,0,0,0,0,0");
            System.out.println("Load Saved Game: " + gameId + " Data: " + gameData);
        }
    }

    @Override
    public void show() {

    }

    private boolean isGamePaused = false;
    private void handleTouch() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (btn1Bounds.contains(touchPos.x, touchPos.y)) {
                isGamePaused = !isGamePaused;
            }

            if (btn2Bounds.contains(touchPos.x, touchPos.y)) {
                long gameId = System.currentTimeMillis();
                String newSaved = gameId + "," + saved;

                preferences.putString("saved", newSaved);
                preferences.putString("game" + gameId, "0,0,0,0,0,0");
                preferences.flush();
            }

            if (btn3Bounds.contains(touchPos.x, touchPos.y)) {
                game.setScreen(new MainScreen(game));
                dispose();
            }
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


        game.batch.begin();

        if (isGamePaused) game.batch.draw(resumeBtn, btn1X, btnY);
        else game.batch.draw(pauseBtn, btn1X, btnY);

        game.batch.draw(saveBtn, btn2X, btnY);
        game.batch.draw(exitBtn, btn3X, btnY);


        game.batch.end();

        handleTouch();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
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
        saveBtn.dispose();
        resumeBtn.dispose();
        pauseBtn.dispose();
        exitBtn.dispose();
    }
}