#Drop user table if it already in the database(for testing purpose)
DROP TABLE IF EXISTS user;
#Create user table
CREATE TABLE user(
  id INTEGER AUTO_INCREMENT,
  username VARCHAR(45) NOT NULL,
  password VARCHAR(45) NOT NULL,
  email VARCHAR(45) NOT NULL,
  phone VARCHAR(45) NOT NULL,
  type VARCHAR(10) DEFAULT 'Client',
  PRIMARY KEY (id)
);