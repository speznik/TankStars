package com.sample.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.*;
import java.util.HashMap;
import java.util.Random;

public class GameScreen2 implements Screen {
//    SpriteBatch batch;
    PolygonSpriteBatch polygonSpriteBatch;
    Sprite p1sprite;
    Texture p1Texture, p2Texture, hillTexture;
    Texture bulletTexture,aimTexture;
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
    TextureRegion aimRegion;
    float aimwidth,aimheight;
    float bulletsize;
    float aimAngle=0;

    public GameScreen2(MyGdxGame game,String gameId,int tankId1,int tankId2) {
        bullets=new HashMap<>();
        bodiestoremove=new Array<>();
        Preferences preferences=Gdx.app.getPreferences("preferences");
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

        String[] tankTextures1={"TankStarsFeature\\Tank4.png","TankStarsFeature\\Tank1.png","TankStarsFeature\\Tank6.png"};
        String[] tankTextures2={"TankStarsFeature\\Tank0.png","TankStarsFeature\\Tank5.png","TankStarsFeature\\Tank3.png"};

        p1Texture = prepareTexture(tankTextures1[tankId1]);

        p2Texture = prepareTexture(tankTextures2[tankId2]);

        bulletTexture=new Texture("TankStarsFeature\\bullet.png");

        bulletsize=screenHeight*PPM/60;



        aimTexture=new Texture("TankStarsFeature\\aim5.png");

        aimTexture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        aimRegion=new TextureRegion(aimTexture,0,0,800,32);
        aimheight=bulletsize;
        aimwidth=aimheight*((float)aimTexture.getWidth()/aimTexture.getHeight());
        // Camera
        camera = new OrthographicCamera();
//        camera.setToOrtho(false);
        viewport=new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();
        // Physics World
        world = new World(new Vector2(0, -9.8f), false);
        b2dr = new Box2DDebugRenderer();
        speedbreaker=new Array<>();
        hill = createBody(0, 0, 0, 0, true, false,"Hill");

        tankheigth = playerSize;
        float tankratio1 = (float) p1Texture.getWidth() / p1Texture.getHeight();
        float tankratio2=(float) p2Texture.getWidth()/ p2Texture.getHeight();
        tankwidth1 = tankratio1 * tankheigth;
        tankwidth2=tankratio2*tankheigth;


        p1sprite=new Sprite(p1Texture);
        p1sprite.setSize(tankwidth1,tankheigth);
        p1sprite.setOriginCenter();
//        p1sprite.setRotation(45);


        player1 = createBody(200, Gdx.graphics.getHeight() + playerSize / 2, tankwidth1, tankheigth, false, true,"p1");
        player2 = createBody(Gdx.graphics.getWidth() + 1500, Gdx.graphics.getHeight()+ playerSize / 2, tankwidth2, tankheigth, false, true,"p2");


        scores=new Scores(Game,Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),gameId,tankId1,tankId2);
//        Preferences preferences = Gdx.app.getPreferences("preferences");

//        if (!gameId.equals("")) {
            // p1 position , p2 position , p1 health , p2 health
//            String gameData = preferences.getString("game" + gameId, "0,0,0,0,0,0");
//            System.out.println("Load Saved Game: " + gameId + " Data: " + gameData);

//        }
        createcollisionhandler();
    }

    private Texture prepareTexture(String file) {
        Texture texture = new Texture(file);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return texture;
    }

    private Body createBody(float x, float y, float width, float height, boolean isStatic, boolean isBox,String data) {

        // Create Body
        BodyDef bodyDef = new BodyDef();

        if (isStatic) bodyDef.type = BodyDef.BodyType.StaticBody;
        else bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.fixedRotation = true;
        bodyDef.position.set(x / PPM, y / PPM);
        Body body = world.createBody(bodyDef);
        body.setUserData(data);

        // Set body Shape
        if (isBox) {
//            PolygonShape shape = new PolygonShape();
//            shape.setAsBox(width / 2 / PPM, height / 2 / PPM);
            CircleShape shape=new CircleShape();
            shape.setRadius(height/2/PPM);
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
                speedbreaker.add(lastX);
                vertices[i] = new Vector2(lastX, lastY);

                lastX = randomPoint(true, lastX, true);

                lastY = randomPoint(false, lastY, false);
                vertices[i + 1] = new Vector2(lastX, lastY);

                lastX = randomPoint(true, lastX, true);
                vertices[i + 2] = new Vector2(lastX, lastY);

                lastX = randomPoint(true, lastX, true);
                speedbreaker.add(lastX);
                lastY = randomPoint(false, lastY, true);
                vertices[i + 3] = new Vector2(lastX, lastY);
            }

            shape.createChain(vertices);
            FixtureDef hillfixturedef=new FixtureDef();
            hillfixturedef.shape=shape;
//            hillfixturedef.density=1f;
//            hillfixturedef.friction=1f;
//            hillfixturedef.restitution=0.1f;
            body.createFixture(hillfixturedef);
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

        float min = isXAxis ? (lastPoint + screenWidth / 2f) : (wantHigher ? (lastPoint + screenHeight / 4.5f) : (lastPoint - screenHeight / 4.5f));
        float max = isXAxis ? (lastPoint + screenWidth / 1.5f) : (wantHigher ? (lastPoint + screenHeight / 4f) : (lastPoint - screenHeight / 4f));

        return random.nextFloat() * (max - min) + min;
    }
    Array<Float> speedbreaker;
//    Array<Float> speedbreaker2;
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
    private float velocity=180f;
    private float velocity_y=-150f;
    private void handleTouch() {
        if(!hastanklanded) return;
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){

            player1.applyForce(new Vector2(-velocity,velocity_y),player1.getWorldCenter(),true);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){

            player1.applyForce(new Vector2(velocity,velocity_y),player1.getWorldCenter(),true);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            if(aimAngle<90)
                aimAngle++;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            if(aimAngle>-20)
                aimAngle--;
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            onfire();
        }

    }
    private int nextedge;
    private float p1y;
    private float p1x;
    private int transitionstep;
    private float rising=-10;
    private float falling=10;
    public void aftermovement(){
        float distance=speedbreaker.get(nextedge)-player1.getPosition().x;
//        float
        float backwards=0;
        if(nextedge>0){
            backwards=player1.getPosition().x-speedbreaker.get(nextedge-1);
        }
//            nextedge--;
        System.out.println(backwards);
        float multiplier=0.55f;
//        if(nextedge%2==0)
//            multiplier=0.45f;
        if( nextedge>0 && backwards<2)
            nextedge--;
        else if(distance<2)
            nextedge++;
        else if ((distance<15 && player1.getLinearVelocity().x>0) ) {

            player1.applyLinearImpulse(-player1.getLinearVelocity().x * multiplier, 0, player1.getWorldCenter().x, player1.getWorldCenter().y, true);

        }
        else if ((nextedge>0 && backwards<15 && player1.getLinearVelocity().x<0)) {

              player1.applyLinearImpulse(-player1.getLinearVelocity().x * multiplier, 0, player1.getWorldCenter().x, player1.getWorldCenter().y, true);
        }
        float p1ynew=player1.getPosition().y;
        float p1xnew=player1.getPosition().x;
        float currentrotation=p1sprite.getRotation();
        float rotationangle=0;

        if(p1ynew>p1y) {
            if(p1xnew<p1x){
                rotationangle=rising;
            }
            if(currentrotation==rising || (currentrotation==0 && transitionstep<30) )
                transitionstep++;
            else {
                rotationangle = falling;
                transitionstep=0;
            }
        }
        else if(p1ynew<p1y){
            if(p1xnew<p1x){
                rotationangle=falling;
            }
            if(currentrotation==falling || (currentrotation==0 && transitionstep<30) )
                transitionstep++;
            else {
                rotationangle = rising;
                transitionstep=0;
            }
        }
        p1y=p1ynew;
        p1x=p1xnew;

        p1sprite.setPosition(player1.getPosition().x * PPM - tankwidth1 / 2, player1.getPosition().y * PPM - tankheigth / 2);

        p1sprite.setRotation(hastanklanded?rotationangle:0);
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
    HashMap<String,Body> bullets;
    Array<String> bodiestoremove;
    float bulletspeed=25f;
    int bulletsfired;
    private void onfire(){
        bulletsfired++;
        String data="Bullet"+bulletsfired;
        Body bullet=createBody(player1.getPosition().x*PPM+playerSize+1,player1.getPosition().y*PPM+playerSize/2,bulletsize,bulletsize,false,true,data);
        float angle=aimAngle*((float)Math.PI/180f);
        bullet.setLinearVelocity(bulletspeed* MathUtils.cos(angle),bulletspeed*MathUtils.sin(angle));
        bullets.put(data,bullet);
    }
    boolean hastanklanded;
    private void createcollisionhandler(){
        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                String f1data=contact.getFixtureA().getBody().getUserData().toString();
                String f2data=contact.getFixtureB().getBody().getUserData().toString();

                if(f1data.equals("Hill") && f2data.equals("p1")){
                    hastanklanded=true;
                }
                else if(f1data.equals("p2") && f2data.contains("Bullet")){
                    bodiestoremove.add(f2data);
                    if(scores.healthbar2.getValue()>10)
                        scores.onHit(false);
                    else
                        System.out.println("Game Over!");
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }
    @Override
    public void render (float delta) {
        update(Gdx.graphics.getDeltaTime());


        ScreenUtils.clear(38 / 255f, 31 / 255f, 88 / 255f, 1);


        polygonSpriteBatch.setProjectionMatrix(camera.combined);
        polygonSpriteBatch.begin();
        polygonSpriteBatch.draw(polygonRegion, hill.getPosition().x, hill.getPosition().y);
        if(hastanklanded)
            polygonSpriteBatch.draw(aimRegion,p1sprite.getX()+p1sprite.getWidth(),p1sprite.getY()+p1sprite.getHeight()/2,0,0,aimwidth*8,aimheight,1f,1f,aimAngle);

        polygonSpriteBatch.end();


        Game.batch.setProjectionMatrix(camera.combined);
        Game.batch.begin();
        Game.batch.draw(button1, middlex, middley,buttonwidth,buttonheight);
//        Game.batch.draw(p1Texture, player1.getPosition().x * PPM - tankwidth1 / 2, player1.getPosition().y * PPM - tankheigth / 2, tankwidth1, tankheigth);
        p1sprite.draw(Game.batch);
        Game.batch.draw(p2Texture, player2.getPosition().x * PPM - tankwidth2 / 2, player2.getPosition().y * PPM - tankheigth / 2, tankwidth2, tankheigth);
        Game.batch.end();


        Game.batch.setProjectionMatrix(scores.stage.getCamera().combined);
        scores.stage.draw();

        handleTouch();
        aftermovement();

        b2dr.render(world, camera.combined.scl(PPM));
        for(String bodyId: bodiestoremove){
            world.destroyBody(bullets.get(bodyId));
            bullets.remove(bodyId);
        }
        bodiestoremove.clear();
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
