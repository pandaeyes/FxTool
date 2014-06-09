package fx.convert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
 
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Converter extends Thread {

	private JTextArea runInfo = null;
	private JButton runBut = null;
	private JCheckBox svnUpdate = null;
	private JCheckBox copyData = null;
	private JCheckBox copyTarget = null;
	private JTextArea cardInfo = null;
	private JList jlist = null;
	
	public Converter(JTextArea runInfo, JButton runBut, JCheckBox svnUpdate, JCheckBox copyData, JCheckBox copyTarget, JTextArea cardInfo, JList jlist) {
		this.runInfo = runInfo;
		this.runBut = runBut;
		this.svnUpdate = svnUpdate;
		this.copyData = copyData;
		this.copyTarget = copyTarget;
		this.cardInfo = cardInfo;
		this.jlist = jlist;
	}
	 
	public void run() {
		try {
			do_run();
			runBut.setEnabled(true);
			writeHistory(cardInfo.getText());
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();  
            e.printStackTrace(new PrintWriter(sw, true));  
            String str = sw.toString(); 
            GUIUtils.areaAppend(runInfo, str);
		}
	}
	public void do_run() throws Exception{
		SelfConfig selfConfig = DataSrcUtile.getInstance().getSelfConfig();
		List<Module> list = new ArrayList<Module>();
		String moduleStr = cardInfo.getText();
		String [] modList = moduleStr.split("[\\r\\n]+");
		boolean notfind = false;
		String notFindMod = "";
		for (int i = 0; i < modList.length; i++) {
			String name = modList[i];
			if (name.trim().length() > 0) {
				if (DataSrcUtile.getInstance().getModule(name) != null) {
					list.add(DataSrcUtile.getInstance().getModule(name));
				} else {
					notfind = true;
					notFindMod += " " + name;
				}
			}
		}
		if (notfind) {
			JOptionPane.showMessageDialog(cardInfo,"没有找到配置信息:" + notFindMod);
		} else if (list.size() > 0){
			if ("linux".equalsIgnoreCase(selfConfig.getOs())) {
				makeFileForLinux(selfConfig, list);
			} else {
				makeFileForWin(selfConfig, list);
			}
			if ("linux".equalsIgnoreCase(selfConfig.getOs())) {
				CmdExecuter.getInstance(runInfo).exec("sh _convert_tmp.sh", "UTF-8");
			} else {
				CmdExecuter.getInstance(runInfo).exec("cmd /c _convert_tmp.bat", "GB2312");
			}
		}
	}
	
	private void makeFileForWin(SelfConfig selfConfig, List<Module> list) throws Exception {
		File file = new File("_convert_tmp.bat");
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"GB2312");
		BufferedWriter confBf =new BufferedWriter(write);
		confBf.write("@echo off\r\n");
		confBf.write("echo 正在生成数据文件\r\n");
		confBf.write("\r\n");
		confBf.write("rem 个人环境\r\n");
		confBf.write("set PHP=" + selfConfig.getPhp() + "\r\n");
		confBf.write("set TOOLPATH=" + selfConfig.getToolPath() + "\r\n");
		confBf.write("set SRVPATH=" + selfConfig.getServerPath() + "\r\n");
		confBf.write("set CLIENTPATH=" + selfConfig.getClientPath() + "\r\n");
		confBf.write("set DATAPATH=" + selfConfig.getDataPaht() + "\r\n");
		confBf.write("\r\n");
		confBf.write("cd /d %DATAPATH%\r\n");
		confBf.write("\r\n"); 
		if (selfConfig.isHasSvn() && svnUpdate.isSelected()) {
			confBf.write("rem svn\r\n");
			confBf.write("svn update\r\n");
			confBf.write("\r\n");
		}
		if (copyData.isSelected()) {
			confBf.write("rem copy data\r\n");
			for (Module module : list) {
				String [] copyCmdList = module.getCopyData().split("\n");
				if (copyCmdList != null && copyCmdList.length > 1) {
					for (String copyCmd : copyCmdList) {
						confBf.write(copyCmd.trim() + "\r\n");
					}
				} else {
					confBf.write(module.getCopyData() + "\r\n");
				}
			}
			confBf.write("\r\n");
		}
		confBf.write("rem convert\r\n");
		confBf.write("cd /d %TOOLPATH%\\convert\r\n");
		for (Module module : list) {
			confBf.write(module.getConvert() + "\r\n");
		}
		if (copyTarget.isSelected()) {
			confBf.write("\r\n");
			confBf.write("rem copy target\r\n");
			if (selfConfig.getType().equals("server")) {
				for (Module module : list) {
					confBf.write(module.getCopyTargetSrv() + "\r\n");
				}
			} else if (selfConfig.getType().equals("client")) {
				for (Module module : list) {
					confBf.write(module.getCopyTargetClient() + "\r\n");
				}
			}
			confBf.write("\r\n");
		}
		confBf.write("\r\n");
		confBf.write("echo finish\r\n");
		confBf.close();
		write.close();
	}
	
	private void makeFileForLinux(SelfConfig selfConfig, List<Module> list) throws Exception {
		File file = new File("_convert_tmp.sh");
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
		BufferedWriter confBf =new BufferedWriter(write);
		confBf.write("#/bin/bash");
		confBf.write("# 服务端数据生成bash版本\n");
		confBf.write("\n");
		confBf.write("PHP=" + GUIUtils.convertToLinux(selfConfig.getPhp()) + "\n");
		confBf.write("TOOLPATH=" + GUIUtils.convertToLinux(selfConfig.getToolPath()) + "\n");
		confBf.write("SRVPATH=" + GUIUtils.convertToLinux(selfConfig.getServerPath()) + "\n");
		confBf.write("CLIENTPATH=" + GUIUtils.convertToLinux(selfConfig.getClientPath()) + "\n");
		confBf.write("DATAPATH=" + GUIUtils.convertToLinux(selfConfig.getDataPaht()) + "\n");
		confBf.write("\n");
		confBf.write("cd ${DATAPATH}\n");
		confBf.write("\n"); 
		if (selfConfig.isHasSvn() && svnUpdate.isSelected()) {
			confBf.write("# svn\n");
			confBf.write("svn update\n");
			confBf.write("\n");
		}
		if (copyData.isSelected()) {
			confBf.write("# copy data\n");
			for (Module module : list) {
				confBf.write(GUIUtils.convertToLinux(module.getCopyData()) + "\n");
			}
			confBf.write("\n");
		}
		confBf.write("# convert\n");
		confBf.write("cd ${TOOLPATH}/convert\n");
		for (Module module : list) {
			confBf.write(GUIUtils.convertToLinux(module.getConvert()) + "\n");
		}
		if (copyTarget.isSelected()) {
			confBf.write("\n");
			confBf.write("# copy target\n");
			if (selfConfig.getType().equals("server")) {
				for (Module module : list) {
					confBf.write(GUIUtils.convertToLinux(module.getCopyTargetSrv()) + "\n");
				}
			} else if (selfConfig.getType().equals("client")) {
				for (Module module : list) {
					confBf.write(GUIUtils.convertToLinux(module.getCopyTargetClient()) + "\n");
				}
			}
			confBf.write("\n");
		}
		confBf.write("\n");
		confBf.write("echo finish\n");
		confBf.close();
		write.close();
	}
	
	private void writeHistory(String runinfo) throws Exception {
		List<String> list = DataSrcUtile.getInstance().getHistoryList();
		String moduleStr = cardInfo.getText();
		String [] modList = moduleStr.split("[\\r\\n]+");
		for (int i = 0; i < modList.length; i++) {
			String name = modList[i];
			if (name.trim().length() > 0) {
				list.remove(name.trim());
			}
		}
		File file = new File(".tool_history");
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
		BufferedWriter confBf =new BufferedWriter(write);
		for (int i = 0; i < modList.length; i++) {
			String name = modList[i];
			if (name.trim().length() > 0) {
				list.add(0, name.trim());
			}
		}
		for (int i = 0; i < list.size(); i++) {
			confBf.write(list.get(i) + "\r\n");
		}
		jlist.updateUI();
		confBf.close();
		write.close();
	}
}
