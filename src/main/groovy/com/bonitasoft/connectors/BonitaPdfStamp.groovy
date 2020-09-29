package com.bonitasoft.connectors

import org.bonitasoft.engine.bpm.document.Document
import org.bonitasoft.engine.bpm.document.DocumentValue
import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;

import groovy.util.logging.Slf4j

import java.nio.file.Files

@Slf4j
class BonitaPdfStamp extends AbstractConnector {

    def static final PDF_INPUT = "pdfDocument"
    def static final STAMP_INPUT = "stamp"
    def static final X_INPUT = "x"
    def static final Y_INPUT = "y"

    def static final RESULT_OUTPUT = "result"

    /**
     * Perform validation on the inputs defined on the connector definition (src/main/resources/bonita-connector-pdf-stamp.def)
     * You should:
     * - validate that mandatory inputs are presents
     * - validate that the content of the inputs is coherent with your use case (e.g: validate that a date is / isn't in the past ...)
     */
    @Override
    void validateInputParameters() throws ConnectorValidationException {
        checkMandatoryDocumentInput(PDF_INPUT)
        checkMandatoryDocumentInput(STAMP_INPUT)
    }

    def checkMandatoryStringInput(inputName) throws ConnectorValidationException {
        def value = getInputParameter(inputName)
        if (value in String) {
            if (!value) {
                throw new ConnectorValidationException(this, "Mandatory parameter '$inputName' is missing.")
            }
        } else {
            throw new ConnectorValidationException(this, "'$inputName' parameter must be a String")
        }
    }

    def checkMandatoryDocumentInput(inputName) throws ConnectorValidationException {
        def value = getInputParameter(inputName)
        if (value in Document) {
            if (!value) {
                throw new ConnectorValidationException(this, "Mandatory parameter '$inputName' is missing.")
            }
        } else {
            throw new ConnectorValidationException(this, "'$inputName' parameter must be a String")
        }
    }
    /**
     * Core method:
     * - Execute all the business logic of your connector using the inputs (connect to an external service, compute some values ...).
     * - Set the output of the connector execution. If outputs are not set, connector fails.
     */
    @Override
    void executeBusinessLogic() throws ConnectorException {
        Document pdfInput = getInputParameter(PDF_INPUT) as Document
        Document stampInput = getInputParameter(STAMP_INPUT) as Document
        int x = getInputParameter(X_INPUT) as int
        int y = getInputParameter(Y_INPUT) as int

        File source = Files.createTempFile(pdfInput.contentFileName, '.pdf').toFile()
        File stamp = Files.createTempFile(pdfInput.contentFileName, '.pdf').toFile()
        source.bytes = apiAccessor.getProcessAPI().getDocumentContent(pdfInput.getContentStorageId())
        stamp.bytes = apiAccessor.getProcessAPI().getDocumentContent(stampInput.getContentStorageId())

        File target = Files.createTempFile(pdfInput.getName(), '.pdf').toFile()


        PdfStamp pdfStamp = new PdfStamp()
        pdfStamp.stampPdfFile(source, target, stamp, x, y)

        DocumentValue result = new DocumentValue(target.bytes, pdfInput.contentMimeType, pdfInput.contentFileName + 'stamped')

        setOutputParameter(RESULT_OUTPUT, result)
    }

    /**
     * [Optional] Open a connection to remote server
     */
    @Override
    void connect() throws ConnectorException {}

    /**
     * [Optional] Close connection to remote server
     */
    @Override
    void disconnect() throws ConnectorException {}
}
