package fr.gameurduxvi.crystal.updater;

import fr.gameurduxvi.crystal.updater.listeners.WindowListener;
import lombok.Getter;
import lombok.val;

import javax.swing.*;
import java.awt.*;

public class InstallFrame extends CommonFrame {

    @Getter
    private static InstallFrame frame;

    public static void newFrame(){
        new InstallFrame();
    }

    private Box mainBox;
    private JLabel statusLabel;
    private JProgressBar progressBar;

    public InstallFrame() {
        super("Mise a jour du logiciel", 400, 100, Box.createHorizontalBox(), false, null);
        frame = this;
        setBackground(Color.WHITE);
        getContent().add(getMainBox());

        val thread = new InstallationThread();
        addWindowListener(new WindowListener.Closed(e -> {
            thread.interrupt();
            System.exit(0);
        }));
    }

    public Box getMainBox(){
        if(mainBox == null){
            mainBox = Box.createVerticalBox();
            mainBox.setOpaque(true);
            mainBox.setBackground(getBackground());

            mainBox.add(componentsWithBorder(30, 30, 10, 30, getBackground(), getStatusLabel()));
            mainBox.add(componentsWithBorder(0, 30, 70, 30, getBackground(), getProgressBar()));
        }
        return mainBox;
    }

    public JLabel getStatusLabel(){
        if(statusLabel == null){
            statusLabel = new JLabel("En attente du processus");
            statusLabel.setOpaque(true);
            statusLabel.setBackground(getBackground());
        }
        return statusLabel;
    }

    public JProgressBar getProgressBar(){
        if(progressBar == null){
            progressBar = new JProgressBar();
            progressBar.setValue(0);
            progressBar.setOpaque(true);
            progressBar.setBackground(getBackground());
        }
        return progressBar;
    }

    @Override
    public void update() {

    }
}
