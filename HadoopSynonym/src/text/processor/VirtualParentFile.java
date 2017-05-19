package text.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import search.util.DetailInfo;
import search.util.PoiInfo;
import common.lib.Log;

public class VirtualParentFile {

	public VirtualParentFile() {
		// TODO Auto-generated constructor stub
	}

	public String processLine(String line) {
        String[] words = line.split(" ");
        String dataid = words[3];
        String city = words[0];
        String query = words[4];
//        System.out.println("dataid:\t" + dataid);
        DetailInfo  di = new DetailInfo();
        PoiInfo pi = di.getPoiInfo(dataid);
        String newLine = query + "\t" + pi.getCategory() + "-" + pi.getSubcategory() + "\t'" + (int)pi.getX()+","+(int)pi.getY()+"\t"+pi.getCity() + "\t" + pi.getCaption();
        //System.out.println(newLine);
        return newLine;
	}
	
	
	public void init(String filename) {
		 int lineNo = 0;
	        File file = new File(filename);
	        LineIterator it = null;
        	boolean append = false;
        	String newFilename = filename + ".all.txt";
	        FileWriter fw =  null;
	        
	        try {
	        	fw = new FileWriter(newFilename, append);
	            it = FileUtils.lineIterator(file);
	        } catch (IOException e) {
	            Log.logger.error(filename + "\t" + Log.getErrorInfoFromException(e) );
	            e.printStackTrace();
	        }
	        try {
	            while (it.hasNext()) {
	                String line = it.nextLine();
	                if (line.startsWith("#")) {
	                    continue;
	                }
	                String newLine = processLine(line);
	                //lineNo++;
	                //String[] words = line.split("\t");
	               System.out.println(newLine);
	                fw.write(newLine + "\n");
	            }
	        	fw.flush();
	        	fw.close();

	        }catch (Exception e) {
	            Log.logger.error(Log.getErrorInfoFromException(e));
	        } finally {
	        	LineIterator.closeQuietly(it);
	        	Log.logger.info("loading " + filename +" finished. line:\t" + lineNo );
	        }

	}
	
	public static void main(String[] args) {
		String filename = "E:/pingce/vp.txt";
		VirtualParentFile vpf = new VirtualParentFile();
		vpf.init(filename);
	}
}
