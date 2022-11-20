package com.sample.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen implements Screen {
    OrthographicCamera camera;
    Viewport viewport;
    MyGdxGame Game;
    Texture button1;
    Texture button2;
    Texture button3;
    Texture homepage;
    private float middlex;
    private float middley;
    private float button2y;
    private float button3y;
//    private float screen;
    float bgWidth, bgHeight;
    float buttonwidth,buttonheight;
//    private float image;
    Rectangle btn1Bounds, btn2Bounds, btn3Bounds;
    public MainScreen(MyGdxGame Game){
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();
        this.Game=Game;
        button1 = new Texture("TankStarsFeature\\Button1.png");
        button2 = new Texture("TankStarsFeature\\Button2.png");
        button3 = new Texture("TankStarsFeature\\Button3.png");
        homepage=new Texture("TankStarsFeature\\HomePage.jpg");

        bgWidth = Gdx.graphics.getWidth();
        bgHeight = Gdx.graphics.getHeight();

        float buttonratio=(float) button1.getWidth()/button1.getHeight();
        buttonwidth=bgWidth/5f;
        buttonheight=buttonwidth/buttonratio;

        middlex=Gdx.graphics.getWidth()/2-buttonwidth/2;
        middley=Gdx.graphics.getHeight()/2;


        button2y=middley-buttonheight-20;
        button3y=middley-2*buttonheight-40;

        btn1Bounds = new Rectangle(middlex, middley, buttonwidth, buttonheight);
        btn2Bounds = new Rectangle(middlex, button2y, buttonwidth, buttonheight);
        btn3Bounds = new Rectangle(middlex, button3y, buttonwidth, buttonheight);

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
                Game.setScreen(new GameScreen2(Game,""));
//                Game.setScreen(new GameScreen(Game, ""));
                dispose();
            }

            if (btn2Bounds.contains(touchPos.x, touchPos.y)) {
                Game.setScreen(new SavedScreen(Game));
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
        Game.batch.setProjectionMatrix(camera.combined);


        Game.batch.begin();

        Game.batch.draw(homepage, 0, 0, bgWidth, bgHeight);

        Game.batch.draw(button1, middlex, middley,buttonwidth,buttonheight);
        Game.batch.draw(button2, middlex, button2y,buttonwidth,buttonheight);
        Game.batch.draw(button3, middlex, button3y,buttonwidth,buttonheight);

        Game.batch.end();


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

    }


}
