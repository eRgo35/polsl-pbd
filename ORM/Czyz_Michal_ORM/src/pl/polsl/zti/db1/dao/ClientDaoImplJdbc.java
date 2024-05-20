package pl.polsl.zti.db1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pl.polsl.zti.db1.domain.Client;
import pl.polsl.zti.db1.util.JdbcUtils;

public class ClientDaoImplJdbc implements ClientDao {

    @Override
    public Client getClient(int id) {
        Client client = null;
        Connection con = null;
        PreparedStatement preparedStatement = null; // polecenie prekompilowane		
        try {
            con = JdbcUtils.getConnection();
            preparedStatement = con.prepareStatement("SELECT * FROM CLIENTS WHERE id = ?");
            preparedStatement.setInt(1, id); // ustawia wartość parametru

            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                client = readClient(resultSet);
            }
        } catch (SQLException ex) {
            JdbcUtils.handleSqlException(ex);
        } finally {
            JdbcUtils.closeSilently(preparedStatement, con);
        }

        return client;
    }

    @Override
    public List<Client> getClients() {
        // lista klientów do zwrócenia		
        final List<Client> clients = new ArrayList<Client>();

        Connection con = null;
        Statement stmt = null;
        try {
            con = JdbcUtils.getConnection();
            stmt = con.createStatement();
            final ResultSet resultSet = stmt.executeQuery("SELECT ID, CLIENT_NO, NAME, SSN, ADDRESS FROM CLIENTS");
            while (resultSet.next()) {
                // utwórz nowy obiekt typu Client
                final Client client = readClient(resultSet);
                // dodaj klienta do listy
                clients.add(client);
            }
        } catch (SQLException ex) {
            JdbcUtils.handleSqlException(ex);
        } finally {
            JdbcUtils.closeSilently(stmt, con);
        }

        return clients;
    }

    @Override
    public List<Client> getClients(String name) {
        // Zaimplementować pobieranie z bazy klientów według nazwy.
        // Znak '_' zastępuje dowolny znak we wzorcu,
        // znak '%' zastępuje dowolny ciąg znaków we wzorcu
        // (wykorzystać operator LIKE).
        // Wartość null w miejscu kryterium powoduje, ze nie jest ono brane pod uwagę.
        // Przykłady:
        //   * wywołanie getClients(null) ma zwrócić wszystkich klientów
        //   * wywołanie getClients("A%") ma zwrócić wszystkich klientów, 
        //	   których nazwy zaczynają się na literę 'A'

        //TODO: Implementacja    	
    	final List<Client> clients = new ArrayList<Client>();
    	
    	Connection con = null;
    	PreparedStatement preparedStatement = null;
    	try {
    		con = JdbcUtils.getConnection();
    		preparedStatement = con.prepareStatement("SELECT ID, CLIENT_NO, NAME, SSN, ADDRESS FROM CLIENTS WHERE NAME LIKE ?");
    		
    		preparedStatement.setString(1, name == null ? "%" : name);    			
    		
    		
    		final ResultSet resultSet = preparedStatement.executeQuery();
    		
    		while (resultSet.next()) {
    			final Client client = readClient(resultSet);
    			clients.add(client);
    		}
    	} catch (SQLException ex) {
    		JdbcUtils.handleSqlException(ex);
    	} finally {
    		JdbcUtils.closeSilently(preparedStatement, con);
    	}
    	
    	return clients;
    }

    private Client readClient(final ResultSet rs) throws SQLException {
        // utwórz nowy obiekt typu Client
        final Client client = new Client();
        // przypisz wartości do poszczególnych pól klienta
        client.setId(rs.getInt("ID"));
        client.setNo(rs.getInt("CLIENT_NO"));
        client.setName(rs.getString("NAME"));
        client.setSsn(rs.getString("SSN"));
        client.setAddress(rs.getString("ADDRESS"));
        return client;
    }

    @Override
    public void insertClients(List<Client> clients) {
        // Zaimplementować wstawianie do bazy podanej listy klientów.
        // Wstawianie wszystkich klientów ma być przeprowadzone w ramach jednej transakcji.
        // Proszę wykorzystać polecenia prekompilowane.

    	//TODO: Implementacja
    	Connection con = null;
    	PreparedStatement preparedStatement = null;
    	try {
    		con = JdbcUtils.getConnection();
    		
    		preparedStatement = con.prepareStatement("INSERT INTO clients(client_no, name, ssn, address) VALUES (?, ?, ?, ?)");

    		for (Client client : clients) {    		
    			preparedStatement.setInt(1, client.getNo());
    			preparedStatement.setString(2, client.getName());
    			preparedStatement.setString(3, client.getSsn());
    			preparedStatement.setString(4, client.getAddress());
    			
    			preparedStatement.addBatch();
    		}
    		
    		preparedStatement.executeBatch();
    		con.commit();
    		
    	} catch (SQLException ex) {
    		try {    			
    			con.rollback();
    		} catch (SQLException e) {
    			JdbcUtils.handleSqlException(e);
    		}
    		
    		JdbcUtils.handleSqlException(ex);
    	} finally {
    		JdbcUtils.closeSilently(preparedStatement, con);
    	}
    }

    @Override
    public void updateClient(Client client) {
        // Zaimplementować aktualizację danych podanego klienta w bazie.	

        //TODO: Implementacja
    	Connection con = null;
    	PreparedStatement preparedStatement = null;
    	try {
    		con = JdbcUtils.getConnection();
    		
    		preparedStatement = con.prepareStatement("UPDATE clients SET name = ?, ssn = ?, address = ?, client_no = ? WHERE id = ?");
    		
    		preparedStatement.setString(1, client.getName());
    		preparedStatement.setString(2, client.getSsn());
    		preparedStatement.setString(3, client.getAddress());
    		preparedStatement.setInt(4, client.getNo());
    		
    		preparedStatement.setInt(5, client.getId());
    		
    		preparedStatement.executeUpdate();
    	} catch (SQLException ex) {
    		JdbcUtils.handleSqlException(ex);
    	} finally {
    		JdbcUtils.closeSilently(preparedStatement, con);
    	}
    }

    @Override
    public void deleteClient(int id) {
        Connection con = null;
        Statement stmt = null;
        try {
            con = JdbcUtils.getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM CLIENTS WHERE id = " + id);
        } catch (SQLException ex) {
            JdbcUtils.handleSqlException(ex);
        } finally {
            JdbcUtils.closeSilently(stmt, con);
        }
    }

    @Override
    public void deleteClient(Client client) {
        deleteClient(client.getId());
    }
}
