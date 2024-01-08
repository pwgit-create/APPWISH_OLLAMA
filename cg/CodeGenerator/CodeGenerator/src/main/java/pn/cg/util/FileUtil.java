package pn.cg.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {


    public static void writeDataToFile(File file, String data)
            throws IOException {

        FileWriter writer = new java.io.FileWriter(file.getPath());
        writer.write(data);

        writer.flush();
        writer.close();
    }

}
