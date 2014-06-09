package fx.convert;

public class Module {
	 
	String name = "";
	String desc = "";
	String subPath = "";
	String copyData = "";
	String convert = "";
	String copyTargetSrv = "";
	String copyTargetClient = "";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubPath() {
		return subPath;
	}
	public void setSubPath(String subPath) {
		this.subPath = subPath;
	}
	public String getCopyData() {
		return copyData;
	}
	public void setCopyData(String copyData) {
		this.copyData = copyData;
	}
	public String getConvert() {
		return convert;
	}
	public void setConvert(String convert) {
		this.convert = convert;
	}
	public String getCopyTargetSrv() {
		return copyTargetSrv;
	}
	public void setCopyTargetSrv(String copyTargetSrv) {
		this.copyTargetSrv = copyTargetSrv;
	}
	public String getCopyTargetClient() {
		return copyTargetClient;
	}
	public void setCopyTargetClient(String copyTargetClient) {
		this.copyTargetClient = copyTargetClient;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
