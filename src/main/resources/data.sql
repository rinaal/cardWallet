INSERT INTO USER ()
VALUES ();

INSERT INTO USER ()
VALUES ();

INSERT INTO USER ()
VALUES ();

INSERT INTO USER_DATA(email,password, user_id)
VALUES ('olav.nordmann@hotmail.com', '1234', 1);

INSERT INTO USER_DATA(email, password, user_id)
VALUES ('kari.nordmann@hotmail.com', '1234', 2);

INSERT INTO USER_DATA(email, password, user_id)
VALUES ('nils.nordmann@hotmail.com', '1234', 3);

INSERT INTO GIFT_CARD(store_name, card_code, balance)
VALUES ('H&M', '12345678', 500);

INSERT INTO GIFT_CARD(store_name, card_code, balance, user_id)
VALUES ('H&M', '87654321', 700, 2);
