package jmxattacks;

import jmxutils.Action;

public abstract class Attack extends Action{

	private String attack_description;
	private String attack_name;
	
	public String getAttack_description() {
		return this.attack_description;
	}
	public void setAttack_description(String attack_description) {
		this.attack_description = attack_description;
	}
	public String getAttack_name() {
		return attack_name;
	}
	public void setAttack_name(String attack_name) {
		this.attack_name = attack_name;
	}
	
	public abstract void prepareExploit();
	public abstract void prepareExploit(String args);
	
}
