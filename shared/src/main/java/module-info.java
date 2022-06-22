module shared {
  requires java.rmi;
  requires java.sql;

  exports com.sep2zg4.viamarket.servermodel;
  exports com.sep2zg4.viamarket.model;
  opens com.sep2zg4.viamarket.model;
  exports dk.via.remote.observer;
}