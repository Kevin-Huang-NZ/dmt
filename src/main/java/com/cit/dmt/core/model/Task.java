package com.cit.dmt.core.model;

public class Task {
    
    private Long id;

    
    private Long projectId;

    
    private String taskName;

    
    private String languageCode;

    
    private String needRoman;

    
    private String createDate;

    
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

    
    public String getTaskName() {
        return taskName;
    }

    
    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    
    public String getLanguageCode() {
        return languageCode;
    }

    
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode == null ? null : languageCode.trim();
    }

    
    public String getNeedRoman() {
        return needRoman;
    }

    
    public void setNeedRoman(String needRoman) {
        this.needRoman = needRoman == null ? null : needRoman.trim();
    }

    
    public String getCreateDate() {
        return createDate;
    }

    
    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
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
