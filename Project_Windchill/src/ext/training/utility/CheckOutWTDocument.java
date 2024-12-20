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
import wt.vc.wip.WorkInProgressHelper;

/**
 * This class will fetch the latest WTDocument and then Checks it Out if it's Checked In.
 */
public class CheckOutWTDocument implements RemoteAccess, Serializable{
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws Exception{
		
		RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
		remoteMethodServer.setUserName("wcadmin");
		remoteMethodServer.setPassword("wcadmin");
		
		doCheckOut();
	}

	/**
	 * This Method will return the latest Version of WTDocument to Check Out.
	 * @throws Exception
	 */
	private static void doCheckOut() throws Exception{
		  
		QuerySpec querySpecDoc = new QuerySpec(WTDocument.class); 
		System.out.println("Entering Check-Out Method");
		
		try {
			querySpecDoc.appendWhere(
					new SearchCondition(WTDocument.class, WTDocument.NUMBER, SearchCondition.EQUAL,"0000000022"), null); //Search condition to search specific WTDocument
			QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec)querySpecDoc);
			System.out.println("Size of the Result:---------------"+queryResult.size());
			
			//Searching for latest version of the WTDocument
			if(queryResult.hasMoreElements()) {
				WTDocument doc = (WTDocument) queryResult.nextElement();
				System.out.println("Found WTDocument:   " + doc.getName());
				WTDocumentMaster docMaster = (WTDocumentMaster) doc.getMaster(); //Association of WTDocument object to it's WTDocumentMaster object.
				QueryResult latestResult = ConfigHelper.service.filteredIterationsOf(docMaster, new LatestConfigSpec()); //Searching for latest Iteration.
				WTDocument latestDoc = (WTDocument) latestResult.nextElement();
				System.out.println("WTDocument version before Check-Out:  "+VersionControlHelper.getIterationDisplayIdentifier(latestDoc));
				
				//Checking if the WTDocument is already Cheecked-Out
				if(!WorkInProgressHelper.isCheckedOut(latestDoc)) {
					System.out.println("Checking Out the WTDocument.");
					//Checking out the WTDocument and retrieving the Working copy.
					WorkInProgressHelper.service.checkout
						(latestDoc, WorkInProgressHelper.service.getCheckoutFolder(), "Checked-Out for training").getWorkingCopy();
					System.out.println("The WTDocument has been Checked-Out.");
					
					//Checking the latest Iteration after Check-In the WTDocument
					QueryResult refreshedResult = ConfigHelper.service.filteredIterationsOf(docMaster, new LatestConfigSpec());
					WTDocument refreshDoc = (WTDocument) refreshedResult.nextElement();
					String type = ClientTypedUtility.getTypeIdentifier(refreshDoc).getTypename();
					
					//Print details of the updated document
                    System.out.println("\nDocument Name:                            " + refreshDoc.getName());
                    System.out.println("Document Number:                          " + refreshDoc.getNumber());
                    System.out.println("Document Latest Version after Check-In:   " + 
                        VersionControlHelper.getIterationDisplayIdentifier(refreshDoc));
                    System.out.println("Document Type Name:                       " + type);
				}else {
					System.out.println("The Document is already Checked-Out. Check it In first.");
				}
				
			}else {
				System.out.println("No matching document found.");
			}
			
		}catch(Exception e){
			// Log error details
            System.err.println("Error during document check-in process: " + e.getMessage());
            e.printStackTrace();
		}
		
	}

}
