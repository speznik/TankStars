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
    float bgWidth, bgHeight;
    float buttonwidth, buttonheight;


    // To Detech Touch on Buttons
    Rectangle btn1Bounds, btn2Bounds, btn3Bounds;

    public MainScreen(MyGdxGame Game) {
        // Initialize Camera and Viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();

        // Store Game Object for Screen Switching
        this.Game = Game;

        // Load Button and BG Textures
        button1 = new Texture("TankStarsFeature\\Button1.png");
        button2 = new Texture("TankStarsFeature\\Button2.png");
        button3 = new Texture("TankStarsFeature\\Button3.png");
        homepage = new Texture("TankStarsFeature\\HomePage.jpg");

        // Full Screen BG
        bgWidth = Gdx.graphics.getWidth();
        bgHeight = Gdx.graphics.getHeight();

        // Ratio to size the buttons
        float buttonratio = (float) button1.getWidth() / button1.getHeight();
        buttonwidth = bgWidth / 5f;
        buttonheight = buttonwidth / buttonratio;

        middlex = Gdx.graphics.getWidth() / 2 - buttonwidth / 2;
        middley = Gdx.graphics.getHeight() / 2;


        button2y = middley - buttonheight - 20;
        button3y = middley - 2 * buttonheight - 40;

        btn1Bounds = new Rectangle(middlex, middley, buttonwidth, buttonheight);
        btn2Bounds = new Rectangle(middlex, button2y, buttonwidth, buttonheight);
        btn3Bounds = new Rectangle(middlex, button3y, buttonwidth, buttonheight);

    }

    @Override
    public void show() {

    }

    private void handleTouch() {
        // Detect Screen Touch/Click
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            // Go To Choose Tank Screen
            if (btn1Bounds.contains(touchPos.x, touchPos.y)) {
                Game.setScreen(new ChooseTank(Game));
                dispose();
            }

            // Saved
            if (btn2Bounds.contains(touchPos.x, touchPos.y)) {
                Game.setScreen(new SavedScreen(Game));
                dispose();
            }

            // Exit the App
            if (btn3Bounds.contains(touchPos.x, touchPos.y)) {
                Gdx.app.exit();
                dispose(); // Free the Memory
            }
        }
    }

    @Override
    public void render(float delta) {
        // To Set the Background Pixels/Colors
        ScreenUtils.clear(0, 0, 1, 1);


        // Update Camera in each frame
        camera.update();

        // Set Sprite Batch Matrix to Camera View
        Game.batch.setProjectionMatrix(camera.combined);


        // Begin Sprite Rendering
        Game.batch.begin();

        // Render Sprites
        Game.batch.draw(homepage, 0, 0, bgWidth, bgHeight);
        Game.batch.draw(button1, middlex, middley, buttonwidth, buttonheight);
        Game.batch.draw(button2, middlex, button2y, buttonwidth, buttonheight);
        Game.batch.draw(button3, middlex, button3y, buttonwidth, buttonheight);

        // End Sprite Rendering
        Game.batch.end();


        // Handle User Input
        handleTouch();
    }

    @Override
    public void resize(int width, int height) {
        // Update Viewport and Camera according to new window size
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
        button1.dispose();
        button2.dispose();
        button3.dispose();

    }
}
