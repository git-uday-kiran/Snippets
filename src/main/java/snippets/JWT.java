package snippets;

import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JWT {

	public static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	public static String generateToken() {
		return generateToken(new HashMap<>(), new HashMap<>());
	}

	public static String generateToken(Map<String, Object> claims, Map<String, Object> headers) {
		JwtBuilder jwtBuilder = Jwts.builder();

		jwtBuilder.addClaims(claims);
		jwtBuilder.setHeader(headers);

		jwtBuilder.signWith(key);
		jwtBuilder.setIssuer("test");
		jwtBuilder.setAudience("audience");
		jwtBuilder.setSubject("test");
//		jwtBuilder.setId(UUID.randomUUID().toString());
		jwtBuilder.setId("Unique ID");

//		Date notBefore = Date.from(Instant.now());
//		LocalDateTime time = LocalDateTime.now().plusMinutes(15);
//		Date notAfter = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());

//		jwtBuilder.setIssuedAt(notBefore);
//		jwtBuilder.setNotBefore(notBefore);
//		jwtBuilder.setExpiration(notAfter);

		return jwtBuilder.compact();
	}

	public static void verfy(String token) {
		JwtParserBuilder parserBuilder = Jwts.parserBuilder();
		parserBuilder.setSigningKey(key);
		parserBuilder.requireIssuer("test");
		parserBuilder.requireAudience("audience");
		parserBuilder.requireSubject("test");

//		Date notBefore = Date.from(Instant.now());
//		LocalDateTime time = LocalDateTime.now().plusMinutes(15);
//		Date notAfter = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
//		
//		parserBuilder.requireIssuedAt(notBefore);
//		parserBuilder.requireNotBefore(notBefore);
//		parserBuilder.requireExpiration(notAfter);

		JwtParser parser = parserBuilder.build();

		try {
			Jwt<?, ?> jwt = parser.parse(token);
			System.out.println("Verified!");
		} catch (Exception e) {
			System.out.println("Couldn't verify the token, " + e);
		}
	}

}
