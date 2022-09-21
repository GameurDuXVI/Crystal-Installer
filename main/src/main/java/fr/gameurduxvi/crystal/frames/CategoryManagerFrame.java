package fr.gameurduxvi.crystal.frames;

import fr.gameurduxvi.crystal.components.CommonFrame;
import fr.gameurduxvi.crystal.Main;
import fr.gameurduxvi.crystal.classes.Category;
import fr.gameurduxvi.crystal.listeners.MouseListener;
import lombok.val;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.function.Consumer;

public class CategoryManagerFrame extends CommonFrame {

    private static CategoryManagerFrame frame;
    public static CategoryManagerFrame getFrame(){
        if(frame == null)
            frame = new CategoryManagerFrame();
        return frame;
    }

    private static Box mainBox;
    private static Box horizontalBox;

    private static JLabel backLabel;
    private static JLabel categoryLabel;
    private static JComboBox comboBox;
    private static JButton addButton;
    private static JButton removeButton;

    private CategoryManagerFrame() {
        super(Main.APP_TITLE + " - Gestion des catégories", 400, 100, new JPanel(new BorderLayout()), MainFrame.getFrame());
        setBackground(Color.WHITE);
        getContent().add(getMainBox());
    }

    public Box getMainBox(){
        if(mainBox == null){
            mainBox = Box.createVerticalBox();
            mainBox.setOpaque(true);
            mainBox.setBackground(getBackground());

            mainBox.add(componentsWithBorder(5, 5, 10, 0, getBackground(), getBackLabel()));
            mainBox.add(componentsWithBorder(10, 30, 10, 0, getBackground(), getCategoryLabel()));
            mainBox.add(componentsWithBorder(0, 30, 30, 20, getBackground(), getHorizontalBox()));
        }
        return mainBox;
    }

    public JLabel getBackLabel(){
        if(backLabel == null){
            backLabel = new JLabel("Retour");
            backLabel.addMouseListener(new MouseListener.Pressed(e -> {
                close();
                MainFrame.getFrame().open();
            }));
            backLabel.setOpaque(true);
            backLabel.setBackground(getBackground());

        }
        return backLabel;
    }

    public JLabel getCategoryLabel(){
        if(categoryLabel == null){
            categoryLabel = new JLabel("Choissisez une catégorie: ");
            categoryLabel.setOpaque(true);
            categoryLabel.setBackground(getBackground());
        }
        return categoryLabel;
    }

    public Box getHorizontalBox(){
        if(horizontalBox == null){
            horizontalBox = Box.createHorizontalBox();
            horizontalBox.setOpaque(true);
            horizontalBox.setBackground(getBackground());

            horizontalBox.add(getComboBox());
            horizontalBox.add(getAddButton());
            horizontalBox.add(getRemoveButton());
        }
        return horizontalBox;
    }

    public JComboBox getComboBox(){
        if(comboBox == null){
            comboBox = new JComboBox<>();
            comboBox.setOpaque(true);
            comboBox.setBackground(getBackground());
            comboBox.addActionListener(e -> {
                if(comboBox.getItemCount() > 0){
                    if(Objects.equals(comboBox.getSelectedItem(), Main.EMPTY_CATEGORY)){
                        addButton.setText("Ajouter");
                        removeButton.setEnabled(false);
                    }
                    else {
                        addButton.setText("Modifier");
                        removeButton.setEnabled(true);
                    }
                }
            });
        }
        return comboBox;
    }

    public JButton getAddButton(){
        if(addButton == null){
            addButton = new JButton("Ajouter");
            addButton.addActionListener(e -> {
                close();
                if(Objects.equals(getComboBox().getSelectedItem(), Main.EMPTY_CATEGORY)){
                    CategoryCreatorFrame.getFrame().open();
                }
                else{
                    LinkManagerFrame.setCategory(((Category)getComboBox().getSelectedItem()));
                    LinkManagerFrame.getFrame().open();
                }
            });
        }
        return addButton;
    }

    public JButton getRemoveButton(){
        if(removeButton == null){
            removeButton = new JButton("Supprimer");
            removeButton.addActionListener(e -> {
                if(removeButton.isEnabled()){
                    String[] options = {"Oui", "Non"};
                    int choix = JOptionPane.showOptionDialog(this, "Êtes vous sur de vouloir supprimer \"" + ((Category) Objects.requireNonNull(getComboBox().getSelectedItem())).getName() + "\" ?", "Supprimer", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (choix == 0) {
                        Main.getApplications().getCategories().remove(getComboBox().getSelectedItem());
                        Main.writeToJSON();
                        update();
                    }
                }
            });
        }
        return removeButton;
    }

    @Override
    public void update() {
        getComboBox().removeAllItems();
        getComboBox().addItem(Main.EMPTY_CATEGORY);
        for (Category category : Main.getApplications().getCategories()) {
            getComboBox().addItem(category);
        }
    }
}
