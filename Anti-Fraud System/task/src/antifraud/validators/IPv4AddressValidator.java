package antifraud.validators;

import antifraud.exceptions.InvalidIpAddressException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component("ipv4AddressValidator")
public class IPv4AddressValidator implements IpAddressValidator{
    /**
     * Validate if the given ip address has the correct IPv4 format.
     *
     * @param ipAddress the address to validate
     * @throws InvalidIpAddressException thrown if given ip address has incorrect format or is null
     */
    @Override
    public void validate(String ipAddress) throws InvalidIpAddressException {
        if (Objects.isNull(ipAddress)) throw new InvalidIpAddressException("ip address is null");
        String[] splitAddress = ipAddress.split("\\.");
        if (splitAddress.length < 4) throw new InvalidIpAddressException();
        if (splitAddress.length != 4) throw new InvalidIpAddressException();

        if (Arrays.stream(splitAddress).filter(this::validOctet).toList().size() != 4)
            throw new InvalidIpAddressException();
    }

    private boolean validOctet(String octetStr) {
        if (octetStr.matches("[0-9]+")) {
            int octetInt = Integer.parseInt(octetStr);
            return octetInt <= 255 && octetInt >= 0;
        }
        return false;
    }
}
