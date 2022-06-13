package pebbles.database;

import com.google.gson.GsonBuilder;
import org.tinylog.Logger;
import pebbles.state.GameState;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class for all functions that works with the json file.
 */
public class Database {
    /**
     * If the result file doesn't exist yet, creates a new file with the given name and writes the last game
     * result in. If it exists, reads in the previous results, adds the last one and writes it into to file.
     *
     * @param filename the name of the file that stores the results
     * @param game the now played game
     */
    public void writeResultToFile(String filename, GameState game){
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var list=new ArrayList<Results>();
        try {
            File readFrom=new File(filename);
            if (!readFrom.exists()){
                list.add(new Results(game.player1,game.player2,game.whoWins()));
                FileWriter writer=new FileWriter(filename);
                writer.write(gson.toJson(list).substring(1,gson.toJson(list).length()-1));
                writer.close();
                return;
            }
            list=getResultFromFile(filename);
            list.add(new Results(game.player1,game.player2,game.whoWins()));
            FileWriter writer=new FileWriter(filename);
            writer.write(gson.toJson(list));
            writer.close();
        } catch (FileNotFoundException e){
            Logger.error("File not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * If the file exists, reads it in, turns it into Results type and gives it to an {@code ArrayList}.
     *
     * @param filename the name of the file waiting to be read
     * @return an {@code ArrayList} filled with {@code Results} type data from the file. If the file doesn'T
     * exist, returns an empty {@code ArrayList}
     * @throws FileNotFoundException if the {@code Scanner} doesn't find the file
     */
    public ArrayList<Results> getResultFromFile(String filename) throws FileNotFoundException {
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var list=new ArrayList<Results>();
        File test=new File(filename);
            if (!test.exists()){
                return list;
            }
            Scanner myReader = new Scanner(test);
            String json="";
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (!data.strip().equals("[") && !data.strip().equals("]")) json=json+data;
                if (data.strip().equals("}")){
                    Logger.debug(json);
                    list.add(gson.fromJson(json,Results.class));
                    json="";
                }
                if (data.strip().equals("},")){
                    json=json.substring(0,json.length()-1);
                    Logger.debug(json);
                    list.add(gson.fromJson(json,Results.class));
                    json="";
                }
            }
            myReader.close();
            return list;
        }

    /**
     * Gives back the last 5 game results stored in the file.
     *
     * @param filename the name of the file storing the results
     * @return an {@code ArrayList} which has maximum 5 elements
     * @throws FileNotFoundException if the file is not found
     */
    public ArrayList<Results> lastFiveGame(String filename) throws FileNotFoundException {
        var list=getResultFromFile(filename);
        if (list.size()<5) return list;
        while (list.size()>5){
            var list2=new ArrayList<Results>();
            for (int i=1;i<list.size();i++){
                list2.add(list.get(i));
            }
            list=list2;
        }
        return list;
    }
}
