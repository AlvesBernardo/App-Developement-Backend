package com.appdevelopement.passinggrade.utils.popups

import android.content.Context
import android.net.Uri
import com.appdevelopement.passinggrade.dto.StudentDTO
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.IOException
import java.util.ArrayList
import org.apache.poi.ss.usermodel.*

class ReadFromExcelFile(private val context: Context) {

    @Throws(IOException::class)
    fun readFromExcel(uri: Uri): List<StudentDTO> {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IOException("Unable to open input stream from URI")
        val workbook = XSSFWorkbook(inputStream)
        val sheet = workbook.getSheetAt(0)

        val studentDTOList = ArrayList<StudentDTO>()
        val iterator = sheet.iterator()

        repeat(2) {
            if (iterator.hasNext()) {
                iterator.next()
            }
        }

        for (row in iterator) {
            val studentNumber = getCellValue(row.getCell(1)).toDouble().toInt()
            val studentName = getCellValue(row.getCell(2))
            val isGraded = getCellValue(row.getCell(3)).isNotEmpty()

            val studentDTO = StudentDTO(
                studentName = studentName,
                studentNumber = studentNumber,
                isGraded = isGraded
            )
            studentDTOList.add(studentDTO)
        }

        workbook.close()
        inputStream.close()
        return studentDTOList
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