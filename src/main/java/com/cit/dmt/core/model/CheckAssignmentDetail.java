package com.cit.dmt.core.model;

public class CheckAssignmentDetail {
    
    private Long id;

    
    private Long roundId;

    
    private Long projectId;

    
    private Long taskId;

    
    private Long userId;

    
    private Long taskDetailId;

    
    private String checkStatus;

    
    private String checkResult;

    
    private String checkMemo;

    
    private String adoptionStatus;

    
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

    
    public Long getTaskDetailId() {
        return taskDetailId;
    }

    
    public void setTaskDetailId(Long taskDetailId) {
        this.taskDetailId = taskDetailId;
    }

    
    public String getCheckStatus() {
        return checkStatus;
    }

    
    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus == null ? null : checkStatus.trim();
    }

    
    public String getCheckResult() {
        return checkResult;
    }

    
    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult == null ? null : checkResult.trim();
    }

    
    public String getCheckMemo() {
        return checkMemo;
    }

    
    public void setCheckMemo(String checkMemo) {
        this.checkMemo = checkMemo == null ? null : checkMemo.trim();
    }

    
    public String getAdoptionStatus() {
        return adoptionStatus;
    }

    
    public void setAdoptionStatus(String adoptionStatus) {
        this.adoptionStatus = adoptionStatus == null ? null : adoptionStatus.trim();
    }
}
