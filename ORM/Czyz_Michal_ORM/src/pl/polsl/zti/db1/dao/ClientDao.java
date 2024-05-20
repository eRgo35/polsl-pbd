package pl.polsl.zti.db1.dao;

import java.util.List;
import pl.polsl.zti.db1.domain.Client;

public interface ClientDao {
	
	/**
	 * Zwraca klienta o podanym id
	 * @param id Id klienta
	 * @return Szukany klient lub null gdy nie znaleziono.
	 */
	public Client getClient(int id);
	
	/**
	 * Zwraca liste wszystkich klientow z bazy
	 * @return Lista wszystkich klientow lub null gdy nie znaleziono
	 */
	public List<Client> getClients(); 
	
	/**
	 * Zwraca liste klientow, ktorzy maja nazwy pasujace do podanego kryterium. 
	 * Znak '_' zastepuje dowolna litere, znak '%' zastepuje dowolny ciag liter.
	 * @param name Nazwa klienta. 
	 *   Jezeli rowna null to kryterium nie jest brane pod uwage.
	 * @return Lista klientow spelniajaca podane kryterium.
	 */
	public List<Client> getClients(String name);

	/**
	 * Wstawia do bazy podanych klientow
	 * @param clients Lista klientow
	 */
	public void insertClients(List<Client> clients);
	
	/**
	 * Aktualizuje dane podanego klienta
	 * @param client
	 */
	public void updateClient(Client client);
	
	/**
	 * Usuwa z bazy klienta o podanym id 
	 * @param id
	 */
	public void deleteClient(int id);
	
	/**
	 * Usuwa z bazy podanego klienta
	 * @param client
	 */
	public void deleteClient(Client client);
}
