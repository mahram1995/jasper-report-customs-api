package com.jasper.misl;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BranchContext {

    private final Map<Integer, Branch> branchMap;
    private final Integer headOfficeBranchId;

    public BranchContext(
            Map<Integer, Branch> branchMap,
            Integer headOfficeBranchId) {

        this.branchMap = Collections.unmodifiableMap(
                new HashMap<>(branchMap));

        this.headOfficeBranchId = headOfficeBranchId;
    }

    private Branch getBranch(Integer branchId) {

        if (branchId == null) {
            branchId = headOfficeBranchId;
        }

        return branchMap.get(branchId);
    }

    public Integer getHeadOfficeBranchId() {
        return headOfficeBranchId;
    }

    public String getBranchName(Integer branchId) {

        Branch branch = getBranch(branchId);

        return branch != null ? branch.getBranchName() : null;
    }

    public String getBranchAddress(Integer branchId) {

        Branch branch = getBranch(branchId);

        return branch != null ? branch.getBranchAddress() : null;
    }

    public String getBankName(Integer branchId) {

        Branch branch = getBranch(branchId);

        return branch != null ? branch.getBankName() : null;
    }
}
