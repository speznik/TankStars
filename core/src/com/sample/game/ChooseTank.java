package com.sample.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ChooseTank {
    OrthographicCamera camera;
    Viewport viewport;
    MyGdxGame Game;
    SpriteBatch sp;
    Texture tank1;
    Texture tank2;
    Texture tank3;
    Image t1;
    Image t2;
    Image t3;
    public ChooseTank(SpriteBatch sp) {
        this.sp=sp;
        tank1=new Texture("TankStarsFeature\\Tank0.png");
        tank2=new Texture("TankStarsFeature\\Tank1.png");
        tank3=new Texture("TankStarsFeature\\Toxictank.jpg");
        t1=new Image(tank1);
        t2=new Image(tank2);
        t3=new Image(tank3);
        Table table=new Table();
        table.top().left();
        table.setFillParent(true);
    }


}
