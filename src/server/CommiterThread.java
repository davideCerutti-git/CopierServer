package server;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class CommiterThread extends Thread{
	
	private String pathiFix, pathLocalRepo;
	private long thSleepTime;
	private List<ItemFile> tmpList,lastList;
	
	public CommiterThread(String path, String localRepo, long sleepTime) {
		pathiFix=path;
		pathLocalRepo=localRepo;
		thSleepTime=sleepTime;
		tmpList=new ArrayList<ItemFile>();
		lastList=new ArrayList<ItemFile>();
	}
	
	@Override
	public void run() {
		readActualListR(pathiFix);
		checkUpdatedFiles(tmpList,lastList);
		tmpList.clear();
		try {
			Thread.sleep(thSleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void checkUpdatedFiles(List<ItemFile> _tmpList, List<ItemFile> _lastList) {
		for(ItemFile item:_tmpList) {
			if(!_lastList.contains(item)) {
			}
		}
	}

	private void readActualListR(String path) {
		File folder = new File(pathiFix);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (file.isFile()) {
				tmpList.add(new ItemFile(file.getAbsolutePath(),getLastModify(file)));
			}
			if (file.isDirectory()) {
				readActualListR(file.getAbsolutePath());
			}
		}
	}
	
	private long getLastModify(File file) {
		FileTime fileTime = null;
		try {
			fileTime = Files.getLastModifiedTime(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Cannot get the last modified time - " + e);
			return -1;
		}
		//DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss.SSS");
		return fileTime.toMillis();
	}
}
