package ext.training.custom.Filters;

import com.ptc.core.ui.validation.DefaultSimpleValidationFilter;
import com.ptc.core.ui.validation.UIValidationCriteria;
import com.ptc.core.ui.validation.UIValidationKey;
import com.ptc.core.ui.validation.UIValidationStatus;

import wt.inf.container.WTContained;
import wt.inf.container.WTContainer;
import wt.part.WTPart;

public class FilterBasedOnLifecycleState extends DefaultSimpleValidationFilter {
	
	private static final String CLASSNAME = FilterBasedOnLifecycleState.class.getName();

	@Override
	public UIValidationStatus preValidateAction(UIValidationKey valKey, UIValidationCriteria valCriteria) {
		UIValidationStatus valStatus = UIValidationStatus.DISABLED;
		
		WTContained contextObj = (WTContained) valCriteria.getContextObject().getObject();
		WTContainer container = contextObj.getContainer();
		
		if(contextObj instanceof WTPart) {
			WTPart part = (WTPart) contextObj;
			String state = part.getState().getState().toString();
			if(state.equalsIgnoreCase("TRAINING")) {
				valStatus = UIValidationStatus.ENABLED;
			}else {
				valStatus = UIValidationStatus.HIDDEN;
			}
		}
		return valStatus;		
	}
	/*
	 * 	
	<Service context="default" name="com.ptc.core.ui.SimpleValidationFilter"
				targetFile="codebase/service.properties">
			<Option cardinality="duplicate" requestor="null" selector="actionVisibilityFilter"
			serviceClass="ext.training.custom.Filters.FilterBasedOnLifecycleState"/>
	</Service>
	 */
	

}
