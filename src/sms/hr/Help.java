package sms.hr;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;

@AuthorizeInstantiation("HR")
public class Help extends Layout{
	
	public Help(){}

}
