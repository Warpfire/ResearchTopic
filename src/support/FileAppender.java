package support;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Class which contains a Static method for appending lines to a file located in C:\Temp\
 *
 */
public class FileAppender {

		/**
		 * Static Method which will attempt to append a String to a file.
		 * @param string The String to be appended to the file.
		 * @param fileName Name of the file the String should be appended to located in C:\Temp\
		 */
	public static void appendToFile(String string,String fileName){
		
		BufferedWriter writer = null;
		
		try{
		File file = new File("C:\\Temp\\"+fileName);
		writer = new BufferedWriter(new FileWriter(file, true));
		writer.write(string);}
		catch (Exception e) {
            e.printStackTrace();
        }
		finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
	}}
}
