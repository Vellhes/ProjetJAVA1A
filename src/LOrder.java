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
import javax.swing.border.EmptyBorder;

public class LOrder {
	static Connection cnx;
	static Statement st;
	static ResultSet rst;
	private JFrame LO;
	private JPanel pMain,pR;
	private JScrollPane sp;
	private JTable tOrder;
	private JLabel lCL;
	private JComboBox<String> cmbCL;
	private JButton bAll,bR;
	String cellClients[]=new String [1000];
	String tabOrder[][]=new String [100][4];
	String columns[]= {"ID","ID client","Reduction","Date Création"};
	String IDcl;
	int i;
	LOrder(){
		//----------------------------------------------------------------
		//Création de la fenêtre
		//----------------------------------------------------------------		
		LO = new JFrame();
		//----------------------------------------------------------------
		//Création du panel principal
		//----------------------------------------------------------------
		pMain = new JPanel();
		pMain.setBorder(new EmptyBorder(5, 5, 5, 5));
		pMain.setLayout(null);
		//----------------------------------------------------------------
		//Création du panel contenant le tableau
		//----------------------------------------------------------------
		sp = new JScrollPane();
		sp.setBounds(10, 11, 564, 217);
		//----------------------------------------------------------------
		//Création du panel de recherche
		//----------------------------------------------------------------
		pR = new JPanel();
		pR.setBounds(10, 239, 564, 61);
		pR.setLayout(null);
		//----------------------------------------------------------------
		//Création du tableau contenant la liste des Commandes
		//----------------------------------------------------------------
		tOrder = new JTable(tabOrder,columns);
		tOrder.setEnabled(false);
		sp.setViewportView(tOrder);
		//----------------------------------------------------------------
		//Création du label
		//----------------------------------------------------------------
		lCL = new JLabel("Client : ");
		lCL.setBounds(10, 25, 70, 14);
		//----------------------------------------------------------------
		//Liaison à la BdD permettant de récupérer la liste des clients
		//----------------------------------------------------------------
		try {
			i=1;
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("Select * from clients order by ID");
			while(rst.next()) {
				cellClients[i]=rst.getInt("ID")+" - "+rst.getString("Nom")+" "+rst.getString("Prénom");
				i++;
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		//----------------------------------------------------------------
		//Placement de la liste des clients dans un tableau
		//----------------------------------------------------------------
		String tabClients[]= new String[i];
		tabClients[0]="------";
		int n=1;
		while (n!=i) {
			tabClients[n]=cellClients[n];
			n++;
		}
		//----------------------------------------------------------------
		//Création de la Combo Box contenant la liste des clients
		//----------------------------------------------------------------
		cmbCL = new JComboBox<String>(tabClients);
		cmbCL.setBounds(57, 21, 174, 22);
		cmbCL.addActionListener((ActionListener) new ActionListener() {     
		     public void actionPerformed(ActionEvent e) {
		    	IDcl = cmbCL.getSelectedItem().toString();
		    	IDcl = IDcl.substring(0,1);
		     }
		});
		//----------------------------------------------------------------
		//Création des boutons
		//----------------------------------------------------------------
		bR = new JButton("Rechercher");
		bR.setBounds(441, 38, 113, 23);
		bR.addActionListener(this::ButtonEvent);
		
		bAll = new JButton("Tout Afficher");
		bAll.setBounds(441, 0, 113, 23);
		bAll.addActionListener(this::ButtonEvent);
		//----------------------------------------------------------------
		//Ajout des composants aux différents panels
		//----------------------------------------------------------------
		
		pMain.add(sp);
		pMain.add(pR);
		
		pR.add(lCL);
		pR.add(cmbCL);
		pR.add(bR);
		pR.add(bAll);
		//----------------------------------------------------------------
		//Caractéristiques de la fenetre
		//----------------------------------------------------------------
		LO.setContentPane(pMain);
		
		LO.setTitle("Liste Commandes");
		LO.setSize(600, 350);
		LO.setLayout(null);
	    LO.setLocationRelativeTo(null);
	    LO.setVisible(true);
	}
	//----------------------------------------------------------------
	//Evenement des boutons
	//----------------------------------------------------------------
	private void ButtonEvent(ActionEvent e) {
		if (e.getSource()==bR) {
			if(cmbCL.getSelectedIndex()==0) JOptionPane.showMessageDialog(LO,"Veuillez choisir un client");
			else RechOrder();
		}
		else ShowAll();
	}
	//----------------------------------------------------------------
	//Fonction permettant l'affichage de toutes les commandes existantes
	//----------------------------------------------------------------
	private void ShowAll() {
		int i=0;
		tOrder.setVisible(false);
		CleanTab();
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT * from commande order BY ID");
			while(rst.next()) {
				tabOrder[i][0]=rst.getString("ID");
				tabOrder[i][1]=rst.getString("ID_client");
				if(rst.getInt("reduction")==0) tabOrder[i][2]="0%";
				else if(rst.getInt("reduction")==1) tabOrder[i][2]="10%";
				tabOrder[i][3]=rst.getString("date_d");
				i=i+1;
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		tOrder.setVisible(true);
	}
	//----------------------------------------------------------------
	//Fonction permettant la recherche des commande pour un client précis
	//----------------------------------------------------------------
	private void RechOrder() {
		int i=0;
		CleanTab();
		tOrder.setVisible(false);
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT * from commande where ID_client=\""+IDcl+"\" order BY ID");
			while(rst.next()) {
				tabOrder[i][0]=""+ rst.getInt("ID") +"";
				tabOrder[i][1]=""+ rst.getInt("ID_client") +"";
				if(rst.getInt("reduction")==0) tabOrder[i][2]="0%";
				else tabOrder[i][2]="10%";
				tabOrder[i][3]=""+rst.getDate("date_d")+"";
				i++;
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		tOrder.setVisible(true);
	}
	//----------------------------------------------------------------
	//Fonction permettant de vider le tableau
	//----------------------------------------------------------------
	private void CleanTab() {
		int n = tabOrder.length;
		i = 0;
		while(i!=n) {
			tabOrder[i][0]="";
			tabOrder[i][1]="";
			tabOrder[i][2]="";
			tabOrder[i][3]="";
			i++;
		}
	}
	
}
