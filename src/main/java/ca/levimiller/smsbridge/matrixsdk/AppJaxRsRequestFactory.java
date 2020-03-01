package ca.levimiller.smsbridge.matrixsdk;

import io.github.ma1uta.matrix.client.RequestParams;
import io.github.ma1uta.matrix.client.factory.jaxrs.JaxRsRequestFactory;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

public class AppJaxRsRequestFactory extends JaxRsRequestFactory {

  public AppJaxRsRequestFactory(String homeserverUrl) {
    super(homeserverUrl);
  }

  public AppJaxRsRequestFactory(Client client, String homeserverUrl) {
    super(client, homeserverUrl);
  }

  protected WebTarget applyQueryParams(RequestParams params, WebTarget path) {
    WebTarget webTarget = super.applyQueryParams(params, path);
    if (params.getUserId() == null) {
      return webTarget;
    }
    return webTarget.queryParam("user_id", this.encode(params.getUserId()));
  }
}
