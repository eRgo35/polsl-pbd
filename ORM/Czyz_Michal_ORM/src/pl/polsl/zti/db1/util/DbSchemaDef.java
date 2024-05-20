package pl.polsl.zti.db1.util;

public class DbSchemaDef {

    private final String CLIENTS_TABLE_DEF = "CLIENTS " +
	    "(ID int NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
	    "CLIENT_NO int, " +
	    "NAME varchar(20) NOT NULL, " +
	    "SSN char(11), " +
	    "ADDRESS VARCHAR(50))";
    
    private final String ORDERS_TABLE_DEF = "ORDERS " +  
	    "(ID int NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
	    "ORDER_NO varchar(20) NOT NULL, " +
	    "CLIENT_ID int NOT NULL, " +
	    "ORDER_DATE date NOT NULL, " +
	    "ORDER_DESC varchar(50), " +
	    "FOREIGN KEY (CLIENT_ID) REFERENCES CLIENTS (ID) ON DELETE CASCADE ON UPDATE CASCADE)";       
    
    public String[] getTablesNames() {
    	return new String[]{"ORDERS", "CLIENTS"}; // drop order
    }
    
    public String[] getTablesDef() {
    	return new String[]{CLIENTS_TABLE_DEF, ORDERS_TABLE_DEF}; // create order
    }

    public String[] getData() {
    	return new String[]{
	    	"Insert Into CLIENTS (ID, CLIENT_NO, NAME, SSN, ADDRESS) Values (1, 1, 'Client 1', '987-65-4301', 'Gliwice')",
	    	"Insert Into CLIENTS (ID, CLIENT_NO, NAME, SSN, ADDRESS) Values (2, 2, 'Client 2', '987-65-4302', 'Chorzow')",
	    	"Insert Into CLIENTS (ID, CLIENT_NO, NAME, SSN, ADDRESS) Values (3, 3, 'Client 3', '987-65-4303', 'Katowice')",    
	    	"Insert Into CLIENTS (ID, CLIENT_NO, NAME, SSN, ADDRESS) Values (4, 4, 'Client 4', '987-65-4304', 'Zabrze')",
	    	"Insert Into CLIENTS (ID, CLIENT_NO, NAME, SSN, ADDRESS) Values (5, 5, 'Client 5', '987-65-4305', 'Tychy')",
	    	//
	    	"Insert Into ORDERS (ID, ORDER_NO, CLIENT_ID, ORDER_DATE, ORDER_DESC) Values (1, '2009-05-07/20', 1, '2009-05-07', 'aaa')",
	    	"Insert Into ORDERS (ID, ORDER_NO, CLIENT_ID, ORDER_DATE, ORDER_DESC) Values (2, '2009-05-07/21', 1, '2009-05-07', 'bbb')",
	    	"Insert Into ORDERS (ID, ORDER_NO, CLIENT_ID, ORDER_DATE, ORDER_DESC) Values (3, '2009-05-07/22', 1, '2009-05-07', 'ccc')",
	    	"Insert Into ORDERS (ID, ORDER_NO, CLIENT_ID, ORDER_DATE, ORDER_DESC) Values (4, '2009-05-08/33', 1, '2009-05-08', 'ddd')",
	    	//    
	    	"Insert Into ORDERS (ID, ORDER_NO, CLIENT_ID, ORDER_DATE, ORDER_DESC) Values (5, '2009-04-07/20', 2, '2009-04-07', 'eee')",   
	    	"Insert Into ORDERS (ID, ORDER_NO, CLIENT_ID, ORDER_DATE, ORDER_DESC) Values (6, '2009-04-07/21', 2, '2009-04-07', 'fff')",   
	    	"Insert Into ORDERS (ID, ORDER_NO, CLIENT_ID, ORDER_DATE, ORDER_DESC) Values (7, '2009-04-07/22', 2, '2009-04-07', 'ggg')",
	    	//    
	    	"Insert Into ORDERS (ID, ORDER_NO, CLIENT_ID, ORDER_DATE, ORDER_DESC) Values (8, '2009-01-07/20', 3, '2009-01-07', 'hhh')",
	    	"Insert Into ORDERS (ID, ORDER_NO, CLIENT_ID, ORDER_DATE, ORDER_DESC) Values (9, '2009-01-07/21', 3, '2009-01-07', 'iii')",
	    	//    
	    	"Insert Into ORDERS (ID, ORDER_NO, CLIENT_ID, ORDER_DATE, ORDER_DESC) Values (10, '2009-05-07/44', 4, '2009-05-07', 'jjj')"
	    };
    }
}
