package pn.cg.task.base;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pn.cg.datastorage.constant.PathConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public abstract class ScriptTask {


    private static final Logger log = LoggerFactory.getLogger(ScriptTask.class);
    private final String PATH_TO_SCRIPT;
    private String[] inputArgs;

    protected ProcessBuilder pb;


    public ScriptTask(String scriptName) {

        PATH_TO_SCRIPT = PathConstants.RESOURCE_PATH + PathConstants.SHELL_SCRIPT_PATH + scriptName;
    }

    public ScriptTask(String scriptName, String[] inputArgs) {
        this.inputArgs = inputArgs;
        PATH_TO_SCRIPT = PathConstants.RESOURCE_PATH + PathConstants.SHELL_SCRIPT_PATH + scriptName;

    }


    protected String RunScript() {

       boolean isLinux = Objects.equals(File.separator, "/");

       String logMessage = isLinux ? "Started Shell Script Task" : "Started PS Script Task";

        log.debug(logMessage);

        pb = new ProcessBuilder();

        if(isLinux)
        SetCommandsLinux(pb);
        else
            SetCommandsWin(pb);

        try {

            Process process = pb.start();

            process.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {

               // log.debug(line);
                sb.append(line);
            }

            return sb.toString();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return "Error";
    }


    protected void SetCommandsLinux(ProcessBuilder pb) {

        List<String> cmdList = new LinkedList<>();

        String SH = "sh";
        cmdList.add(SH);
        cmdList.add(PATH_TO_SCRIPT);

        if (inputArgs != null)
            cmdList.addAll(Arrays.asList(inputArgs));


        pb.command(cmdList);


    }

    protected void SetCommandsWin(ProcessBuilder pb) {

        List<String> cmdList = new LinkedList<>();

        String PS = "powershell.exe";
        cmdList.add(PS);

        cmdList.add(PATH_TO_SCRIPT);

        if (inputArgs != null)
            cmdList.add("-pathToJavaFile "+"\""+inputArgs[0]+"\"") ;


        pb.command(cmdList);


    }

}
