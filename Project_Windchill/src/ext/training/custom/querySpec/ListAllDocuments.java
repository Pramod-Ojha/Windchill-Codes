package ext.training.custom.querySpec;

import java.io.Serializable;

import wt.doc.WTDocument;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.type.ClientTypedUtility;

public class ListAllDocuments implements RemoteAccess, Serializable {
	
	private static final long serialVersionUID=1L;

	public static void main(String[] args) throws Exception {
	
		RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
		remoteMethodServer.setUserName("wcadmin");
		remoteMethodServer.setPassword("wcadmin");
		
		getDocument();

	}
	
	public static void getDocument() throws Exception {
		
		QuerySpec querySpecDoc = new QuerySpec(WTDocument.class);
		//querySpecDoc.appendWhere(new SearchCondition(WTDocument.class, WTDocument.NUMBER, SearchCondition.EQUAL,"0000000013"), new int[] {0});
		QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec)querySpecDoc);
		
		System.out.println("Size of the Result: "+queryResult.size());
		
		while(queryResult.hasMoreElements()) {
			WTDocument doc = (WTDocument)queryResult.nextElement();
			String type = ClientTypedUtility.getTypeIdentifier(doc).getTypename();
			
			System.out.println("\nObject Name:     "+ doc.getName());
			System.out.println("Object Number:   "+doc.getNumber());
			System.out.println("Object Type:     "+doc.getType());
			System.out.println("Lifecycle Name:  "+doc.getLifeCycleName());
			System.out.println("Lifecycle State: "+doc.getLifeCycleState());	
			System.out.println("Object Subtype:  "+type);
			System.out.println("Object Version:  "+doc.getVersionDisplayIdentifier());		}
	}

}
