package ext.training.mvc.Builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.ptc.core.ui.resources.ComponentMode;
import com.ptc.jca.mvc.components.JcaComponentParams;
import com.ptc.jca.mvc.components.JcaTableConfig;
import com.ptc.mvc.components.AbstractComponentBuilder;
import com.ptc.mvc.components.ColumnConfig;
import com.ptc.mvc.components.ComponentBuilder;
import com.ptc.mvc.components.ComponentConfig;
import com.ptc.mvc.components.ComponentConfigFactory;
import com.ptc.mvc.components.ComponentParams;

import ext.training.resource.CustomPreferenceResourceRB;
import wt.doc.WTDocument;
import wt.doc.WTDocumentMaster;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.fc.IdentityChangeHistory;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;
import wt.util.WTMessage;
import wt.util.WTStandardDateFormat;

@ComponentBuilder({"TrainingReNumberHistoryBuilder"})
public class TrainingReNumberHistoryBuilder extends AbstractComponentBuilder {
	
	private static final String RESOURCE = CustomPreferenceResourceRB.class.getName();
	private static final String IDENTITY_HISTORY_ID = "object.key.id";
	

	@Override
	public Object buildComponentData(ComponentConfig paramComponentConfig, ComponentParams paramComponentParams) throws Exception {
		Object localObject = ((JcaComponentParams)paramComponentParams).getContextObject();
		ArrayList<HashMap<String, String>> map = new ArrayList<HashMap<String, String>>();
		if (localObject instanceof WTPart) {
			WTPartMaster master = (WTPartMaster) ((WTPart) localObject).getMaster();
			QueryResult qr3 =getHistoryResultSet(master);
			while (qr3.hasMoreElements()) {
				Object nextElement = (Persistable) qr3.nextElement();
				if (nextElement instanceof IdentityChangeHistory) {
					IdentityChangeHistory idc = (IdentityChangeHistory) nextElement;
					HashMap<String, String> map1 = new HashMap<String, String>();
					map1.put("name", master.getName());
					map1.put("oldNumber", idc.getOldValue());
					map1.put("newNumber", idc.getNewValue());
					map1.put("user", idc.getPrincipal().getName()); 

					Date date = new Date(idc.getPersistInfo().getModifyStamp().getTime());
					String format = WTStandardDateFormat.format(date, "yyyy-MM-dd HH:mm zzz");
					map1.put("modifiedDate", format);
					map.add(map1);
				}
			}
			
		}
		else if (localObject instanceof WTDocument) {
			WTDocumentMaster master = (WTDocumentMaster) ((WTDocument) localObject).getMaster();
			QueryResult qr3 =getHistoryResultSet(master);
			while (qr3.hasMoreElements()) {
				Object nextElement = (Persistable) qr3.nextElement();
				if (nextElement instanceof IdentityChangeHistory) {
					IdentityChangeHistory idc = (IdentityChangeHistory) nextElement;
					HashMap<String, String> map1 = new HashMap<String, String>();
					map1.put("name", master.getName());
					map1.put("oldNumber", idc.getOldValue());
					map1.put("newNumber", idc.getNewValue());
					map1.put("user", idc.getPrincipal().getName()); 

					Date date = new Date(idc.getPersistInfo().getModifyStamp().getTime());
					String format = WTStandardDateFormat.format(date, "yyyy-MM-dd HH:mm zzz");
					map1.put("modifiedDate", format);
					map.add(map1);
				}
			}
		}
		else if (localObject instanceof EPMDocument) {
			EPMDocumentMaster master = (EPMDocumentMaster) ((EPMDocument) localObject).getMaster();
			QueryResult qr3 =getHistoryResultSet(master);
			while (qr3.hasMoreElements()) {
				Object nextElement = (Persistable) qr3.nextElement();
				if (nextElement instanceof IdentityChangeHistory) {
					IdentityChangeHistory idc = (IdentityChangeHistory) nextElement;
					HashMap<String, String> map1 = new HashMap<String, String>();
					map1.put("name", master.getName());
					map1.put("oldNumber", idc.getOldValue());
					map1.put("newNumber", idc.getNewValue());
					map1.put("user", idc.getPrincipal().getName()); 

					Date date = new Date(idc.getPersistInfo().getModifyStamp().getTime());
					String format = WTStandardDateFormat.format(date, "yyyy-MM-dd HH:mm zzz");
					map1.put("modifiedDate", format);
					map.add(map1);
				}
			}
		}
		return map;
	}

	@Override
	public ComponentConfig buildComponentConfig(ComponentParams paramComponentParams) throws WTException {
		ComponentConfigFactory localComponentConfigFactory = getComponentConfigFactory();
		JcaTableConfig localJcaTableConfig = (JcaTableConfig) localComponentConfigFactory.newTableConfig();

		// localJcaTableConfig.setLabel("Training RENUMBER HISTORY");
		localJcaTableConfig.setLabel( CustomPreferenceResourceRB.TRAINING_RENUMBER);
		localJcaTableConfig.setId("history.partConfigReNumberHistory");
		localJcaTableConfig.setSelectable(true);
		localJcaTableConfig.setHelpContext("ObjectOviewInfoTablesAbout");
		localJcaTableConfig.setComponentMode(ComponentMode.VIEW);


		ColumnConfig localColumnConfig6 = localComponentConfigFactory.newColumnConfig("name", true);
		localColumnConfig6.setLabel(WTMessage.getLocalizedMessage(RESOURCE, CustomPreferenceResourceRB.NAME, null));
		localColumnConfig6.setWidth(170);
		localJcaTableConfig.addComponent(localColumnConfig6);

		/*
		 * ColumnConfig localColumnConfig7 =
		 * localComponentConfigFactory.newColumnConfig("version", true);
		 * localColumnConfig7.setLabel(WTMessage.getLocalizedMessage(RESOURCE,
		 * CommonMessagesRB.VERSION, null)); localColumnConfig7.setWidth(170);
		 * localJcaTableConfig.addComponent(localColumnConfig7);
		 */

		/*
		 * ColumnConfig localColumnConfig8 =
		 * localComponentConfigFactory.newColumnConfig("infoPageAction", false);
		 * localColumnConfig8.setDataUtilityId("infoPageAction");
		 * localJcaTableConfig.addComponent(localColumnConfig8);
		 */

		ColumnConfig localColumnConfig2 = localComponentConfigFactory.newColumnConfig("oldNumber", true);
		localColumnConfig2.setLabel(WTMessage.getLocalizedMessage(RESOURCE, CustomPreferenceResourceRB.OLD_NUMBER, null));
		localColumnConfig2.setWidth(170);
		localJcaTableConfig.addComponent(localColumnConfig2);

		ColumnConfig localColumnConfig3 = localComponentConfigFactory.newColumnConfig("newNumber", true);
		localColumnConfig3.setLabel(WTMessage.getLocalizedMessage(RESOURCE, CustomPreferenceResourceRB.NEW_NUMBER, null));
		localColumnConfig3.setWidth(170);
		localJcaTableConfig.addComponent(localColumnConfig3);


		ColumnConfig localColumnConfig4 = localComponentConfigFactory.newColumnConfig("user", true);
		localColumnConfig4.setLabel("Modified By");
		localJcaTableConfig.addComponent(localColumnConfig4);

		ColumnConfig localColumnConfig5 = localComponentConfigFactory.newColumnConfig("modifiedDate", true);
		localColumnConfig5.setLabel(WTMessage.getLocalizedMessage(RESOURCE, CustomPreferenceResourceRB.MODIFIED_DATE, null));
		localColumnConfig5.setDefaultSort(true);
		localJcaTableConfig.addComponent(localColumnConfig5);

		return localJcaTableConfig;
	}

	/**
	 * getHistoryResultSet.
	 * 
	 * @param master
	 *            WTPartMaster
	 * @return QueryResult of the IdentityChangeHistory table where
	 *         IdentityChangeHistory.ATTRIBUTE_NAME='number' and
	 *         IdentityChangeHistory ida3a3=WTPartMaster ida2a2
	 * @throws WTException
	 *             the WTException
	 */
	public QueryResult getHistoryResultSet(final WTPartMaster master) throws WTException {
		QuerySpec qs3 = new QuerySpec(IdentityChangeHistory.class);
		long id = master.getPersistInfo().getObjectIdentifier().getId();

		// Attribute Name should be Name
		SearchCondition condition2 = new SearchCondition(IdentityChangeHistory.class,
				IdentityChangeHistory.ATTRIBUTE_NAME, SearchCondition.EQUAL, "number");
		qs3.appendWhere(condition2, new int[] { 0 });
		qs3.appendAnd();

		// IdentityChangeHistory ida3a3 should be equal to WTPartMaster ida2a2
		SearchCondition condition3 = new SearchCondition(IdentityChangeHistory.class, IDENTITY_HISTORY_ID,
				SearchCondition.EQUAL, id);
		qs3.appendWhere(condition3, new int[] { 0 });

		System.out.println(" Query ----------"+qs3);
		return PersistenceHelper.manager.find((StatementSpec) qs3);
	}

	private QueryResult getHistoryResultSet(EPMDocumentMaster master) throws WTException {
		QuerySpec qs3 = new QuerySpec(IdentityChangeHistory.class);
		long id = master.getPersistInfo().getObjectIdentifier().getId();

		// Attribute Name should be Name
		SearchCondition condition2 = new SearchCondition(IdentityChangeHistory.class,
				IdentityChangeHistory.ATTRIBUTE_NAME, SearchCondition.EQUAL, "number");
		qs3.appendWhere(condition2, new int[] { 0 });
		qs3.appendAnd();

		// IdentityChangeHistory ida3a3 should be equal to WTPartMaster ida2a2
		SearchCondition condition3 = new SearchCondition(IdentityChangeHistory.class, IDENTITY_HISTORY_ID,
				SearchCondition.EQUAL, id);
		qs3.appendWhere(condition3, new int[] { 0 });

		return PersistenceHelper.manager.find((StatementSpec) qs3);
	}

	private QueryResult getHistoryResultSet(WTDocumentMaster master) throws WTException {
		QuerySpec qs3 = new QuerySpec(IdentityChangeHistory.class);
		long id = master.getPersistInfo().getObjectIdentifier().getId();

		// Attribute Name should be Name
		SearchCondition condition2 = new SearchCondition(IdentityChangeHistory.class,
				IdentityChangeHistory.ATTRIBUTE_NAME, SearchCondition.EQUAL, "number");
		qs3.appendWhere(condition2, new int[] { 0 });
		qs3.appendAnd();

		// IdentityChangeHistory ida3a3 should be equal to WTPartMaster ida2a2
		SearchCondition condition3 = new SearchCondition(IdentityChangeHistory.class, IDENTITY_HISTORY_ID,
				SearchCondition.EQUAL, id);
		qs3.appendWhere(condition3, new int[] { 0 });

		return PersistenceHelper.manager.find((StatementSpec) qs3);
	}
}
