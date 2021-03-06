/* ====================
    REMOTE
   ==================== */

/* USER */
CREATE TABLE IF NOT EXISTS user (
  id INTEGER NOT NULL AUTO_INCREMENT,
  name_u VARCHAR(30),
  surname_u VARCHAR(30),
  phone_u INTEGER NOT NULL,
  birth_u VARCHAR(50) NOT NULL,
  email_u VARCHAR(100) NOT NULL,
  password_u VARCHAR(100) NOT NULL,
  delete_u INTEGER NOT NULL,
  date_create_u VARCHAR(30) NOT NULL,
  date_update_u VARCHAR(30) NOT NULL,
  PRIMARY KEY (email_u)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/* DOG */
CREATE TABLE IF NOT EXISTS dog (
  id VARCHAR(50) NOT NULL,
  id_nfc_d VARCHAR(25),
  id_user_d INTEGER NOT NULL,
  name_d VARCHAR(30),
  breed_d VARCHAR(50) NOT NULL,
  colour_d VARCHAR(50) NOT NULL,
  birth_d VARCHAR(100) NOT NULL,
  size_d INTEGER NOT NULL,
  sex_d INTEGER NOT NULL,
  delete_d INTEGER NOT NULL,
  date_create_d VARCHAR(30) NOT NULL,
  date_update_d VARCHAR(30) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_user_d) REFERENCES user(id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/* VACINATION */
CREATE TABLE IF NOT EXISTS vacination (
  id VARCHAR(50) NOT NULL,
  id_dog_v INTEGER NOT NULL,
  name_v VARCHAR(30),
  date_when_v VARCHAR(30) NOT NULL,
  delete_v INTEGER NOT NULL,
  date_create_v VARCHAR(30) NOT NULL,
  date_update_v VARCHAR(30) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_dog_v) REFERENCES dog(id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/* FRIEND */
CREATE TABLE IF NOT EXISTS friend (
  id VARCHAR(50) NOT NULL,
  id_user_f INTEGER NOT NULL,
  id_friend_f INTEGER NOT NULL,
  delete_f INTEGER NOT NULL,
  date_create_f VARCHAR(30) NOT NULL,
  date_update_f VARCHAR(30) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT C_Friend UNIQUE (id_user_f, id_friend_f),
  FOREIGN KEY (id_user_f) REFERENCES user(id),
  FOREIGN KEY (id_friend_f) REFERENCES user(id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/* REPORT */
CREATE TABLE IF NOT EXISTS report (
  id VARCHAR(50) NOT NULL,
  id_user_r INTEGER NOT NULL,
  id_dog_r INTEGER NOT NULL,
  location_r INTEGER NOT NULL,
  date_create_r VARCHAR(30) NOT NULL,
  date_update_r VARCHAR(30) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id_user_r) REFERENCES user(id),
  FOREIGN KEY (id_dog_r) REFERENCES dog(id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;