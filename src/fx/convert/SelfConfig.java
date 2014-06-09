package fx.convert;

public class SelfConfig {
 
	private boolean isFinish = false;
	private String php = "";
	private String type = "";
	private String toolPath = "";
	private String serverPath = "";
	private String clientPath = "";
	private String dataPaht = "";
	private boolean hasSvn = false;
	private String os = "win";
	
	public boolean isFinish() {
		return isFinish;
	}
	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}
	public String getPhp() {
		return php;
	}
	public void setPhp(String php) {
		this.php = php;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getToolPath() {
		return toolPath;
	}
	public void setToolPath(String toolPath) {
		this.toolPath = toolPath;
	}
	public String getServerPath() {
		return serverPath;
	}
	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}
	public String getDataPaht() {
		return dataPaht;
	}
	public void setDataPaht(String dataPaht) {
		this.dataPaht = dataPaht;
	}
	public String getClientPath() {
		return clientPath;
	}
	public void setClientPath(String clientPath) {
		this.clientPath = clientPath;
	}
	public boolean isHasSvn() {
		return hasSvn;
	}
	public void setHasSvn(boolean hasSvn) {
		this.hasSvn = hasSvn;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
}
