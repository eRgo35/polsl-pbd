package pl.polsl.zti.db1.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import pl.polsl.zti.db1.ConfigConsts;
import pl.polsl.zti.db1.domain.Client;

public class ClientDaoImplJpa implements ClientDao {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY;

    static {
        ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory(ConfigConsts.PERSISTANCE_UNIT_NAME);
    }

    @Override
    public Client getClient(int id) {
//    	return null;
        final EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        final Client theClient = entityManager.find(Client.class, id);
        entityManager.close();
        return theClient;
    }

    @Override
    public List<Client> getClients() {
//   	    return null;
        final EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        final Query query = entityManager.createQuery("select c from Client as c", Client.class);
        // działa w Hibernate, ale nie w EclipseLink:
//        final Query query = entityManager.createQuery("from Client", Client.class);
        final List<Client> clientList = query.getResultList();
        entityManager.close();
        return clientList;
    }

    @Override
    public List<Client> getClients(String name) {
//   	    return null;
        if (name == null || name.trim().isEmpty()) {
            return getClients();
        }

        final EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        final Query query = entityManager.createQuery("select c from Client as c where c.name like :param", Client.class);
        query.setParameter("param", name);
        final List<Client> clientList = query.getResultList();
        entityManager.close();
        return clientList;
    }

    @Override
    public void insertClients(List<Client> clients) {
        //TODO Implementacja
    	final EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
    	EntityTransaction transaction = entityManager.getTransaction();
    	transaction.begin();
    	
    	for (Client client : clients) {    		
    		entityManager.persist(client);
    	}
    	
    	transaction.commit();
    	entityManager.close();
    }

    @Override
    public void updateClient(Client client) {
        //TODO Implementacja
    	final EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
    	EntityTransaction transaction = entityManager.getTransaction();
    	transaction.begin();
    	
    	final Client managedClient = entityManager.find(Client.class, client.getId());
    	
    	managedClient.setId(client.getId());
    	managedClient.setNo(client.getNo());
    	managedClient.setName(client.getName());
    	managedClient.setSsn(client.getSsn());
    	managedClient.setAddress(client.getAddress());
    	
    	transaction.commit();
    	entityManager.close();
    }

    @Override
    public void deleteClient(Client client) {
        final EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // wersja 1
//        final Client managedClient = entityManager.merge(client);
        // wersja 2 - bardziej efektywna
        final Client managedClient = entityManager.find(Client.class, client.getId());

        entityManager.remove(managedClient);

        transaction.commit();
        entityManager.close();
    }

    @Override
    public void deleteClient(int id) {
        // Zaimplementować usuwanie klienta o podanym id
        // Najpierw wydobyć klienta, potem go usunąć

    	//TODO Implementacja
    	final EntityManager entityManager = ENTITY_MANAGER_FACTORY.createEntityManager();
    	EntityTransaction transaction = entityManager.getTransaction();
    	transaction.begin();
    	
    	final Client managedClient = entityManager.find(Client.class, id);
    	
    	entityManager.remove(managedClient);
    	
    	transaction.commit();
    	entityManager.close();
    }
}
