package com.cit.dmt.core.model;

public class CheckAssignment {
    
    private Long id;

    
    private Long roundId;

    
    private Long projectId;

    
    private Long taskId;

    
    private Long userId;

    
    private Integer amount;

    
    private Integer done;

    
    private String assignStatus;

    
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    
    public Long getRoundId() {
        return roundId;
    }

    
    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    
    public Long getProjectId() {
        return projectId;
    }

    
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    
    public Long getTaskId() {
        return taskId;
    }

    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    
    public Long getUserId() {
        return userId;
    }

    
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    
    public Integer getAmount() {
        return amount;
    }

    
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    
    public Integer getDone() {
        return done;
    }

    
    public void setDone(Integer done) {
        this.done = done;
    }

    
    public String getAssignStatus() {
        return assignStatus;
    }

    
    public void setAssignStatus(String assignStatus) {
        this.assignStatus = assignStatus == null ? null : assignStatus.trim();
    }
}
