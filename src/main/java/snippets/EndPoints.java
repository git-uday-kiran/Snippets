package snippets;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.JSONObject;

@Path("test")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EndPoints {

	@POST
	@Path("file")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)

	public static Response upload(FormDataMultiPart multiPart) {
		Map<String, List<FormDataBodyPart>> map = multiPart.getFields();
		List<FormDataBodyPart> files = map.get("");

		FormDataBodyPart file = files.get(0);
		InputStream stream = file.getValueAs(InputStream.class);

		JSONObject response = new JSONObject();
		response.put("accepted", true);
		response.put("message", "accepted file");

		// --- TEST FOR QR READER---
//		try {
//			String content = QrCode.decode(ImageIO.read(stream));
//			System.out.println("content : " + content);
//			response.put("content", content);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		return Response.accepted().entity(response.toString()).build();
	}
}
