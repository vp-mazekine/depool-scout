package network.everscale;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.math.BigInteger;
import java.util.Map;
import tech.deplant.java4ever.framework.ContractAbi;
import tech.deplant.java4ever.framework.Credentials;
import tech.deplant.java4ever.framework.DeployHandle;
import tech.deplant.java4ever.framework.Tvc;
import tech.deplant.java4ever.framework.datatype.Address;
import tech.deplant.java4ever.framework.datatype.TvmCell;
import tech.deplant.java4ever.framework.template.Template;

/**
 * Java template class for deploy of <strong>DePoolV1_5</strong> contract for Everscale blockchain.
 */
public record DePoolV1_5Template(ContractAbi abi, Tvc tvc) implements Template {
  public DePoolV1_5Template(Tvc tvc) throws JsonProcessingException {
    this(DEFAULT_ABI(), tvc);
  }

  public DePoolV1_5Template() throws JsonProcessingException {
    this(DEFAULT_ABI(),DEFAULT_TVC());
  }

  public static ContractAbi DEFAULT_ABI() throws JsonProcessingException {
    return ContractAbi.ofString("{\"header\":[\"time\",\"expire\"],\"functions\":[{\"name\":\"constructor\",\"inputs\":[{\"name\":\"minStake\",\"type\":\"uint64\"},{\"name\":\"validatorAssurance\",\"type\":\"uint64\"},{\"name\":\"proxyCode\",\"type\":\"cell\"},{\"name\":\"validatorWallet\",\"type\":\"address\"},{\"name\":\"participantRewardFraction\",\"type\":\"uint8\"}],\"outputs\":[]},{\"name\":\"addOrdinaryStake\",\"inputs\":[{\"name\":\"stake\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"withdrawFromPoolingRound\",\"inputs\":[{\"name\":\"withdrawValue\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"addVestingStake\",\"inputs\":[{\"name\":\"stake\",\"type\":\"uint64\"},{\"name\":\"beneficiary\",\"type\":\"address\"},{\"name\":\"withdrawalPeriod\",\"type\":\"uint32\"},{\"name\":\"totalPeriod\",\"type\":\"uint32\"}],\"outputs\":[]},{\"name\":\"addLockStake\",\"inputs\":[{\"name\":\"stake\",\"type\":\"uint64\"},{\"name\":\"beneficiary\",\"type\":\"address\"},{\"name\":\"withdrawalPeriod\",\"type\":\"uint32\"},{\"name\":\"totalPeriod\",\"type\":\"uint32\"}],\"outputs\":[]},{\"name\":\"withdrawPart\",\"inputs\":[{\"name\":\"withdrawValue\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"withdrawAll\",\"inputs\":[],\"outputs\":[]},{\"name\":\"cancelWithdrawal\",\"inputs\":[],\"outputs\":[]},{\"name\":\"transferStake\",\"inputs\":[{\"name\":\"dest\",\"type\":\"address\"},{\"name\":\"amount\",\"type\":\"uint64\"}],\"outputs\":[]},{\"name\":\"participateInElections\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"},{\"name\":\"validatorKey\",\"type\":\"uint256\"},{\"name\":\"stakeAt\",\"type\":\"uint32\"},{\"name\":\"maxFactor\",\"type\":\"uint32\"},{\"name\":\"adnlAddr\",\"type\":\"uint256\"},{\"name\":\"signature\",\"type\":\"bytes\"}],\"outputs\":[],\"id\":\"0x4E73744B\"},{\"name\":\"ticktock\",\"inputs\":[],\"outputs\":[]},{\"name\":\"completeRoundWithChunk\",\"inputs\":[{\"name\":\"roundId\",\"type\":\"uint64\"},{\"name\":\"chunkSize\",\"type\":\"uint8\"}],\"outputs\":[]},{\"name\":\"completeRound\",\"inputs\":[{\"name\":\"roundId\",\"type\":\"uint64\"},{\"name\":\"participantQty\",\"type\":\"uint32\"}],\"outputs\":[]},{\"name\":\"onStakeAccept\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"},{\"name\":\"comment\",\"type\":\"uint32\"},{\"name\":\"elector\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"onStakeReject\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"},{\"name\":\"comment\",\"type\":\"uint32\"},{\"name\":\"elector\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"onSuccessToRecoverStake\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"},{\"name\":\"elector\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"onFailToRecoverStake\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"},{\"name\":\"elector\",\"type\":\"address\"}],\"outputs\":[]},{\"name\":\"terminator\",\"inputs\":[],\"outputs\":[]},{\"name\":\"setValidatorRewardFraction\",\"inputs\":[{\"name\":\"fraction\",\"type\":\"uint8\"}],\"outputs\":[]},{\"name\":\"receiveFunds\",\"inputs\":[],\"outputs\":[]},{\"name\":\"getLastRoundInfo\",\"inputs\":[],\"outputs\":[]},{\"name\":\"getParticipantInfo\",\"inputs\":[{\"name\":\"addr\",\"type\":\"address\"}],\"outputs\":[{\"name\":\"total\",\"type\":\"uint64\"},{\"name\":\"withdrawValue\",\"type\":\"uint64\"},{\"name\":\"reinvest\",\"type\":\"bool\"},{\"name\":\"reward\",\"type\":\"uint64\"},{\"name\":\"stakes\",\"type\":\"map(uint64,uint64)\"},{\"name\":\"vestings\",\"type\":\"map(uint64,tuple)\",\"components\":[{\"name\":\"remainingAmount\",\"type\":\"uint64\"},{\"name\":\"lastWithdrawalTime\",\"type\":\"uint64\"},{\"name\":\"withdrawalPeriod\",\"type\":\"uint32\"},{\"name\":\"withdrawalValue\",\"type\":\"uint64\"},{\"name\":\"owner\",\"type\":\"address\"}]},{\"name\":\"locks\",\"type\":\"map(uint64,tuple)\",\"components\":[{\"name\":\"remainingAmount\",\"type\":\"uint64\"},{\"name\":\"lastWithdrawalTime\",\"type\":\"uint64\"},{\"name\":\"withdrawalPeriod\",\"type\":\"uint32\"},{\"name\":\"withdrawalValue\",\"type\":\"uint64\"},{\"name\":\"owner\",\"type\":\"address\"}]}]},{\"name\":\"getDePoolInfo\",\"inputs\":[],\"outputs\":[{\"name\":\"poolClosed\",\"type\":\"bool\"},{\"name\":\"minStake\",\"type\":\"uint64\"},{\"name\":\"validatorAssurance\",\"type\":\"uint64\"},{\"name\":\"participantRewardFraction\",\"type\":\"uint8\"},{\"name\":\"validatorRewardFraction\",\"type\":\"uint8\"},{\"name\":\"validatorWallet\",\"type\":\"address\"},{\"name\":\"proxies\",\"type\":\"address[]\"},{\"name\":\"stakeFee\",\"type\":\"uint64\"},{\"name\":\"retOrReinvFee\",\"type\":\"uint64\"},{\"name\":\"proxyFee\",\"type\":\"uint64\"}]},{\"name\":\"getParticipants\",\"inputs\":[],\"outputs\":[{\"name\":\"participants\",\"type\":\"address[]\"}]},{\"name\":\"getRounds\",\"inputs\":[],\"outputs\":[{\"name\":\"rounds\",\"type\":\"map(uint64,tuple)\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"supposedElectedAt\",\"type\":\"uint32\"},{\"name\":\"unfreeze\",\"type\":\"uint32\"},{\"name\":\"stakeHeldFor\",\"type\":\"uint32\"},{\"name\":\"vsetHashInElectionPhase\",\"type\":\"uint256\"},{\"name\":\"step\",\"type\":\"uint8\"},{\"name\":\"completionReason\",\"type\":\"uint8\"},{\"name\":\"stake\",\"type\":\"uint64\"},{\"name\":\"recoveredStake\",\"type\":\"uint64\"},{\"name\":\"unused\",\"type\":\"uint64\"},{\"name\":\"isValidatorStakeCompleted\",\"type\":\"bool\"},{\"name\":\"rewards\",\"type\":\"uint64\"},{\"name\":\"participantQty\",\"type\":\"uint32\"},{\"name\":\"validatorStake\",\"type\":\"uint64\"},{\"name\":\"validatorRemainingStake\",\"type\":\"uint64\"},{\"name\":\"handledStakesAndRewards\",\"type\":\"uint64\"}]}]}],\"events\":[{\"name\":\"DePoolClosed\",\"inputs\":[]},{\"name\":\"RoundStakeIsAccepted\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"},{\"name\":\"comment\",\"type\":\"uint32\"}]},{\"name\":\"RoundStakeIsRejected\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"},{\"name\":\"comment\",\"type\":\"uint32\"}]},{\"name\":\"ProxyHasRejectedTheStake\",\"inputs\":[{\"name\":\"queryId\",\"type\":\"uint64\"}]},{\"name\":\"ProxyHasRejectedRecoverRequest\",\"inputs\":[{\"name\":\"roundId\",\"type\":\"uint64\"}]},{\"name\":\"RoundCompleted\",\"inputs\":[{\"name\":\"round\",\"type\":\"tuple\",\"components\":[{\"name\":\"id\",\"type\":\"uint64\"},{\"name\":\"supposedElectedAt\",\"type\":\"uint32\"},{\"name\":\"unfreeze\",\"type\":\"uint32\"},{\"name\":\"stakeHeldFor\",\"type\":\"uint32\"},{\"name\":\"vsetHashInElectionPhase\",\"type\":\"uint256\"},{\"name\":\"step\",\"type\":\"uint8\"},{\"name\":\"completionReason\",\"type\":\"uint8\"},{\"name\":\"stake\",\"type\":\"uint64\"},{\"name\":\"recoveredStake\",\"type\":\"uint64\"},{\"name\":\"unused\",\"type\":\"uint64\"},{\"name\":\"isValidatorStakeCompleted\",\"type\":\"bool\"},{\"name\":\"rewards\",\"type\":\"uint64\"},{\"name\":\"participantQty\",\"type\":\"uint32\"},{\"name\":\"validatorStake\",\"type\":\"uint64\"},{\"name\":\"validatorRemainingStake\",\"type\":\"uint64\"},{\"name\":\"handledStakesAndRewards\",\"type\":\"uint64\"}]}]},{\"name\":\"StakeSigningRequested\",\"inputs\":[{\"name\":\"electionId\",\"type\":\"uint32\"},{\"name\":\"proxy\",\"type\":\"address\"}]},{\"name\":\"TooLowDePoolBalance\",\"inputs\":[{\"name\":\"replenishment\",\"type\":\"uint256\"}]},{\"name\":\"RewardFractionsChanged\",\"inputs\":[{\"name\":\"validator\",\"type\":\"uint8\"},{\"name\":\"participants\",\"type\":\"uint8\"}]}],\"data\":[],\"ABI version\":2}");
  }

  public static Tvc DEFAULT_TVC() {
    return Tvc.ofBase64String("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");
  }

  public DeployHandle<DePoolV1_5> prepareDeploy(int contextId, int workchainId,
      Credentials credentials, BigInteger minStake, BigInteger validatorAssurance,
      TvmCell proxyCode, Address validatorWallet, Integer participantRewardFraction) {
    Map<String, Object> initialDataFields = Map.of();
    Map<String, Object> params = Map.of("minStake", minStake, 
        "validatorAssurance", validatorAssurance, 
        "proxyCode", proxyCode, 
        "validatorWallet", validatorWallet, 
        "participantRewardFraction", participantRewardFraction);
    return new DeployHandle<DePoolV1_5>(DePoolV1_5.class, contextId, abi(), tvc(), workchainId, credentials, initialDataFields, params, null);
  }
}
