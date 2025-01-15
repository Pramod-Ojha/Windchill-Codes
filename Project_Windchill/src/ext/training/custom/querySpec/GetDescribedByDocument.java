package ext.training.custom.querySpec;

import java.io.Serializable;

import wt.doc.WTDocument;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.WTPart;
import wt.part.WTPartHelper;
import wt.part.WTPartMaster;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.vc.VersionControlHelper;
import wt.vc.config.ConfigHelper;
import wt.vc.config.LatestConfigSpec;

public class GetDescribedByDocument implements RemoteAccess, Serializable{
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws Exception{
		
		RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
		remoteMethodServer.setUserName("wcadmin");
		remoteMethodServer.setPassword("wcadmin");
		
		getDescribedByDocument();

	}

	private static void getDescribedByDocument() throws Exception {
		
		QuerySpec querySpecPart = new QuerySpec(WTPart.class);
		querySpecPart.appendWhere(new SearchCondition(WTPart.class, WTPart.NUMBER, SearchCondition.EQUAL, "GPLM-00010003"), new int[] {0});
		QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec)querySpecPart);
		System.out.println("Size of the Result: --------- "+queryResult.size());
		
		System.out.println("\nSearching for WTPart");
		
		if(queryResult.hasMoreElements()) {
			WTPart part = (WTPart) queryResult.nextElement();
			WTPartMaster partMaster = part.getMaster();
			QueryResult masterResult = ConfigHelper.service.filteredIterationsOf(partMaster, new LatestConfigSpec());
			System.out.println("\nSize of masterResult Container:   "+masterResult.size());
			WTPart latestPart = (WTPart) masterResult.nextElement();
			System.out.println("\nPart name:   "+latestPart.getName());
			System.out.println("Part latest Version: "+VersionControlHelper.getIterationDisplayIdentifier(latestPart));
		
			QueryResult refResult = WTPartHelper.service.getDescribedByDocuments(latestPart);
			
			while(refResult.hasMoreElements()) {
			WTDocument desDoc = (WTDocument) refResult.nextElement();
			System.out.println("\nDescribed by Document:   "+desDoc.getName()+ "     "+desDoc.getNumber());
			System.out.println("DbD Version: "+VersionControlHelper.getIterationDisplayIdentifier(desDoc));
			}
		
		}

		
	}

}
