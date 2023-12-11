package com.gamecodeschool.snakegame;

import android.content.Context;
import android.graphics.Point;

public class GameObjectFactory {
    private Context context;
    private Point gridSize;
    private int blockSize;

    public GameObjectFactory(Context context, Point gridSize, int blockSize) {
        this.context = context;
        this.gridSize = gridSize;
        this.blockSize = blockSize;
    }

    public Apple createApple() {
        return new Apple(context, gridSize, blockSize);
    }

    public Snake createSnake() {
        return new Snake(context, gridSize, blockSize);
    }

    public Orange createOrange() {
        return new Orange(context, gridSize, blockSize);
    }

    public Bomb createBomb() {
        return new Bomb(context, gridSize, blockSize);
    }
}
