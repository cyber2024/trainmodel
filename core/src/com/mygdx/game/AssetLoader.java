package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Russell Elfenbein on 9/21/2015.
 */
public class AssetLoader {
    public static Texture wagonT;
    public static Sprite wagonS;

    public static void load(){
        wagonT = new Texture("wagon.png");
        wagonS = new Sprite(wagonT);

    }
}
