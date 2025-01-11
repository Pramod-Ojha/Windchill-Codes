package ext.training.utility.querySpec;

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
 * This class is used to get the latest WTPart object from Windchill.
 * It demonstrates fetching the latest revision and iteration of a part based on its number.
 */
public class LatestParts implements RemoteAccess, Serializable {
    
    private static final long serialVersionUID = 1L; // Required for Serializable interface to ensure versioning.

    public static void main(String[] args) throws Exception {
        
        // Establishing a connection with the Windchill Remote Method Server (RMS).
        RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
        remoteMethodServer.setUserName("wcadmin"); 
        remoteMethodServer.setPassword("wcadmin"); 
        
        // Invoking the method to fetch the latest WTPart.
        getLatestPart();        
    }
    
    /**
     * This method retrieves the latest version and iteration of a WTPart object.
     * It searches for a WTPart by its number, fetches all iterations, and filters out the latest.
     * 
     * @throws Exception in case of any issues during Windchill operations.
     */
    public static void getLatestPart() throws Exception {
        
        QuerySpec querySpecPart = new QuerySpec(WTPart.class);
        querySpecPart.appendWhere(
                new SearchCondition(WTPart.class, WTPart.NUMBER, SearchCondition.EQUAL, "GPLM-00010021"), null);
        QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec) querySpecPart);
        System.out.println("Size of the Result:    " + queryResult.size());
        
        if (queryResult.hasMoreElements()) {
            
            WTPart part = (WTPart) queryResult.nextElement(); // Fetch the first WTPart object from the result.

            // Retrieve the master object (WTPartMaster) for the part.
            // WTPartMaster represents the identity of the part across all versions and iterations.
            WTPartMaster partMaster = part.getMaster();

            // Retrieve all iterations of the partMaster and filter them to get the latest iteration.
            QueryResult latestResult = ConfigHelper.service.filteredIterationsOf(partMaster, new LatestConfigSpec());            
            WTPart latestObject = (WTPart) latestResult.nextElement(); // Fetch the latest WTPart object from the filtered results.
            String type = ClientTypedUtility.getTypeIdentifier(latestObject).getTypename(); // Get the part type name using ClientTypedUtility.

            // Print details of the latest part object.
            System.out.println("\nPart Name:             " + latestObject.getName());
            System.out.println("Part Number:           " + latestObject.getNumber());
            System.out.println("Part Latest Version:   " + VersionControlHelper.getIterationDisplayIdentifier(latestObject));
            System.out.println("Part Type Name:        " + type);
        }
        else {
        	System.out.println("No Element found in the WTPart table");
        }
    }
}

