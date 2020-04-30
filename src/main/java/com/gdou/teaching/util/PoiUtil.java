package com.gdou.teaching.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.util
 * @ClassName: PoiUtil
 * @Author: carrymaniac
 * @Description: Poi-util
 * @Date: 2019/12/21 7:06 下午
 * @Version:
 */
@Component
public class PoiUtil {
    public Workbook createSheet(String sheetName,List<String> colunNames,List<List<String>> data){
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        //设置列名
        Row head = sheet.createRow(0);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        for(int i = 0,size=colunNames.size();i<size;i++){
            Cell cell = head.createCell(i);
            cell.setCellValue(colunNames.get(i));
            cell.setCellStyle(cellStyle);
        }
        //设置列值
        if(data!=null&&!data.isEmpty()){
            for(int i = 0,size=data.size();i<size;i++){
                Row row = sheet.createRow(i+1);
                List<String> rowList = data.get(i);
                for(int j = 0,rsize=rowList.size();j<rsize;j++){
                    Cell cell = row.createCell(j);
                    cell.setCellValue(rowList.get(j));
                }
            }
        }
        return workbook;
    }
}
