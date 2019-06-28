import org.junit.Test;

/**
 * Created by adarshpandey on 1/13/15.
 */
public class CachingTest {

    @Test
    public void testCache() {
        System.out.println("=========================================");
        System.out.println("*********** Caching Test Start **********");
        System.out.println("=========================================");


        Cache cache = new Cache(4);

        cache.put("first",1);

        cache.put("second",2);
        cache.put("third",3);
        cache.put("four",4);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert cache.get("first").equals(1);


    }
}