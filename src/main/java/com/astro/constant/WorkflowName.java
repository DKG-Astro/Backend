package com.astro.constant;

public enum WorkflowName {

    INDENT("IndentWorkflow", "INDENT WORKFLOW"),
    CP("ContingencyPurchaseWorkflow", "CONTINGENCY PURCHASE WORKFLOW"),
    PO("PO/SO/WO-Workflow", "PO/SO/WO WORKFLOW"),
    TENDER("TenderWorkflow", "TENDER WORKFLOW");

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
