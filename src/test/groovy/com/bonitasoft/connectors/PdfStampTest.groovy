package com.bonitasoft.connectors

import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import spock.lang.Specification

import java.nio.file.Files

class PdfStampTest extends Specification {
    def "should stamp PdfFile"() {
        given:
        File source = aPdfSample()
        File target = Files.createTempFile(source.getName(), '.pdf').toFile()
        File stamp = new File(getClass().getResource('/BonitasoftLogo.png').toURI())
        PdfStamp pdfStamp = new PdfStamp()
        assert source.exists()
        assert target.exists()
        assert stamp.exists()

        when:
        pdfStamp.stampPdfFile(source, target, stamp, 100, 200)

        then:
        target.exists()
        target.canRead()
        target.size() > 0
        println target.absolutePath

    }

    private File aPdfSample() {
        File pdf = Files.createTempFile('pdfSample', '.pdf').toFile()
        PdfWriter writer = new PdfWriter(pdf.getAbsolutePath())
        PdfDocument pdfDoc = new PdfDocument(writer)
        Document doc = new Document(pdfDoc)
        doc.add(new Paragraph("Sample doc"))
        doc.close()
        pdf
    }
}
