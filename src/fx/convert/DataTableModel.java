package fx.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

 
public class DataTableModel extends AbstractTableModel {
	
	private List<Module> list = null;
	
	public DataTableModel(HashMap<String, Module> map) {
		this.list = toList(map);
	}
	
	public String getColumnName(int col){
		switch (col) {
		case 0:
			return "";
		case 1:
			return "模块名";
		case 2:
			return "备注";
		default:
			return "";
		}
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Module mod = list.get(rowIndex);
		if (columnIndex == 0) {
			return rowIndex + 1;
		} else if (columnIndex == 1 || columnIndex == -1) {
			return mod.getName();
		} else if (columnIndex == 2) { 
			return mod.getDesc();
		} else {
			if (!GUIUtils.isNull(mod.getCopyTargetClient()) && !GUIUtils.isNull(mod.getCopyTargetSrv())) {
				return "Both";
			} else if (!GUIUtils.isNull(mod.getCopyTargetSrv())) {
				return "Srv";
			} else {
				return "Cli";
			}
		}
	}
	
	private List<Module> toList(HashMap<String, Module> map) {
		List<Module> list = new ArrayList<Module>();
		for (String key : map.keySet()) {
			list.add(map.get(key));
		}
		ModuleComparator comparator = new ModuleComparator();
		Collections.sort(list, comparator);
		return list;
	}
}

class ModuleComparator implements Comparator {
	public int compare(Object arg0, Object arg1) {
		if (arg0 instanceof Module && arg1 instanceof Module) {
			return ((Module)arg0).getName().compareTo(((Module)arg1).getName());
		} else {
			return 0;
		}
	}
}
