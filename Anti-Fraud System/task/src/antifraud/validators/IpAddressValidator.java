package antifraud.validators;

import antifraud.exceptions.InvalidIpAddressException;

public interface IpAddressValidator {
    /** Validate if the given ip address has the correct format.
     *
     * @param ipAddress the address to validate
     * @throws InvalidIpAddressException thrown if given ip address has incorrect format or is null
     * */
    void validate(String ipAddress) throws InvalidIpAddressException;
}

