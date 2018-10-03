package org.example.pacman;

/**
 * This class contain information about a single Enemy.
 * It contains x and y coordinates (int) and whether or not the enemy
 * is alive or not (boolean)
 */

public class Enemy {
    public boolean isAlive = true;
    private int enemyX, enemyY;

    public Enemy(int enemyX, int enemyY) {
        this.enemyX = enemyX;
        this.enemyY = enemyY;
    }

    public int getEnemyX() {
        return enemyX;
    }

    public void setEnemyX(int enemyX) {
        this.enemyX = enemyX;
    }

    public int getEnemyY() {
        return enemyY;
    }

    public void setEnemyY(int enemyY) {
        this.enemyY = enemyY;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
