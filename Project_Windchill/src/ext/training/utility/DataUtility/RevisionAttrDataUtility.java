package ext.training.utility.DataUtility;

import java.io.Serializable;

import com.ptc.core.components.descriptor.ModelContext;
import com.ptc.core.components.factory.dataUtilities.DefaultDataUtility;
import com.ptc.core.components.rendering.guicomponents.AttributeGuiComponent;
import com.ptc.core.lwc.server.PersistableAdapter;
import com.ptc.core.meta.common.UpdateOperationIdentifier;
import com.ptc.core.ui.resources.ComponentMode;

import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.iba.definition.StringDefinition;
import wt.iba.value.StringValue;
import wt.part.WTPart;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;
import wt.vc.VersionControlHelper;
import wt.vc.VersionIdentifier;

/**
 * Requirement for this Class
 * 1. Revision Attribute should not be visible in Create Layout
 * 2. Revision Attribute should not be visible in Edit Layout
 * 3. Revision Attribute should hold the value of the Revision of the object only.
 * 4. Once the  object is revised, the Attribute value should change to current revision
 */
public class RevisionAttrDataUtility extends DefaultDataUtility {

	/**
	 *This method will copy the Revision to the new Attribute
	 */
	@Override
	public Object getDataValue(String paramString, Object paramObject, ModelContext paramModelContext) throws WTException {
		System.out.println("Inside getDataValue Method");
		StringValue stringvalue = null;
		ComponentMode mode = paramModelContext.getDescriptorMode();
		Object object = super.getDataValue(paramString, paramObject, paramModelContext);
		AttributeGuiComponent guiComp = (AttributeGuiComponent) object;
		String revision = null;

		if (mode.equals(ComponentMode.VIEW)) {
			if(paramObject instanceof WTPart) {
				WTPart part = (WTPart) paramObject;
				VersionIdentifier versionIdentifier = VersionControlHelper.getVersionIdentifier(part);
				revision = versionIdentifier.getValue();
				System.out.println("Inside instance of WTPart"+revision);
				final QueryResult qr = VersionControlHelper.service.allVersionsOf(part);
				if(qr.size()==1)
				{
					PersistableAdapter pers = new PersistableAdapter(part, null, null, new UpdateOperationIdentifier());
					pers.load("Revision");
					String revisionNum=(String) pers.get("Revision");
					if (revisionNum == null) {
						StringDefinition stringdefinition = getStringDef("Revision");
						stringvalue = getStringValue((Persistable) part, stringdefinition);
						if (stringvalue == null) {
							stringvalue = StringValue.newStringValue(stringdefinition, part, revision);
						}
					}else {
						System.out.println("No need to update part");
						return object;
					}
				}
				else
				{
					return object;
				}
			}else {

				return null;
			}	
			PersistenceHelper.manager.save(stringvalue);

			return revision;
		}
		else if(!mode.equals(ComponentMode.VIEW))
		{
			guiComp.setComponentHidden(true);
			return object;
		}

		return object;
	}

	private static StringDefinition getStringDef(String stringDefName) throws WTException {

		QuerySpec queryspec = new QuerySpec(StringDefinition.class);
		SearchCondition searchcondition = new SearchCondition(StringDefinition.class, "name", "=", stringDefName);
		queryspec.appendWhere(searchcondition, new int[] { 0 });
		QueryResult queryresult = PersistenceHelper.manager.find((StatementSpec) queryspec);
		StringDefinition stringdefinition = null;
		if (queryresult.hasMoreElements()) { return (StringDefinition) queryresult.nextElement(); }

		return stringdefinition;
	}

	private static StringValue getStringValue(Persistable persistable, StringDefinition stringdefinition) throws WTException {

		if (stringdefinition == null) { return null; }
		QuerySpec queryspec = new QuerySpec(StringValue.class);
		queryspec.appendWhere(new SearchCondition(StringValue.class, "theIBAHolderReference.key", "=", persistable.getPersistInfo()
				.getObjectIdentifier()), new int[]
						{ 0 });
		queryspec.appendAnd();
		queryspec.appendWhere(new SearchCondition(StringValue.class, "definitionReference.key", "=", stringdefinition.getPersistInfo()
				.getObjectIdentifier()), new int[]
						{ 0 });
		QueryResult queryresult = PersistenceHelper.manager.find((StatementSpec) queryspec);
		if (queryresult.hasMoreElements()) {

			return (StringValue) queryresult.nextElement();
		} else {
			return null;

		}
	}

	/*
	<Service context="default" name="com.ptc.core.components.descriptor.DataUtility"
		targetFile="codebase/com/ptc/core/components/components.dataUtilities.properties">
		<Option cardinality="duplicate" order="0" overridable="true"
	  	requestor="java.lang.Object" selector="Revision"
	  	serviceClass="ext.training.utility.DataUtility.RevisionAttrDataUtility"/>
	</Service>
	 */

}
