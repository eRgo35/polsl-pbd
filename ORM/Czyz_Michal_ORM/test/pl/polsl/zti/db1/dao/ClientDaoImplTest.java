package pl.polsl.zti.db1.dao;

import java.util.ArrayList;
import java.util.List;
import pl.polsl.zti.db1.domain.Client;
import pl.polsl.zti.db1.util.DbSchemaDef;
import pl.polsl.zti.db1.util.JdbcUtils;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClientDaoImplTest {

    private static ClientDao clientDao;

    @BeforeClass
    public static void oneTimeSetUp() {
//        clientDao = new ClientDaoImplJdbc();
        clientDao = new ClientDaoImplJpa();
    }

    @Before
    public void setUp() {
        JdbcUtils.restoreDbSchema(new DbSchemaDef());
    }

    @Test
    public void testGetClient() {

        Client client = clientDao.getClient(1);
        assertEquals("Zwrocono nieprawidlowego klienta", 1, client.getId());

        client = clientDao.getClient(-1);
        assertNull("Zwrocono klienta pomimio, ze nie powinno", client);
    }

    @Test
    public void testGetClients() {

        List<Client> clnList = clientDao.getClients();
        long count = (Long) JdbcUtils.executeScalar("SELECT COUNT(*) FROM CLIENTS");
        assertEquals("Nieprawidlowa liczba zwroconych wynikow", count, clnList.size());

        clnList = clientDao.getClients(null);
        count = (Long) JdbcUtils.executeScalar("SELECT COUNT(*) FROM CLIENTS");
        assertEquals("Nieprawidlowa liczba zwroconych wynikow", count, clnList.size());

        clnList = clientDao.getClients("C%");
        count = (Long) JdbcUtils.executeScalar("SELECT COUNT(*) FROM CLIENTS WHERE name LIKE 'C%'");
        assertEquals("Nieprawidlowa liczba zwroconych wynikow", count, clnList.size());

        clnList = clientDao.getClients("C");
        count = (Long) JdbcUtils.executeScalar("SELECT COUNT(*) FROM CLIENTS WHERE name LIKE 'C'");
        assertEquals("Nieprawidlowa liczba zwroconych wynikow", count, clnList.size());

        clnList = clientDao.getClients("_li%");
        count = (Long) JdbcUtils.executeScalar("SELECT COUNT(*) FROM CLIENTS WHERE name LIKE '_li%'");
        assertEquals("Nieprawidlowa liczba zwroconych wynikow", count, clnList.size());
    }

    @Test
    public void testInsertClients() {

        long clientsCount = (Long) JdbcUtils.executeScalar("SELECT COUNT(*) FROM CLIENTS");

        final long newClientsCount = 100;
        final List<Client> newClients = new ArrayList<Client>();
        for (int i = 0; i < newClientsCount; i++) {
            final Client client = new Client();
            final int clientNo = i + 100;
            client.setNo(clientNo);
            client.setSsn("12-3123-" + clientNo);
            client.setName("New client " + clientNo);
            client.setAddress("New address " + clientNo);
            newClients.add(client);
        }
        clientsCount += newClients.size();

        clientDao.insertClients(newClients);

        long count = (Long) JdbcUtils.executeScalar("SELECT COUNT(*) FROM CLIENTS");
        assertEquals("Nieprawidlowa liczba wstawionych wierszy", clientsCount, count);

        count = (Long) JdbcUtils.executeScalar(
                "SELECT COUNT(*) FROM CLIENTS WHERE name LIKE 'New client%'");
        assertEquals("Blednie wstawione wartosci dla kolumny name", newClientsCount, count);

        count = (Long) JdbcUtils.executeScalar(
                "SELECT COUNT(*) FROM CLIENTS WHERE address LIKE 'New address%'");
        assertEquals("Blednie wstawione wartosci dla kolumny address", newClientsCount, count);
    }

    @Test
    public void testUpdateClient() {
        final Client cln = new Client();
        cln.setId(1);
        cln.setNo(10);
        cln.setSsn("10");
        cln.setName("Updated client 10");
        cln.setAddress("Updated address 10");

        clientDao.updateClient(cln);

        long count = (Long) JdbcUtils.executeScalar(
                "SELECT COUNT(*) FROM CLIENTS WHERE id = 1 AND client_no = 10");
        assertEquals("Bledna aktualizacja numeru klienta", 1, count);

        count = (Long) JdbcUtils.executeScalar(
                "SELECT COUNT(*) FROM CLIENTS WHERE id = 2 AND client_no = 2");
        assertEquals("Zaktualizowano dane nieprawidlowego klienta", 1, count);

        count = (Long) JdbcUtils.executeScalar(
                "SELECT COUNT(*) FROM CLIENTS WHERE id = 1 AND ssn='10'");
        assertEquals("Bledna aktualizacja ssn klienta", 1, count);

        count = (Long) JdbcUtils.executeScalar(
                "SELECT COUNT(*) FROM CLIENTS WHERE id = 1 AND name = 'Updated client 10'");
        assertEquals("Bledna aktualizacja nazwy klienta", 1, count);

        count = (Long) JdbcUtils.executeScalar(
                "SELECT COUNT(*) FROM CLIENTS WHERE id = 1 AND address = 'Updated address 10'");
        assertEquals("Bledna aktualizacja adresu klienta", 1, count);
    }

    @Test
    public void testDeleteClientId() {
        clientDao.deleteClient(1);
        assertNull("Klient o identyfikatorze 1 nie zostal usuniety",
                JdbcUtils.executeScalar("SELECT id FROM CLIENTS WHERE id = 1"));
    }

    @Test
    public void testDeleteClient() {
        final Client client = clientDao.getClient(1);
        assertEquals("Zwrocono nieprawidlowego klienta", 1, client.getId());
        clientDao.deleteClient(client);
        assertNull("Klient o identyfikatorze 1 nie zostal usuniety",
                JdbcUtils.executeScalar("SELECT id FROM CLIENTS WHERE id = 1"));
    }
}
