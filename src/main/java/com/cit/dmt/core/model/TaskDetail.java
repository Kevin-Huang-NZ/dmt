package com.cit.dmt.core.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

public class TaskDetail {

    @ExcelIgnore
    private Long id;

    @ExcelIgnore
    private Long projectId;

    @ExcelIgnore
    private Long taskId;

    @ExcelProperty(index = 0)
    private String original;

    @ExcelProperty(index = 1)
    private String country;

    @ExcelProperty(index = 2)
    private String language;

    @ExcelProperty(index = 3)
    private String gec;

    @ExcelProperty(index = 4)
    private String memo;

    @ExcelIgnore
    private String romanStatus;

    @ExcelIgnore
    private String roman;

    @ExcelIgnore
    private String transStatus;

    @ExcelIgnore
    private String transliteration;

    @ExcelIgnore
    private String freeTranslation;

    @ExcelIgnore
    private String checkStatus;

    @ExcelIgnore
    private String transResult;

    
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

    
    public String getOriginal() {
        return original;
    }

    
    public void setOriginal(String original) {
        this.original = original == null ? null : original.trim();
    }

    
    public String getCountry() {
        return country;
    }

    
    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    
    public String getLanguage() {
        return language;
    }

    
    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    
    public String getGec() {
        return gec;
    }

    
    public void setGec(String gec) {
        this.gec = gec == null ? null : gec.trim();
    }

    
    public String getMemo() {
        return memo;
    }

    
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    
    public String getRomanStatus() {
        return romanStatus;
    }

    
    public void setRomanStatus(String romanStatus) {
        this.romanStatus = romanStatus == null ? null : romanStatus.trim();
    }

    
    public String getRoman() {
        return roman;
    }

    
    public void setRoman(String roman) {
        this.roman = roman == null ? null : roman.trim();
    }

    
    public String getTransStatus() {
        return transStatus;
    }

    
    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus == null ? null : transStatus.trim();
    }

    
    public String getTransliteration() {
        return transliteration;
    }

    
    public void setTransliteration(String transliteration) {
        this.transliteration = transliteration == null ? null : transliteration.trim();
    }

    
    public String getFreeTranslation() {
        return freeTranslation;
    }

    
    public void setFreeTranslation(String freeTranslation) {
        this.freeTranslation = freeTranslation == null ? null : freeTranslation.trim();
    }

    
    public String getCheckStatus() {
        return checkStatus;
    }

    
    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus == null ? null : checkStatus.trim();
    }

    
    public String getTransResult() {
        return transResult;
    }

    
    public void setTransResult(String transResult) {
        this.transResult = transResult == null ? null : transResult.trim();
    }
}
