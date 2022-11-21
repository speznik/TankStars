package com.sample.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.*;
import java.util.Random;

public class GameScreen2 implements Screen {
//    SpriteBatch batch;
    PolygonSpriteBatch polygonSpriteBatch;
    Texture p1Texture, p2Texture, hillTexture;
    private OrthographicCamera camera;

    private Box2DDebugRenderer b2dr;
    private World world;
    private Body player1, player2, hill;
    private final float playerSize = 100;
    private final float PPM = 32;
    Viewport viewport;
    TextureRegion textureRegion;

    PolygonRegion polygonRegion;
    MyGdxGame Game;
    private float screenWidth, screenHeight;
    Rectangle btn1Bounds;

    Texture button1;
    private float middlex;
    private float middley;
    private float button2y;
    private float button3y;
    //    private float screen;
    float bgWidth, bgHeight;
    float buttonwidth,buttonheight;
    Scores scores;
    float tankheigth;
//    float tankratio1;
    float tankwidth1;
    float tankwidth2;
    public int tankId1;
    public int tankId2;

    public GameScreen2(MyGdxGame game,String gameId,int tankId1,int tankId2) {
        Preferences preferences=Gdx.app.getPreferences("prefrences");
        if(!gameId.equals("")) {
            String[] gameData = preferences.getString("game" + gameId, "0,0,0,0,100,100,80,80,"+tankId1+","+tankId2).split(",");
            tankId1=Integer.parseInt(gameData[8]);
            tankId2=Integer.parseInt(gameData[9]);
        }
        this.Game=game;
        this.tankId1=tankId1;
        this.tankId2=tankId2;
//        batch = new SpriteBatch();
        polygonSpriteBatch = new PolygonSpriteBatch();


        screenWidth = Gdx.graphics.getWidth() / PPM;
        screenHeight = Gdx.graphics.getHeight() / PPM;
        //menu bar
        button1 = new Texture("TankStarsFeature\\Component1.png");
        float buttonratio=(float) button1.getWidth()/button1.getHeight();
        buttonwidth=bgWidth/5f/PPM;
        buttonheight=buttonwidth/buttonratio/PPM;
        middlex=(Gdx.graphics.getWidth()/2-buttonwidth/2)/PPM;
        middley=(Gdx.graphics.getHeight()/2)/PPM;
        btn1Bounds = new Rectangle(middlex, middley, buttonwidth, buttonheight);
        // Textures
        hillTexture = prepareTexture("TankStarsFeature\\Hill.png");
        hillTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        textureRegion = new TextureRegion(hillTexture);
        textureRegion.setRegion(0, 0, hillTexture.getWidth() * 10, hillTexture.getHeight() * 10);

        String[] tankTextures1={"TankStarsFeature\\Tank4.png","TankStarsFeature\\Tank1.png","TankStarsFeature\\toxictank.jpg"};
        String[] tankTextures2={"TankStarsFeature\\Tank0.png","TankStarsFeature\\Tank5.png","TankStarsFeature\\toxictank.jpg"};

        p1Texture = prepareTexture(tankTextures1[tankId1]);
        p2Texture = prepareTexture(tankTextures2[tankId2]);

        // Camera
        camera = new OrthographicCamera();
//        camera.setToOrtho(false);
        viewport=new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();
        // Physics World
        world = new World(new Vector2(0, -9.8f), false);
        b2dr = new Box2DDebugRenderer();

        hill = createBody(0, 0, 0, 0, true, false);

        tankheigth = playerSize;
        float tankratio1 = (float) p1Texture.getWidth() / p1Texture.getHeight();
        float tankratio2=(float) p2Texture.getWidth()/ p2Texture.getHeight();
        tankwidth1 = tankratio1 * tankheigth;
        tankwidth2=tankratio2*tankheigth;

        player1 = createBody(200, Gdx.graphics.getHeight() + playerSize / 2, tankwidth1, tankheigth, false, true);
        player2 = createBody(Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight()+ playerSize / 2, tankwidth2, tankheigth, false, true);


        scores=new Scores(Game,Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),gameId,tankId1,tankId2);
//        Preferences preferences = Gdx.app.getPreferences("preferences");

//        if (!gameId.equals("")) {
            // p1 position , p2 position , p1 health , p2 health
//            String gameData = preferences.getString("game" + gameId, "0,0,0,0,0,0");
//            System.out.println("Load Saved Game: " + gameId + " Data: " + gameData);

//        }
    }

    private Texture prepareTexture(String file) {
        Texture texture = new Texture(file);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return texture;
    }

    private Body createBody(float x, float y, float width, float height, boolean isStatic, boolean isBox) {
        // Create Body
        BodyDef bodyDef = new BodyDef();

        if (isStatic) bodyDef.type = BodyDef.BodyType.StaticBody;
        else bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.fixedRotation = true;
        bodyDef.position.set(x / PPM, y / PPM);
        Body body = world.createBody(bodyDef);

        // Set body Shape
        if (isBox) {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

            body.createFixture(shape, 1f);
            shape.dispose();
        } else {
            ChainShape shape = new ChainShape();

            float lastX = x / PPM;
            float lastY = y / PPM;

            Vector2[] vertices = new Vector2[302];
            vertices[0] = new Vector2(lastX, lastY);

            lastY = screenHeight / 1.5f;
            vertices[1] = new Vector2(lastX, lastY);

            for (int i = 2; i < vertices.length; i += 4) {
                lastX = randomPoint(true, lastX, true);
                vertices[i] = new Vector2(lastX, lastY);

                lastX = randomPoint(true, lastX, true);
                lastY = randomPoint(false, lastY, false);
                vertices[i + 1] = new Vector2(lastX, lastY);

                lastX = randomPoint(true, lastX, true);
                vertices[i + 2] = new Vector2(lastX, lastY);

                lastX = randomPoint(true, lastX, true);
                lastY = randomPoint(false, lastY, true);
                vertices[i + 3] = new Vector2(lastX, lastY);
            }

            shape.createChain(vertices);

            body.createFixture(shape, 1f);
            shape.dispose();


            float[] vertices2 = new float[vertices.length * 2];

            int counter = 0;
            for (int i = 0; i < vertices.length; i++) {
                vertices2[counter] = vertices[i].x * PPM;
                vertices2[counter + 1] = vertices[i].y * PPM;

                counter += 2;
            }

            short[] triangles = new EarClippingTriangulator().computeTriangles(vertices2).toArray();
            polygonRegion = new PolygonRegion(textureRegion, vertices2, triangles);
        }

        return body;
    }

    private float randomPoint(boolean isXAxis, float lastPoint, boolean wantHigher) {
        Random random = new Random();

        float min = isXAxis ? (lastPoint + screenWidth / 10) : (wantHigher ? (lastPoint + screenHeight / 4.5f) : (lastPoint - screenHeight / 4.5f));
        float max = isXAxis ? (lastPoint + screenWidth / 5) : (wantHigher ? (lastPoint + screenHeight / 4f) : (lastPoint - screenHeight / 4f));

        return random.nextFloat() * (max - min) + min;
    }

    private void update(float delta) {
        // World Update
        world.step(1 / 60f, 6, 2);

        // Camera Update
        Vector3 position = camera.position;
        position.x = (player1.getPosition().x) * PPM + Gdx.graphics.getWidth() / 2.3f;
        position.y = player1.getPosition().y * PPM + Gdx.graphics.getHeight() / 15f;
        camera.position.set(position);
        camera.update();
    }

//    @Override
//    public void render() {
//
//    }

    @Override
    public void show() {

    }
    private void handleTouch() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (btn1Bounds.contains(touchPos.x, touchPos.y)) {
                Game.setScreen(new GameScreen(Game,""));
//                Game.setScreen(new GameScreen(Game, ""));
                dispose();
            }


        }
    }
    @Override
    public void render(float delta) {
        update(Gdx.graphics.getDeltaTime());


        ScreenUtils.clear(38 / 255f, 31 / 255f, 88 / 255f, 1);


        polygonSpriteBatch.setProjectionMatrix(camera.combined);
        polygonSpriteBatch.begin();
        polygonSpriteBatch.draw(polygonRegion, hill.getPosition().x, hill.getPosition().y);
        polygonSpriteBatch.end();


        Game.batch.setProjectionMatrix(camera.combined);
        Game.batch.begin();
        Game.batch.draw(button1, middlex, middley,buttonwidth,buttonheight);
        Game.batch.draw(p1Texture, player1.getPosition().x * PPM - tankwidth1 / 2, player1.getPosition().y * PPM - tankheigth / 2, tankwidth1, tankheigth);
        Game.batch.draw(p2Texture, player2.getPosition().x * PPM - tankwidth2 / 2, player2.getPosition().y * PPM - tankheigth / 2, tankwidth2, tankheigth);
        Game.batch.end();


        Game.batch.setProjectionMatrix(scores.stage.getCamera().combined);
        scores.stage.draw();

        handleTouch();


        b2dr.render(world, camera.combined.scl(PPM));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

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
//        batch.dispose();
        p1Texture.dispose();
        p2Texture.dispose();
        world.dispose();
        b2dr.dispose();
    }
}
