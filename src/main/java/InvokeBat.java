import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class InvokeBat {
    public void runbat(String batName) {
        try {
            Process ps = Runtime.getRuntime().exec(batName);
            InputStream in = ps.getInputStream();
            int c;
            while ((c = in.read()) != -1) {
//                System.out.print(c);// 如果你不需要看输出，这行可以注销掉
            }
            in.close();
            ps.waitFor();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("child thread done");
    }
    
    public static void creatBat(String command, String batURL) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(batURL);
			fw.write(command);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
		}
	}

    public static void main(String[] args) {
    	
    	String path = "D:\\jac_scl";//\\auto_oracl_bak
    	File file =new File(path);    
    	//如果文件夹不存在则创建    
    	if  (!file .exists()  && !file .isDirectory())      
    	{       
    	    System.out.println("//不存在");  
    	    file .mkdir();    
    	} else   
    	{  
    	    System.out.println("//目录存在");  
    	}  
    	
    	path = "D:\\jac_scl\\auto_oracl_bak";
    	file =new File(path);    
    	//如果文件夹不存在则创建    
    	if  (!file .exists()  && !file .isDirectory())      
    	{       
    	    System.out.println("//不存在");  
    	    file .mkdir();    
    	} else   
    	{  
    	    System.out.println("//目录存在");  
    	} 
    	
    	/*String batName = path+"\\exp_exception_log.bat";
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("@echo on").append(JavaTools.enter);
    	sb.append("set d=%DATE:~0,4%%DATE:~5,2%%DATE:~8,2%").append(JavaTools.enter);
    	sb.append("exp jac_scl/jac_scl@192.168.10.92:1521/ORCL file=D:/jac_scl/auto_oracl_bak/EXCEPTION_LOG_LESS20151203.dmp ");
    	sb.append("log=D:\\jac_scl\\auto_oracl_bak\\EXCEPTION_LOG_LESS20151203.log ");
    	sb.append("tables=EXCEPTION_LOG_LESS20151203").append(JavaTools.enter);
    	sb.append("@echo on").append(JavaTools.enter);
    	sb.append("zip -m D:/jac_scl/auto_oracl_bak/EXCEPTION_LOG_LESS20151203.zip ");
    	sb.append("D:/jac_scl/auto_oracl_bak/EXCEPTION_LOG_LESS20151203.dmp");
		creatBat(sb.toString(),batName);*/
    	
//    	InvokeBat test1 = new InvokeBat();
//        test1.runbat(batName);
//        System.out.println("main thread");
    }
}
