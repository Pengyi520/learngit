package com.qihoo360.replugin.sample.service.response;

import com.py.library.network.base.BaseResponse;

import java.util.List;

/**
 * Created by pengyi on 2017/7/26.
 */

public class BillResponse extends BaseResponse<List<BillResponse.DataModel>> {

    public static class DataModel {
        private String payAmount;// 缴费金额
        private String contractNo;// 合同编号，即账单的缴费编码
        private String billDate;// 账单日
        private String orderNo;// 合同编号，即账单的缴费编码
        private int billType;// 缴费类型
        private String billKey;// 用户在缴费机构编号
        private String orderStatus;
        private boolean isLast = false;
        private int position = -1;
        private String groupName;
        private String groupId;
        private String tradeOrderNo;
        private String projectCode;
        private long updateTime;
        private String orderStatusDesc;
        private String puid;// 大平台用户id

        public String getPuid() {
            return puid;
        }

        public void setPuid(String puid) {
            this.puid = puid;
        }

        public String getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(String payAmount) {
            this.payAmount = payAmount;
        }

        public String getContractNo() {
            return contractNo;
        }

        public void setContractNo(String contractNo) {
            this.contractNo = contractNo;
        }

        public String getBillDate() {
            return billDate;
        }

        public void setBillDate(String billDate) {
            this.billDate = billDate;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public int getBillType() {
            return billType;
        }

        public void setBillType(int billType) {
            this.billType = billType;
        }

        public String getBillKey() {
            return billKey;
        }

        public void setBillKey(String billKey) {
            this.billKey = billKey;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public boolean isLast() {
            return isLast;
        }

        public void setLast(boolean last) {
            isLast = last;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getTradeOrderNo() {
            return tradeOrderNo;
        }

        public void setTradeOrderNo(String tradeOrderNo) {
            this.tradeOrderNo = tradeOrderNo;
        }

        public String getProjectCode() {
            return projectCode;
        }

        public void setProjectCode(String projectCode) {
            this.projectCode = projectCode;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getOrderStatusDesc() {
            return orderStatusDesc;
        }

        public void setOrderStatusDesc(String orderStatusDesc) {
            this.orderStatusDesc = orderStatusDesc;
        }


    }
}
