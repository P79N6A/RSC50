package com.shrcn.business.scl.model;


public class Hitem {
	private String version;
	private String revision;
	private String what;
	private String who;
	private String when;
	private String why;
	
	public Hitem(String version, String revision, String what, String who,
			String when, String why) {
		this.version = version;
		this.revision = revision;
		this.what = what;
		this.who = who;
		this.when = when;
		this.why = why;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public String getWhat() {
		return what;
	}
	public void setWhat(String what) {
		this.what = what;
	}
	public String getWho() {
		return who;
	}
	public void setWho(String who) {
		this.who = who;
	}
	public String getWhen() {
		return when;
	}
	public void setWhen(String when) {
		this.when = when;
	}
	public String getWhy() {
		return why;
	}
	public void setWhy(String why) {
		this.why = why;
	}
	
	public String asXML() {
		return "<Hitem version=\"" + version + "\" revision=\"" + revision + //$NON-NLS-1$ //$NON-NLS-2$
				"\" what=\"" + what + "\" who=\""+who+ "\" when=\"" + when + //$NON-NLS-1$ //$NON-NLS-2$
				"\" why=\"" + why + "\"/>"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
