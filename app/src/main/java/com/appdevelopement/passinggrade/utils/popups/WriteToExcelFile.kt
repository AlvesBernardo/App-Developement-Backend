package com.appdevelopement.passinggrade.utils.popups

import android.content.Context
import android.os.Environment
import com.appdevelopement.passinggrade.pages.grading.CriterionRecord
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class WriteToExcelFile(private val context: Context) {
  private val excelColumns = arrayOf("Id", "Competence", "Grade", "Comment")

  fun writeToExcel(fileName: String, totalGrade: Double, records: List<CriterionRecord>, comments: List<String>) {
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

      row.createCell(0, CellType.NUMERIC).setCellValue(index + 1.0) // Id column as numeric
      row.createCell(1, CellType.STRING).setCellValue(record.name) // Competence column as string

      val gradeCell = row.createCell(2, CellType.NUMERIC)
      gradeCell.setCellValue(record.progress.toDouble() / 10.0) // Grade column as numeric
      applyGradeCellStyle(gradeCell, record.progress)

      row.createCell(3, CellType.STRING).setCellValue(comments[index]) // Comment column as string
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

  private fun applyGradeCellStyle(cell: XSSFCell, progress: Int) {
    val cellStyle = cell.sheet.workbook.createCellStyle()
    val font = cell.sheet.workbook.createFont()

    when {
      progress < 20 -> font.color = IndexedColors.DARK_RED.index
      progress < 40 -> font.color = IndexedColors.RED.index
      progress < 55 -> font.color = IndexedColors.ORANGE.index
      else -> font.color = IndexedColors.BRIGHT_GREEN.index
    }

    cellStyle.setFont(font)
    cell.cellStyle = cellStyle
  }
}
