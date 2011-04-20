/*
 * Copyright 2010 Antoine Seilles (Natoine)
 *   This file is part of dao-consultation.

    controler-resource is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    controler-resource is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with dao-consultation.  If not, see <http://www.gnu.org/licenses/>.

 */
package fr.natoine.dao.consultation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import fr.natoine.model_consultation.Consultation;
import fr.natoine.model_resource.URI;
import fr.natoine.model_user.UserAccount;

public class DAOConsultation 
{
	private EntityManagerFactory emf = null ;
	
	public DAOConsultation(EntityManagerFactory _emf)
	{
		emf = _emf ;
	}

	//CreateConsultation
	/**
	 * Creates a consultation
	 * @param _person
	 * @param _start
	 * @param _stop
	 * @param _uri_consulted
	 * @param _context_creation
	 */
	public boolean createsConsultation(UserAccount _person, Date _start, Date _stop, URI _uri_consulted, String _context_creation)
	{
		if(_start != null && _stop != null)
		{
			Consultation consultation = new Consultation();
			consultation.setPerson(_person);
			consultation.setStart(_start);
			consultation.setStop(_stop);
			consultation.setConsulted(_uri_consulted);
			consultation.setContextCreation(_context_creation);
			consultation.setDuration();
			//EntityManagerFactory emf = this.setEMF();
			EntityManager em = emf.createEntityManager();
	        EntityTransaction tx = em.getTransaction();
	        try
	        {
		        tx.begin();
		        if(_person.getId() != null)
				{
					UserAccount _synchro_person = em.find(UserAccount.class, _person.getId());
					if(_synchro_person != null) consultation.setPerson(_synchro_person);
				}
		        if(_uri_consulted.getId() != null)
				{
					URI _synchro_uri = em.find(URI.class, _uri_consulted.getId());
					if(_synchro_uri != null) consultation.setConsulted(_synchro_uri);
				}
		        em.persist(consultation);
		        tx.commit();
		       // em.close();
		        return true ;
	        }
	        catch(Exception e)
	        {
	        	System.out.println("[CreateConsultation.createConsultation] fails to create a consultation"
	        			+ " context creation : " + _context_creation
	        			+ " person : " + _person.getLabel()
	        			+ " URI : " + _uri_consulted.getEffectiveURI()
	        			+ " cause : " + e.getMessage());
	        	tx.rollback();
	        	//em.close();
	        	return false;
	        }
		}
		else 
		{
			System.out.println("[CreateConsultation.createConsultation] fails to create a consultation One of the Date is null");
			return false ;
		}
	}
	
	//RetrieveConsultation
	/**
	 * Retrieves all the consultations of a Person
	 * @param _person
	 * @return
	 */
	public List<Consultation> retrieveConsultations(UserAccount _person)
	{
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try
		{
			tx.begin();
			List<Consultation> consultations = ((List<Consultation>) em.createQuery("from Consultation where person = ?").setParameter(1, _person).getResultList())  ;
			tx.commit();
			//em.close();
			return consultations;
		}
		catch(Exception e)
		{
			//tx.commit();
			tx.rollback();
			//em.close();
			System.out.println("[RetrieveConsultation.retrieveConsultations] unable to retrieve Consultations"
					+ " cause : " + e.getMessage());
			return new ArrayList<Consultation>();
		}
	}
	/**
	 * Retrieves all the consultations relative to a URI (URI object)
	 * @param _consulted_resource
	 * @return
	 */
	public List<Consultation> retrieveConsultations(URI _consulted_resource)
	{
		//EntityManagerFactory emf = this.setEMF();
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try{
			tx.begin();
			List<Consultation> consultations = ((List<Consultation>) em.createQuery("from Consultation where consulted = ?").setParameter(1, _consulted_resource).getResultList()) ;
			tx.commit();
			//em.close();
			return consultations;
		}
		catch(Exception e)
		{
			//tx.commit();
			tx.rollback();
			//em.close();
			System.out.println("[RetrieveConsultation.retrieveConsultations] unable to retrieve Consultations"
					+ " cause : " + e.getMessage());
			return new ArrayList<Consultation>();
		}
	}
	/**
	 * Retrieves all the consultations relative to one URI (string for the URI)
	 * @param _effective_uri
	 * @return
	 */
	public List<Consultation> retrieveConsultations(String _effective_uri)
	{
		//EntityManagerFactory emf = this.setEMF();
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try{
			tx.begin();
			URI true_uri = (URI) em.createQuery("from URI where effectiveURI = ?").setParameter(1, _effective_uri).getSingleResult() ;
			tx.commit();
			if(true_uri != null) return this.retrieveConsultations(true_uri);
			else
			{
				System.out.println("[RetrieveConsultation.retrieveConsultations] unable to retrieve Consultations cause : no URI " + _effective_uri );
				return new ArrayList<Consultation>();
			}
		}
		catch(Exception e)
		{
			//tx.commit();
			tx.rollback();
			//em.close();
			System.out.println("[RetrieveConsultation.retrieveConsultations] unable to retrieve Consultations"
					+ " cause : " + e.getMessage());
			return new ArrayList<Consultation>();
		}
	}
}