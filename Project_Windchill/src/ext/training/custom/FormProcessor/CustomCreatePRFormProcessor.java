package ext.training.custom.FormProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.ptc.core.components.beans.ObjectBean;
import com.ptc.core.components.forms.FormProcessingStatus;
import com.ptc.core.components.forms.FormResult;
import com.ptc.core.components.util.FeedbackMessage;
import com.ptc.netmarkets.util.beans.NmCommandBean;
import com.ptc.windchill.enterprise.maturity.forms.processors.CreatePromotionRequestFormProcessor;

import wt.log4j.LogR;
import wt.util.WTException;

public class CustomCreatePRFormProcessor extends CreatePromotionRequestFormProcessor{
	private static final Logger LOGGER = LogR.getLogger(CustomCreatePRFormProcessor.class.getName());
	private static final String CANNOT_SET_TARGET_STATE_TO_CANCELLED_IN_THIS_CONTAINER = "Cannot Set Target State to Cancelled in this Container!!!";
	private static final String CANCELLED = "CANCELLED";
	private static final String MATURITY_STATE = "maturityState";
	private static final String PRODUCT_CONTAINER = "Gplm Product";
	
	@SuppressWarnings("unchecked")
	@Override
	public FormResult doOperation(NmCommandBean nmBean, List<ObjectBean> objectList) throws WTException {
		LOGGER.info("Method doOperation of CustomCreatePRFormProcessor :: START");
		FormResult result = null;
		String targetState = "";
		String containerName = "";
		String inValidContainer = PRODUCT_CONTAINER;
		try {			
			Map<String, ArrayList<String>> nmBeanComboMap = nmBean.getComboBox();
			if(nmBeanComboMap!=null && !nmBeanComboMap.isEmpty())
				targetState = nmBeanComboMap.getOrDefault(MATURITY_STATE, new ArrayList<>()).stream().findFirst().orElse("");
			containerName = nmBean.getContainer().getName();
			LOGGER.info("productContainer : "+PRODUCT_CONTAINER);
			LOGGER.info("targetState : "+targetState);
			LOGGER.info("containerName : "+containerName);

			//if inValidContainer is not empty or null proceed with logic.
			if(!inValidContainer.isEmpty() && inValidContainer!=null) {
				if(inValidContainer.equalsIgnoreCase(containerName) && targetState.equalsIgnoreCase(CANCELLED)) {
					result = new FormResult(FormProcessingStatus.FAILURE);
					FeedbackMessage failure = new FeedbackMessage();
					failure.addMessage(CANNOT_SET_TARGET_STATE_TO_CANCELLED_IN_THIS_CONTAINER);
					result.addFeedbackMessage(failure);
					LOGGER.info("Method doOperation of CustomCreatePRFormProcessor :: END");
					return result;
				}
			}
		}catch (WTException e) {
			LOGGER.debug("Exception in the doOperation method of CustomCreatePRFormProcessor : "+e.getLocalizedMessage());
		}
		LOGGER.info("Method doOperation of CustomCreatePRFormProcessor :: END");
		return super.doOperation(nmBean, objectList);		
	}
	
	/*
	<!-- Custom Promotion Request Form Processor : START -->
	<objecttype name="promotionRequest" class="wt.maturity.PromotionNotice" resourceBundle="com.ptc.windchill.enterprise.maturity.promotionRequestActionsRB">
	      <action name="create" selectRequired="true" ajax="component" uicomponent="PROMOTE">
	       <description>Custom Create Promotion Request Wizard</description>
	        <command class="ext.formprocessor.CustomCreatePRFormProcessor" method="execute" windowType="popup"/>
	   	 </action>
	</objecttype>
	<!-- Custom Promotion Request Form Processor : END -->
	*/

}
