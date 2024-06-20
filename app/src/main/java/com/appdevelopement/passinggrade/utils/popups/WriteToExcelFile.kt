package com.appdevelopement.passinggrade.utils.popups

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException // Add this import
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class WriteToExcelFile(private val context: Context) {
  private val excelColumns = arrayOf("Id", "Competence", "Grade", "Comment")

  fun writeToExcel(fileName: String, records: List<Array<String>>) {
    val excelWorkbook = XSSFWorkbook()

    val sheet = excelWorkbook.createSheet("Grades")

    val headerFont =
        excelWorkbook.createFont().apply {
          color = IndexedColors.BLACK.index
          setBold(true)
        }

    val headerCellStyle = excelWorkbook.createCellStyle().apply { setFont(headerFont) }

    val headerRow = sheet.createRow(0)

    for ((index, columnName) in excelColumns.withIndex()) {
      val cell = headerRow.createCell(index)
      cell.setCellValue(columnName)
      cell.cellStyle = headerCellStyle
    }

    for ((index, record) in records.withIndex()) {
      val row = sheet.createRow(index + 1) // Avoiding the header row

      for ((j, fieldValue) in record.withIndex()) {
        val cell = row.createCell(j)
        cell.setCellValue(fieldValue)

        if (j == 2) { // Assuming grade is the third column
          val grade = fieldValue.toDoubleOrNull()
          if (grade != null) {
            val cellStyle = excelWorkbook.createCellStyle()
            val font = excelWorkbook.createFont()

            when {
              grade < 2 -> font.color = IndexedColors.DARK_RED.getIndex()
              grade < 4 -> font.color = IndexedColors.RED.getIndex()
              grade < 5.5 -> font.color = IndexedColors.ORANGE.getIndex()
              else -> font.color = IndexedColors.BRIGHT_GREEN.getIndex()
            }

            cellStyle.setFont(font)
            cell.cellStyle = cellStyle
          }
        }
      }
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
