package com.jasper.misl;


import net.sf.jasperreports.engine.JRDefaultScriptlet;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class CommonMethod   extends JRDefaultScriptlet {

    public static void main(String[] args) {// Oracle database configuration
        String url = "jdbc:oracle:thin:@//192.168.1.190:1521/devdb";
        String username = "AIBL030426";
        String password = "a";

        try (Connection connection =
                     DriverManager.getConnection(url, username, password)) {

//            System.out.println("Database connected successfully!");
//
//            // Test 1: Get Head Office Branch ID
//            Integer headOfficeId =
//                    getHeadOfficeBranchId( connection);
//
//            System.out.println("Head Office Branch ID: " + headOfficeId);
//
//            // Test 2: Get Branch Name using Head Office
//            String headOfficeName =
//                    getBranchName(null, connection);
//
//            System.out.println("Head Office Branch Name: " + headOfficeName);
//
//            // Test 3: Get Bank Name using Head Office
//            String bankName =
//                    getBankName(null, connection);
//
//            System.out.println("Bank Name: " + bankName);
//
//            // Test 4: Get Branch Address using Head Office
//            String address =
//                    getBranchAddress(null, connection);
//
//            System.out.println("Branch Address: " + address);
//
//            // Test 5: Test with a specific branch ID
//            Integer branchId = 801;
//
//            String branchName =
//                    getBranchName(branchId, connection);
//
//            System.out.println("Specific Branch Name: " + branchName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(amountFormat(1833300000));
    }
    private String getString(Connection connection, String sql, String columnLabel) {
        try {
            Statement statement = connection.createStatement();

            for(ResultSet resultSet = statement.executeQuery(sql); resultSet.next(); columnLabel = resultSet.getString(columnLabel)) {
            }

            return columnLabel;
        } catch (SQLException var6) {
            return "No data found";
        }
    }



    public  String CommonFooter(){
        return "Powered by Ababil, A product of Millennium";
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

//    private static final String SQL_BRANCH_NAME =
//            "SELECT b.NAME FROM ORG_OWN_BRANCHES b WHERE b.BRANCH_ID= ?";
//    private static final String SQL_BANK_NAME =
//            "SELECT BK.NAME FROM ORG_OWN_BRANCHES B \n" +
//                    "        JOIN ORG_BANK BK ON BK.BANK_ID = B.BANK_ID WHERE B.BRANCH_ID = ?";
//    private static final String SQL_BRANCH_ADDRESS =
//            "SELECT B.ADDRESS_LINE FROM ORG_OWN_BRANCHES b WHERE b.BRANCH_ID = ?";
//
//    private static final String SQL_GET_HEAD_OFFICE_BRANCH_ID =
//            "SELECT branch_id FROM ORG_OWN_BRANCHES b WHERE B.IS_HEAD_OFFICE=1";
//
//
//    public static Integer getHeadOfficeBranchId(Connection reportConnection) throws SQLException {
//
//        try (PreparedStatement statement = reportConnection.prepareStatement(SQL_GET_HEAD_OFFICE_BRANCH_ID)) {
//
//            try (ResultSet resultSet = statement.executeQuery()) {
//                return resultSet.next() ? resultSet.getInt(1) : null;
//            }
//        }
//    }
//
//
//    public static String getBranchName(Integer branchId, Connection reportConnection)
//            throws SQLException {
//        if (branchId == null) {
//            Integer headOfficeBranchId = getHeadOfficeBranchId(reportConnection);
//
//            if (headOfficeBranchId == null) {
//                return null; // Or throw an exception if Head Office ID is mandatory
//            }
//
//            branchId = headOfficeBranchId;
//        }
//        return queryString(SQL_BRANCH_NAME, branchId, reportConnection);
//    }
//
//    public static String getBankName(Integer branchId, Connection reportConnection)
//            throws SQLException {
//
//        if (branchId == null) {
//            Integer headOfficeBranchId = getHeadOfficeBranchId(reportConnection);
//
//            if (headOfficeBranchId == null) {
//                return null; // Or throw an exception if Head Office ID is mandatory
//            }
//
//            branchId = headOfficeBranchId;
//        }
//
//        return queryString(SQL_BANK_NAME, branchId, reportConnection);
//    }
//
//    public static String getBranchAddress(Integer branchId, Connection reportConnection)
//            throws SQLException {
//        if (branchId == null) {
//            Integer headOfficeBranchId = getHeadOfficeBranchId(reportConnection);
//
//            if (headOfficeBranchId == null) {
//                return null; // Or throw an exception if Head Office ID is mandatory
//            }
//
//            branchId = headOfficeBranchId;
//        }
//
//        return queryString(SQL_BRANCH_ADDRESS, branchId, reportConnection);
//    }
//
//    private static String queryString(String sql, Integer branchId, Connection connection)
//            throws SQLException {
//        if (branchId == null || connection == null) {
//            return null;
//        }
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setInt(1, branchId);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                return resultSet.next() ? resultSet.getString(1) : null;
//            }
//        }
//    }




}
