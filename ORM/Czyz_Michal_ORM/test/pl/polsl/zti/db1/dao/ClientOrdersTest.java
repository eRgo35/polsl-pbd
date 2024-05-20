package pl.polsl.zti.db1.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityTransaction;
//import javax.persistence.Persistence;
import pl.polsl.zti.db1.ConfigConsts;
import pl.polsl.zti.db1.domain.Client;
import pl.polsl.zti.db1.domain.Order;
import pl.polsl.zti.db1.util.DbSchemaDef;
import pl.polsl.zti.db1.util.JdbcUtils;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClientOrdersTest {

    @BeforeClass
    public static void oneTimeSetUp() {
    }

    @Before
    public void setUp() {
        JdbcUtils.restoreDbSchema(new DbSchemaDef());
    }

    @Test
    public void testAddClientOrder() {
    	//fail("Odkomentować poniższy kod w trzeciej części ćwiczenia.");

        final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(ConfigConsts.PERSISTANCE_UNIT_NAME);
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Client client = new Client();
        client.setName("olo");
        Order order = new Order();
        order.setDescription("aaaa");
        order.setDate(new Date());
        order.setNo("111");
        order.setClient(client);
        client.getOrders().add(order);
        entityManager.persist(client);
        transaction.commit();
        entityManager.close();

        long countClients = (Long) JdbcUtils.executeScalar("SELECT COUNT(*) FROM CLIENTS WHERE NAME='olo'");
        long countOrders = (Long) JdbcUtils.executeScalar("SELECT COUNT(*) FROM ORDERS WHERE ORDER_NO='111'");

        assertEquals("Klient nie został dodany", 1, countClients);
        assertEquals("Zamówienie nie zostało dodane", 1, countOrders);
    }
    
    @Test
    public void testSelectClientOrder() {
    	final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(ConfigConsts.PERSISTANCE_UNIT_NAME);
    	final EntityManager entityManager = entityManagerFactory.createEntityManager();
    	
    	final EntityTransaction transaction = entityManager.getTransaction();
    	transaction.begin();
    	
    	final Client client = entityManager.find(Client.class, 1);
    	// select client0_.id as id1_1_0_, client0_.address as address2_1_0_, client0_.name as name3_1_0_, client0_.client_no as client_n4_1_0_, client0_.ssn as ssn5_1_0_ from clients client0_ where client0_.id=?
    	// Można zauważyć, że tworzona kwerenda automatycznie przypisuje aliasy do istniejących nazw pól.
    	// Sama struktura kwerendy wygląda tak samo jak tą co implementowałem wcześniej.
    	// Łącznie ze znakiem '?' który zapewne w czasie wykonywania jest podmieniany szukanyn przeze mnie ID = 1
    	
    	final Set<Order> orders = client.getOrders();
    	
    	for (Order order : orders) {
    		System.out.println(order.getNo() + ": " + order.getDescription());   		
    	}
    	// Output: 
//    	2009-05-07/20: aaa
//    	2009-05-08/33: ddd
//    	2009-05-07/22: ccc
//    	2009-05-07/21: bbb
    	
    	// select orders0_.CLIENT_ID as CLIENT_I5_1_0_, orders0_.id as id1_0_0_, orders0_.id as id1_0_1_, orders0_.CLIENT_ID as CLIENT_I5_0_1_, orders0_.ORDER_DATE as ORDER_DA2_0_1_, orders0_.ORDER_DESC as ORDER_DE3_0_1_, orders0_.ORDER_NO as ORDER_NO4_0_1_ from ORDERS orders0_ where orders0_.CLIENT_ID=?
    	// Analogicznie z zamówieniami
    	// automatycznie pobiera zamówienia zaliasowane do nazw wewnętrznych oraz uwzględnia ID = 1 poprzez znak '?'
    	
    	transaction.commit();
    	entityManager.close();
    }
    
    @Test
    public void testLazyEagerSession() {
    	final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(ConfigConsts.PERSISTANCE_UNIT_NAME);
    	final EntityManager entityManager = entityManagerFactory.createEntityManager();
    	
    	final EntityTransaction transaction = entityManager.getTransaction();
    	transaction.begin();
    	
    	final Client client = entityManager.find(Client.class, 1);
    	// kwerenda tutaj jest taka sama bo szukamy tego samego id
    	
    	transaction.commit();
    	entityManager.close();
    	
    	final Set<Order> orders = client.getOrders();
    	
    	for (Order order : orders) {
    		System.out.println(order.getNo() + ": " + order.getDescription());    		
    	}
    	
    	// Bardzo ciekawa rzecz, mianowicie sesja typu `Lazy` nie pozwala na dostęp do danych poza sesją (gdy została zamknięta)
    	// natomiast gdy w `Client.java` zmienimy ustawienia relacji z `Lazy` na `Eager` to wtedy cały test przechodzi pomyślnie.
    	
    	// select client0_.id as id1_1_0_, client0_.address as address2_1_0_, client0_.name as name3_1_0_, client0_.client_no as client_n4_1_0_, client0_.ssn as ssn5_1_0_, orders1_.CLIENT_ID as CLIENT_I5_1_1_, orders1_.id as id1_0_1_, orders1_.id as id1_0_2_, orders1_.CLIENT_ID as CLIENT_I5_0_2_, orders1_.ORDER_DATE as ORDER_DA2_0_2_, orders1_.ORDER_DESC as ORDER_DE3_0_2_, orders1_.ORDER_NO as ORDER_NO4_0_2_ from clients client0_ left outer join ORDERS orders1_ on client0_.id=orders1_.CLIENT_ID where client0_.id=?
    	// Ta kwerenda tworzona podczas `eager` jest jeszcze dłuższa niż poprzednie. 
    	// Główną zauważalną różnicą jest to, iż system używa JOIN i łączy tabele aby uzyskać potrzebne dane w jednej kwerendzie.
    }
    
    @Test
    public void testCascadesTypesInsert() {
    	final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(ConfigConsts.PERSISTANCE_UNIT_NAME);
    	final EntityManager entityManager = entityManagerFactory.createEntityManager();
    	
    	final EntityTransaction transaction = entityManager.getTransaction();
    	transaction.begin();
    	
    	final Client client = entityManager.find(Client.class, 1);
    	// select client0_.id as id1_1_0_, client0_.address as address2_1_0_, client0_.name as name3_1_0_, client0_.client_no as client_n4_1_0_, client0_.ssn as ssn5_1_0_, orders1_.CLIENT_ID as CLIENT_I5_1_1_, orders1_.id as id1_0_1_, orders1_.id as id1_0_2_, orders1_.CLIENT_ID as CLIENT_I5_0_2_, orders1_.ORDER_DATE as ORDER_DA2_0_2_, orders1_.ORDER_DESC as ORDER_DE3_0_2_, orders1_.ORDER_NO as ORDER_NO4_0_2_ from clients client0_ left outer join ORDERS orders1_ on client0_.id=orders1_.CLIENT_ID where client0_.id=?
    	// kwerenda znalezienia clienta o id 1
    	
    	Order order = new Order();
        order.setDescription("Test");
        order.setDate(new Date());
        order.setNo("1337");
        order.setClient(client);
    	// utworzono nowe zamówienie
        
        client.getOrders().add(order);
        // insert into ORDERS (CLIENT_ID, ORDER_DATE, ORDER_DESC, ORDER_NO) values (?, ?, ?, ?)
        // wykonywane jest wstawienie nowego rekordu do tabeli ORDERS
        
        entityManager.persist(client);
        
    	transaction.commit();
    	transaction.begin();
    	// część do sprawdzenia
    	
    	final Set<Order> orders = client.getOrders();
    	
    	for (Order ordert : orders) {
    		System.out.println(ordert.getNo() + ": " + ordert.getDescription());    		
    	}
    	// Przy wyświetleniu wszystkich zamówień lista wygląda następująco:
//    	2009-05-07/21: bbb
//    	1337: Test
//    	2009-05-08/33: ddd
//    	2009-05-07/22: ccc
//    	2009-05-07/20: aaa
    	// Zatem lista zamówień zaaktualizowała się w kliencie
    	
    	final Query query = entityManager.createQuery("select c from Order as c where c.description like :param", Order.class);
        query.setParameter("param", "Test");
        final List<Order> orderList = query.getResultList();
        for (Order order_tested : orderList) {
        	System.out.println(order_tested.getNo() + ": " + order_tested.getDescription());    		        	
        }
    	
        // 1337: Test
        // Co oznacza że w tabeli `Orders` wszystko wstawiło się pomyślnie
        
    	transaction.commit();
    	entityManager.close();
    }
    
    @Test
    public void testCascadesTypesUpdate() {
    	final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(ConfigConsts.PERSISTANCE_UNIT_NAME);
    	final EntityManager entityManager = entityManagerFactory.createEntityManager();
    	
    	final EntityTransaction transaction = entityManager.getTransaction();
    	transaction.begin();
    	
    	final Client client = entityManager.find(Client.class, 1);
    	// select client0_.id as id1_1_0_, client0_.address as address2_1_0_, client0_.name as name3_1_0_, client0_.client_no as client_n4_1_0_, client0_.ssn as ssn5_1_0_, orders1_.CLIENT_ID as CLIENT_I5_1_1_, orders1_.id as id1_0_1_, orders1_.id as id1_0_2_, orders1_.CLIENT_ID as CLIENT_I5_0_2_, orders1_.ORDER_DATE as ORDER_DA2_0_2_, orders1_.ORDER_DESC as ORDER_DE3_0_2_, orders1_.ORDER_NO as ORDER_NO4_0_2_ from clients client0_ left outer join ORDERS orders1_ on client0_.id=orders1_.CLIENT_ID where client0_.id=?
    	// kwerenda znalezienia clienta o id 1
    	
    	Order ddd = null;
    	Set<Order> orders = client.getOrders();
    	
    	for (Order order : orders) {
    		if (order.getDescription().equals("ddd")) {
    			ddd = order;
    			break;
    		}
    	}
    	
    	ddd.setDescription("dddUpdated");
    	ddd.setNo("420");
        
        entityManager.persist(client);
        
    	transaction.commit();
    	transaction.begin();
    	// część do sprawdzenia

    	for (Order ordert : orders) {
    		System.out.println(ordert.getNo() + ": " + ordert.getDescription());    		
    	}
    	// Przy wyświetleniu wszystkich zamówień lista wygląda następująco:
//    	2009-05-07/22: ccc
//    	420: dddUpdated
//    	2009-05-07/20: aaa
//    	2009-05-07/21: bbb
    	// Zatem lista zamówień zaaktualizowała się w kliencie
    	
    	final Query query = entityManager.createQuery("select c from Order as c where c.no like :param", Order.class);
        query.setParameter("param", "420");
        final List<Order> orderList = query.getResultList();
        for (Order order_tested : orderList) {
        	System.out.println(order_tested.getNo() + ": " + order_tested.getDescription());    		        	
        }
        
        // 420: dddUpdated
        // Zatem wystąpiła także kaskada zmian do tabeli Orders
    	
    	transaction.commit();
    	entityManager.close();
    }
    
    @Test
    public void testCascadesTypesDelete() {
    	final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(ConfigConsts.PERSISTANCE_UNIT_NAME);
    	final EntityManager entityManager = entityManagerFactory.createEntityManager();
    	
    	final EntityTransaction transaction = entityManager.getTransaction();
    	transaction.begin();
    	
    	final Client client = entityManager.find(Client.class, 1);
    	// select client0_.id as id1_1_0_, client0_.address as address2_1_0_, client0_.name as name3_1_0_, client0_.client_no as client_n4_1_0_, client0_.ssn as ssn5_1_0_, orders1_.CLIENT_ID as CLIENT_I5_1_1_, orders1_.id as id1_0_1_, orders1_.id as id1_0_2_, orders1_.CLIENT_ID as CLIENT_I5_0_2_, orders1_.ORDER_DATE as ORDER_DA2_0_2_, orders1_.ORDER_DESC as ORDER_DE3_0_2_, orders1_.ORDER_NO as ORDER_NO4_0_2_ from clients client0_ left outer join ORDERS orders1_ on client0_.id=orders1_.CLIENT_ID where client0_.id=?
    	// kwerenda znalezienia clienta o id 1
    	
    	Order ddd = null;
    	Set<Order> orders = client.getOrders();
    	
    	for (Order order : orders) {
    		if (order.getDescription().equals("ddd")) {
    			ddd = order;
    			break;
    		}
    	}
    	
    	client.getOrders().remove(ddd);
    	entityManager.remove(ddd);
        
        entityManager.persist(client);
        
    	transaction.commit();
    	transaction.begin();
    	// część do sprawdzenia

    	for (Order ordert : orders) {
    		System.out.println(ordert.getNo() + ": " + ordert.getDescription());    		
    	}
    	
    	final Query query = entityManager.createQuery("select c from Order as c where c.description like :param", Order.class);
        query.setParameter("param", "ddd");
        final List<Order> orderList = query.getResultList();
        for (Order order_tested : orderList) {
        	System.out.println(order_tested.getNo() + ": " + order_tested.getDescription());    		        	
        }
        // wynik jest pusty zatem rekord został kaskadowo usunięty z obu tabel
    	
    	// Przy wyświetleniu wszystkich zamówień lista wygląda następująco:
//    	2009-05-07/22: ccc
//    	2009-05-07/20: aaa
//    	2009-05-07/21: bbb
    	// Zatem zamówienie zostało usunięte z listy zamówień
    	
    	transaction.commit();
    	entityManager.close();
    }
}
