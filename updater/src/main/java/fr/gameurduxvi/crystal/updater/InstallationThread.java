package fr.gameurduxvi.crystal.updater;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Pattern;

public class InstallationThread extends Thread {

    public InstallationThread() {
        this.start();
    }

    @SneakyThrows
    @Override
    public void run() {
        val status = "Téléchargment en cours";
        InstallFrame.getFrame().getStatusLabel().setText(status + " 0MB sur 0MB ");
        InstallFrame.getFrame().getProgressBar().setValue(0);

        String path = URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
        if(path.startsWith("/")) path = path.substring(1);
        String dir = path.replace(FilenameUtils.getName(path), "");
        System.out.println("Downloading updater to " + dir);

        try (Scanner scanner = new Scanner(new URL("https://raw.githubusercontent.com/GameurDuXVI/crystal/main/version.txt").openStream(), StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            String version = (scanner.hasNext() ? scanner.next() : "").replace("\n", "").replace("\r", "");
            String urlStr = "https://raw.githubusercontent.com/GameurDuXVI/crystal/main/versions/main/Crystal-" + version + ".jar";
            URL url = new URL(urlStr);

            File file = new File(dir + "/" + FilenameUtils.getName(urlStr));
            file.delete();

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
                    InstallFrame.getFrame().getStatusLabel().setText(status + " " + (finalDownloadedFileSize / 1000) + "KB sur " + (finalCompleteFileSize / 1000) + "KB");
                    InstallFrame.getFrame().getProgressBar().setValue((int)(currentProgress));
                });

                bout.write(data, 0, x);
            }
            bout.close();
            in.close();

            if(Main.ancientFilePath != null){
                try {
                    new File(Main.ancientFilePath).delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Runtime.getRuntime().exec("java -jar " + dir + FilenameUtils.getName(urlStr), null, new File(dir));
            System.out.println("Execute next statement: " + "java -jar " + dir + FilenameUtils.getName(urlStr));

            InstallFrame.getFrame().close();
            System.exit(0);
        } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de la mise a jour !\n" + e.getMessage());
    }
    }
}
