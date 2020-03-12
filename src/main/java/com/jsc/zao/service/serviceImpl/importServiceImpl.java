package com.jsc.zao.service.serviceImpl;

import com.jsc.zao.service.ImportService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: WW
 * @Date: 2019/12/11 0011 13:35
 * @Description:
 */
@Service
public class importServiceImpl implements ImportService {
    @Override
    public List getBankListByExcel(InputStream in, String fileName) throws Exception {
        List list = new ArrayList<>();
        //创建一个excel
        Workbook workbook = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType)) {
            workbook = new HSSFWorkbook(in);
        } else if (".xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(in);
        } else {
            throw new Exception("请上传excel文件！");
        }
        if(workbook == null){
            throw new Exception("创建失败");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        for(int i=0;i<workbook.getNumberOfSheets();i++){
            sheet = workbook.getSheetAt(i);
            if(sheet==null){
                continue;
            }
            for(int j=1;j<=sheet.getLastRowNum();j++){
                row = sheet.getRow(j);
                if (row == null ) {
                    continue;
                }
                List<Object> li = new ArrayList<>();
                for (int y =0; y < 6; y++) {
                    cell = row.getCell(y);
                    if(cell==null){
                        li.add("");
                    }else if (cell.getCellTypeEnum()== CellType.NUMERIC){
                        double d = cell.getNumericCellValue();
                        NumberFormat nf = NumberFormat.getInstance();
                        String s = nf.format(d);
                        if (s.indexOf(",") >= 0) {
                        //这种方法对于自动加".0"的数字可直接解决  但如果是科学计数法的数字就转换成了带逗号的，
                        // 例如：12345678912345的科学计数法是1.23457E+13，
                        // 经过这个格式化后就变成了字符串“12,345,678,912,345”，这也并不是想要的结果，所以要将逗号去掉
                            s = s.replace(",", "");
                        }
                            li.add(s);
                        } else {
                            li.add(cell);
                        }
                }
                while(li.size()<6){
                    li.add("");
                }
                list.add(li);
            }
        }
        workbook.close();
        return list;
    }
    /**
     * 判断文件格式
     *
     * @param inStr
     * @param fileName
     * @return
     * @throws Exception
     */
//    public Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
//        Workbook workbook = null;
//        String fileType = fileName.substring(fileName.lastIndexOf("."));
//        if (".xls".equals(fileType)) {
//            workbook = new HSSFWorkbook(inStr);
//        } else if (".xlsx".equals(fileType)) {
//            workbook = new XSSFWorkbook(inStr);
//        } else {
//            throw new Exception("请上传excel文件！");
//        }
//        return workbook;
//    }
}
