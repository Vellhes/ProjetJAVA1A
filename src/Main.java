import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Main{
	static Connection cnx;
	static Statement st;
	static ResultSet rst;
	private JFrame f;
	private JMenu mclient, mproduit, morder, memprunt; 
	private JMenuItem c1, c2, p1, p2, e1, e2, e3, o1, o2;
    private JLabel icon;
	Main(){
		f = new JFrame();
		JMenuBar mb = new JMenuBar();
		icon = new JLabel(new ImageIcon("images/Magasin.png"));
		//----------------------------------------------------------------
		//Menu Client
		//----------------------------------------------------------------
        mclient=new JMenu("Clients");
        mclient.setMnemonic('C');
        c1=new JMenuItem("Nouveau");
        c2=new JMenuItem("Liste");
        c1.addActionListener(this::MenuClient);
        c2.addActionListener(this::MenuClient);
        //----------------------------------------------------------------
        //Ajouts des sous menus au menu "Client"
        //----------------------------------------------------------------
        mclient.add(c1);
        mclient.add(c2);
        //----------------------------------------------------------------
        //Menu Produit
        //----------------------------------------------------------------
        mproduit=new JMenu("Produits");
        mproduit.setMnemonic('P');        
        p1=new JMenuItem("Nouveau"); 
        p2=new JMenuItem("Liste");
        p1.addActionListener(this::MenuProduit);
        p2.addActionListener(this::MenuProduit);
        //----------------------------------------------------------------
        //Ajouts des sous menus au menu "produits"
        //----------------------------------------------------------------
        mproduit.add(p1);
        mproduit.add(p2);
        //----------------------------------------------------------------
        //Menu Commande
        //----------------------------------------------------------------
        morder=new JMenu("Commande");
        morder.setMnemonic('C'); 
        o1=new JMenuItem("Nouveau");
        o2=new JMenuItem("Liste");
        o1.addActionListener(this::MenuCommande);
        o2.addActionListener(this::MenuCommande);
        //----------------------------------------------------------------
        //Ajouts des sous menus au menu "Commande"
        //----------------------------------------------------------------
        morder.add(o1);
        morder.add(o2);
        //----------------------------------------------------------------
        //Menu Emprunt
        //----------------------------------------------------------------
        memprunt=new JMenu("Emprunt");
        memprunt.setMnemonic('E'); 
        e1=new JMenuItem("Nouveau");
        e2=new JMenuItem("Retour");
        e3=new JMenuItem("Liste");
        e1.addActionListener(this::MenuEmprunt);
        e2.addActionListener(this::MenuEmprunt);
        e3.addActionListener(this::MenuEmprunt);
        //----------------------------------------------------------------
        //Ajouts des sous menus au menu "Emprunt"
        //----------------------------------------------------------------
        memprunt.add(e1);
        memprunt.add(e2);
        memprunt.add(e3);
        //----------------------------------------------------------------
        //Ajout des menus à la barre de Menus
        //----------------------------------------------------------------
        mb.add(mclient);
        mb.add(mproduit);
        mb.add(morder);
        mb.add(memprunt);
        //----------------------------------------------------------------
        //Page d'accueil
        //----------------------------------------------------------------
        f.setJMenuBar(mb);
        f.add(icon);
	    f.setTitle("Projet JAVA");
	    f.setSize(800, 325);
	    f.setLocationRelativeTo(null);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setVisible(true);
	}
	private void MenuClient(ActionEvent e) {      
		if(e.getSource()==c1)	
			new NClient();
		else if(e.getSource()==c2)
			new LClient();
	}
	private void MenuProduit(ActionEvent e)  {
		if(e.getSource()==p1)
			new NProduit();
		else if(e.getSource()==p2)
			new LProduit();
	}
	private void MenuEmprunt(ActionEvent e) {
		if(e.getSource()==e1) {
		int i=0;
		try {
			cnx=DriverManager.getConnection("jdbc:mysql://localhost:3308/projet_java?serverTimezone=UTC", "root", "");
			st=cnx.createStatement();
			rst=st.executeQuery("SELECT ID from commande");
			while(rst.next()) {
				i++;
			}
		}catch(SQLException r) {
			System.out.println(r.getMessage());
		}
		if(i!=0) new NEmprunt();
		else JOptionPane.showMessageDialog(f,"Aucune commande existante, veuillez en créer une avant de créer un emprunt.");
		}
		else if(e.getSource()==e2)
			new REmprunt();
		else if(e.getSource()==e3)
			new LEmprunt();
	}
	private void MenuCommande(ActionEvent e) {
		if(e.getSource()==o1)
			new NOrder();
		else if(e.getSource()==o2)
			new LOrder();
	}
	    public static void main(String[] args) {
	    	new Main();
	}

}
