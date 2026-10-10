package sirenko.mar.bank.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import sirenko.mar.bank.AccountProperties;
import sirenko.mar.bank.entity.Account;
import sirenko.mar.bank.entity.User;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final SessionFactory sessionFactory;
    private final AccountProperties accountProperties;

    public AccountService (SessionFactory sessionFactory, AccountProperties accountProperties) {
        this.sessionFactory = sessionFactory;
        this.accountProperties = accountProperties;
    }

    public void accountCreate(Long idUser) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();
            User user = session.find(User.class, idUser);

            if (user == null) {
                transaction.rollback();
                throw new IllegalArgumentException("No user with this Id exists, id: " + idUser);
            }

            Account account = new Account(user, accountProperties.getDefaultAmount());
            user.addAccount(account);
            transaction.commit();

        } catch (Exception e) {

            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void accountClose(Long idAccount) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();
            Account account = session.find(Account.class, idAccount);

            if (account == null) {
                transaction.rollback();
                throw new IllegalArgumentException("No account with ID: " + idAccount);

            } else if (account.getMoneyAmount().compareTo(BigDecimal.ZERO) < 0) {
                transaction.rollback();
                throw new IllegalStateException("Cannot close account with ID " + idAccount
                        + " because it has a negative balance: " + account.getMoneyAmount());
            }

            User user = account.getUser();

            if (user.getAccountList().size() <= 1) {
                transaction.rollback();
                throw new IllegalStateException("The account with ID " + idAccount
                        + " cannot be closed because it is the only account of the user.");
            }

            if (account.getMoneyAmount().compareTo(BigDecimal.ZERO) > 0) {
                for (Account userAccount : user.getAccountList()) {
                    if (!userAccount.getId().equals(account.getId())) {

                        userAccount.setMoneyAmount(userAccount.getMoneyAmount().add(account.getMoneyAmount()));
                        session.merge(userAccount);
                        break;
                    }
                }
            }
            user.getAccountList().remove(account);
            session.remove(account);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void accountDeposit(Long idAccount, BigDecimal moneyAmount) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Account account = session.find(Account.class, idAccount);

            if (account == null) {
                transaction.rollback();
                throw new IllegalArgumentException("No account with ID: " + idAccount);
            }

            account.setMoneyAmount(account.getMoneyAmount().add(moneyAmount));
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void accountWithdraw(Long idAccount, BigDecimal attemptedWithdraw) {

        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Account account = session.find(Account.class, idAccount);

            if (account == null) {
                transaction.rollback();
                throw new IllegalArgumentException("No account with ID: " + idAccount);
            }

            if (account.getMoneyAmount().compareTo(attemptedWithdraw) < 0) {
                transaction.rollback();
                throw new IllegalStateException("No such money to withdraw from account, attempted withdraw: "
                        + attemptedWithdraw);
            }
            account.setMoneyAmount(account.getMoneyAmount().subtract(attemptedWithdraw));
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.getStatus().canRollback()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void accountTransfer (Long fromAccountId, Long toAccountId, BigDecimal amount) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {

            transaction = session.beginTransaction();

            Account accountFrom = session.find(Account.class, fromAccountId);

            if (accountFrom == null) {
                transaction.rollback();
                throw new IllegalArgumentException("No account with ID: " + fromAccountId);
            }

            if (accountFrom.getMoneyAmount().compareTo(amount) < 0) {
                transaction.rollback();
                throw new IllegalStateException(
                        "No such money to transfer from account, attempted transfer: "
                                + amount);
            }
            Account accountTo = session.find(Account.class, toAccountId);

            if (accountTo == null) {
                transaction.rollback();
                throw new IllegalArgumentException("No account with ID: " + toAccountId);
            }

            if (accountFrom.getUser().getId().equals(accountTo.getUser().getId())) {
                accountFrom.setMoneyAmount(accountFrom.getMoneyAmount().subtract(amount));
                accountTo.setMoneyAmount(accountTo.getMoneyAmount().add(amount));

            } else {
                BigDecimal transferAmountAndFee = amount.multiply(accountProperties.getTransferCommission().add(BigDecimal.ONE));

                if (accountFrom.getMoneyAmount().compareTo(transferAmountAndFee) < 0) {
                    transaction.rollback();
                    throw new IllegalStateException("No such money to transfer from account, attempted transfer: " + amount);
                }
                accountFrom.setMoneyAmount(accountFrom.getMoneyAmount().subtract(transferAmountAndFee));
                accountTo.setMoneyAmount(accountTo.getMoneyAmount().add(amount));
            }
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

}
