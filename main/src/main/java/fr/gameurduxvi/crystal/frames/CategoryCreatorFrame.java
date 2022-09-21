package fr.gameurduxvi.crystal.frames;

import fr.gameurduxvi.crystal.components.CommonFrame;
import fr.gameurduxvi.crystal.Main;
import fr.gameurduxvi.crystal.classes.Category;
import fr.gameurduxvi.crystal.listeners.KeyListener;
import fr.gameurduxvi.crystal.listeners.MouseListener;

import javax.swing.*;
import java.awt.*;

public class CategoryCreatorFrame extends CommonFrame {

    private static CategoryCreatorFrame frame;
    public static CategoryCreatorFrame getFrame(){
        if(frame == null)
            frame = new CategoryCreatorFrame();
        return frame;
    }

    private Box mainBox;
    private Box horizontalBox;

    private JLabel backLabel;
    private JLabel nameLabel;
    private JTextField nameField;
    private JButton addButton;


    private CategoryCreatorFrame() {
        super(Main.APP_TITLE + " - Gestion des catégories", 400, 100, new JPanel(new BorderLayout()), CategoryManagerFrame.getFrame());
        setBackground(Color.WHITE);
        getContent().add(getMainBox());
    }

    public Box getMainBox(){
        if(mainBox == null){
            mainBox = Box.createVerticalBox();
            mainBox.setOpaque(true);
            mainBox.setBackground(getBackground());

            mainBox.add(componentsWithBorder(5, 5, 10, 0, getBackground(), getBackLabel()));
            mainBox.add(componentsWithBorder(10, 30, 10, 0, getBackground(), getNameLabel()));
            mainBox.add(componentsWithBorder(0, 30, 30, 20, getBackground(),getHorizontalBox()));
        }
        return mainBox;
    }

    public JLabel getBackLabel(){
        if(backLabel == null){
            backLabel = new JLabel("Retour");
            backLabel.addMouseListener(new MouseListener.Pressed(e -> {
                close();
                CategoryManagerFrame.getFrame().open();
            }));
            backLabel.setOpaque(true);
            backLabel.setBackground(getBackground());
        }
        return backLabel;
    }

    public JLabel getNameLabel(){
        if(nameLabel == null){
            nameLabel = new JLabel("Nom: ");
            nameLabel.setOpaque(true);
            nameLabel.setBackground(getBackground());
        }
        return nameLabel;
    }

    public Box getHorizontalBox(){
        if(horizontalBox == null){
            horizontalBox = Box.createHorizontalBox();
            horizontalBox.setOpaque(true);
            horizontalBox.setBackground(getBackground());

            horizontalBox.add(getNameField());
            horizontalBox.add(getAddButton());
        }
        return horizontalBox;
    }

    public JTextField getNameField(){
        if(nameField == null){
            nameField = new JTextField();
            nameField.setOpaque(true);
            nameField.setBackground(getBackground());
        }
        return nameField;
    }

    public JButton getAddButton(){
        if(addButton == null){
            addButton = new JButton("Ajouter");
            addButton.addActionListener(e -> {
                if(getNameField().getText().length() < 3){
                    JOptionPane.showMessageDialog(this, "Vous devez entrer un nom qui contient plus de 3 charactères !", "Erreur", JOptionPane.ERROR_MESSAGE, null);
                    return;
                }

                Main.getApplications().getCategories().add(new Category(getNameField().getText()));
                Main.writeToJSON();
                close();
                CategoryManagerFrame.getFrame().open();
            });
        }
        return addButton;
    }

    @Override
    public void update() {

    }
}
