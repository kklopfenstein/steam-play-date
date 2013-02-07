package playdate.common.steam.parser;

import java.io.InputStream;
import java.net.URI;

import org.codehaus.httpcache4j.HTTPRequest;
import org.codehaus.httpcache4j.HTTPResponse;
import org.codehaus.httpcache4j.cache.HTTPCache;
import org.codehaus.httpcache4j.cache.MemoryCacheStorage;
import org.codehaus.httpcache4j.resolver.HTTPClientResponseResolver;

public class SteamCache {
	
	private static HTTPCache cache;
	
	private static HTTPCache getCache() {
		if(cache == null) {
			cache = new HTTPCache(
					new MemoryCacheStorage(),
					HTTPClientResponseResolver.createMultithreadedInstance()
					);
		}
		return cache;
	}
	
	public static InputStream getRequestStream(String steamURL) {
		HTTPRequest request = new HTTPRequest(URI.create(steamURL.toString()));
		HTTPResponse response = null;
		try {
			response = SteamCache.getCache().execute(request);
		} finally {
			if(response != null) {
				response.consume();
			}
		}
		return response.getPayload().getInputStream();
	}
	
	public static InputStream getRefreshRequestStream(String steamURL) {
		HTTPRequest request = new HTTPRequest(URI.create(steamURL.toString()));
		HTTPResponse response = null;
		try {
			response = SteamCache.getCache().executeRefresh(request);
		} finally {
			if(response != null) {
				response.consume();
			}
		}
		return response.getPayload().getInputStream();
	}
}
