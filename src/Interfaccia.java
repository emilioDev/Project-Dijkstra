import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import javax.swing.*;

public class Interfaccia extends JFrame{
	
	private JTable tbl = new JTable();
	private DefaultTableModel dtm = new DefaultTableModel(0, 0){
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	private JScrollPane scpTable = new JScrollPane();

	private JPanel pnlNorth = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
	private JPanel pnlGlobalCenter = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
	private JPanel pnlCenter = new JPanel(new GridLayout(4,4,10,3));
	private JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

	private JLabel lblNodi = new JLabel("Set number of routers");
	private JTextField txtNodi = new JTextField(5);
	private JButton btnNodi = new JButton("Enter");

	private JLabel lblFrom = new JLabel("From");
	private JTextField txtInputFrom = new JTextField(5);
	private JLabel lblTo = new JLabel("To");
	private JTextField txtInputTo = new JTextField(5);
	private JLabel lblDistance = new JLabel("Distance");
	private JTextField txtInputDistance = new JTextField(5);
	private JButton btnEntry = new JButton("Add entry");

	private JButton btnCalc = new JButton("Calculate");
	private JButton btnCancel = new JButton("Cancel");

	private JLabel lblNodoRadice = new JLabel("Root node");
	private JTextField txtNodoRadice = new JTextField(5);

	private MatriceAdiacenze mtrDcz;

	final Font globalFont = new Font("Arial", Font.BOLD, 16);

	public Interfaccia(){
		super("Dijkstra");

		pnlNorth.setBackground(Color.cyan);
		pnlCenter.setBackground(Color.cyan);
		pnlGlobalCenter.setBackground(Color.cyan);
		pnlSouth.setBackground(Color.cyan);

		lblDistance.setFont(globalFont);
		lblFrom.setFont(globalFont);
		lblNodi.setFont(globalFont);
		lblTo.setFont(globalFont);


		txtNodi.setHorizontalAlignment(JTextField.CENTER);
		txtNodi.setFont(globalFont);
		pnlNorth.add(lblNodi);
		pnlNorth.add(txtNodi);
		pnlNorth.add(btnNodi);
		add(pnlNorth, BorderLayout.NORTH);
		
		initPanelCenter();

		dtm.setColumnIdentifiers(new String[]{
				"From", "To", "Distance"});
		tbl.setModel(dtm);

		scpTable.getViewport().add(tbl);
		pnlSouth.add(scpTable);

		add(pnlSouth, BorderLayout.SOUTH);

		/*** problema con il l'aquisizione del numero di nodi ***/

		btnNodi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setNumNodi();
			}
		});

		txtNodi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setNumNodi();
			}
		});

		/********************************************************/

		btnEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setNewEntry();
			}
		});

		txtInputDistance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setNewEntry();
			}
		});

		btnCalc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calcSinkTree();
			}
		});

		txtNodoRadice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				calcSinkTree();
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tbl.isRowSelected(tbl.getSelectedRow())){
					dtm.removeRow(tbl.getSelectedRow());
				}
				else {
					txtNodi.setEditable(true);
					txtNodi.setText("");
					btnNodi.setEnabled(true);
					txtInputTo.setText("");
					txtInputFrom.setText("");
					txtInputDistance.setText("");
					txtNodoRadice.setText("");
					dtm.setRowCount(0);
				}
			}
		});

		setResizable(false);
		setBounds(450, 40, 540, 680);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void setNumNodi(){
		Integer numNodiTxt = null;
		try{
			numNodiTxt = Integer.parseInt(txtNodi.getText());
		}catch (NumberFormatException w){
			w.printStackTrace();
		}
		if (numNodiTxt.intValue() > Integer.valueOf(1) && numNodiTxt instanceof Integer) {
			try {
				mtrDcz = new MatriceAdiacenze(Integer.parseInt(txtNodi.getText()));
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
		}
		if (mtrDcz instanceof MatriceAdiacenze) {
			btnNodi.setEnabled(false);
			txtNodi.setEditable(false);
		}
	}

	private void setNewEntry(){
		if (!txtInputFrom.getText().equals("") && !txtInputTo.getText().equals("") && !txtInputDistance.getText().equals("")
				&& mtrDcz instanceof MatriceAdiacenze) {
			dtm.addRow(new Object[]{
					txtInputFrom.getText(), txtInputTo.getText(), txtInputDistance.getText()
			});
			try {
				mtrDcz.addVertex(new Vertex(Integer.parseInt(txtInputFrom.getText()),
						Integer.parseInt(txtInputTo.getText()),
						Integer.parseInt(txtInputDistance.getText())));
			}
			catch (NumberFormatException ex){
				ex.printStackTrace();
			}
			txtInputFrom.setText("");
			txtInputTo.setText("");
			txtInputDistance.setText("");
		}
	}

	private void calcSinkTree(){
		Integer nodoRadiceValore = null;
		try {
			nodoRadiceValore = Integer.parseInt(txtNodoRadice.getText());
		}
		catch (NumberFormatException ex){
			ex.printStackTrace();
		}
		if(nodoRadiceValore instanceof Integer && mtrDcz instanceof MatriceAdiacenze) {
			int[] sinkTree = mtrDcz.dijkstra(nodoRadiceValore);
			String outputSinkTree = "From      To         Distance\n";
			for (int i = 0; i < Integer.parseInt(txtNodi.getText()); i++) {
				if (sinkTree[i] == Integer.MAX_VALUE)
					outputSinkTree += txtNodoRadice.getText() + "             " + (i + 1) + "           " + "\u221E \n";
				else
					outputSinkTree += txtNodoRadice.getText() + "             " + (i + 1) + "           " + sinkTree[i] + "\n";
			}

			JOptionPane.showMessageDialog(null,
					outputSinkTree,
					"SinkTree",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}


	private void initPanelCenter(){

		lblNodoRadice.setFont(globalFont);

		txtNodoRadice.setFont(globalFont);
		txtNodoRadice.setHorizontalAlignment(JTextField.CENTER);

		pnlCenter.add(lblFrom);

		txtInputFrom.setHorizontalAlignment(JTextField.CENTER);
		txtInputFrom.setFont(globalFont);
		pnlCenter.add(txtInputFrom);
		pnlCenter.add(lblNodoRadice);
		pnlCenter.add(txtNodoRadice);

		pnlCenter.add(lblTo);
		txtInputTo.setHorizontalAlignment(JTextField.CENTER);
		txtInputTo.setFont(globalFont);
		pnlCenter.add(txtInputTo);
		pnlCenter.add(new JLabel());
		pnlCenter.add(new JLabel());


		pnlCenter.add(lblDistance);
		txtInputDistance.setHorizontalAlignment(JTextField.CENTER);
		txtInputDistance.setFont(globalFont);
		pnlCenter.add(txtInputDistance);
		pnlCenter.add(new JLabel());
		pnlCenter.add(new JLabel());

		pnlCenter.add(new JLabel());
		pnlCenter.add(btnEntry);

		pnlCenter.add(btnCalc);
		pnlCenter.add(btnCancel);

		pnlGlobalCenter.add(pnlCenter);

		add(pnlGlobalCenter, BorderLayout.CENTER);
	}

	public static void main(String[] argv){
		new Interfaccia();
	}
}