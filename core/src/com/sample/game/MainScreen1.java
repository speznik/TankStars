package com.sample.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen1 implements Screen {
    MyGdxGame game;
    OrthographicCamera camera;
    Viewport viewport;

    float bgWidth, bgHeight, btnX, btn1Y, btn2Y, btn3Y;
    Rectangle btn1Bounds, btn2Bounds, btn3Bounds;

    Texture bg, newBtn, resumeBtn, exitBtn;

    public MainScreen1(MyGdxGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();

        bg = new Texture("TankStarsFeature\\HomePage.jpg");
        bgWidth = Gdx.graphics.getWidth();
        bgHeight = Gdx.graphics.getHeight();

        newBtn = new Texture("TankStarsFeature\\Button1.png");
        newBtn.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        resumeBtn = new Texture("TankStarsFeature\\Button2.png");
        resumeBtn.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        exitBtn = new Texture("TankStarsFeature\\Button3.png");
        exitBtn.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        btnX = Gdx.graphics.getWidth() / 2 - newBtn.getWidth() / 2;
        btn1Y = Gdx.graphics.getHeight() / 3 + newBtn.getHeight();
        btn2Y = btn1Y - 60;
        btn3Y = btn2Y - 60;

        btn1Bounds = new Rectangle(btnX, btn1Y, newBtn.getWidth(), newBtn.getHeight());
        btn2Bounds = new Rectangle(btnX, btn2Y, newBtn.getWidth(), newBtn.getHeight());
        btn3Bounds = new Rectangle(btnX, btn3Y, newBtn.getWidth(), newBtn.getHeight());
    }

    @Override
    public void show() {

    }

    private void handleTouch() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (btn1Bounds.contains(touchPos.x, touchPos.y)) {
                game.setScreen(new GameScreen(game, ""));
                dispose();
            }

            if (btn2Bounds.contains(touchPos.x, touchPos.y)) {
                game.setScreen(new SavedScreen(game));
                dispose();
            }

            if (btn3Bounds.contains(touchPos.x, touchPos.y)) {
                Gdx.app.exit();
                dispose();
            }
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 1, 1);



        camera.update();
        game.batch.setProjectionMatrix(camera.combined);


        game.batch.begin();

        game.batch.draw(bg, 0, 0, bgWidth, bgHeight);

        game.batch.draw(newBtn, btnX, btn1Y);
        game.batch.draw(resumeBtn, btnX, btn2Y);
        game.batch.draw(exitBtn, btnX, btn3Y);


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
        bg.dispose();
        newBtn.dispose();
        resumeBtn.dispose();
        exitBtn.dispose();
    }
}