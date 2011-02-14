package sms.index;


import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebApplication;

import sms.Role.Role;
import sms.Role.UserRoleAuthorizer;
import sms.session.MySession;

public class MyApp extends WebApplication{
	public Class getHomePage(){
		return Login.class;
	}
	
	//public Session newSession(Request request, Response response) {
		//return new MySession(request);
	//}
	
	public Session newSession(Request request, Response response) {
		return new MySession(MyApp.this, request);
	}
	
	public void init(){
		super.init();
		getDebugSettings().setAjaxDebugModeEnabled(false);
		getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(new UserRoleAuthorizer()));
        //MetaDataRoleAuthorizationStrategy.authorize(sms.admin.Help.class, Role.administration);
        //MetaDataRoleAuthorizationStrategy.authorize(sms.advising.Help.class, Role.department);
		//MetaDataRoleAuthorizationStrategy.authorize(sms.hr.Help.class, Role.hr);
	}
	
}
