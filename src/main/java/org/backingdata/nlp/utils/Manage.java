package org.backingdata.nlp.utils;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * NLP Utilities Library management class.
 * 
 * @author Francesco Ronzano
 *
 */
public class Manage {
	
	private static Logger logger = Logger.getLogger(Manage.class);
	
	private static String resourceFolder = "";
	
	/**
	 * Set the path to the resource folder.
	 * 
	 * @param resFolder if false, it is impossible to set the path
	 * @return
	 */
	public static boolean setResourceFolder(String resFolder) {
		boolean retVal = false;
		
		if(resFolder != null) {
			File resPath = new File(resFolder.trim());
			if(resPath != null && resPath.exists() && resPath.isDirectory()) {
				resourceFolder = resPath.getAbsolutePath();
				logger.info("Set resource folder equal to: " + resourceFolder);
				retVal = true;
			}
			else {
				logger.error("The resource folder path of the NLP Utilities library is not valid.");
			}
		}
		
		return retVal;
	}
	
	/**
	 * Get the path to the resource folder.
	 * 
	 * @return empty or null if no resource folder path is set
	 */
	public static String getResourceFolder() {
		if(resourceFolder == null) {
			logger.error("The resource folder of the NLP Utilities library is not set.");
		}
		
		if(resourceFolder.length() > 0) {
			if(resourceFolder.endsWith("/") || resourceFolder.endsWith("\\")) {
					resourceFolder = resourceFolder.substring(0, resourceFolder.length() - 2);
			}
		}
		return new String(resourceFolder);
	}
	
}
