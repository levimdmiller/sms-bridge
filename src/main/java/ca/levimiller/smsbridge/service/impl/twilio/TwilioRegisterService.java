package ca.levimiller.smsbridge.service.impl.twilio;

import gov.nist.javax.sip.DialogTimeoutEvent;
import gov.nist.javax.sip.SipListenerExt;
import gov.nist.javax.sip.SipStackExt;
import gov.nist.javax.sip.clientauthutils.AuthenticationHelper;
import gov.nist.javax.sip.clientauthutils.UserCredentials;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Properties;
import javax.sip.ClientTransaction;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.TimeoutEvent;
import javax.sip.Transaction;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TwilioRegisterService implements SipListenerExt {
  private SipStackExt sipStack;
  private HeaderFactory headerFactory;
  private AddressFactory addressFactory;
  private MessageFactory messageFactory;
  private SipProvider sipProvider;
  private UserCredentials userCredentials;

  String ip;
  int port;
  String localIp;
  int localPort;
  String protocol;
  String realm;
  String username;
  String password;

  public TwilioRegisterService() throws UnknownHostException {
    username = "+17787609051";
    password = "Letmein1234567890";
    realm = "levimiller-d.sip.us2.twilio.com";
    protocol = "TCP";
    localPort = 5060;
    localIp = InetAddress.getLocalHost().getHostName();
    port = 14212;
    ip = "0.tcp.ngrok.io";

    userCredentials = new UserCredentials() {

      @Override
      public String getUserName() {
        return username;
      }

      @Override
      public String getPassword() {
        return password;
      }

      @Override
      public String getSipDomain() {
        return realm;
      }
    };
  }

  @EventListener(ApplicationReadyEvent.class)
  public void register() throws Exception {
    SipFactory sipFactory = SipFactory.getInstance();
    sipFactory.setPathName("gov.nist");

    Properties properties = new Properties();
    properties.setProperty("javax.sip.STACK_NAME", "SIP_CLIENT");

    properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");
    properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "sip_client/out/log/debug.txt");
    properties.setProperty("gov.nist.javax.sip.SERVER_LOG", "sip_client/out/log/server.txt");
    properties.setProperty("javax.sip.IP_ADDRESS", ip);
    properties.setProperty("javax.sip.TRANSPORT", protocol);

    sipStack = (SipStackExt) sipFactory.createSipStack(properties);
    headerFactory = sipFactory.createHeaderFactory();
    addressFactory = sipFactory.createAddressFactory();
    messageFactory = sipFactory.createMessageFactory();
    sipProvider = sipStack.createSipProvider(
        sipStack.createListeningPoint(localIp, localPort, protocol));
    sipProvider.addSipListener(this);
    sendRegister();
  }

  public void sendRegister() throws SipException, ParseException, InvalidArgumentException {
    Address address = addressFactory.createAddress("sip:" + username + "@" + realm);

    Request request = this.messageFactory.createRequest("REGISTER sip:" + realm + " SIP/2.0\r\n\r\n");
    request.addHeader(this.headerFactory.createViaHeader(ip, port, protocol, null));
    request.addHeader(this.headerFactory.createMaxForwardsHeader(70));
    request.addHeader(this.headerFactory.createFromHeader(address, "defg"));
    request.addHeader(this.headerFactory.createToHeader(address, null));
    request.addHeader(this.sipProvider.getNewCallId());
    request.addHeader(this.headerFactory.createCSeqHeader(1L, "REGISTER"));
    request.addHeader(this.headerFactory.createContactHeader());

    log.info("Register request over websocket \n" + request);

    ClientTransaction ctx = sipProvider.getNewClientTransaction(request);
    ctx.sendRequest();
  }

  @Override
  public void processRequest(RequestEvent requestEvent) {
    log.info("RequestEvent: {} ", requestEvent.getServerTransaction().getDialog());
  }

  @Override
  public void processResponse(ResponseEvent responseEvent) {
    log.info("responseEvent.getResponse()" + responseEvent.getResponse());
    if(responseEvent.getResponse().getStatusCode() == Response.UNAUTHORIZED){

      AuthenticationHelper authenticationHelper =
          sipStack.getAuthenticationHelper((challengedTransaction, realm) -> userCredentials, headerFactory);
      try {
        authenticationHelper.handleChallenge(responseEvent.getResponse(),responseEvent.getClientTransaction(),sipProvider,2000).sendRequest();
      } catch (SipException e) {
        e.printStackTrace();
      }
    }else if(responseEvent.getResponse().getStatusCode() == Response.OK){
      log.info("responseEvent.getResponse()" + responseEvent.getResponse());

    }
  }

  @Override
  public void processTimeout(TimeoutEvent timeoutEvent) {
    Transaction transaction;
    if (timeoutEvent.isServerTransaction()) {
      transaction = timeoutEvent.getServerTransaction();
    } else {
      transaction = timeoutEvent.getClientTransaction();
      log.info("{}", timeoutEvent.getTimeout().getValue());
    }
    log.info("state = " + transaction.getState());
    log.info("dialog = " + transaction.getDialog());
    log.info("Transaction Time out");
  }

  @Override
  public void processIOException(IOExceptionEvent exceptionEvent) {
    log.info("Expcetion occured "+exceptionEvent.getPort());
  }

  @Override
  public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {

    if (transactionTerminatedEvent.isServerTransaction())
      log.info("Transaction terminated event recieved"
          + transactionTerminatedEvent.getServerTransaction());
    else {
      log.info("Transaction terminated "
          + transactionTerminatedEvent.getClientTransaction());
    }
  }

  @Override
  public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
    log.info("processDialogTerminated" +dialogTerminatedEvent);
  }

  @Override
  public void processDialogTimeout(DialogTimeoutEvent arg0) {
    log.info("processDialogTimeout: {}", arg0);
  }
}
