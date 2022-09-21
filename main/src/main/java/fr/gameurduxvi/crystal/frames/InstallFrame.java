package fr.gameurduxvi.crystal.frames;

import fr.gameurduxvi.crystal.components.CommonFrame;
import fr.gameurduxvi.crystal.Main;
import fr.gameurduxvi.crystal.listeners.MouseListener;
import fr.gameurduxvi.crystal.listeners.WindowListener;
import fr.gameurduxvi.crystal.threads.InstallationThread;
import lombok.Getter;
import lombok.Setter;
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
    private JButton okButton;

    public InstallFrame() {
        super(Main.APP_TITLE + " - Installations des programmes", 400, 100, Box.createHorizontalBox(), false, null);
        frame = this;
        setBackground(Color.WHITE);
        getContent().add(getMainBox());

        val thread = new InstallationThread();
        addWindowListener(new WindowListener.Closed(e -> {
            MainFrame.getFrame().open();
            thread.interrupt();
        }));
    }

    public Box getMainBox(){
        if(mainBox == null){
            mainBox = Box.createVerticalBox();
            mainBox.setOpaque(true);
            mainBox.setBackground(getBackground());

            mainBox.add(componentsWithBorder(30, 20, 10, 30, getBackground(), getStatusLabel()));
            mainBox.add(componentsWithBorder(0, 20, 70, 30, getBackground(), getProgressBar()));
            mainBox.add(CommonFrame.componentsWithBorder(0, 20, 30, 30, InstallFrame.getFrame().getBackground(), getOkButton()));
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

    public JButton getOkButton(){
        if(okButton == null){
            okButton = new JButton("Ok");
            okButton.addMouseListener(new MouseListener.Clicked(e -> {
                MainFrame.toInstall.clear();
                MainFrame.toDowload.clear();
                MainFrame.getFrame().open();
                InstallFrame.getFrame().close();
            }));
            okButton.setVisible(false);
        }
        return okButton;
    }

    @Override
    public void update() {

    }
}
