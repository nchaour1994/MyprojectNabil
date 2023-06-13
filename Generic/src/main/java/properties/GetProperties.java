package properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GetProperties {


  public static Properties loadProperty() {
      Properties property = new Properties();
      FileInputStream fileInputStream = null;
      try {
          fileInputStream = new FileInputStream(" ../Walgreens/src/main/resources/runnerW.xml");
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      }
      try {
          property.load(fileInputStream);
      } catch (IOException e) {
          e.printStackTrace();
      }


      return property;
  }



}
