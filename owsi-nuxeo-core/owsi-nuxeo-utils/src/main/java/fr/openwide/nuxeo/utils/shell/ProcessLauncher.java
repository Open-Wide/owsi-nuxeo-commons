package fr.openwide.nuxeo.utils.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * @author mkalam-alami
 *
 */
public class ProcessLauncher {

    private final String[] commands;

    public ProcessLauncher(String... commands) {
        this.commands = commands;
        
    }
    
    public String run() throws InterruptedException, IOException {
        // Exécuter process de manière synchrone
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(commands);
        proc.waitFor();

        // Lire sortie
        InputStream stdIs = null;
        try {
            stdIs = proc.getInputStream();
            BufferedReader stdReader = new BufferedReader(new InputStreamReader(stdIs));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = stdReader.readLine()) != null) {
                output.append(line);
            }
            return output.toString();
        }
        catch (IOException e) {
            if (stdIs != null) {
                stdIs.close();
            }
            throw e;
        }
    }
    
}
