package h100

import com.google.appengine.api.memcache.MemcacheServiceFactory

class CacheHelper {
  static <T> T get(String cacheName, Closure<T> closure) {
    def memcacheService = MemcacheServiceFactory.memcacheService
    T retval = null
    try {
      retval = memcacheService.get(cacheName)
      if (!retval) {
        retval = closure.call()
        memcacheService.put(cacheName, retval)
      }
    } catch (Exception e) {
      retval = closure.call()
      memcacheService.put(cacheName, retval)
    }
    return retval
  }

  static void clearAll() {
    MemcacheServiceFactory.memcacheService.clearAll()
  }
}