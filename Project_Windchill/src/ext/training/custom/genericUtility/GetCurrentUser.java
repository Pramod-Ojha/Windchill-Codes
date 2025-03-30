package ext.training.custom.genericUtility;

import java.io.Serializable;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.WTUser;
import wt.session.SessionHelper;

public class GetCurrentUser implements RemoteAccess, Serializable {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws Exception {
		
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		rms.setUserName("wcadmin");
		rms.setPassword("wcadmin");
		
		getUser();
	}

	private static void getUser() throws Exception{
		
		WTUser currentUser = (WTUser)SessionHelper.manager.getPrincipal();
		System.out.println(currentUser.getFullName());
		System.out.println(currentUser.getName());
		
	}
}
