## Blockchain Module

The **Blockchain Module** contains the smart contracts and tooling used to record trades and settlements on a private Ethereum network for the Stock Brokerage Platform.

### Responsibilities

- Define Solidity smart contracts that represent trades, ownership, and settlements.
- Provide deployment scripts and configuration for the private Ethereum network.
- Supply ABIs and addresses used by the backend `transaction-service` for on-chain interaction.

### Typical Structure

Although the exact layout may vary, this module usually contains:

- `contracts/` – Solidity contracts (e.g. `TradeLedger.sol`, `Settlement.sol`).
- `scripts/` – Deployment and maintenance scripts.
- `test/` – Automated tests for smart contract behavior.
- `hardhat.config.js` or equivalent – Network and compiler configuration.

### Tooling

Common tools used here include:

- **Hardhat** (or Truffle) for:
  - Compiling contracts.
  - Running contract tests.
  - Deploying contracts to local or remote Ethereum networks.
- **Node.js** for scripts and configuration.

### Key Commands

From the `blockchain/` directory (adjust to your actual tooling if different):

```bash
# Install dependencies
npm install

# Compile contracts
npx hardhat compile

# Run contract tests
npx hardhat test

# Deploy to local network
npx hardhat deploy --network localhost
```

### Configuration

Network and deployment configuration typically includes:

- RPC URLs for local and remote networks.
- Deployer account/private key (never commit real secrets to source control).
- Gas settings and deployment parameters.

Contract addresses produced by deployments should be:

- Stored in a way accessible to the `transaction-service` (e.g. JSON artifacts, environment variables, or configuration files).

### Integration with Backend

The `transaction-service` uses:

- Contract ABIs and addresses to encode and send transactions.
- The Ethereum node RPC endpoint to submit and monitor transactions.

See the `transaction-service` README and configuration for details on how it connects to this module.

