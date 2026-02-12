// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract TradeRegistry {
    struct Trade {
        address buyer;
        address seller;
        string stockSymbol;
        uint256 quantity;
        uint256 price;
        uint256 timestamp;
    }
    
    Trade[] public trades;
    mapping(address => uint256[]) public userTrades;
    
    event TradeRecorded(
        uint256 indexed tradeId,
        address buyer,
        address seller,
        string stockSymbol,
        uint256 quantity,
        uint256 price
    );
    
    function recordTrade(
        address _buyer,
        address _seller,
        string memory _stockSymbol,
        uint256 _quantity,
        uint256 _price
    ) external returns (uint256) {
        Trade memory newTrade = Trade({
            buyer: _buyer,
            seller: _seller,
            stockSymbol: _stockSymbol,
            quantity: _quantity,
            price: _price,
            timestamp: block.timestamp
        });
        
        trades.push(newTrade);
        uint256 tradeId = trades.length - 1;
        
        userTrades[_buyer].push(tradeId);
        userTrades[_seller].push(tradeId);
        
        emit TradeRecorded(tradeId, _buyer, _seller, _stockSymbol, _quantity, _price);
        return tradeId;
    }
    
    function getUserTrades(address user) external view returns (uint256[] memory) {
        return userTrades[user];
    }
    
    function getTrade(uint256 tradeId) external view returns (Trade memory) {
        require(tradeId < trades.length, "Invalid trade ID");
        return trades[tradeId];
    }
    
    function getTotalTrades() external view returns (uint256) {
        return trades.length;
    }
}

