package com.hanmaster.games.moonlander;

import com.javarush.engine.cell.*;
import com.javarush.engine.cell.Game;

public class MoonLanderGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

    private Rocket rocket;
    private GameObject landscape;
    private GameObject platform;
    private boolean isGameStopped;
    private boolean isUpPressed;
    private boolean isLeftPressed;
    private boolean isRightPressed;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
        showGrid(false);
    }

    @Override
    public void onTurn(int step) {
        rocket.move(isUpPressed, isLeftPressed, isRightPressed);
        if (score > 0) {
            score--;
        }
        check();
        setScore(score);
        drawScene();
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x > WIDTH || y >= HEIGHT || y <= 0) return;
        super.setCellColor(x, y, color);
    }

    @Override
    public void onKeyPress(Key key) {
        if (key == Key.SPACE && isGameStopped) {
            createGame();
            return;
        }
        if (key == Key.UP) {
            isUpPressed = true;
        }
        if (key == Key.LEFT) {
            isRightPressed = false;
            isLeftPressed = true;
        }
        if (key == Key.RIGHT) {
            isRightPressed = true;
            isLeftPressed = false;
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if (key == Key.UP) {
            isUpPressed = false;
        }
        if (key == Key.RIGHT) {
            isRightPressed = false;
        }
        if (key == Key.LEFT) {
            isLeftPressed = false;
        }
    }

    private void createGame() {
        isGameStopped = false;
        createGameObjects();
        drawScene();
        setTurnTimer(50);
        isUpPressed = false;
        isLeftPressed = false;
        isRightPressed = false;
        score = 1000;
    }

    private void drawScene() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                setCellColor(i, j, Color.BLACK);
            }
        }
        landscape.draw(this);
        rocket.draw(this);
    }

    private void createGameObjects() {
        rocket = new Rocket(WIDTH / 2, 0);
        landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);
        platform = new GameObject(23, MoonLanderGame.HEIGHT - 1, ShapeMatrix.PLATFORM);
    }

    private void check() {
        boolean correctLanding = rocket.isCollision(platform) && rocket.isStopped();
        if(rocket.isCollision(landscape) && !correctLanding) {
            gameOver();
        }
        if (correctLanding) {
            win();
        }
    }

    private void win() {
        rocket.land();
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "You WIN!!", Color.WHITE, 20);
        stopTurnTimer();
    }

    private void gameOver() {
        score = 0;
        rocket.crash();
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "Crashed!!", Color.WHITE, 20);
        stopTurnTimer();
    }
}
