package fr.gameurduxvi.crystal.updater;

import java.io.UnsupportedEncodingException;
import java.net.URL;

public class Main {

    public static final URL ICON;
    public static final URL TITLE_ICON;
    public static final URL CLOSE_ICON;

    public static String ancientFilePath;

    static {
        ICON = Main.class.getClassLoader().getResource("crystalIcon.png");
        TITLE_ICON = Main.class.getClassLoader().getResource("crystalTitleIcon.png");
        CLOSE_ICON = Main.class.getClassLoader().getResource("close.png");
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        if(args.length > 0){
            ancientFilePath = args[0];
        }

        new InstallFrame().open();
    }
}
