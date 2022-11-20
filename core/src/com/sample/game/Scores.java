package com.sample.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sun.tools.jconsole.Tab;

public class Scores implements Disposable {
    SpriteBatch sp;
    Stage stage;
    Viewport viewport;
    int p1score=100;
    int p2score=100;
    boolean isGamePaused;
    boolean isGameOver;
    boolean menuShowing;
    private Label p1scorelabel;
    private Label p2scorelabel;
    Image menu;
    ProgressBar healthbar1;
    ProgressBar healthbar2;
    Image resumegamebutton;
    Image savegamebutton;
    Image exitgamebutton;
    ImageButton menubutton;
    Skin skin;
    MyGdxGame game;
    public Scores(MyGdxGame game,float width,float height,String gameId){
        this.sp=game.batch;
        this.game=game;
        final Preferences preferences=Gdx.app.getPreferences("preferences");
        final String saved = preferences.getString("saved", "");
        String[] gameData=new String[]{"0","0","0","0","100","100"};
        if(!gameId.equals("")) gameData = preferences.getString("game" + gameId, "0,0,0,0,100,100").split(",");
        viewport= new FitViewport(width,height,new OrthographicCamera());
        stage=new Stage(viewport, game.batch);

        final Table table =new Table();
        table.top();
        table.setFillParent(true);
        skin = new Skin(Gdx.files.internal("comic/skin/comic-ui.json"));
        skin.getFont("font").getData().setScale(6);
//        p1scorelabel=new Label("100",skin);
//        p2scorelabel=new Label("100",skin);

//        menubutton=new ImageButton();
        Texture texture=new Texture("TankStarsFeature\\Component1.png");
//        Texture texture1=new Texture("TankStarsFeature\\HealthBar.jpg");
//        Texture texture2=new Texture("TankStarsFeature\\HealthBar.jpg");
        Texture texture3=new Texture("TankStarsFeature\\Button2.png");
        Texture texture4=new Texture("TankStarsFeature\\savegame.jpg");
        Texture texture5=new Texture("TankStarsFeature\\Button3.png");
        menu=new Image(texture);
        healthbar1=new ProgressBar(0,100,5,false,skin);
        healthbar2=new ProgressBar(0,100,5,false,skin);
        healthbar1.setValue(Float.parseFloat(gameData[4]));
        healthbar2.setValue(Float.parseFloat(gameData[5]));

        menu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                menuShowing=!menuShowing;
                resumegamebutton.setVisible(menuShowing);
                savegamebutton.setVisible(menuShowing);
                exitgamebutton.setVisible(menuShowing);
            }
        });
//        TextureRegion textureRegion=new TextureRegion(texture);
//        menu.setDrawable(skin,new TextureRegionDrawable(textureRegion));
        table.add(menu).align(Align.left).pad(10);
        table.row();
        table.add(p1scorelabel).align(Align.left).pad(10).expandX();
        table.add(p2scorelabel).align(Align.right).pad(10);
        table.row().pad(20);
        table.add(healthbar1).width(width/2-40);
//        table.row()
        table.add(healthbar2).width(width/2-40).padLeft(20);
//
        table.row();

        resumegamebutton=new Image(texture3);
        savegamebutton=new Image(texture4);
        exitgamebutton=new Image(texture5);
        resumegamebutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                menuShowing=!menuShowing;
                resumegamebutton.setVisible(menuShowing);
                savegamebutton.setVisible(menuShowing);
                exitgamebutton.setVisible(menuShowing);
            }
        });

        savegamebutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                long gameId = System.currentTimeMillis();
                String newSaved = gameId + "," + saved;

                preferences.putString("saved", newSaved);
                preferences.putString("game" + gameId, "0,0,0,0,"+healthbar1.getValue()+","+healthbar2.getValue());
                preferences.flush();

                menuShowing=!menuShowing;
                resumegamebutton.setVisible(menuShowing);
                savegamebutton.setVisible(menuShowing);
                exitgamebutton.setVisible(menuShowing);

            }
        });

        exitgamebutton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                goToMainScreen();
            }
        });
        Table table2=new Table();
        table2.setFillParent(true);
        table2.top().padTop(225);
        table2.add(resumegamebutton);
        table2.row().padTop(20).padBottom(10);
        table2.add(savegamebutton);
        table2.row().pad(10);
        table2.add(exitgamebutton);
        resumegamebutton.setVisible(menuShowing);
        savegamebutton.setVisible(menuShowing);
        exitgamebutton.setVisible(menuShowing);


        stage.addActor(table);
        stage.addActor(table2);
        Gdx.input.setInputProcessor(stage);
    }
//    public void showHidebuttons(){
//        table.add(resumegamebutton).pad(10);
//        table.row();
//        table.add(savegamebutton).pad(10);
//        table.row();
//        table.add(exitgamebutton).pad(10);
//    }
    @Override
    public void dispose() {
        stage.dispose();
    }
    public void goToMainScreen(){
        game.setScreen(new MainScreen(game));
        dispose();
    }
}
