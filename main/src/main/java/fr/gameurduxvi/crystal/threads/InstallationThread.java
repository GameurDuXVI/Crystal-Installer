package fr.gameurduxvi.crystal.threads;

import fr.gameurduxvi.crystal.Main;
import fr.gameurduxvi.crystal.classes.Link;
import fr.gameurduxvi.crystal.components.CommonFrame;
import fr.gameurduxvi.crystal.frames.InstallFrame;
import fr.gameurduxvi.crystal.frames.LinkManagerFrame;
import fr.gameurduxvi.crystal.frames.MainFrame;
import fr.gameurduxvi.crystal.listeners.MouseListener;
import lombok.val;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class InstallationThread extends Thread {

    private static final File downloadPath = new File(System.getProperty("user.home") + "/Downloads/");
    private static int toDownload;
    private static int toInstall;

    public InstallationThread() {
        this.start();

        toDownload = MainFrame.toDowload.size();
        toInstall = MainFrame.toInstall.size();
    }

    @Override
    public void run() {
        ArrayList<Link> toRemove = new ArrayList<>();

        for (int i = 0; i < MainFrame.toDowload.size(); i++) {
            Link link = MainFrame.toDowload.get(i);

            boolean downloaded = download(i, link);
            while(!downloaded){
                String[] options = {"Oui", "Non"};
                int choix = JOptionPane.showOptionDialog(InstallFrame.getFrame(), "À priorie le programme a rencontré une ou plusieurs erreurs !\nVoulez vous relancer le téléchargement de " + link.getName() + "?", "Choix", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                if (choix == 0) {
                    downloaded = download(i, link);
                }
                else{
                    toRemove.add(link);
                    break;
                }
            }
        }

        for (Link link : toRemove) {
            MainFrame.toInstall.remove(link);
        }

        for (int i = 0; i < MainFrame.toInstall.size(); i++) {
            Link link = MainFrame.toInstall.get(i);

            boolean installed = install(i, link);
            while(!installed){
                String[] options = {"Oui", "Non"};
                int choix = JOptionPane.showOptionDialog(InstallFrame.getFrame(), "À priorie le programme a rencontré une ou plusieurs erreurs !\nVoulez vous relancer l'installation de " + link.getName() + "?", "Choix", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                if (choix == 0) {
                    installed = install(i, link);
                }
                else{
                    val extension = FilenameUtils.getExtension(link.getUrl());
                    File file = new File(downloadPath + "/setup" + link.getName() + "." + extension);
                    file.delete();
                    break;
                }
            }
        }

        InstallFrame.getFrame().getStatusLabel().setText("Installations réussi");
        InstallFrame.getFrame().getProgressBar().getParent().setVisible(false);
        InstallFrame.getFrame().getOkButton().setVisible(true);
    }




    public boolean download(int i, Link link){
        val status = "Téléchargment en cours de \"" + link.getName() + "\"";
        InstallFrame.getFrame().getStatusLabel().setText(status + " 0MB sur 0MB (" + (i) + "/" + (toDownload + toInstall) + ")");
        InstallFrame.getFrame().getProgressBar().setValue((int)((double)i / (toDownload + toInstall) * 100));

        URL url;
        try {
            url = new URL(link.getUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(InstallFrame.getFrame(), "L'url suivant n'est pas un url réglmenté !\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        long completeFileSize = 0;
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("User-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1941.0 Safari/537.36");
            completeFileSize = conn.getContentLength();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(InstallFrame.getFrame(), "Impossible d'établir une connection !\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        JOptionPane.showMessageDialog(InstallFrame.getFrame(), link.getUrl(), "Erreur", JOptionPane.ERROR_MESSAGE);
        val extension = FilenameUtils.getExtension(link.getUrl());

        try{
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
            FileOutputStream fos = new FileOutputStream(downloadPath.getAbsolutePath() + "/setup" + link.getName().replace(" ", "_").replaceAll("[^a-zA-Z ]", "") + "." + extension.replaceAll("[^a-zA-Z ]", ""));
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;

                final double currentProgress = (double)downloadedFileSize / (double)completeFileSize;

                long finalDownloadedFileSize = downloadedFileSize;
                long finalCompleteFileSize = completeFileSize;
                SwingUtilities.invokeLater(() -> {
                    InstallFrame.getFrame().getStatusLabel().setText(status + " " + (finalDownloadedFileSize / 1000000) + "MB sur " + (finalCompleteFileSize / 1000000) + "MB (" + (i) + "/" + (toDownload + toInstall) + ")");
                    InstallFrame.getFrame().getProgressBar().setValue((int) ((i + currentProgress) / (toDownload + toInstall) * 100));
                });

                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



       /* val extension = FilenameUtils.getExtension(link.getUrl());
        try {
            Files.copy(inputStream, Paths.get(downloadPath.getAbsolutePath() + "/setup" + link.getName() + "." + extension), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(InstallFrame.getFrame(), "Impossible d'écrire le fichier !\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }*/
        return true;
    }

    public boolean install(int i, Link link){
        InstallFrame.getFrame().getStatusLabel().setText("Installation en cours de \"" + link.getName() + "\" (" + (MainFrame.toInstall.size() + i) + "/" + (toDownload + toInstall) + ")");
        InstallFrame.getFrame().getProgressBar().setValue((int) ((((double)(MainFrame.toInstall.size() + i)) / (toDownload + toInstall)) * 100));

        val extension = FilenameUtils.getExtension(link.getUrl());

        val runtime = Runtime.getRuntime();
        Process process;
        System.out.println(downloadPath.getAbsolutePath() + "/setup" + link.getName().replace(" ", "_").replaceAll("[^a-zA-Z ]", "") + "." + extension);
        try {
            //process = runtime.exec(new String[] {"cmd.exe", "/c", downloadPath.getAbsolutePath() + "/setup" + link.getName().replace(" ", "_").replaceAll("[^a-zA-Z ]", "") + "." + extension}, null, new File(downloadPath.getAbsolutePath() + "/"));
            process = runtime.exec(downloadPath.getAbsolutePath() + "/setup" + link.getName().replace(" ", "_").replaceAll("[^a-zA-Z ]", "") + "." + extension, null, new File(downloadPath.getAbsolutePath() + "/"));
        } catch (IOException e) {
            if(e.getMessage().contains("CreateProcess error=740") || e.getMessage().contains("CreateProcess error=193")){
                try {
                    process = runtime.exec("cmd /c " + downloadPath.getAbsolutePath() + "/setup" + link.getName().replace(" ", "_").replaceAll("[^a-zA-Z ]", "") + "." + extension, null, new File(downloadPath.getAbsolutePath() + "/"));
                } catch (IOException ioException) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(InstallFrame.getFrame(), "Une erreur s'est produite lors du lancement du programme !\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            else{
                e.printStackTrace();
                JOptionPane.showMessageDialog(InstallFrame.getFrame(), "Une erreur s'est produite lors du lancement du programme !\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(InstallFrame.getFrame(), "Une erreur s'est produite !\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        System.out.println("Exited with code " + process.exitValue());

        val exitCode = process.exitValue();
        if (exitCode != 0) {
            JOptionPane.showMessageDialog(InstallFrame.getFrame(), "Le programme s'est terminé par un code d'erreur " + exitCode, "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        File file = new File(downloadPath + "/setup" + link.getName() + "." + extension);

        file.delete();

        return true;
    }
}
