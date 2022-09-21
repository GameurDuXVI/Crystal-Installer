package fr.gameurduxvi.crystal.frames;

import fr.gameurduxvi.crystal.Main;
import fr.gameurduxvi.crystal.classes.Category;
import fr.gameurduxvi.crystal.classes.Link;
import fr.gameurduxvi.crystal.components.CommonFrame;
import fr.gameurduxvi.crystal.listeners.KeyListener;
import fr.gameurduxvi.crystal.listeners.MouseListener;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class OptionsFrame extends CommonFrame {

    private static OptionsFrame frame;
    public static OptionsFrame getFrame(){
        if(frame == null)
            frame = new OptionsFrame();
        return frame;
    }

    private Box mainBox;
    private Box firstHorizontalBox;
    private Box secondHorizontalBox;

    private JLabel backLabel;
    private JLabel nameLabel;
    private JLabel linkLabel;
    private JTextField nameField;
    private JButton modifyButton;
    private JButton addButton;
    private JComboBox<Object> comboBox;

    private OptionsFrame() {
        super(Main.APP_TITLE + " - Options", 400, 300, new JPanel(new BorderLayout()), MainFrame.getFrame());
        setBackground(Color.WHITE);
        getContent().add(getMainBox());
    }

    public Box getMainBox(){
        if(mainBox == null){
            mainBox = Box.createVerticalBox();

//            mainBox.add(componentsWithBorder(5, 5, 10, 0, getBackground(), getBackLabel()));
//            mainBox.add(componentsWithBorder(10, 30, 10, 0, getBackground(), getNameLabel()));
//            mainBox.add(componentsWithBorder(0, 30, 30, 20, getBackground(), getFirstHorizontalBox()));
//            mainBox.add(componentsWithBorder(10, 30, 10, 0, getBackground(), getLinkLabel()));
//            mainBox.add(componentsWithBorder(0, 30, 30, 20, getBackground(), getSecondHorizontalBox()));
        }
        return mainBox;
    }



    @Override
    public void update() {
    }
}
