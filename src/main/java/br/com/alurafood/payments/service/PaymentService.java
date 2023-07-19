package br.com.alurafood.payments.service;

import br.com.alurafood.payments.dto.PaymentDTO;
import br.com.alurafood.payments.http.OrderClient;
import br.com.alurafood.payments.model.Payment;
import br.com.alurafood.payments.model.Status;
import br.com.alurafood.payments.repository.PaymentRepositoy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepositoy repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderClient pedido;


    public Page<PaymentDTO> findAll(Pageable pagination) {
        return repository
                .findAll(pagination)
                .map(p -> modelMapper.map(p, PaymentDTO.class));
    }

    public PaymentDTO findById(Long id) {
        Payment payment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        return modelMapper.map(payment, PaymentDTO.class);
    }

    public PaymentDTO createPayment(PaymentDTO dto) {
        Payment payment = modelMapper.map(dto, Payment.class);
        payment.setStatus(Status.CRIADO);
        repository.save(payment);

        return modelMapper.map(payment, PaymentDTO.class);
    }

    public PaymentDTO updatePayment(Long id, PaymentDTO dto) {
        Payment payment = modelMapper.map(dto, Payment.class);
        payment.setId(id);
        payment = repository.save(payment);
        return modelMapper.map(payment, PaymentDTO.class);
    }

    public void deletePayment(Long id) {
        repository.deleteById(id);
    }

    public void confirmPayment(Long id){
        Optional<Payment> payment = repository.findById(id);

        if (payment.isEmpty()) {
            throw new EntityNotFoundException();
        }

        payment.get().setStatus(Status.CONFIRMADO);
        repository.save(payment.get());
        pedido.updatePayment(payment.get().getOrderId());
    }


    public void updateStatus(Long id) {
        Optional<Payment> payment = repository.findById(id);

        if (payment.isEmpty()) {
            throw new EntityNotFoundException();
        }

        payment.get().setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
        repository.save(payment.get());

    }
}

