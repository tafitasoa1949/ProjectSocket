
package com.mycompany.jeureseau;

import java.net.Socket;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClientPlayeur extends Playeur implements Runnable{
    public ClientPlayeur() {}

	public ClientPlayeur(Socket socket)  throws Exception{	
		super(socket);
		adversaire = new ClientPlayeur();
	}	

	public void traiterMessage(String message) {
		Scanner scan = new Scanner(message);
		int type = scan.nextInt();
		switch(type) {
		case Coeff.NUM : 
			numero = scan.nextInt();
			jouer = true;
			break;
		case Coeff.GAGNE : 
			aGagne();
			break;
		case Coeff.PERDU : 
			aPerdu();
			break;
		case Coeff.EGALITE : 
			egalite();
			break;
		case Coeff.PARTIE_GAGNEE : 
			partieGagnee = true;
			jeuFini = true;
			break;
		case Coeff.PARTIE_PERDUE : 
			partiePerdue = true;
			jeuFini = true;
			break;
		case Coeff.CHOIX_ADVERSAIRE : 
			adversaire.choix = Safidy.valueOf(scan.next());
			break;
		}
		setChanged();
		notifyObservers();
	}

	public void run() {
		while(!jeuFini)
			try {
				String message = in.readLine();
				traiterMessage(message);
			}
		catch(Exception exc) {
			exc.printStackTrace();
		}
	}

	public static void main(String[] arg) throws Exception {
		int portChifoumi = 4567;

		String nomMachine = JOptionPane.showInputDialog
		(null, "Sur quelle machine est le serveur  ?");

		Socket socket = new Socket(nomMachine, portChifoumi);
		ClientPlayeur joueur = new ClientPlayeur (socket);
		JFrame fenetre = new JFrame();
		fenetre.setContentPane(new Client(joueur));
		fenetre.pack();
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.setVisible(true);
	}
}
