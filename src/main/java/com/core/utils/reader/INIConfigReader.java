package main.java.com.core.utils.reader;

import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.FileReader;
import java.util.HashMap;

public class INIConfigReader {
  private String configFile;
  private Ini iniConfigReaderSession;

//  public Logger logger = Logger.getLogger(INIConfigReader.class);

  // The `INIConfigReader` constructor is initializing a new instance of the class. It takes a
  // `configFile` parameter, which is the path to the INI configuration file.
  public INIConfigReader(String configFile) {
    this.configFile = configFile;
    iniConfigReaderSession = new Ini();

    try {
      iniConfigReaderSession.load(new FileReader(this.configFile));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

/**
 * The function retrieves data from a specific section in an INI configuration file and returns it as a
 * HashMap.
 * 
 * @param sectionName The sectionName parameter is a String that represents the name of the section in
 * the configuration file from which you want to retrieve data.
 * @return The method is returning a HashMap<String, String> object.
 */
  public HashMap<String, String> getDataFromSection(String sectionName) {
    HashMap<String, String> retVal = new HashMap<String, String>();
    Profile.Section section = iniConfigReaderSession.get(sectionName);
    for (String optionKey : section.keySet()) {
      retVal.put(optionKey, section.get(optionKey));
    }
    return retVal;
  }
}
