import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class NClient{
	static Connection cnx;
	static Statement st;
	static ResultSet rst;
	private JFrame NC;
	private JButton badd;
	private JLabel lsurname, lname,lID;
	private JTextField tfsurname, tfname, tfID;
	private JCheckBox cbFidele;
	public NClient(){
		//----------------------------------------------------------------
		//Création de la fenêtre
		//----------------------------------------------------------------
		NC=new JFrame("Nouveau Client");
		//----------------------------------------------------------------
		//Création du Bouton "Ajouter
		//----------------------------------------------------------------
	    badd=new JButton("Ajouter");
	    badd.setBounds(380,220,80,20);
	    badd.addActionListener(this::AjoutClient);
	    //----------------------------------------------------------------
	    //Création des labels
	    //----------------------------------------------------------------
	    lsurname=new JLabel("nom : ");
	    lsurname.setBounds(70,50,100,30);
	    
	    lname=new JLabel("prénom : ");
	    lname.setBounds(70,90,100,30);
	    
	    lID=new JLabel("ID : ");
	    lID.setBounds(70,130,100,30);
	    //----------------------------------------------------------------
	    //Création des champs de texte
	    //----------------------------------------------------------------
	    tfsurname=new JTextField();
	    tfsurname.setBounds(150,50,150,30);
	    
	    tfname=new JTextField();
	    tfname.setBounds(150,90,150,30);
	    
	    tfID=new JTextField();
	    tfID.setBounds(150,130,150,30);
	    //----------------------------------------------------------------
	    //Création de la check box client fidèle
	    //----------------------------------------------------------------
	    cbFidele=new JCheckBox("Client Fidèle");
	    cbFidele.setBounds(65,170,150,30);
	    //----------------------------------------------------------------
	    //Ajouts des composants à la fiche
	    //----------------------------------------------------------------
	    NC.add(badd);
	    
	    NC.add(lsurname);
	    NC.add(lname);
	    NC.add(lID);
	    
	    NC.add(tfsurname);
	    NC.add(tfname);
	    NC.add(tfID);
	    
	    NC.add(cbFidele);
	    //----------------------------------------------------------------
	    //Caractéristiques de la fiche
	    //----------------------------------------------------------------
	    NC.setSize(500,300);
	    NC.setLayout(null);
	    NC.setLocationRelativeTo(null);
	    NC.setVisible(true);
	    tfsurname.requestFocus();
	    int intID=IDnumber();
	    String stringID=String.valueOf(intID);
	    tfID.setText(stringID);
	    tfID.setEnabled(false);
	}
	//----------------------------------------------------------------
	//Fonction permettant d'ajouter un client à la base
	//----------------------------------------------------------------
	private void AjoutClSQL(String ID,String nom,String prenom,String fid) {
		try {
			String query="INSERT INTO `clients` (`ID`, `nom`, `prénom` , `Fidelité`) VALUES ('"+ID+"', '"+prenom+"', '"+nom+"' , '"+fid+"');";
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			st.executeUpdate(query);
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	//----------------------------------------------------------------
	//Fonction permettant de récupérer l'ID du prochain client à ajouter
	//----------------------------------------------------------------
	private int IDnumber() {
		int res;
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT c.ID from clients as c where c.ID NOT IN ( SELECT DISTINCT c1.ID FROM clients as c1, clients as c2 WHERE c1.ID!=c2.ID AND c1.ID<c2.ID)");
			while(rst.next()) {
				res=rst.getInt("ID")+1;
				return res;
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return 1;
	}
	//----------------------------------------------------------------
	//Fonction appelée lors de l'appui du bouton ajouter
	//----------------------------------------------------------------
	private void AjoutClient(ActionEvent e) {
		String ID = tfID.getText().trim();
		String name = tfname.getText().trim();
		String surname = tfsurname.getText().trim();
		String fidy = "1";
		String fidn = "0";
		if(e.getSource()==badd) {
			if(surname.isEmpty()) {
				JOptionPane.showMessageDialog(NC,"ATTENTION : \"nom\" non rempli !!");
			}
			else if(name.isEmpty()){
				JOptionPane.showMessageDialog(NC,"ATTENTION : \"prénom\" non rempli !!");
			}
			else {
				if (cbFidele.isSelected()){
					AjoutClSQL(ID,name,surname,fidy);
					JOptionPane.showMessageDialog(NC,"Client Fidèle : "+ name + " "+surname+ ", ajouté");
				}
				else {
					AjoutClSQL(ID,name,surname,fidn);
					JOptionPane.showMessageDialog(NC,"Client : "+ name + " "+surname+ ", ajouté");
					
				}
				tfname.setText("");
				tfsurname.setText("");
				tfID.setText(String.valueOf(IDnumber()));
				cbFidele.setSelected(false);
				tfsurname.requestFocus();
			}
		}
	}
}
