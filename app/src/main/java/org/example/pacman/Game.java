package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

/**
 *
 * This class should contain all your game logic
 */

public class Game {
    //context is a reference to the activity
    private Context context;
    private int points = 0; //how many points do we have
    private boolean running = false;
    private int direction;
    //bitmap of the pacman
    private Bitmap pacBitmap;
    //bitmap of the coins
    private Bitmap coinBitmap;
    //bitmap of the enemy
    private Bitmap enemyBitmap;
    //textview reference to points
    private TextView pointsView;
    //textview reference to counter
    private TextView counterView;
    private int level = 0;
    private boolean gameWon = false;
    private int pacx, pacy;
    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();
    //the list of enemies - I am using only one enemy but if the in the future there will be more
    //enemies implemented then an array is needed
    private ArrayList<Enemy> enemies = new ArrayList<>();
    //a reference to the gameview
    private GameView gameView;
    private int h,w; //height and width of screen

    public Game(Context context, TextView view)
    {
        this.context = context;
        this.pointsView = view;
        this.counterView = view;
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.goldcoin);
        // Resize of the coin
        coinBitmap = Bitmap.createScaledBitmap(coinBitmap, 50, 50, true);
        enemyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        // Resize of the enemy
        enemyBitmap = Bitmap.createScaledBitmap(enemyBitmap, 100, 100, true);
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    public void newGame()
    {
        //starting coordinates for pacman
        pacx = 50;
        pacy = 200;

        //reset the points
        points = 0;
        //reset direction
        direction = 0;
        running = true;

        if(gameWon == false){
            level = 0;
        }

        // when a new game is started, all the coins will have the state of not collected
        // and will be redraw on the canvas
        for (GoldCoin coin: getCoins()){
            coin.setColected(false);
        }

        // when a new game is started, all the enemies will have the state of alive
        // and will be redraw on the canvas
        for (Enemy enemy: getEnemies()){
            enemy.setAlive(true);
        }

        gameView.invalidate(); //redraw screen
        pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
    }

    public void pauseGame(){
        for (Enemy enemy: getEnemies()){

            // create an if statement to check if the enemies are not alive then the game can not
            // paused else it can
            if (!enemy.isAlive()){
                CharSequence text = "Game ended, create a new game!";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                gameView.invalidate();
            }
            else {
                setRunning(false);
                CharSequence text = "Game paused!";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    public void resumeGame(){
        for (Enemy enemy: getEnemies()){

            // create an if statement to check if the enemies are not alive then the game can not
            // resumed else it can
            if (!enemy.isAlive() ){
                CharSequence text = "Game ended, create a new game!";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                gameView.invalidate();
            }
            else {
                setRunning(true);
                CharSequence text = "Game resumed!";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }

    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;

        // set the position of the coins
        if (getCoins().size() ==0) {

            GoldCoin coin1 = new GoldCoin(300, 170);
            GoldCoin coin2 = new GoldCoin(670, 270);
            GoldCoin coin3 = new GoldCoin(500, 370);
            GoldCoin coin4 = new GoldCoin(400, 455);
            GoldCoin coin5 = new GoldCoin(550, 100);
            GoldCoin coin6 = new GoldCoin(680, 520);

            coins.add(coin1);
            coins.add(coin2);
            coins.add(coin3);
            coins.add(coin4);
            coins.add(coin5);
            coins.add(coin6);
        }

        // set the position of the enemy
        if (getEnemies().size() ==0) {

            Enemy enemy1 = new Enemy(0, 430);
            Enemy enemy2 = new Enemy(300, 250);

            enemies.add(enemy1);
            enemies.add(enemy2);
        }
    }

    // create the movement for the pacman
    public void movePacmanUp(int pixels)
    {
        //still within our boundaries?
        if (pacy+pixels+pacBitmap.getHeight()<h && pacy+pixels>0) {
            pacy = pacy + pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    public void movePacmanLeft(int pixels)
    {
        //still within our boundaries?
        if (pacx+pixels+pacBitmap.getWidth()<w && pacx+pixels>0) {
            pacx = pacx + pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    public void movePacmanRight(int pixels)
    {
        //still within our boundaries?
        if (pacx+pixels+pacBitmap.getWidth()<w && pacx+pixels>0) {
            pacx = pacx + pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    public void movePacmanDown(int pixels)
    {
        //still within our boundaries?
        if (pacy+pixels+pacBitmap.getHeight()<h && pacy+pixels>0) {
            pacy = pacy + pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    public void moveEnemyX(int pixels)
    {
        for(Enemy enemy: getEnemies()) {
            if(enemy.getEnemyX() + pixels + enemyBitmap.getWidth() < w && enemy.getEnemyX() + pixels > 0) {
                enemy.setEnemyX(enemy.getEnemyX() + pixels);
                doCollisionCheck();
                gameView.invalidate();
            }
        }
    }

    public void moveEnemyY(int pixels)
    {
        for(Enemy enemy: getEnemies()) {
            if(enemy.getEnemyY() + pixels + enemyBitmap.getHeight() < h && enemy.getEnemyY() + pixels > 0) {
                enemy.setEnemyY(enemy.getEnemyY() + pixels);
                doCollisionCheck();
                gameView.invalidate();
            }
        }
    }

    // check if the gold coins are collected and if yes then sow a toast
    // and end the game
    public void checkAllCoinsCollected() {
        boolean allCoinsCollected = true;

        for (GoldCoin coin: getCoins()){
            if (coin.isColected() == false) {
                allCoinsCollected = false;
            }
        }

        if (allCoinsCollected){
            CharSequence text = "All coins were collected!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            setRunning(false);

            CharSequence nextLevel = "Next level is coming up";
            int durationNextLevel = Toast.LENGTH_LONG;

            Toast toastNextLevel = Toast.makeText(context, nextLevel, durationNextLevel);
            toastNextLevel.show();
            level++;
            gameWon = true;
            newGame();
        }
    }

    public void GameOver() {

            CharSequence text = "Game Over!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            setRunning(false);
            gameWon = false;
            newGame();
    }

    //and if yes, then update the necessary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    public void doCollisionCheck()
    {
        int middleOfPacmanX = pacx + pacBitmap.getWidth()/2;
        int middleOfPacmanY = pacy + pacBitmap.getHeight()/2;

        for (GoldCoin coin: getCoins()){
            int middleOfCoinX = coin.getCoinX() + coinBitmap.getWidth()/2;
            int middleOfCoinY = coin.getCoinY() + coinBitmap.getHeight()/2;

            if (!coin.isColected()){
                double x = Math.pow(middleOfPacmanX - middleOfCoinX, 2);
                double y = Math.pow(middleOfPacmanY - middleOfCoinY, 2);
                double distance = Math.sqrt(x+y);

                if (distance < 80) {
                    coin.setColected(true);
                    points ++;
                    gameView.invalidate();
                    pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
                }
            }
        }

        // same check as with the gold coins but now if the pacman touches the
        // enemy and if yes, then end the game
        for (Enemy enemy: getEnemies()){
            int middleOfEnemyX = enemy.getEnemyX() + coinBitmap.getWidth()/2;
            int middleOfEnemyY = enemy.getEnemyY() + coinBitmap.getHeight()/2;

            if (enemy.isAlive()){
                double x = Math.pow(middleOfPacmanX - middleOfEnemyX, 2);
                double y = Math.pow(middleOfPacmanY - middleOfEnemyY, 2);
                double distance = Math.sqrt(x+y);

                if (distance < 80) {
                    enemy.setAlive(false);
                    setRunning(false);

                    CharSequence text = "Game over!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    gameWon = false;
                    gameView.invalidate();
                }
            }
        }
        checkAllCoinsCollected();
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getPacx()
    {
        return pacx;
    }

    public int getPacy()
    {
        return pacy;
    }

    public int getlevel()
    {
        return level;
    }

    public int getPoints()
    {
        return points;
    }

    public ArrayList<GoldCoin> getCoins()
    {
        return coins;
    }

    public ArrayList<Enemy> getEnemies()
    {
        return enemies;
    }

    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }

    public Bitmap getCoinBitmap() { return coinBitmap; }

    public Bitmap getEnemyBitmap() { return enemyBitmap; }
}