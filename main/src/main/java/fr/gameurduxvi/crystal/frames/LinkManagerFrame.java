package fr.gameurduxvi.crystal.frames;

import fr.gameurduxvi.crystal.components.CommonFrame;
import fr.gameurduxvi.crystal.Main;
import fr.gameurduxvi.crystal.classes.Category;
import fr.gameurduxvi.crystal.classes.Link;
import fr.gameurduxvi.crystal.listeners.KeyListener;
import fr.gameurduxvi.crystal.listeners.MouseListener;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LinkManagerFrame extends CommonFrame {

    private static LinkManagerFrame frame;
    public static LinkManagerFrame getFrame(){
        if(frame == null)
            frame = new LinkManagerFrame();
        return frame;
    }

    @Getter
    private static Category category;
    public static void setCategory(Category category) {
        LinkManagerFrame.category = category;
        getFrame().update();
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

    private LinkManagerFrame() {
        super(Main.APP_TITLE + " - Gestion des applications dans \"" + category.getName() + "\"", 400, 200, new JPanel(new BorderLayout()), CategoryManagerFrame.getFrame());
        setBackground(Color.WHITE);
        getContent().add(getMainBox());
    }

    public Box getMainBox(){
        if(mainBox == null){
            mainBox = Box.createVerticalBox();

            mainBox.add(componentsWithBorder(5, 5, 10, 0, getBackground(), getBackLabel()));
            mainBox.add(componentsWithBorder(10, 30, 10, 0, getBackground(), getNameLabel()));
            mainBox.add(componentsWithBorder(0, 30, 30, 20, getBackground(), getFirstHorizontalBox()));
            mainBox.add(componentsWithBorder(10, 30, 10, 0, getBackground(), getLinkLabel()));
            mainBox.add(componentsWithBorder(0, 30, 30, 20, getBackground(), getSecondHorizontalBox()));
        }
        return mainBox;
    }

    public JLabel getBackLabel(){
        if(backLabel == null){
            backLabel = new JLabel("Retour");
            backLabel.setOpaque(true);
            backLabel.setBackground(getBackground());
            backLabel.addMouseListener(new MouseListener.Pressed(e -> {
                close();
                CategoryManagerFrame.getFrame().open();
            }));
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

    public Box getFirstHorizontalBox(){
        if(firstHorizontalBox == null){
            firstHorizontalBox = Box.createHorizontalBox();
            firstHorizontalBox.setOpaque(true);
            firstHorizontalBox.setBackground(getBackground());

            firstHorizontalBox.add(getNameField());
            firstHorizontalBox.add(getModifyButton());
        }
        return firstHorizontalBox;
    }

    public JTextField getNameField(){
        if(nameField == null){
            nameField = new JTextField();
            nameField.setOpaque(true);
            nameField.setBackground(getBackground());
            nameField.addKeyListener(new KeyListener.Released(e -> modifyButton.setEnabled(!nameField.getText().equals(category.getName()))));
        }
        return nameField;
    }

    public JButton getModifyButton(){
        if(modifyButton == null){
            modifyButton = new JButton("Modifier");
            modifyButton.setEnabled(false);
            modifyButton.addActionListener(e -> {
                if(modifyButton.isEnabled()){
                    if(getNameField().getText().length() < 3){
                        JOptionPane.showMessageDialog(this, "Vous devez entrer un nom qui contient plus de 3 charactères !", "Erreur", JOptionPane.ERROR_MESSAGE, null);
                        return;
                    }
                    modifyButton.setEnabled(false);
                    category.setName(getNameField().getText());
                    Main.writeToJSON();
                    update();
                }
            });
        }
        return modifyButton;
    }

    public JLabel getLinkLabel(){
        if(linkLabel == null){
            linkLabel = new JLabel("Choissisez une application: ");
            linkLabel.setOpaque(true);
            linkLabel.setBackground(getBackground());
        }
        return linkLabel;
    }

    public Box getSecondHorizontalBox(){
        if(secondHorizontalBox == null){
            secondHorizontalBox = Box.createHorizontalBox();

            secondHorizontalBox.add(getComboBox());
            secondHorizontalBox.add(getAddButton());
        }
        return secondHorizontalBox;
    }

    public JComboBox<Object> getComboBox(){
        if(comboBox == null){
            comboBox = new JComboBox<>();
            comboBox.setOpaque(true);
            comboBox.setBackground(getBackground());
            comboBox.addActionListener(e -> {
                if(comboBox.getItemCount() > 0){
                    if(Objects.equals(comboBox.getSelectedItem(), Main.EMPTY_LINK)){
                        addButton.setText("Ajouter");
                    }
                    else {
                        addButton.setText("Modifier");
                    }
                }
            });
        }
        return comboBox;
    }

    public JButton getAddButton(){
        if(addButton == null){
            addButton = new JButton("Ajouter");
            addButton.addMouseListener(new MouseListener.Clicked(e -> {
                close();
                if(Objects.equals(getComboBox().getSelectedItem(), Main.EMPTY_LINK)){
                    new LinkCreatorFrame(category, null).open();
                }
                else{
                    new LinkCreatorFrame(category, ((Link)getComboBox().getSelectedItem())).open();
                }
            }));
        }
        return addButton;
    }

    @Override
    public void update() {
        if(getCategory() != null){
            setTitle(Main.APP_TITLE + " - Gestion des applications dans \"" + category.getName() + "\"");
            getNameField().setText(category.getName());
            getComboBox().removeAllItems();
            getComboBox().addItem(Main.EMPTY_LINK);
            for (Link Link : getCategory().getLinks()) {
                getComboBox().addItem(Link);
            }
        }
    }
}
