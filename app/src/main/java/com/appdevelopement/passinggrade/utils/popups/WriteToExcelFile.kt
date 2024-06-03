package com.appdevelopement.passinggrade.utils.popups

import android.content.Context
import android.os.Environment
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.IndexedColors
import java.io.File
import java.io.FileOutputStream
import java.io.IOException // Add this import

class WriteToExcelFile(private val context: Context) {
    private val excelColumns = arrayOf("Id", "Competence", "Grade", "Comment")

    fun writeToExcel(fileName: String, records: List<Array<String>>) {
        val excelWorkbook = XSSFWorkbook()

        val sheet = excelWorkbook.createSheet("Grades")

        val headerFont = excelWorkbook.createFont().apply {
            color = IndexedColors.BLACK.index
            setBold(true)
        }

        val headerCellStyle = excelWorkbook.createCellStyle().apply {
            setFont(headerFont)
        }

        val headerRow = sheet.createRow(0)

        for ((index, columnName) in excelColumns.withIndex()) {
            val cell = headerRow.createCell(index)
            cell.setCellValue(columnName)
            cell.cellStyle = headerCellStyle
        }

        for ((index, record) in records.withIndex()) {
            val row = sheet.createRow(index + 1) // Avoiding the header row

            for ((j, fieldValue) in record.withIndex())
                row.createCell(j).setCellValue(fieldValue)
        }

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        try {
            val fos = FileOutputStream(File(path, "$fileName.xlsx"))
            excelWorkbook.write(fos)
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        excelWorkbook.close()
    }
}