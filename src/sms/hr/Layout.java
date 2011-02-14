package sms.hr;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;

import sms.index.Home;

@AuthorizeInstantiation("HR")
public class Layout extends Home{
	
	public Layout(){}
}
