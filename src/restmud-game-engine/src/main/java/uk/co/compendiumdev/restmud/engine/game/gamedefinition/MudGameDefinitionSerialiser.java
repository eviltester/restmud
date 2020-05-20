package uk.co.compendiumdev.restmud.engine.game.gamedefinition;


import com.google.gson.Gson;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.IdDescriptionPair;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Serialise to, or load games from, json
 * Also list the in built games available
 */
public class MudGameDefinitionSerialiser {

    public List<IdDescriptionPair> getBuiltInGamesAsIdDescriptionPairs() {

        List<IdDescriptionPair> pairs = new ArrayList<IdDescriptionPair>();

        for(String filename : this.getListOfBuiltInGames()){
            pairs.add(new IdDescriptionPair(filename, "/games/"+filename));
        }
        return pairs;
    }

    public String convertToJson(MudGameDefinition defn) {
        Gson gson = new Gson();
        return gson.toJson(defn);
    }

    public MudGameDefinition createDefnFromJson(String jsonDefn) {
        Gson gson = new Gson();
        MudGameDefinition deserialiseddefn = gson.fromJson(jsonDefn, MudGameDefinition.class);
        return deserialiseddefn;
    }

    public void writeToFile(File file, MudGameDefinition defn) {
        try {
            PrintWriter output = new PrintWriter(file);
            output.print(convertToJson(defn));
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed to write to file " + file.getAbsolutePath());
        }
    }

    public String readJsonFromResource(String path) {
        InputStream jsonStreamToRead = this.getClass().getResourceAsStream(path);

        String fileContentsJson = readResourceStreamIntoString(jsonStreamToRead, path);

        return fileContentsJson;
    }

    public List<String> getListOfBuiltInGames(){
        List<String>fileNames = new ArrayList<>();

        InputStream jsonStreamToRead = this.getClass().getResourceAsStream("/games/");

        String files = readResourceStreamIntoString(jsonStreamToRead, "/games");

        if(files.trim().length()==0){
            // no games
            // TODO: bug does not load list when packaged in a jar on mac, but can load a given game with -gamename
            System.out.println("could not read build in games folder - using cached list");
            files = readJsonFromResource("/games/cachedgameslist.txt");
        }


        final String[] filenames = files.split("\n");
        for(String filename : filenames){
            filename = filename.trim();
            if(filename.length()>0 && filename.endsWith(".json")){
                fileNames.add(filename);
            }
        }
        //fileNames.addAll(Arrays.asList(files.split("\n")));
        
        return fileNames;
    }

    private String readResourceStreamIntoString(InputStream streamToRead, String nameOfStream) {

        StringBuilder readStream = new StringBuilder();
        try{
            Reader reader = new BufferedReader(new InputStreamReader(streamToRead));
            int c;
            while ((c = reader.read()) != -1) {
                readStream.append((char) c);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to read resource " + nameOfStream);
        }

        return readStream.toString();

    }
}
