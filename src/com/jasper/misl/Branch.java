package com.jasper.misl;


public class Branch {

    private Integer branchId;
    private String branchName;
    private String branchAddress;
    private String bankName;
    private boolean headOffice;

    public Branch(Integer branchId, String branchName,
                      String branchAddress, String bankName,
                      boolean headOffice) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.branchAddress = branchAddress;
        this.bankName = bankName;
        this.headOffice = headOffice;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public String getBankName() {
        return bankName;
    }

    public boolean isHeadOffice() {
        return headOffice;
    }
}