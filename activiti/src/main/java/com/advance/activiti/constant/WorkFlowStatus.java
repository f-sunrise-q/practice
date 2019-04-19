package com.advance.activiti.constant;

public enum  WorkFlowStatus {
    /**
     * 草稿
     */
    DRAFT("工作签发人创建作业",1),
    /**
     * 待处理
     */
    TODEAL("待工作许可人确认",2),
    /**
     * 待处理(处理驳回)
     */
    DRAFT_RETURN("待工作许可人确认(被删除)",3),
    /**
     * 待处理(审批驳回)
     */
    TODEAL_RETURN("待工作许可人确认（审批驳回）",4),
    /**
     * 待审批
     */
    TOAPPROVE("待工作负责人确认",5),
    /**
     * 审批驳回
     */
    TOAPPROVE_RETURN("待工作负责人确认（审批驳回）",6),
    /**
     * 二级审批
     */
    TOAPPROVE_TWO("工作负责人签到",7),
    /**
     * 二级审批(审批驳回)
     */
    TOAPPROVE_TWO_RETURN("作业施工中",8),
    /**
     * 顶级审批
     */
    TOAPPROVE_THREE("工作许可人确认结束",9),
    /**
     * 顶级审批
     */
    TOAPPROVE_FOUR("工作许可人确认结束(审批驳回)",10),
    /**
     * 完成
     */
    FINISH("完成",0);

    private String name;
    private Integer index;

    WorkFlowStatus(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public static String getDescriptionByIndex(Integer index){
        WorkFlowStatus[] statuses = WorkFlowStatus.values();
        for(WorkFlowStatus status : statuses){
            if(status.getIndex().equals(index)){
                return status.getName();
            }
        }
        return "";
    }
}
