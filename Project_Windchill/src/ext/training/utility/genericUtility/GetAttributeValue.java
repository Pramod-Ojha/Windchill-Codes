package ext.training.utility.genericUtility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ptc.core.lwc.server.PersistableAdapter;
import com.ptc.core.meta.common.UpdateOperationIdentifier;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.vc.config.ConfigHelper;
import wt.vc.config.LatestConfigSpec;

public class GetAttributeValue implements RemoteAccess, Serializable{
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws Exception{
		
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		rms.setUserName("wcadmin");
		rms.setPassword("wcadmin");
		
		readAttributeValue();

	}

	private static void readAttributeValue() throws Exception{
		
		String name = "name";
		String number = "number";
		String country = "Country";
		String testAttr = "TestAttr";
		
		List<String> attrList = new ArrayList<String>();
		attrList.add(name);
		attrList.add(number);
		attrList.add(country);
		attrList.add(testAttr);
		
		QuerySpec querySpec = new QuerySpec(WTPart.class);
		querySpec.appendWhere(new SearchCondition(WTPart.class, WTPart.NUMBER, SearchCondition.EQUAL,"0000000008"), null);
		QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec)querySpec);
		System.out.println("Size of queryResult: -------- "+queryResult.size());
		System.out.println();
		
		if(queryResult.hasMoreElements()) {
			WTPart part = (WTPart) queryResult.nextElement();
			WTPartMaster partMaster = part.getMaster();
			QueryResult latestResult = ConfigHelper.service.filteredIterationsOf(partMaster, new LatestConfigSpec());
			WTPart latestPart = (WTPart) latestResult.nextElement();
			
			PersistableAdapter adapter = new PersistableAdapter(latestPart, null, null, new UpdateOperationIdentifier());
			adapter.persist();
			adapter.load(attrList);
			for(String attribute : attrList) {
				Object value = adapter.get(attribute);
				System.out.println("Attribute: "+attribute +"     "+ "Value: "+value);
			}
		}else {
			System.out.println("No Result found");
		}
		
	}

}
