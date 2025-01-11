package ext.training.utility.genericUtility;

import java.io.Serializable;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.WTPart;
import wt.part.WTPartHelper;
import wt.part.WTPartMaster;
import wt.part.WTPartUsageLink;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.vc.VersionControlHelper;
import wt.vc.config.ConfigHelper;
import wt.vc.config.LatestConfigSpec;

public class GetBOMStructure implements RemoteAccess, Serializable {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws Exception {
		
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		rms.setUserName("wcadmin");
		rms.setPassword("wcadmin");
		
		getStructure();
	}

	private static void getStructure() throws Exception{
		
		QuerySpec querySpecPart = new QuerySpec(WTPart.class);
        querySpecPart.appendWhere(
                new SearchCondition(WTPart.class, WTPart.NUMBER, SearchCondition.EQUAL, "0000000009"), null);
        QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec) querySpecPart);
        System.out.println("Size of the Result:    " + queryResult.size());
        
        if (queryResult.hasMoreElements()) {
            
            WTPart part = (WTPart) queryResult.nextElement(); // Fetch the first WTPart object from the result.
            WTPartMaster partMaster = part.getMaster();

            // Retrieve all iterations of the partMaster and filter them to get the latest iteration.
            QueryResult latestResult = ConfigHelper.service.filteredIterationsOf(partMaster, new LatestConfigSpec());            
            WTPart latestObject = (WTPart) latestResult.nextElement(); // Fetch the latest WTPart object from the filtered results.

            // Print details of the latest part object.
            System.out.println("\nPart Name:             " + latestObject.getName());
            System.out.println("Part Number:           " + latestObject.getNumber());
            System.out.println("Part Latest Version:   " + VersionControlHelper.getIterationDisplayIdentifier(latestObject));
		
		QueryResult qr = WTPartHelper.service.getUsesWTPartMasters(latestObject);
		while(qr.hasMoreElements()) {
		      WTPartUsageLink ul = (WTPartUsageLink) qr.nextElement();
		      WTPartMaster pm = (WTPartMaster) ul.getUses();
		      System.out.println(pm.getName());
		      System.out.println(pm.getContainer());
		}
		
	}

}
}


