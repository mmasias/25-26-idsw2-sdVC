package com.myuniverse.views.terminal;

public class GridCursor {
    private int x;
    private int y;
    private int gridWidth;
    private int gridHeight;
    private int cameraX;
    private int cameraY;

    public GridCursor(int x, int y) {
        this.x = x;
        this.y = y;
        this.cameraX = 0;
        this.cameraY = 0;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getCameraX() { return cameraX; }
    public int getCameraY() { return cameraY; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public void moveUp()    { if (y > 0) y--; }
    public void moveDown()  { y++; }
    public void moveLeft()  { if (x > 0) x--; }
    public void moveRight() { x++; }

    public void setGridBounds(int width, int height) {
        this.gridWidth = width;
        this.gridHeight = height;
    }

    public void ensureVisible(int viewportWidth, int viewportHeight) {
        if (x < cameraX) cameraX = x;
        if (x >= cameraX + viewportWidth) cameraX = x - viewportWidth + 1;
        if (y < cameraY) cameraY = y;
        if (y >= cameraY + viewportHeight) cameraY = y - viewportHeight + 1;
    }

    public void clampToGrid() {
        if (gridWidth > 0 && x >= gridWidth) x = gridWidth - 1;
        if (gridHeight > 0 && y >= gridHeight) y = gridHeight - 1;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
    }
}