package fr.gameurduxvi.crystal.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public abstract class KeyListener extends KeyAdapter {

    protected Consumer<KeyEvent> action;

    protected KeyListener(Consumer<KeyEvent> action) {
        this.action = action;
    }

    public static class Typed extends KeyListener {
        public Typed(Consumer<KeyEvent> action) {super(action);}
        @Override public void keyTyped(KeyEvent e) {action.accept(e);}
    }

    public static class Pressed extends KeyListener {
        public Pressed(Consumer<KeyEvent> action) {super(action);}
        @Override public void keyPressed(KeyEvent e) {action.accept(e);}
    }

    public static class Released extends KeyListener {
        public Released(Consumer<KeyEvent> action) {super(action);}
        @Override public void keyReleased(KeyEvent e) {action.accept(e);}
    }
}
