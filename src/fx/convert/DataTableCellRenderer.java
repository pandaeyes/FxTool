package fx.convert;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
 
public class DataTableCellRenderer extends DefaultTableCellRenderer {
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (isSelected) {
			cell.setForeground(table.getSelectionForeground());
		} else {
			if( column == 0){
				cell.setForeground(Color.GRAY);
			} else {
				cell.setForeground(table.getForeground());
			}
		}
		return cell;
	}
}
