package ext.training.utility;

import java.io.Serializable;

import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;

/**
 * This class is used to get the Latest Object
 */
public class LatestParts implements RemoteAccess, Serializable{
	
	private static final long serialVersionUID=1L;

	public static void main(String[] args) throws Exception {
		
		RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
		remoteMethodServer.setUserName("wcadmin");
		remoteMethodServer.setPassword("wcadmin");
		
		getLatestParts();
	}
	
	public static void getLatestParts() throws Exception {
		
	}

}
