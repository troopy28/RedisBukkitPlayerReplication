package org.kubithon.playerreplication;

import org.bukkit.craftbukkit.libs.jline.internal.Log;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by troopy28 on 20/02/2017.
 */
public final class Utils {

    private static final String UTF_8 = "UTF-8";

    private Utils() {

    }

    /**
     * @param filePathString Path to the file to check.
     * @return Returns that a file exists at the specified path.
     */
    public static boolean fileExists(String filePathString) {
        File f = new File(filePathString);
        return f.exists() && !f.isDirectory();
    }

    /**
     * Write the specified content at the specified file. If the file already exists, overwrite it, otherwise, creates
     * a new one and then write the content.
     *
     * @param filePath    Path to the file to write.
     * @param fileContent Content of the file that will be written.
     * @return Returns <b>true</b> if the operation succeeded. Otherwise returns <b>false</b>.
     */
    public static boolean writeFileContent(String filePath, String fileContent) {
        try {
            PrintWriter writer = new PrintWriter(filePath, UTF_8);
            writer.println(fileContent);
            writer.close();
            return true;
        } catch (IOException e) {
            Log.trace(e);
            return false;
        }
    }

    public static String readFileContent(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            Log.trace(e);
            return null;
        }
    }
}
