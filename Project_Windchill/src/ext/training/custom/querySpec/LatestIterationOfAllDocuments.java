/**
 * 
 */
package ext.training.custom.querySpec;

import java.io.Serializable;

import wt.doc.WTDocument;
import wt.doc.WTDocumentMaster;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.type.ClientTypedUtility;
import wt.vc.VersionControlHelper;
import wt.vc.config.ConfigHelper;
import wt.vc.config.LatestConfigSpec;

/**
 * This class will return the Latest Version & Iteration of all WTDocuments
 */
public class LatestIterationOfAllDocuments implements RemoteAccess, Serializable{
	
		private static final long serialVersionUID=1L;

	public static void main(String[] args) throws Exception{
		
		RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
		remoteMethodServer.setUserName("wcadmin");
		remoteMethodServer.setPassword("wcadmin");
		
		getAllDocuments();
	}

	private static void getAllDocuments() throws Exception{
		 QuerySpec querySpecDoc = new QuerySpec(WTDocument.class);
		 QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec)querySpecDoc);
		 System.out.println("Size of the Result table:--------------"+queryResult.size());
		 
		 if(queryResult.hasMoreElements()) {
		 
			 	while(queryResult.hasMoreElements()) {
			 		WTDocument doc = (WTDocument) queryResult.nextElement();
			 		WTDocumentMaster docMaster = (WTDocumentMaster) doc.getMaster();
			 		QueryResult masterResult = ConfigHelper.service.filteredIterationsOf(docMaster, new LatestConfigSpec());
			 		WTDocument latestVersion = (WTDocument) masterResult.nextElement();
			 		String type = ClientTypedUtility.getTypeIdentifier(latestVersion).getTypename();
			 
			 		// Print details of the latest part object.
			 		System.out.println("\nPart Name:             " + latestVersion.getName());
			 		System.out.println("Part Number:           " + latestVersion.getNumber());
			 		System.out.println("Part Latest Version:   " + VersionControlHelper.getIterationDisplayIdentifier(latestVersion));
			 		System.out.println("Part Type Name:        " + type);
			 	}
		 }
		 else {
			 System.out.println("No Element found in WTDocument Result");
		 }
		
	}

}
