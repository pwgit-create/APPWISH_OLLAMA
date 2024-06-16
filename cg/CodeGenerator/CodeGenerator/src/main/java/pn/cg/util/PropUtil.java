package pn.cg.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pn.cg.datastorage.constant.PathConstants;

public class PropUtil {
    private static final Logger log = LoggerFactory.getLogger(PropUtil.class);


    /**
     * Extracts a properties file from the project's resource folder and transforms it into a Properties object
     * @param fileName The name of the file that contains properties is in the resource folder.
     * @return Properties
     */
    public static Properties ReadPropertiesFile(String fileName) throws IOException {
        Properties properties = null;
        try (FileInputStream fileInputStream = new FileInputStream(PathConstants.RESOURCE_PATH + fileName))  {
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException ioException) {
            log.error("Error on reading props file!");
        }
        return properties;
    }
}