package fx.convert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JTextArea;

public class CmdExecuter {

	private static CmdExecuter exec = null;
	private JTextArea runInfo = null;
	 
	private CmdExecuter(JTextArea runInfo) {
		this.runInfo = runInfo;
	}

	public static CmdExecuter getInstance(JTextArea runInfo) {
		if (exec == null) {
			exec = new CmdExecuter(runInfo);
		}
		return exec;
	}
	
	public void exec(String cmds, String charsetName) throws Exception {
		Process proc = Runtime.getRuntime().exec(cmds);
		InputStream isinfo = proc.getInputStream();
		InputStream iserr = proc.getErrorStream();
        InputStreamReader isr = new InputStreamReader(isinfo, charsetName);
        BufferedReader br = new BufferedReader(isr);
        
        InputStreamReader isrerr = new InputStreamReader(iserr, charsetName);
        BufferedReader brerr = new BufferedReader(isrerr);
        String line = null; 
        while ((line = br.readLine()) != null){
        	GUIUtils.areaAppend(runInfo, line);
        }
        int exitVal = proc.waitFor();
        while ((line = brerr.readLine()) != null){
        	exitVal = 1;
        	GUIUtils.areaAppend(runInfo, "ERROR:" + line);
        }
        if(exitVal == 0){
        	GUIUtils.areaAppend(runInfo, "执行成功");
        }else{
        	GUIUtils.areaAppend(runInfo, "执行失败");
        } 
	}
}
