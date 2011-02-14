package sms.admin;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;

import sms.index.Home;

@AuthorizeInstantiation("Administration")
public class Layout extends Home{
	public Layout(){
	}
}
