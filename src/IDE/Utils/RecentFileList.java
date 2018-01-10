package IDE.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RecentFileList {
    private static int recentSize = 5;
    public static ObservableList<File> recentFiles = FXCollections.observableArrayList();

    public static void loadRecent() throws IOException, ClassNotFoundException {
        File recent = new File("recent.txt");
        recent.createNewFile();
        FileInputStream in = new FileInputStream(recent);
        ObjectInputStream s = new ObjectInputStream(in);
        addAllRecent((List<File>) s.readObject());
        System.out.println(recentFiles);
    }

    public static void saveRecent() throws IOException {
        FileOutputStream f = new FileOutputStream("recent.txt");
        ObjectOutput s = new ObjectOutputStream(f);
        s.writeObject(new ArrayList<>(recentFiles));
    }

    public static void addRecent(File file) {
        if (file.isFile() && file.exists()) {
            recentFiles.remove(file);
            if (recentFiles.size() > recentSize - 1) {
                recentFiles.remove(recentFiles.size() - 1);
            }
            recentFiles.add(0, file);
        }
    }

    public static void addAllRecent(List<File> elements) {
        recentFiles.removeAll(elements);
        if (recentFiles.size() > recentSize - 1) {
            recentFiles.remove(recentFiles.size() - elements.size(), recentFiles.size());
        }
        for (File file: elements) {
            addRecent(file);
        }
    }
}
