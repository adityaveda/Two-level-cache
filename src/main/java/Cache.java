

import java.lang.ref.SoftReference;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/*the general idea is to implement LRU eviction policy while building the cache and accessing it. Some methods are mocked. 
The approach we are taking is that first populate the L1 cache which is in memory cache and once that is full, we start building the L2 cache which is on filesystem.  */

class Cache implements Service {
    Node head;
    Node tail;
    HashMap<String, Node> map = null;
    int cap = 0;

    public Cache(int capacity) {
        this.cap = capacity;
        this.map = new HashMap<>();
    }


    private void removeNode(Node n){
        if(n.prev!=null){
            n.prev.next = n.next;
        }else{
            head = n.next;
        }

        if(n.next!=null){
            n.next.prev = n.prev;
        }else{
            tail = n.prev;
        }
    }

    private void offerNode(Node n){
        if(tail!=null){
            tail.next = n;
        }

        n.prev = tail;
        n.next = null;
        tail = n;

        if(head == null){
            head = tail;
        }
    }

    @Override
    public void put(String key, Object value) {

        if(map.containsKey(key)){
            Node t = map.get(key);
            t.value = value;

            //move to tail
            removeNode(t);
            offerNode(t);
        }else{
            if(map.size()>=cap){
            Node t = map.get(key);
            t.value = value;
            pushToFileSystem(t);
            map.remove(head.key);
                removeNode(head);
            }

            //add to tail
            Node node = new Node(key, value);
            offerNode(node);
            map.put(key, node);
        }

    }

    public void pushToFileSystem(Node t){
        // method to push older cache entries into FS by writing them to disk
        //we will use serialization for this
    }

    public Object L2lookup(String key){
        //method to lookup if the key is present in the file on disk
        //method definition mocked
        //will return null or t.value

    }

    @Override
    public Object get(String key) {
        if(map.get(key)==null){
           if(L2lookup(key)==null){
               return null;
           }
           else {
               Node t = (Node) L2lookup(key);
               removeNode(t);
               offerNode(t);
               return t.value;
           }
        }

        //move to tail
        Node t = map.get(key);

        removeNode(t);
        offerNode(t);

        return t.value;

    }
}
