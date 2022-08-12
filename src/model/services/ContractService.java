package model.services;

import model.entities.Contract;
import model.entities.Installment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ContractService {

    private OnlinePaymentService onlinePaymentService;

    public ContractService(OnlinePaymentService taxService) {
        this.onlinePaymentService = taxService;
    }

    public void processContract(Contract contract, int months) {

        // Não era necessário criar uma lista, bastava criar um método addInstalment e removeInstallment em Contract.
        List<Installment> installments = new ArrayList<>();

        // Para o cálculo dos meses, seria interessante um método.
        Calendar cal = Calendar.getInstance();

        cal.setTime(contract.getDate());

        for (int i = 1; i <= months; i++) {
            double amountInterest = onlinePaymentService.interest(contract.getTotalValue() / months, i);
            double feeAmount = onlinePaymentService.paymentFee(amountInterest);

            // Ao invés de utilizar "2", poderia utilizar Calendar.MONTH, que seria muito mais fácil, mas não está errado.
            cal.add(2, 1);
            Installment inst = new Installment(cal.getTime(), feeAmount);

            installments.add(inst);
        }

        contract.setInstallments(installments);
    }
}
