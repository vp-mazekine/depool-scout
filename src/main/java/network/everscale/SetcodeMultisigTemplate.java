package network.everscale;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.math.BigInteger;
import java.util.Map;

import network.everscale.SetcodeMultisig;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.DeployHandle;
import tech.deplant.java4ever.framework.Sdk;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.template.Template;

/**
 * Java template class for deploy of <strong>SetcodeMultisig</strong> contract for Everscale blockchain.
 */
public record SetcodeMultisigTemplate(ContractAbi abi, Tvc tvc) implements Template {
  public SetcodeMultisigTemplate(Tvc tvc) throws JsonProcessingException {
    this(DEFAULT_ABI(), tvc);
  }

  public SetcodeMultisigTemplate() throws JsonProcessingException {
    this(DEFAULT_ABI(),DEFAULT_TVC());
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"ABI version\":2,\"version\":\"2.3\",\"header\":[\"pubkey\",\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"owners\",\"type\":\"uint256[]\"},{\"name\":\"reqConfirms\",\"type\":\"uint8\"},{\"name\":\"lifetime\",\"type\":\"uint32\"}],\"outputs\":[]},{\"name\":\"sendTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"flags\",\"type\":\"uint8\"},{\"name\":\"payload\",\"type\":\"cell\"}],\"outputs\":[]},{\"name\":\"submitTransaction\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"allBalance\",\"type\":\"bool\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"stateInit\",\"type\":\"optional(cell)\"}],\"outputs\":[{\"name\":\"transId\",\"type\":\"uint64\"}]},{\"name\":\"confirmTransaction\",\"inputs\":[{\"name\":\"transactionId\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"isConfirmed\",\"inputs\":[{\"name\":\"mask\",\"type\":\"uint32\"},{\"name\":\"index\",\"type\":\"uint8\"}],\"outputs\":[{\"name\":\"confirmed\",\"type\":\"bool\"}]},{\"name\":\"getParameters\",\"inputs\":[],\"outputs\":[{\"name\":\"maxQueuedTransactions\",\"type\":\"uint8\"},{\"name\":\"maxCustodianCount\",\"type\":\"uint8\"},{\"name\":\"expirationTime\",\"type\":\"uint64\"},{\"name\":\"minValue\",\"type\":\"uint128\"},{\"name\":\"requiredTxnConfirms\",\"type\":\"uint8\"},{\"name\":\"requiredUpdConfirms\",\"type\":\"uint8\"}]},{\"name\":\"getTransaction\",\"inputs\":[{\"name\":\"transactionId\",\"type\":\"uint64\"}],\"outputs\":[{\"name\":\"trans\",\"type\":\"tuple\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signsRequired\",\"type\":\"uint8\"},{\"name\":\"signsReceived\",\"type\":\"uint8\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"sendFlags\",\"type\":\"uint16\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"stateInit\",\"type\":\"optional(cell)\"}]}]},{\"name\":\"getTransactions\",\"inputs\":[],\"outputs\":[{\"name\":\"transactions\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signsRequired\",\"type\":\"uint8\"},{\"name\":\"signsReceived\",\"type\":\"uint8\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"sendFlags\",\"type\":\"uint16\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"stateInit\",\"type\":\"optional(cell)\"}]}]},{\"name\":\"getCustodians\",\"inputs\":[],\"outputs\":[{\"name\":\"custodians\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"pubkey\",\"type\":\"uint256\"}]}]},{\"name\":\"submitUpdate\",\"inputs\":[{\"name\":\"codeHash\",\"type\":\"optional(uint256)\"},{\"name\":\"owners\",\"type\":\"optional(uint256[])\"},{\"name\":\"reqConfirms\",\"type\":\"optional(uint8)\"},{\"name\":\"lifetime\",\"type\":\"optional(uint32)\"}],\"outputs\":[{\"name\":\"updateId\",\"type\":\"uint64\"}]},{\"name\":\"confirmUpdate\",\"inputs\":[{\"name\":\"updateId\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"executeUpdate\",\"inputs\":[{\"name\":\"updateId\",\"type\":\"uint64\"},{\"name\":\"code\",\"type\":\"optional(cell)\"}],\"outputs\":[]},{\"name\":\"getUpdateRequests\",\"inputs\":[],\"outputs\":[{\"name\":\"updates\",\"type\":\"tuple[]\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"signs\",\"type\":\"uint8\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"codeHash\",\"type\":\"optional(uint256)\"},{\"name\":\"custodians\",\"type\":\"optional(uint256[])\"},{\"name\":\"reqConfirms\",\"type\":\"optional(uint8)\"},{\"name\":\"lifetime\",\"type\":\"optional(uint32)\"}]}]}],\"events\":[],\"data\":[],\"fields\":[{\"name\":\"_pubkey\",\"type\":\"uint256\"},{\"name\":\"_timestamp\",\"type\":\"uint64\"},{\"name\":\"_constructorFlag\",\"type\":\"bool\"},{\"name\":\"m_ownerKey\",\"type\":\"uint256\"},{\"name\":\"m_requestsMask\",\"type\":\"uint256\"},{\"name\":\"m_transactions\",\"type\":\"map(uint64,tuple)\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"signsRequired\",\"type\":\"uint8\"},{\"name\":\"signsReceived\",\"type\":\"uint8\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"value\",\"type\":\"uint128\"},{\"name\":\"sendFlags\",\"type\":\"uint16\"},{\"name\":\"payload\",\"type\":\"cell\"},{\"name\":\"bounce\",\"type\":\"bool\"},{\"name\":\"stateInit\",\"type\":\"optional(cell)\"}]},{\"name\":\"m_custodians\",\"type\":\"map(uint256,uint8)\"},{\"name\":\"m_custodianCount\",\"type\":\"uint8\"},{\"name\":\"m_updateRequests\",\"type\":\"map(uint64,tuple)\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"index\",\"type\":\"uint8\"},{\"name\":\"signs\",\"type\":\"uint8\"},{\"name\":\"confirmationsMask\",\"type\":\"uint32\"},{\"name\":\"creator\",\"type\":\"uint256\"},{\"name\":\"codeHash\",\"type\":\"optional(uint256)\"},{\"name\":\"custodians\",\"type\":\"optional(uint256[])\"},{\"name\":\"reqConfirms\",\"type\":\"optional(uint8)\"},{\"name\":\"lifetime\",\"type\":\"optional(uint32)\"}]},{\"name\":\"m_updateRequestsMask\",\"type\":\"uint32\"},{\"name\":\"m_requiredVotes\",\"type\":\"uint8\"},{\"name\":\"m_defaultRequiredConfirmations\",\"type\":\"uint8\"},{\"name\":\"m_lifetime\",\"type\":\"uint32\"}]}");
  }

  public static Tvc DEFAULT_TVC() {
    return Tvc.ofBase64String("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");
  }

  public DeployHandle<SetcodeMultisig> prepareDeploy(Sdk sdk, Credentials credentials,
                                                     BigInteger[] owners, Integer reqConfirms, Long lifetime) {
    Map<String, Object> initialDataFields = Map.of();
    Map<String, Object> params = Map.of("owners", owners, 
        "reqConfirms", reqConfirms, 
        "lifetime", lifetime);
    return new DeployHandle<SetcodeMultisig>(SetcodeMultisig.class, sdk, abi(), tvc(), sdk.clientConfig().abi().workchain(), credentials, initialDataFields, params, null);
  }
}
