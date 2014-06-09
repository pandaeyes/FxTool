package fx.convert;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class MainFrame extends JFrame {
	 
	private String title = "数据生成辅助工具";
	private Dimension dimension = new Dimension(750, 500);
	private JTextArea runInfo = new JTextArea();
	private JTextArea cardInfo = new JTextArea();
	private JButton runBut = new JButton("生成数据");
	private JCheckBox svnUpdate = new JCheckBox();
	private JCheckBox copyData = new JCheckBox();
	private JCheckBox copyTarget = new JCheckBox();
	private JList jlist = new JList();
	private int row;
	private int column;
	
	public static void main(String [] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new MainFrame();
	}

	public MainFrame() {
		try {
			DataSrcUtile.getInstance().init();
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();  
			e.printStackTrace(new PrintWriter(sw, true));  
			String str = sw.toString(); 
			GUIUtils.areaAppend(runInfo, str);
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPane = new JPanel();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainPane, BorderLayout.CENTER);
		mainPane.setLayout(new BorderLayout());
		mainPane.add(getInputPane(), BorderLayout.NORTH);
		mainPane.add(getRunInfoPane(), BorderLayout.CENTER);
		setSize(dimension);
		setTitle(title);
		if (!DataSrcUtile.getInstance().getSelfConfig().isHasSvn()) {
			svnUpdate.setSelected(false);
			svnUpdate.setEnabled(false);
		}
		GUIUtils.centerWindow(this);
		setVisible(true);
	}
	
	private JPanel getInputPane() {
		JPanel cardInfoPane = new JPanel();
		cardInfoPane.setLayout(new BorderLayout());
		JPanel inputPane = new JPanel();
		JPanel eastPane = new JPanel();
		JPanel eastPane2 = new JPanel();
		inputPane.setLayout(new BorderLayout());
		eastPane.setLayout(new BorderLayout());
		eastPane2.setLayout(new BorderLayout());
		cardInfoPane.setBorder(BorderFactory.createTitledBorder("数据表列表"));
		cardInfo.setRows(4);
		inputPane.add(getDataListPane(), BorderLayout.CENTER);
		
		JPanel eastWPane = new JPanel();
		JPanel eastCPane = new JPanel();
		eastPane.add(eastWPane, BorderLayout.WEST);
		eastPane.add(eastCPane, BorderLayout.CENTER);
		eastWPane.setLayout(new GridLayout(4, 1, 3, 3));
		eastCPane.setLayout(new GridLayout(4, 1, 3, 3));
		eastWPane.add(new JLabel("SVN update：", JLabel.RIGHT));
		eastWPane.add(new JLabel("Copy Data：", JLabel.RIGHT));
		eastWPane.add(new JLabel("Copy Target：", JLabel.RIGHT));
		eastWPane.add(new JLabel("操作：", JLabel.RIGHT));
		svnUpdate.setSelected(true);
		copyData.setSelected(true);
		copyTarget.setSelected(true);
		eastCPane.add(svnUpdate);
		eastCPane.add(copyData);
		eastCPane.add(copyTarget);
		eastCPane.add(runBut);
		runBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				runBut.setEnabled(false);
				runInfo.setText("");
				Converter converter = new Converter(runInfo, runBut, svnUpdate, copyData, copyTarget, cardInfo, jlist);
				converter.start();
			}
		});
		inputPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		eastPane2.add(eastPane, BorderLayout.NORTH);
		cardInfoPane.add(eastPane2, BorderLayout.EAST);
		cardInfoPane.add(inputPane, BorderLayout.CENTER);
		return cardInfoPane;
	}
	
	private JPanel getRunInfoPane() {
		JPanel runInfoPane = new JPanel();
		runInfoPane.setLayout(new BorderLayout());
		runInfo.setText("输入文件名后请点击写生成数据按钮！");
		runInfo.setEditable(false);
		runInfoPane.add(new JScrollPane(runInfo), BorderLayout.CENTER);
		runInfoPane.setBorder(BorderFactory.createTitledBorder("运行结果"));
		return runInfoPane;
	}
	
	private JPanel getDataListPane() {
		JPanel dataPane = new JPanel();
		dataPane.setLayout(new BorderLayout());
		cardInfo.setColumns(20);
		cardInfo.setRows(11);
		JPanel spPane = new JPanel();
		spPane.setLayout(new BorderLayout(7, 7));
		JPanel delPane = new JPanel();
		delPane.setLayout(new BorderLayout());
		JLabel nLabel = new JLabel("已选列表：", JLabel.LEFT);
		delPane.add(nLabel, BorderLayout.WEST);
		JLabel eLabel = new JLabel("清除 ");
		eLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		eLabel.setForeground(Color.BLUE);
		eLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				cardInfo.setText("");
			}
		});
		delPane.add(eLabel, BorderLayout.EAST);
		spPane.add(delPane, BorderLayout.NORTH);
		spPane.add(new JScrollPane(cardInfo), BorderLayout.CENTER);
		dataPane.add(spPane, BorderLayout.EAST);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.NORTH);
		JPanel allPane = new JPanel();
		JPanel offenPane = new JPanel();
		tabbedPane.addTab("全部", allPane);
		tabbedPane.addTab("常用", offenPane);
		dataPane.add(tabbedPane, BorderLayout.CENTER);
		DataTableModel model = new DataTableModel(DataSrcUtile.getInstance().getMap());
		allPane.setLayout(new BorderLayout());
		offenPane.setLayout(new BorderLayout());
		JTable table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setPreferredScrollableViewportSize(new Dimension(50, 30));
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row =((JTable)e.getSource()).getSelectedRow();
					String name = "" + ((JTable)e.getSource()).getValueAt(row, -1);
					GUIUtils.areaAppend(cardInfo, name.trim());
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key >= KeyEvent.VK_A && key <= KeyEvent.VK_Z) {
					selectedKey((JTable)e.getSource(), key + 32);
				}
			}
		});
		table.setDefaultRenderer(Object.class, new DataTableCellRenderer());
		table.getColumnModel().getColumn(0).setMaxWidth(22);
		table.getColumnModel().getColumn(3).setMaxWidth(30);
//		table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
//			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//				setHorizontalAlignment(JLabel.CENTER);
//				Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//				return cell;
//			}
//		});
		allPane.add(new JScrollPane(table), BorderLayout.CENTER);
		jlist = new JList(new DataDefaultListModel());
		jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		offenPane.add(new JScrollPane(jlist), BorderLayout.CENTER);
		jlist.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if (e.getClickCount() == 2) {
					int row =((JList)e.getSource()).getSelectedIndex();
					String name = "" + ((JList)e.getSource()).getModel().getElementAt(row);
					GUIUtils.areaAppend(cardInfo, name.trim());
				}
			}
		});
		return dataPane;
	}
	
	private void selectedKey(JTable table, int key) {
		int count = table.getRowCount();
		int selected = table.getSelectedRow();
		String name = "";
		int selectindex = -1;
		for (int i = 0; i < count; i++) {
			name = "" + table.getValueAt(i, -1);
			if (name.trim().length() > 0) {
				char c = name.trim().charAt(0);
				if (c == key) {
					if ((selected + 1) == i) {
						table.getSelectionModel().setSelectionInterval(i, i);
						Rectangle rect = table.getCellRect(i + 1, 0, true);
						table.scrollRectToVisible(rect);
						return;
					} else {
						if (selectindex == -1) {
							selectindex = i;
						}
					}
				}
			}
		}
		if (selectindex != -1) {
			table.getSelectionModel().setSelectionInterval(selectindex, selectindex);
			Rectangle rect = table.getCellRect(selectindex, 0, true);
			table.scrollRectToVisible(rect);
		}
	}
}
