package com.astro.constant;

public enum WorkflowName {

    INDENT("IndentWorkflow", "INDENT WORKFLOW");

    private final String key;
    private final String value;

    WorkflowName(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }
}
