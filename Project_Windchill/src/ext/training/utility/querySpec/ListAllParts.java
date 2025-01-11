package ext.training.utility.querySpec;

import java.io.Serializable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.WTPart;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.type.ClientTypedUtility;
import wt.vc.VersionControlHelper;

public class ListAllParts implements RemoteAccess, Serializable {
	
	private static final long serialVersionUID=1L;

	public static void main(String[] args) throws Exception {
		
		RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
		
		remoteMethodServer.setUserName("wcadmin");
		remoteMethodServer.setPassword("wcadmin");
		
		getPart();
	}
	
	public static void getPart() throws Exception{
		
		QuerySpec querySpecPart = new QuerySpec(WTPart.class);
		//querySpecPart.appendWhere(new SearchCondition(WTPart.class, WTPart.NUMBER, SearchCondition.EQUAL, "GPLM-00010021"), new int[] {0});
		QueryResult queryResult= PersistenceHelper.manager.find((StatementSpec)querySpecPart);
		System.out.println("Size of the Result: "+queryResult.size());
		
		
		
		while(queryResult.hasMoreElements()) {
			WTPart part = (WTPart)queryResult.nextElement();
			String type1 = ClientTypedUtility.getTypeIdentifier(part).getTypename();
			
			System.out.println("\nObject Name: "+ part.getName());
			System.out.println("Object Type: "+part.getType());
			System.out.println("Lifecycle Name: "+part.getLifeCycleName());
			System.out.println("Lifecycle State: "+part.getLifeCycleState());	
			System.out.println("Object Subtype: "+type1);
			System.out.println("Object Version: "+VersionControlHelper.getIterationDisplayIdentifier(part));
		}
	}

}
