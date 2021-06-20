package sample;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    private final Gson gson ;
    private final String folderName = "data";

    public Logger() {

        this.gson = new Gson();
    }


    public Object readData(Class dataClass , String fileName) throws IOException{
        Object data;

        data = gson.fromJson(new FileReader(folderName +"/" + fileName) , dataClass);
        return data;
    }

    public void writeData(Object data , String fileName) throws IOException{
        FileWriter fs = null;



        File folder = new File(folderName);
        if(!folder.exists()) {
            folder.mkdir();
        }

        File file = new File(folderName  + "/" +fileName );
        if(!file.exists()) {
            file.createNewFile();
        }
        fs = new FileWriter(file);
        fs.write(gson.toJson(data));
        fs.close();

    }



}

