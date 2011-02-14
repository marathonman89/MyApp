package sms.admin;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;

import sms.Role.Role;

@AuthorizeInstantiation("Administration")
public class Help extends Layout{
	public Help(){
		//MetaDataRoleAuthorizationStrategy.authorize(this, Role.administration);
	}
}
