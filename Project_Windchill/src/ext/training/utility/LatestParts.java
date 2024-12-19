package ext.training.utility;

import java.io.Serializable;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.type.ClientTypedUtility;
import wt.vc.VersionControlHelper;
import wt.vc.config.ConfigHelper;
import wt.vc.config.LatestConfigSpec;

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
		
		QuerySpec querySpecPart = new QuerySpec(WTPart.class);
		querySpecPart.appendWhere(new SearchCondition(WTPart.class,WTPart.NUMBER,SearchCondition.EQUAL,"GPLM-00010021"), new int[] {0});
		QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec)querySpecPart);
		System.out.println("Size of the Result:     "+ queryResult.size());
		
		
		if (queryResult.hasMoreElements()) {
			WTPart part = (WTPart)queryResult.nextElement();
			WTPartMaster partMaster = part.getMaster();
			QueryResult result = ConfigHelper.service.filteredIterationsOf(partMaster, new LatestConfigSpec());
			System.out.println("Master Object Size: "+result.size());
			
			WTPart latestObject = (WTPart) result.nextElement();
			String type = ClientTypedUtility.getTypeIdentifier(latestObject).getTypename();
			System.out.println(latestObject.getName()+ "  " + VersionControlHelper.getIterationDisplayIdentifier(latestObject)+"   "+ type);
		}
	}

}
