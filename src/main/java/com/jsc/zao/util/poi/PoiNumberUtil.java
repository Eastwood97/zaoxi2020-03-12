package com.jsc.zao.util.poi;

import com.jsc.zao.bean.Number;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;



public class PoiNumberUtil {
    public static ResponseEntity<byte[]> exportNumberExcel(List<Number> numbers) {
        HttpHeaders headers = null;
        ByteArrayOutputStream baos = null;
        try {
            //1.创建Excel文档
            HSSFWorkbook workbook = new HSSFWorkbook();
            //2.创建文档摘要
            workbook.createInformationProperties();
            //3.获取文档信息，并配置
            DocumentSummaryInformation dsi = workbook.getDocumentSummaryInformation();
            //3.1文档类别
            dsi.setCategory("取号信息");
            //3.2设置文档管理员
            dsi.setManager("admin");
            //3.3设置组织机构
            dsi.setCompany("枣西译码");
            //4.获取摘要信息并配置
            SummaryInformation si = workbook.getSummaryInformation();
            //4.1设置文档主题
            si.setSubject("取号信息表");
            //4.2.设置文档标题
            si.setTitle("取号信息");
            //4.3 设置文档作者
            si.setAuthor("admin");
            //4.4设置文档备注
            si.setComments("备注信息暂无");
            //创建Excel表单
            HSSFSheet sheet = workbook.createSheet("取号信息表");
            //创建日期显示格式
            HSSFCellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
            //创建标题的显示样式
            HSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            //定义列的宽度
            sheet.setColumnWidth(0, 5 * 256);
            sheet.setColumnWidth(1, 12 * 256);
            sheet.setColumnWidth(2, 10 * 256);
            sheet.setColumnWidth(3, 5 * 256);
            sheet.setColumnWidth(4, 12 * 256);
            sheet.setColumnWidth(5, 20 * 256);
            sheet.setColumnWidth(6, 10 * 256);
            sheet.setColumnWidth(7, 10 * 256);
            sheet.setColumnWidth(8, 16 * 256);
            sheet.setColumnWidth(9, 20 * 256);
            sheet.setColumnWidth(10, 20 * 256);
            sheet.setColumnWidth(11, 20 * 256);
            sheet.setColumnWidth(12, 16 * 256);
            sheet.setColumnWidth(13, 14 * 256);
            sheet.setColumnWidth(14, 14 * 256);

            //5.设置表头
            HSSFRow headerRow = sheet.createRow(0);
            HSSFCell cell0 = headerRow.createCell(0);
            cell0.setCellValue("ID");
            cell0.setCellStyle(headerStyle);
            HSSFCell cell1 = headerRow.createCell(1);
            cell1.setCellValue("PLMN");
            cell1.setCellStyle(headerStyle);
            HSSFCell cell2 = headerRow.createCell(2);
            cell2.setCellValue("IMSI");
            cell2.setCellStyle(headerStyle);
            HSSFCell cell3 = headerRow.createCell(3);
            cell3.setCellValue("IMEI");
            cell3.setCellStyle(headerStyle);
            HSSFCell cell4 = headerRow.createCell(4);
            cell4.setCellValue("电话号码");
            cell4.setCellStyle(headerStyle);
            HSSFCell cell5 = headerRow.createCell(5);
            cell5.setCellValue("号码性质");
            cell5.setCellStyle(headerStyle);
            HSSFCell cell6 = headerRow.createCell(6);
            cell6.setCellValue("归属地");
            cell6.setCellStyle(headerStyle);
            HSSFCell cell7 = headerRow.createCell(7);
            cell7.setCellValue("LAT");
            cell7.setCellStyle(headerStyle);
            HSSFCell cell8 = headerRow.createCell(8);
            cell8.setCellValue("LNG");
            cell8.setCellStyle(headerStyle);
            HSSFCell cell9 = headerRow.createCell(9);
            cell9.setCellValue("捕获时间");
            cell9.setCellStyle(headerStyle);
            HSSFCell cell10 = headerRow.createCell(10);
            cell10.setCellValue("捕获地");
            cell10.setCellStyle(headerStyle);
            HSSFCell cell11 = headerRow.createCell(11);
            cell11.setCellValue("描述");
            cell11.setCellStyle(headerStyle);
            HSSFCell cell12 = headerRow.createCell(12);
            cell12.setCellValue("记录名");
            cell12.setCellStyle(headerStyle);
            HSSFCell cell13 = headerRow.createCell(13);
            cell13.setCellValue("案件名");
            cell13.setCellStyle(headerStyle);
            HSSFCell cell14 = headerRow.createCell(14);
            cell14.setCellValue("前端设备ID");
            cell14.setCellStyle(headerStyle);

            //6.装数据
            for (int i = 0; i < numbers.size(); i++) {
                HSSFRow row = sheet.createRow(i + 1);
                Number number = numbers.get(i);
                row.createCell(0).setCellValue(number.getId());
                row.createCell(1).setCellValue(number.getPlmn());
                row.createCell(2).setCellValue(number.getImsi());
                row.createCell(3).setCellValue(number.getImei());
                row.createCell(4).setCellValue(number.getIsdn());
                row.createCell(5).setCellValue(number.getIsTarget());
                row.createCell(6).setCellValue(number.getArea());
                row.createCell(7).setCellValue(number.getLat());
                row.createCell(8).setCellValue(number.getLng());
                HSSFCell catchTimeCell=row.createCell(9);
                catchTimeCell.setCellValue(number.getCatchTime());
                catchTimeCell.setCellStyle(dateCellStyle);
                row.createCell(10).setCellValue(number.getCatchArea());
                row.createCell(11).setCellValue(number.getDesc());
                row.createCell(12).setCellValue(number.getRecordName());
                row.createCell(13).setCellValue(number.getCaseName());
                row.createCell(14).setCellValue(number.getFrtDevId());

            }
            headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment",
                    new String("取号表.xls".getBytes("UTF-8"), "iso-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            baos = new ByteArrayOutputStream();
            workbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.CREATED);
    }


}
