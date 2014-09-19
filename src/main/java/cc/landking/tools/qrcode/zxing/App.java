package cc.landking.tools.qrcode.zxing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.zxing.WriterException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, WriterException, InterruptedException
    {
    	String text = "http://landking.cc/search.php?q=经测试，中间小图片尺寸占二维码总宽度的2/7比较合适，既满足了图片的清晰度，又不影响二维码的加密数据。";
    	InputStream logo = App.class.getClassLoader().getResourceAsStream("logo.png");
    	File file = new File("qrcode.png");
    	((QrcodeEncoder)QrcodeEncoder.withLogo(text, logo).withSize(156, 156)).writeToFileWithLogo(file);
    	QrcodeEncoder.from(text).writeToStream(new FileOutputStream("qrcode1.png"));
    	Thread.sleep(2000);
        System.out.println( QrcodeEncoder.decode(new FileInputStream("qrcode.png")) );
    }
}
