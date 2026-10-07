package sample.Service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sample.model.Student;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelService {

    public List<Student> readStudents(File file) {

        List<Student> students = new ArrayList<>();

        try (
                FileInputStream inputStream =
                        new FileInputStream(file);

                Workbook workbook =
                        new XSSFWorkbook(inputStream)
        ) {

            Sheet sheet = workbook.getSheetAt(0);

            /*
             * We DO NOT trust the existing Excel headers.
             *
             * Our application defines the structure:
             *
             * Column A -> name
             * Column B -> ssn
             * Column C -> mail
             * Column D -> password
             */

            for (int rowIndex = 0;
                 rowIndex <= sheet.getLastRowNum();
                 rowIndex++) {

                Row row = sheet.getRow(rowIndex);

                if (row == null) {
                    continue;
                }

                // Skip completely empty rows
                if (isEmptyRow(row)) {
                    continue;
                }

                /*
                 * If the first row contains headers,
                 * skip it.
                 *
                 * Otherwise the first row is treated
                 * as a student.
                 */
                if (rowIndex == 0 && looksLikeHeader(row)) {
                    continue;
                }

                String name =
                        getCellValue(row.getCell(0));

                String ssn =
                        getCellValue(row.getCell(1));

                String mail =
                        getCellValue(row.getCell(2));

                String password =
                        getCellValue(row.getCell(3));

                if (name.isBlank() && ssn.isBlank()) {
                    continue;
                }

                students.add(
                        new Student(
                                name,
                                ssn,
                                mail,
                                password
                        )
                );
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to read Excel file: "
                            + file.getName(),
                    e
            );
        }

        return students;
    }

    private String getCellValue(Cell cell) {

        if (cell == null) {
            return "";
        }

        DataFormatter formatter =
                new DataFormatter();

        return formatter
                .formatCellValue(cell)
                .trim();
    }

    private boolean isEmptyRow(Row row) {

        for (int i = 0; i < 4; i++) {

            Cell cell = row.getCell(i);

            if (cell != null &&
                    !getCellValue(cell).isBlank()) {

                return false;
            }
        }

        return true;
    }

    private boolean looksLikeHeader(Row row) {

        String first =
                getCellValue(row.getCell(0))
                        .toLowerCase();

        String second =
                getCellValue(row.getCell(1))
                        .toLowerCase();

        return first.equals("name")
                || first.equals("student name")
                || second.equals("ssn")
                || second.equals("national id");
    }
}