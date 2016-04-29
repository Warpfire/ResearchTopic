package support;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

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
		Writer out = null;
		try{
			out = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream("C:\\Temp\\"+fileName), "UTF-8"));

			out.write(string);}
		catch (Exception e) {
            e.printStackTrace();
        }
		finally {
            try {
                // Close the writer regardless of what happens...
            	out.close();
            } catch (Exception e) {
            }
	}}
}
