package com.WWI16AMA.backend_api.Account.ProtectedAccount;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Account.VereinsKontoTransaction;
import com.WWI16AMA.backend_api.Events.EmailNotificationEvent;
import com.WWI16AMA.backend_api.Events.ExtTransactionEvent;
import com.WWI16AMA.backend_api.Events.IntTransactionEvent;
import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventListener {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ApplicationEventPublisher publisher;


    @EventListener
    public void makeExternalTransaction(final ExtTransactionEvent transactionEvent) {

        Transaction tr = transactionEvent.getTransaction();
        Account account = transactionEvent.getAccount();

        tr.setType(tr.getAmount() > 0 ? Transaction.FeeType.EINZAHLUNG : Transaction.FeeType.AUSZAHLUNG);

        checkIfBalanceGetsLow(account, tr);

        account.addTransaction(tr);
        accountRepository.save(account);
    }

    @EventListener
    public void makeInternalTransaction(IntTransactionEvent ev) {

        Transaction tr = ev.getTransaction();
        Account memAcc = ev.getAccount();

        // TODO mit Holtermann absprechen
        boolean korrekteAbbuchung = tr.getAmount() <= 0 &&
                (tr.getType().equals(Transaction.FeeType.MITGLIEDSBEITRAG)
                        || tr.getType().equals(Transaction.FeeType.GEBÜHRFLUGZEUG));

        boolean korrekteGutschrift = tr.getAmount() >= 0 &&
                (tr.getType().equals(Transaction.FeeType.GUTSCHRIFTAMT)
                        || tr.getType().equals(Transaction.FeeType.GUTSCHRIFTLEISTUNG) || tr.getType().equals(Transaction.FeeType.BETANKUNGSKOSTENERSTATTUNG));

        if (!(korrekteAbbuchung || korrekteGutschrift)) {
            throw new IllegalArgumentException("Die Transaktion ist weder eine korrekte Abbuchung " +
                    "noch eine korrekte Gutschrift");
        }

        checkIfBalanceGetsLow(memAcc, tr);


        VereinsAccount vAcc = VereinsAccount.getInstance(accountRepository);
        VereinsKontoTransaction vtr = new VereinsKontoTransaction(tr, memAcc);
        memAcc.addTransaction(tr);
        vAcc.addTransaction(vtr);

        accountRepository.save(memAcc);
        accountRepository.save(vAcc);
    }

    /**
     * Muss ausgeführt werden, bevor die Transaktion durchgeführt wird
     *
     * @param acc
     * @param tr
     */
    private void checkIfBalanceGetsLow(Account acc, Transaction tr) {

        // Vereinsaccount hat keinen zugeordneten Member, daher keine Prüfung
        if (acc instanceof VereinsAccount) return;

        Member mem = memberRepository.findByMemberBankingAccount(acc)
                .orElseThrow(() -> new IllegalStateException("Member des Accounts kann nicht gefunden werden"));

        double balance = mem.getMemberBankingAccount().getBalance();
        double trAmount = tr.getAmount();

        if (balance >= 200.0 && balance + trAmount < 200) {
            publisher.publishEvent(new EmailNotificationEvent(mem, EmailNotificationEvent.Type.LOW_BALANCE, tr));
        }
    }
}
