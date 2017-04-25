package org.base.component.utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Blank;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelTool {

    private WritableWorkbook wwb = null;
    private WritableSheet ws = null;
    private FileOutputStream fos = null;
    private WritableCellFormat wcf = null;
    private WritableFont wf_blk = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
            UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
    private WritableFont wf_red = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
            UnderlineStyle.NO_UNDERLINE, Colour.RED);

    private InputStream fis = null;
    private Workbook wb = null;

    /**
     * 在指定路径下新建一个Excel文件。
     * 
     * @parma String FileName 文件名称+文件路径
     * @return boolean
     */
    public boolean createExcel(String FileName) throws Exception {
        boolean result = false;
        try {
            this.fos = new FileOutputStream(FileName);
            this.wwb = Workbook.createWorkbook(this.fos);
            result = true;

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 设置当前sheet工作薄的名称及sheet号
     * 
     * @parma int SheetIdx(sheet号) String SheetName(工作薄的名称)
     * @return boolean
     */
    public boolean setCurSheet(int SheetIdx, String SheetName) {
        boolean result = false;
        try {
            this.ws = this.wwb.createSheet(SheetName, SheetIdx);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 向cell单元格中写入String型数据并可以设置单元格背景
     * 
     * @parma int col(列号) int row(行号) txt 内容
     * @return boolean
     */
    public boolean setCellText(int col, int row, String txt, String bgcolor) {
        boolean result = false;
        try {
            this.wf_blk = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            this.setCellBgcolor(bgcolor);

            Label label = new Label(col, row, txt, this.wcf);
            this.ws.addCell(label);

            result = true;

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    public boolean setCellText(int col, int row, String txt, String bgcolor, String alignment) {
        boolean result = false;
        try {
            this.wf_blk = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            this.setCellBgcolor(bgcolor);
            this.setAlignment(alignment);
            this.setVerticalAlignment("centre");

            Label label = new Label(col, row, txt, this.wcf);
            this.ws.addCell(label);

            result = true;

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    public boolean setCellTextColor(int col, int row, String txt) {
        boolean result = false;
        try {

            this.wcf = new WritableCellFormat(this.wf_blk);
            this.setAlignment("centre");
            this.setVerticalAlignment("centre");
            this.wcf.setWrap(true);

            Label label = new Label(col, row, txt, this.wcf);
            this.ws.addCell(label);

            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 设置带颜色有效性验证
     * 
     * @param col
     * @param row
     * @param txt
     * @param colorValidate
     *            1表示红色，其他数字为黑色
     * @return
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-8-16上午11:57:45
     */
    public boolean setCellTextColor(int col, int row, String txt, int colorValidate) {
        boolean result = false;
        try {

            if (colorValidate == 1) {
                this.wcf = new WritableCellFormat(this.wf_red);
            } else {
                this.wcf = new WritableCellFormat(this.wf_blk);
            }

            this.setAlignment("centre");
            this.setVerticalAlignment("centre");
            this.wcf.setWrap(true);

            Label label = new Label(col, row, txt, this.wcf);
            this.ws.addCell(label);

            result = true;

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    private void setAlignment(String alignment) throws WriteException {
        if ("LEFT".equalsIgnoreCase(alignment)) {
            this.wcf.setAlignment(Alignment.LEFT);
        } else if ("RIGHT".equalsIgnoreCase(alignment)) {
            this.wcf.setAlignment(Alignment.RIGHT);
        } else {
            this.wcf.setAlignment(Alignment.CENTRE);
        }
    }

    private void setVerticalAlignment(String verticalAlignment) throws WriteException {
        if ("TOP".equalsIgnoreCase(verticalAlignment)) {
            this.wcf.setVerticalAlignment(VerticalAlignment.TOP);
        } else if ("BOTTOM".equalsIgnoreCase(verticalAlignment)) {
            this.wcf.setVerticalAlignment(VerticalAlignment.BOTTOM);
        } else {
            this.wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
        }
    }

    /**
     * 设置单元格，文字、颜色、水平对齐方式。
     * 
     * @param col
     *            列号
     * @param row
     *            行号
     * @param txt
     *            文本内容
     * @param alignment
     *            对齐方式：centre 居中，left 居左，right 居右
     * @return 成功为true，失败为false
     * @author: <a href="mailto:justin.t.wang@163.com">王耀军(justin.t.wang)</a>  
     * @date: 2013-8-16下午03:02:24
     */
    public boolean setCellTextColor(int col, int row, String txt, String alignment) {
        boolean result = false;
        try {
            this.wcf = new WritableCellFormat(this.wf_blk);
            this.setAlignment(alignment);
            this.setVerticalAlignment("centre");
            this.wcf.setWrap(true);

            Label label = new Label(col, row, txt, this.wcf);
            this.ws.addCell(label);

            result = true;

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 向cell单元格中写入Date型数据并可以设置单元格背景
     * 
     * @parma int col(列号) int row(行号) txt 内容
     * @return boolean
     */
    public boolean setCellDate(int col, int row, Date txt, String bgcolor) {
        boolean result = false;
        try {
            this.wf_blk = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            this.setCellBgcolor(bgcolor);

            DateTime label = new DateTime(col, row, txt, this.wcf);
            this.ws.addCell(label);
            result = true;

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 向cell单元格中写入数字：double型数字并可以设置单元格背景
     * 
     * @parma int col(列号) int row(行号) txt 内容
     * @return boolean
     */
    public boolean setCellNumber(int col, int row, double txt, String bgcolor) {
        boolean result = false;
        try {
            this.wf_blk = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
            this.setCellBgcolor(bgcolor);

            Number label = new Number(col, row, txt, this.wcf);
            this.ws.addCell(label);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;

    }

    /**
     * 向cell单元格中写入String型数据并可以设置字体样式
     * 
     * @parma int col(列号) int row(行号) txt 内容 int format 字体样式编号 int fontSize 字体大小
     * @return boolean
     */
    public boolean setCellText(int col, int row, String txt, int format, int fontSize) {
        boolean result = false;
        try {
            this.setCellFont(format, fontSize);
            this.wcf = new WritableCellFormat(this.wf_blk);
            this.setAlignment("centre");
            this.setVerticalAlignment("centre");

            Label label = new Label(col, row, txt, this.wcf);
            this.ws.addCell(label);

            result = true;

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 向cell单元格中写入Date型数据并可以设置字体样式
     * 
     * @parma int col(列号) int row(行号) txt 内容 int format 字体样式编号 int fontSize 字体大小
     * @return boolean
     */
    public boolean setCellDate(int col, int row, Date txt, int format, int fontSize) {
        boolean result = false;
        try {
            this.setCellFont(format, fontSize);
            this.wcf = new WritableCellFormat(this.wf_blk);
            this.setAlignment("centre");
            this.setVerticalAlignment("centre");
            DateTime label = new DateTime(col, row, txt, this.wcf);
            this.ws.addCell(label);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 向cell单元格中写入数字：double型数字并可以设置字体样式
     * 
     * @parma int col(列号) int row(行号) txt 内容 int format 字体样式编号 int fontSize 字体大小
     * @return boolean
     */
    public boolean setCellNumber(int col, int row, double txt, int format, int fontSize) {
        boolean result = false;
        try {
            this.setCellFont(format, fontSize);
            this.wcf = new WritableCellFormat(this.wf_blk);
            this.setAlignment("centre");
            this.setVerticalAlignment("centre");

            Number label = new Number(col, row, txt, this.wcf);
            this.ws.addCell(label);
            result = true;

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 向cell单元格中写入String型数据
     * 
     * @parma int col(列号) int row(行号) txt 内容
     * @return boolean
     */
    public boolean setCellText(int col, int row, String txt) {
        boolean result = false;
        try {
            this.wcf = new WritableCellFormat(this.wf_blk);
            this.wcf.setAlignment(Alignment.CENTRE);
            this.wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
            this.wcf.setWrap(true);

            Label label = new Label(col, row, txt, this.wcf);
            this.ws.addCell(label);
            result = true;
        } catch (Exception e) {
            result = false;
        } finally {
            try {

            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 向cell单元格中写入Date型数据
     * 
     * @parma int col(列号) int row(行号) txt 内容
     * @return boolean
     */
    public boolean setCellDate(int col, int row, Date txt) {
        boolean result = false;
        try {
            DateTime label = new DateTime(col, row, txt);
            this.ws.addCell(label);
            result = true;
        } catch (Exception e) {
            result = false;
        }

        return result;
    }

    /**
     * 向cell单元格中写入数字：double型数字。
     * 
     * @parma int col(列号) int row(行号) txt 内容
     * @return boolean
     */
    public boolean setCellNumber(int row, int col, double txt) {
        boolean result = false;
        try {
            Number label = new Number(col, row, txt);
            this.ws.addCell(label);
            result = true;

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 向cell单元格中写入数字：float型数字。
     * 
     * @parma int col(列号) int row(行号) txt 内容
     * @return boolean
     */
    public boolean setCellNumber(int col, int row, float txt) {
        boolean result = false;
        try {
            Number label = new Number(col, row, txt);
            this.ws.addCell(label);
            result = true;

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将写入的cell的内容保存到Excel文件中.
     * 
     * @parma
     * @return boolean
     */
    public boolean saveExcel() {
        boolean result = false;
        try {
            // 写入Exel工作表
            this.wwb.write();

            // 关闭Excel工作薄对象
            this.wwb.close();

            result = true;

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 打开Excel.
     * 
     * @parma fileName Excel文件名+文件路径（绝对路径）
     * @return boolean
     */
    public boolean openExcel(String fileName) {
        boolean result = false;
        try {
            this.fis = new FileInputStream(fileName);
            this.wb = Workbook.getWorkbook(this.fis);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {

            } catch (Exception e) {

            }

        }
        return result;
    }
    
    public boolean openExcel(byte[] data) {
        boolean result = false;
        try {
            this.fis = new ByteArrayInputStream(data);
            this.wb = Workbook.getWorkbook(this.fis);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {

            } catch (Exception e) {

            }

        }
        return result;
    }

    /**
     * 打开Excel.
     * 
     * @parma fileName Excel流
     * @return boolean
     */
    public boolean openExcel(FileInputStream stream) {
        boolean result = false;
        try {
            this.wb = Workbook.getWorkbook(stream);
            result = true;

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 关闭打开的Excel. @
     * 
     * @return boolean
     */
    public boolean closeExcel() {
        boolean result = false;
        try {
            this.wb.close();
            this.fis.close();
            result = true;
        } catch (Exception e) {
            result = false;
        } finally {
            try {
                this.wb.close();
                this.fis.close();
            } catch (Exception e) {
                result = false;
            }

        }
        return result;
    }

    public static void main(String[] args) {
        try {
            ExcelTool c = new ExcelTool();
            c.createExcel("d:/11.xls");
            c.setCurSheet(0, "11");
            c.setColumnView(1, 100);
            c.saveExcel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到工作薄的名称
     * 
     * @parma sheetIndex 工作薄号
     * @return String
     */

    public String getSheetName(int sheetIndex) {

        String sheetNam = "";
        try {
            jxl.Sheet rs = this.wb.getSheet(sheetIndex);
            sheetNam = rs.getName();
        } catch (Exception e) {
            sheetNam = "";
        } finally {
            try {

            } catch (Exception e) {
                sheetNam = "";
            }
        }
        return sheetNam;

    }

    /**
     * 得到当前工作薄的总列数
     * 
     * @parma sheetIndex 工作薄号
     * @return int
     */

    public int getColCount(int sheetIndex) {

        int colCnt = 0;
        try {
            jxl.Sheet rs = this.wb.getSheet(sheetIndex);
            colCnt = rs.getColumns();
        } catch (Exception e) {
            colCnt = 0;
        } finally {
            try {

            } catch (Exception e) {
                colCnt = 0;
            }

        }
        return colCnt;

    }

    /**
     * 得到当前工作薄的总行数
     * 
     * @parma sheetIndex 工作薄号
     * @return int
     */

    public int getRowCount(int sheetIndex) {

        int colCnt = 0;
        try {
            jxl.Sheet rs = this.wb.getSheet(sheetIndex);
            colCnt = rs.getRows();
        } catch (Exception e) {
            colCnt = 0;
        } finally {
            try {

            } catch (Exception e) {
                colCnt = 0;
            }

        }
        return colCnt;

    }

    /**
     * 获取某一列的所有单元格
     * 
     * @parma col 列数
     * @return String[]
     */

    public String[] getColArray(int col) {

        Sheet rs = this.wb.getSheet(0);
        Cell[] getArray = rs.getColumn(col);
        String Str[] = new String[getArray.length];
        try {
            for (int i = 0; i < getArray.length; i++) {
                Cell c00 = rs.getCell(col, i);
                Str[i] = c00.getContents();
            }
        } catch (Exception e) {
            Str = null;
        } finally {
            try {

            } catch (Exception e) {
                Str = null;
            }

        }
        return Str;

    }

    /**
     * 获取某一行的所有单元格
     * 
     * @parma col 列数
     * @return String[]
     */

    public String[] getRowArray(int row) {

        Sheet rs = this.wb.getSheet(0);
        Cell[] getArray = rs.getRow(row);
        String Str[] = new String[getArray.length];

        try {
            for (int i = 0; i < getArray.length; i++) {
                Cell c00 = rs.getCell(i, row);
                Str[i] = c00.getContents();
            }
        } catch (Exception e) {
            Str = null;
        } finally {
            try {

            } catch (Exception e) {
                Str = null;
            }

        }
        return Str;

    }
    
    /**
     * 获取某一行的所有单元格 倪卫俊
     * 
     * @parma col 列数
     * @return String[]
     */
    public String[] getRowArrayN(int row) {

        Sheet rs = this.wb.getSheet(0);
        Cell[] getArrayN = rs.getRow(0);
        //Cell[] getArray = rs.getRow(row);
        String Str[] = new String[getArrayN.length];

        try {
            for (int i = 0; i < getArrayN.length; i++) {
                Cell c00 = rs.getCell(i, row);
                Str[i] = c00.getContents();
            }
        } catch (Exception e) {
            Str = null;
        } finally {
            try {

            } catch (Exception e) {
                Str = null;
            }

        }
        return Str;

    }

    /**
     * 设置字体（共11中样式） 包括字体样式、颜色、大小、加粗、斜体、下划线等的设置。
     * 
     * @parma int format 样式编号 int fontSize 字体大小
     * @return boolean
     */
    public void setCellFont(int format, int fontSize) {
        if (0 == format) {
            // 字体 ARIAL 颜色红
            this.wf_blk = new WritableFont(WritableFont.ARIAL, fontSize, WritableFont.NO_BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.RED);
        } else if (1 == format) {
            // 字体 ARIAL 颜色红,加粗,单下划线,斜体
            this.wf_blk = new WritableFont(WritableFont.ARIAL, fontSize, WritableFont.BOLD, true,
                    UnderlineStyle.SINGLE, Colour.RED);
        } else if (2 == format) {
            // 字体 TAHOMA 颜色绿
            this.wf_blk = new WritableFont(WritableFont.TAHOMA, fontSize, WritableFont.NO_BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.GREEN);
        } else if (3 == format) {
            // 字体 TAHOMA 颜色绿,加粗,单下划线,斜体
            this.wf_blk = new WritableFont(WritableFont.TAHOMA, fontSize, WritableFont.BOLD, true,
                    UnderlineStyle.SINGLE, Colour.GREEN);
        } else if (4 == format) {
            // 字体 TIMES 颜色蓝
            this.wf_blk = new WritableFont(WritableFont.TIMES, fontSize, WritableFont.NO_BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLUE);
        } else if (5 == format) {
            // 字体 TAHOMA 颜色蓝,加粗,单下划线,斜体
            this.wf_blk = new WritableFont(WritableFont.TIMES, fontSize, WritableFont.BOLD, true,
                    UnderlineStyle.SINGLE, Colour.BLUE);
        } else if (6 == format) {
            // 字体 COURIER 颜色深蓝
            this.wf_blk = new WritableFont(WritableFont.COURIER, fontSize, WritableFont.BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.DARK_BLUE);
        } else if (7 == format) {
            // 字体 COURIER 颜色深蓝,加粗,单下划线,斜体
            this.wf_blk = new WritableFont(WritableFont.COURIER, fontSize, WritableFont.BOLD, true,
                    UnderlineStyle.SINGLE, Colour.DARK_BLUE);
        } else if (8 == format) {
            // 字体 ARIAL 颜色深红,加粗,单下划线,斜体
            this.wf_blk = new WritableFont(WritableFont.ARIAL, fontSize, WritableFont.BOLD, true,
                    UnderlineStyle.SINGLE, Colour.DARK_RED);
        } else if (9 == format) {
            // 字体 TAHOMA 颜色黑,加粗,单下划线,斜体
            this.wf_blk = new WritableFont(WritableFont.TAHOMA, fontSize, WritableFont.BOLD, true,
                    UnderlineStyle.SINGLE, Colour.BLACK);
        } else if (10 == format) {
            // 字体 TAHOMA 颜色黑, 加粗
            this.wf_blk = new WritableFont(WritableFont.TAHOMA, fontSize, WritableFont.BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLACK);

        } else {
            // 字体 TAHOMA 颜色黑
            this.wf_blk = new WritableFont(WritableFont.TAHOMA, fontSize, WritableFont.NO_BOLD, false,
                    UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        }
    }

    public void setColumnView(int colIdx, int length) {
        this.ws.setColumnView(colIdx, length);
    }

    /**
     * 设置单个cell的背景色
     * 
     * @parma int col 列号 int row 行号 String colorStr 背景色
     * @return boolean
     */
    public void setCellBgcolor(String colorStr) {
        try {

            this.wcf = new WritableCellFormat(this.wf_blk);
            if (colorStr.equalsIgnoreCase("red")) {
                this.wcf.setBackground(Colour.RED);

            } else if (colorStr.equalsIgnoreCase("green")) {
                this.wcf.setBackground(Colour.GREEN);

            } else if (colorStr.equalsIgnoreCase("blue")) {
                this.wcf.setBackground(Colour.BLUE);

            } else if (colorStr.equalsIgnoreCase("yellow")) {
                this.wcf.setBackground(Colour.YELLOW);

            } else if (colorStr.equalsIgnoreCase("brown")) {
                this.wcf.setBackground(Colour.BROWN);

            } else if (colorStr.equalsIgnoreCase("dark_blue")) {
                this.wcf.setBackground(Colour.DARK_BLUE);
            } else {
                this.wcf.setBackground(Colour.BLACK);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得某个单元格的内容。不论单元格是何种数据类型都将返回字符型。
     * 
     * @parma int col 列号 int row 行号
     * @return String
     */
    public String getCellContent(int col, int row) {
        String cellContent = "";
        try {
            // 默认打开第一张工作薄。
            Sheet rs = this.wb.getSheet(0);
            // 取得某一单元格的内容
            Cell c00 = rs.getCell(col, row);
            cellContent = c00.getContents();
        } catch (Exception e) {
            cellContent = "";
        } finally {
            try {

            } catch (Exception e) {
                cellContent = "";
            }
        }
        return cellContent;
    }

    /**
     * 取得某个单元格数据类型。没传入参数sheetIndex,默认取第一长工作表
     * 
     * @parma int col 列号 int row 行号
     * @return String
     */
    public String getCellType(int col, int row) {
        String typeStr = "";
        try {
            // 读取第一张工作表
            Sheet rs = this.wb.getSheet(0);
            // 获得第一个单元格对象
            Cell c00 = rs.getCell(col, row);
            // 判断单元格的类型, 做出相应的转化
            if (c00.getType() == CellType.LABEL) {
                typeStr = "String";
            }
            if (c00.getType() == CellType.DATE) {
                typeStr = "Date";
            }
            if (c00.getType() == CellType.NUMBER) {
                typeStr = "Number";
            }
        } catch (Exception e) {
            typeStr = "";
        } finally {
            try {

            } catch (Exception e) {
                typeStr = "";
            }
        }
        return typeStr;
    }

    /**
     * 取得某个单元格字符内容。没传入参数sheetIndex,默认取第一长工作表
     * 
     * @parma int col 列号 int row 行号
     * @return String
     */
    public String getCellTex(int col, int row) {
        String cellContent = "";
        try {
            // 默认打开第一张工作薄。
            Sheet rs = this.wb.getSheet(0);
            Cell c00 = rs.getCell(col, row);
            LabelCell labelc00 = (LabelCell) c00;
            cellContent = labelc00.getString();
        } catch (Exception e) {
            cellContent = "";
        } finally {
            try {

            } catch (Exception e) {
                cellContent = "";
            }
        }
        return cellContent;
    }

    /**
     * 取得某个单元格数字内容。没传入参数sheetIndex,默认取第一长工作表
     * 
     * @parma int col 列号 int row 行号
     * @return String
     */
    public double getCellNum(int col, int row) {
        double cellContent = 0;
        try {
            // 默认打开第一张工作薄。
            Sheet rs = this.wb.getSheet(0);
            Cell c10 = rs.getCell(col, row);
            NumberCell numc10 = (NumberCell) c10;
            cellContent = numc10.getValue();
        } catch (Exception e) {
            cellContent = 0;
        } finally {
            try {

            } catch (Exception e) {
                cellContent = 0;
            }
        }
        return cellContent;
    }

    /**
     * 取得某个单元格日期内容。没传入参数sheetIndex,默认取第一长工作表
     * 
     * @parma int col 列号 int row 行号
     * @return Date
     */
    public Date getCellDate(int col, int row) {
        Date cellContent = null;
        try {
            // 默认打开第一张工作薄。
            Sheet rs = this.wb.getSheet(0);
            Cell c00 = rs.getCell(col, row);
            DateCell labeldate00 = (DateCell) c00;
            cellContent = labeldate00.getDate();
        } catch (Exception e) {
            cellContent = null;
        } finally {
            try {

            } catch (Exception e) {
                cellContent = null;
            }
        }
        return cellContent;
    }

    /**
     * 读取指定路径下的Excel文件的数据。没传入参数sheetIndex,默认取第一张工作表
     * 
     * @parma
     * @return String[][] 返回二维数组
     */
    public String[][] readExcel() {
        String[][] strTemp = null;
        try {
            Sheet rs = this.wb.getSheet(0);
            int rows = rs.getRows();
            int cols = rs.getColumns();
            strTemp = new String[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    Cell ctemp = rs.getCell(j, i);
                    strTemp[i][j] = ctemp.getContents().trim();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {

            } catch (Exception e) {
            }
        }
        return strTemp;
    }

    /**
     * 读取指定路径下的Excel文件的数据。没传入参数sheetIndex,默认取第一张工作表 第k行开始读
     * 
     * @parma
     * @return String[][] 返回二维数组
     */
    public String[][] readExcel(int k) {
        String[][] strTemp = null;
        try {
            Sheet rs = this.wb.getSheet(0);
            int rows = rs.getRows();

            if (k > rows - 1) {
                return null;
            }

            int cols = rs.getColumns();
            strTemp = new String[rows - k][cols];
            for (int i = 0; i < rows - k; i++) {
                for (int j = 0; j < cols; j++) {
                    Cell ctemp = rs.getCell(j, i + k);
                    strTemp[i][j] = ctemp.getContents().trim();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {

            } catch (Exception e) {
            }
        }
        return strTemp;
    }

    /**
     * 读取指定路径下的Excel文件的数据。没传入参数sheetIndex, 取第sheetIdx张工作表 第starRowIdx行开始读,直到结束
     * 
     * @param sheetIdx
     * @param starRowIdx
     * @return String[][] 返回二维数组 add by shangxiaowei 20120629
     */
    public String[][] readExcelData(int sheetIdx, int starRowIdx) {
        if (sheetIdx < 0 || starRowIdx < 0) {
            return null;
        }

        String[][] strTemp = null;
        try {
            Sheet rs = this.wb.getSheet(sheetIdx);
            if (null == rs) {
                return null;
            }
            int rows = rs.getRows();
            if (starRowIdx > rows - 1) {
                return null;
            }
            int cols = rs.getColumns();
            strTemp = new String[rows - starRowIdx][cols];
            for (int i = 0; i < rows - starRowIdx; i++) {
                for (int j = 0; j < cols; j++) {
                    Cell ctemp = rs.getCell(j, i + starRowIdx);
                    strTemp[i][j] = ctemp.getContents().trim();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strTemp;
    }

    /**
     * 读取指定路径下的Excel文件的数据。没传入参数sheetIndex,默认取第一张工作表 读取第k行到第m行
     * 
     * @parma
     * @return String[][] 返回二维数组
     */
    public String[][] readExcel(int k, int m) {
        String[][] strTemp = null;
        try {
            Sheet rs = this.wb.getSheet(0);
            int rows = rs.getRows();

            if (k > rows - 1 || m > rows - 1) {
                return null;
            }

            int cols = rs.getColumns();
            strTemp = new String[rows][cols];
            for (int i = k; i < m; i++) {
                for (int j = 0; j < cols; j++) {
                    Cell ctemp = rs.getCell(j, i);
                    strTemp[i][j] = ctemp.getContents().trim();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {

            } catch (Exception e) {
            }
        }
        return strTemp;
    }

    /**
     * 读取指定路径下的Excel文件的数据。没传入参数sheetIndex,默认取第一张工作表 读取第k行
     * 
     * @parma
     * @return String[][] 返回一维数组
     */
    public String[] readExcelOneRow(int k) {
        String[] strTemp = null;
        try {
            Sheet rs = this.wb.getSheet(0);
            int rows = rs.getRows();

            if (k > rows) {
                return null;
            }

            int cols = rs.getColumns();
            strTemp = new String[cols];

            for (int j = 0; j < cols; j++) {
                Cell ctemp = rs.getCell(j, k);
                if (null == ctemp.getContents().trim() || "".equals(ctemp.getContents().trim())) {
                    continue;
                }
                strTemp[j] = ctemp.getContents();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {

            } catch (Exception e) {
            }
        }
        return strTemp;
    }

    /**
     * 读取指定路径下的Excel文件的数据。没传入参数sheetIndex,默认取第一张工作表 读取第k行
     * 
     * @param sheetIdx
     *            sheet 的索引，从0开始;
     * @param rowIdx
     *            行的索引，从0开始;
     * @return String[][] 返回一维数组 add by shangxiaowei 20120629
     */
    public String[] readExcelOneRow(int sheetIdx, int rowIdx) {
        if (sheetIdx < 0 || rowIdx < 0) {
            return null;
        }

        String[] strTemp = null;
        try {
            Sheet rs = this.wb.getSheet(sheetIdx);
            if (null == rs) {
                return null;
            }

            int rows = rs.getRows();

            if (rowIdx > rows) {
                return null;
            }

            int cols = rs.getColumns();
            strTemp = new String[cols];

            for (int j = 0; j < cols; j++) {
                Cell ctemp = rs.getCell(j, rowIdx);
                if (null == ctemp.getContents().trim() || "".equals(ctemp.getContents().trim())) {
                    continue;
                }
                strTemp[j] = ctemp.getContents();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return strTemp;
    }

    public String toCN(String strvalue) {
        try {
            if (strvalue == null) {
                return null;
            } else {
                strvalue = new String(strvalue.getBytes("gb2312"), "GBK");
                return strvalue;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 
     * @param strvalue
     * @return
     */
    public String DBtoCN(String strvalue) {
        try {
            if (strvalue == null) {
                return null;
            } else {
                strvalue = new String(strvalue.getBytes("ISO-8859-1"), "gb2312");
                return strvalue;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 合并单元格
     * 
     * @param col1
     *            开始列号 从0开始计算
     * @param row1
     *            开始行号 从0开始计算
     * @param col2
     *            结束列号 从0开始计算
     * @param row2
     *            结算行号 从0开始计算
     * @throws RowsExceededException
     * @throws WriteException
     */
    public void mergeCells(int col1, int row1, int col2, int row2) throws RowsExceededException, WriteException {
        this.ws.mergeCells(col1, row1, col2, row2);
    }

    /**
     * 设置下拉列表
     * 
     * @param row
     * @param col
     * @param list
     * @param str
     * @throws RowsExceededException
     * @throws WriteException
     */
    public void setCellFeatures(int col, int row, List<String> list, String str) throws RowsExceededException,
            WriteException {
        Label la = new Label(col, row, str);
        
        WritableCellFeatures features = new WritableCellFeatures();
        features.setDataValidationList(list);
       
        la.setCellFeatures(features);
        
        this.ws.addCell(la);
    }
    
    /**
     * 设置下拉列表  倪卫俊
     * 
     * @param row
     * @param col
     * @param list
     * @param str
     * @throws RowsExceededException
     * @throws WriteException
     */
    public void setCellFeaturesR(int col, int row,int cols, int rows, int cole, int rowe) throws RowsExceededException,
            WriteException {
        Blank bl = new Blank(col,row);
        WritableCellFeatures features = new WritableCellFeatures();
        
        features.setDataValidationRange(cols, rows, cole, rowe);
       
        bl.setCellFeatures(features);
        
        this.ws.addCell(bl);
    }

    public void closeFos() throws IOException {
        if (this.fos != null) {
            this.fos.close();
            this.fos = null;
        }
    }

    public void closeFis() throws IOException {
        if (this.fis != null) {
            this.fis.close();
            this.fis = null;
        }
    }

    public WritableWorkbook getWwb() {
        return this.wwb;
    }

    public void setWwb(WritableWorkbook wwb) {
        this.wwb = wwb;
    }

    public WritableSheet getWs() {
        return this.ws;
    }

    public void setWs(WritableSheet ws) {
        this.ws = ws;
    }

    public Workbook getWb() {
        return this.wb;
    }

    public void setWb(Workbook wb) {
        this.wb = wb;
    }

    public WritableFont getWf() {
        return this.wf_blk;
    }

    public void setWf(WritableFont wf) {
        this.wf_blk = wf;
    }

    public WritableCellFormat getWcf() {
        return this.wcf;
    }

    public void setWcf(WritableCellFormat wcf) {
        this.wcf = wcf;
    }

}
