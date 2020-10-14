package DictionaryApp;

import Model.Word;
import javafx.collections.FXCollections;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TheFavorite {

    private ArrayList<String> favList;
    private File fav;
    private FileReader favReader;
    private FileWriter favWriter;

    public TheFavorite() throws IOException {

    }

    public void initialize() throws IOException {
        try {
            fav = new File("./fav.txt");
            if (fav.createNewFile()) {
                System.out.println("Favourite created");
            } else {
                System.out.println("Favourite detected.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setFavList();
    }

    private void setFavList() throws IOException {
        try {
            favList = new ArrayList<>();
            favReader = new FileReader(fav);
            BufferedReader bufferedFavReader = new BufferedReader(favReader);
            String line;

            while ((line = bufferedFavReader.readLine()) != null) {
                System.out.println(line);
                favList.add(line);
                bufferedFavReader.readLine();
            }
            favReader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void markFav(String title, String definition) throws IOException {
        if (!isAdded(title)) {
            favWriter = new FileWriter(fav, true);
            favWriter.write(title + "\n" + definition);
            System.out.println(title + " added to Favorite list.");
            favWriter.close();
            setFavList();
        }
    }

    public void unmarkFav(String a) throws IOException {
        if (isAdded(a)) {
            File tempFav = new File("./favtmp.txt");
            BufferedReader reader = new BufferedReader(new FileReader(fav));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFav));

            String lineToRemove = a;
            String currentLine;
            boolean removeDefinition = false;

            while((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();

                if(trimmedLine.equals(lineToRemove) == true) {
                    currentLine = reader.readLine();
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();

            System.out.println(a +" removed from Favorite list.");
            fav.delete();
            tempFav.renameTo(fav);

            setFavList();
        }
    }

    public boolean isAdded(String a) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fav));
        boolean check = false;
        String lineToCheck = a;
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(lineToCheck)) {
                currentLine = reader.readLine();
                reader.close();
                return true;
            }
        }
        reader.close();
        return false;
    }

    public ArrayList<String> getFavList() {
        return favList;
    }

    public String getDefinition(String a) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fav));
        boolean check = false;
        String lineToCheck = a;
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(lineToCheck)) {
                currentLine = reader.readLine();
                reader.close();
                return currentLine;
            }
        }
        reader.close();
        return "";
    }

    public static void main(String[] args) throws IOException {
        TheFavorite f = new TheFavorite();
        f.initialize();
        //f.markFav("home","that home definition");
        //f.markFav("node","that node nothing");
        //System.out.println(f.getDefinition("home"));
    }
}