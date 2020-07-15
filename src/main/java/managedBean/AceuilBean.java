package managedBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

import entities.Conge;
import entities.OperateurC;
import entities.Utilitaire;
import persistence.CongeDAO;

@ManagedBean
@ViewScoped
public class AceuilBean {

	@ManagedProperty(value = "#{appManagerBean}")
	private AppManagerBean appManagerBean;
	private List<Conge> mesConges;
	private HttpSession mySession;
	private ArrayList<Conge> allDemandesSentToMe;
	private boolean interimat;
	private ArrayList<Conge> allMyDemandesRejettes;
	private ArrayList<Conge> allMyDemandesValides;
	private int allDemandesSentToMeSize;
	private String monCongeStatut;
	private String interimatStatut;
	private OperateurC monCompte;

	/**
	 * 
	 * Demandes de cong�
	 * 
	 */

	private Conge congeSelected;
	private String motifDecision;

	public AceuilBean() {

	}

	@PostConstruct
	public void init() {

		mySession = Utilitaire.getSession();
		monCompte = (OperateurC) mySession.getAttribute("sessionUser");
		monCongeStatut = "";
		interimatStatut = "";
		loadAllDemandeSent();
	}

	private void loadAllDemandeSent() {
		// TODO Auto-generated method stub
		allDemandesSentToMe = new ArrayList<Conge>();
		allDemandesSentToMeSize = 0;
		monCompte = (OperateurC) mySession.getAttribute("sessionUser");
		for (Conge c : appManagerBean.getDemandeConges()) {
			if (c.getReferant() != null)
				if (monCompte.getId() == c.getReferant().getId()) {
					allDemandesSentToMe.add(c);
				}
			if (c.getEmployee().getId() == monCompte.getId()) {
				monCongeStatut = "Mon cong� n'est pas encore valid�.";
			}
		}
		allDemandesSentToMeSize = allDemandesSentToMe.size();
		loadMesDemandesValides();
	}

	public void loadMesDemandesValides() {

		allMyDemandesValides = new ArrayList<Conge>();

		for (Conge c : appManagerBean.getCongesValides()) {
			if (monCompte.getId() == c.getEmployee().getId()) {
				allMyDemandesValides.add(c);
				monCongeStatut = "Cong� accept�";
			}
		}

		if (!monCongeStatut.equalsIgnoreCase("Mon cong� n'est pas encore valid�.")
				&& !monCongeStatut.equalsIgnoreCase("Cong� accept�")) {
			monCongeStatut = "Pas de demande de cong�";
		}

	}

	public void loadMesDemandesRejettes() {

		allMyDemandesRejettes = new ArrayList<Conge>();
		monCompte = (OperateurC) mySession.getAttribute("sessionUser");

		for (Conge c : appManagerBean.getCongesValides()) {
			if (monCompte.getId() == c.getEmployee().getId()) {
				allMyDemandesRejettes.add(c);
			}
		}
	}

	public void checkStatus() {

		if (interimat) {

			interimatStatut = "";
		}
	}

	public void checkNotification() {

		if (appManagerBean.checkNotification(monCompte) > 0) {
			loadAllDemandeSent();
			Utilitaire.updateComponents(Arrays.asList("form1:analysedemande", "form1:analysedemande"));
		}
	}

	public void changestateConge() {

		int state = 2;
		if (congeSelected != null) {

			Conge copyConge = new Conge(congeSelected);

			if (state == 2) {
				copyConge.setState(state);
				copyConge.setDateValidation(Calendar.getInstance().getTime());
				copyConge.setMotifValidation(motifDecision);
			} else if (state == 3) {
				copyConge.setState(state);
				copyConge.setDateValidation(Calendar.getInstance().getTime());
				copyConge.setMotifRejet(motifDecision);

			}

			boolean saved = CongeDAO.saveOrUpdate(copyConge);

			if (saved) {
				motifDecision = "";
				appManagerBean.removeFromDemandeConge(copyConge);
				loadAllDemandeSent();
				Utilitaire.afficherInformation("D�cision enregistr�e");
				Utilitaire.updateForm("form1");
			} else if (!saved) {
				Utilitaire.afficherAttention("A votre attention SVP", "LA d�cision n'a pas pu �tre enregistr�e");
				Utilitaire.updateForm("form1");
			}
		}

	}

	public void changestateConge2() {

		int state = 3;
		if (congeSelected != null) {

			Conge copyConge = new Conge(congeSelected);

			if (state == 2) {
				copyConge.setState(state);
				copyConge.setDateValidation(Calendar.getInstance().getTime());
				copyConge.setMotifValidation(motifDecision);
			} else if (state == 3) {
				copyConge.setState(state);
				copyConge.setDateValidation(Calendar.getInstance().getTime());
				copyConge.setMotifRejet(motifDecision);

			}

			boolean saved = CongeDAO.saveOrUpdate(copyConge);

			if (saved) {
				motifDecision = "";
				appManagerBean.removeFromDemandeConge(copyConge);
				loadAllDemandeSent();
				Utilitaire.afficherInformation("D�cision enregistr�e");
				Utilitaire.updateForm("form1");
			} else if (!saved) {
				Utilitaire.afficherAttention("A votre attention SVP", "LA d�cision n'a pas pu �tre enregistr�e");
				Utilitaire.updateForm("form1");
			}
		}

	}

	public AppManagerBean getAppManagerBean() {
		return appManagerBean;
	}

	public void setAppManagerBean(AppManagerBean appManagerBean) {
		this.appManagerBean = appManagerBean;
	}

	public List<Conge> getMesConges() {
		return mesConges;
	}

	public void setMesConges(List<Conge> mesConges) {
		this.mesConges = mesConges;
	}

	public ArrayList<Conge> getAllDemandesSentToMe() {
		return allDemandesSentToMe;
	}

	public void setAllDemandesSentToMe(ArrayList<Conge> allDemandesSentToMe) {
		this.allDemandesSentToMe = allDemandesSentToMe;
	}

	public boolean isInterimat() {
		return interimat;
	}

	public void setInterimat(boolean interimat) {
		this.interimat = interimat;
	}

	public ArrayList<Conge> getAllMyDemandesRejettes() {
		return allMyDemandesRejettes;
	}

	public void setAllMyDemandesRejettes(ArrayList<Conge> allMyDemandesRejettes) {
		this.allMyDemandesRejettes = allMyDemandesRejettes;
	}

	public ArrayList<Conge> getAllMyDemandesValides() {
		return allMyDemandesValides;
	}

	public void setAllMyDemandesValides(ArrayList<Conge> allMyDemandesValides) {
		this.allMyDemandesValides = allMyDemandesValides;
	}

	public int getAllDemandesSentToMeSize() {
		return allDemandesSentToMeSize;
	}

	public void setAllDemandesSentToMeSize(int allDemandesSentToMeSize) {
		this.allDemandesSentToMeSize = allDemandesSentToMeSize;
	}

	public String getMonCongeStatut() {
		return monCongeStatut;
	}

	public void setMonCongeStatut(String monCongeStatut) {
		this.monCongeStatut = monCongeStatut;
	}

	public String getInterimatStatut() {
		return interimatStatut;
	}

	public void setInterimatStatut(String interimatStatut) {
		this.interimatStatut = interimatStatut;
	}

	public Conge getCongeSelected() {
		return congeSelected;
	}

	public void setCongeSelected(Conge congeSelected) {
		this.congeSelected = congeSelected;
	}

	public String getMotifDecision() {
		return motifDecision;
	}

	public void setMotifDecision(String motifDecision) {
		this.motifDecision = motifDecision;
	}

}
