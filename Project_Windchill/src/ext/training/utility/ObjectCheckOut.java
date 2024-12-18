package ext.training.utility;

import java.io.Serializable;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.WTPart;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.vc.VersionControlHelper;
import wt.vc.wip.WorkInProgressHelper;

public class ObjectCheckOut implements RemoteAccess, Serializable {

	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) throws Exception {
		
		RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
		remoteMethodServer.setUserName("wcadmin");
		remoteMethodServer.setPassword("wcadmin");
		
		getObjectCheckOut();
	}
	
	public static void getObjectCheckOut() throws Exception {
		
		QuerySpec querySpecPart = new QuerySpec(WTPart.class);
		WTPart part = null;
		boolean islatest = true;
		System.out.println("Into Checkout Method");
		
		try {
			
		querySpecPart.appendWhere(
				new SearchCondition(WTPart.class, WTPart.NUMBER, SearchCondition.EQUAL, "GPLM-00010021"),new int[]{0});
		QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec)querySpecPart);
		
		System.out.println("Size of the Result: "+ queryResult.size());
		int versionCounter = 1;
		
		while(queryResult.hasMoreElements()) {
			WTPart part2 = (WTPart)queryResult.nextElement();
			QueryResult allVersions = VersionControlHelper.service.allVersionsOf(part2);
			System.out.println("Version Size: "+allVersions.size());
			
			while(allVersions.hasMoreElements()) 
			{
				if(islatest) //Validate if its the latest version or not
				{
					System.out.println("Counter: "+versionCounter);
					versionCounter++;
					part = (WTPart)allVersions.nextElement();
					if(!WorkInProgressHelper.isCheckedOut(part)) 
					{
						System.out.println("Checking out the Part");
						WorkInProgressHelper.service.checkout
								(part, WorkInProgressHelper.service.getCheckoutFolder(), "Checked out for training").getWorkingCopy();
						System.out.println("Object is Checked Out");
					}
					else {
						System.out.println("Object is already CheckedOut");
					}
				}
				islatest = false;
				break;
			}
			break;
		}
		}catch (NumberFormatException nfe)
		{
			System.out.println("Inside catch Block");
		}
		
	}

}
