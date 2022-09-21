package fr.gameurduxvi.crystal.frames;

import fr.gameurduxvi.crystal.components.CommonFrame;
import fr.gameurduxvi.crystal.components.CommonScrollBar;
import fr.gameurduxvi.crystal.components.JPlaceholderTextField;
import fr.gameurduxvi.crystal.Main;
import fr.gameurduxvi.crystal.classes.Category;
import fr.gameurduxvi.crystal.classes.Link;
import fr.gameurduxvi.crystal.components.XBoxLayout;
import fr.gameurduxvi.crystal.listeners.KeyListener;
import fr.gameurduxvi.crystal.listeners.MouseListener;
import fr.gameurduxvi.crystal.utils.ImageUtils;
import lombok.val;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/***
 * Main frame of Crystal Application
 */
public class MainFrame extends CommonFrame {

    public static ArrayList<Link> toInstall = new ArrayList<>();
    public static ArrayList<Link> toDowload = new ArrayList<>();
    public static Map<Link, Container> linkIndex = new HashMap<>();
    public static Map<Category, Container> categoryIndex = new HashMap<>();


    private static MainFrame frame;
    public static MainFrame getFrame(){
        if(frame == null)
            frame = new MainFrame();
        return frame;
    }

    private Box mainBox;
    private Box listBox;
    private Box footerBox;

    private JPanel searchBox;
    private JPanel emptyPanel;
    private JPanel copyrightPanel;

    private JTextField searchField;
    private Button manageButton;
    private Button installButton;
    private Button optionButton;
    private JLabel copyrightLabel;
    private CommonScrollBar scrollPane;

    private MainFrame() {
        super(Main.APP_TITLE, 350, 400, Box.createHorizontalBox(), null);
        setBackground(Color.WHITE);
        content.add(getMainBox());
    }

    public Box getMainBox(){
        if(mainBox == null){
            mainBox = Box.createVerticalBox();
            mainBox.setOpaque(true);
            mainBox.setBackground(getBackground());

            mainBox.add(getSearchBox());
            mainBox.add(getScrollPane());
            mainBox.add(getFooterBox());
            mainBox.add(getCopyrightPanel());
        }
        return mainBox;
    }

    public JTextField getSearchField(){
        if(searchField == null){
            searchField = new JPlaceholderTextField("Recherche"){@Override public void setBorder(Border border) {}};
            searchField.grabFocus();
            searchField.setFont(new Font("Arial", Font.PLAIN, 20));
            searchField.setBackground(getBackground());
            searchField.setOpaque(true);
            searchField.setForeground(Color.black);
            searchField.setColumns(20);
            searchField.addKeyListener(new KeyListener.Released(e -> {
                boolean containsOne = false;

                for(Category category: categoryIndex.keySet()){
                    boolean containsOneLink = false;

                    for(Link link: category.getLinks()){
                        if(link.getName().toLowerCase().contains(searchField.getText().toLowerCase(Locale.ROOT))){
                            linkIndex.get(link).setVisible(true);
                            containsOneLink = true;
                            containsOne = true;
                        }
                        else{
                            linkIndex.get(link).setVisible(false);
                        }
                    }
                    categoryIndex.get(category).setVisible(containsOneLink);
                }
                getEmptyPanel().setVisible(!containsOne);
            }));
        }
        return searchField;
    }

    public JPanel getSearchBox(){
        if(searchBox == null){
            val tempPanel = componentsWithBorder(5, 5, 5, 5, getBackground(), getSearchField());
            tempPanel.setMaximumSize(new Dimension(this.getWidth() + 20, 15));
            searchBox = componentsWithBorder(0, 0, 1, 0, Color.BLACK, tempPanel);
            searchBox.setMaximumSize(new Dimension(this.getWidth() + 20, 15));
        }
        return searchBox;
    }

    public Box getListBox() {
        if(listBox == null){
            listBox = Box.createVerticalBox();
            listBox.setOpaque(true);
            listBox.setBackground(getBackground());
        }
        return listBox;
    }

    public CommonScrollBar getScrollPane(){
        if(scrollPane == null){
            scrollPane = new CommonScrollBar(getListBox());
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setPreferredSize(new Dimension(400, 100));
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.setOpaque(true);
            scrollPane.setBackground(getBackground());
        }
        return scrollPane;
    }

    public JPanel getEmptyPanel(){
        if(emptyPanel == null){
            emptyPanel = new JPanel();
            emptyPanel.setOpaque(true);
            emptyPanel.setBackground(getListBox().getBackground());
            emptyPanel.setVisible(false);

            val emptyLabel = new JLabel("Aucune donnée");
            emptyLabel.setOpaque(true);
            emptyLabel.setBackground(getListBox().getBackground());
            emptyPanel.add(emptyLabel);
        }
        return emptyPanel;
    }

    public Box getFooterBox(){
        if(footerBox == null){
            footerBox = Box.createHorizontalBox();
            footerBox.setOpaque(true);
            footerBox.setBackground(getBackground());
            footerBox.setMaximumSize(new Dimension(getWidth(), 20));

            footerBox.add(getOption());
            footerBox.add(getManageButton());
            footerBox.add(getInstallButton());
        }
        return footerBox;
    }

    public Button getOption(){
        if(optionButton == null){
            optionButton = new Button("Options");

            optionButton.addMouseListener(new MouseListener.Clicked(e -> {
                OptionsFrame.getFrame().open();
            }));
        }
        return optionButton;
    }

    public Button getManageButton(){
        if(manageButton == null){
            manageButton = new Button("Gérer les liens");

            manageButton.addMouseListener(new MouseListener.Clicked(e -> {
                setVisible(false);
                CategoryManagerFrame.getFrame().open();
            }));
        }
        return manageButton;
    }

    public Button getInstallButton(){
        if(installButton == null){
            installButton = new Button("Continuer");
            installButton.addMouseListener(new MouseListener.Clicked(e -> {
                if(toDowload.size() == 0){
                    JOptionPane.showMessageDialog(null, "Vous n'avez pas cocher une seule case !");
                    return;
                }
                setVisible(false);
                InstallFrame.newFrame();
                InstallFrame.getFrame().open();
            }));
        }
        return installButton;
    }

    public JPanel getCopyrightPanel(){
        if(copyrightPanel == null){
            copyrightPanel = new JPanel(new BorderLayout());
            copyrightPanel.setOpaque(true);
            copyrightPanel.setBackground(getBackground());

            copyrightPanel.add(getCopyrightLabel(), BorderLayout.EAST);

            copyrightPanel.setMaximumSize(new Dimension(300, 10));
        }
        return copyrightPanel;
    }

    public JLabel getCopyrightLabel(){
        if(copyrightLabel == null){
            copyrightLabel = new JLabel("Made by GameurDuXVI");
            val font = copyrightLabel.getFont();
            copyrightLabel.setFont(new Font(font.getName(), font.getStyle(), 10));
            copyrightLabel.setOpaque(true);
            copyrightLabel.setBackground(getBackground());
        }
        return copyrightLabel;
    }

    public void update(){
        getListBox().removeAll();

        linkIndex.clear();
        categoryIndex.clear();

        getListBox().add(getEmptyPanel());
        getEmptyPanel().setVisible(Main.getApplications().getCategories().size() == 0);

        for (Category category : Main.getApplications().getCategories()) {
            val categoryPanel = Box.createVerticalBox();
            categoryPanel.setOpaque(true);
            categoryPanel.setBackground(getListBox().getBackground());
            getListBox().add(categoryPanel);

            val categoryLabel = new JLabel(category.getName(), SwingConstants.CENTER);
            categoryLabel.setOpaque(true);
            categoryLabel.setBackground(getListBox().getBackground());
            categoryPanel.add(componentsWithBorder(0, 0, 0, 10, getListBox().getBackground(), categoryLabel));

            categoryIndex.put(category, categoryPanel);

            int h = 0;

            for (Link link : category.getLinks()) {
                h+=50;

                val linkPanel = Box.createHorizontalBox();
                linkPanel.setOpaque(true);
                linkPanel.setBackground(getListBox().getBackground());
                linkPanel.setMaximumSize(new Dimension(350, 50));

                val linkLabel = new JLabel(link.getName(), JLabel.LEFT);
                linkLabel.setOpaque(true);
                linkLabel.setBackground(getListBox().getBackground());

                val linkButton = new JButton("Ouvrir");
                linkButton.setVisible((!link.isDownloadable() && !link.isInstallable()));
                linkButton.addMouseListener(new MouseListener.Clicked(e -> {
                    try {
                        if(!openWebpage(new URL(link.getUrl()))){
                            JOptionPane.showMessageDialog(InstallFrame.getFrame(), "Une erreur est survenue lors de l'ouverture du lien !", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (MalformedURLException malformedURLException) {
                        malformedURLException.printStackTrace();
                        JOptionPane.showMessageDialog(InstallFrame.getFrame(), "L'url suivant n'est pas un url réglmenté !\n" + malformedURLException.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }));

                val linkDownloadCkeckbox = new JCheckBox();
                linkDownloadCkeckbox.setVisible(!(!link.isDownloadable() && !link.isInstallable()));
                linkDownloadCkeckbox.setText("Télecharger");
                linkDownloadCkeckbox.setForeground(Color.gray);
                linkDownloadCkeckbox.setOpaque(true);
                linkDownloadCkeckbox.setSelected(toDowload.contains(link));
                linkDownloadCkeckbox.setBackground(getListBox().getBackground());
                linkDownloadCkeckbox.addMouseListener(new MouseListener.Pressed(e -> {
                    if(!linkDownloadCkeckbox.isSelected()){
                        if(!toDowload.contains(link)){
                            toDowload.add(link);
                        }
                    }
                    else{
                        toDowload.remove(link);
                    }
                }));

                val linkInstallCkeckbox = new JCheckBox();
                linkInstallCkeckbox.setVisible(!(!link.isDownloadable() && !link.isInstallable()));
                linkInstallCkeckbox.setText("Installer");
                linkInstallCkeckbox.setEnabled(link.isInstallable());
                linkInstallCkeckbox.setSelected(toInstall.contains(link));
                linkInstallCkeckbox.setForeground(link.isInstallable()? Color.gray : Color.lightGray);
                linkInstallCkeckbox.setOpaque(true);
                linkInstallCkeckbox.setBackground(getListBox().getBackground());
                linkInstallCkeckbox.addMouseListener(new MouseListener.Pressed(e -> {
                    if(link.isInstallable()){
                        if(!linkInstallCkeckbox.isSelected()){
                            if(!toDowload.contains(link)){
                                toDowload.add(link);
                            }
                            if(!toInstall.contains(link)){
                                toInstall.add(link);
                            }
                            linkDownloadCkeckbox.setSelected(true);
                            linkDownloadCkeckbox.setEnabled(false);
                        }
                        else{
                            toInstall.remove(link);
                            linkDownloadCkeckbox.setEnabled(true);
                        }
                    }
                }));

                BufferedImage image;
                JLabel linkIcon = null;
                if(link.getIcon() != null){
                    ByteArrayInputStream bis = new ByteArrayInputStream(link.getIcon());
                    try {
                        image = ImageIO.read(bis);
                        linkIcon = new JLabel(new ImageIcon(ImageUtils.resizeImage(image, 26, 26)));
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            linkIcon = new JLabel(new ImageIcon(ImageUtils.resizeImage(ImageIO.read(Main.EMPTY_ICON), 26, 26)));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
                else{
                    try {
                        linkIcon = new JLabel(new ImageIcon(ImageUtils.resizeImage(ImageIO.read(Main.EMPTY_ICON), 26, 26)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                linkIcon.setOpaque(true);
                linkIcon.setBackground(getListBox().getBackground());
                linkPanel.add(linkIcon);
                linkPanel.add(componentsWithBorder(0, 5, 0, 0, getListBox().getBackground(), linkLabel));
                linkPanel.add(linkButton);
                linkPanel.add(linkDownloadCkeckbox);
                linkPanel.add(linkInstallCkeckbox);
                linkPanel.setBorder(BorderFactory.createLineBorder(getListBox().getBackground(), 10));
                categoryPanel.add(linkPanel);
                linkIndex.put(link, linkPanel);

                if(link.getName().toLowerCase().contains(searchField.getText().toLowerCase(Locale.ROOT))){
                    linkIndex.get(link).setVisible(true);
                }
                else{
                    linkIndex.get(link).setVisible(false);
                }
            }
            categoryPanel.setMaximumSize(new Dimension(getWidth(), h));
        }

        val adaptatorPanel = new JPanel();
        adaptatorPanel.setOpaque(true);
        adaptatorPanel.setBackground(getListBox().getBackground());
        adaptatorPanel.setPreferredSize(new Dimension(getWidth() - 4, 0));
        getListBox().add(adaptatorPanel);

        invalidate();
        validate();
        repaint();
    }

    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean openWebpage(URL url) {
        try {
            return openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }
}
