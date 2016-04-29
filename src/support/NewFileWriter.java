package support;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Class which contains a Static method for printing a string to a new file located in C:\Temp\
 */
public class NewFileWriter {

	/**
	 * Static Method which will create a new file with a String as contents.
		 * @param string The String to be the contents of the file.
		 * @param fileName Name of the file that should be created. This file is located in C:\Temp\
	 */
	public static void writeFile(String string,String fileName){
		
		BufferedWriter writer = null;
		
		try{
		File file = new File("C:\\Temp\\"+fileName);
		writer = new BufferedWriter(new FileWriter(file));
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
