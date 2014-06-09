package fx.convert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
 

public class DataSrcUtile {
 
	private static DataSrcUtile dataSrc = null;
	private SelfConfig selfConfig = null;
	private HashMap<String, Module> map = new HashMap<String, Module>();
	private List<String> historyList = new ArrayList<String>();
	
	private DataSrcUtile() {
		selfConfig = new SelfConfig();
	}
	
	public static DataSrcUtile getInstance() {
		if (dataSrc == null) {
			dataSrc = new DataSrcUtile();
		}
		return dataSrc;
	}
	
	public void init() throws Exception {
		xmlParse();
		readConfig();
		readHistory();
	}
	
	private void xmlParse() {
		File file = new File("mod_config.xml");
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder; 
		Document document = null; 
		try {
			builder = builderFactory.newDocumentBuilder();
		    document = builder.parse(file);
		    Element root = document.getDocumentElement();
		    NodeList list = root.getChildNodes();
		    int size = list.getLength();
		    for (int i = 0; i < size; i++) {
		    	Module mod = new Module();
		    	Node enode = list.item(i);
		    	if (enode instanceof Element && enode.getNodeName() == "module") {
			    	Element node = (Element)list.item(i);
			    	mod.setName(node.getAttribute("name"));
			    	mod.setDesc(node.getAttribute("desc"));
			    	NodeList itemList = node.getChildNodes();
			    	for (int j = 0; j < itemList.getLength(); j++) {
			    		Node itemNod = itemList.item(j);
			    		if (itemNod instanceof Element) {
			    			Element itemNode = (Element)itemNod;
				    		String nodeName = itemNode.getNodeName();
				    		if (nodeName == "subPath") {
				    			mod.setSubPath(itemNode.getAttribute("path").trim());
				    		} else if (nodeName == "cmd_copy_data") {
				    			mod.setCopyData(itemNode.getTextContent().trim());
				    		} else if (nodeName == "cmd_convert") {
				    			mod.setConvert(itemNode.getTextContent().trim());
				    		} else if (nodeName == "cmd_copy_target_server") {
				    			mod.setCopyTargetSrv(itemNode.getTextContent().trim());
				    		} else if (nodeName == "cmd_copy_target_client") {
				    			mod.setCopyTargetClient(itemNode.getTextContent().trim());
				    		}
			    		}
			    	}
			    	map.put(mod.getName(), mod);
		    	}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void readConfig() throws Exception {
		File file = new File("selfconfig.xml");
		if (file.isFile()) {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			Document document = null; 
			builder = builderFactory.newDocumentBuilder();
		    document = builder.parse(file);
		    Element root = document.getDocumentElement();
		    NodeList list = root.getChildNodes();
		    selfConfig.setFinish(true);
		    for (int i =0; i < list.getLength(); i++) {
		    	if (list.item(i) instanceof Element) {
		    		Element ele = (Element)list.item(i);
		    		if (ele.getTagName().equalsIgnoreCase("php")) {
		    			selfConfig.setPhp(ele.getAttribute("value"));
		    		} else if (ele.getTagName().equalsIgnoreCase("type")) {
		    			selfConfig.setType(ele.getAttribute("value"));
		    		} else if (ele.getTagName().equalsIgnoreCase("hassvn")) {
		    			if (ele.getAttribute("value").equalsIgnoreCase("true")) {
		    				selfConfig.setHasSvn(true);
		    			} else {
		    				selfConfig.setHasSvn(false);
		    			}
		    		} else if (ele.getTagName().equalsIgnoreCase("toolpath")) {
		    			selfConfig.setToolPath(ele.getAttribute("value"));
		    		} else if (ele.getTagName().equalsIgnoreCase("serverpath")) {
		    			selfConfig.setServerPath(ele.getAttribute("value"));
		    		} else if (ele.getTagName().equalsIgnoreCase("clientpath")) {
		    			selfConfig.setClientPath(ele.getAttribute("value"));
		    		} else if (ele.getTagName().equalsIgnoreCase("datapath")) {
		    			selfConfig.setDataPaht(ele.getAttribute("value"));
		    		} else if (ele.getTagName().equalsIgnoreCase("os")) {
		    			selfConfig.setOs(ele.getAttribute("value"));
		    		}
		    	}
		    }
		} else {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
			BufferedWriter confBf =new BufferedWriter(write);
			confBf.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
			confBf.write("<config>\r\n");
			confBf.write("    <php value = \"php\"/>\r\n");
			confBf.write("    <os value = \"win\"/>\r\n");
			confBf.write("    <!-- 用户类型 server or client -->\r\n");
			confBf.write("    <type value = \"server\"/>\r\n");
			confBf.write("    <hassvn value = \"false\"/>\r\n");
			confBf.write("    <toolpath value = \"D:\\mhfx\\tools\"/>\r\n");
			confBf.write("    <serverpath value = \"D:\\mhfx\\server\"/>\r\n");
			confBf.write("    <clientpath value = \"D:\\mhfx\\resources\"/>\r\n");
			confBf.write("    <!-- 指定到 /p配置文件 目录 -->\r\n");
			confBf.write("    <datapath value = \"" + URLDecoder.decode("D%3A%5Cmhfx%5C%E4%BF%AE%E4%BB%992%5C%E7%AD%96%E5%88%92%E6%96%87%E6%A1%A3%5Cs%E6%95%B0%E5%80%BC%5Cp%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6", "UTF-8") + "\"/>\r\n");
			confBf.write("</config>\r\n");
			confBf.close();
			write.close();
		}
	}
	
	private void readHistory() throws Exception {
		File file = new File(".tool_history");
		if (file.isFile()) {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader br = new BufferedReader(reader);
			String line = null;
			while ((line = br.readLine()) != null){
				if (line.trim().length() > 0 && map.get(line.trim()) != null) {
					historyList.add(line.trim());
				}
            }
			br.close();
			reader.close();
		}
	}

	public SelfConfig getSelfConfig() {
		return selfConfig;
	}
	
	public Module getModule(String name) {
		return map.get(name);
	}
	
	public HashMap<String, Module> getMap() {
		return map;
	}
	
	public List<String> getHistoryList() {
		return historyList;
	}
}
