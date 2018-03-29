create database CLAG_Bank;
use CLAG_Bank;
show tables;
Create table Customer(
CustomerID int not null,
FName varchar(100) not null,
LName varchar(100) not null,
DoB date not null,
Street varchar(100) not null,
City varchar(100) not null,
State varchar(100) not null,
Zipcode varchar(100) not null,
Country varchar(100) not null,
Email varchar(100) not null,
Gender enum('M', 'F'),
Phone varchar(100) not null,
Creditstatus enum('Excellent', 'Good', 'Bad') not null,
AnnualIncome int not null,
Username varchar(100) not null,
Password varchar(100) not null,
SecurityQuestion varchar(100) not null,
SecurityAnswer varchar(100) not null,
Primary key(CustomerID)
);
Create table CustomerAccounts(
CustomerID int not null,
AccountID int not null,
AccountTypeID int not null,
Primary key(CustomerID, AccountID)
);
Create table Accounts(
AccountTypeID int not null,
AccountName varchar(100) not null,
Primary key(AccountTypeID)
);
Create table DebitTransactions(
DebitTransactionID int not null auto_increment,
AccountID int not null,
AccountBalanceID int not null,
Date date not null,
Amount double not null,
OldAccountBalance double not null,
NewAccountBalance double not null,
Primary key(DebitTransactionID)
);
Create table AccountBalance(
AccountBalanceID int not null,
CurrentAccountBalance double not null,
AccountID int not null,
Primary Key(AccountBalanceID)
);
Create table CustomerCreditCards(
CustomerID int not null,
CreditCardID int not null,
CreditTypeID int not null,
Primary key(CustomerID, CreditCardID)
);
Create table CreditCards(
CreditTypeID int not null,
Cardtype varchar(100) not null,
AnnualFees varchar(100) not null,
IntroAPR varchar(100) not null,
RegularAPR varchar(100) not null,
Rewardrate varchar(100) not null,
RewardBonus int not null,
LateFee int not null,
CreditHistory Enum('Excellent','Good','Bad'),
Primary Key(CreditTypeID)
);
Insert into CreditCards(CreditTypeID, Cardtype, AnnualFees, IntroAPR, RegularAPR, Rewardrate, RewardBonus, LateFee, CreditHistory)
values
('1', 'Alpha', '$0 for 1st year, $95 after', '0% for 12 months', '14.24%-24.24%', '1-5% cash back', '0', '30', 'Good'),
('2', 'Bravo', 'None', '0% for 12 months', '12.24%-24.24%', '1-5% cash back', '0', '40', 'Good'),
('3', 'Charlie', 'None', '0% for 15 months', '14.24%-25.24%', '1-3% cash back', '150', '35', 'Good'),
('4', 'Delta', 'None', '0% for 12 months', '13.24%-23.24%', '1-3% cash back', '150', '30', 'Excellent'),
('5', 'Echo', '$39', '0% for 12 months', '24.99%', '1.5% cash back', '0', '10', 'Good'),
('6', 'Foxtrot', 'None', '0% for 15 months', '16.24%-24.99%', '1.5% cash back', '150', '30', 'Excellent');
Create table CreditTransactions(
CreditTransactionID int not null auto_increment,
CreditCardID int not null,
CreditBalanceID int not null,
Date date not null,
Amount double not null,
OldAccountBalance double not null,
NewAccountBalance double not null,
Primary key(CreditTransactionID)
);
Create table CreditBalance(
CreditBalanceID int not null,
CurrentBalance double not null,
CreditCardID int not null,
Primary Key(CreditBalanceID)
);
Create table CustomerLoans(
CustomerID int not null,
LoanID int not null,
LoanTypeID int not null,
status enum('Approved', 'Rejected') not null,
Primary Key(CustomerID, LoanID)
);
Create table Loans(
LoanTypeID int not null,
LoanName varchar(100) not null,
InterestRate double not null,
Primary Key(LoanTypeID)
);
Create table LoanTransactions(
LoanTransactionID int not null auto_increment,
LoanID int not null,
LoanBalanceID int not null,
Date date not null,
Amount double not null,
OldAccountBalance double not null,
NewAccountBalance double not null,
Primary key(LoanTransactionID)
);
Create table LoanBalanace(
LoanBalanceID int not null,
CurrentBalance double not null,
LoanID int not null,
Primary key(LoanBalanceID)
);
Create table Complaints(
CustomerID int not null,
EmployeeID int not null,
Subject varchar(100) not null,
Date date not null,
Primary key(CustomerID, EmployeeID)
);
Create table Employee(
EmployeeID int not null,
FirstName varchar(100) not null,
LastName varchar(100) not null,
DoB date not null,
Street varchar(100) not null,
City varchar(100) not null,
State varchar(100) not null,
Zipcode varchar(100) not null,
Phone varchar(100) not null,
Title varchar(100) not null,
Salary int not null,
OfficeLocation varchar(100) not null,
HireDate date not null
);
Create table Timestamps(
TimestampID int not null auto_increment,
EmployeeID int not null,
Time time not null,
Punchtype enum('Clock in', 'Clock out') not null,
Date date not null,
Primary key(TimestampID)
);