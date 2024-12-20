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
import wt.vc.wip.WorkInProgressHelper;

public class CheckOutWTPart implements RemoteAccess, Serializable {

	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) throws Exception {
		
		RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
		remoteMethodServer.setUserName("wcadmin");
		remoteMethodServer.setPassword("wcadmin");
		
		getObjectCheckOut();
	}
	
	public static void getObjectCheckOut() throws Exception {
		
		QuerySpec querySpecPart = new QuerySpec(WTPart.class);
		System.out.println("Into Checkout Method");
		
		try {
			querySpecPart.appendWhere(
					new SearchCondition(WTPart.class, WTPart.NUMBER, SearchCondition.EQUAL,"GPLM-00010021"), null); //Search condition to search specific WTPart
			QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec)querySpecPart);
			System.out.println("Size of the Result:---------------"+queryResult.size());
			
			//Searching for latest version of the WTPart
			if(queryResult.hasMoreElements()) {
				WTPart part = (WTPart) queryResult.nextElement();
				System.out.println("\nFound WTPart:   " + part.getName());
				WTPartMaster partMaster = part.getMaster(); //Association of WTPart object to it's WTPartMaster object.
				QueryResult latestResult = ConfigHelper.service.filteredIterationsOf(partMaster, new LatestConfigSpec()); //Searching for latest Iteration.
				WTPart latestPart = (WTPart) latestResult.nextElement();
				System.out.println("WTPart version before Check-Out:  "+VersionControlHelper.getIterationDisplayIdentifier(latestPart));
				
				//Checking if the WTPart is already Cheecked-Out
				if(!WorkInProgressHelper.isCheckedOut(latestPart)) {
					System.out.println("\nChecking Out the WTPart.");
					//Checking out the WTPart and retrieving the Working copy.
					WorkInProgressHelper.service.checkout
						(latestPart, WorkInProgressHelper.service.getCheckoutFolder(), "Checked-Out for training").getWorkingCopy();
					System.out.println("The WTPart has been Checked-Out.");
					
					//Checking the latest Iteration after Check-In the WTPart
					QueryResult refreshedResult = ConfigHelper.service.filteredIterationsOf(partMaster, new LatestConfigSpec());
					WTPart refreshPart = (WTPart) refreshedResult.nextElement();
					String type = ClientTypedUtility.getTypeIdentifier(refreshPart).getTypename();
					
					//Print details of the updated Part
                    System.out.println("\nPart Name:                            " + refreshPart.getName());
                    System.out.println("Part Number:                          " + refreshPart.getNumber());
                    System.out.println("Part Latest Version after Check-In:   " + 
                        VersionControlHelper.getIterationDisplayIdentifier(refreshPart));
                    System.out.println("Part Type Name:                       " + type);
				}else {
					System.out.println("The Part is already Checked-Out. Check it In first.");
				}
				
			}else {
				System.out.println("No matching Part found.");
			}
			
		}catch(Exception e){
			// Log error details
            System.err.println("Error during Part check-in process: " + e.getMessage());
            e.printStackTrace();
		}
		
	}

}
