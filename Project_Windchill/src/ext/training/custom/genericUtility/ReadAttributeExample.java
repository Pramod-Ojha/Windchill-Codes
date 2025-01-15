package ext.training.custom.genericUtility;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ptc.core.lwc.server.PersistableAdapter;
import com.ptc.core.meta.common.UpdateOperationIdentifier;

import wt.doc.WTDocument;
import wt.doc.WTDocumentMaster;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;
import wt.vc.config.ConfigHelper;
import wt.vc.config.LatestConfigSpec;

public class ReadAttributeExample implements RemoteAccess, Serializable{
	
	private static final long serialVersionUID=1L;

public static void main(String[] args) throws Exception{
	
	RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
	remoteMethodServer.setUserName("wcadmin");
	remoteMethodServer.setPassword("wcadmin");
	
	run();
}

	public static void run( ) throws WTException,PropertyVetoException, IOException {
		final String color = "color";
		final String address = "address";

		List<String> attributeList =new ArrayList<String>();
		attributeList.add(color);
		attributeList.add(address);

		QuerySpec qs = new QuerySpec(WTDocument.class);
		System.out.println("Into Retriving IBA  value ");
		try{
			qs.appendWhere(new SearchCondition(WTDocument.class,WTDocument.NUMBER, SearchCondition.EQUAL,"0000000037"), null);
			QueryResult qr = PersistenceHelper.manager.find((StatementSpec)qs);
			System.out.println("Query size : "+qr.size());
			if(qr.hasMoreElements()) {
				WTDocument doc = (WTDocument) qr.nextElement();
				WTDocumentMaster docMaster = (WTDocumentMaster) doc.getMaster();
				QueryResult latestResult = ConfigHelper.service.filteredIterationsOf(docMaster, new LatestConfigSpec());
				WTDocument latestObject = (WTDocument) latestResult.nextElement();
						try{
							PersistableAdapter adapter = new PersistableAdapter(latestObject, null,null, new UpdateOperationIdentifier());			
							adapter.persist();
							adapter.load(attributeList);
							for (String attribute : attributeList) {
				                Object value = adapter.get(attribute);
				                System.out.println("Attribute: " + attribute + ", Value: " + value);
				            
								System.out.println(" last line in for loop ");
							} 
							System.out.println(" Out of For loop ");
							
						}catch(WTException e) {
							e.printStackTrace();
							System.out.println("Line 90 Inside catch block");
						}
					}
		} catch(NumberFormatException nfe)
		{
			System.out.println("Into Catch Block");
		}
	}

}

