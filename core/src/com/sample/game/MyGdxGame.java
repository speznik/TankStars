package com.sample.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends Game {
	SpriteBatch batch;
	Texture button1;
	Texture button2;
	Texture button3;
	Texture homepage;
	public BitmapFont font;
	FreeTypeFontGenerator fontGenerator;
	FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
	private float middlex;
	private float middley;
	private float button2y;
	private float button3y;
	private float screen;
	private float image;


	@Override
	public void create () {
		batch = new SpriteBatch();
		font=new BitmapFont();
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("TankStarsFeature\\Lato-Regular.ttf"));
		fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = 40;
		fontParameter.borderWidth = 2;
		fontParameter.borderColor = Color.BLACK;
		fontParameter.color = Color.WHITE;
		font = fontGenerator.generateFont(fontParameter);
		setScreen(new MainScreen(this));
//		img = new Texture("TankStarsFeature\\download.jpg");
//		button1 = new Texture("TankStarsFeature\\Button1.png");
//		button2 = new Texture("TankStarsFeature\\Button2.png");
//		button3 = new Texture("TankStarsFeature\\Button3.png");
//		homepage=new Texture("TankStarsFeature\\HomePage.jpg");
//		middlex=Gdx.graphics.getWidth()/2-button1.getWidth()/2;
//		middley=Gdx.graphics.getHeight()/2;
//		button2y=middley-button1.getHeight()-20;
//		button3y=middley-button2.getHeight()-button1.getHeight()-40;
//		screen=Gdx.graphics.getWidth();
//		image=screen*(screen/homepage.getHeight());
	}


	public void drawImage(Texture image,float x,float y){
		batch.draw(image, x,y);
	}
	@Override
	public void render () {
//		ScreenUtils.clear(0, 0, 0, 1);

		//for drawing images
		// for putting buttons in middle Gdx.graphics.getWidth()
//		batch.begin();
//		batch.draw(homepage,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
//		drawImage(button1,middlex,middley);
//		drawImage(button2,middlex,button2y);
//		drawImage(button3,middlex,button3y);

		super.render();
//		batch.draw(img, 50, 50);
		//for writing text
//		font.draw(batch, "Welcome to Drop!!! ", 100, 150);
//		font.draw(batch, "Tap anywhere to begin!", 100, 100);
//		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
//		homepage.dispose();
//		button1.dispose();
//		button2.dispose();
//		button3.dispose();
		font.dispose();
	}
}
