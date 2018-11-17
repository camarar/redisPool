package redis.pool.demo;

import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Hello world!
 *
 */
public class App 
{
	final static Logger logger = Logger.getLogger(App.class);
	
    public static void main( String[] args )	
    {
    	
        System.out.println( "Hello World!" );
        
        logger.info("Hello World!");
        
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
        Jedis jedis = null;
        
        try  {
        	jedis = pool.getResource();
        	logger.debug("Get resource from pool.");
        	jedis.set("foo", "bar");
        	String foobar = jedis.get("foo");
        	
        	System.out.println("Get Redis: " + foobar);	
        	
			jedis.zadd("sose", 0, "car"); jedis.zadd("sose", 0, "bike"); 
			Set<String> sose = jedis.zrange("sose", 0, -1);
			
			// Simple PING command        
	        System.out.println( "\nCache Command  : Ping" );
	        System.out.println( "Cache Response : " + jedis.ping());

	        // Simple get and put of integral data types into the cache
	        System.out.println( "\nCache Command  : GET Message" );
	        System.out.println( "Cache Response : " + jedis.get("Message"));

	        System.out.println( "\nCache Command  : SET Message" );
	        System.out.println( "Cache Response : " + jedis.set("Message", "Hello! The cache is working from Java!"));

	        // Demostrate "SET Message" executed as expected...
	        System.out.println( "\nCache Command  : GET Message" );
	        System.out.println( "Cache Response : " + jedis.get("Message"));

	        // Get the client list, useful to see if connection list is growing...
	        System.out.println( "\nCache Command  : CLIENT LIST" );
	        System.out.println( "Cache Response : " + jedis.clientList());
	        
	        
	        // Simple set with expiration
	        System.out.println( "\nCache Command  : SET Message Expire" );
	        System.out.println( "Cache Response : " + jedis.setex("MessageExpire", 10, "Hello! The cache is working from Java with expire!"));
	        
	     // Simple get and put of integral data types into the cache
	        System.out.println( "\nCache Command  : GET Message Expire" );
	        System.out.println( "Cache Response : " + jedis.get("MessageExpire"));
	        
	        Thread.sleep(11000);
	        System.out.println( "\n Wait 11s..." );
        	
	     // Simple get and put of integral data types into the cache
	        System.out.println( "\nCache Command  : GET Message Expire" );
	        System.out.println( "Cache Response : " + jedis.get("MessageExpire"));
	        
	        logger.debug("Redis clients: " + jedis.clientList());
	        logger.debug("Redis is connected: " + jedis.isConnected());
	        
			
		} catch (JedisConnectionException ex) {
			System.out.println( "Erro Jedis Connection: " + ex );
			logger.debug("Erro Jedis Connection: " + ex);
		} catch (Exception ex) {
			System.out.println( "Erro: " + ex );
			logger.debug("Erro: " + ex );
		} finally {
			if (jedis != null) {
			    jedis.close();
			    logger.debug("Close jedis client.");
			}
			logger.debug("Jedis client is null[" + (jedis == null) + "].");

		}
        
        pool.close();
        logger.info("Close connection pool.");
        
        
    }
}
