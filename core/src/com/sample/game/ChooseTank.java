package com.sample.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ChooseTank implements Screen {
    OrthographicCamera camera;
    Viewport viewport;
    MyGdxGame Game;
    Texture tank1;
    Texture tank2;
    Texture tank3;


    // Message to Show Status
    String Message;
    float Messagex,Messagey; // Message Coordinates
    float tank1x;
    float tank2x;
    float tank3x;
    float tanky;
    float tankwidth1;
    float tankwidth2;
    float tankwidth3;
    float tankheigth;

    // Variable to Store Selected Tanks
    int tankid1=-1;
//    int tankid2=-1;

    // Bounds to handle touch on buttons
    Rectangle tank1Bounds,tank2Bounds,tank3Bounds;

    public ChooseTank(MyGdxGame game) {
        Messagex=100;
        Messagey=Gdx.graphics.getHeight()-200;
        Message="Choose tank1";
        this.Game = game;
        tank1 = new Texture("TankStarsFeature\\Tank4.png");
        tank2 = new Texture("TankStarsFeature\\Tank1.png");
        tank3=new Texture("TankStarsFeature\\Tank6.png");

        tankheigth = Gdx.graphics.getHeight() / 15f;
        float tankratio1 = (float) tank1.getWidth() / tank1.getHeight();
        float tankratio2=(float)tank2.getWidth()/ tank2.getHeight();
        float tankratio3=(float)tank3.getWidth()/ tank3.getHeight();
        tankwidth1 = tankratio1 * tankheigth;
        tankwidth2=tankratio2*tankheigth;
        tankwidth3=tankratio3*tankheigth;

        tanky=Gdx.graphics.getHeight()/2f-tankheigth/2;


        tank2x=Gdx.graphics.getWidth()/2f-tankwidth2;
        tank1x =tank2x-50-tankwidth1 ;
        tank3x=tank2x+50+tankwidth2;

        tank1Bounds = new Rectangle(tank1x, tanky, tankwidth1, tankheigth);
        tank2Bounds = new Rectangle(tank2x, tanky, tankwidth2, tankheigth);
        tank3Bounds= new Rectangle(tank3x, tanky, tankwidth3, tankheigth);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(38 / 255f, 31 / 255f, 88 / 255f, 1);


//        camera.update();
//        Game.batch.setProjectionMatrix(camera.combined);


        Game.batch.begin();

//        Game.batch.draw(homepage, 0, 0, bgWidth, bgHeight);

        Game.batch.draw(tank1, tank1x, tanky, tankwidth1, tankheigth);
        Game.batch.draw(tank2, tank2x, tanky,tankwidth2,tankheigth);
        Game.batch.draw(tank3, tank3x, tanky,tankwidth3,tankheigth);

        Game.font.draw(Game.batch, Message,Messagex,Messagey);
        Game.batch.end();


        handleTouch();
    }
    private void handleTouch() {
        if (Gdx.input.justTouched()) {
            // Capture Touch Position
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
//            camera.unproject(touchPos);


            // If touched Tank 1
            if (tank1Bounds.contains(touchPos.x, touchPos.y)) {
                System.out.println("Tank1");

                // Chosen as Player 1's Tank
                if(tankid1==-1){
                    tankid1=0;
                    Message="Choose tank2";
                }

                // Chosen as Player 2's Tank
                else {
                    Game.setScreen(new GameScreen2(Game, "", tankid1,0));
                    dispose();
                }
            }

            if (tank2Bounds.contains(touchPos.x, touchPos.y)) {
                System.out.println("Tank2");
                if(tankid1==-1) {
                    tankid1=1;
                    Message="Choose tank2";
                }
                else {
                    Game.setScreen(new GameScreen2(Game, "", tankid1,1));
                    dispose();
                }
            }

            if (tank3Bounds.contains(touchPos.x, touchPos.y)) {
                System.out.println("Tank3");
                if(tankid1==-1) {
                    tankid1=2;
                    Message="Choose tank2";
                }
                else {
                    Game.setScreen(new GameScreen2(Game, "", tankid1,2));
                    dispose();
                }
            }
        }
    }
    @Override
    public void resize(int width, int height) {

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
