import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LProduit {
	static Connection cnx;
	static Statement st;
	static ResultSet rst;
	private JFrame LP;
	private JPanel pMain,pT,pR;
	private JTable tLP;
	private JLabel lR,lTY,lTI;
	private JTextField tTI;
	private JComboBox<String> cmbTY;
	private JScrollPane sp;
	private JButton bR,bAll;
	String selection;
	String typ;
	String car;
	String data[][]=new String[100][5];
	String columns[]=new String [5];
	String type[]= {"CD","DVD","Dictionnaire","BD","Manuel","Roman"};
	
	LProduit(){
		columns[0]="ID";
		columns[1]="Tarif";
		columns[2]="Titre";
		columns[3]="Stock";
		columns[4]="Caractéristique";
		

		LP = new JFrame();
		
		pMain = new JPanel();
		pMain.setBorder(new EmptyBorder(5, 5, 5, 5));
		pMain.setLayout(null);
		
		pT = new JPanel();
		pT.setBounds(5, 5, 774, 143);
		pT.setLayout(null);
		
		pR = new JPanel();
		pR.setBounds(5, 154, 774, 102);
		pR.setLayout(null);
		
		sp = new JScrollPane();
		sp.setBounds(10, 11, 754, 121);
		
		tLP = new JTable(data,columns);
		tLP.setBounds(10, 11, 754, 121);
		tLP.setEnabled(false);
		sp.setViewportView(tLP);
		
		lR = new JLabel("Recherche : ");
		lR.setBounds(10, 11, 100, 14);
		
		lTY = new JLabel("Type : ");
		lTY.setBounds(20, 36, 46, 14);
		
		lTI = new JLabel("Titre : ");
		lTI.setBounds(20, 61, 46, 14);
		
		cmbTY = new JComboBox<String>(type);
		cmbTY.setBounds(61, 32, 211, 18);
		
		tTI = new JTextField();
		tTI.setBounds(61, 58, 211, 20);
		tTI.setColumns(10);
		
		bR = new JButton("Rechercher");
		bR.setBounds(603, 57, 161, 23);
		bR.addActionListener(this::ButtonEvent);
		
		bAll = new JButton("Tout afficher");
		bAll.setBounds(603, 32, 161, 23);
		bAll.addActionListener(this::ButtonEvent);
		
		pMain.add(pT);
		pMain.add(pR);
		
		pT.add(sp);
		
		pR.add(lR);
		pR.add(lTY);
		pR.add(lTI);
		pR.add(cmbTY);
		pR.add(tTI);
		pR.add(bAll);
		pR.add(bR);
		
		LP.setContentPane(pMain);
		LP.setTitle("Liste Produits");
		LP.setSize(800,300);
		LP.setLocationRelativeTo(null);
	    LP.setVisible(true);
	    cmbTY.addActionListener((ActionListener) new ActionListener() {     
		     public void actionPerformed(ActionEvent e) {
		        selection=cmbTY.getSelectedItem().toString();
		        if(selection=="CD") {
		        	tLP.setVisible(false);
					data[0][0]="-------";
					data[0][1]="-------";
					data[0][2]="-------";
					data[0][3]="-------";
					data[0][4]="Année";
					typ="CD";
					car="Année";
					tLP.setVisible(true);
		        }
		        else if(selection=="DVD") {
		        	tLP.setVisible(false);
					data[0][0]="-------";
					data[0][1]="-------";
					data[0][2]="-------";
					data[0][3]="-------";
					data[0][4]="Réalisateur";
					typ="DVD";
					car="Réalisateur";
					tLP.setVisible(true);
		        }
		        else if(selection=="Dictionnaire") {
		        	tLP.setVisible(false);
					data[0][0]="-------";
					data[0][1]="-------";
					data[0][2]="-------";
					data[0][3]="-------";
					data[0][4]="Langue";
					typ="Dictionnaire";
					car="Langue";
					tLP.setVisible(true);
		        }
		        else {
		        	tLP.setVisible(false);
					data[0][0]="-------";
					data[0][1]="-------";
					data[0][2]="-------";
					data[0][3]="-------";
					data[0][4]="Auteur";
					typ=selection;
					car="Auteur";
					tLP.setVisible(true);
		        }
		     }
		   });
	}
	
	private void ButtonEvent(ActionEvent e) {
		 
		 CleanTab();
		if(e.getSource()==bAll) {
			ShowAll(typ,car);
		}
		else {
			RechPrSQL(typ,car);
		}
}
	private void ShowAll(String typ,String car) {
		int i = 1;
		tLP.setVisible(false);
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT * from produits where Type=\""+typ+"\" order BY ID");
			while(rst.next()) {
				data[i][0]=rst.getString("ID");
				data[i][1]=rst.getString("Tarif");
				data[i][2]=rst.getString("Titre");
				data[i][3]=rst.getString("Stock");
				data[i][4]=rst.getString(car);	
				i=i+1;
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		tLP.setVisible(true);
	}
	
	private void RechPrSQL(String typ, String car) {
		int i = 1;
		tLP.setVisible(false);
		String Title=tTI.getText().trim();
		if(Title.isEmpty()) {
			JOptionPane.showMessageDialog(LP,"ATTENTION : \"Titre\" non rempli !!");
		}
		else {
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT * FROM `produits` where Type=\""+typ+"\" and Titre=\""+Title+"\" order by ID");
			while(rst.next()) {
				data[i][0]=rst.getString("ID");
				data[i][1]=rst.getString("Tarif");
				data[i][2]=rst.getString("Titre");
				data[i][3]=rst.getString("Stock");
				data[i][4]=rst.getString(car);	
				i=i+1;
			}
		}catch(SQLException e) {
		System.out.println(e.getMessage());
	}
	}
		tLP.setVisible(true);
	}
	
	private void CleanTab() {
		int n=data.length;
		int i=1;
		while(i!=n) {
			data[i][0]="";
			data[i][1]="";
			data[i][2]="";
			data[i][3]="";
			data[i][4]="";
			i++;
		}
	}
}
