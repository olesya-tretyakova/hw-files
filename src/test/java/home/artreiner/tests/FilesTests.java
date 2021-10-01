package home.artreiner.tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import net.lingala.zip4j.ZipFile;
import org.junit.jupiter.api.Test;

import java.io.*;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FilesTests {

    @Test
    public void parseTxtFileTest() throws IOException {
        open("https://filesamples.com/formats/txt");
       File textFile = $("#output > div:last-child a").download();

       String result;
       try (InputStream is = new FileInputStream(textFile)) {
           result = new String(is.readAllBytes(), "UTF-8");
       }
       assertThat(result).containsIgnoringCase("lorem ipsum dolor sit amet");
    }

    @Test
    public void downloadPdfFileTest() throws IOException {
        open("https://file-examples.com/index.php/sample-documents-download/sample-pdf-download/");
        File download = $("a[download='file-sample_150kB.pdf']").download();
        PDF parsed = new PDF(download);
        assertThat(parsed.text).containsIgnoringCase("Lorem ipsum dolor sit amet");
        assertThat(parsed.numberOfPages).isEqualTo(4);
    }

    @Test
    public void parseExcelFileTest() throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("file.xls")) {
            XLS parsedXls = new XLS(inputStream);
            assertThat(
                    parsedXls.excel
                            .getSheetAt(0)
                            .getRow(3)
                            .getCell(4)
                            .getStringCellValue()
            ).isEqualTo("France");
        }
    }

    @Test
    public void downloadExcelTest() throws FileNotFoundException {
        open("https://file-examples.com/index.php/sample-documents-download/sample-xls-download/");
        File download = $("a[download='file_example_XLS_10.xls']").download();
        XLS parsedXls = new XLS(download);
        assertThat(
                parsedXls.excel
                        .getSheetAt(0)
                        .getRow(3)
                        .getCell(4)
                        .getStringCellValue()
        ).isEqualTo("France");
    }

    @Test
    public void parseZipFileTest() throws Exception {
        String zipPassword = "123123";
        ZipFile zipFile = new ZipFile(new File("src/test/resources/zip_2MB.zip"));
        if (zipFile.isEncrypted())
        {
            zipFile.setPassword(zipPassword.toCharArray());
        }
        assertThat(zipFile.getFileHeaders().toString()).containsIgnoringCase("file-sample");
        assertThat(zipFile.getFileHeaders().toString()).containsIgnoringCase("file_example");
    }
}
