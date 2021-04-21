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
        private final String fname, lname, pin, birthday, gender, address, zip;

        public PersonalInformation(String fname, String lname, String pin, String birthday,
                                   String gender, String address, String zip) {
            this.fname = fname;
            this.lname = lname;
            this.pin = pin;
            this.birthday = birthday;
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

        public String getFname() { return fname; }

        public String getLname() { return lname; }

        public String getPin() { return pin; }

        public String getBirthday() { return birthday; }

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
                return this.cardNumber.equals(((AccountInformation) object).cardNumber);
            else
                return false;
        }

        public String getCardNumber() { return cardNumber; }

        public String getEmail() { return email; }

        public String getUsername() { return username; }

        public String getPassword() { return password; }

    }

    public final static class BalanceInformation {
        private double availableEUR, availableUSD, availableRON, awaitingEUR, awaitingUSD, awaitingRON,
                totalWithdrawnEUR, totalWithdrawnUSD, totalWithdrawnRON, totalDepositedEUR, totalDepositedUSD,
                totalDepositedRON, totalTransfersEUR, totalTransfersUSD, totalTransfersRON, totalWithdrawsEUR,
                totalWithdrawsUSD, totalWithdrawsRON, totalDepositsEUR, totalDepositsUSD, totalDepositsRON;

        public BalanceInformation(double availableEUR, double availableUSD, double availableRON, double awaitingEUR,
                                  double awaitingUSD, double awaitingRON, double totalWithdrawnEUR,
                                  double totalWithdrawnUSD, double totalWithdrawnRON, double totalDepositedEUR,
                                  double totalDepositedUSD, double totalDepositedRON, double totalTransfersEUR,
                                  double totalTransfersUSD, double totalTransfersRON, double totalWithdrawsEUR,
                                  double totalWithdrawsUSD, double totalWithdrawsRON, double totalDepositsEUR,
                                  double totalDepositsUSD, double totalDepositsRON) {
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
    }

}