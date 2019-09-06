DELETE TABLE IF EXISTS cheese;
CREATE TABLE cheese(
  id INT NOT NULL AUTOINCREMENT,
  name VARCHAR(15) NOT NULL,
  description VARCHAR NOT NULL,
  type ENUM('Hard','Soft','Fake')
);
