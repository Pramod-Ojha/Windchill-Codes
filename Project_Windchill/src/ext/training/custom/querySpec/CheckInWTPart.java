package ext.training.custom.querySpec;

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
import wt.vc.VersionControlHelper;
import wt.vc.config.ConfigHelper;
import wt.vc.config.LatestConfigSpec;
import wt.vc.wip.WorkInProgressHelper;
import wt.vc.wip.Workable;

public class CheckInWTPart implements RemoteAccess, Serializable {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) throws Exception {

        // Establish connection to Windchill
        RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
        remoteMethodServer.setUserName("wcadmin");
        remoteMethodServer.setPassword("wcadmin");

        QuerySpec querySpecPart = new QuerySpec(WTPart.class);
        System.out.println("Into CheckIn Method");

        try {
            // Add a condition to query for the part by its unique number
            querySpecPart.appendWhere(
                    new SearchCondition(WTPart.class, WTPart.NUMBER, SearchCondition.EQUAL, "GPLM-00010003"), null);

            // Execute the query to find the WTPart
            QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec)querySpecPart);

            System.out.println("Size of the Result: " + queryResult.size());

            // Process the result
            if (queryResult.hasMoreElements()) { 
                WTPart part = (WTPart) queryResult.nextElement(); // Fetch the single, unique WTPart
                System.out.println("Found WTPart: " + part.getName());

                // Get the master of the part to retrieve its latest iteration
                WTPartMaster partMaster = part.getMaster();
                QueryResult latestResult = ConfigHelper.service.filteredIterationsOf(partMaster, new LatestConfigSpec());

                if (latestResult.hasMoreElements()) {
                    WTPart latestObject = (WTPart) latestResult.nextElement(); // Fetch the latest iteration
                    System.out.println("Part Version before Check-In: " + VersionControlHelper.getIterationDisplayIdentifier(latestObject));

                    // Check if the part is checked out before attempting to check it in
                    if (WorkInProgressHelper.isCheckedOut(latestObject)) {
                        System.out.println("Checking In the Part");
                        WorkInProgressHelper.service.checkin((Workable) latestObject, "Checked In through code for training- 1");
                        System.out.println("Object is Checked In");

                        // Refresh the object to retrieve the updated version
                        QueryResult refreshedResult = ConfigHelper.service.filteredIterationsOf(partMaster, new LatestConfigSpec());
                        if (refreshedResult.hasMoreElements()) {
                            WTPart updatedObject = (WTPart) refreshedResult.nextElement();
                            System.out.println("Part Version after Check-In: " + VersionControlHelper.getIterationDisplayIdentifier(updatedObject));
                        } else {
                            System.out.println("Failed to fetch updated part after check-in.");
                        }
                    } else {
                        System.out.println("First, Checkout the Part - Then Check-In");
                    }
                } else {
                    System.out.println("No latest iteration found for this part.");
                }
            } else {
                System.out.println("No WTPart found with the given number.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

