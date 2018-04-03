Create table CustomerAccounts
(
  CustomerID int not null,
  AccountID int not null,
  AccountTypeID int not null,
  Primary key(CustomerID, AccountID)
);
Create table Accounts
(
  AccountTypeID int not null,
  AccountName varchar(100) not null,
  Primary key(AccountTypeID)
);
Create table DebitTransactions
(
  DebitTransactionID int not null
  auto_increment,
AccountID int not null,
AccountBalanceID int not null,
Date date not null,
Amount double not null,
OldAccountBalance double not null,
NewAccountBalance double not null,
Primary key
  (DebitTransactionID)
);
  Create table AccountBalance(
AccountBalanceID int not null,
CurrentAccountBalance double not null,
AccountID int not null,
Primary Key
  (AccountBalanceID)
);
  Create table CustomerCreditCards
  (
    CustomerID int not null,
    CreditCardID int not null,
    CreditTypeID int not null,
    Primary key(CustomerID, CreditCardID)
  );

  Create table CreditTransactions
  (
    CreditTransactionID int not null
    auto_increment,
CreditCardID int not null,
CreditBalanceID int not null,
Date date not null,
Amount double not null,
OldAccountBalance double not null,
NewAccountBalance double not null,
Primary key
    (CreditTransactionID)
);
    Create table CreditBalance(
CreditBalanceID int not null,
CurrentBalance double not null,
CreditCardID int not null,
Primary Key
    (CreditBalanceID)
);
    Create table CustomerLoans(
CustomerID int not null,
LoanID int not null,
LoanTypeID int not null,
status enum('Approved', 'Rejected') not null,
Primary Key
    (CustomerID, LoanID)
);
    Create table Loans(
LoanTypeID int not null,
LoanName varchar(100) not null,
InterestRate double not null,
Primary Key
    (LoanTypeID)
);
    Create table LoanTransactions
    (
      LoanTransactionID int not null
      auto_increment,
LoanID int not null,
LoanBalanceID int not null,
Date date not null,
Amount double not null,
OldAccountBalance double not null,
NewAccountBalance double not null,
Primary key
      (LoanTransactionID)
);
      Create table LoanBalanace(
LoanBalanceID int not null,
CurrentBalance double not null,
LoanID int not null,
Primary key
      (LoanBalanceID)
);
Create table Timestamps
(
  TimestampID int not null
  auto_increment,
EmployeeID int not null,
Time time not null,
Punchtype enum
        ('Clock in', 'Clock out') not null,
Date date not null,
Primary key
        (TimestampID)
);