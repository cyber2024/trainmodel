package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Russell Elfenbein on 9/21/2015.
 */
public class Wagon {
    public Vector2 position, velocity, acceleration, forceResultant, size, forceFront, forceRear,
            forceBreaks, centre;
    public float mass_chassis, mass_load, rollingFriction, stressFront, strainFront, momentum;
    public int index;



    public Wagon(){
        size = new Vector2(11,6);
        position = new Vector2();
        centre = new Vector2(position.x+size.x*0.5f, position.y+size.y*0.5f);
        velocity = new Vector2();
        acceleration = new Vector2();
        forceResultant =  new Vector2();
        rollingFriction = 0;
        forceFront = new Vector2();
        forceRear = new Vector2();
        forceBreaks = new Vector2();

        mass_chassis  = 24000/9.8f;
        mass_load = 148000/9.8f;

    }



    public void physics(Vector2 fFront, Vector2 fRear, Vector2 fBreaks){

    }
    public void update(float delta){

    }

}
