package inclass;

import java.util.Map;
import java.util.concurrent.*;

public class Momoizer<A, V>{
//        implements Computable<A, V> {
//    //don't need to guard datastructure if we use ConcurrentMap
//    private final Map<A, V> cache = new ConcurrentHashMap<>();
//    private final Computable<A, V> computable;
//
//
//    public V compute(A argument) throws InterruptedException {
//        V result = cache.get(argument);
//
//        if (result == null) {
//            result = computable.compute(argument);
//            cache.put(argument.result);
//        }
//
//        return result;
//    }
//
//

}
