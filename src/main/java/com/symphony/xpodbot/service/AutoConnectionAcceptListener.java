package com.symphony.xpodbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.events.SymConnectionAccepted;
import org.symphonyoss.client.events.SymConnectionRequested;
import org.symphonyoss.client.exceptions.ConnectionsException;
import org.symphonyoss.client.services.ConnectionsEventListener;
import org.symphonyoss.symphony.clients.ConnectionsClient;
import org.symphonyoss.symphony.clients.model.SymUserConnection;

import java.util.List;

/**
 * Created by nick.tarsillo on 8/7/17.
 */
public class AutoConnectionAcceptListener implements ConnectionsEventListener {
  private static final Logger LOG = LoggerFactory.getLogger(AutoConnectionAcceptListener.class);

  private ConnectionsClient connectionsClient;

  public AutoConnectionAcceptListener (ConnectionsClient connectionsClient) {
    this.connectionsClient = connectionsClient;
  }

  @Override
  public void onSymConnectionRequested(SymConnectionRequested symConnectionRequested) {
    try {
      List<SymUserConnection> connectionList = connectionsClient.getPendingRequests();
      for (SymUserConnection userConnection : connectionList) {
        connectionsClient.acceptConnectionRequest(userConnection);
      }
    } catch (ConnectionsException e) {
      LOG.error("Get pending requests failed: ", e);
    }
  }

  @Override
  public void onSymConnectionAccepted(SymConnectionAccepted symConnectionAccepted) {}
}
