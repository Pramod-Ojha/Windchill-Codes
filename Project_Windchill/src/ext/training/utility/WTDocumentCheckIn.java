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
 * This class finds the latest iteration of a WTDocument and Checks it In if it is Checked Out.
 */
public class WTDocumentCheckIn implements RemoteAccess, Serializable {
    
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) throws Exception {
        // Set credentials for Remote Method Server
        RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
        remoteMethodServer.setUserName("wcadmin");
        remoteMethodServer.setPassword("wcadmin");
        
        // Call the check-in method
        doCheckOut();
    }

    /**
     * Finds the latest iteration of a WTDocument by its number and checks it in.
     */
    private static void doCheckOut() throws Exception {
        // Search query in WTDocument table
        QuerySpec querySpecDoc = new QuerySpec(WTDocument.class);
        System.out.println("Entering Check-In Method");
        
        try {
            // Add search condition for WTDocument.NUMBER
            querySpecDoc.appendWhere(
                new SearchCondition(WTDocument.class, WTDocument.NUMBER, SearchCondition.EQUAL, "0000000022"), null);
            
            // Execute query and get results
            QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec) querySpecDoc);
            System.out.println("\nNumber of matching documents: " + queryResult.size());
            
            // Process the result to fetch the latest iteration of the document
            if (queryResult.hasMoreElements()) {
                // Fetch the WTDocument object from the query result
                WTDocument doc = (WTDocument) queryResult.nextElement();
                System.out.println("Found WTDocument: " + doc.getName());

                // Retrieve the master reference of the document
                WTDocumentMaster docMaster = (WTDocumentMaster) doc.getMaster();

                // Retrieve the latest iteration of the document
                QueryResult latestResult = ConfigHelper.service.filteredIterationsOf(docMaster, new LatestConfigSpec());
                if (!latestResult.hasMoreElements()) {
                    System.out.println("No latest iteration found for document master.");
                    return;
                }

                // Fetch the latest WTDocument object
                WTDocument latestDoc = (WTDocument) latestResult.nextElement();
                System.out.println("Document Version before Check-In: " + 
                    VersionControlHelper.getIterationDisplayIdentifier(latestDoc));

                // Perform check-in if the document is checked out
                if (WorkInProgressHelper.isCheckedOut(latestDoc)) {
                    System.out.println("\nChecking In the Document");
                    WorkInProgressHelper.service.checkin(latestDoc, "Checked-In through code for training - 4");
                    System.out.println("Document has been Checked-In");

                    // Refresh the latest version after check-in
                    QueryResult refreshResult = ConfigHelper.service.filteredIterationsOf(docMaster, new LatestConfigSpec());
                    if (refreshResult.hasMoreElements()) {
                        WTDocument updatedDoc = (WTDocument) refreshResult.nextElement();
                        String type = ClientTypedUtility.getTypeIdentifier(updatedDoc).getTypename();

                        // Print details of the updated document
                        System.out.println("\nDocument Name:                            " + updatedDoc.getName());
                        System.out.println("Document Number:                          " + updatedDoc.getNumber());
                        System.out.println("Document Latest Version after Check-In:   " + 
                            VersionControlHelper.getIterationDisplayIdentifier(updatedDoc));
                        System.out.println("Document Type Name:                       " + type);
                    } else {
                        System.out.println("Failed to fetch the updated document after check-in.");
                    }

                } else {
                    System.out.println("\nDocument is already checked in. Please check it out first.");
                }
            } else {
                System.out.println("No matching document found.");
            }

        } catch (Exception e) {
            // Log error details
            System.err.println("Error during document check-in process: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

