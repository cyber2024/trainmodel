package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Russell Elfenbein on 9/21/2015.
 */
public class Train {
    BitmapFont font = new BitmapFont();
    private float drawBarLength = 4f;
    private float minDist = 0.5f*drawBarLength, maxDist = drawBarLength;

    private int numWagons = 5;
    private ArrayList<Wagon> wagons;

    public Train(){
        wagons = new ArrayList<Wagon>();
        for(int i = 0; i < numWagons; i++){
            wagons.add(new Wagon());
            wagons.get(i).position.set(-(i*wagons.get(i).size.x +minDist*i),2.5f);
        }
    }

    public void update(float delta){
        for(Wagon wagon : wagons){
            wagon.acceleration.set(wagon.forceResultant.x/wagon.mass_chassis,
                    wagon.forceResultant.y/wagon.mass_chassis);
            wagon.velocity.add(wagon.acceleration);
            wagon.position.add(wagon.velocity.cpy().scl(delta));
            wagon.centre = new Vector2(wagon.position.x+wagon.size.x*0.5f,
                    wagon.position.y+wagon.size.y*0.5f);
        }
    }

    public void checkCollision(float delta){

    }

    public float calcCollissionTime(int i, float delta){

        return 0;
    }

    public int getCollision(){
        Wagon prevWagon, wagon, nextWagon;
        int indexMO = -1, indexMS = -1, index = -1;
        float maxSpacing = 0, maxOverlap = 0, tmpOverlap = 0;

        for(int i = 0; i < wagons.size(); i++){
            wagon = wagons.get(i);
            if(i < wagons.size()-1){
                nextWagon = wagons.get(i+1);
                tmpOverlap = wagon.position.x - nextWagon.position.x-wagon.size.x;
                if(tmpOverlap < minDist){
                    if(tmpOverlap<maxOverlap){
                        maxOverlap = tmpOverlap+minDist;
                        indexMO = i;
                        Gdx.app.log("Collision at wagon maxoverlap",""+i);
                    }
                }
                if(tmpOverlap>maxDist){
                    if(tmpOverlap>maxSpacing)
                        maxSpacing = tmpOverlap-maxDist;
                        indexMS = i;
                    Gdx.app.log("Collision at wagon maxspacing",""+i);
                }
            }
            if(maxOverlap > maxSpacing){
                Gdx.app.log("Collision at wagon ",""+i);
                return i;
            }
        }
        return -1;
    }

    public void physics(float delta){
        Wagon prevWagon, wagon, nextWagon;
        int index = -1;
        float maxSpacing = 0, maxOverlap = 0, tmpOverlap = 0;

        index = getCollision();


        for(int i = 0; i < wagons.size(); i++) {
            wagon = wagons.get(i);

            wagon.acceleration.set(wagon.forceResultant.x / wagon.mass_chassis,
                    wagon.forceResultant.y / wagon.mass_chassis);
            wagon.velocity.add(wagon.acceleration);
            wagon.position.add(wagon.velocity.cpy().scl(delta));

            if(i>0){
                //test distance and velocity of wagon ahead
                prevWagon = wagons.get(i-1);
                if(wagon.position.dst(prevWagon.position) < wagon.size.x ){
                    prevWagon.position.set(wagon.position.x+wagon.size.x, 2.5f);
                    Vector2 tmp = prevWagon.velocity.add(wagon.velocity).scl(0.5f);
                    prevWagon.velocity.set(tmp);
                    wagon.velocity.set(tmp);
                }



            }
            if(i<wagons.size()-1){
                //test wagons behind
                nextWagon = wagons.get(i+1);
                if(wagon.position.dst(nextWagon.position) > wagon.size.x +maxDist){
                    nextWagon.position.set(wagon.position.x-wagon.size.x-maxDist, 2.5f);
                    Vector2 tmp = nextWagon.velocity.add(wagon.velocity).scl(0.5f);
                    wagon.velocity.set(tmp);
                    nextWagon.velocity.set(tmp);
                }


            }
            //wagon.forceResultant.set(wagon.forceFront.add(wagon.forceRear.add(wagon.forceBreaks)));

            //add resultant forceResultant
        }
    }

    public void draw(SpriteBatch batch){
        font.setColor(1,0,0,1);
        font.getData().setScale(0.1f,0.1f);
        for (Wagon wagon : wagons) {
            AssetLoader.wagonS.setPosition(wagon.position.x, wagon.position.y);

            float x = wagon.velocity.x;
            if(wagon.velocity.x >= 0)
                AssetLoader.wagonS.setColor(1f*(5f/x), 1f, 1f*(5f/x), 1f);
            if(wagon.velocity.x < 0)
                AssetLoader.wagonS.setColor(1f, 1f*(-5f/x), 1f*(-5f/x), 1f);
            AssetLoader.wagonS.draw(batch);

            font.draw(batch,"Vel: "+Math.round(wagon.velocity.x), wagon.position.x, wagon.position
                   .y+2f);
        }
    }

    public boolean stop(int i){
        if(i<wagons.size()) {
            //wagons.get(i).velocity.set(0, 0);
            wagons.get(i).forceResultant.x -= 100;
            return true;
        }
        return false;
    }
    public boolean start(int i){
        if(i<wagons.size()) {
            //wagons.get(i).velocity.set(0, 0);
            wagons.get(i).forceResultant.x += 100;
            return true;
        }
        return false;
    }
    public boolean zero(int i){
        if(i<wagons.size()) {
            //wagons.get(i).velocity.set(0, 0);
            wagons.get(i).forceResultant.x = 0;
            return true;
        }
        return false;
    }

    public void reset(){
        wagons.clear();
        wagons = new ArrayList<Wagon>();
        for(int i = 0; i < numWagons; i++){
            wagons.add(new Wagon());
            wagons.get(i).position.set(-(i*wagons.get(i).size.x +minDist*i),2.5f);
        }
    }
}
