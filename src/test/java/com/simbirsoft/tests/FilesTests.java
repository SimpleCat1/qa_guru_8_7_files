package com.simbirsoft.tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class FilesTests {
    @Test
    public void readFileTxt_TextIsRight() throws Exception {
        String result;
        try (InputStream stream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("files/file1.txt")) {//try нужна, чтобы потом файл закрыл java, а то что в круглых скобках. ну не нашел в интернете на это ответ, а так все обычно в теле try пишется
            result = new String(stream.readAllBytes(), "UTF-8");
        }
        assertThat(result).contains("Текст для проверки\r\nТекст");
    }

    @Test
    public void readFilePDF_TextIsRight() throws Exception {
        PDF stream = new PDF(getClass().getClassLoader().getResource("files/PDF.pdf"));
        assertThat(stream.text).contains("Здравствуйте, зовут \r\nPostman\r\nТекст, текст, текст\r\n");
    }


    private static Stream<Arguments> testsData() {
        return Stream.of(
                arguments(5, 0, 0),
                arguments(6, 1, 0),
                arguments(1, 2, 0),
                arguments(6, 0, 1),
                arguments(2, 1, 1),
                arguments(1, 2, 1)
        );
    }
    @ParameterizedTest(name = "{index} - {0} is a palindrome")
    @MethodSource("testsData")
    void readFileXLSX_TextIsRight(int textInArea, int x, int y) throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("files/ЕКСЕЛЬ.xlsx")) {
            //try нужен, чтобы файл закрыли, когда всю работу с ним сделаем в блоке try(java закроет )
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(x).getCell(y).getNumericCellValue()).isEqualTo(textInArea);
        }
    }
}
