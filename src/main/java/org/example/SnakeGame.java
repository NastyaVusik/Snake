package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

        private final int TILE_SIZE = 20;
        private final int GRID_WIDTH = 30;
        private final int GRID_HEIGHT = 20;
        private final int SCREEN_WIDTH = GRID_WIDTH * TILE_SIZE;
        private final int SCREEN_HEIGHT = GRID_HEIGHT * TILE_SIZE;

        private final List<Point> snake = new ArrayList<>();
        private Point food;
        private char direction = 'R';
        private boolean running = false;
        private Timer timer;

    public SnakeGame() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new KeyHandler());
        startGame();
    }

private void startGame() {
    snake.clear();
    snake.add(new Point(GRID_WIDTH / 2, GRID_HEIGHT / 2));
    spawnFood();
    running = true;
    timer = new Timer(100, this);
    timer.start();
    }

    private void spawnFood() {
        Random random = new Random();
        food = new Point(random.nextInt(GRID_WIDTH), random.nextInt(GRID_HEIGHT));
    }

    private void move() {
        if (!running) return;
        Point head = snake.get(0);
        Point newHead = switch (direction) {
            case 'U' -> new Point(head.x, head.y - 1);
            case 'D' -> new Point(head.x, head.y + 1);
            case 'L' -> new Point(head.x - 1, head.y);
            case 'R' -> new Point(head.x + 1, head.y);
            default -> head;
        };

        if (newHead.equals(food)) {
            snake.add(0, newHead);
            spawnFood();
        } else {
            snake.add(0, newHead);
            snake.remove(snake.size() - 1);
        }

        checkCollision();
    }

    private void checkCollision() {
    Point head = snake.get(0);
        if (head.x < 0 || head.y < 0 || head.x >= GRID_WIDTH || head.y >= GRID_HEIGHT) {
        running = false;
    }
        for (int i = 1; i < snake.size(); i++) {
        if (head.equals(snake.get(i))) {
            running = false;
        }
    }
        if (!running) timer.stop();
}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            g.setColor(Color.RED);
            g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            g.setColor(Color.GREEN);
            for (Point point : snake) {
                g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        } else {
            g.setColor(Color.WHITE);
            g.drawString("Game Over", SCREEN_WIDTH / 2 - 30, SCREEN_HEIGHT / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> { if (direction != 'D') direction = 'U'; }
                case KeyEvent.VK_DOWN -> { if (direction != 'U') direction = 'D'; }
                case KeyEvent.VK_LEFT -> { if (direction != 'R') direction = 'L'; }
                case KeyEvent.VK_RIGHT -> { if (direction != 'L') direction = 'R'; }
            }
        }
    }
}