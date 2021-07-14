package com.cit.dmt.core.model;

public class CheckRoundAssignmentSts {
    
    private Integer total;
    private Integer unassigned;
    private Integer assigned;
    private Integer waitingCheck;
    private Integer checked;
    private Integer waitingAdoption;
    private Integer adopted;
    private Integer rejected;
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getUnassigned() {
		return unassigned;
	}
	public void setUnassigned(Integer unassigned) {
		this.unassigned = unassigned;
	}
	public Integer getAssigned() {
		return assigned;
	}
	public void setAssigned(Integer assigned) {
		this.assigned = assigned;
	}
	public Integer getChecked() {
		return checked;
	}
	public void setChecked(Integer checked) {
		this.checked = checked;
	}
	public Integer getWaitingAdoption() {
		return waitingAdoption;
	}
	public void setWaitingAdoption(Integer waitingAdoption) {
		this.waitingAdoption = waitingAdoption;
	}
	public Integer getAdopted() {
		return adopted;
	}
	public void setAdopted(Integer adopted) {
		this.adopted = adopted;
	}
	public Integer getRejected() {
		return rejected;
	}
	public void setRejected(Integer rejected) {
		this.rejected = rejected;
	}
	public Integer getWaitingCheck() {
		return waitingCheck;
	}
	public void setWaitingCheck(Integer waitingCheck) {
		this.waitingCheck = waitingCheck;
	}
    
}
