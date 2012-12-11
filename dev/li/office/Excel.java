package li.office;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Excel {
    public static void main(String[] args) throws Exception {
        File file = new File("F:\\PPS\\人力资源部\\2010会员档案总列表.xls");

        List<List<String>> table = read(file);

        for (List<String> line : table) {
            for (String cell : line) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }

    public static List<List<String>> read(File file) {
        try {

            List<List<String>> table = new ArrayList<List<String>>();

            Workbook workbook = new HSSFWorkbook(new BufferedInputStream(new FileInputStream(file)));

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    List<String> line = new ArrayList<String>();
                    for (Cell cell : row) {
                        line.add(cell.toString());
                    }
                    table.add(line);
                }
            }
            return table;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}