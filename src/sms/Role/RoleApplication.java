package sms.Role;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebApplication;

import sms.admin.Help;
import sms.index.Home;
import sms.session.MySession;

public class RoleApplication extends WebApplication{

	public RoleApplication(){
		super();
	}
	public Class getHomePage()
    {
        return Home.class;
    }
	public Session newSession(Request request, Response response)
    {
        return new MySession(request);
    }
	protected void init(){
        super.init();
        //getDebugSettings().setDevelopmentUtilitiesEnabled(true);
        getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(new UserRoleAuthorizer()));
        MetaDataRoleAuthorizationStrategy.authorize(Help.class, Role.administration);
    }

}
