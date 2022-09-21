package fr.gameurduxvi.crystal.frames;

import fr.gameurduxvi.crystal.components.CommonFrame;
import fr.gameurduxvi.crystal.Main;
import fr.gameurduxvi.crystal.classes.Category;
import fr.gameurduxvi.crystal.classes.Link;
import fr.gameurduxvi.crystal.listeners.MouseListener;
import fr.gameurduxvi.crystal.utils.ImageUtils;
import lombok.val;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class LinkCreatorFrame extends CommonFrame {

    private int iFileChooser = -1;

    public Category category;
    public Link link;

    private Box mainBox;
    private Box firstHorizontal;
    private Box secondHorizontalBox;
    private Box thirdHorizontalBox;

    private JLabel backLabel;
    private JLabel nameLabel;
    private JLabel urlLabel;
    private JLabel imageLabel;
    private JLabel iconImageLabel;
    private JTextField nameField;
    private JTextField urlField;
    private JFileChooser iconFileChooser;
    private JButton imageChooserButton;
    private JButton removeButton;
    private JButton addButton;
    private JCheckBox downloadCheckBox;
    private JCheckBox installCheckBox;



    public LinkCreatorFrame(Category category, Link link) {
        super(Main.APP_TITLE + " - Configuration d'une nouvelle application", 400, 250, new JPanel(new BorderLayout()), LinkManagerFrame.getFrame());
        this.category = category;
        this.link = link;
        setBackground(Color.WHITE);
        getContent().add(getMainBox());
    }

    public Box getMainBox(){
        if(mainBox == null){
            mainBox = Box.createVerticalBox();
            mainBox.setOpaque(true);
            mainBox.setBackground(getBackground());

            mainBox.add(componentsWithBorder(5, 5, 5, 0, getBackground(), getBackLabel()));
            mainBox.add(componentsWithBorder(0, 30, 0, 0, getBackground(), getNameLabel()));
            mainBox.add(componentsWithBorder(0, 30, 5, 20, getBackground(), getNameField()));
            mainBox.add(componentsWithBorder(0, 30, 0, 0, getBackground(), getUrlLabel()));
            mainBox.add(componentsWithBorder(0, 30, 5, 20, getBackground(), getUrlField()));
            mainBox.add(componentsWithBorder(0, 30, 5, 20, getBackground(), getFirstHorizontal()));
            mainBox.add(getSecondHorizontalBox());
            mainBox.add(getThirdHorizontalBox());
        }
        return mainBox;
    }

    public JLabel getBackLabel(){
        if(backLabel == null){
            backLabel = new JLabel("Retour");
            backLabel.addMouseListener(new MouseListener.Pressed(e -> {
                close();
                LinkManagerFrame.getFrame().open();
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

    public JTextField getNameField(){
        if(nameField == null){
            nameField = new JTextField(link == null? "" : link.getName());
            nameField.setOpaque(true);
            nameField.setBackground(getBackground());
        }
        return nameField;
    }

    public JLabel getUrlLabel(){
        if(urlLabel == null){
            urlLabel = new JLabel("Url: ");
            urlLabel.setOpaque(true);
            urlLabel.setBackground(getBackground());
        }
        return urlLabel;
    }

    public JTextField getUrlField(){
        if(urlField == null){
            urlField = new JTextField(link == null? "" : link.getUrl());
            urlField.setOpaque(true);
            urlField.setBackground(getBackground());
        }
        return urlField;
    }

    public Box getFirstHorizontal(){
        if(firstHorizontal == null){
            firstHorizontal = Box.createHorizontalBox();
            firstHorizontal.setOpaque(true);
            firstHorizontal.setBackground(getBackground());

            firstHorizontal.add(getImageLabel());
            firstHorizontal.add(componentsWithBorder(0, 0, 0, 50, getBackground(), getIconImageLabel()));
            firstHorizontal.add(getImageChooserButton());
        }
        return firstHorizontal;
    }

    public JLabel getImageLabel(){
        if(imageLabel == null){
            imageLabel = new JLabel("Icon: ");
            imageLabel.setOpaque(true);
            imageLabel.setBackground(getBackground());
        }
        return imageLabel;
    }

    public JFileChooser getIconFileChooser(){
        if(iconFileChooser == null){
            iconFileChooser = new JFileChooser();
            iconFileChooser.setFileFilter(new FileNameExtensionFilter("Images (.png, .jpg)", "png", "jpg"));
            iconFileChooser.setDialogTitle("Specify a file to save");
            iconFileChooser.addActionListener(e -> {
                if(e.getActionCommand().equalsIgnoreCase("ApproveSelection")){
                    BufferedImage bImage = null;
                    try {
                        bImage = ImageUtils.resizeImage(ImageIO.read(iconFileChooser.getSelectedFile()), 26, 26);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    iconImageLabel.setIcon(new ImageIcon(bImage));
                    open();
                }
            });
        }
        return iconFileChooser;
    }

    public JLabel getIconImageLabel(){
        if(iconImageLabel == null){
            BufferedImage image = null;
            if(link == null){
                try {
                    image = ImageUtils.resizeImage(ImageIO.read(Main.EMPTY_ICON), 26, 26);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                if(link.getIcon() != null){
                    ByteArrayInputStream bis = new ByteArrayInputStream(link.getIcon());
                    try {
                        image = ImageIO.read(bis);
                        image = ImageUtils.resizeImage(image, 26, 26);
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            image = ImageUtils.resizeImage(ImageIO.read(Main.EMPTY_ICON), 26, 26);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
                else{
                    try {
                        image = ImageUtils.resizeImage(ImageIO.read(Main.EMPTY_ICON), 26, 26);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            iconImageLabel = new JLabel(new ImageIcon(image));
            iconImageLabel.setOpaque(true);
            iconImageLabel.setBackground(getBackground());
        }
        return iconImageLabel;
    }

    public JButton getImageChooserButton(){
        if(imageChooserButton == null){
            imageChooserButton = new JButton("Choisir une image");
            imageChooserButton.addMouseListener(new MouseListener.Clicked(e -> iFileChooser = getIconFileChooser().showOpenDialog(this)));
        }
        return imageChooserButton;
    }

    public Box getSecondHorizontalBox(){
        if(secondHorizontalBox == null){
            secondHorizontalBox = Box.createHorizontalBox();
            secondHorizontalBox.setOpaque(true);
            secondHorizontalBox.setBackground(getBackground());

            secondHorizontalBox.add(getDownloadCheckBox());
            secondHorizontalBox.add(getInstallCheckBox());
        }
        return secondHorizontalBox;
    }

    public JCheckBox getDownloadCheckBox(){
        if(downloadCheckBox == null){
            downloadCheckBox = new JCheckBox("Téléchargable");
            downloadCheckBox.setOpaque(true);
            downloadCheckBox.setBackground(getBackground());
            downloadCheckBox.setSelected(link != null? link.isDownloadable() : false);
            downloadCheckBox.setEnabled(!(link != null? link.isInstallable() : false));
        }
        return downloadCheckBox;
    }

    public JCheckBox getInstallCheckBox(){
        if(installCheckBox == null){
            installCheckBox = new JCheckBox("Installable");
            installCheckBox.setOpaque(true);
            installCheckBox.setBackground(getBackground());
            installCheckBox.setSelected(link != null? link.isInstallable() : false);
            installCheckBox.addActionListener(e -> {
                if(installCheckBox.isSelected()){
                    getDownloadCheckBox().setSelected(true);
                    getDownloadCheckBox().setEnabled(false);
                }
                else{
                    getDownloadCheckBox().setEnabled(true);
                }
            });
        }
        return installCheckBox;
    }

    public Box getThirdHorizontalBox(){
        if(thirdHorizontalBox == null){
            thirdHorizontalBox = Box.createHorizontalBox();
            thirdHorizontalBox.setOpaque(true);
            thirdHorizontalBox.setBackground(getBackground());

            if(getRemoveButton() != null)
                thirdHorizontalBox.add(componentsWithBorder(10, 5, 10, 5, getBackground(), getRemoveButton()));
            thirdHorizontalBox.add(componentsWithBorder(10, 5, 10, 5, getBackground(), getAddButton()));
        }
        return thirdHorizontalBox;
    }

    public JButton getRemoveButton(){
        if(removeButton == null && link != null){
            removeButton = new JButton("Supprimer");
            removeButton.addMouseListener(new MouseListener.Clicked(e -> {
                String[] options = {"Oui", "Non"};
                int choix = JOptionPane.showOptionDialog(this, "Êtes vous sur de vouloir supprimer \"" + link.getName() + "\" ?", "Supprimer", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (choix == 0) {
                    category.getLinks().remove(link);
                    Main.writeToJSON();
                    close();
                    LinkManagerFrame.getFrame().open();
                }
            }));
        }
        return removeButton;
    }

    public JButton getAddButton(){
        if(addButton == null){
            addButton = new JButton(link == null? "Ajouter": "Modifier");
            addButton.addActionListener(e -> {
                if(getNameField().getText().length() < 3){
                    JOptionPane.showMessageDialog(this, "Vous devez entrer un nom qui contient plus de 3 charactères !", "Erreur", JOptionPane.ERROR_MESSAGE, null);
                    return;
                }

                try {
                    new URL(getUrlField().getText());
                } catch (MalformedURLException malformedURLException) {
                    malformedURLException.printStackTrace();
                    JOptionPane.showMessageDialog(this, "L'url est incorrecte !", "Erreur", JOptionPane.ERROR_MESSAGE, null);
                    return;
                }

                BufferedImage bImage;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte [] data = null;
                if(iFileChooser == JFileChooser.APPROVE_OPTION){
                    if(getIconFileChooser().getSelectedFile() == null){
                        JOptionPane.showMessageDialog(this, "Vous n'avez pas choissis d'icone !", "Erreur", JOptionPane.ERROR_MESSAGE, null);
                        return;
                    }

                    if(!getIconFileChooser().getSelectedFile().exists()){
                        JOptionPane.showMessageDialog(this, "Le fichier n'existe pas !", "Erreur", JOptionPane.ERROR_MESSAGE, null);
                        return;
                    }

                    val extension = Optional.of(getIconFileChooser().getSelectedFile().getAbsolutePath())
                            .filter(f -> f.contains("."))
                            .map(f -> f.substring(getIconFileChooser().getSelectedFile().getAbsolutePath().lastIndexOf(".") + 1));
                    if(!extension.get().equalsIgnoreCase("png") && !extension.get().equalsIgnoreCase("jpg")){
                        JOptionPane.showMessageDialog(this, "L'extension doit etre de type png ou jpg !", "Erreur", JOptionPane.ERROR_MESSAGE, null);
                        return;
                    }
                    try {
                        bImage = ImageUtils.resizeImage(ImageIO.read(getIconFileChooser().getSelectedFile()), 26, 26);
                        ImageIO.write(bImage, extension.get(), bos );
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    data = bos.toByteArray();
                }

                if(link != null){
                    link.setName(getNameField().getText());
                    link.setUrl(getUrlField().getText());
                    if(data != null){
                        link.setIcon(data);
                    }
                    link.setDownloadable(getDownloadCheckBox().isSelected());
                    link.setInstallable(getInstallCheckBox().isSelected());
                }
                else{
                    if(data == null){
                        BufferedImage i;
                        try {
                            i = ImageUtils.resizeImage(ImageIO.read(Main.EMPTY_ICON), 26, 26);
                            ImageIO.write(i, "png", bos );
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        data = bos.toByteArray();
                    }
                    category.getLinks().add(new Link(getNameField().getText(), getUrlField().getText(), data, getDownloadCheckBox().isSelected(), getInstallCheckBox().isSelected()));
                }
                Main.writeToJSON();
                close();
                LinkManagerFrame.getFrame().open();
            });
        }
        return addButton;
    }

    @Override
    public void update() {

    }
}
