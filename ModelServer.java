import java.io.FileReader;
import java.io.IOException;
import settings.Settings;

public class ModelServer {
	private Settings settings;
	private static int serverPort;
	private long commiterSleepTime;
	private static String pathServeriFix, pathLocalCommit;
	private ThreadedServer ts;
	private CommiterThread ct;
	
	public ModelServer() {
		readSettings();
		ts=new ThreadedServer(serverPort);
		ct=new CommiterThread(pathServeriFix, pathLocalCommit,commiterSleepTime);
		ts.start();
	}
	
	private void readSettings() {
		settings = new Settings();
		try {
			settings.load(new FileReader("properties/settings.cfg"));
		} catch (IOException e) {
			//log.error(e);
		}
		serverPort = Integer.parseInt(settings.getProperty("copierServerPort"));
		pathServeriFix=settings.getProperty("pathServeriFix");
		pathLocalCommit=settings.getProperty("pathLocalCommit");
		commiterSleepTime=(long)Integer.parseInt(settings.getProperty("commiterSleepTime"));
	}
}
