package com.sample.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class MyGdxGame extends Game {
	SpriteBatch batch;
	public BitmapFont font;
	FreeTypeFontGenerator fontGenerator;
	FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("TankStarsFeature\\Lato-Regular.ttf"));
		fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = 40;
		fontParameter.borderWidth = 2;
		fontParameter.borderColor = Color.BLACK;
		fontParameter.color = Color.WHITE;
		font = fontGenerator.generateFont(fontParameter);
		setScreen(new MainScreen(this));
	}


	public void drawImage(Texture image,float x,float y){
		batch.draw(image, x,y);
	}
	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
