package org.example;

public class User {


    private final PersonalInformation personalInformation;
    private final AccountInformation accountInformation;
    private final BalanceInformation balanceInformation;

    public User(PersonalInformation personalInformation, AccountInformation accountInformation,
                BalanceInformation balanceInformation) {
        this.personalInformation = personalInformation;
        this.accountInformation = accountInformation;
        this.balanceInformation = balanceInformation;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof User)
            return this.personalInformation.equals(((User) object).personalInformation) ||
                    this.accountInformation.equals(((User) object).accountInformation);
        else
            return false;
    }

    public PersonalInformation getPersonalInformation() {  return personalInformation;  }

    public AccountInformation getAccountInformation() { return accountInformation; }

    public BalanceInformation getBalanceInformation() { return balanceInformation; }

    public final static class PersonalInformation {
        private final String fname, lname, pin, birthdate, gender, address, zip;

        public PersonalInformation(String fname, String lname, String pin, String birthdate,
                                   String gender, String address, String zip) {
            this.fname = fname;
            this.lname = lname;
            this.pin = pin;
            this.birthdate = birthdate;
            this.gender = gender;
            this.address = address;
            this.zip = zip;
        }

        @Override
        public boolean equals(Object object) {
            if(object instanceof PersonalInformation)
                return this.pin.equals(((PersonalInformation) object).pin);
            else
                return false;
        }

        public String getFullName() { return fname + " " + lname; }

        public String getFname() { return fname; }

        public String getLname() { return lname; }

        public String getPin() { return pin; }

        public String getBirthdate() { return birthdate; }

        public String getGender() { return gender; }

        public String getAddress() { return address; }

        public String getZip() { return zip; }

    }

    public final static class AccountInformation {
        private final String cardNumber, email, username, password;

        public AccountInformation(String cardNumber, String email, String username, String password) {
            this.cardNumber = cardNumber;
            this.email = email;
            this.username = username;
            this.password = password;
        }

        @Override
        public boolean equals(Object object) {
            if(object instanceof AccountInformation)
                return this.cardNumber.equals(((AccountInformation) object).cardNumber) ||
                       this.email.equals(((AccountInformation) object).email) ||
                       this.username.equals(((AccountInformation) object).username);
            else
                return false;
        }

        public String getCardNumber() { return cardNumber; }

        public String getEmail() { return email; }

        public String getUsername() { return username; }

        public String getPassword() { return password; }

    }

    public final static class BalanceInformation {
        private String ownerPIN;
        private double availableEUR, availableUSD, availableRON, awaitingEUR, awaitingUSD, awaitingRON,
                totalWithdrawnEUR, totalWithdrawnUSD, totalWithdrawnRON, totalDepositedEUR, totalDepositedUSD,
                totalDepositedRON, totalTransfersEUR, totalTransfersUSD, totalTransfersRON, totalWithdrawsEUR,
                totalWithdrawsUSD, totalWithdrawsRON, totalDepositsEUR, totalDepositsUSD, totalDepositsRON;

        public BalanceInformation(String ownerPIN, double availableEUR, double availableUSD, double availableRON,
                                  double awaitingEUR, double awaitingUSD, double awaitingRON, double totalWithdrawnEUR,
                                  double totalWithdrawnUSD, double totalWithdrawnRON, double totalDepositedEUR,
                                  double totalDepositedUSD, double totalDepositedRON, double totalTransfersEUR,
                                  double totalTransfersUSD, double totalTransfersRON, double totalWithdrawsEUR,
                                  double totalWithdrawsUSD, double totalWithdrawsRON, double totalDepositsEUR,
                                  double totalDepositsUSD, double totalDepositsRON) {
            this.ownerPIN = ownerPIN;
            this.availableEUR = availableEUR;
            this.availableUSD = availableUSD;
            this.availableRON = availableRON;
            this.awaitingEUR = awaitingEUR;
            this.awaitingUSD = awaitingUSD;
            this.awaitingRON = awaitingRON;
            this.totalWithdrawnEUR = totalWithdrawnEUR;
            this.totalWithdrawnUSD = totalWithdrawnUSD;
            this.totalWithdrawnRON = totalWithdrawnRON;
            this.totalDepositedEUR = totalDepositedEUR;
            this.totalDepositedUSD = totalDepositedUSD;
            this.totalDepositedRON = totalDepositedRON;
            this.totalTransfersEUR = totalTransfersEUR;
            this.totalTransfersUSD = totalTransfersUSD;
            this.totalTransfersRON = totalTransfersRON;
            this.totalWithdrawsEUR = totalWithdrawsEUR;
            this.totalWithdrawsUSD = totalWithdrawsUSD;
            this.totalWithdrawsRON = totalWithdrawsRON;
            this.totalDepositsEUR = totalDepositsEUR;
            this.totalDepositsUSD = totalDepositsUSD;
            this.totalDepositsRON = totalDepositsRON;
        }

        public String getOwnerPIN() { return ownerPIN; }

        public double getAvailableEUR() { return availableEUR; }

        public double getAvailableUSD() { return availableUSD; }

        public double getAvailableRON() { return availableRON; }

        public double getAwaitingEUR() { return awaitingEUR; }

        public double getAwaitingUSD() { return awaitingUSD; }

        public double getAwaitingRON() { return awaitingRON; }

        public double getTotalWithdrawnEUR() { return totalWithdrawnEUR; }

        public double getTotalWithdrawnUSD() { return totalWithdrawnUSD; }

        public double getTotalWithdrawnRON() { return totalWithdrawnRON; }

        public double getTotalDepositedEUR() { return totalDepositedEUR; }

        public double getTotalDepositedUSD() { return totalDepositedUSD; }

        public double getTotalDepositedRON() { return totalDepositedRON; }

        public double getTotalTransfersEUR() { return totalTransfersEUR; }

        public double getTotalTransfersUSD() { return totalTransfersUSD; }

        public double getTotalTransfersRON() { return totalTransfersRON; }

        public double getTotalWithdrawsEUR() { return totalWithdrawsEUR; }

        public double getTotalWithdrawsUSD() { return totalWithdrawsUSD; }

        public double getTotalWithdrawsRON() { return totalWithdrawsRON; }

        public double getTotalDepositsEUR() { return totalDepositsEUR; }

        public double getTotalDepositsUSD() { return totalDepositsUSD; }

        public double getTotalDepositsRON() { return totalDepositsRON; }

        public void setOwnerPIN(String ownerPIN) { this.ownerPIN = ownerPIN; }

        public void setAvailableEUR(double availableEUR) { this.availableEUR = availableEUR; }

        public void setAvailableUSD(double availableUSD) { this.availableUSD = availableUSD; }

        public void setAvailableRON(double availableRON) { this.availableRON = availableRON; }

        public void setAwaitingEUR(double awaitingEUR) { this.awaitingEUR = awaitingEUR; }

        public void setAwaitingUSD(double awaitingUSD) { this.awaitingUSD = awaitingUSD; }

        public void setAwaitingRON(double awaitingRON) { this.awaitingRON = awaitingRON; }

        public void setTotalWithdrawnEUR(double totalWithdrawnEUR) { this.totalWithdrawnEUR = totalWithdrawnEUR; }

        public void setTotalWithdrawnUSD(double totalWithdrawnUSD) { this.totalWithdrawnUSD = totalWithdrawnUSD; }

        public void setTotalWithdrawnRON(double totalWithdrawnRON) { this.totalWithdrawnRON = totalWithdrawnRON; }

        public void setTotalDepositedEUR(double totalDepositedEUR) { this.totalDepositedEUR = totalDepositedEUR; }

        public void setTotalDepositedUSD(double totalDepositedUSD) { this.totalDepositedUSD = totalDepositedUSD; }

        public void setTotalDepositedRON(double totalDepositedRON) { this.totalDepositedRON = totalDepositedRON; }

        public void setTotalTransfersEUR(double totalTransfersEUR) { this.totalTransfersEUR = totalTransfersEUR; }

        public void setTotalTransfersUSD(double totalTransfersUSD) { this.totalTransfersUSD = totalTransfersUSD; }

        public void setTotalTransfersRON(double totalTransfersRON) { this.totalTransfersRON = totalTransfersRON; }

        public void setTotalWithdrawsEUR(double totalWithdrawsEUR) { this.totalWithdrawsEUR = totalWithdrawsEUR; }

        public void setTotalWithdrawsUSD(double totalWithdrawsUSD) { this.totalWithdrawsUSD = totalWithdrawsUSD; }

        public void setTotalWithdrawsRON(double totalWithdrawsRON) { this.totalWithdrawsRON = totalWithdrawsRON; }

        public void setTotalDepositsEUR(double totalDepositsEUR) { this.totalDepositsEUR = totalDepositsEUR; }

        public void setTotalDepositsUSD(double totalDepositsUSD) { this.totalDepositsUSD = totalDepositsUSD; }

        public void setTotalDepositsRON(double totalDepositsRON) { this.totalDepositsRON = totalDepositsRON; }

    }

}