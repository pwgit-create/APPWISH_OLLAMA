package pn.cg.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pn.cg.app_system.code_generation.ClassCompiler;
import pn.cg.datastorage.constant.PathConstants;

public class PropUtil {
        private static final Logger log = LoggerFactory.getLogger(PropUtil.class);

    
     public static Properties ReadPropertiesFile(String fileName) throws IOException {

        Properties properties = null;

        FileInputStream fileInputStream = null;

        try {

            log.info(System.getProperty("user.dir"));

            fileInputStream = new FileInputStream(PathConstants.RESOURCE_PATH+fileName);
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException ioException) {

            ioException.printStackTrace();
        } finally {
            if(fileInputStream != null)
            fileInputStream.close();
        }

        return properties;
    }
}
