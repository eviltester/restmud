package uk.co.compendiumdev.restmud.engine.game.gamedefinition;


import com.google.gson.Gson;
import uk.co.compendiumdev.restmud.output.json.jsonReporting.IdDescriptionPair;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MudGameDefinitionSerialiser {

    public List<IdDescriptionPair> getBuildInGamesAsIdDescriptionPairs() {

        List<IdDescriptionPair> pairs = new ArrayList<IdDescriptionPair>();

        for(String filename : this.getListOfBuildInGames()){
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

        StringBuilder json = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (jsonStreamToRead, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                json.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to read resource " + path);
        }
        return json.toString();
    }

    public List<String> getListOfBuildInGames(){
        List<String>fileNames = new ArrayList<>();

        InputStream jsonStreamToRead = this.getClass().getResourceAsStream("/games");

        StringBuilder files = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (jsonStreamToRead, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                files.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to read resource folder /games");
        }


        fileNames.addAll(Arrays.asList(files.toString().split("\n")));

        return fileNames;
    }
}
