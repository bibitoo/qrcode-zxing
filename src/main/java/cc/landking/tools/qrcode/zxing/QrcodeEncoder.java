package cc.landking.tools.qrcode.zxing;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import net.glxn.qrgen.core.AbstractQRCode;
import net.glxn.qrgen.core.exception.QRGenerationException;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.core.vcard.VCard;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrcodeEncoder  extends AbstractQRCode{
	boolean rgb = true;
	BufferedImage logo;
	protected String text;
	  public static final int BLACK = 0xFF000000;
	  public static final int WHITE = 0xFFFFFFFF;
	  
	  private  int color;
	  private  int bgColor;
	  
	public QrcodeEncoder(String text){
		this(text,null);
	}
	public QrcodeEncoder(String text, BufferedImage logo){

		 this(text,logo,BLACK, WHITE);
	}
	public QrcodeEncoder(String text, BufferedImage logo, int color, int bgColor){
		this.text = text;
		this.logo = logo;
		this.color = color;
		this.bgColor = bgColor;
		 qrWriter = new QRCodeWriter();
		 hints.put(EncodeHintType.MARGIN, 1);
		 hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		 hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
	}
	
	public static void overlapImage(BufferedImage image, OutputStream imgSavePath,
			InputStream logoPath, String format) {
		try {
		BufferedImage logo  = ImageIO.read(logoPath);
		overlapImage(image,imgSavePath,logo,format);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
		public static void overlapImage(BufferedImage image, OutputStream imgSavePath,
				BufferedImage logo, String format) {
		try {
			
			Graphics2D g = image.createGraphics();
			int width = image.getWidth() /5;
			int height = image.getHeight() / 5;
			int x = (image.getWidth() - width) / 2;
			int y = (image.getHeight() - height) / 2;
			g.drawImage(logo, x, y, width, height, null);
			g.dispose();
			ImageIO.write(image, format, imgSavePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void createQrCode(String content, int width, int height,
			OutputStream imageSavePath, InputStream logoPath, String formate,
			int fontColor, int bgColor) throws IOException {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.MARGIN, 1);
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		BitMatrix matrix = null;
		try {
			matrix = new MultiFormatWriter().encode(content,
					BarcodeFormat.QR_CODE, width, height, hints);
			writeToStreamWithLogo(matrix, formate, imageSavePath, logoPath, fontColor,
					bgColor);
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
	public  void writeToFileWithLogo( 
			File imageSavePath)
			throws IOException {

		FileOutputStream file = new FileOutputStream(imageSavePath);
		writeToStreamWithLogo( file);
	}
	public  void writeToFileWithLogo( 
			File imageSavePath, InputStream logoPath)
			throws IOException {

		FileOutputStream file = new FileOutputStream(imageSavePath);
		writeToStreamWithLogo( file,logoPath);
	}
	public  void writeToFileWithLogo( String format,
			File imageSavePath, InputStream logoPath)
			throws IOException {

		FileOutputStream file = new FileOutputStream(imageSavePath);
		writeToStreamWithLogo(format, file,logoPath);
	}

	public  void writeToFileWithLogo( String format,
			File imageSavePath, InputStream logoPath, int fontColor, int bgColor)
			throws IOException {

		FileOutputStream file = new FileOutputStream(imageSavePath);
		writeToStreamWithLogo(format, file,logoPath,fontColor,bgColor);
	}
	public  void writeToStreamWithLogo(OutputStream imageSavePath)
			throws IOException {
		writeToStreamWithLogo(ImageType.PNG.name().toLowerCase(),imageSavePath,null,color,bgColor);
	}
	public  void writeToStreamWithLogo(OutputStream imageSavePath, InputStream logoPath)
			throws IOException {
		writeToStreamWithLogo(ImageType.PNG.name().toLowerCase(),imageSavePath,logoPath,color,bgColor);
	}
	public  void writeToStreamWithLogo( String format,
			OutputStream imageSavePath, InputStream logoPath)
			throws IOException {
		writeToStreamWithLogo(format,imageSavePath,logoPath,color,bgColor);
	}
	public  void writeToStreamWithLogo(String format,
			OutputStream imageSavePath, InputStream logoPath, int fontColor, int bgColor)
			throws IOException {
		BitMatrix matrix = null;
		try {
			matrix = new MultiFormatWriter().encode(text,
					BarcodeFormat.QR_CODE, width, height, hints);
			writeToStreamWithLogo(matrix, format, imageSavePath, logoPath, fontColor,
					bgColor);
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
	public  void writeToStreamWithLogo(BitMatrix matrix, String format,
			OutputStream imageSavePath, InputStream logoPath, int fontColor, int bgColor)
			throws IOException {
		this.color = fontColor;
		this.bgColor = bgColor;
		if(logoPath != null){
			 logo  = ImageIO.read(logoPath);
		}
		BufferedImage image = write2File(matrix, format);
		if(logo != null){
		overlapImage(image, imageSavePath, logo,
				format);
		}else{
			if (!ImageIO.write(image, format,imageSavePath) ){
				throw new IOException((new StringBuilder(
						"Could not write an image of format ")).append(format)
						.append(" to ").toString());
			} 
		}
	}
	public  BufferedImage write2File(BitMatrix matrix, String format)
			throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		
			return image;
	}


	public  void writeToStream(BitMatrix matrix, String format,
			OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException((new StringBuilder(
					"Could not write an image of format ")).append(format)
					.toString());
		}
	}
	int getBufferedImageColorModel() {

		if(rgb){
			return BufferedImage.TYPE_INT_RGB;
		}
	    // Use faster BINARY if colors match default
	    return color == BLACK && bgColor == WHITE ? BufferedImage.TYPE_BYTE_BINARY : BufferedImage.TYPE_INT_RGB;
	  }
	public  BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height,
				getBufferedImageColorModel());
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? color : bgColor);
			}
		}
		return image;
	}
	
	public static  QrcodeEncoder withLogo(String text , InputStream logo )throws IOException{
			return new QrcodeEncoder(text,  ImageIO.read(logo)) ;
	}

	   public static QrcodeEncoder  from(String text) {
	        return new QrcodeEncoder(text);
	    }

	    /**
	     * Creates a a QR Code from the given {@link VCard}.
	     * <p/>
	     * The QRCode will have the following defaults:     <br/> {size: 100x100}<br/>{imageType:PNG}  <br/><br/>
	     *
	     * @param vcard the vcard to encode as QRCode
	     * @return the QRCode object
	     */
	    public static AbstractQRCode from(VCard vcard) {
	        return new QrcodeEncoder(vcard.toString());
	    }

	    @Override
	    public File file() {
	        File file;
	        try {
	            file = createTempFile();
	            MatrixToImageWriter.writeToPath(createMatrix(text), imageType.toString(), file.toPath());
	        } catch (Exception e) {
	            throw new QRGenerationException("Failed to create QR image from text due to underlying exception", e);
	        }

	        return file;
	    }

	    @Override
	    public File file(String name) {
	        File file;
	        try {
	            file = createTempFile(name);
	            MatrixToImageWriter.writeToPath(createMatrix(text), imageType.toString(), file.toPath());
	        } catch (Exception e) {
	            throw new QRGenerationException("Failed to create QR image from text due to underlying exception", e);
	        }

	        return file;
	    }

	    @Override
	    protected void writeToStream(OutputStream stream) throws IOException, WriterException {
	        MatrixToImageWriter.writeToStream(createMatrix(text), imageType.toString(), stream);
	    }
	    
	    @SuppressWarnings({ "unchecked", "finally" })
		public static String decode(InputStream imgPath) {
			String content = "";
			try {

				BufferedImage image = null;
				try {
					image = ImageIO.read(imgPath);
					if (null == image) {
						return content;
					}
					LuminanceSource source = new BufferedImageLuminanceSource(image);
					BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
							source));
					@SuppressWarnings("rawtypes")
					Hashtable hints = new Hashtable();
					hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
					hints.put(EncodeHintType.ERROR_CORRECTION,
							ErrorCorrectionLevel.H);
					Result result = new MultiFormatReader().decode(bitmap, hints);
					content = result.getText();
				} catch (IOException e1) {
					e1.printStackTrace();
					content = "";
				} catch (ReaderException e2) {
					e2.printStackTrace();
					content = "";
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				return content;
			}
		}
}
