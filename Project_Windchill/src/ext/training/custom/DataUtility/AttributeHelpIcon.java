package ext.training.custom.DataUtility;

import java.util.ArrayList;

import com.ptc.core.components.descriptor.ModelContext;
import com.ptc.core.components.factory.dataUtilities.DefaultDataUtility;
import com.ptc.core.components.rendering.GuiComponent;
import com.ptc.core.components.rendering.guicomponents.IconComponent;
import com.ptc.core.ui.resources.ComponentMode;

import wt.util.WTException;

public class AttributeHelpIcon extends DefaultDataUtility {

	private static final String No = null;

	@Override
	public Object getDataValue(String paramString, Object paramObject, ModelContext paramModelContext) 
			throws WTException {

		ComponentMode mode=paramModelContext.getDescriptorMode();
		
		ArrayList<Object> components = new ArrayList<Object>();
		components.add((GuiComponent)super.getDataValue(paramString, paramObject, paramModelContext));
		if (mode.equals(ComponentMode.VIEW)) {
			IconComponent icon = new IconComponent("netmarkets/images/help_tablebutton.gif");
			icon.setId(paramString);
			icon.setTooltip("Example of icon tooltip(information)");
			components.add(icon);	
		}
		return components;
	}

	/*
	  <Service context="default"
	  name="com.ptc.core.components.descriptor.DataUtility" targetFile=
	  "codebase/com/ptc/core/components/components.dataUtilities.properties">
	  <Option cardinality="singleton" order="0" overridable="true"
	  requestor="java.lang.Object" selector="HelpAttribute"
	  serviceClass="ext.training.custom.DataUtility.AttributeHelpIcon"/>
	  </Service>
	 */

}

