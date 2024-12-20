package ext.training.utility;

import java.io.Serializable;

import wt.doc.WTDocument;
import wt.doc.WTDocumentMaster;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.type.ClientTypedUtility;
import wt.vc.VersionControlHelper;
import wt.vc.config.ConfigHelper;
import wt.vc.config.LatestConfigSpec;

public class LatestDocument implements RemoteAccess, Serializable{
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws Exception{
		
		RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
		remoteMethodServer.setUserName("wcadmin");
		remoteMethodServer.setPassword("wcadmin");
		
		getLatestDocument();
	}
	
	public static void getLatestDocument() throws Exception{
		
		QuerySpec querySpecDoc = new QuerySpec(WTDocument.class);
		querySpecDoc.appendWhere
			(new SearchCondition(WTDocument.class, WTDocument.NUMBER, SearchCondition.EQUAL,"0000000011"), new int[] {0});
		QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec)querySpecDoc);
		System.out.println("Size of the Result:     "+queryResult.size());
		
			if(queryResult.hasMoreElements()) {
				WTDocument doc = (WTDocument) queryResult.nextElement();
				WTDocumentMaster docMaster = (WTDocumentMaster) doc.getMaster();
				QueryResult latestResult = ConfigHelper.service.filteredIterationsOf(docMaster, new LatestConfigSpec());
				WTDocument latestObject = (WTDocument) latestResult.nextElement();
				String type = ClientTypedUtility.getTypeIdentifier(latestObject).getTypename();
				
				// Print details of the latest part object.
	            System.out.println("\nPart Name:             " + latestObject.getName());
	            System.out.println("Part Number:           " + latestObject.getNumber());
	            System.out.println("Part Latest Version:   " + VersionControlHelper.getIterationDisplayIdentifier(latestObject));
	            System.out.println("Part Type Name:        " + type);
				}
			else {
				System.out.println("No Element Found in Result");
			}
	}

}
