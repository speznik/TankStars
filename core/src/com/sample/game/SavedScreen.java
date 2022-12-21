package com.sample.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sample.game.MyGdxGame;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SavedScreen implements Screen {
    MyGdxGame game;
    Preferences preferences;

    Stage stage;

    Skin skin;
    OrthographicCamera camera;
    Viewport viewport;

    public SavedScreen(final MyGdxGame game) {
        camera = new OrthographicCamera();

        // To Fill Screen
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();

        this.game = game;
        preferences = Gdx.app.getPreferences("preferences");

        // List Of Games
        String savedGames = preferences.getString("saved", "1532262046724,1532261046024");
        if (savedGames.equals("")) return;

        // Load in Array
        String[] savedGamesArray = savedGames.split(",");

        // Create Stage with viewport and spritebatch
        stage = new Stage(new ScreenViewport(), game.batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);


        skin = new Skin(Gdx.files.internal("tubular/skin/tubular-ui.json"));
        skin.getFont("font").getData().setScale(4);


        // Loop through saved games list and add each on table
        for (int i = 0; i < savedGamesArray.length; i++) {
            table.row().pad(20);

            final String gameId = savedGamesArray[i];


            // Play Button to load the game
            TextButton playButton =new TextButton("Play", skin);
            playButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    game.setScreen(new GameScreen2(game, gameId,0,0));
                    dispose();
                }
            });

            table.add(new Label("Saved Game " + (i + 1), skin)).left();
            table.add(new Label(dateFormatted(Long.parseLong(gameId)), skin)).expandX();
            table.add(playButton);
        }

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    private String dateFormatted(long ms) {
        DateFormat format = new SimpleDateFormat("MMM dd HH:mm");
        Date date = new Date(ms);
        return format.format(date);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(90 / 255f, 31 / 255f, 175 / 255f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        stage.draw();


    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        skin.dispose();
    }
}