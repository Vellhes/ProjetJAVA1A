import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultComboBoxModel;
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

public class REmprunt {
	static Connection cnx;
	static Statement st;
	static ResultSet rst;
	private JFrame RE;
	private JPanel pMain,pR,pD;
	private JScrollPane sp;
	private JTable tEmprunt;
	private JLabel lCL,lO,lE,lD;
	private JComboBox<String> cmbCL, cmbO;
	private JTextField tfEmp,tfJ,tfM,tfA;
	private JButton bOK;
	String IDcl;
	String IDo;
	String data[][] = new String [100][4];
	String columns[] = {"ID","ID_produit","Quantité","date_d"};
	String cellClients[] = new String[1000];
	String cellCommande[] = new String[1000];
	int i;
	REmprunt(){
		//----------------------------------------------------------------
		//Création de la fenêtre
		//----------------------------------------------------------------
		RE = new JFrame();
		//----------------------------------------------------------------
		//Placement de la liste des clients dans un tableau
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
		
		String tabClients[]= new String[i];
		tabClients[0]="------";
		int n=1;
		while(n!=i) {
			tabClients[n]=cellClients[n];
			n++;
		}
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
		sp.setBounds(10, 11, 514, 183);
		//----------------------------------------------------------------
		//Création du panel détail
		//----------------------------------------------------------------
		pD = new JPanel();
		pD.setBounds(269, 205, 255, 95);
		pD.setLayout(null);
		//----------------------------------------------------------------
		//Création du panel rendu
		//----------------------------------------------------------------
		pR = new JPanel();
		pR.setBounds(10, 205, 249, 95);
		pR.setLayout(null);
		//----------------------------------------------------------------
		//Création du tableau des emprunts
		//----------------------------------------------------------------
		tEmprunt = new JTable(data,columns);
		sp.setViewportView(tEmprunt);
		tEmprunt.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent arg0) {
            	String prod="";
                try{
                    int i = tEmprunt.getSelectedRow();
                    try {
                		cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
            			st=cnx.createStatement();
            			rst=st.executeQuery("Select * from produits where ID=\""+tEmprunt.getValueAt(i,1).toString() +"\"order by ID");
            			while(rst.next()) {
            				prod = rst.getString("Titre");
            				}
                    }
                	catch(Exception e) {System.out.println(e.getMessage());}
                    tfEmp.setText(tEmprunt.getValueAt(i, 1).toString()+" - "+prod+" * "+tEmprunt.getValueAt(i, 2).toString());
                    }
                catch (Exception e){JOptionPane.showMessageDialog(null, "erreur de déplacement");}
            }    
		});
		//----------------------------------------------------------------
		//Création des labels
		//----------------------------------------------------------------
		lE = new JLabel("Emprunt : ");
		lE.setBounds(10, 11, 58, 14);
		
		lD = new JLabel("Date de retour : (JJ/MM/AAAA)");
		lD.setBounds(10, 36, 197, 14);
		
		lCL = new JLabel("Client : ");
		lCL.setBounds(35, 11, 63, 14);
		
		lO = new JLabel("Commande :");
		lO.setBounds(2, 40, 100, 14);
		//----------------------------------------------------------------
		//Création des combobox
		//----------------------------------------------------------------
		cmbCL = new JComboBox<String>(tabClients);
		cmbCL.setBounds(83, 7, 156, 22);
		//----------------------------------------------------------------
		//ActionListener permettant de récupérer l'id client et de remplir la combobox des commandes
		//----------------------------------------------------------------
		cmbCL.addActionListener((ActionListener) new ActionListener() {     
		     public void actionPerformed(ActionEvent e) {
		    	IDcl = cmbCL.getSelectedItem().toString();
		    	IDcl = IDcl.substring(0,1);
		    	cmbSQL(IDcl,cmbO);
		    	}
		     }
		);
		     
		cmbO = new JComboBox<String>();
		cmbO.setBounds(83, 36, 156, 22);
		//----------------------------------------------------------------
		//ActionListener permettant de récupérer l'identifiant de la commande et d'afficher tous ses emprunts
		//----------------------------------------------------------------
		cmbO.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IDo=cmbO.getSelectedItem().toString();
				IDo=IDo.substring(0,1);
				RechEmpr(IDo);
			}
		});
		//----------------------------------------------------------------
		//Création des champs de texte
		//----------------------------------------------------------------
		tfEmp = new JTextField();
		tfEmp.setBounds(67, 8, 178, 20);
		tfEmp.setColumns(10);
		tfEmp.setEnabled(false);
		
		tfJ = new JTextField();
		tfJ.setBounds(10, 61, 40, 20);
		tfJ.setColumns(10);
		
		tfM = new JTextField();
		tfM.setBounds(60, 61, 40, 20);
		tfM.setColumns(10);
		
		tfA = new JTextField();
		tfA.setBounds(110, 61, 70, 20);
		tfA.setColumns(10);
		//----------------------------------------------------------------
		//Création des boutons
		//----------------------------------------------------------------
		bOK = new JButton("OK");
		bOK.setBounds(190, 60, 55, 23);
		bOK.addActionListener(this::ButtonEvent);
		//----------------------------------------------------------------
		//Ajout des composants aux panles
		//----------------------------------------------------------------
		pR.add(cmbCL);
		pR.add(cmbO);
		pR.add(lCL);
		pR.add(lO);
		
		pD.add(lD);
		pD.add(lE);
		pD.add(tfEmp);
		pD.add(tfA);
		pD.add(tfM);
		pD.add(tfJ);
		pD.add(bOK);
		
		pMain.add(pD);
		pMain.add(pR);
		pMain.add(sp);
		//----------------------------------------------------------------
		//Caractéristiques de la fenêtre
		//----------------------------------------------------------------
		RE.setContentPane(pMain);
		RE.setTitle("Retour d'emprunts");
		RE.setSize(550, 350);
		RE.setLayout(null);
	    RE.setLocationRelativeTo(null);
	    RE.setVisible(true);
	}
	//----------------------------------------------------------------
	//Evenement du bouton de rendu
	//----------------------------------------------------------------
	private void ButtonEvent(ActionEvent e) {
		if(e.getSource()==bOK) {
			String jour=tfJ.getText().trim();
			String mois=tfM.getText().trim();
			String annee=tfA.getText().trim();
			if(tfEmp.getText().trim().isEmpty()) JOptionPane.showMessageDialog(RE,"ATTENTION : aucun emprunt sélectionné !!");
			else if(jour.isEmpty()) JOptionPane.showMessageDialog(RE,"ATTENTION : case jour non remplie !!");
			else if(mois.isEmpty()) JOptionPane.showMessageDialog(RE,"ATTENTION : case mois non remplie !!");
			else if(annee.isEmpty()) JOptionPane.showMessageDialog(RE,"ATTENTION : case année non remplie !!");
			else {
				AjoutDateF();
				AjoutTarif();
				ModifStock();
				tfEmp.setText("");
				tfJ.setText("");
				tfM.setText("");
				tfA.setText("");
				cmbCL.setSelectedIndex(0);
			}		
		}
	}
	//----------------------------------------------------------------
	//Fonction permettant de remplir une combobox avec les commandes du client sélectionné
	//----------------------------------------------------------------
	private void cmbSQL(String ID,JComboBox<String> cmb) {
		String tab[]=new String[10];
		try {
    		i=0;
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("Select * from commande where ID_client=\""+ID+"\" order by ID");
			while(rst.next()) {
				String cell=rst.getString("ID")+" - "+rst.getString("date_d");
				tab[i]=cell;
				i++;
			}
			}catch(SQLException f) {
				System.out.println(f.getMessage());
			}
		cmb.setModel(new DefaultComboBoxModel<String>(tab));
    	}
	//----------------------------------------------------------------
	//Fonction permettant de rechercher les emprunts concernant une même commande
	//----------------------------------------------------------------
	private void RechEmpr(String ID) {
		tEmprunt.setVisible(false);
		CleanTab();
		try {
			i=0;
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("Select * from emprunt where ID=\""+ID+"\" and date_f='0000-00-00' order by ID");
			while(rst.next()) {
				data[i][0]=rst.getString("ID");
				data[i][1]=rst.getString("ID_prod");
				data[i][2]=rst.getString("Quantité");
				data[i][3]=rst.getString("date_d");
				i++;
		}
		}catch(SQLException f) {
			System.out.println(f.getMessage());
		}
		tEmprunt.setVisible(true);
	}
	//----------------------------------------------------------------
	//Fonction permettant de modifier le stock
	//----------------------------------------------------------------
	private void ModifStock() {
		try {
			int qte=(int) tEmprunt.getValueAt(i, 2);
			String query="UPDATE produits SET stock = stock + "+qte+" where id=\""+tfEmp.getText().substring(0,1)+"\";";
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			st.executeUpdate(query);
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	//----------------------------------------------------------------
	//Fonction permettant d'ajouter la date de rendu dans la BdD
	//----------------------------------------------------------------	
	private void AjoutDateF() {	
		try {
			String query="UPDATE `emprunt` SET `date_f` = '"+tfA.getText().trim()+"-"+tfM.getText().trim()+"-"+tfJ.getText().trim()+"' WHERE `emprunt`.`ID` = \""+IDo+"\" and emprunt.ID_prod=\""+tfEmp.getText().substring(0,1)+"\";";
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			st.executeUpdate(query);
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	//----------------------------------------------------------------
	//Fonction permettant d'ajouter le prix du produit rendu a la commande
	//----------------------------------------------------------------
	private void AjoutTarif() {
		int duree = 0;
		int tarif = 0;
		int qte = 0;
		double reduc = 1;
		try {
			i=0;
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("Select date_f-date_d as duree from emprunt where ID=\""+IDo+"\" and ID_prod =\""+tfEmp.getText().substring(0,1)+"\";");
			while(rst.next()) {
				duree=rst.getInt("duree");
			}
		}catch(SQLException f) {
			System.out.println(f.getMessage());
		}
		
		try {
			i=0;
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("Select tarif from produits where ID=\""+tfEmp.getText().substring(0,1)+"\"");
			while(rst.next()) {
				tarif = rst.getInt("Tarif");
			}
		}catch(SQLException f) {
			System.out.println(f.getMessage());
		}
		
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT * from emprunt where ID=\""+IDo+"\" and ID_prod=\""+tfEmp.getText().substring(0,1)+"\";");
			while(rst.next()) {
				qte = rst.getInt("Quantité");
			}
		}catch(SQLException f) {
			System.out.println(f.getMessage());
		}
		
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("Select * from clients where ID = \""+IDcl+"\";");
			while(rst.next()) {
				if(rst.getInt("Fidelité")==1) reduc = 0.9;
			}
		}catch(SQLException f) {
			System.out.println(f.getMessage());
		}
		
		double prix = ((duree+1)*tarif*qte*reduc);
		try {
			String query="UPDATE `emprunt` SET `tarif` = '"+prix+"' where ID=\""+IDo+"\" and ID_prod =\""+tfEmp.getText().substring(0,1)+"\";";
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			st.executeUpdate(query);
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		JOptionPane.showMessageDialog(RE,"Emprunt rendu. A payer : "+prix+" €.");
	}
	//----------------------------------------------------------------
	//Fonction permettant de vider la table contenant les emprunts
	//----------------------------------------------------------------
	private void CleanTab() {
		int n = data.length;
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
