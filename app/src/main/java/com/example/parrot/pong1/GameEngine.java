package com.example.parrot.pong1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {

    // -----------------------------------
    // ## ANDROID DEBUG VARIABLES
    // -----------------------------------

    // Android debug variables
    final static String TAG="PONG-GAME";

    // -----------------------------------
    // ## SCREEN & DRAWING SETUP VARIABLES
    // -----------------------------------

    // screen size
    int screenHeight;
    int screenWidth;

    int middleOfTheScreen = screenWidth/2;
    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;

    int score;

    // -----------------------------------
    // ## GAME SPECIFIC VARIABLES
    // -----------------------------------

    // ----------------------------
    // ## SPRITES
    // ----------------------------
    int ballXPosition;      // keep track of ball -x
    int ballYPosition;      // keep track of ball -y



    //Racket position

    int racketXPosition;
    int racketYPosition;


    boolean racketIsMovingLeft;
    boolean racketIsMovingRight;

    // ----------------------------
    // ## GAME STATS - number of lives, score, etc
    // ----------------------------


    public GameEngine(Context context, int w, int h) {
        super(context);


        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;


        this.printScreenInfo();

        // @TODO: Add your sprites to this section
        // This is optional. Use it to:
        //  - setup or configure your sprites
        //  - set the initial position of your sprites
        this.ballXPosition = this.screenWidth / 2;
        this.ballYPosition = this.screenHeight / 2;

        this.racketXPosition = 250;
        this.racketYPosition = 1500;

        // @TODO: Any other game setup stuff goes here


    }

    // ------------------------------
    // HELPER FUNCTIONS
    // ------------------------------

    // This funciton prints the screen height & width to the screen.
    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }


    // ------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------
    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // ------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------

    String directionBallIsMoving = "down";
    String personTapped = "";



    // 1. Tell Android the (x,y) positions of your sprites
    public void updatePositions() {
        // @TODO: Update the position of the sprites






        // 1.make ball bounce of left wall
            if (directionBallIsMoving == "down"){
                this.ballYPosition = this.ballYPosition + 10;


                if(this.ballYPosition >= this.screenHeight){
                    directionBallIsMoving = "up";

                }

            }
            if(directionBallIsMoving == "up"){
                this.ballYPosition = this.ballYPosition - 10;

                if(this.ballYPosition <= 0){
                    directionBallIsMoving = "down";
                }
            }

            if(personTapped.contentEquals("right")){
                this.racketXPosition = this.racketXPosition + 10;
                if(racketXPosition > screenWidth){
                    directionBallIsMoving = "left";
                }
            }
            else if(personTapped.contentEquals("left")){
                this.racketXPosition = this.racketXPosition - 10;
//                if(racketXPosition <= 0){
//                    directionBallIsMoving = "right";
//                }
            }
            //2. change the direction of the racket




        // 1. calculate a new position for the ball!



//        this.ballYPosition = this.ballYPosition - 10;

//        // LEFT:
//        this.ballXPosition = this.ballXPosition - 10;
//        // RIGHT:
//        this.ballXPosition = this.ballXPosition + 10;
//        // DOWN:
//        this.ballYPosition = this.ballYPosition + 10;
//        // UP:
//        this.ballYPosition = this.ballYPosition - 10;


        // @TODO: Collision detection code

if(ballYPosition >= racketYPosition){
    Log.d(TAG,"touching racket");
    directionBallIsMoving = "up";
    score = score + 1;
}



    }

    // 2. Tell Android to DRAW the sprites at their positions
    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------
            // Put all your drawing code in this section

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255,0,0,255));
            paintbrush.setColor(Color.WHITE);

            //@TODO: Draw the sprites (rectangle, circle, etc)

            // 1. Draw the ball
            this.canvas.drawRect(
                    ballXPosition,
                    ballYPosition,
                    ballXPosition + 50,
                    ballYPosition + 50,
                    paintbrush);
            // this.canvas.drawRect(left, top, right, bottom, paintbrush);


            //2. Draw the racket
            paintbrush.setColor(Color.YELLOW);
            this.canvas.drawRect(this.racketXPosition,this.racketYPosition,this.racketXPosition+400,this.racketYPosition + 50,paintbrush);

            paintbrush.setColor(Color.WHITE);
            //@TODO: Draw game statistics (lives, score, etc)
            paintbrush.setTextSize(60);
            canvas.drawText("Score :" + score, 20, 100, paintbrush);

            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    // Sets the frame rate of the game
    public void setFPS() {
        try {
            gameThread.sleep(50);
        }
        catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_DOWN) {
            // user pushed down on screen

            float fingerXPosition = event.getX();
            float fingerYPosition = event.getY();


            if(fingerXPosition <= middleOfTheScreen){

                personTapped = "left";

            }else if(fingerXPosition > middleOfTheScreen){


                personTapped = "right";

            }

            Log.d(TAG,"Person's pressed:"+ fingerXPosition+","+fingerYPosition);
        }
        else if (userAction == MotionEvent.ACTION_UP) {
            // user lifted their finger
        }
        return true;
    }
}