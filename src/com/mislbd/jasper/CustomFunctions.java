package com.mislbd.jasper;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.jasperreports.engine.JRDefaultScriptlet;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;

public class CustomFunctions extends JRDefaultScriptlet {

    public static void main(String[] args) {// Oracle database configuration

    }
    private String getString(Connection connection, String sql, String columnLabel) {
        try {
            Statement statement = connection.createStatement();

            for(ResultSet resultSet = statement.executeQuery(sql); resultSet.next(); columnLabel = resultSet.getString(columnLabel)) {
            }

            return columnLabel;
        } catch (SQLException var6) {
            var6.printStackTrace();
            return "No data found";
        }
    }



    public String getCommonReportFooter() {
        return "Powered by Ababil, a product of Millennium";
    }

    public static   String amountFormat(double amount){
        
        String  vamount=String.valueOf(amount);
        double scientificValuePositive = Double.parseDouble(vamount);
        DecimalFormat decimalFormat = new DecimalFormat("0.######");
        vamount = decimalFormat.format(scientificValuePositive);
        // System.out.println(vamount);

        String fraction="";
        String integer="";
        String formattedAmount="";
        if(vamount.contains(".")){
            fraction=vamount.split("\\.")[1];

            integer=vamount.split("\\.")[0];
            if(integer.length()<=3){
                formattedAmount=integer + ".00";
            }else{
                formattedAmount=formatIntegerPart(integer)+"." + fraction;
            }

        }else {
            if(vamount.length()<=3){
                formattedAmount=vamount + ".00";
            }else{
                formattedAmount=formatIntegerPart(vamount) + ".00";
            }

        }
        return  formattedAmount;
    }

    public  static String formatIntegerPart(String amount){
        String formattedAmount="";

        formattedAmount =amount.substring(amount.length()-3,amount.length());

        for (int i=amount.length()-3 ; i>0; i=i-2 ){
            if(i>=2){
                formattedAmount= amount.substring(i-2,i) +"," + formattedAmount;
            }if(i<2){
                formattedAmount= amount.substring(i-1,i) +"," + formattedAmount;
            }
        }
        return formattedAmount;
    }

    private static final String SQL_BRANCH_NAME =
            "SELECT b.NAME FROM ORG_OWN_BRANCHES b WHERE b.BRANCH_ID= ?";
    private static final String SQL_BANK_NAME =
            "SELECT BK.NAME FROM ORG_OWN_BRANCHES B \n" +
                    "        JOIN ORG_BANK BK ON BK.BANK_ID = B.BANK_ID WHERE B.BRANCH_ID = ?";
    private static final String SQL_BRANCH_ADDRESS =
            "SELECT B.ADDRESS_LINE FROM ORG_OWN_BRANCHES b WHERE b.BRANCH_ID = ?";

    private static final String SQL_GET_HEAD_OFFICE_BRANCH_ID =
            "SELECT branch_id FROM ORG_OWN_BRANCHES b WHERE B.IS_HEAD_OFFICE=1";
    private static final String SQL_GET_CURRENT_DATE = " select get_current_date current_date from dual";


    public static Integer getHeadOfficeBranchId(Connection reportConnection) throws SQLException {

        try (PreparedStatement statement = reportConnection.prepareStatement(SQL_GET_HEAD_OFFICE_BRANCH_ID)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) : null;
            }
        }
    }

    public static String getCurrentDate(Connection reportConnection){
        try (PreparedStatement statement = reportConnection.prepareStatement(SQL_GET_CURRENT_DATE)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getString(1) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static String getBranchName(Connection reportConnection,Integer branchId)
            throws SQLException {
        if (branchId == null) {
            Integer headOfficeBranchId = getHeadOfficeBranchId(reportConnection);

            if (headOfficeBranchId == null) {
                return null; // Or throw an exception if Head Office ID is mandatory
            }

            branchId = headOfficeBranchId;
        }
        return queryString(SQL_BRANCH_NAME, branchId, reportConnection);
    }

    public static String getBankName(Connection reportConnection,Integer branchId)
            throws SQLException {

        if (branchId == null) {
            Integer headOfficeBranchId = getHeadOfficeBranchId(reportConnection);
            if (headOfficeBranchId == null) {
                return "No Data Found"; // Or throw an exception if Head Office ID is mandatory
            }
            branchId = headOfficeBranchId;
        }
        return queryString(SQL_BANK_NAME, branchId, reportConnection);
    }

    public String getBankNameOld(Connection connection, Integer pbranchid) {
        if (pbranchid == null) {
            return this.getHoBankName(connection);
        } else {
            String sql = "SELECT BDESCRIPTION FROM BRANCH WHERE BID = " + pbranchid;
            return this.getString(connection, sql, "BDESCRIPTION");
        }
    }

    public String getHoBankName(Connection connection) {
        String sql = "SELECT BDESCRIPTION FROM BRANCH WHERE BISHEADOFFICE=1 ";
        return this.getString(connection, sql, "BDESCRIPTION");
    }


    public static String getBranchAddress(Connection reportConnection,Integer branchId )
            throws SQLException {
        if (branchId == null) {
            Integer headOfficeBranchId = getHeadOfficeBranchId(reportConnection);

            if (headOfficeBranchId == null) {
                return "No Data Found"; // Or throw an exception if Head Office ID is mandatory
            }

            branchId = headOfficeBranchId;
        }

        return queryString(SQL_BRANCH_ADDRESS, branchId, reportConnection);
    }
    public static String getApplicationDateAndPrintDateWithUser(
            String token, Connection reportConnection, Boolean isSingleLine) {

        try {
            // Application date from database
            String applicationDate = getCurrentDate(reportConnection);
            applicationDate = new SimpleDateFormat("dd-MM-yyyy")
                    .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(applicationDate));

            // Username from token
            String userName = getLoginUserName(token);

            // Current system date (Print Date)
            String printDate = new SimpleDateFormat("dd-MM-yyyy hh:mm a")
                    .format(new java.util.Date());
            if (Boolean.TRUE.equals(isSingleLine)) {
                return "Print Date : " + printDate
                        + "   Application Date : " + applicationDate
                        + "   User Name : " + (userName != null ? userName : "");
            } else {
                return "Print Date : " + printDate
                        + "\nApplication Date : " + applicationDate
                        + "\nUser Name : " + (userName != null ? userName : "");
            }

        } catch (Exception e) {
            return "Print Date: "
                    + new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date())
                    + "\nApplication Date: "
                    + "\nUser Name: ";
        }
    }

    public static String getLoginUserName(String token) {

        try {
            if (token == null || token.trim().isEmpty()) {
                return null;
            }

            // Remove Bearer prefix
            if (token.startsWith("Bearer ")) {
                token = token.substring(7).trim();
            }

            // Check standard JWT format
            String[] parts = token.split("\\.");

            if (parts.length == 3) {

                // Decode JWT payload
                byte[] decodedBytes = Base64.getUrlDecoder().decode(parts[1]);

                String payload = new String(decodedBytes, StandardCharsets.UTF_8);

                JsonNode jsonNode = new ObjectMapper().readTree(payload);

                if (jsonNode.hasNonNull("sub")) {
                    return jsonNode.get("sub").asText();
                }

                return null;
            }

            // Handle Base64 encoded username:password
            byte[] decodedBytes = Base64.getDecoder().decode(token);

            String decodedToken = new String(decodedBytes, StandardCharsets.UTF_8);

            int colonPosition = decodedToken.indexOf(':');

            if (colonPosition > 0) {
                return decodedToken.substring(0, colonPosition);
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }

    private static String queryString(String sql, Integer branchId, Connection connection)
            throws SQLException {
        if (branchId == null || connection == null) {
            return "No Data Found";
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, branchId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getString(1) : null;
            }
        }
    }




}
