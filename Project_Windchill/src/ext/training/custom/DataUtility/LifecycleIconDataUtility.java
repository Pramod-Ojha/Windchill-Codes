package ext.training.custom.DataUtility;

import com.ptc.core.components.descriptor.ModelContext;
import com.ptc.core.components.factory.dataUtilities.DefaultDataUtility;
import com.ptc.core.components.rendering.guicomponents.IconComponent;

import wt.part.WTPart;
import wt.util.WTException;


public class LifecycleIconDataUtility extends DefaultDataUtility {
	
	private static final String No = null;
	
	@Override
	public Object getDataValue(String paramString, Object paramObject, ModelContext paramModelContext) 
			throws WTException {
		WTPart part = (WTPart) paramObject;
		String state = part.getState().getState().toString();
		String imgURL = null;
		
		if(state.equals("RELEASED")) {
			imgURL = "netmarkets/images/green.gif";			
		}
		else if(state.equals("INWORK")) {
			imgURL = "netmarkets/images/yellow.gif";
		}
		else if (state.equals("CANCELLED")){
			imgURL = "netmarkets/images/red.gif";
		}
		else {
			imgURL = "netmarkets/images/black.gif";
		}
		
		IconComponent iconComponent = new IconComponent(imgURL);
		
		return iconComponent;
	}

	/**
	 * Xconf Entry
		<Service name="com.ptc.core.components.descriptor.DataUtility" 
			targetFile= "codebase/com/ptc/core/components/components.dataUtilities.properties">
		<Option cardinality="singleton" order="0" overridable="true"
		requestor="java.lang.Object" selector="lifecycleIconDataUtility"
		serviceClass="ext.training.custom.DataUtility.LifecycleIconDataUtility"/>
		</Service>
*/
	
	

}
