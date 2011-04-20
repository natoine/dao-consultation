package fr.natoine.controler.consultation;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import fr.natoine.dao.consultation.DAOConsultation;
import fr.natoine.model_consultation.Consultation;
import fr.natoine.model_resource.Resource;
import fr.natoine.model_resource.URI;
import fr.natoine.model_user.Person;
import fr.natoine.model_user.UserAccount;
import junit.framework.TestCase;

public class ConsultationControlerTest extends TestCase 
{
	private EntityManagerFactory emf_consultation = Persistence.createEntityManagerFactory("consultation");
	
	public ConsultationControlerTest(String name) 
	{		    
		super(name);
	}
	
	public void testCreateConsultation()
	{
		//CreateConsultation _controlerConsultation = new CreateConsultation();
		DAOConsultation _controlerConsultation = new DAOConsultation(emf_consultation);
		String context = "Test ControlerConsultation";
		URI uri_test = new URI();
		uri_test.setEffectiveURI("http://www.test_consultation.fr");
		Person user = new Person();
		user.setFirstName("test_consultation");
		user.setLastName("test_consultation");
		//user.setContextInscription(context);
		user.setMail("test_consultation@gmail.com");
		Resource represents = new Resource();
		represents.setRepresentsResource(uri_test);
		represents.setLabel("test de consultation");
		user.setRepresents(represents);
		UserAccount person = new UserAccount();
		person.setUser(user);
		person.setInscription(new Date());
		person.setPseudonyme("test consultation pseudo");
		person.setPassword("password");
		person.setRepresents(represents);
		person.setLabel("test label");
		Date start = new Date();
		Date stop = new Date();
		_controlerConsultation.createsConsultation(person, start, stop, uri_test, context);
	}
	
	public void testRetrieveConsultation()
	{
		//RetrieveConsultation controlerConsultation = new RetrieveConsultation();
		DAOConsultation controlerConsultation = new DAOConsultation(emf_consultation);
		List<Consultation> consultations = controlerConsultation.retrieveConsultations("http://www.test_consultation.fr") ;
		for(Consultation consultation : consultations)
		{
			System.out.println("[ConsultationControlerTest.testRetrieveConsultation] Consultation"
					+ " person : " + consultation.getPerson().getLabel()
					+ " start : " + consultation.getStart()
					+ " stop : " + consultation.getStop()
					+ " uri : " + consultation.getConsulted().getEffectiveURI());
		}
	}
}
