package com.appdevelopement.passinggrade.utils

import android.content.Context
import android.net.Uri
import com.appdevelopement.passinggrade.dto.CompentenceDto
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.IOException


class ImportGradingSheet(private val context: Context) {

    @Throws(IOException::class)
    fun readFromExcel(uri: Uri): List<CompentenceDto> {
        val inputStream =
            context.contentResolver.openInputStream(uri) ?: throw IOException("Unable to open input stream from URI")

        val workbook = XSSFWorkbook(inputStream)
        val sheet = workbook.getSheetAt(0)
        val criteriaDTO = ArrayList<CompentenceDto>()

        // Skip first two rows
        for (i in 0 until 2) {
            sheet.getRow(i)
        }

        // Iterate through rows
        for (i in 2..sheet.lastRowNum) {
            val row = sheet.getRow(i)
            if (row != null) {
                val name = getCellValue(row.getCell(1))
                val weight = getCellValue(row.getCell(2)).toIntOrNull() ?: 0
                val mustpass = getCellValue(row.getCell(3)).toBoolean()

                val competenceDto = CompentenceDto(
                    idComptence = 0,  // Assign a proper ID if required
                    idExam = 0,       // Assign a proper Exam ID if required
                    dtName = name, dtCompetenceWeight = weight, dtMustPass = mustpass
                )
                criteriaDTO.add(competenceDto)
            }
        }

        workbook.close()
        inputStream.close()
        return criteriaDTO
    }

    private fun getCellValue(cell: Cell?): String {
        return when (cell?.cellType) {
            CellType.STRING -> cell.stringCellValue
            CellType.NUMERIC -> cell.numericCellValue.toString()
            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            CellType.FORMULA -> cell.cellFormula
            else -> ""
        }
    }
}