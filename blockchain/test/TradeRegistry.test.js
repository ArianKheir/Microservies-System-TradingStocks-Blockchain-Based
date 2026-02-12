const { expect } = require("chai");
const { ethers } = require("hardhat");

describe("TradeRegistry", function () {
  let tradeRegistry;
  let owner;
  let buyer;
  let seller;

  beforeEach(async function () {
    [owner, buyer, seller] = await ethers.getSigners();

    const TradeRegistry = await ethers.getContractFactory("TradeRegistry");
    tradeRegistry = await TradeRegistry.deploy();
    await tradeRegistry.waitForDeployment();
  });

  it("Should record a trade", async function () {
    const tx = await tradeRegistry.recordTrade(
      buyer.address,
      seller.address,
      "AAPL",
      100,
      ethers.parseEther("150")
    );

    await expect(tx)
      .to.emit(tradeRegistry, "TradeRecorded")
      .withArgs(0, buyer.address, seller.address, "AAPL", 100, ethers.parseEther("150"));

    const trade = await tradeRegistry.getTrade(0);
    expect(trade.buyer).to.equal(buyer.address);
    expect(trade.seller).to.equal(seller.address);
    expect(trade.stockSymbol).to.equal("AAPL");
    expect(trade.quantity).to.equal(100);
  });

  it("Should return user trades", async function () {
    await tradeRegistry.recordTrade(
      buyer.address,
      seller.address,
      "AAPL",
      100,
      ethers.parseEther("150")
    );

    const userTrades = await tradeRegistry.getUserTrades(buyer.address);
    expect(userTrades.length).to.equal(1);
    expect(userTrades[0]).to.equal(0);
  });
});

