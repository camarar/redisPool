package br.com.infoglobo.redis.pool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Hello world!
 *
 */
public class Run 
{

	private static Jedis jedis = null;
	
	private static JedisPool config(String host, int port) {
		JedisPool pool = new JedisPool(new JedisPoolConfig(), host, port);
		return pool;
	}
	
	
	public static void set(String host, int port, String key, String value, int seconds) {
		JedisPool pool = config(host, port); 
		
		try {
			// Obtem um recurso do pool de conexoes do Redis
			jedis = pool.getResource();
			
			
			// Se "seconds" for igual a zero, o registro nao expira
			if (seconds == 0) {
				jedis.set(key, value);
			}else {
				jedis.setex(key, seconds, value);
			}
			
		} catch (JedisConnectionException e) {
	        throw e;
		}finally {
			if (jedis != null) {
			    jedis.close();
			}
		}
		
		pool.close();
	}
	
	public static String get(String host, int port, String key) {
		JedisPool pool = config(host, port); 
		String value = null;
		
		try {
			jedis = pool.getResource();
				
			value = jedis.get(key);
			
		} catch (JedisConnectionException e) {
	        throw e;
		}finally {
			if (jedis != null) {
			    jedis.close();
			}
		}
		
		pool.close();
		return value;
	}
	
	public static void remove(String host, int port, String key) {
		JedisPool pool = config(host, port); 
		
		try {
			jedis = pool.getResource();
			jedis.del(key);
			
		} catch (JedisConnectionException e) {
	        throw e;
		}finally {
			if (jedis != null) {
			    jedis.close();
			   
			}
		}
		
		pool.close();
	}
	
	
	
    public static void main( String[] args )	
    {
    	
        System.out.println( "Testando!" );

        JedisPool pool = config("localhost", 6379);

        jedis = pool.getResource();
        

    	set("localhost", 6379, "foo", "bar", 0);
    	String foobar = get("localhost", 6379, "foo");
    	
    	System.out.println("Get Redis: " + foobar);	
		
		// Simple PING command        
        System.out.println( "\nCache Command  : Ping" );
        System.out.println( "Cache Response : " + jedis.ping());

        // Simple get and put of integral data types into the cache
        System.out.println( "\nCache Command  : GET Message" );
        System.out.println( "Cache Response : " + get("localhost", 6379, "Message"));

        System.out.println( "\nCache Command  : SET Message" );
        set("localhost", 6379, "Message", "Hello! The cache is working from Java!", 0);

        // Demostrate "SET Message" executed as expected...
        System.out.println( "\nCache Command  : GET Message" );
        System.out.println( "Cache Response : " + get("localhost", 6379, "Message"));

        // Get the client list, useful to see if connection list is growing...
        System.out.println( "\nCache Command  : CLIENT LIST" );
        System.out.println( "Cache Response : " + jedis.clientList());
        
        
        // Simple set with expiration
        System.out.println( "\nCache Command  : SET Message Expire" );
        set("localhost", 6379, "MessageExpire", "Hello! The cache is working from Java with expire!", 5);
        
     // Simple get and put of integral data types into the cache
        System.out.println( "\nCache Command  : GET Message Expire" );
        System.out.println( "Cache Response : " + get("localhost", 6379, "MessageExpire"));
        
        try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println( "\nWait 5s..." );
    	
     // Simple get and put of integral data types into the cache
        System.out.println( "\nCache Command  : GET Message Expire" );
        System.out.println( "Cache Response : " + get("localhost", 6379, "MessageExpire"));

     
        System.out.println( "\nClose Pool..." );
        pool.close();

    }
}
