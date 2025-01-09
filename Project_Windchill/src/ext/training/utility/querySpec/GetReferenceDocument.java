package ext.training.utility.querySpec;

import java.io.Serializable;

import wt.doc.WTDocument;
import wt.doc.WTDocumentMaster;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.WTPart;
import wt.part.WTPartHelper;
import wt.part.WTPartMaster;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.vc.VersionControlHelper;
import wt.vc.config.ConfigHelper;
import wt.vc.config.LatestConfigSpec;

public class GetReferenceDocument implements RemoteAccess, Serializable {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) throws Exception {

        RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
        remoteMethodServer.setUserName("wcadmin");
        remoteMethodServer.setPassword("wcadmin");

        getReferenceDocument();
    }

    private static void getReferenceDocument() throws Exception {

        QuerySpec querySpecPart = new QuerySpec(WTPart.class);
        querySpecPart.appendWhere(new SearchCondition(WTPart.class, WTPart.NUMBER, SearchCondition.EQUAL, "GPLM-00010003"), new int[] { 0 });
        QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec) querySpecPart);
        System.out.println("Size of the Result: --------- " + queryResult.size());

        System.out.println("\nSearching for WTPart");

        if (queryResult.hasMoreElements()) {
            WTPart part = (WTPart) queryResult.nextElement();
            WTPartMaster partMaster = part.getMaster();
            QueryResult masterResult = ConfigHelper.service.filteredIterationsOf(partMaster, new LatestConfigSpec());
            System.out.println("\nSize of masterResult Container:   " + masterResult.size());
            WTPart latestPart = (WTPart) masterResult.nextElement();
            System.out.println("\nPart name:   " + latestPart.getName());
            System.out.println("Part latest Version: " + VersionControlHelper.getIterationDisplayIdentifier(latestPart));

            QueryResult refResult = WTPartHelper.service.getReferencesWTDocumentMasters(latestPart);
            System.out.println("\nSize of ReferenceResult container: "+refResult.size());

            while (refResult.hasMoreElements()) {
                WTDocumentMaster docMaster = (WTDocumentMaster) refResult.nextElement(); // Cast to WTDocumentMaster
                System.out.println("\nReferenced Document Master:   " + docMaster.getName() + "     " + docMaster.getNumber());
                

                // Retrieve the latest iteration of the WTDocument
                QueryResult docResult = ConfigHelper.service.filteredIterationsOf(docMaster, new LatestConfigSpec());
                if (docResult.hasMoreElements()) {
                    WTDocument refDoc = (WTDocument) docResult.nextElement();
                    System.out.println("Referenced Document:   " + refDoc.getName() + "     " + refDoc.getNumber());
                    System.out.println("RefDoc Version: " + VersionControlHelper.getIterationDisplayIdentifier(refDoc));
                } else {
                    System.out.println("No iterations found for WTDocumentMaster: " + docMaster.getNumber());
                }
            }

        }
    }

}
