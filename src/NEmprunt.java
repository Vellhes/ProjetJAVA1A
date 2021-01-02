import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class NEmprunt {
	static Connection cnx;
	static Statement st;
	static ResultSet rst;
	private JFrame NE;
	private JPanel pMain,pP,pE;
	private JLabel lR,lTY,lLP,lE,lQ;
	private JTextField tfTI,tfID;
	private JComboBox<String> cmbTY, cmbP;
	private JButton bAdd;
	private JSpinner sQ;
	String type[] = {"CD","DVD","Dictionnaire","BD","Manuel","Roman"};
	String typ;
	String prod;
	String Ptitle;
	NEmprunt(){
		//----------------------------------------------------------------
		//Création de la fenêtre
		//----------------------------------------------------------------
		NE = new JFrame();
		//----------------------------------------------------------------
		//Création du panel principal
		//----------------------------------------------------------------
		pMain = new JPanel();
		pMain.setBorder(new EmptyBorder(5, 5, 5, 5));
		pMain.setLayout(null);
		//----------------------------------------------------------------
		//Création du panel produit
		//----------------------------------------------------------------
		pP = new JPanel();
		pP.setBounds(10, 11, 297, 156);
		pP.setLayout(null);
		//----------------------------------------------------------------
		//Création du panel emprunt
		//----------------------------------------------------------------
		pE = new JPanel();
		pE.setBounds(10, 179, 297, 41);
		pE.setLayout(null);
		//----------------------------------------------------------------
		//Création des labels
		//----------------------------------------------------------------
		lR = new JLabel("Recherche : ");
		lR.setBounds(10, 11, 76, 14);
		
		lTY = new JLabel("Type : ");
		lTY.setBounds(20, 36, 46, 14);
		
		lLP = new JLabel("Liste Produits : ");
		lLP.setBounds(10, 86, 94, 14);
		
		lE = new JLabel("ID Commande : ");
		lE.setBounds(5, 11, 100, 14);
		
		lQ = new JLabel("Quantité : ");
		lQ.setBounds(191, 140, 64, 14);
		//----------------------------------------------------------------
		//Création des champs de texte
		//----------------------------------------------------------------
		tfTI = new JTextField();
		tfTI.setBounds(57, 58, 230, 20);
		tfTI.setColumns(10);
		
		tfID = new JTextField();
		tfID.setBounds(95, 10, 32, 20);
		tfID.setColumns(10);
		//----------------------------------------------------------------
		//Création du spinner quantité
		//----------------------------------------------------------------
		sQ = new JSpinner();
		sQ.setBounds(246, 137, 41, 20);
		//----------------------------------------------------------------
		//Création de la combo box "type produit"
		//----------------------------------------------------------------
		cmbTY = new JComboBox<String>(type);
		cmbTY.setBounds(57, 32, 230, 22);
		//----------------------------------------------------------------
		//ActionListener permettant de remplir la combntenant tous les produits du type selectionné
		//----------------------------------------------------------------
		cmbTY.addActionListener((ActionListener) new ActionListener() {     
		     public void actionPerformed(ActionEvent e) {
		        typ=cmbTY.getSelectedItem().toString();
		        cmbSQL(typ,cmbP);
		     }}
		     );
		//----------------------------------------------------------------
		//Création de la combobox contenant les produits
		//----------------------------------------------------------------
		cmbP = new JComboBox<String>();
		cmbP.setBounds(10, 111, 277, 22);
		cmbP.addActionListener((ActionListener) new ActionListener() {     
		     public void actionPerformed(ActionEvent e) {
		    	 prod=cmbP.getSelectedItem().toString();
		    	 Ptitle=prod.substring(4);
		    	 prod=prod.substring(0, 1);
		    	 bAdd.setEnabled(true);
		     }
		});
		//----------------------------------------------------------------
		//Création du bouton d'ajout
		//----------------------------------------------------------------
		bAdd = new JButton("Ajouter Emprunt");
		bAdd.setBounds(131, 7, 156, 23);
		bAdd.addActionListener(this::ButtonEvent);
		bAdd.setEnabled(false);
		//----------------------------------------------------------------
		//ajout des composants aux panels
		//----------------------------------------------------------------
		pMain.add(pE);
		pMain.add(pP);
		
		pP.add(lR);
		pP.add(lTY);
		pP.add(lLP);
		pP.add(cmbP);
		pP.add(cmbTY);
		pP.add(lQ);
		pP.add(sQ);

		pE.add(lE);
		pE.add(bAdd);
		pE.add(tfID);
		//----------------------------------------------------------------
		//Caractéristiques de la fenêtre
		//----------------------------------------------------------------
		NE.setContentPane(pMain);
		NE.setTitle("Nouvel Emprunt");
		NE.setSize(329, 270);
		NE.setLayout(null);
	    NE.setLocationRelativeTo(null);
	    NE.setVisible(true);
	}
	//----------------------------------------------------------------
	//Evenement du bouton d'ajout
	//----------------------------------------------------------------
	private void ButtonEvent(ActionEvent e) {
		String sID = tfID.getText().trim();
		int stock=getStock();
		int EmpExist=CheckEmprunt();
		int OrderExist=CheckCommande();
		int qte=((Integer)sQ.getValue());
		if(qte==0) {
			JOptionPane.showMessageDialog(NE,"ATTENTION : \"Quantité\" nulle !!");
		}
		else if(sID.isEmpty()) {
			JOptionPane.showMessageDialog(NE,"ATTENTION : \"ID Commande\" non rempli !!");
		}
		else {
			if(stock>=qte) {
				if(OrderExist==1) {
					if(EmpExist==0) {
						NEmpruntSQL();
						JOptionPane.showMessageDialog(NE,"Emprunt de "+qte+" "+typ+" "+Ptitle+" ajouté à la commande n°"+sID+".");
						ModifStock(qte,stock,sID);
						AllClear();
						}
					else JOptionPane.showMessageDialog(NE,"L'emprunt de ce produit existe déjà pour cette commande.");
					}
				else  JOptionPane.showMessageDialog(NE,"La commande "+sID+" n'existe pas.");
				}
			else JOptionPane.showMessageDialog(NE,"Stock insuffisant, veuillez choisir une plus petite quantité.");
		}
	}
	//----------------------------------------------------------------
	//Fonction permettant de récuperer la liste des produits et de les mettre dans une combobox
	//----------------------------------------------------------------
	private void cmbSQL(String typ,JComboBox<String> cmb) {
		int i = 0;
		
		String tab[]=new String[100];
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT * from produits where Type = \""+typ+"\"");
			while(rst.next()) {
				String cell=rst.getString("ID")+" - "+rst.getString("Titre");
				tab[i]=cell;
				i=i+1;
			}
		}catch(SQLException f) {
			System.out.println(f.getMessage());
		}
		cmb.setModel(new DefaultComboBoxModel<String>(tab));
	}
	//----------------------------------------------------------------
	//Foncion permettant l'ajout d'un emprunt dans la BdD
	//----------------------------------------------------------------
	private void NEmpruntSQL() {
		Date ToDay = new Date(System.currentTimeMillis());
		int qte=((Integer)sQ.getValue());
		try {
			String query="INSERT INTO `emprunt` (`ID`, `ID_prod`, `Tarif`, `Quantité`, `date_d`, `date_f`) VALUES ('"+tfID.getText().trim()+"', '"+prod+"', '0', '"+qte+"', '"+ToDay+"', '0000-00-00');";
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			st.executeUpdate(query);
		}catch(SQLException f) {
			System.out.println(f.getMessage());
		}
	}
	//----------------------------------------------------------------
	//Fonction permettant de vider le textfield ID et désactiver la bouton d'ajout
	//----------------------------------------------------------------	
	private void AllClear() {
		tfID.setText("");
		bAdd.setEnabled(false);
	}
	//----------------------------------------------------------------
	//Fonction permettant de récupérer le stock disponible
	//----------------------------------------------------------------
	private int getStock() {
		int stock = 0;
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT stock from produits where ID=\""+prod+"\"");
			while(rst.next()) {
				stock=rst.getInt("stock");
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return stock;
	}
	//----------------------------------------------------------------
	//Fonction permettant de modifier le stock après un ajout
	//----------------------------------------------------------------
	private void ModifStock(int qte,int stk,String ID) {
		try {
			String query=("UPDATE `produits` SET `Stock` = '"+ (stk-qte) +"' WHERE `produits`.`ID` = \""+ID+"\";");
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			st.executeUpdate(query);
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	//----------------------------------------------------------------
	//Fonction permettant de vérifier si un emprunt existe déjà
	//----------------------------------------------------------------
	private int CheckEmprunt() {
		int i=0;
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT * from emprunt where ID_prod=\""+prod+"\" and ID=\""+tfID.getText().trim()+"\"");
			while(rst.next()) {
				i++;
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return i;
	}
	//----------------------------------------------------------------
	//Fonction permettant de vérifier si la commande choisit existe
	//----------------------------------------------------------------
	private int CheckCommande() {
		int i=0;
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT * from commande where ID=\""+tfID.getText().trim()+"\"");
			while(rst.next()) {
				i++;
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return i;
	}
		
}

