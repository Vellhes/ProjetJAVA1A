import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class NOrder {
	static Connection cnx;
	static Statement st;
	static ResultSet rst;
	private JFrame NO;
	private JPanel pMain;
	private JLabel lID, lCL;
	private JTextField tfID;
	private JComboBox<String> cmbCL;
	private JButton bAdd;
	int i;
	String IDcl;
	String Ncl;
	String cellClients[]=new String [1000];
	String fidel;
	NOrder(){
		//----------------------------------------------------------------
		//Cr�ation de la fen�tre
		//----------------------------------------------------------------
		NO = new JFrame();
		//----------------------------------------------------------------
		//Liaison a la BdD permettant de r�cuperer la liste des clients existants
		//----------------------------------------------------------------
		try {
			i=1;
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("Select * from clients order by ID");
			while(rst.next()) {
				cellClients[i]=rst.getInt("ID")+" - "+rst.getString("Nom")+" "+rst.getString("Pr�nom");
				i++;
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		//----------------------------------------------------------------
		//Placement des Clients dans un tableau
		//----------------------------------------------------------------
		String tabClients[]= new String[i];
		tabClients[0]="------";
		int n=1;
		while (n!=i) {
			tabClients[n]=cellClients[n];
			n++;
		}
		//----------------------------------------------------------------
		//Cr�ation du panel principal
		//----------------------------------------------------------------
		pMain = new JPanel();
		pMain.setBorder(new EmptyBorder(5, 5, 5, 5));
		pMain.setLayout(null);
		//----------------------------------------------------------------
		//Cr�ation des labels
		//----------------------------------------------------------------
		lID = new JLabel("ID : ");
		lID.setBounds(10, 11, 46, 14);

		lCL = new JLabel("Client : ");
		lCL.setBounds(10, 57, 46, 14);
		//----------------------------------------------------------------
		//Cr�ation du champ de texte
		//----------------------------------------------------------------
		tfID = new JTextField();
		tfID.setBounds(63, 8, 34, 20);
		tfID.setColumns(10);
		tfID.setEnabled(false);
		//----------------------------------------------------------------
		//Cr�ation de la combo box
		//----------------------------------------------------------------		
		cmbCL = new JComboBox<String>(tabClients);
		cmbCL.setBounds(63, 53, 197, 22);
		//----------------------------------------------------------------
		//ActionListener permettant de r�cup�rer l'id et le nom d'un client 
		// puis de v�rifier sa fid�lit� lors de sa s�lection
		//----------------------------------------------------------------
		cmbCL.addActionListener((ActionListener) new ActionListener() {     
		     public void actionPerformed(ActionEvent e) {
		    	IDcl = cmbCL.getSelectedItem().toString();
		    	Ncl = IDcl.substring(4);
		    	IDcl = IDcl.substring(0,1);
		    	try {
					cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
					st=cnx.createStatement();
					rst=st.executeQuery("SELECT * from clients where ID=\""+IDcl+"\";");
					while(rst.next()) {
						fidel=rst.getString("Fidelit�");
					}
				}catch(SQLException f) {
					System.out.println(f.getMessage());
				}
		     }
		});
		//----------------------------------------------------------------
		//Cr�ation du bouton d'ajout
		//----------------------------------------------------------------		
		bAdd = new JButton("Ajouter Commande");
		bAdd.setBounds(10, 107, 250, 23);
		bAdd.addActionListener(this::ButtonEvent);
		//----------------------------------------------------------------
		//Ajout des composants au panel principal
		//----------------------------------------------------------------		
		pMain.add(bAdd);
		pMain.add(cmbCL);
		pMain.add(lCL);
		pMain.add(lID);
		pMain.add(tfID);
		//----------------------------------------------------------------
		//Caract�ristiques de la fenetre
		//----------------------------------------------------------------
		NO.setContentPane(pMain);
		NO.setTitle("Nouvel Emprunt");
		NO.setSize(289, 179);
		NO.setLayout(null);
	    NO.setLocationRelativeTo(null);
	    NO.setVisible(true);
		//----------------------------------------------------------------
		//Auto incr�mentation du textfield pour l'id de la commande
		//----------------------------------------------------------------
	    tfID.setText(String.valueOf(IDnumber()));
	}
	//----------------------------------------------------------------
	//Evenement du bouton ajouter
	//----------------------------------------------------------------	
	private void ButtonEvent(ActionEvent e) {
		if(IDcl=="-") {
			JOptionPane.showMessageDialog(NO,"Veuillez s�lectionner un client.");
		}
		else {
		AjoutCommandeSQL(IDcl,fidel);
		JOptionPane.showMessageDialog(NO,"Commande n�"+tfID.getText()+" ajout�e pour le client "+Ncl+".");
	    tfID.setText(String.valueOf(IDnumber()));
	    cmbCL.setSelectedIndex(0);
		}
	}
	//----------------------------------------------------------------
	//Fonction permettant de r�cup�rer l'ID de la prochaine commande � ajouter
	//----------------------------------------------------------------
	private int IDnumber() {
		int res;
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT c.ID from commande as c where c.ID NOT IN ( SELECT DISTINCT c1.ID FROM commande as c1, commande as c2 WHERE c1.ID!=c2.ID AND c1.ID<c2.ID)");
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
	//Fonction permettant l'ajout de la commande dans la base de donn�es
	//----------------------------------------------------------------
	private void AjoutCommandeSQL(String IDcl,String fidel) {
		Date ToDay = new Date(System.currentTimeMillis());
		try {
			String query=("INSERT INTO `commande` (`ID`, `ID_client`, `reduction`, `date_d`) VALUES ('"+tfID.getText().trim()+"', '"+IDcl+"', '"+fidel+"', '"+ToDay+"');");
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			st.executeUpdate(query);
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
