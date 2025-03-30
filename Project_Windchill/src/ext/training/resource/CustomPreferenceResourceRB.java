package ext.training.resource;

import wt.util.resource.RBEntry;
import wt.util.resource.RBNameException;
import wt.util.resource.RBUUID;
import wt.util.resource.WTListResourceBundle;

@RBUUID("ext.training.resource.CustomPreferenceResourceRB")
@RBNameException
public class CustomPreferenceResourceRB extends WTListResourceBundle {
	
	@RBEntry("Roles which should be allowed for Setstate Action")
    public static final String CUSTOM_PREF_LABEL_NAME = "CUSTOM_PREF.displayName";
    @RBEntry("Roles which should be allowed for Setstate Action")
    public static final String CUSTOM_PREF_LABEL_DESCRIPTION = "CUSTOM_PREF.description";
    @RBEntry("Roles which should be allowed for Setstate Action")
    public static final String CUSTOM_PREF_LABEL_LONG_DESCRIPTION = "CUSTOM_PREF.longDescription";
	
	@RBEntry("Organization in which setstae should be disabled")
    public static final String CUSTOM_PREFF_LABEL_NAME = "CUSTOM_PREFF.displayName";
    @RBEntry("Organization in which setstae should be disabled")
    public static final String CUSTOM_PREFF_LABEL_DESCRIPTION = "CUSTOM_PREFF.description";
    @RBEntry("Organization in which setstae should be disabled")
    public static final String CUSTOM_PREFF_LABEL_LONG_DESCRIPTION = "CUSTOM_PREFF.longDescription";


   // Preference Categories
    @RBEntry("Training")
    public static final String Roles_DISPLAY_NAME = "Roles.displayName";
    @RBEntry("Preference for Training Purpose")
    public static final String Roles_DESCRIPTION = "Roles.description";

    @RBEntry("Number")
    public static final String NUMBER = "Number";

    @RBEntry("Name")
    public static final String NAME = "Name";

    @RBEntry("Organization ID")
    public static final String ORGANIZATION_ID = "Organization ID";

    @RBEntry("Version")
    public static final String VERSION = "Version";

    @RBEntry("State")
    public static final String STATE = "State";
 
    @RBEntry("Training Number History")
    public static final String TRAINING_RENUMBER = "TRAINING RENUMBER HISTORY";

    @RBEntry("Rename Type")
    public static final String RENAME_TYPE = "Rename Type";

    @RBEntry("Old Name")
    public static final String OLD_NAME = "Old Name"; 

    @RBEntry("New Name")
    public static final String NEW_NAME = "New Name";

    @RBEntry("Modified Date")
    public static final String MODIFIED_DATE = "Modified Date";
    

    @RBEntry("Old Number")
    public static final String OLD_NUMBER = "Old Number"; 

    @RBEntry("New Number")
    public static final String NEW_NUMBER = "New Number";

    
}