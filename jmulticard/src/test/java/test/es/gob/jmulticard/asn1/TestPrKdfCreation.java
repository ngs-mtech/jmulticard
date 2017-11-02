package test.es.gob.jmulticard.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import es.gob.jmulticard.asn1.der.pkcs15.PrKdf;
import junit.framework.TestCase;

/** Prueba de creaci&oacute;n de PrKDK PKCS#15.
 * @author Tom&aacute;s Garc&iacute;a-Mer&aacute;s */
public class TestPrKdfCreation extends TestCase {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(TestPrKdfCreation.class.getName());

    private static final int BUFFER_SIZE = 4096;

    private static final String[] TEST_FILES = new String[] {
        "PRKDF_TGM.BER", //$NON-NLS-1$
        "PrKDF_2281552478270226931.der", //$NON-NLS-1$
        "PrKDF_TUI_01.der" //$NON-NLS-1$
    };

    private static final byte[] SAMPLE_PRKDF = new byte[] {
		(byte) 0x30, (byte) 0x43, (byte) 0x30, (byte) 0x0E, (byte) 0x0C, (byte) 0x05, (byte) 0x46, (byte) 0x49,
		(byte) 0x52, (byte) 0x4D, (byte) 0x41, (byte) 0x03, (byte) 0x02, (byte) 0x06, (byte) 0xC0, (byte) 0x04,
		(byte) 0x01, (byte) 0x02, (byte) 0x30, (byte) 0x22, (byte) 0x04, (byte) 0x14, (byte) 0x9B, (byte) 0x99,
		(byte) 0xAF, (byte) 0x11, (byte) 0x22, (byte) 0x81, (byte) 0x57, (byte) 0xCF, (byte) 0xED, (byte) 0x7A,
		(byte) 0x4D, (byte) 0x27, (byte) 0x60, (byte) 0x50, (byte) 0xBE, (byte) 0x9C, (byte) 0x2D, (byte) 0xED,
		(byte) 0x98, (byte) 0x7B, (byte) 0x03, (byte) 0x03, (byte) 0x06, (byte) 0x74, (byte) 0x00, (byte) 0x03,
		(byte) 0x02, (byte) 0x03, (byte) 0xE8, (byte) 0x02, (byte) 0x01, (byte) 0x00, (byte) 0xA0, (byte) 0x02,
		(byte) 0x30, (byte) 0x00, (byte) 0xA1, (byte) 0x09, (byte) 0x30, (byte) 0x07, (byte) 0x30, (byte) 0x02,
		(byte) 0x04, (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x00, (byte) 0x30, (byte) 0x81, (byte) 0xF0,
		(byte) 0x30, (byte) 0x10, (byte) 0x0C, (byte) 0x07, (byte) 0x43, (byte) 0x49, (byte) 0x46, (byte) 0x52,
		(byte) 0x41, (byte) 0x44, (byte) 0x4F, (byte) 0x03, (byte) 0x02, (byte) 0x06, (byte) 0xC0, (byte) 0x04,
		(byte) 0x01, (byte) 0x02, (byte) 0x30, (byte) 0x26, (byte) 0x04, (byte) 0x18, (byte) 0x04, (byte) 0x16,
		(byte) 0x04, (byte) 0x14, (byte) 0x2F, (byte) 0xD8, (byte) 0x79, (byte) 0xDF, (byte) 0x9E, (byte) 0xAC,
		(byte) 0x07, (byte) 0xB1, (byte) 0x00, (byte) 0x04, (byte) 0x0F, (byte) 0xF6, (byte) 0x6C, (byte) 0xD2,
		(byte) 0x27, (byte) 0x1E, (byte) 0xA6, (byte) 0x21, (byte) 0x68, (byte) 0x3A, (byte) 0x03, (byte) 0x03,
		(byte) 0x06, (byte) 0x44, (byte) 0x00, (byte) 0x03, (byte) 0x02, (byte) 0x03, (byte) 0xC0, (byte) 0x02,
		(byte) 0x01, (byte) 0x01, (byte) 0xA0, (byte) 0x81, (byte) 0xA7, (byte) 0x30, (byte) 0x81, (byte) 0xA4,
		(byte) 0x30, (byte) 0x81, (byte) 0xA1, (byte) 0x31, (byte) 0x2F, (byte) 0x30, (byte) 0x2D, (byte) 0x06,
		(byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x03, (byte) 0x0C, (byte) 0x26, (byte) 0x56, (byte) 0x49,
		(byte) 0x43, (byte) 0x45, (byte) 0x4E, (byte) 0x54, (byte) 0x45, (byte) 0x20, (byte) 0x4F, (byte) 0x52,
		(byte) 0x54, (byte) 0x49, (byte) 0x5A, (byte) 0x20, (byte) 0x47, (byte) 0x4F, (byte) 0x4E, (byte) 0x5A,
		(byte) 0x41, (byte) 0x4C, (byte) 0x45, (byte) 0x5A, (byte) 0x20, (byte) 0x2D, (byte) 0x20, (byte) 0x4E,
		(byte) 0x49, (byte) 0x46, (byte) 0x3A, (byte) 0x32, (byte) 0x30, (byte) 0x38, (byte) 0x35, (byte) 0x34,
		(byte) 0x33, (byte) 0x31, (byte) 0x36, (byte) 0x44, (byte) 0x31, (byte) 0x12, (byte) 0x30, (byte) 0x10,
		(byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x05, (byte) 0x13, (byte) 0x09, (byte) 0x32,
		(byte) 0x30, (byte) 0x38, (byte) 0x35, (byte) 0x34, (byte) 0x33, (byte) 0x31, (byte) 0x36, (byte) 0x44,
		(byte) 0x31, (byte) 0x10, (byte) 0x30, (byte) 0x0E, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04,
		(byte) 0x2A, (byte) 0x0C, (byte) 0x07, (byte) 0x56, (byte) 0x49, (byte) 0x43, (byte) 0x45, (byte) 0x4E,
		(byte) 0x54, (byte) 0x45, (byte) 0x31, (byte) 0x17, (byte) 0x30, (byte) 0x15, (byte) 0x06, (byte) 0x03,
		(byte) 0x55, (byte) 0x04, (byte) 0x04, (byte) 0x0C, (byte) 0x0E, (byte) 0x4F, (byte) 0x52, (byte) 0x54,
		(byte) 0x49, (byte) 0x5A, (byte) 0x20, (byte) 0x47, (byte) 0x4F, (byte) 0x4E, (byte) 0x5A, (byte) 0x41,
		(byte) 0x4C, (byte) 0x45, (byte) 0x5A, (byte) 0x31, (byte) 0x13, (byte) 0x30, (byte) 0x11, (byte) 0x06,
		(byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x0B, (byte) 0x0C, (byte) 0x0A, (byte) 0x43, (byte) 0x69,
		(byte) 0x75, (byte) 0x64, (byte) 0x61, (byte) 0x64, (byte) 0x61, (byte) 0x6E, (byte) 0x6F, (byte) 0x73,
		(byte) 0x31, (byte) 0x0D, (byte) 0x30, (byte) 0x0B, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04,
		(byte) 0x0A, (byte) 0x0C, (byte) 0x04, (byte) 0x41, (byte) 0x43, (byte) 0x43, (byte) 0x56, (byte) 0x31,
		(byte) 0x0B, (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x03, (byte) 0x55, (byte) 0x04, (byte) 0x06,
		(byte) 0x13, (byte) 0x02, (byte) 0x45, (byte) 0x53, (byte) 0xA1, (byte) 0x0A, (byte) 0x30, (byte) 0x08,
		(byte) 0x30, (byte) 0x02, (byte) 0x04, (byte) 0x00, (byte) 0x02, (byte) 0x02, (byte) 0x08, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
    };

    /** Prueba la creaci&oacute;n de PrKDK con volcados en disco.
     * @throws Exception en caso de cualquier tipo de error */
    @Test
    public static void testPrKdf() throws Exception {
        byte[] prkdfdata;
        for (final String element : TEST_FILES) {
            prkdfdata = getDataFromInputStream(ClassLoader.getSystemResourceAsStream(element));
            //LOGGER.info("\n\nPrKDF completo (" + Integer.toString(prkdfdata.length) + "):"); //$NON-NLS-1$ //$NON-NLS-2$
            //LOGGER.info(HexUtils.hexify(prkdfdata, true));
            final PrKdf prkdf = new PrKdf();
            Assert.assertNotNull(prkdf);
            prkdf.setDerValue(prkdfdata);
            LOGGER.info("\n" + prkdf.toString() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /** Prueba de an&aacute;lisis de PrKDF de prueba.
     * @throws Exception En cualquier error. */
    @Test
    public static void testSamplePrKdf() throws Exception {
    	final PrKdf prkdf = new PrKdf();
        Assert.assertNotNull(prkdf);
        prkdf.setDerValue(SAMPLE_PRKDF);
        LOGGER.info("\n" + prkdf.toString() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /** Lee un flujo de datos de entrada y los recupera en forma de array de
     * bytes. Este m&eacute;todo consume pero no cierra el flujo de datos de
     * entrada.
     * @param input
     *        Flujo de donde se toman los datos.
     * @return Los datos obtenidos del flujo.
     * @throws IOException
     *         Cuando ocurre un problema durante la lectura */
    private static byte[] getDataFromInputStream(final InputStream input) throws IOException {
        if (input == null) {
            return new byte[0];
        }
        int nBytes = 0;
        final byte[] buffer = new byte[BUFFER_SIZE];
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((nBytes = input.read(buffer)) != -1) {
            baos.write(buffer, 0, nBytes);
        }
        return baos.toByteArray();
    }
}