package sms.Role;

import org.apache.wicket.Session;
import org.apache.wicket.authorization.strategies.role.IRoleCheckingStrategy;
import org.apache.wicket.authorization.strategies.role.Roles;

import sms.admin.Log;
import sms.lookup.Lookup;
import sms.session.MySession;
import sms.user.Employee;

public class UserRoleAuthorizer implements IRoleCheckingStrategy{

	public UserRoleAuthorizer()
    {
    }

	public boolean hasAnyRole(Roles roles)
    {
		MySession authSession = (MySession)Session.get();
		//return authSession.getLoggedEmployee().hasAnyRole(roles);
		//for(int i = 0; i < roles.size(); i++)
			//System.out.println("test here: "+roles.toString());
		
		boolean result = authSession.getUserLoggedRoles().hasAnyRole(roles);
		
		if(!result){
			Lookup lookup2 = Lookup.AllLookupStatus();
			Employee emp = authSession.getLoggedEmployee();
			String file = lookup2.getLog_file_used();
			String logMessage = "WARNING! User with Account: " + Employee.displayAccountId(emp.getEmployee_id()) + " tried to access a restricted page ("+roles.toString()+ " page)";
			Log.loggedAction(lookup2.isLog_enabled(), file, logMessage);
		}
			
		return result;
    }

}
