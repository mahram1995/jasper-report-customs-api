package com.jasper.misl;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class BranchUtil {

    // Separate cache for each database connection
    private static final Map<Connection, BranchContext> CACHE =
            new WeakHashMap<>();

    private BranchUtil() {
    }

    private static synchronized BranchContext getContext(
            Connection connection) throws SQLException {

        if (connection == null) {
            throw new SQLException("Report connection cannot be null");
        }

        BranchContext context = CACHE.get(connection);

        if (context == null) {

            // First call: load all branch data
            context = loadAllBranchInformation(connection);

            // Cache only after successful loading
            CACHE.put(connection, context);
        }

        return context;
    }

    // Load branch information from the client's database
    private static BranchContext loadAllBranchInformation(
            Connection connection) throws SQLException {

        Map<Integer, Branch> branchMap = new HashMap<>();

        Integer headOfficeId = null;

        String SQL="SELECT A.BRANCH_ID,\n" +
                "       A.NAME,\n" +
                "       A.ADDRESS_LINE,\n" +
                "       A.IS_HEAD_OFFICE,\n" +
                "       B.NAME BANK_NAME\n" +
                "  FROM ORG_OWN_BRANCHES A JOIN ORG_BANK B ON A.BANK_ID = B.BANK_ID";

        try (PreparedStatement ps = connection.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Integer id = rs.getInt("BRANCH_ID");

                boolean isHeadOffice =
                        rs.getInt("IS_HEAD_OFFICE") == 1;

                Branch info = new Branch(
                        id,
                        rs.getString("BRANCH_NAME"),
                        rs.getString("ADDRESS_LINE"),
                        rs.getString("BANK_NAME"),
                        isHeadOffice
                );

                branchMap.put(id, info);

                if (isHeadOffice) {
                    headOfficeId = id;
                }
            }
        }


        return new BranchContext(branchMap, headOfficeId);
    }

    public static Integer getHeadOfficeBranchId(Connection connection)
            throws SQLException {

        return getContext(connection).getHeadOfficeBranchId();
    }

    public static String getBranchName(
            Integer branchId, Connection connection) throws SQLException {

        return getContext(connection).getBranchName(branchId);
    }

    public static String getBankName(
            Integer branchId, Connection connection) throws SQLException {

        return getContext(connection).getBankName(branchId);
    }

    public static String getBranchAddress(
            Integer branchId, Connection connection) throws SQLException {

        return getContext(connection).getBranchAddress(branchId);
    }
}