package h100

import com.google.appengine.api.memcache.Expiration
import com.google.appengine.api.memcache.MemcacheServiceFactory

class CacheHelper {
  static <T> T get(String cacheName, Closure<T> closure) {
    def memcacheService = MemcacheServiceFactory.memcacheService
    T retval
    try {
      retval = memcacheService.get(cacheName)
      if (!retval) {
        retval = closure.call()
        memcacheService.put(cacheName, retval, Expiration.byDeltaSeconds(60*60*3))
      }
    } catch (Exception e) {
      retval = closure.call()
      memcacheService.put(cacheName, retval, Expiration.byDeltaSeconds(60*60*3))
    }
    return retval
  }

  static void clearAll() {
    MemcacheServiceFactory.memcacheService.clearAll()
  }
}