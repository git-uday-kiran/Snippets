package snippets;

import java.awt.image.BufferedImage;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrCode {

	public static BufferedImage encode(String content, BarcodeFormat format, int width, int height) {
		try {
			BitMatrix matrix = new QRCodeWriter().encode(content, format, width, height);
			return MatrixToImageWriter.toBufferedImage(matrix);
		} catch (WriterException e) {
			System.out.println("Exception while, " + e);
		}

		return null;
	}

	public static String decode(BufferedImage image) {
		QRCodeReader reader = new QRCodeReader();

		LuminanceSource source = new BufferedImageLuminanceSource(image);
		Binarizer binarizer = new HybridBinarizer(source);

		try {
			Result result = reader.decode(new BinaryBitmap(binarizer));
			return result.getText();
		} catch (Exception e) {
			System.out.println("Exception while QR Code is " + e);
		}

		return "";
	}

}
