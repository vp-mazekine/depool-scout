package network.everscale;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Boolean;
import java.lang.Byte;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.String;
import java.lang.Void;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.FunctionHandle;
import tech.deplant.java4ever.framework.contract.AbstractContract;
import tech.deplant.java4ever.framework.datatype.Address;

/**
 * Java wrapper class for usage of <strong>DePoolV2</strong> contract for Everscale blockchain.
 */
public class DePoolV2 extends DePoolParticipants {
  public DePoolV2(int contextId, String address) throws JsonProcessingException {
    super(contextId,address,DEFAULT_ABI(),Credentials.NONE);
  }

  public DePoolV2(int contextId, String address, ContractAbi abi) {
    super(contextId,address,abi,Credentials.NONE);
  }

  public DePoolV2(int contextId, String address, Credentials credentials) throws
      JsonProcessingException {
    super(contextId,address,DEFAULT_ABI(),credentials);
  }

  @JsonCreator
  public DePoolV2(int contextId, String address, ContractAbi abi, Credentials credentials) {
    super(contextId,address,abi,credentials);
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"header\":[\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"minStake\",\"type\":\"uint64\"},{\"name\":\"validatorAssurance\",\"type\":\"uint64\"},{\"name\":\"proxyCode\",\"type\":\"cell\"},{\"name\":\"validatorWallet\",\"type\":\"address\"},{\"name\":\"participantRewardFraction\",\"type\":\"uint8\"}],\"outputs\":[]},{\"name\":\"addOrdinaryStake\",\"inputs\":[{\"name\":\"stake\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"withdrawFromPoolingRound\",\"inputs\":[{\"name\":\"withdrawValue\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"addVestingStake\",\"inputs\":[{\"name\":\"stake\",\"type\":\"uint64\"},{\"name\":\"beneficiary\",\"type\":\"address\"},{\"name\":\"withdrawalPeriod\",\"type\":\"uint32\"},{\"name\":\"totalPeriod\",\"type\":\"uint32\"}],\"outputs\":[]},{\"name\":\"addLockStake\",\"inputs\":[{\"name\":\"stake\",\"type\":\"uint64\"},{\"name\":\"beneficiary\",\"type\":\"address\"},{\"name\":\"withdrawalPeriod\",\"type\":\"uint32\"},{\"name\":\"totalPeriod\",\"type\":\"uint32\"}],\"outputs\":[]},{\"name\":\"withdrawPart\",\"inputs\":[{\"name\":\"withdrawValue\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"withdrawAll\",\"inputs\":[],\"outputs\":[]},{\"name\":\"cancelWithdrawal\",\"inputs\":[],\"outputs\":[]},{\"name\":\"transferStake\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"amount\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"participateInElections\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"},{\"name\":\"validatorKey\",\"type\":\"uint256\"},{\"name\":\"stakeAt\",\"type\":\"uint32\"},{\"name\":\"maxFactor\",\"type\":\"uint32\"},{\"name\":\"adnlAddr\",\"type\":\"uint256\"},{\"name\":\"signature\",\"type\":\"bytes\"}],\"outputs\":[],\"id\":\"0x4E73744B\"},{\"name\":\"ticktock\",\"inputs\":[],\"outputs\":[]},{\"name\":\"completeRoundWithChunk\",\"inputs\":[{\"name\":\"roundId\",\"type\":\"uint64\"},{\"name\":\"chunkSize\",\"type\":\"uint8\"}],\"outputs\":[]},{\"name\":\"completeRound\",\"inputs\":[{\"name\":\"roundId\",\"type\":\"uint64\"},{\"name\":\"participantQty\",\"type\":\"uint32\"}],\"outputs\":[]},{\"name\":\"onStakeAccept\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"},{\"name\":\"comment\",\"type\":\"uint32\"},{\"name\":\"elector\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"onStakeReject\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"},{\"name\":\"comment\",\"type\":\"uint32\"},{\"name\":\"elector\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"onSuccessToRecoverStake\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"},{\"name\":\"elector\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"onFailToRecoverStake\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"},{\"name\":\"elector\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"terminator\",\"inputs\":[],\"outputs\":[]},{\"name\":\"setValidatorRewardFraction\",\"inputs\":[{\"name\":\"fraction\",\"type\":\"uint8\"}],\"outputs\":[]},{\"name\":\"receiveFunds\",\"inputs\":[],\"outputs\":[]},{\"name\":\"getLastRoundInfo\",\"inputs\":[],\"outputs\":[]},{\"name\":\"getParticipantInfo\",\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"outputs\":[{\"name\":\"total\",\"type\":\"uint64\"},{\"name\":\"withdrawValue\",\"type\":\"uint64\"},{\"name\":\"reinvest\",\"type\":\"bool\"},{\"name\":\"reward\",\"type\":\"uint64\"},{\"name\":\"stakes\",\"type\":\"map(uint64,uint64)\"},{\"name\":\"vestings\",\"type\":\"map(uint64,tuple)\",\"components\":[{\"name\":\"remainingAmount\",\"type\":\"uint64\"},{\"name\":\"lastWithdrawalTime\",\"type\":\"uint64\"},{\"name\":\"withdrawalPeriod\",\"type\":\"uint32\"},{\"name\":\"withdrawalValue\",\"type\":\"uint64\"},{\"name\":\"owner\",\"type\":\"address\"}]},{\"name\":\"locks\",\"type\":\"map(uint64,tuple)\",\"components\":[{\"name\":\"remainingAmount\",\"type\":\"uint64\"},{\"name\":\"lastWithdrawalTime\",\"type\":\"uint64\"},{\"name\":\"withdrawalPeriod\",\"type\":\"uint32\"},{\"name\":\"withdrawalValue\",\"type\":\"uint64\"},{\"name\":\"owner\",\"type\":\"address\"}]}]},{\"name\":\"getDePoolInfo\",\"inputs\":[],\"outputs\":[{\"name\":\"poolClosed\",\"type\":\"bool\"},{\"name\":\"minStake\",\"type\":\"uint64\"},{\"name\":\"validatorAssurance\",\"type\":\"uint64\"},{\"name\":\"participantRewardFraction\",\"type\":\"uint8\"},{\"name\":\"validatorRewardFraction\",\"type\":\"uint8\"},{\"name\":\"balanceThreshold\",\"type\":\"uint64\"},{\"name\":\"validatorWallet\",\"type\":\"address\"},{\"name\":\"proxies\",\"type\":\"address[]\"},{\"name\":\"stakeFee\",\"type\":\"uint64\"},{\"name\":\"retOrReinvFee\",\"type\":\"uint64\"},{\"name\":\"proxyFee\",\"type\":\"uint64\"}]},{\"name\":\"getParticipants\",\"inputs\":[],\"outputs\":[{\"name\":\"participants\",\"type\":\"address[]\"}]},{\"name\":\"getDePoolBalance\",\"inputs\":[],\"outputs\":[{\"name\":\"value0\",\"type\":\"int256\"}]},{\"name\":\"getRounds\",\"inputs\":[],\"outputs\":[{\"name\":\"rounds\",\"type\":\"map(uint64,tuple)\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"supposedElectedAt\",\"type\":\"uint32\"},{\"name\":\"unfreeze\",\"type\":\"uint32\"},{\"name\":\"stakeHeldFor\",\"type\":\"uint32\"},{\"name\":\"vsetHashInElectionPhase\",\"type\":\"uint256\"},{\"name\":\"step\",\"type\":\"uint8\"},{\"name\":\"completionReason\",\"type\":\"uint8\"},{\"name\":\"stake\",\"type\":\"uint64\"},{\"name\":\"recoveredStake\",\"type\":\"uint64\"},{\"name\":\"unused\",\"type\":\"uint64\"},{\"name\":\"isValidatorStakeCompleted\",\"type\":\"bool\"},{\"name\":\"rewards\",\"type\":\"uint64\"},{\"name\":\"participantQty\",\"type\":\"uint32\"},{\"name\":\"validatorStake\",\"type\":\"uint64\"},{\"name\":\"validatorRemainingStake\",\"type\":\"uint64\"},{\"name\":\"handledStakesAndRewards\",\"type\":\"uint64\"}]}]}],\"events\":[{\"name\":\"DePoolClosed\",\"inputs\":[]},{\"name\":\"RoundStakeIsAccepted\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"},{\"name\":\"comment\",\"type\":\"uint32\"}]},{\"name\":\"RoundStakeIsRejected\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"},{\"name\":\"comment\",\"type\":\"uint32\"}]},{\"name\":\"ProxyHasRejectedTheStake\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"}]},{\"name\":\"ProxyHasRejectedRecoverRequest\",\"inputs\":[{\"name\":\"roundId\",\"type\":\"uint64\"}]},{\"name\":\"RoundCompleted\",\"inputs\":[{\"name\":\"round\",\"type\":\"tuple\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"supposedElectedAt\",\"type\":\"uint32\"},{\"name\":\"unfreeze\",\"type\":\"uint32\"},{\"name\":\"stakeHeldFor\",\"type\":\"uint32\"},{\"name\":\"vsetHashInElectionPhase\",\"type\":\"uint256\"},{\"name\":\"step\",\"type\":\"uint8\"},{\"name\":\"completionReason\",\"type\":\"uint8\"},{\"name\":\"stake\",\"type\":\"uint64\"},{\"name\":\"recoveredStake\",\"type\":\"uint64\"},{\"name\":\"unused\",\"type\":\"uint64\"},{\"name\":\"isValidatorStakeCompleted\",\"type\":\"bool\"},{\"name\":\"rewards\",\"type\":\"uint64\"},{\"name\":\"participantQty\",\"type\":\"uint32\"},{\"name\":\"validatorStake\",\"type\":\"uint64\"},{\"name\":\"validatorRemainingStake\",\"type\":\"uint64\"},{\"name\":\"handledStakesAndRewards\",\"type\":\"uint64\"}]}]},{\"name\":\"StakeSigningRequested\",\"inputs\":[{\"name\":\"electionId\",\"type\":\"uint32\"},{\"name\":\"proxy\",\"type\":\"address\"}]},{\"name\":\"TooLowDePoolBalance\",\"inputs\":[{\"name\":\"replenishment\",\"type\":\"uint256\"}]},{\"name\":\"RewardFractionsChanged\",\"inputs\":[{\"name\":\"validator\",\"type\":\"uint8\"},{\"name\":\"participants\",\"type\":\"uint8\"}]}],\"data\":[],\"ABI version\":2}");
  }

  public FunctionHandle<Void> addOrdinaryStake(BigInteger stake) {
    Map<String, Object> params = Map.of("stake", stake);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "addOrdinaryStake", params, null);
  }

  public FunctionHandle<Void> withdrawFromPoolingRound(BigInteger withdrawValue) {
    Map<String, Object> params = Map.of("withdrawValue", withdrawValue);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "withdrawFromPoolingRound", params, null);
  }

  public FunctionHandle<Void> addVestingStake(BigInteger stake, Address beneficiary,
      Long withdrawalPeriod, Long totalPeriod) {
    Map<String, Object> params = Map.of("stake", stake, 
        "beneficiary", beneficiary, 
        "withdrawalPeriod", withdrawalPeriod, 
        "totalPeriod", totalPeriod);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "addVestingStake", params, null);
  }

  public FunctionHandle<Void> addLockStake(BigInteger stake, Address beneficiary,
      Long withdrawalPeriod, Long totalPeriod) {
    Map<String, Object> params = Map.of("stake", stake, 
        "beneficiary", beneficiary, 
        "withdrawalPeriod", withdrawalPeriod, 
        "totalPeriod", totalPeriod);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "addLockStake", params, null);
  }

  public FunctionHandle<Void> withdrawPart(BigInteger withdrawValue) {
    Map<String, Object> params = Map.of("withdrawValue", withdrawValue);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "withdrawPart", params, null);
  }

  public FunctionHandle<Void> withdrawAll() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "withdrawAll", params, null);
  }

  public FunctionHandle<Void> cancelWithdrawal() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "cancelWithdrawal", params, null);
  }

  public FunctionHandle<Void> transferStake(Address dest, BigInteger amount) {
    Map<String, Object> params = Map.of("dest", dest, 
        "amount", amount);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "transferStake", params, null);
  }

  public FunctionHandle<Void> participateInElections(BigInteger queryId, BigInteger validatorKey,
      Long stakeAt, Long maxFactor, BigInteger adnlAddr, Byte[] signature) {
    Map<String, Object> params = Map.of("queryId", queryId, 
        "validatorKey", validatorKey, 
        "stakeAt", stakeAt, 
        "maxFactor", maxFactor, 
        "adnlAddr", adnlAddr, 
        "signature", signature);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "participateInElections", params, null);
  }

  public FunctionHandle<Void> ticktock() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "ticktock", params, null);
  }

  public FunctionHandle<Void> completeRoundWithChunk(BigInteger roundId, Integer chunkSize) {
    Map<String, Object> params = Map.of("roundId", roundId, 
        "chunkSize", chunkSize);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "completeRoundWithChunk", params, null);
  }

  public FunctionHandle<Void> completeRound(BigInteger roundId, Long participantQty) {
    Map<String, Object> params = Map.of("roundId", roundId, 
        "participantQty", participantQty);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "completeRound", params, null);
  }

  public FunctionHandle<Void> onStakeAccept(BigInteger queryId, Long comment, Address elector) {
    Map<String, Object> params = Map.of("queryId", queryId, 
        "comment", comment, 
        "elector", elector);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "onStakeAccept", params, null);
  }

  public FunctionHandle<Void> onStakeReject(BigInteger queryId, Long comment, Address elector) {
    Map<String, Object> params = Map.of("queryId", queryId, 
        "comment", comment, 
        "elector", elector);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "onStakeReject", params, null);
  }

  public FunctionHandle<Void> onSuccessToRecoverStake(BigInteger queryId, Address elector) {
    Map<String, Object> params = Map.of("queryId", queryId, 
        "elector", elector);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "onSuccessToRecoverStake", params, null);
  }

  public FunctionHandle<Void> onFailToRecoverStake(BigInteger queryId, Address elector) {
    Map<String, Object> params = Map.of("queryId", queryId, 
        "elector", elector);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "onFailToRecoverStake", params, null);
  }

  public FunctionHandle<Void> terminator() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "terminator", params, null);
  }

  public FunctionHandle<Void> setValidatorRewardFraction(Integer fraction) {
    Map<String, Object> params = Map.of("fraction", fraction);
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "setValidatorRewardFraction", params, null);
  }

  public FunctionHandle<Void> receiveFunds() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "receiveFunds", params, null);
  }

  public FunctionHandle<Void> getLastRoundInfo() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<Void>(Void.class, contextId(), address(), abi(), credentials(), "getLastRoundInfo", params, null);
  }

/*
  public FunctionHandle<ResultOfGetParticipantInfo> getParticipantInfo(Address addr) {
    Map<String, Object> params = Map.of("addr", addr);
    return new FunctionHandle<ResultOfGetParticipantInfo>(ResultOfGetParticipantInfo.class, contextId(), address(), abi(), credentials(), "getParticipantInfo", params, null);
  }
*/

  public FunctionHandle<ResultOfGetDePoolInfo> getDePoolInfo() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetDePoolInfo>(ResultOfGetDePoolInfo.class, contextId(), address(), abi(), credentials(), "getDePoolInfo", params, null);
  }

/*
  public FunctionHandle<ResultOfGetParticipants> getParticipants() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetParticipants>(ResultOfGetParticipants.class, contextId(), address(), abi(), credentials(), "getParticipants", params, null);
  }
*/

  public FunctionHandle<ResultOfGetDePoolBalance> getDePoolBalance() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetDePoolBalance>(ResultOfGetDePoolBalance.class, contextId(), address(), abi(), credentials(), "getDePoolBalance", params, null);
  }

  public FunctionHandle<ResultOfGetRounds> getRounds() {
    Map<String, Object> params = Map.of();
    return new FunctionHandle<ResultOfGetRounds>(ResultOfGetRounds.class, contextId(), address(), abi(), credentials(), "getRounds", params, null);
  }

/*
  public record ResultOfGetParticipantInfo(BigInteger total, BigInteger withdrawValue,
      Boolean reinvest, BigInteger reward, Map<BigInteger, BigInteger> stakes,
      Map<BigInteger, Map<String, Object>> vestings, Map<BigInteger, Map<String, Object>> locks) {
  }
*/

  public record ResultOfGetDePoolInfo(Boolean poolClosed, BigInteger minStake,
      BigInteger validatorAssurance, Integer participantRewardFraction,
      Integer validatorRewardFraction, BigInteger balanceThreshold, Address validatorWallet,
      Address[] proxies, BigInteger stakeFee, BigInteger retOrReinvFee, BigInteger proxyFee) {
  }

/*
  public record ResultOfGetParticipants(Address[] participants) {
  }
*/

  public record ResultOfGetDePoolBalance(BigInteger value0) {
  }

  public record ResultOfGetRounds(Map<BigInteger, Map<String, Object>> rounds) {
  }
}
