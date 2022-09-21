package fr.gameurduxvi.crystal;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import fr.gameurduxvi.crystal.classes.Applications;
import fr.gameurduxvi.crystal.classes.Category;
import fr.gameurduxvi.crystal.classes.Link;
import fr.gameurduxvi.crystal.frames.InstallFrame;
import fr.gameurduxvi.crystal.frames.MainFrame;
import lombok.Getter;
import lombok.val;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/***
 * Main class of Crystal Application
 *
 * @author GameurDuXVI
 * @version 0.90.1
 */
public class Main {

    public static URL ICON;
    public static URL TITLE_ICON;
    public static URL CLOSE_ICON;
    public static URL EMPTY_ICON;

    public static final String APP_NAME = "Crystal";
    public static final String VERSION = "0.90.1";
    public static final String APP_TITLE = APP_NAME + " v" + VERSION;

    public static final Category EMPTY_CATEGORY = new Category("Creer une catégorie");
    public static final Link EMPTY_LINK = new Link("Inserer un programme", "", null, false, false);

    static {
        ICON = Main.class.getClassLoader().getResource("crystalIcon.png");
        TITLE_ICON = Main.class.getClassLoader().getResource("crystalTitleIcon.png");
        CLOSE_ICON = Main.class.getClassLoader().getResource("close.png");
        EMPTY_ICON = Main.class.getClassLoader().getResource("empty.png");
    }

    @Getter
    private static Applications applications;

    /***
     * Start function of Application
     * @param args Arguments
     * @throws IOException
     */
    public static void main(String[] args) {
        createOrReadFromJson();

        try (Scanner scanner = new Scanner(new URL("https://raw.githubusercontent.com/GameurDuXVI/crystal/main/version.txt").openStream(), StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            String version = (scanner.hasNext() ? scanner.next() : "").replace("\n", "").replace("\r", "");;
            System.out.println("Checking application version...");

            String urlStr = "https://raw.githubusercontent.com/GameurDuXVI/crystal/main/versions/updater/Updater.jar";
            String path = URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            if(path.startsWith("/")) path = path.substring(1);
            String dir = path.replace(FilenameUtils.getName(path), "");

            File fileUpdater = new File(dir + FilenameUtils.getName(urlStr));

            System.out.println("Currect version: " + VERSION);
            System.out.println("Server version: " + version);

            if(fileUpdater.exists())
                fileUpdater.delete();

            if(version.length() != version.length() || !version.contains(VERSION)) {
                String[] options = {"Oui", "Non"};
                int choix = JOptionPane.showOptionDialog(InstallFrame.getFrame(), "Une nouvelle version est disponnible (" + version + "). Voulez-vous l'installer? ", "Choix", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (choix == 0) {
                    System.out.println("New version available (" + version + ")");
                    System.out.println("Downloading updater to " + dir);

                    URL url = new URL(urlStr);

                    long completeFileSize = 0;
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestProperty("User-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1941.0 Safari/537.36");
                    completeFileSize = conn.getContentLength();

                    BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                    FileOutputStream fos = new FileOutputStream(dir + "/" + FilenameUtils.getName(urlStr));
                    BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
                    byte[] data = new byte[1024];
                    long downloadedFileSize = 0;
                    int x = 0;
                    while ((x = in.read(data, 0, 1024)) >= 0) {
                        downloadedFileSize += x;

                        final double currentProgress = (double) downloadedFileSize / (double) completeFileSize;

                        long finalDownloadedFileSize = downloadedFileSize;
                        long finalCompleteFileSize = completeFileSize;
                        SwingUtilities.invokeLater(() -> {
                            System.out.println((finalDownloadedFileSize / 1000) + "KB sur " + (finalCompleteFileSize / 1000) + "KB");
                        });

                        bout.write(data, 0, x);
                    }
                    bout.close();
                    in.close();

                    Runtime.getRuntime().exec("java -jar " + dir + FilenameUtils.getName(urlStr) + " " + path, null, new File(dir));
                    System.out.println("Execute statement: " + "java -jar " + dir + FilenameUtils.getName(urlStr) + " " + path);

                    System.exit(0);
                }
            }
            else{
                System.out.println("Up to date");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de la vérification de la mise a jour !\n" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de la vérification de la mise a jour !\n" + e.getMessage());
        }
            // Launch MainFrame
            MainFrame.getFrame().open();
    }

    /***
     * Write existing {@link Applications} to the json file using {@link Gson}
     */
    public static void writeToJSON(){
        // Create gson instance
        val gson = new Gson();

        // Get path of running app
        String path = null;
        try {
            path = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de la récupération du chemin de lancement !");
            System.exit(1);
        }

        val jsonFile = new File(path + "/apps.json");
        FileWriter fw = null;
        try {
            fw = new FileWriter(jsonFile);
            fw.write(gson.toJson(applications));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de l'écriture du fichier !");
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * Read existing {@link Applications} from the json file using {@link Gson}
     */
    public static void createOrReadFromJson() {
        // Create gson instance
        val gson = new Gson();

        // Get path of running app
        String path = null;
        try {
            path = new File(".").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de la récupération du chemin de lancement !");
            System.exit(1);
        }

        val jsonFile = new File(path + "/apps.json");
        if (!jsonFile.exists()) {
            try {
                jsonFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Impossible de creer le fichier !");
                System.exit(2);
            }

            // Create first json String from Applications class
            applications = new Applications();

            // Try to write json to file
            try {
                FileWriter fw = new FileWriter(jsonFile);
                fw.write(gson.toJson(applications));
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Impossible d'écrire dans le fichier !");
                System.exit(3);
            }
        }
        else{
            Reader reader = null;
            try {
                reader = Files.newBufferedReader(Paths.get(jsonFile.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Impossible de lire le fichier !");
                System.exit(4);
            }

            try {
                applications = gson.fromJson(reader, Applications.class);
            }catch (JsonIOException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de la conversion du fichier json !");
                System.exit(5);
            }
        }
    }
}
