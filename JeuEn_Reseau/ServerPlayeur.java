
package com.mycompany.jeureseau;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ServerPlayeur extends Playeur implements Runnable{
    private Server serveur;

	public ServerPlayeur(Socket socket, int numero, Server serveur) {
		super(socket, numero);
		this.serveur = serveur;
	}

	public void envoyerNumero() {
		out.println(Coeff.NUM + " " + numero);
	}
	void traiterMessage(String message) {
		int type;
		Scanner scan = new Scanner(message);

		type =scan.nextInt();

		switch (type) {
		case Coeff.CHOIX : choix = Safidy.valueOf(scan.next());
		serveur.prevenirAdversaire(numero);
		serveur.examinerChoix();
		break;
		}
	}

	public void aGagne() {
		super.aGagne();
		out.println(Coeff.GAGNE);
		if (partieGagnee()) out.println(Coeff.PARTIE_GAGNEE); 
	}

	public void aPerdu() {
		super.aPerdu();
		out.println(Coeff.PERDU);
		if (adversaire.partieGagnee()) out.println(Coeff.PARTIE_PERDUE);
	}

	public void egalite() {
		System.out.println("egalite");
		super.egalite();
		out.println(Coeff.EGALITE);
	}

	public void run() {
		String message;

		try {
			message = in.readLine();
			while (!jeuFini) {
				traiterMessage(message);
				message = in.readLine();
			}
		}
		catch(IOException exc) {

		}
	}
}
