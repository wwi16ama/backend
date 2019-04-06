package com.WWI16AMA.backend_api.Account.ProtectedAccount;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Account.VereinsKontoTransaction;
import com.WWI16AMA.backend_api.Events.ExtTransactionEvent;
import com.WWI16AMA.backend_api.Events.IntTransactionEvent;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventListener {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AccountRepository accountRepository;


    @Async
    @EventListener
    public void makeExternalTransaction(final ExtTransactionEvent transactionEvent) {

        Transaction tr = transactionEvent.getTransaction();
        tr.setType(tr.getAmount() > 0 ? Transaction.FeeType.EINZAHLUNG : Transaction.FeeType.AUSZAHLUNG);

        Account account = transactionEvent.getAccount();
        account.addTransaction(tr);

        accountRepository.save(account);
    }

    @Async
    @EventListener
    public void makeInternalTransaction(IntTransactionEvent ev) {

        Transaction tr = ev.getTransaction();

        // TODO mit Holtermann absprechen
        boolean korrekteAbbuchung = tr.getAmount() < 0 &&
                (tr.getType().equals(Transaction.FeeType.MITGLIEDSBEITRAG)
                        || tr.getType().equals(Transaction.FeeType.GEBÜHRFLUGZEUG));

        boolean korrekteGutschrift = tr.getAmount() > 0 &&
                (tr.getType().equals(Transaction.FeeType.GUTSCHRIFTAMT)
                        || tr.getType().equals(Transaction.FeeType.GUTSCHRIFTLEISTUNG));

        if (!(korrekteAbbuchung || korrekteGutschrift)) {
            throw new IllegalArgumentException("Die Transaktion ist weder eine korrekte Abbuchung" +
                    "noch eine korrekte Gutschrift");
        }


        Account memAcc = ev.getAccount();
        memAcc.addTransaction(tr);
        System.out.println("memAcc.transactions.size:\t" + memAcc.getTransactions().size());


        VereinsAccount vAcc = VereinsAccount.getInstance(accountRepository);
        // System.out.println("acc: "+ev.getAccount());
        VereinsKontoTransaction vtr = new VereinsKontoTransaction(tr, ev.getAccount());
        // System.out.println("text:"+tr.getText());
        vAcc.addTransaction(vtr);
        System.out.println("vAcc.transactions.size:\t\t" + vAcc.getTransactions().size());

        accountRepository.save(memAcc);
        vAcc = accountRepository.save(vAcc);
    }
}
