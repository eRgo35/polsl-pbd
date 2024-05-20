package pl.polsl.zti.db1;

public final class ConfigConsts {

    private ConfigConsts() {
    }
    /**
     * Nazwa (lub adres ip) serwera bazodanowego
     */
    public final static String SERVER_NAME = "localhost";
    /**
     * Port serwera
     */
    public final static int SERVER_PORT = 6033;
    /**
     * Nazwa uzytkownika
     */
    public final static String USERNAME = "root";
    /**
     * Haslo uzytkownika
     */
    public final static String PASSWORD = "";
    /**
     * Nazwa bazy
     */
    public final static String DATABASE_NAME = "laborm";
    /**
     * URL połączenia JDBC z bazą danych
     */
    public final static String JDBC_URL
            = "jdbc:mysql://"
            + SERVER_NAME
            + ":" + SERVER_PORT
            + "/" + DATABASE_NAME;

    public final static String PERSISTANCE_UNIT_NAME = "laborm-persistance-unit";
}
