package com.sample.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Scores implements Disposable {
    SpriteBatch sp;

    // Stage to Draw Scene2D Actors
    Stage stage;

    Viewport viewport;


    // To Keep Track of Pause Menu Visibility
    boolean menuShowing;

    // Game Over Message Label
    protected Label gameOverShowing;
    private Label p1scorelabel;
    private Label p2scorelabel;
    private Label p3scorelabel;
    private Label p4scorelabel;
    private float PPM = 32;
    Image menu;
    ProgressBar healthbar1;
    ProgressBar healthbar2;
    ProgressBar fuelbar1;
    ProgressBar fuelbar2;
    Image resumegamebutton;
    Image savegamebutton;
    Image exitgamebutton;

    ImageButton menubutton;

    // Skin for Designing Scene2d components
    Skin skin;
    Skin skin1;
    MyGdxGame game;

    public Scores(MyGdxGame game, final GameScreen2 gameScreen2, float width, float height, String gameId, final int tankId1, final int tankId2) {
        this.sp = game.batch;
        this.game = game;

        final Preferences preferences = Gdx.app.getPreferences("preferences");


        // Default Game Data
        final String saved = preferences.getString("saved", "");
        String[] gameData = new String[]{"0", "0", "0", "0", "100", "100", "100", "100", "" + tankId1, "" + tankId2};

        // Load Saved Game from Preferences
        if (!gameId.equals(""))
            gameData = preferences.getString("game" + gameId, "0,0,0,0,100,100,100,100," + tankId1 + "," + tankId2).split(",");


        viewport = new FitViewport(width, height, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        final Table table = new Table();
        table.top();
        table.setFillParent(true);
        skin = new Skin(Gdx.files.internal("comic/skin/comic-ui.json"));
        skin.getFont("font").getData().setScale(6);
        skin1 = new Skin(Gdx.files.internal("pixthulhu/skin/pixthulhu-ui.json"));
        skin1.getFont("font").getData().setScale(6);
        p1scorelabel = new Label("Health", skin);
        p2scorelabel = new Label("Health", skin);
        p3scorelabel = new Label("Fuel", skin);
        p4scorelabel = new Label("Fuel", skin);

        Texture texture = new Texture("TankStarsFeature\\Component1.png");
        Texture texture3 = new Texture("TankStarsFeature\\Button2.png");
        Texture texture4 = new Texture("TankStarsFeature\\savegame.jpg");
        Texture texture5 = new Texture("TankStarsFeature\\Button3.png");

        menu = new Image(texture);
        healthbar1 = new ProgressBar(0, 100, 5, false, skin);
        healthbar2 = new ProgressBar(0, 100, 5, false, skin);

        fuelbar1 = new ProgressBar(0, 100, 0.5f, false, skin1);
        fuelbar2 = new ProgressBar(0, 100, 0.5f, false, skin1);

        // Set Progressbar values from game data
        fuelbar1.setValue(Float.parseFloat(gameData[6]));
        fuelbar2.setValue(Float.parseFloat(gameData[7]));

        healthbar1.setValue(Float.parseFloat(gameData[4]));
        healthbar2.setValue(Float.parseFloat(gameData[5]));

        // Pause Menu Button Listeners
        menu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                menuShowing = !menuShowing;
                resumegamebutton.setVisible(menuShowing);
                savegamebutton.setVisible(menuShowing);
                exitgamebutton.setVisible(menuShowing);
            }
        });
        table.add(menu).align(Align.left).pad(10);
        table.row();
        table.add(p1scorelabel).align(Align.left).pad(10).expandX();
        table.add(p2scorelabel).align(Align.right).pad(10);
        table.row().pad(20);
        table.add(healthbar1).width(width / 2 - 40);
//        table.row()
        table.add(healthbar2).width(width / 2 - 40).padLeft(20);
        table.row();
        table.add(p3scorelabel).align(Align.left).pad(10).expandX();
        table.add(p4scorelabel).align(Align.right).pad(10);
        table.row().pad(20);
        table.add(fuelbar1).width(width / 3 - 40).align(Align.left);
        table.add(fuelbar2).width(width / 3 - 40).align(Align.right).padLeft(20);
        table.row();

        resumegamebutton = new Image(texture3);
        savegamebutton = new Image(texture4);
        exitgamebutton = new Image(texture5);
        resumegamebutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                menuShowing = !menuShowing;
                resumegamebutton.setVisible(menuShowing);
                savegamebutton.setVisible(menuShowing);
                exitgamebutton.setVisible(menuShowing);
            }
        });

        savegamebutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                // Set Game Id to current time
                long gameId = System.currentTimeMillis();
                String newSaved = gameId + "," + saved;

                // Save Vertices
                String verticesarray = "";
                for (int i = 0; i < gameScreen2.vertices.length; i++) {
                    String comma = i == 0 ? "" : ",";
                    verticesarray += comma + gameScreen2.vertices[i].x + "," + gameScreen2.vertices[i].y;

                }

                // Put Strings in Prefs
                preferences.putString("saved", newSaved); // list of games
                preferences.putString("game" + gameId, gameScreen2.player1.getPosition().x * PPM + "," + gameScreen2.player1.getPosition().y * PPM + "," + gameScreen2.player2.getPosition().x * PPM + "," + gameScreen2.player2.getPosition().y * PPM + "," + healthbar1.getValue() + "," + healthbar2.getValue() + "," + fuelbar1.getValue() + "," + fuelbar2.getValue() + "," + tankId1 + "," + tankId2); // single game
                preferences.putString("vertices" + gameId, verticesarray);
                preferences.flush();

                menuShowing = !menuShowing;
                resumegamebutton.setVisible(menuShowing);
                savegamebutton.setVisible(menuShowing);
                exitgamebutton.setVisible(menuShowing);

            }
        });

        exitgamebutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                goToMainScreen();
            }
        });
        Table table2 = new Table();
        table2.setFillParent(true);
        table2.top().padTop(300);
        table2.add(resumegamebutton);
        table2.row().padTop(20).padBottom(10);
        table2.add(savegamebutton);
        table2.row().pad(10);
        table2.add(exitgamebutton);
        gameOverShowing = new Label("Game Over", skin);
        table2.row().padTop(100);
        table2.add(gameOverShowing).expandX();
        gameOverShowing.setVisible(false);
        resumegamebutton.setVisible(menuShowing);
        savegamebutton.setVisible(menuShowing);
        exitgamebutton.setVisible(menuShowing);


        // add tables to stage
        stage.addActor(table);
        stage.addActor(table2);

        // Allow Input on Scene2d stage
        Gdx.input.setInputProcessor(stage);
    }

    // Called when tank is hit
    public void onHit(boolean isTank1, int deduct) {
        if (isTank1) healthbar1.setValue(healthbar1.getValue() - deduct);
        else healthbar2.setValue((healthbar2.getValue() - deduct));
    }

    // Called on tank movememnet
    public void reducedfuel(boolean isTank1) {
        if (isTank1) fuelbar1.setValue(fuelbar1.getValue() - 0.5f);
        else fuelbar2.setValue((fuelbar2.getValue() - 0.5f));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void goToMainScreen() {
        game.setScreen(new MainScreen(game));
        dispose();
    }
}
