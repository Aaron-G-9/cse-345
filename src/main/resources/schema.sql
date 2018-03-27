create table customer (
  customer_id serial primary key,
  customer_name text,
  date_of_birth DATE,
  street_address text,
  state_address varchar(2),
  country text,
  email text,
  gender text,
  contact_number text,
  username varchar(100),
  c_password text,
  security_question text,
  security_answer text,
  security_picture text
);


