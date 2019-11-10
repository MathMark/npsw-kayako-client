package kayakoAPI.fileWriter;

import common.annotaions.DateType;
import common.annotaions.Excel;
import kayakoAPI.pojos.Conversation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import kayakoAPI.pojos.conversationStatus.Status;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ExcelWriter {

    private String fileName;

    private Workbook workbook;
    private CreationHelper creationHelper;
    private Sheet sheet;
    private FileOutputStream fileOutputStream;

    private Logger logger = Logger.getLogger(ExcelWriter.class.getName());

    public ExcelWriter(String fileName){
        this.fileName = fileName;
    }

    public ExcelWriter(){}

    @Deprecated
    public void writeConversations(String[] columnHeaders, List<Conversation> conversations) throws IOException {
        initialize();
        createHeaderCells(columnHeaders);

        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        int rowNum = 1;
        int columnNum;

        for(Conversation conversation : conversations) {
            columnNum = 0;
            Row row = sheet.createRow(rowNum++);

            row.createCell(columnNum++)
                    .setCellValue(conversation.getSubject());

            row.createCell(columnNum++)
                    .setCellValue(Status.fromInt(conversation.getStatusId()).toString());

            //Requester

            row.createCell(columnNum++)
                    .setCellValue(conversation.getRequester().getFullName());


            row.createCell(columnNum++)
                    .setCellValue(conversation.getRequester().getAcpLink());


            Cell createdAtCell = row.createCell(columnNum++);
            createdAtCell.setCellValue(conversation.getCreatedAt());
            createdAtCell.setCellStyle(dateCellStyle);

            Cell updatedAtCell = row.createCell(columnNum++);
            updatedAtCell.setCellValue(conversation.getUpdatedAt());
            updatedAtCell.setCellStyle(dateCellStyle);
        }

        // Resize all columns to fit the content size
        for(int i = 0; i < columnHeaders.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(fileOutputStream);
        fileOutputStream.close();

        // Closing the workbook
        workbook.close();
    }

    public <T> List<T> readObjects(String filePath, Class clazz) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(filePath));
        Sheet sheet = workbook.getSheetAt(0);

        DataFormatter dataFormatter = new DataFormatter();

        Field[] fields = clazz.getDeclaredFields();
        Row row;
        Cell cell = null;

        Excel excel;
        String cellValue;

        int rowNum = 1;
        boolean stop = false;

        List<T> result = new ArrayList<>();
        try {
            while (!stop) {
                row = sheet.getRow(rowNum);
                Object instance = Class.forName(clazz.getName()).newInstance();
                for (Field field : fields) {
                    if ((excel = field.getAnnotation(Excel.class)) != null) {
                        cell = row.getCell(excel.cellNum());
                        cellValue = dataFormatter.formatCellValue(cell);
                        if ("END".equals(cellValue)) {
                            stop = true;
                            break;
                        }else {
                            if (field.isAnnotationPresent(DateType.class)) {
                                if(!"".equals(cellValue)){
                                    new PropertyDescriptor(field.getName(), clazz)
                                            .getWriteMethod()
                                            .invoke(instance, new SimpleDateFormat("MM/dd/yy").parse(cellValue));
                                }
                            } else {
                                System.out.println(field.getName());
                                new PropertyDescriptor(field.getName(), clazz)
                                        .getWriteMethod()
                                        .invoke(instance, ("".equals(cellValue) ? "(NULL)" : cellValue));
                            }
                        }
                    }
                }
                rowNum++;
                result.add((T)instance);
            }
        }catch (Exception e){
            logger.severe("Error row index - " + rowNum + " cell index - " + cell.getColumnIndex() + "\n");
            e.printStackTrace();
        }

        workbook.close();
        return result;
    }

    public <T> void writeObjects(List<T> objects, Class objectClass) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException {
        initialize();

        List<String> headers = getHeaders(objectClass);
        createHeaders(headers);

        int rowNum = 1;
        List<String> objectFields;

        logger.info("Starting recording objects...");

        for(T object : objects){
            objectFields = getFields(objectClass, object);
            write(rowNum,objectFields);
            rowNum++;
        }

        logger.info("Objects recorded successfully.");

        for(int i = 0; i < headers.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(fileOutputStream);
        fileOutputStream.close();

        // Closing the workbook
        workbook.close();
        logger.info("File saved.");
    }

    private List<String> getHeaders(Class objectClass){
        Field[] objectFields = objectClass.getDeclaredFields();

        Excel excel;
        List<String> headers = new ArrayList<>();
        for (Field field : objectFields) {
            if ((excel = field.getAnnotation(Excel.class)) != null) {
                if (excel.isCustomObject()) {
                    headers.addAll(getHeaders(field.getType()));
                } else {
                    headers.add(excel.columnName());
                }
            }
        }
        return headers;
    }

    private <T> List<String> getFields(Class objectClass, T object) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        List<String> fields = new ArrayList<>();

        Field[] objectFields = objectClass.getDeclaredFields();
        Excel excel;
        for (Field field : objectFields) {
            if ((excel = field.getAnnotation(Excel.class)) != null) {
                if (excel.isCustomObject()) {
                    Object value = new PropertyDescriptor(field.getName(),objectClass)
                            .getReadMethod()
                            .invoke(object);
                    if(value != null){
                        fields.addAll(getFields(field.getType(),value));
                    }
                } else {
                    try {
                        String value = new PropertyDescriptor(field.getName(), objectClass)
                                .getReadMethod()
                                .invoke(object).toString();
                        fields.add((value != null) ? value : "(NULL)");
                    }catch (NullPointerException e){
                        fields.add("(NULL)");
                    }
                }
            }
        }
        return fields;
    }

    private <T> void write(int rowNum, List<String> objectFields){
        Row row = sheet.createRow(rowNum);
        Cell cell;
        for(int i = 0; i < objectFields.size(); i++){
            cell = row.createCell(i);
            cell.setCellValue(objectFields.get(i));
        }
    }

    private void initialize() throws FileNotFoundException {
        workbook = new XSSFWorkbook();
        creationHelper = workbook.getCreationHelper();
        sheet = workbook.createSheet(fileName);
        fileOutputStream = new FileOutputStream(fileName + ".xlsx");
    }

    private void createHeaders(List<String> names){
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.BLUE_GREY.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);

        for(int i = 0; i < names.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(names.get(i));
            cell.setCellStyle(headerCellStyle);
        }
    }

    @Deprecated
    private void createHeaderCells(String[] columnHeaders){
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.BLUE_GREY.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);

        for(int i = 0; i < columnHeaders.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
            cell.setCellStyle(headerCellStyle);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
