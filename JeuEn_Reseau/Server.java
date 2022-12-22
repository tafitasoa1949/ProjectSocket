
package com.mycompany.jeureseau;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerPlayeur[] lesJoueurs;
	private int numSuivant = 0;
	public Server(ServerPlayeur[] lesJoueurs) {
		this.lesJoueurs = lesJoueurs;
	}

	public void ajouterJoueur(Socket socket) {
		lesJoueurs[numSuivant] = new ServerPlayeur(socket, numSuivant, this);
		numSuivant++;
		if (numSuivant == 2) donnerFeuVert();
	}

	public void traiterMessage(String message, Playeur joueur) {
		System.out.println(message);
		Scanner scan = new Scanner(message);
		int type = scan.nextInt();
	}

	public void chercherJoueurGagnant() {
		attendre();
		Safidy choix0 = lesJoueurs[0].choix;
		Safidy choix1 = lesJoueurs[1].choix;
		if (choix0.equals(choix1)) egalite();
		if (choix0 == Safidy.CISEAUX) {
			if (choix1 ==  Safidy.FEUILLE) validerGain(0);
			else if  (choix1 ==  Safidy.CAILLOU) validerGain(1);
		}
		else if (choix0 == Safidy.FEUILLE)  {
			if (choix1 ==  Safidy.CAILLOU) validerGain(0); 
			else if  (choix1 ==  Safidy.CISEAUX) validerGain(1); 
		}
		else if (choix0 == Safidy.CAILLOU)  {
			if (choix1 ==  Safidy.CISEAUX) validerGain(0);  
			else if  (choix1 ==  Safidy.FEUILLE) validerGain(1); 
		}
	}
	
	public void egalite() {	
		lesJoueurs[0].egalite();	
		lesJoueurs[1].egalite();
	}
	
	public void annulerChoix() {
		lesJoueurs[0].choix = null;
		lesJoueurs[1].choix = null;
	}

	void validerGain(int numJoueur) {
		lesJoueurs[numJoueur].aGagne();
		lesJoueurs[1 - numJoueur].aPerdu();		
	}
	
	public void donnerFeuVert() {
		for (ServerPlayeur j : lesJoueurs) j.envoyerNumero();
	}

	public static void main(String[] arg) throws Exception {
		int portChifoumi = 4567;
		ServerSocket  receptionniste =  new ServerSocket(portChifoumi);		
		ServerPlayeur[] lesJoueurs = new ServerPlayeur[2];

		Server serveur = new Server(lesJoueurs);
		for (int i = 0; i < 2; i++)	serveur.ajouterJoueur(receptionniste.accept());

		new Server(lesJoueurs);
	}
	
	public void prevenirAdversaire(int numero) {
		Safidy choix = lesJoueurs[numero].choix;
		lesJoueurs[1 - numero].out.println(Coeff.CHOIX_ADVERSAIRE + " " + choix);
	}

	public void attendre() {
		try {
			Thread.sleep(3000);
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public void examinerChoix() {
		boolean fait = ((lesJoueurs[0].choix != null) && (lesJoueurs[1].choix != null));
		System.out.println(fait);
		if (fait) {
			chercherJoueurGagnant();
			annulerChoix();
		}
	}
}
