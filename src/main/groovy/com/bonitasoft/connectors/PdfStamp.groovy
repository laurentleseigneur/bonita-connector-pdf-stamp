package com.bonitasoft.connectors

import com.itextpdf.io.image.ImageData
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfVersion
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.WriterProperties
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image

class PdfStamp {

    def stampPdfFile(File source, File target, File imageFile, int x, int y) {
        createTarget(target)

        PdfReader reader = new PdfReader(source)
        PdfWriter writer = new PdfWriter(target.getAbsolutePath())

        PdfDocument pdfDocument = new PdfDocument(reader, writer)
        Document document = new Document(pdfDocument)

        ImageData imageData = ImageDataFactory.create(imageFile.bytes);
        Image image = new Image(imageData).setFixedPosition(1, x, y);
        document.add(image)

        document.close();
    }

    def createTarget(File target) {
        PdfWriter writer = new PdfWriter(target.getAbsolutePath(), new WriterProperties().setPdfVersion(PdfVersion.PDF_2_0))
        PdfDocument pdfDocument = new PdfDocument(writer)
        pdfDocument.addNewPage()
        Document document = new Document(pdfDocument)
        document.close()

    }

}
