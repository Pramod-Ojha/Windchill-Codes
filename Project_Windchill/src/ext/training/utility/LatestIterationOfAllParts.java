package ext.training.utility;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.type.ClientTypedUtility;
import wt.vc.VersionControlHelper;
import wt.vc.config.ConfigHelper;
import wt.vc.config.LatestConfigSpec;

/**
 * This class returns the latest Revision and Iteration of all WTParts.
 */
public class LatestIterationOfAllParts implements RemoteAccess, Serializable {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) throws Exception {
        RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
        remoteMethodServer.setUserName("wcadmin");
        remoteMethodServer.setPassword("wcadmin");

        getLatestParts();
    }

    public static void getLatestParts() throws Exception {
        QuerySpec querySpecPart = new QuerySpec(WTPart.class);
        QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec) querySpecPart);
        System.out.println("Total WTParts in the system: " + queryResult.size());

        // Use a Set to store processed WTPartMasters to avoid duplicates
        Set<WTPartMaster> processedMasters = new HashSet<>();

        // Loop through all WTParts
        while (queryResult.hasMoreElements()) {
            WTPart part = (WTPart) queryResult.nextElement();
            WTPartMaster partMaster = part.getMaster();

            // Skip if this master has already been processed
            if (processedMasters.contains(partMaster)) {
                continue;
            }
            processedMasters.add(partMaster);

            // Fetch latest revision and iteration for this part master
            QueryResult latestResult = ConfigHelper.service.filteredIterationsOf(partMaster, new LatestConfigSpec());

            if (latestResult.hasMoreElements()) {
                WTPart latestPart = (WTPart) latestResult.nextElement();
                String type = ClientTypedUtility.getTypeIdentifier(latestPart).getTypename();

                System.out.println(latestPart.getName() + "  "
                        + VersionControlHelper.getIterationDisplayIdentifier(latestPart) + "   " + type);
            }
        }
    }
}
