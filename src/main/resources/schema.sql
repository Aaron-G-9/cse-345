create table customer (
  customer_id serial primary key,
  first_name varchar(100) not null,
  last_name varchar(100),
  date_of_birth DATE not null,
  street text ,
  city text ,
  state varchar(30) ,
  country varchar(100) ,
  email text not null,
  gender text not null,
  phone varchar(30),
  credit_status text not null ,
  username varchar(100),
  c_password text,
  security_question text,
  security_answer text,
  security_picture varchar(100),
  annual_income DECIMAL(13,2),
  zipcode text 
);


create table credit_cards(
  card_id serial primary key,
  card_name varchar(50) not null,
  annual_fees decimal(13,2),
  intro_apr decimal(5,4),
  months_of_intro_apr int,
  regular_apr_min DECIMAL(5,4),
  regular_apr_max decimal(5,4),
  reward_rate_min DECIMAL(5,4),
  reward_rate_max DECIMAL(5,4),
  reward_bonus DECIMAL(13,2),
  late_fee DECIMAL(13,2),
  credit_history enum('excellent', 'good', 'bad')
);

/* I need better naming conventions. Ask KaJuan */
create table has_credit_card(
  id serial,
  customer_id bigint unsigned,
  card_id bigint unsigned,
  foreign key (customer_id) REFERENCES customer (customer_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  foreign key (card_id) REFERENCES credit_cards (card_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

Insert into credit_cards (card_name, annual_fees, intro_apr, months_of_intro_apr, regular_apr_min, regular_apr_max, reward_rate_min, reward_rate_max, reward_bonus, late_fee, credit_history) values
  ('Alpha', 95, 0, 12, .1424, .2424, .01, .05, 0, 30, 'good'),
  ('Bravo', null, 0, 12, .1224, .2424, .01, .05, 0, 40, 'good'),
  ('Charlie', null, 0, 15, .1424, .2424, .01, .03, 150, 35, 'good'),
  ('Delta', null, 0, 12, .1324, .2324, .01, .03, 150, 30, 'excellent'),
  ('Echo', 39, 0, 12, .1324, .2499, .015, .015, 0, 10, 'good'),
  ('Foxtrot', null, 0, 15, .1624, .2499, .015, .015, 150, 30, 'excellent')
;

create table employee(
  employee_id serial primary key,
  first_name varchar(100) not null,
  last_name VARCHAR(100),
  date_of_birth date not null,
  street text not null,
  city text not null,
  state varchar(2) not null,
  zipcode text not null,
  phone VARCHAR(20),
  title varchar(100) not null,
  salary decimal(13,2),
  office_location text not null,
  hire_date date not null
);

create table complaints(
  customer_id bigint unsigned, 
  employee_id bigint unsigned,
  employee_name VARCHAR(100),
  subject text not null,
  filed_on date not null,
  foreign key (customer_id) REFERENCES customer (customer_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  foreign key (employee_id) REFERENCES employee (employee_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

create table loans (
  loan_id serial primary key,
  loan_name VARCHAR(100),
  necessary_credit VARCHAR(100),
  monthly_payment_minimum decimal(13,2)
);

insert into loans (loan_name, necessary_credit, monthly_payment_minimum) values
( 'personal loan', 'good', 100), 
('housing loan', 'excellent', 100), 
('car loan', 'good', 100), 
('medical loan', 'bad', 100 );

create table has_loan(
  id serial,
  customer_id bigint unsigned,
  loan_id bigint unsigned,
  foreign key (customer_id) REFERENCES customer (customer_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  foreign key (loan_id) REFERENCES loans (loan_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

/* Interest rollover bank wide on certain dates */
create table accounts (
  account_id serial primary key,
  account_name varchar(100),
  account_type enum('basic_checking', 'joint_checking', 'student_checking', 'senior_checking', 'saving', 'money_market',  'ira'),
  early_withdraw_fee DECIMAL(13,2),
  max_withdraw DECIMAL(13,2),
  min_age int,
  max_age int,
  interest decimal(5,4),
  processing_delay bigint,
  minimum_balance DECIMAL(13,2),
  minimum_deposit DECIMAL(13,2)
);

create table has_account(
  id serial,
  customer_id bigint unsigned,
  account_id bigint unsigned,
  foreign key (customer_id) REFERENCES customer (customer_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  foreign key (account_id) REFERENCES accounts (account_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

insert into accounts 
(account_name, account_type, early_withdraw_fee, max_withdraw, min_age, max_age, interest, processing_delay, minimum_balance, minimum_deposit) 
VALUES
('Regal Basic Checking', 'basic_checking', 0, 1000, 18, 300, .008, 0, 20, null),
('Regal Student Checking', 'student_checking', 0, 500, 18, 30, .005, 0, 5, null),
('Regal Senior Checking', 'senior_checking', 0, 1500, 62, 300, .02, 0, 15, null),
('Regal Simple Saving', 'saving', 200, 1500, 0, 300, .04, 172800000, 200, null),
('Regal Money Market', 'money_market', 500, 1500, 0, 300, .08, 172800000, 1500, null),
('Regal IRA', 'ira', null, null, 0, 300, .10, null, 1500, 100);

create table transactions (
  transaction_id serial primary key,
  account_id bigint unsigned,
  customer_id bigint unsigned,
  creation_fee decimal(13,2),
  credit_id bigint unsigned,
  loan_id bigint unsigned,
  old_balance DECIMAL(13,2) not null,
  delta DECIMAL(13,2) not null, 
  new_balance DECIMAL(13,2) as (old_balance + delta),
  transaction_time timestamp default current_timestamp,
  status enum('pending', 'finished'),
  foreign key (account_id) REFERENCES accounts (account_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  foreign key (customer_id) REFERENCES customer (customer_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  foreign key (credit_id) REFERENCES credit_cards (card_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  foreign key (loan_id) REFERENCES loans (loan_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

Create table timestamps(
  timestamp_id serial primary key,
  employee_id bigint unsigned not null,
  time timestamp not null default current_timestamp,
  punch_type varchar(100) not null,
  foreign key (employee_id) REFERENCES employee (employee_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
);

insert into customer (first_name, last_name, date_of_birth, email, gender, username, c_password, security_question, security_answer, credit_status) 
values
('aaron', 'goodfellow', '1996-12-06', 'amgoodfellow@oakland.edu', 'male', 'amgoodfellow', 'je suis me', 'favorite color?', 'green', 'good');

insert into has_credit_card (customer_id, card_id) values (1, 2);
insert into has_account (customer_id, account_id) values (1, 1);
insert into has_account (customer_id, account_id) values (1, 4);

insert into transactions (account_id, customer_id, old_balance, delta) values (1, 1, 0, 500);
insert into transactions (account_id, customer_id, old_balance, delta) values (4, 1, 0, 1500);
insert into transactions (credit_id, customer_id, old_balance, delta) values (2, 1, 0, 2000);

insert into employee
(first_name, last_name, date_of_birth, street, city, state, zipcode, phone, title, salary, office_location, hire_date) values
('KaJuan', 'Johnson', '1994-08-24', '2222 Auburn Rd.', 'Auburn Hills', 'MI', '48439', '810-965-1453', 'Developer', 400000, '231 Dodge Hall', now()),
('Doris', 'Gjata', '1998-03-14', '2222 Russell Rd.', 'Auburn Hills', 'MI', '48326', '810-465-1453', 'UI/UX', 300000, '231 Dodge Hall', now()),
('Corey', 'Rowe', '1996-11-19', '2222 Fenton Rd.', 'Grand Blanc', 'MI', '48439', '810-238-0219', 'ADA Expert', 900000, '231 Dodge Hall', now());