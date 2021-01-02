import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LClient {
	static Connection cnx;
	static Statement st;
	static ResultSet rst;
	private JFrame LC;
	private JPanel pMain,pT,pR;
	private JTable tLC;
	private JScrollPane sp;
	private JLabel lR,lN,lP;
	private JTextField tfN,tfP;
	private JButton bR,bAll;
	String data[][]= new String[100][4];
	String columns[]= {"ID","Nom","Prénom","Fidélité"};

	public LClient(){
		//----------------------------------------------------------------
		//Création de la fenêtre
		//----------------------------------------------------------------
		LC = new JFrame();
		//----------------------------------------------------------------
		//Panel principal
		//----------------------------------------------------------------
		pMain= new JPanel();
		pMain.setBorder(new EmptyBorder(5, 5, 5, 5));
		pMain.setLayout(null);
		//----------------------------------------------------------------
		//Panel contenant le ScorllPane
		//----------------------------------------------------------------
		pT = new JPanel();
		pT.setBounds(5, 5, 424, 158);
		pT.setLayout(null);
		//----------------------------------------------------------------
		//Panel avec les options de recherche
		//----------------------------------------------------------------
		pR = new JPanel();
		pR.setBounds(5, 166, 424, 90);
		pR.setLayout(null);
		//----------------------------------------------------------------
		//ScrollPane contenant le tableau de clients
		//----------------------------------------------------------------	
		sp=new JScrollPane();
		sp.setBounds(5, 17, 409, 138);
		//----------------------------------------------------------------
		//Tableau contenant les informations clients
		//----------------------------------------------------------------
		tLC = new JTable(data,columns);
		sp.setViewportView(tLC);
		tLC.setBounds(27, 11, 375, 128);
		tLC.setShowVerticalLines(false);	
		//----------------------------------------------------------------
		//Labels
		//----------------------------------------------------------------
		lR = new JLabel("Recherche : ");
		lR.setBounds(10, 11, 100, 14);
		
		lN = new JLabel("Nom : ");
		lN.setBounds(39, 40, 46, 14);
		
		lP = new JLabel("Prénom : ");
		lP.setBounds(20, 65, 70, 14);
		//----------------------------------------------------------------
		//TextFields
		//----------------------------------------------------------------
		tfN = new JTextField();
		tfN.setBounds(76, 37, 207, 20);
		tfN.setColumns(10);
		
		tfP = new JTextField();
		tfP.setBounds(76, 62, 207, 20);
		tfP.setColumns(10);
		//----------------------------------------------------------------
		//Boutons
		//----------------------------------------------------------------
		bR = new JButton("Rechercher");
		bR.setBounds(293, 61, 110, 23);
		bR.addActionListener(this::ButtonEvent);
		
		bAll = new JButton("Tout afficher");
		bAll.setBounds(293, 34, 110, 23);
		bAll.addActionListener(this::ButtonEvent);
		//----------------------------------------------------------------
		//Ajout des panels au panel principal
		//----------------------------------------------------------------
		pMain.add(pT);
		pMain.add(pR);
		//----------------------------------------------------------------
		//Ajouts des composants aux panels
		//----------------------------------------------------------------
		pT.add(sp);
		
		
		pR.add(lR);
		pR.add(lN);
		pR.add(lP);
		pR.add(tfN);
		pR.add(tfP);
		pR.add(bR);
		pR.add(bAll);
		
		pT.add(sp);
		//----------------------------------------------------------------
		//Caractéristiques de la fiche
		//----------------------------------------------------------------
		LC.setContentPane(pMain);
        LC.setTitle("Liste Clients");
		LC.setSize(450,300);
		LC.setLocationRelativeTo(null);
	    LC.setVisible(true);
	    tLC.setEnabled(false);
	}
	//----------------------------------------------------------------
	//Evenements pour les différents boutons
	//----------------------------------------------------------------
	private void ButtonEvent(ActionEvent e) {
		if(e.getSource()==bR) {
			RechClient();
			tfN.setText("");
			tfP.setText("");
		}
		else {
			ShowAll();
		}
	}
	//----------------------------------------------------------------
	//Fonction permettant l'affichage de tous les clients
	//----------------------------------------------------------------
	private void ShowAll(){
		CleanTab();
		tLC.setVisible(false);
		int i=0;
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT * from clients order BY ID");
			while(rst.next()) {
				data[i][0]=rst.getString("ID");
				data[i][1]=rst.getString("nom");
				data[i][2]=rst.getString("prénom");
				if(rst.getInt("Fidelité")==1) data[i][3]="oui";
				else data[i][3]="non";
				i=i+1;
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		tLC.setVisible(true);
		bAll.setEnabled(false);
	}
	//----------------------------------------------------------------
	//Fonction permettant la recherche d'un client voulu
	//----------------------------------------------------------------
	private void RechClient() {
		int i=0;
		tLC.setVisible(false);
		String nom=tfN.getText().trim();
		String prenom=tfP.getText().trim();
		if(nom.isEmpty() && prenom.isEmpty()) JOptionPane.showMessageDialog(LC,"ATTENTION : Champs non rempli !!");
		else if(nom.isEmpty()) {
			CleanTab();
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT * from clients where prénom=\""+prenom+"\" order BY ID");
			while(rst.next()) {
				data[i][0]=rst.getString("ID");
				data[i][1]=rst.getString("nom");
				data[i][2]=rst.getString("prénom");
				if(rst.getInt("Fidelité")==1) data[i][3]="oui";
				else data[i][3]="non";
				i=i+1;
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		}
		else if(prenom.isEmpty()) {
			CleanTab();
			try {
				cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
				st=cnx.createStatement();
				rst=st.executeQuery("SELECT * from clients where nom=\""+nom+"\" order BY ID");
				while(rst.next()) {
					data[i][0]=rst.getString("ID");
					data[i][1]=rst.getString("nom");
					data[i][2]=rst.getString("prénom");
					if(rst.getInt("Fidelité")==1) data[i][3]="oui";
					else data[i][3]="non";
					i=i+1;
				}
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		else {
			CleanTab();
			try {
				cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
				st=cnx.createStatement();
				rst=st.executeQuery("SELECT * from clients where nom=\""+nom+"\" and prénom=\""+prenom+"\" order BY ID");
				while(rst.next()) {
					data[i][0]=rst.getString("ID");
					data[i][1]=rst.getString("nom");
					data[i][2]=rst.getString("prénom");
					if(rst.getInt("Fidelité")==1) data[i][3]="oui";
					else data[i][3]="non";
					i=i+1;
				}
			}catch(SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		tLC.setVisible(true);
		bAll.setEnabled(true);
	}
	//----------------------------------------------------------------
	//Fonction permettant de réinitialiser la table
	//----------------------------------------------------------------
	private void CleanTab() {
		int n=data.length;
		int i=0;
		while(i!=n) {
			data[i][0]="";
			data[i][1]="";
			data[i][2]="";
			data[i][3]="";
			i++;
		}
	}
}
