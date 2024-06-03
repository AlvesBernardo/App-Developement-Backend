package com.appdevelopement.passinggrade.utils.popups

import android.content.Context
import android.net.Uri
import com.appdevelopement.passinggrade.dto.StudentDTO
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.IOException

class ReadFromExcelFile(private val context: Context) {

    @Throws(IOException::class)
    fun readFromExcel(uri: Uri): List<StudentDTO> {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IOException("Unable to open input stream from URI")
        val workbook = XSSFWorkbook(inputStream)
        val sheet = workbook.getSheetAt(0)

        val data = mutableListOf<StudentDTO>()
        for (row in sheet) {
            val name = row.getCell(0).stringCellValue
            val studentNumber = row.getCell(1).numericCellValue.toInt() // Convert to Int
            val isGraded = row.getCell(2).booleanCellValue
            data.add(StudentDTO(name, studentNumber, isGraded))
        }
        workbook.close()
        inputStream.close()
        return data
    }
}
