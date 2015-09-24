package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Russell Elfenbein on 9/23/2015.
 */
public class Wagon2 {
    public Vector2 position, velocity, acceleration, size, forceResultant;
    public float mass,momentum, rearSpacing, collisionTime;

    public Wagon2(){
        size = new Vector2(11,6);
        position = new Vector2();
        velocity = new Vector2();
        forceResultant = new Vector2();
        acceleration = new Vector2();
        mass  = 24000/9.8f + 148000/9.8f;
        momentum = 0f;
        collisionTime = -1;
    }
}
