package com.advance.activiti.dto;

public class ProcessDealDto {
    private String instanceId;

    /**
     * 当前节点的类型，工作签发人确认、工作许可人确认、作业负责人确认。。。
     */
    private Integer NodeType;

    /**
     * 是否删除，仅工作签发人使用
     */
    private boolean delete;

    /**
     * 处理结果
     */
    private Integer check;

    /**
     * 当前处理人
     */
    private String personId;

    private String others;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Integer getNodeType() {
        return NodeType;
    }

    public void setNodeType(Integer nodeType) {
        NodeType = nodeType;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public Integer getCheck() {
        return check;
    }

    public void setCheck(Integer check) {
        this.check = check;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }
}
