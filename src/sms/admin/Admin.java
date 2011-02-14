package sms.admin;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;

import sms.index.Home;
import sms.session.MySession;
import sms.user.Employee;

@AuthorizeInstantiation("Administration")
public class Admin extends Home{
	
	public Admin(){

	}
}
