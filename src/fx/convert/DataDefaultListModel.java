package fx.convert;

import java.util.List;

import javax.swing.DefaultListModel;
 
public class DataDefaultListModel extends DefaultListModel {
	
	private List<String> list = null;

	public DataDefaultListModel() {
		list = DataSrcUtile.getInstance().getHistoryList();
	}
	 
	public int getSize() {
		return list.size();
	}
	
	public Object getElementAt(int index) {
		return list.get(index);
	}
}
