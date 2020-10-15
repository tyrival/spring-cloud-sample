package com.acrel.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.acrel.exceptions.CommonException;
import com.acrel.exceptions.ExceptionEnum;
import com.opencsv.CSVReader;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author: ZhouChenyu
 */
public class ExcelUtils {

    private final static String EXT_XLS = "xls";
    private final static String EXT_XLSX = "xlsx";
    private final static String EXT_CSV = "csv";

    /**
     * 根据后缀读取Excel各类文件
     * @param path
     * @return
     */
    public static List<Map<String, Object>> read(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new CommonException(ExceptionEnum.FILE_NOT_FOUND);
        }
        String ext = path.substring(path.lastIndexOf(".") + 1);
        if (EXT_XLS.equals(ext)) {
            return readXls(path);
        } else if (EXT_XLSX.equals(ext)) {
            return readXlsx(path);
        } else if (EXT_CSV.equals(ext)) {
            return readCsv(path);
        }
        throw new CommonException(ExceptionEnum.FILE_TYPE_ERROR);

    }

    /**
     * 读取csv
     * @param path
     * @return
     */
    public static List<Map<String, Object>> readCsv(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            CSVReader reader = new CSVReader(isr);
            String[] header = reader.readNext();
            String[] nextLine;
            List<Map<String, Object>> list = new ArrayList<>();
            while ((nextLine = reader.readNext()) != null) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < nextLine.length; i++) {
                    map.put(header[i], nextLine[i]);
                }
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            throw new CommonException(ExceptionEnum.EXCEL_READ_FAIL);
        }
    }

    /**
     * 读取xls
     * @param path
     * @return
     */
    public static List<Map<String, Object>> readXls(String path) {
        try {
            File file = new File(path);
            FileInputStream in = new FileInputStream(file);
            Workbook workbook = new HSSFWorkbook(in);
            List<Map<String, Object>> list = new ArrayList<>();

            int sheetCount = workbook.getNumberOfSheets();
            if (sheetCount <= 0) {
                return list;
            }
            for (int i = 0; i < sheetCount; i++) {
                List<Map<String, Object>> temp = readSheet(workbook.getSheetAt(i));
                list.addAll(temp);
            }
            return list;
        } catch (Exception e) {
            throw new CommonException(ExceptionEnum.EXCEL_READ_FAIL);
        }
    }

    /**
     * 解析页
     * @return
     */
    private static List<Map<String, Object>> readSheet(Sheet sheet) {
        List<Map<String, Object>> list = new ArrayList<>();
        int rowNum = sheet.getLastRowNum();
        if (rowNum <= 1) {
            return list;
        }

        List<String> headers = new ArrayList<>();
        Row headerRow = sheet.getRow(0);
        int colNum = headerRow.getLastCellNum();
        if (colNum <= 0) {
            return list;
        }
        for (int i = 0; i < colNum; i++) {
            headers.add((String) getValue(headerRow.getCell(i)));
        }

        for (int i = 1; i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            try {
                int end = row.getLastCellNum();
                Map<String, Object> map = new HashMap<>();
                for (int j = 0; j < end; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        continue;
                    }
                    cell.setCellType(1);
                    map.put(headers.get(j), getValue(cell));
                }
                if (map.size() > 0) {
                    list.add(map);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    private static Object getValue(Cell cell) {
        Object obj = null;
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case ERROR:
                obj = cell.getErrorCellValue();
                break;
            case NUMERIC:
                DataFormatter df = new DataFormatter();
                obj = Double.parseDouble(df.formatCellValue(cell));
                break;
            case STRING:
                obj = cell.getStringCellValue();
                break;
            default:
                break;
        }
        return obj;
    }

    /**
     * 读取XLSX
     *
     * @param path
     * @return
     */
    public static List<Map<String, Object>> readXlsx(String path) {
        try {
            XlsxReader excel = new XlsxReader();
            excel.readSheet(path);
            Map<String, Object> headerMap = excel.getHeaderMap();
            List<Map<String, Object>> result = excel.getResult();
            List<Map<String, Object>> output = new ArrayList<>();
            if (result.size() <= 0) {
                return output;
            }
            for (int i = 0; i < result.size(); i++) {
                Map<String, Object> map = result.get(i);
                Map<String, Object> item = new HashMap<>();
                for (String key : map.keySet()) {
                    Object itemVal = map.get(key);
                    String itemKey = (String) headerMap.get(key);
                    item.put(itemKey, itemVal);
                }
                output.add(item);
            }
            return output;
        } catch (Exception e) {
            throw new CommonException(ExceptionEnum.EXCEL_READ_FAIL);
        }
    }

    /**
     * 读取xlsx文件辅助类，主要用于解决读取大容量文件时的内存溢出
     */
    public static class XlsxReader extends DefaultHandler {
        /**
         * 提取列名称的正则表达式
         */
        private static final String DISTILL_COLUMN_REG = "^([A-Z]{1,})";

        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private int curRow = 0;
        private String curCellName = "";

        /**
         * 存储第一行的文本，作为Key
         */
        private Map<String, Object> headerMap = new HashMap<>();

        /**
         * 存储读取结果，第一行作为键
         */
        private List<Map<String, Object>> result = new ArrayList<>();

        /**
         * 读取当前行的数据。key是单元格名称如A1,value是单元格中的值。如果单元格式空,则没有数据。
         */
        private Map<String, Object> rowValueMap = new HashMap<>();

        /**
         * 存储单页的结果
         */
        private List<Map<String, Object>> dataList = new ArrayList<>();

        /**
         * 处理单行数据的回调方法
         *
         * @param curRow
         * @param rowValueMap
         */
        public void optRows(int curRow, Map<String, Object> rowValueMap) {
            Map<String, Object> dataMap = new HashMap<>();
            rowValueMap.forEach((k, v) -> dataMap.put(removeNum(k), v));
            dataList.add(dataMap);
        }

        /**
         * 删除单元格名称中的数字，只保留列号。
         *
         * @param cellName 单元格名称。如:A1
         * @return 列号。如：A
         */
        private String removeNum(String cellName) {
            Pattern pattern = Pattern.compile(DISTILL_COLUMN_REG);
            Matcher m = pattern.matcher(cellName);
            if (m.find()) {
                return m.group(1);
            }
            return "";
        }

        /**
         * 读取Excel所有sheet页的数据。
         */
        public void readSheet(String filePath) throws Exception {
            OPCPackage pkg = OPCPackage.open(filePath);
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();
            XMLReader parser = getSheetParser(sst);
            int index = 1;
            while (getSheet(r, index) != null) {
                InputStream sheet = getSheet(r, index);
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource);
                if (this.dataList.size() > 0) {
                    if (index == 1) {
                        this.headerMap = this.dataList.get(0);
                    }
                    this.dataList.remove(0);
                    this.result.addAll(this.dataList);
                    this.dataList = new ArrayList<>();
                }
                sheet.close();
                index++;
            }
            pkg.close();
        }

        /**
         * 获取页内容
         *
         * @param r
         * @param sheetNum
         * @return
         */
        public InputStream getSheet(XSSFReader r, int sheetNum) {
            try {
                return r.getSheet("rId" + sheetNum);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            // c => 单元格
            if (name.equals("c")) {

                // 如果下一个元素是 SST 的索引，则将nextIsString标记为true
                String cellType = attributes.getValue("t");
                if (cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
            }

            // 置空
            lastContents = "";

            /**
             * 记录当前读取单元格的名称
             */
            String cellName = attributes.getValue("r");
            if (cellName != null && !cellName.isEmpty()) {
                curCellName = cellName;
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            // 根据SST的索引值的到单元格的真正要存储的字符串
            // 这时characters()方法可能会被调用多次
            if (nextIsString) {
                try {
                    int idx = Integer.parseInt(lastContents);
                    lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                } catch (Exception e) {

                }
            }

            // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
            // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
            if (name.equals("v")) {
                String value = lastContents.trim();
                value = value.equals("") ? " " : value;
                rowValueMap.put(curCellName, value);
            } else if (name.equals("row")) {
                optRows(curRow, rowValueMap);
                rowValueMap.clear();
                curRow++;
            }
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            // 得到单元格内容的值
            lastContents += new String(ch, start, length);
        }

        /**
         * 获取单个sheet页的xml解析器。
         *
         * @param sst
         * @return
         * @throws SAXException
         */
        private XMLReader getSheetParser(SharedStringsTable sst) throws SAXException {
            XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
            this.sst = sst;
            parser.setContentHandler(this);
            return parser;
        }

        public Map<String, Object> getHeaderMap() {
            return headerMap;
        }

        public List<Map<String, Object>> getResult() {
            return result;
        }
    }

    /**
     * Excel表格导出
     * @param response HttpServletResponse对象
     * @param excelData Excel表格的数据，封装为List<List<String>>
     * @param sheetName sheet的名字
     * @param fileName 导出Excel的文件名
     * @param columnWidth Excel表格的宽度，建议为15
     * @throws IOException 抛IO异常
     */
    public static void exportExcel(HttpServletResponse response,
                                   List<List<String>> excelData,
                                   String sheetName,
                                   String fileName,
                                   int columnWidth) throws IOException {
        //声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格，设置表格名称
        HSSFSheet sheet = workbook.createSheet(sheetName);
        //设置表格列宽度
        sheet.setDefaultColumnWidth(columnWidth);
        //写入List<List<String>>中的数据
        int rowIndex = 0;
        for(List<String> data : excelData){
            //创建一个row行，然后自增1
            HSSFRow row = sheet.createRow(rowIndex++);
            //遍历添加本行数据
            for (int i = 0; i < data.size(); i++) {
                //创建一个单元格
                HSSFCell cell = row.createCell(i);
                //创建一个内容对象
                HSSFRichTextString text = new HSSFRichTextString(data.get(i));
                //将内容对象的文字内容写入到单元格中
                cell.setCellValue(text);
            }
        }
        //准备将Excel的输出流通过response输出到页面下载
        //八进制输出流
        response.setContentType("application/octet-stream");
        //设置导出Excel的名称
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        //刷新缓冲
        response.flushBuffer();
        //workbook将Excel写入到response的输出流中，供页面下载该Excel文件
        workbook.write(response.getOutputStream());
        //关闭workbook
        workbook.close();

    }
    /**
     * 通过上传的文件读取xls
     * @param file
     * @return
     */
    public static List<Map<String, Object>> readXls(MultipartFile file) {
        try {

            Workbook workbook = new HSSFWorkbook(file.getInputStream());
            List<Map<String, Object>> list = new ArrayList<>();

            int sheetCount = workbook.getNumberOfSheets();
            if (sheetCount <= 0) {
                return list;
            }
            for (int i = 0; i < sheetCount; i++) {
                List<Map<String, Object>> temp = readSheet(workbook.getSheetAt(i));
                list.addAll(temp);
            }
            return list;
        } catch (Exception e) {
            throw new CommonException(ExceptionEnum.EXCEL_READ_FAIL);
        }
    }


}
