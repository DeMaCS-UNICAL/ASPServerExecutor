package resources;

import java.io.IOException;
import java.util.Properties;

public class Config {
	private Properties configFile;

	public Config() {
		this.configFile = new Properties();
		try {
			configFile.load(Config.class.getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getAbsolutePath() {
		return configFile.getProperty("absolute_path");
	}

	public Properties getConfigFile() {
		return configFile;
	}

	public void setConfigFile(Properties configFile) {
		this.configFile = configFile;
	}

}
