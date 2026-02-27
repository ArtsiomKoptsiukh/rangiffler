package io.student.rangiffler.config;

public enum LocalConfig implements Config {
  INSTANCE;


  @Override
  public String frontUrl() {
    return "http://localhost:3001";
  }

  @Override
  public String authJdbcUrl() {
    return "jdbc:mysql://localhost:3306/rangiffler-auth?serverTimezone=UTC&createDatabaseIfNotExist=true";
  }

  @Override
  public String apiJdbcUrl() {
    return "jdbc:mysql://localhost:3306/rangiffler-api?serverTimezone=UTC&createDatabaseIfNotExist=true";
  }

  @Override
  public String dbUsername() {
    return "root";
  }

  @Override
  public String dbPassword() {
    return "secret";
  }
}
