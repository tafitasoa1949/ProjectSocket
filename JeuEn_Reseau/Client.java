
package com.mycompany.jeureseau;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Client extends Box implements ActionListener, Observer{
    private JButton feuilleM = new JButton(new ImageIcon("Caille.jpg"));
	private JButton ciseauxM = new JButton(new ImageIcon("Ciseaux.jpg"));
	private JButton caillouM= new JButton(new ImageIcon("Feuille.jpg"));
	private JButton feuilleA = new JButton(new ImageIcon("Caille.jpg"));
	private JButton ciseauxA = new JButton(new ImageIcon("Ciseaux.jpg"));
	private JButton caillouA= new JButton(new ImageIcon("Feuille.jpg"));
	private JLabel nombrePoints = new JLabel("0");
	private JLabel action = new JLabel("Wait for the opponent");
	private ClientPlayeur joueur;
	private JLabel nombrePointsAdversaire = new JLabel("0");
	private Color couleurBouton;
	private JButton boutonChoixAdversaire;
	private JButton boutonChoisi;

	public Client(ClientPlayeur joueur) {
		super(BoxLayout.Y_AXIS);
		this.joueur = joueur;
		joueur.addObserver(this);

		JPanel panneau = new JPanel();
		panneau.add(new JLabel("Playeur : "));
		panneau.add(action);
		add(panneau);

		panneau = new JPanel();
		panneau.add(new JLabel("Number of points : "));
		panneau.add(nombrePoints);
		add(panneau);

		panneau = new JPanel();
		panneau.add(feuilleM);
		panneau.add(ciseauxM);
		panneau.add(caillouM);
		add(panneau);
		if (!joueur.jouer) {
			feuilleM.setEnabled(false);
			ciseauxM.setEnabled(false);
			caillouM.setEnabled(false);
		}
		else action.setText("Let's Play");
		feuilleM.setActionCommand(Safidy.FEUILLE.toString());
		ciseauxM.setActionCommand(Safidy.CISEAUX.toString());
		caillouM.setActionCommand(Safidy.CAILLOU.toString());
		feuilleM.addActionListener(this);
		ciseauxM.addActionListener(this);
		caillouM.addActionListener(this);

		add(new JLabel("the opponent"));
		panneau = new JPanel();
		panneau.add(feuilleA);
		panneau.add(ciseauxA);
		panneau.add(caillouA);
		add(panneau);

		panneau = new JPanel();
		panneau.add(new JLabel("Number of points(the opponent): "));
		panneau.add(nombrePointsAdversaire);
		add(panneau);

		couleurBouton = feuilleM.getBackground();
	}

	public void actionPerformed(ActionEvent evt) {
		boutonChoisi = (JButton) evt.getSource();
		boutonChoisi.setBackground(Color.RED);
		joueur.choix = Safidy.valueOf(boutonChoisi.getActionCommand());
		joueur.out.println(Coeff.CHOIX + " " + boutonChoisi.getActionCommand());
		action.setText("miandry choix ny adversaire");
		feuilleM.setEnabled(false);
		ciseauxM.setEnabled(false);
		caillouM.setEnabled(false);
		joueur.jouer = false;
		indiquerChoixAdversaire();
	}

	public void indiquerChoixAdversaire() {
		Safidy choixAdversaire = joueur.adversaire.choix;

		if ((joueur.choix != null) &&( choixAdversaire != null)) {
			if (choixAdversaire == Safidy.FEUILLE) boutonChoixAdversaire = feuilleA;
			else if (choixAdversaire == Safidy.CISEAUX) boutonChoixAdversaire = ciseauxA;
			else if (choixAdversaire == Safidy.CAILLOU) boutonChoixAdversaire = caillouA;
			boutonChoixAdversaire.setBackground(Color.RED);
		}
	}
	
	public void update(Observable o, Object obj) {
		nombrePoints.setText(Integer.toString(joueur.nbPoints));
		nombrePointsAdversaire.setText(Integer.toString(joueur.adversaire.nbPoints));
		indiquerChoixAdversaire();		
		if (joueur.partieGagnee) {
			action.setText("You win");
			feuilleM.setEnabled(false);
			ciseauxM.setEnabled(false);
			caillouM.setEnabled(false);
		}
		else if (joueur.partiePerdue) {
			action.setText("You lost");
			feuilleM.setEnabled(false);
			ciseauxM.setEnabled(false);
			caillouM.setEnabled(false);
		}  
		else if (joueur.jouer) {
			if (boutonChoisi != null) boutonChoisi.setBackground(couleurBouton);
			feuilleM.setEnabled(true);
			ciseauxM.setEnabled(true);
			caillouM.setEnabled(true);
			action.setText("Let's Play");
			if (boutonChoixAdversaire != null) 
				boutonChoixAdversaire.setBackground(couleurBouton); 
		}
	}
}
