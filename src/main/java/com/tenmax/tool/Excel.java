//package com.tenmax.tool;
//
//
//import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.text.SimpleDateFormat;
//
//public class Excel {
//
//    public static void main(String[] args) {
//        String path = "F:\\1.xls";
//        File file = new File(path);
//
//        InputStream in = null;
//        Workbook workbook = null;
//        FormulaEvaluator formulaEvaluator = null;
//        try {
//            in = new FileInputStream(file);
//
//            if (file.getName().endsWith("xlsx")) {
//                workbook = new XSSFWorkbook(in);
//                formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
//            } else {
//                workbook = new HSSFWorkbook(in);
//                formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
//            }
//            Sheet sheet = workbook.getSheetAt(0);
//            for (Row row : sheet) {
//                for (Cell cell : row) {
//                    //结果比较
//                    System.out.println(getCellValue(cell) + " → " + getCellValueFormula(cell, formulaEvaluator));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//    //未处理公式
//    public static String getCellValue(Cell cell) {
//        if (cell == null) {
//            return null;
//        }
//        switch (cell.getCellType()) {
//            case Cell.CELL_TYPE_STRING:
//                return cell.getRichStringCellValue().getString().trim();
//            case Cell.CELL_TYPE_NUMERIC:
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//非线程安全
//                    return sdf.format(cell.getDateCellValue());
//                } else {
//                    return String.valueOf(cell.getNumericCellValue());
//                }
//            case Cell.CELL_TYPE_BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            case Cell.CELL_TYPE_FORMULA:
//                return cell.getCellFormula();
//            default:
//                return null;
//        }
//    }
//
//    //处理公式
//    public static String getCellValueFormula(Cell cell, FormulaEvaluator formulaEvaluator) {
//        if (cell == null || formulaEvaluator == null) {
//            return null;
//        }
//        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
//            cell.setCellType(Cell.CELL_TYPE_NUMERIC); //设置其单元格类型为数字。(设置为字符串无值)重要！！
//            return String.valueOf(formulaEvaluator.evaluate(cell).getNumberValue());
//        }
//        return getCellValue(cell);
//    }
//}
//              <!-- https://mvnrepository.com/artifact/org.apache.poi/poi -->
//              <dependency>
//              <groupId>org.apache.poi</groupId>
//              <artifactId>poi</artifactId>
//              <version>3.17</version>
//              </dependency>
//              <!-- https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml -->
//              <dependency>
//              <groupId>org.apache.poi</groupId>
//              <artifactId>poi-ooxml</artifactId>
//              <version>3.17</version>
//              </dependency>