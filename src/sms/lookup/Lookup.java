package sms.lookup;

import java.io.Serializable;

public class Lookup implements Serializable{
	private Boolean enr_on;
	private Integer year_started;
	private String sem_started;
	private String log_file_used;
	private Boolean log_enabled;
	
	public Lookup(){}

	public Lookup(Boolean enrOn, Integer yearStarted,
			String semStarted) {
		super();
		enr_on = enrOn;
		year_started = yearStarted;
		sem_started = semStarted;
	}
	public Lookup(Boolean enr_on, Boolean log_enabled, Integer year_started, String sem_started){
		super();
		this.enr_on = enr_on;
		this.log_enabled = log_enabled;
		this.year_started = year_started;
		this.sem_started = sem_started;
	}
	public Boolean isEnr_on() {
		return enr_on;
	}
	public void setEnr_on(Boolean enrOn) {
		enr_on = enrOn;
	}
	public Integer getYear_started() {
		return year_started;
	}
	public void setYear_started(Integer yearStarted) {
		year_started = yearStarted;
	}
	public String getSem_started() {
		return sem_started;
	}
	public void setSem_started(String semStarted) {
		sem_started = semStarted;
	}
	public Boolean isEnrollmentEnabled(){
		Boolean result = new LookupQuery().fetchData("getEnr_On").isEnr_on();
		return result;
	}
	public String getTwoSems(){
		return "1st-2nd";
	}
	public String getLog_file_used() {
		return log_file_used;
	}

	public void setLog_file_used(String logFileUsed) {
		log_file_used = logFileUsed;
	}

	public Boolean isLog_enabled() {
		return log_enabled;
	}

	public void setLog_enabled(Boolean logEnabled) {
		log_enabled = logEnabled;
	}
	
	/**Static methods or function**/
	public static Lookup EnrollmentStatus(){
		LookupQuery query = new LookupQuery();
		return query.fetchData("getEnrollmentStatus");
	}
	public static Lookup AllLookupStatus(){
		LookupQuery query = new LookupQuery();
		return query.fetchData("getAllLookupStatus");
	}
	public static String LogFile(){
		LookupQuery query = new LookupQuery();
		return query.fetchDatum("LogFileName");
	}
}
