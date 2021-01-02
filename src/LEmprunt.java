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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

public class LEmprunt {
	static Connection cnx;
	static Statement st;
	static ResultSet rst;
	private JFrame LE;
	private JPanel pMain,pR;
	private JScrollPane sp;
	private JLabel lID;
	private JSpinner sID;
	private JButton bR;
	private JTable tEmprunt;
	String data[][]=new String [100][6];
	String columns[]= {"ID commande","ID prod","quantité","date d","date f","Tarif"};
	LEmprunt(){
		//----------------------------------------------------------------
		//Cr"ation de la fenêtre
		//----------------------------------------------------------------
		LE = new JFrame();
		//----------------------------------------------------------------
		//Création du panel principal
		//----------------------------------------------------------------
		pMain = new JPanel();
		pMain.setBorder(new EmptyBorder(5, 5, 5, 5));
		pMain.setLayout(null);
		//----------------------------------------------------------------
		//Création du scroll pane
		//----------------------------------------------------------------
		sp = new JScrollPane();
		sp.setBounds(10, 11, 564, 212);
		//----------------------------------------------------------------
		//Implémentation du tableau dans el scroll pane
		//----------------------------------------------------------------
		tEmprunt = new JTable(data,columns);
		sp.setViewportView(tEmprunt);
		//----------------------------------------------------------------
		//Création du panel de recherche
		//----------------------------------------------------------------
		pR = new JPanel();
		pR.setBounds(10, 234, 564, 66);
		pR.setLayout(null);
		//----------------------------------------------------------------
		//Création du labels
		//----------------------------------------------------------------
		lID = new JLabel("ID Commande : ");
		lID.setBounds(10, 28, 107, 14);
		//----------------------------------------------------------------
		//Evenement du Spinner quantité
		//----------------------------------------------------------------
		sID = new JSpinner();
		sID.setBounds(110, 25, 55, 20);
		//----------------------------------------------------------------
		//Evenement du bouton rechercher
		//----------------------------------------------------------------
		bR = new JButton("Rechercher");
		bR.setBounds(412, 24, 142, 23);
		bR.addActionListener(this::ButtonEvent);
		//----------------------------------------------------------------
		//Ajout des composants aux panels
		//----------------------------------------------------------------
		pR.add(bR);
		pR.add(sID);
		pR.add(lID);
		
		pMain.add(pR);
		pMain.add(sp);
		//----------------------------------------------------------------
		//Caractéristiques de la fenêtre
		//----------------------------------------------------------------
		LE.setContentPane(pMain);
		LE.setSize(600, 350);
		LE.setTitle("Liste d'emprunts");
		LE.setLayout(null);
	    LE.setLocationRelativeTo(null);
	    LE.setVisible(true);
	}
	//----------------------------------------------------------------
	//Evenement sur les boutons
	//----------------------------------------------------------------
	private void ButtonEvent(ActionEvent e) {
		if(e.getSource()==bR) {
			int exist = CheckEmprunt();
			if(exist==1) RechEmprunt();
			else if(exist==0) JOptionPane.showMessageDialog(LE,"Commande n°"+(Integer)sID.getValue()+" inexistante, veuillez saisir une autre valeur");
		}
	}
	//----------------------------------------------------------------
	//Fonction permettant de rechercher les emprunts pour une commande
	//----------------------------------------------------------------
	private void RechEmprunt() {
		int i=0;
		double total=0;
		CleanTab();
		tEmprunt.setVisible(false);
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT * from emprunt where ID=\""+(Integer)sID.getValue()+"\" order BY ID");
			while(rst.next()) {
				data[i][0]=rst.getString("ID");
				data[i][1]=rst.getString("ID_prod");
				data[i][2]=rst.getString("Quantité");
				data[i][3]=rst.getString("date_d");
				if(rst.getInt("Tarif")==0) {
				data[i][4]="non rendu";
				data[i][5]="0";
				}	
				else {
					data[i][4]=rst.getString("date_f");
					data[i][5]=rst.getString("Tarif");
				}
				total = total + rst.getInt("Tarif");
				i++;
			}
			data[i][0]="-----";
			data[i][1]="-----";
			data[i][2]="-----";
			data[i][3]="-----";
			data[i][4]="-----";
			data[i][5]="-----";
			i++;
			data[i][0]="";
			data[i][1]="";
			data[i][2]="";
			data[i][3]="";
			data[i][4]="Total à payer :";
			data[i][5]=""+total+"";
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		tEmprunt.setVisible(true);
	}
	//----------------------------------------------------------------
	//Fonction permettant de vider le tableau
	//----------------------------------------------------------------
	private void CleanTab() {
		int n = data.length;
		int i=0;
		while(i!=n) {
			data[i][0]="";
			data[i][1]="";
			data[i][2]="";
			data[i][3]="";
			data[i][4]="";
			data[i][5]="";
			i++;
		}
	}
	//----------------------------------------------------------------
	//Fonction permettant de vérifier si la commande existe
	//----------------------------------------------------------------
	private int CheckEmprunt() {
		int i=0;
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT * from commande where ID = \""+(Integer)sID.getValue()+"\"");
			while(rst.next()) {
				i++;
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return i;
	}
}
	
