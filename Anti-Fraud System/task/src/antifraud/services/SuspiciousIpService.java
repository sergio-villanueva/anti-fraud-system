package antifraud.services;

import antifraud.database.entities.SuspiciousIpAddressEntity;
import antifraud.database.repositories.SuspiciousIpAddressRepository;
import antifraud.database.repositories.TransactionRepository;
import antifraud.exceptions.IpAddressAlreadyExistsException;
import antifraud.exceptions.IpAddressDoesNotExistException;
import antifraud.models.SuspiciousIpAddressDTO;
import antifraud.models.SuspiciousIpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuspiciousIpService {
    @Autowired
    private SuspiciousIpAddressRepository suspiciousIpAddressRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /** Add a suspicious ip address to the database
     * @param request the request containing the suspicious ip address
     * @exception IpAddressAlreadyExistsException thrown when given ip address already exists in database
     * */
    public SuspiciousIpAddressDTO addSuspiciousIpAddress(SuspiciousIpRequest request) throws IpAddressAlreadyExistsException {
        if (suspiciousIpAddressRepository.existsByIpAddress(request.getIpAddress())) throw new IpAddressAlreadyExistsException();

        SuspiciousIpAddressEntity suspiciousIpAddressEntity = SuspiciousIpAddressEntity.builder()
                .ipAddress(request.getIpAddress())
                .build();
        // search for previous transactions made with suspicious ip and update
        transactionRepository.findByIpAddress(request.getIpAddress()).forEach((transactionEntity -> {
            transactionEntity.setSuspiciousIpAddressEntity(suspiciousIpAddressEntity); // set parent in child
            suspiciousIpAddressEntity.getTransactionEntities().add(transactionEntity); // set child in parent
        }));
        // save suspicious ip and its updated transactions and return dto
        return toDTO(suspiciousIpAddressRepository.save(suspiciousIpAddressEntity));
    }


    /** Retrieve all suspicious ip addresses sorted by their id in ascending order
     * */
    public List<SuspiciousIpAddressDTO> retrieveAllSuspiciousIpAddresses() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        return suspiciousIpAddressRepository.findAll(sort).stream().map(this::toDTO).toList();
    }


    /** Delete a suspicious ip address in the database
     * @param ipAddress the ip address to delete
     * @exception IpAddressDoesNotExistException thrown when given ip address does not exist
     * */
    public void deleteSuspiciousIpAddress(String ipAddress) throws IpAddressDoesNotExistException {
        suspiciousIpAddressRepository.findByIpAddress(ipAddress).ifPresentOrElse((suspiciousIpAddressEntity -> {
            // uncheck transactions made from the suspicious ip address
            suspiciousIpAddressEntity.getTransactionEntities().forEach((transactionEntity -> {
                transactionEntity.setSuspiciousIpAddressEntity(null);
                transactionRepository.save(transactionEntity);
            }));
            // remove the ip from the suspicious ip table
            suspiciousIpAddressRepository.deleteByIpAddress(ipAddress);
        }), () -> {
            // ip address not found
            throw new IpAddressDoesNotExistException();
        });

    }

    private SuspiciousIpAddressDTO toDTO(SuspiciousIpAddressEntity entity) {
        return SuspiciousIpAddressDTO.builder()
                .id(entity.getId())
                .ipAddress(entity.getIpAddress())
                .build();
    }

}
