package server;

public class ItemFile  {
	private String absolutePathFile;
	private long lastModify;
	static private boolean FOLDER=false;
	static private boolean FILE=true;

	public ItemFile(String absolutePathFile, long lastModify,boolean folder ) {
		super();
		this.absolutePathFile = absolutePathFile;
		this.lastModify = lastModify;
	}

}
