const hre = require("hardhat");

async function main() {
  const [deployer] = await hre.ethers.getSigners();
  console.log("Deploying contracts with account:", deployer.address);

  // Deploy TradeRegistry
  const TradeRegistry = await hre.ethers.getContractFactory("TradeRegistry");
  const tradeRegistry = await TradeRegistry.deploy();
  await tradeRegistry.waitForDeployment();
  console.log("TradeRegistry deployed to:", await tradeRegistry.getAddress());

  // Deploy StockToken for a sample stock (e.g., AAPL)
  const StockToken = await hre.ethers.getContractFactory("StockToken");
  const stockToken = await StockToken.deploy("Apple Inc", "AAPL");
  await stockToken.waitForDeployment();
  console.log("StockToken (AAPL) deployed to:", await stockToken.getAddress());

  console.log("\nDeployment completed!");
  console.log("TradeRegistry:", await tradeRegistry.getAddress());
  console.log("StockToken (AAPL):", await stockToken.getAddress());
}

main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });

