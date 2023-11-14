package com.hrproject.rabbitmq.consumer;

import com.hrproject.rabbitmq.model.ExpenseModel;
import com.hrproject.repository.entity.Company;

import com.hrproject.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseConsumer {

    private final CompanyService service;
    @RabbitListener(queues = ("${rabbitmq.expense-queue}"))
    public void expense(ExpenseModel model) {
        if (model != null) {
            System.out.println("burdasin");
            // model null değilse devam et
            Double expenseValue = model.getExpense(); // Double tipi kullanıldı, uygunsa bu tipi kullanabilirsiniz

            if (expenseValue != null) {
                // model.getExpense() null değilse devam et
                String expenseString = String.valueOf(expenseValue);
                System.out.println(expenseString);


                    service.avansexpense(expenseValue, model.getName(), model.getSurname(), model.getCompany(),model.getSayi());
                    System.out.println("Avans verildi");
                }
            else {
                // model.getExpense() null ise gerekli işlemleri yap veya hatayı gider
                // Örneğin: loglama, hata mesajı oluşturma vb.
                System.out.println(model.toString());
                service.maasekle(model.getSayi(), model.getMaas(), model.getName(), model.getSurname(), model.getCompany());
                System.out.println("Expense değeri null");
            }
            }
        }
    }

