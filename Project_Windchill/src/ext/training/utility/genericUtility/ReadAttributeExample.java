package ext.training.utility.genericUtility;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import com.ptc.core.lwc.server.PersistableAdapter;
import com.ptc.core.meta.common.UpdateOperationIdentifier;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.WTPart;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;

public class ReadAttributeExample implements RemoteAccess, Serializable {
    
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) throws WTException, RemoteException, InvocationTargetException {
        if (!RemoteMethodServer.ServerFlag) {
            RemoteMethodServer.getDefault().invoke("readAttribute", ReadAttributeExample.class.getName(), null, new Class[0], new Object[0]);
        } else {
            readAttribute();
        }
    }

    public static void readAttribute() throws WTException {
        // Replace with the actual part number and attribute name
        String partNumber = "0000000006";
        String attributeName = "HelpAttribute";

        // Find the WTPart
        WTPart part = findWTPartByNumber(partNumber);

        if (part == null) {
            System.out.println("WTPart not found for number: " + partNumber);
            return;
        }

        // Use PersistableAdapter to load the attribute
        PersistableAdapter adapter = new PersistableAdapter(part, null, null, new UpdateOperationIdentifier());
        adapter.load(attributeName);

        // Retrieve the attribute value
        String attributeValue = (String) adapter.get(attributeName);

        if (attributeValue != null) {
            System.out.println("Value of " + attributeName + " is: " + attributeValue);
        } else {
            System.out.println("Attribute " + attributeName + " is not set.");
        }
    }

    private static WTPart findWTPartByNumber(String number) throws WTException {
        // Use QuerySpec to find WTPart by its number
        QuerySpec querySpec = new QuerySpec(WTPart.class);
        querySpec.appendWhere(new SearchCondition(WTPart.class, WTPart.NUMBER, SearchCondition.EQUAL, number), null);

        QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec) querySpec);

        if (queryResult.hasMoreElements()) {
            return (WTPart) queryResult.nextElement();
        }
        return null;
    }
}
