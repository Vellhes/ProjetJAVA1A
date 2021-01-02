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
import javax.swing.JTextField;

public class NProduit {
	static Connection cnx;
	static Statement st;
	static ResultSet rst;
	private JFrame NP;
	private JLabel lSN,lD,lL,lT,lP,lID,lAS,lR,lLA,lA,lS;
	private JButton bCD,bDVD,bD,bBD,bM,bR,bAddCD,bAddDVD,bAddD,bAddBD,bAddR,bAddM;
	private JTextField tfT,tfP,tfID,tfAS,tfR,tfLA,tfA,tfS;
	NProduit(){
		//----------------------------------------------------------------
		//Création de la fenêtre//
		//----------------------------------------------------------------
		NP=new JFrame("Nouveau Produit");
		//----------------------------------------------------------------
		//Création des Labels// 
		//----------------------------------------------------------------
		lSN=new JLabel("Support Numérique : ");
		lSN.setBounds(10, 10, 120, 30);
		
		lD=new JLabel("Document simple : ");
		lD.setBounds(10,130,120,30);
		
		lL=new JLabel("Livre : ");
		lL.setBounds(10,210,120,30);
		
		lT=new JLabel("Titre : ");
		lT.setBounds(145,30,50,30);
		
		lP=new JLabel("Tarif : ");
		lP.setBounds(145,60,50,30);
		
		lID=new JLabel("ID : ");
		lID.setBounds(228,60,50,30);
		
		lAS=new JLabel("Année de Sortie : ");
		lAS.setBounds(145,90,100,30);
		
		lR=new JLabel("Réalisateur : ");
		lR.setBounds(145,120,100,30);
		
		lLA=new JLabel("Langue : ");
		lLA.setBounds(145,150,100,30);
		
		lA=new JLabel("Auteur : ");
		lA.setBounds(145,180,100,30);
		
		lS=new JLabel("Stock : ");
		lS.setBounds(145,210,100,30);
		//----------------------------------------------------------------
		//Création des boutons//
		//----------------------------------------------------------------
		bCD=new JButton("CD");
		bCD.setBounds(10,45,120,30);
		
		bDVD=new JButton("DVD");
		bDVD.setBounds(10,90,120,30);
		
		bD=new JButton("Dictionnaire");
		bD.setBounds(10,170,120,30);
		
		bBD=new JButton("BD");
		bBD.setBounds(10,250,120,30);
		
		bM=new JButton("Manuel");
		bM.setBounds(10,295,120,30);
		
		bR=new JButton("Roman");
		bR.setBounds(10,340,120,30);
		
		bAddCD=new JButton("Ajouter CD");
		bAddCD.setBounds(320,340,150,30);
		
		bAddDVD=new JButton("Ajouter DVD");
		bAddDVD.setBounds(320,340,150,30);
		
		bAddD=new JButton("Ajouter Dictionnaire");
		bAddD.setBounds(320,340,150,30);
		
		bAddBD=new JButton("Ajouter BD");
		bAddBD.setBounds(320,340,150,30);
		
		bAddR=new JButton("Ajouter Roman");
		bAddR.setBounds(320,340,150,30);
		
		bAddM=new JButton("Ajouter Manuel");
		bAddM.setBounds(320,340,150,30);
		//----------------------------------------------------------------
		//Création des champs de texte
		//----------------------------------------------------------------
		tfT=new JTextField();
		tfT.setBounds(190,33,270,25);
		
		tfP=new JTextField();
		tfP.setBounds(190,63,28,25);
		
		tfID=new JTextField();
		tfID.setBounds(255,63,28,25);
		
		tfAS=new JTextField();
		tfAS.setBounds(250,93,35,25);
		
		tfR=new JTextField();
		tfR.setBounds(225,123,100,25);
		
		tfLA=new JTextField();
		tfLA.setBounds(203,153,100,25);
		
		tfA=new JTextField();
		tfA.setBounds(198,183,100,25);
		
		tfS=new JTextField();
		tfS.setBounds(194,213,35,25);
		//----------------------------------------------------------------
		//Ajout des Labels//
		//----------------------------------------------------------------
		NP.add(lSN);
		NP.add(lD);
		NP.add(lL);
		NP.add(lT);
		NP.add(lP);
		NP.add(lID);
		NP.add(lAS);
		NP.add(lR);
		NP.add(lLA);
		NP.add(lA);
		NP.add(lS);
		//----------------------------------------------------------------
		//AJouts des Boutons//
		//----------------------------------------------------------------
		NP.add(bCD);
		NP.add(bDVD);
		NP.add(bD);
		NP.add(bBD);
		NP.add(bM);
		NP.add(bR);
		NP.add(bAddCD);
		NP.add(bAddDVD);
		NP.add(bAddD);
		NP.add(bAddBD);
		NP.add(bAddR);
		NP.add(bAddM);
		//----------------------------------------------------------------
		//Action Listener sur les boutons
		//----------------------------------------------------------------
		bCD.addActionListener(this::EventButtonP);
		bDVD.addActionListener(this::EventButtonP);
		bD.addActionListener(this::EventButtonP);
		bBD.addActionListener(this::EventButtonP);
		bR.addActionListener(this::EventButtonP);
		bM.addActionListener(this::EventButtonP);
		bAddCD.addActionListener(this::EventButtonA);
		bAddDVD.addActionListener(this::EventButtonA);
		bAddD.addActionListener(this::EventButtonA);
		bAddR.addActionListener(this::EventButtonA);
		bAddBD.addActionListener(this::EventButtonA);
		bAddM.addActionListener(this::EventButtonA);
		//----------------------------------------------------------------
		//Ajoutes des Champs de texte//
		//----------------------------------------------------------------
		NP.add(tfT);
		NP.add(tfP);
		NP.add(tfID);
		NP.add(tfAS);
		NP.add(tfR);
		NP.add(tfLA);
		NP.add(tfA);
		NP.add(tfS);
		//----------------------------------------------------------------
		//Disable de tous les champs et label hormis tarif et titre//
		//----------------------------------------------------------------
		DisableAll();
		tfID.setEnabled(false);
		//----------------------------------------------------------------
		//Caractéristiques de la fenêtre
		//----------------------------------------------------------------
		NP.setSize(500,430);
	    NP.setLayout(null);
	    NP.setLocationRelativeTo(null);
	    NP.setVisible(true);
	    
	    tfID.setText(String.valueOf(IDnumber()));
	    
	    VisibleFalse();
	}
	//----------------------------------------------------------------------------------------------------------------
	//Fonction permettant de disable tous les champs de caractéristiques spécifiques (année de sortie, langue, etc...)
	//----------------------------------------------------------------------------------------------------------------
	private void DisableAll() {
		lAS.setEnabled(false);
		lR.setEnabled(false);
		lLA.setEnabled(false);
		lA.setEnabled(false);
		tfAS.setEnabled(false);
		tfR.setEnabled(false);
		tfLA.setEnabled(false);
		tfA.setEnabled(false);
	}
	//----------------------------------------------------------------
	//Evenements des boutons de produit
	//----------------------------------------------------------------
	private void EventButtonP(ActionEvent e) {
		if(e.getSource()==bCD) {
			DisableAll();
			lAS.setEnabled(true);
			tfAS.setEnabled(true);
			AllClear();
			VisibleFalse();
			bAddCD.setVisible(true);
		}
		else if(e.getSource()==bDVD) {
			DisableAll();
			lR.setEnabled(true);
			tfR.setEnabled(true);
			AllClear();
			VisibleFalse();
			bAddDVD.setVisible(true);
		}
		else if(e.getSource()==bD) {
			DisableAll();
			lLA.setEnabled(true);
			tfLA.setEnabled(true);
			AllClear();
			VisibleFalse();
			bAddD.setVisible(true);
		}
		else if(e.getSource()==bBD){
			EnableAutor();
			AllClear();
			VisibleFalse();
			bAddBD.setVisible(true);
		}
		else if(e.getSource()==bR){
			EnableAutor();
			AllClear();
			VisibleFalse();
			bAddR.setVisible(true);
		}
		else {
			EnableAutor();
			AllClear();
			VisibleFalse();
			bAddM.setVisible(true);
		}
	}
	//----------------------------------------------------------------
	//Evenement des boutons ajouter
	//----------------------------------------------------------------
	private void EventButtonA(ActionEvent e) {
		//----------------------------------------------------------------
		//Déclarations des attributs
		//----------------------------------------------------------------
		String type="";
		String ID=tfID.getText().trim();
		String Titre=tfT.getText().trim();
		String Prix=tfP.getText().trim();
		String Annee=tfAS.getText().trim();
		String Real=tfR.getText().trim();
		String Langue=tfLA.getText().trim();
		String Auteur=tfA.getText().trim();
		String Stock=tfS.getText().trim();
		//----------------------------------------------------------------
		//Vérification du contenu des champs
		//----------------------------------------------------------------
		if(Titre.isEmpty()) JOptionPane.showMessageDialog(NP,"ATTENTION : \"Titre\" non rempli !!");
		else if(Prix.isEmpty()) JOptionPane.showMessageDialog(NP,"ATTENTION : \"Tarif\" non rempli !!");
		else if(Stock.isEmpty()) JOptionPane.showMessageDialog(NP,"ATTENTION : \"Stock\" non rempli !!");
		if(e.getSource()==bAddCD) {
			if(Annee.isEmpty()) JOptionPane.showMessageDialog(NP,"ATTENTION : \"Année de sortie\" non rempli !!");
			else type="CD";
		}
		else if(e.getSource()==bAddDVD) {
			if(Real.isEmpty()) JOptionPane.showMessageDialog(NP,"ATTENTION : \"Réalisateur\" non rempli !!");
			else type="DVD";
		}
		else if(e.getSource()==bAddD) {
			if(Langue.isEmpty()) JOptionPane.showMessageDialog(NP,"ATTENTION : \"Langue\" non rempli !!");
			else type="Dictionnaire";
		}
		else if(e.getSource()==bAddBD) {
			if(Auteur.isEmpty()) JOptionPane.showMessageDialog(NP,"ATTENTION : \"Auteur\" non rempli !!");
			else type="BD";
		}
		else if(e.getSource()==bAddM) {
			if(Auteur.isEmpty()) JOptionPane.showMessageDialog(NP,"ATTENTION : \"Auteur\" non rempli !!");
			else type="Manuel";
		}
		else {
			if(Auteur.isEmpty()) JOptionPane.showMessageDialog(NP,"ATTENTION : \"Auteur\" non rempli !!");
			else type="Roman";
		}
		if (!type.isEmpty()) {
		JOptionPane.showMessageDialog(NP,type+" : "+Titre+" ajouté.");
		AjoutPrSQL(ID,type,Titre,Annee,Real,Langue,Auteur,Prix,Stock);
		AllClear();
		tfID.setText(String.valueOf(IDnumber()));
		VisibleFalse();
		}
	}
	//----------------------------------------------------------------
	//Fonction pour activer le textfield "auteur" afin d'éviter une répétition de code
	//----------------------------------------------------------------
	private void EnableAutor() {
		DisableAll();
		lA.setEnabled(true);
		tfA.setEnabled(true);
	}
	//----------------------------------------------------------------
	//Fonction permettant de vider tout les TextField
	//----------------------------------------------------------------
	private void AllClear() {
		tfAS.setText("");
		tfR.setText("");
		tfLA.setText("");
		tfA.setText("");
		tfS.setText("");
	}
	//----------------------------------------------------------------
	//Fonction rendant tous les boutons d'ajouts inivsibles
	//----------------------------------------------------------------
	private void VisibleFalse() {
		 bAddCD.setVisible(false);
		 bAddDVD.setVisible(false);
		 bAddD.setVisible(false);
		 bAddBD.setVisible(false);
		 bAddR.setVisible(false);
		 bAddM.setVisible(false);
	}
	//----------------------------------------------------------------
	//Fonction permettant l'ajout dans la base de données
	//----------------------------------------------------------------
	private void AjoutPrSQL(String ID, String type, String title, String year, String realisator, String Language, String Autor, String Price, String Stock) {
		try {
			String query="INSERT INTO `produits` (`ID`, `Type`, `Titre`, `Année`, `Réalisateur`, `Langue`, `Auteur`, `Tarif`, `Stock`) VALUES ('"+ID+"', '"+type+"', '"+title+"', '"+year+"', '"+realisator+"', '"+Language+"', '"+Autor+"', '"+Price+"', '"+Stock+"');";
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			st.executeUpdate(query);
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	//----------------------------------------------------------------
	//Fonction permettant de récupérer le prochain ID
	//----------------------------------------------------------------
	private int IDnumber() {
		int res;
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT p.ID from produits as p where p.ID NOT IN (SELECT DISTINCT p1.ID FROM produits as p1, produits as p2 WHERE p1.ID!=p2.ID AND p1.ID<p2.ID)");
			while(rst.next()) {
				res=rst.getInt("ID")+1;
				return res;
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return 1;
	}
}
