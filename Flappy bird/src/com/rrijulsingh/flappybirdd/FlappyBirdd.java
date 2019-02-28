package com.rrijulsingh.flappybirdd;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBirdd extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	int flapstate=0;
	float birdY=0;
	float velocity=0;
	int gamestate=0;
	Texture toptube;
	Texture bottomtube;
	float gap=400;
	float maxoffset; //amount tube moved up and down
	Random randomGenerator;
	float tubevelocity=4;
	int nooftubes=4;
	float Distancebetweentubes;
	float[] tubex=new float[nooftubes];
    float[] tubeOffSet=new float[nooftubes];
    Circle birdcircle;
    ShapeRenderer shapeRenderer; //just like a batch which enables us to make shapes.
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;
    int score=0;
    int scoringtube=0;
    BitmapFont font;
Texture gameover;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png"); // texture is just like an image. it is called texture in games.
        birds=new Texture[2];
        font=new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
        gameover=new Texture("newgameover.jpg");
        birds[0]=new Texture("bird.png");
        birds[1]=new Texture("bird2.png");
        toptube=new Texture("toptube.png");
        maxoffset=Gdx.graphics.getHeight()/2-gap/2-100;
        randomGenerator=new Random();
        bottomtube=new Texture("bottomtube.png");
        Distancebetweentubes=Gdx.graphics.getWidth()*3/4;
        topTubeRectangles=new Rectangle[nooftubes];
        bottomTubeRectangles=new Rectangle[nooftubes];

        birdcircle=new Circle();
shapeRenderer=new ShapeRenderer();
startGame();
	}

public void startGame(){

    birdY=Gdx.graphics.getHeight()/2- birds[flapstate].getHeight()/2;
    for(int i=0; i<nooftubes;i++){
        tubeOffSet[i]=(randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-gap-750);
        //generates a number between 0 and 1
        tubex[i]=Gdx.graphics.getWidth()/2-toptube.getWidth()/2 + Gdx.graphics.getWidth() +  i * Distancebetweentubes;
        topTubeRectangles[i]=new Rectangle();
        bottomTubeRectangles[i]=new Rectangle();

    }

}
	@Override
	public void render () {
        batch.begin(); //tell the render methoda that we start the sprites now.
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gamestate == 1) {
            if(tubex[scoringtube]<Gdx.graphics.getWidth()/2){
                score++;
                Gdx.app.log("score", String.valueOf(score));

                if(scoringtube< nooftubes - 1){

                    scoringtube++;
                }

                else
                    {

                    scoringtube=0;
                }

             }
            if (Gdx.input.justTouched()) {
                velocity=-20;

            }
            for(int i=0; i<nooftubes;i++) {
                if(tubex[i]< -toptube.getWidth()){
                    tubex[i]+=nooftubes*Distancebetweentubes;
                    tubeOffSet[i]=(randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-gap-750);
                }
                else {
                    tubex[i] = tubex[i] - tubevelocity;
                }
                batch.draw(toptube, tubex[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i]);
                batch.draw(bottomtube, tubex[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffSet[i]);
                topTubeRectangles[i]=new Rectangle(tubex[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i],toptube.getWidth(),toptube.getHeight());
                bottomTubeRectangles[i]=new Rectangle(tubex[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffSet[i],bottomtube.getWidth(),bottomtube.getHeight());
            }

            if(birdY>0) {
                velocity++;
                birdY -= velocity;
            }else{
                gamestate=2; //if the bird is not at the top of the screen.
            }

        }else if(gamestate==0){
            if (Gdx.input.justTouched()) {
                gamestate = 1;
            }
        }else if(gamestate==2){

            batch.draw(gameover,Gdx.graphics.getWidth()/2- gameover.getWidth()/2,Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);
            if (Gdx.input.justTouched()) {
                gamestate = 1;
                startGame();
                score=0;
                scoringtube=0;
                velocity=0;
            }
        }
        if (flapstate == 0) {
            flapstate = 1;
        } else {
            flapstate = 0;
        }

        batch.draw(birds[flapstate], Gdx.graphics.getWidth() / 2 - birds[flapstate].getWidth() / 2, birdY);
        font.draw(batch,String.valueOf(score),100,200);
        batch.end();
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.RED);
        birdcircle.set(Gdx.graphics.getWidth()/2,birdY + birds[flapstate].getHeight()/2,birds[flapstate].getWidth()/2);
        //shapeRenderer.circle(birdcircle.x,birdcircle.y,birdcircle.radius);
        for(int i=0; i<nooftubes;i++) {
          //  shapeRenderer.rect(tubex[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffSet[i],toptube.getWidth(),toptube.getHeight());
            //shapeRenderer.rect(tubex[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffSet[i],bottomtube.getWidth(),bottomtube.getHeight());
            if(Intersector.overlaps(birdcircle,topTubeRectangles[i])|| Intersector.overlaps(birdcircle,bottomTubeRectangles[i])){
                gamestate=2; //means the game is over.
            }

        }
        //shapeRenderer.end();

    }
	

}
