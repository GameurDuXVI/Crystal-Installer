package fr.gameurduxvi.crystal.components;

import fr.gameurduxvi.crystal.Main;
import fr.gameurduxvi.crystal.listeners.WindowListener;
import lombok.Getter;
import lombok.val;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class CommonFrame extends JFrame {

    @Getter
    public Container content;

    private JLabel titleLabel;

    public CommonFrame(String title, int width, int height, Container content, boolean closeable, CommonFrame exitFrame) throws HeadlessException {
        val screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setSize(width, height);
        setUndecorated(true);
        setLocation((int) (screenSize.getWidth() / 2) - (width / 2), (int) (screenSize.getHeight() / 2) - (height / 2));
        setIconImage(new ImageIcon(Main.ICON).getImage());

        Box frameBox = Box.createVerticalBox();
        add(frameBox);

        frameBox.add(getTitlePanel(title, closeable, exitFrame));
        super.setTitle(title);

        this.content = content;
        this.content.setPreferredSize(getSize());
        frameBox.add(componentsWithBorder(1,1,1,1, Color.BLACK, this.content));

        addWindowListener(new WindowListener.Activated(e -> update()));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public CommonFrame(String title, int width, int height, Container content, CommonFrame exitFrame){
        this(title, width, height, content, true, exitFrame);
    }

    public void open(){
        setVisible(true);
        pack();
    }

    public void close(){
        dispose();
    }

    private JPanel getTitlePanel(String title, boolean closeAble, CommonFrame exitFrame) {
        // Jpanel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setSize(getWidth(), 32);
        panel.setBackground(new Color(21, 21, 32));

        // Jpanel listener
        val l = new MouseAdapter() {
            int x, y;
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                int xx = e.getXOnScreen();
                int yy = e.getYOnScreen();
                setLocation(xx-x, yy-y);
            }
        };
        panel.addMouseListener(l);
        panel.addMouseMotionListener(l);

        if(closeAble){
            // Close Image Label
            BufferedImage closeImage = null;
            try {
                closeImage = ImageIO.read(Main.CLOSE_ICON);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JLabel closeLabel = new JLabel(new ImageIcon(closeImage));
            closeLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(exitFrame != null){
                        close();
                        exitFrame.open();
                    }
                    else{
                        System.exit(0);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    closeLabel.setBackground(new Color(40, 80, 60, 100));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    closeLabel.setBackground(null);
                }
            });
            panel.add(closeLabel, BorderLayout.EAST);
        }

        // AppImage Label
        BufferedImage appImage = null;
        try {
            appImage = ImageIO.read(Main.TITLE_ICON);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel appLabel = new JLabel(new ImageIcon(appImage));
        panel.add(appLabel, BorderLayout.WEST);

        // Title label
        titleLabel = new JLabel(title);
        titleLabel.setFont(Font.getFont("The New York Times"));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(panel.getBackground());
        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    @Override
    public void setTitle(String title){
        titleLabel.setText(title);
        super.setTitle(title);
    }

    public abstract void update();



    public static JPanel componentsWithBorder(int top, int left, int bottom, int right, Color color, Component... components){
        val panel = new JPanel(new BorderLayout());
        for (Component component : components) {
            panel.add(component);
        }
        panel.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, color));
        return panel;
    }
}
