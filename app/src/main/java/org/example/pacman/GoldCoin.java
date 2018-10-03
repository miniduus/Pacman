package org.example.pacman;

/**
 * This class should contain information about a single GoldCoin.
 * such as x and y coordinates (int) and whether or not the goldcoin
 * has been taken (boolean)
 */

public class GoldCoin {
    public boolean colected = false;
    private int coinX, coinY;

    public GoldCoin(int coinX, int coinY) {
        this.coinX = coinX;
        this.coinY = coinY;
    }

    public int getCoinX() {
        return coinX;
    }

    public int getCoinY() {
        return coinY;
    }

    public boolean isColected() {
        return colected;
    }

    public void setColected(boolean colected) {
        this.colected = colected;
    }
}
