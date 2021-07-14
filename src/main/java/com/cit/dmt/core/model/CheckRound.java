package com.cit.dmt.core.model;

public class CheckRound {
    
    private Long id;

    
    private Long projectId;

    
    private Long taskId;

    
    private String roundName;

    
    private String startDate;

    
    private String dueDate;

    
    private String status;

    
    private String memo;

    
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
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

    
    public String getRoundName() {
        return roundName;
    }

    
    public void setRoundName(String roundName) {
        this.roundName = roundName == null ? null : roundName.trim();
    }

    
    public String getStartDate() {
        return startDate;
    }

    
    public void setStartDate(String startDate) {
        this.startDate = startDate == null ? null : startDate.trim();
    }

    
    public String getDueDate() {
        return dueDate;
    }

    
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate == null ? null : dueDate.trim();
    }

    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    
    public String getMemo() {
        return memo;
    }

    
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}
