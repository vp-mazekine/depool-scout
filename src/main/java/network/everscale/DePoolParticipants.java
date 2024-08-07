package network.everscale;

import org.jetbrains.annotations.Nullable;
import tech.deplant.java4ever.binding.Abi;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.contract.AbstractContract;
import tech.deplant.java4ever.framework.contract.Contract;
import tech.deplant.java4ever.framework.datatype.Address;

import java.math.BigInteger;
import java.util.Map;

public class DePoolParticipants extends AbstractContract implements Contract {

    public DePoolParticipants(int contextId, Address address, ContractAbi abi, Credentials credentials) {
        super(contextId, address, abi, credentials);
    }

    public DePoolParticipants(int contextId, String address, ContractAbi abi, Credentials credentials) {
        super(contextId, address, abi, credentials);
    }

    public DePoolParticipants(int contextId, Address address, ContractAbi abi, Abi.Signer signer) {
        super(contextId, address, abi, signer);
    }

    public FunctionHandle<ResultOfGetParticipants> getParticipants() {
        Map<String, Object> params = Map.of();
        return new FunctionHandle<ResultOfGetParticipants>(ResultOfGetParticipants.class, contextId(), address(), abi(), credentials(), "getParticipants", params, null);
    }

    public FunctionHandle<ResultOfGetParticipantInfo> getParticipantInfo(Address addr) {
        Map<String, Object> params = Map.of("addr", addr);
        return new FunctionHandle<ResultOfGetParticipantInfo>(ResultOfGetParticipantInfo.class, contextId(), address(), abi(), credentials(), "getParticipantInfo", params, null);
    }

    public record ResultOfGetParticipants(Address[] participants) {
    }

    public record ResultOfGetParticipantInfo(BigInteger total, BigInteger withdrawValue,
                                             Boolean reinvest, BigInteger reward, Map<BigInteger, BigInteger> stakes,
                                             Map<BigInteger, Map<String, Object>> vestings, Map<BigInteger, Map<String, Object>> locks,
                                             @Nullable Address vestingDonor, @Nullable Address lockDonor) {
    }
}
