package sms.program;

import java.io.Serializable;

public class Program implements Serializable{
	private int program_code;
	private String program_name;
	private int dept_code;
	
	public Program(){}
	public Program(int programCode, String programName, int deptCode) {
		super();
		program_code = programCode;
		program_name = programName;
		dept_code = deptCode;
	}
	public int getProgram_code() {
		return program_code;
	}
	public void setProgram_code(int programCode) {
		program_code = programCode;
	}
	public String getProgram_name() {
		return program_name;
	}
	public void setProgram_name(String programName) {
		program_name = programName;
	}
	public int getDept_code() {
		return dept_code;
	}
	public void setDept_code(int deptCode) {
		dept_code = deptCode;
	}
	public static Program getProgramFromCode(int code){
		Program program = new Program();
		program.setProgram_code(code);
		ProgramQuery query = new ProgramQuery();
		return query.getDatum(program, "getProgramFromCode");
	}
}
