package fx.convert;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

public class Tester {
 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			Process proc = Runtime.getRuntime().exec(new String [] {"cmd", "/c", "test.bat"});
//			Process proc = Runtime.getRuntime().exec("cmd /c cd D:/WorkSpace/Java; dir");
//			Process proc = Runtime.getRuntime().exec("cd ~");
//			InputStream stderr = proc.getInputStream();
//            InputStreamReader isr = new InputStreamReader(stderr);
//            BufferedReader br = new BufferedReader(isr);
//            String line = null; 
//            while ((line = br.readLine()) != null){
//            	System.out.println(line);
//            }
//            int exitVal = proc.waitFor();
//            if(exitVal == 0){
//            	System.out.println("成功");
//            }else{
//            	System.out.println("失败");
//            } 
			System.out.println(URLEncoder.encode("D:\\mhfx\\修仙2\\策划文档\\s数值\\p配置文件", "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
