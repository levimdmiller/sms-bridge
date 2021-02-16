package ca.levimiller.smsbridge.matrixsdk;

import io.github.ma1uta.matrix.client.AbstractClientBuilder;
import io.github.ma1uta.matrix.client.AppServiceClient;
import io.github.ma1uta.matrix.client.RequestParams;
import io.github.ma1uta.matrix.client.factory.RequestFactory;

public class ExtendedAppServiceClient extends AppServiceClient {

  public ExtendedAppServiceClient(RequestFactory factory,
      RequestParams defaultParams) {
    super(factory, defaultParams);
  }

  @Override
  public ExtendedRoomMethods room() {
    return new ExtendedRoomMethods(this.getRequestFactory(), this.getDefaultParams());
  }

  public static class Builder extends AbstractClientBuilder<ExtendedAppServiceClient> {
    public Builder() {
    }

    public ExtendedAppServiceClient newInstance() {
      return new ExtendedAppServiceClient(this.getFactory(), this.getDefaultParams());
    }
  }
}
