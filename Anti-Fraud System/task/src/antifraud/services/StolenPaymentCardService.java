package antifraud.services;

import antifraud.database.entities.StolenPaymentCardEntity;
import antifraud.database.entities.TransactionEntity;
import antifraud.database.repositories.StolenPaymentCardRepository;
import antifraud.database.repositories.TransactionRepository;
import antifraud.exceptions.CardNumberAlreadyExistsException;
import antifraud.exceptions.CardNumberDoesNotExistException;
import antifraud.models.StolenPaymentCardDTO;
import antifraud.models.StolenPaymentCardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StolenPaymentCardService {

    @Autowired
    private StolenPaymentCardRepository stolenPaymentCardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /** Add a stolen payment card in the db
     * @param request the request containing the stolen payment card
     * @exception CardNumberAlreadyExistsException thrown if stolen payment card already exists in database
     * */
    public StolenPaymentCardDTO addStolenPaymentCard(StolenPaymentCardRequest request) throws CardNumberAlreadyExistsException {
        if (stolenPaymentCardRepository.existsByCardNumber(request.getCardNumber())) throw new CardNumberAlreadyExistsException();

        StolenPaymentCardEntity stolenPaymentCardEntity = StolenPaymentCardEntity.builder()
                .cardNumber(request.getCardNumber())
                .build();
        // search for previous transactions made with stolen card and update
        transactionRepository.findByCardNumber(request.getCardNumber()).forEach((transactionEntity -> {
            transactionEntity.setStolenPaymentCardEntity(stolenPaymentCardEntity); // set parent in child
            stolenPaymentCardEntity.getTransactionEntities().add(transactionEntity); // set child in parent
        }));
        // save stolen card and its updated transactions and return dto
        return toDTO(stolenPaymentCardRepository.save(stolenPaymentCardEntity));
    }

    /** Retrieve all stolen payment cards sorted by their ids in ascending order
     * */
    public List<StolenPaymentCardDTO> retrieveAllStolenPaymentCards() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        return stolenPaymentCardRepository.findAll(sort).stream().map(this::toDTO).toList();
    }

    /** Delete a stolen payment card in the database
     * @param cardNumber the card number to delete by
     * @exception CardNumberDoesNotExistException thrown when the card number does not exist
     * */
    public void deleteStolenPaymentCard(String cardNumber) throws CardNumberDoesNotExistException {
        stolenPaymentCardRepository.findByCardNumber(cardNumber).ifPresentOrElse((stolenPaymentCardEntity -> {
            // uncheck transactions made from the stolen card
            stolenPaymentCardEntity.getTransactionEntities().forEach((transactionEntity -> {
                transactionEntity.setStolenPaymentCardEntity(null);
                transactionRepository.save(transactionEntity);
            }));
            // remove from the stolen card table
            stolenPaymentCardRepository.deleteByCardNumber(cardNumber);
        }), () -> {
            // stolen card not found
            throw new CardNumberDoesNotExistException();
        });

    }

    private StolenPaymentCardDTO toDTO(StolenPaymentCardEntity entity) {
        return StolenPaymentCardDTO.builder()
                .id(entity.getId())
                .cardNumber(entity.getCardNumber())
                .build();
    }

}
